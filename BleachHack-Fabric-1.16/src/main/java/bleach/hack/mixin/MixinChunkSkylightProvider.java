package bleach.hack.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import bleach.hack.module.ModuleManager;
import bleach.hack.module.mods.NoRender;
import net.minecraft.world.chunk.light.ChunkSkyLightProvider;

@Mixin(ChunkSkyLightProvider.class)
public class MixinChunkSkylightProvider {

	@Inject(method = "recalculateLevel", at = @At("HEAD"), cancellable = true)
	protected void recalculateLevel(long id, long excludedId, int maxLevel, CallbackInfoReturnable<Integer> ci) {
		if (((NoRender) ModuleManager.getModule("NoRender")).shouldRemoveWorld(4)) {
			ci.setReturnValue(15);
		}
	}
}