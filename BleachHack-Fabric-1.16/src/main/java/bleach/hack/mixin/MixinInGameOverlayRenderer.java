package bleach.hack.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import bleach.hack.module.ModuleManager;
import bleach.hack.module.mods.NoRender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(InGameOverlayRenderer.class)
public class MixinInGameOverlayRenderer {

	@Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
	private static void onRenderFireOverlay(MinecraftClient minecraftClient, MatrixStack matrixStack, CallbackInfo ci) {
		if (((NoRender) ModuleManager.getModule("NoRender")).shouldRemoveOverlay(1)) {
			ci.cancel();
		}
	}

	@Inject(method = "renderUnderwaterOverlay", at = @At("HEAD"), cancellable = true)
	private static void onRenderUnderwaterOverlay(MinecraftClient minecraftClient, MatrixStack matrixStack, CallbackInfo ci) {
		if (((NoRender) ModuleManager.getModule("NoRender")).shouldRemoveOverlay(3)) {
			ci.cancel();
		}
	}
}