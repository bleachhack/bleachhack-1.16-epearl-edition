package bleach.hack.mixin;

import org.spongepowered.asm.mixin.Mixin;
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

	@Inject(method = "getSubmergedFluidState", at = @At("HEAD"), cancellable = true)
	private void getSubmergedFluidState(CallbackInfoReturnable<FluidState> cir) {
		if (((NoRender) ModuleManager.getModule("NoRender")).shouldRemoveOverlay(3)) {
			cir.setReturnValue(Fluids.EMPTY.getDefaultState());
		}
	}
}