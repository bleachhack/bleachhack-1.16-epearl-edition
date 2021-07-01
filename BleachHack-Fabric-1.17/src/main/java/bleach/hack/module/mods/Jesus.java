package bleach.hack.module.mods;

import org.lwjgl.glfw.GLFW;

import bleach.hack.eventbus.BleachSubscribe;
import bleach.hack.event.events.EventBlockShape;
import bleach.hack.event.events.EventTick;
import bleach.hack.module.ModuleCategory;
import bleach.hack.setting.base.SettingMode;
import bleach.hack.module.Module;
import bleach.hack.util.world.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShapes;

public class Jesus extends Module {

	public Jesus() {
		super("Jesus", GLFW.GLFW_KEY_J, ModuleCategory.PLAYER, "Allows you to walk on water",
				new SettingMode("Mode", "Dolphin", "Solid"));
	}

	@BleachSubscribe
	public void onTick(EventTick event) {
		Entity e = mc.player.getRootVehicle();

		if (e.isSneaking() || e.fallDistance > 3f)
			return;

		if (WorldUtils.isFluid(new BlockPos(e.getPos().add(0, 0.3, 0)))) {
			e.setVelocity(e.getVelocity().x, 0.08, e.getVelocity().z);
		} else if (WorldUtils.isFluid(new BlockPos(e.getPos().add(0, 0.1, 0)))) {
			e.setVelocity(e.getVelocity().x, 0.05, e.getVelocity().z);
		} else if (WorldUtils.isFluid(new BlockPos(e.getPos().add(0, 0.05, 0)))) {
			e.setVelocity(e.getVelocity().x, 0.01, e.getVelocity().z);
		} else if (WorldUtils.isFluid(e.getBlockPos())) {
			e.setVelocity(e.getVelocity().x, -0.005, e.getVelocity().z);
			e.setOnGround(true);
		}
	}

	@BleachSubscribe
	public void onBlockShape(EventBlockShape event) {
		if (getSetting(0).asMode().mode == 1
				&& WorldUtils.isFluid(event.getPos())
				&& !mc.player.isSneaking()
				&& !mc.player.isTouchingWater()
				&& mc.player.getY() >= event.getPos().getY() + 0.9) {
			event.setShape(VoxelShapes.cuboid(0, 0, 0, 1, 0.9, 1));
		}
	}
}