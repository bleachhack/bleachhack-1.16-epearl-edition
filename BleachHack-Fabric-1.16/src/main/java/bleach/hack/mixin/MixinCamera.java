package bleach.hack.mixin;

import bleach.hack.module.Module;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import bleach.hack.module.ModuleManager;
import bleach.hack.module.mods.NoRender;
import net.minecraft.client.render.Camera;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;

@Mixin(Camera.class)
public class MixinCamera {

	@Unique
	private boolean bypassCameraClip;

	@Shadow
	private double clipToSpace(double desiredCameraDistance) { return 0; }

	@Inject(method = "getSubmergedFluidState", at = @At("HEAD"), cancellable = true)
	private void getSubmergedFluidState(CallbackInfoReturnable<FluidState> cir) {
		if (((NoRender) ModuleManager.getModule("NoRender")).shouldRemoveOverlay(3)) {
			cir.setReturnValue(Fluids.EMPTY.getDefaultState());
		}
	}

	@Inject(method = "clipToSpace", at = @At("HEAD"), cancellable = true)
	private void onClipToSpace(double desiredCameraDistance, CallbackInfoReturnable<Double> info) {
		if (bypassCameraClip) {
			bypassCameraClip = false;
		} else {
			Module betterCamera = ModuleManager.getModule("BetterCamera");

			if (betterCamera.isEnabled()) {
				if (betterCamera.getSetting(0).asToggle().state) {
					info.setReturnValue(betterCamera.getSetting(1).asToggle().state
							? betterCamera.getSetting(1).asToggle().getChild(0).asSlider().getValue() : desiredCameraDistance);
				} else if (betterCamera.getSetting(1).asToggle().state) {
					bypassCameraClip = true;
					clipToSpace(desiredCameraDistance);
				}
			}
		}
	}
}