/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDrinker420/bleachhack-1.14/).
 * Copyright (c) 2019 Bleach.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bleach.hack.gui.window;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import bleach.hack.gui.window.widget.WindowWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class Window {

	public int x1;
	public int y1;
	public int x2;
	public int y2;

	public String title;
	public ItemStack icon;

	public boolean closed;
	public boolean selected = false;

	private List<WindowWidget> widgets = new ArrayList<>();

	protected boolean dragging = false;
	protected int dragOffX;
	protected int dragOffY;

	public Window(int x1, int y1, int x2, int y2, String title, ItemStack icon) {
		this(x1, y1, x2, y2, title, icon, false);
	}

	public Window(int x1, int y1, int x2, int y2, String title, ItemStack icon, boolean closed) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.title = title;
		this.icon = icon;
		this.closed = closed;
	}

	public List<WindowWidget> getWidgets() {
		return widgets;
	}

	public <T extends WindowWidget> T addWidget(T widget) {
		widgets.add(widget);
		return widget;
	}

	public void render(MatrixStack matrix, int mouseX, int mouseY) {
		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;

		if (dragging) {
			x2 = (x2 - x1) + mouseX - dragOffX - Math.min(0, mouseX - dragOffX);
			y2 = (y2 - y1) + mouseY - dragOffY - Math.min(0, mouseY - dragOffY);
			x1 = Math.max(0, mouseX - dragOffX);
			y1 = Math.max(0, mouseY - dragOffY);
		}

		drawBar(matrix, mouseX, mouseY, textRend);

		for (WindowWidget w : widgets) {
			w.render(matrix, x1, y1, mouseX, mouseY);
		}

		boolean blockItem = icon != null && icon.getItem() instanceof BlockItem;

		/* window icon */
		if (icon != null) {
			RenderSystem.pushMatrix();
			RenderSystem.scalef(0.6f, 0.6f, 1f);

			DiffuseLighting.enableGuiDepthLighting();
			MinecraftClient.getInstance().getItemRenderer().renderInGui(
					icon, (int) ((x1 + (blockItem ? 3 : 2)) * 1 / 0.6), (int) ((y1 + 2) * 1 / 0.6));
			DiffuseLighting.disableGuiDepthLighting();

			RenderSystem.popMatrix();
		}

		/* window title */
		textRend.drawWithShadow(matrix, title,
				x1 + (icon == null || icon.getItem() == Items.AIR ? 4 : (blockItem ? 15 : 14)), y1 + 3, -1);
	}

	protected void drawBar(MatrixStack matrix, int mouseX, int mouseY, TextRenderer textRend) {
		/* background */
		DrawableHelper.fill(matrix, x1, y1 + 1, x1 + 1, y2 - 1, 0xff6060b0);
		horizontalGradient(matrix, x1 + 1, y1, x2 - 1, y1 + 1, 0xff6060b0, 0xff8070b0);
		DrawableHelper.fill(matrix, x2 - 1, y1 + 1, x2, y2 - 1, 0xff8070b0);
		horizontalGradient(matrix, x1 + 1, y2 - 1, x2 - 1, y2, 0xff6060b0, 0xff8070b0);

		DrawableHelper.fill(matrix, x1 + 1, y1 + 12, x2 - 1, y2 - 1, 0x90606090);

		/* title bar */
		horizontalGradient(matrix, x1 + 1, y1 + 1, x2 - 1, y1 + 12, (selected ? 0xff6060b0 : 0xff606060), (selected ? 0xff8070b0 : 0xffa0a0a0));

		/* buttons */
		//fillGrey(matrix, x2 - 12, y1 + 3, x2 - 4, y1 + 11);
		textRend.draw(matrix, "x", x2 - 10, y1 + 3, 0);
		textRend.draw(matrix, "x", x2 - 11, y1 + 2, -1);

		//fillGrey(matrix, x2 - 22, y1 + 3, x2 - 14, y1 + 11);
		textRend.draw(matrix, "_", x2 - 21, y1 + 2, 0);
		textRend.draw(matrix, "_", x2 - 22, y1 + 1, -1);
	}

	public boolean shouldClose(int mouseX, int mouseY) {
		return selected && mouseX > x2 - 23 && mouseX < x2 && mouseY > y1 + 2 && mouseY < y1 + 12;
	}

	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (mouseX >= x1 && mouseX <= x2 - 2 && mouseY >= y1 && mouseY <= y1 + 11) {
			dragging = true;
			dragOffX = (int) mouseX - x1;
			dragOffY = (int) mouseY - y1;
		}

		if (selected) {
			for (WindowWidget w : widgets) {
				w.mouseClicked(x1, y1, (int) mouseX, (int) mouseY, button);
			}
		}
	}

	public void mouseReleased(double mouseX, double mouseY, int button) {
		dragging = false;

		if (selected) {
			for (WindowWidget w : widgets) {
				w.mouseReleased(x1, y1, (int) mouseX, (int) mouseY, button);
			}
		}
	}

	public void tick() {
		for (WindowWidget w : widgets) {
			w.tick();
		}
	}

	public void charTyped(char chr, int modifiers) {
		if (selected) {
			for (WindowWidget w : widgets) {
				w.charTyped(chr, modifiers);
			}
		}
	}

	public void keyPressed(int keyCode, int scanCode, int modifiers) {
		if (selected) {
			for (WindowWidget w : widgets) {
				w.keyPressed(keyCode, scanCode, modifiers);
			}
		}
	}

	public static void fill(MatrixStack matrix, int x1, int y1, int x2, int y2) {
		fill(matrix, x1, y1, x2, y2, 0xff6060b0, 0xff8070b0, 0x00000000);
	}

	public static void fill(MatrixStack matrix, int x1, int y1, int x2, int y2, int fill) {
		fill(matrix, x1, y1, x2, y2, 0xff6060b0, 0xff8070b0, fill);
	}

	public static void fill(MatrixStack matrix, int x1, int y1, int x2, int y2, int colTop, int colBot, int colFill) {
		DrawableHelper.fill(matrix, x1, y1 + 1, x1 + 1, y2 - 1, colTop);
		DrawableHelper.fill(matrix, x1 + 1, y1, x2 - 1, y1 + 1, colTop);
		DrawableHelper.fill(matrix, x2 - 1, y1 + 1, x2, y2 - 1, colBot);
		DrawableHelper.fill(matrix, x1 + 1, y2 - 1, x2 - 1, y2, colBot);
		DrawableHelper.fill(matrix, x1 + 1, y1 + 1, x2 - 1, y2 - 1, colFill);
	}

	public static void horizontalGradient(MatrixStack matrix, int x1, int y1, int x2, int y2, int color1, int color2) {
		float alpha1 = (color1 >> 24 & 255) / 255.0F;
		float red1   = (color1 >> 16 & 255) / 255.0F;
		float green1 = (color1 >> 8 & 255) / 255.0F;
		float blue1  = (color1 & 255) / 255.0F;
		float alpha2 = (color2 >> 24 & 255) / 255.0F;
		float red2   = (color2 >> 16 & 255) / 255.0F;
		float green2 = (color2 >> 8 & 255) / 255.0F;
		float blue2  = (color2 & 255) / 255.0F;
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.disableAlphaTest();
		RenderSystem.defaultBlendFunc();
		RenderSystem.shadeModel(GL11.GL_SMOOTH);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(7, VertexFormats.POSITION_COLOR);
		bufferBuilder.vertex(x1, y1, 0).color(red1, green1, blue1, alpha1).next();
		bufferBuilder.vertex(x1, y2, 0).color(red1, green1, blue1, alpha1).next();
		bufferBuilder.vertex(x2, y2, 0).color(red2, green2, blue2, alpha2).next();
		bufferBuilder.vertex(x2, y1, 0).color(red2, green2, blue2, alpha2).next();
		tessellator.draw();
		RenderSystem.shadeModel(GL11.GL_FLAT);
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.enableTexture();
	}

	public static void verticalGradient(MatrixStack matrix, int x1, int y1, int x2, int y2, int color1, int color2) {
		float alpha1 = (color1 >> 24 & 255) / 255.0F;
		float red1   = (color1 >> 16 & 255) / 255.0F;
		float green1 = (color1 >> 8 & 255) / 255.0F;
		float blue1  = (color1 & 255) / 255.0F;
		float alpha2 = (color2 >> 24 & 255) / 255.0F;
		float red2   = (color2 >> 16 & 255) / 255.0F;
		float green2 = (color2 >> 8 & 255) / 255.0F;
		float blue2  = (color2 & 255) / 255.0F;
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.disableAlphaTest();
		RenderSystem.defaultBlendFunc();
		RenderSystem.shadeModel(GL11.GL_SMOOTH);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(7, VertexFormats.POSITION_COLOR);
		bufferBuilder.vertex(x2, y1, 0).color(red1, green1, blue1, alpha1).next();
		bufferBuilder.vertex(x1, y1, 0).color(red1, green1, blue1, alpha1).next();
		bufferBuilder.vertex(x1, y2, 0).color(red2, green2, blue2, alpha2).next();
		bufferBuilder.vertex(x2, y2, 0).color(red2, green2, blue2, alpha2).next();
		tessellator.draw();
		RenderSystem.shadeModel(GL11.GL_FLAT);
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.enableTexture();
	}
}