package bleach.hack.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import bleach.hack.module.ModuleManager;
import bleach.hack.module.mods.NoRender;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;

@Mixin(BackgroundRenderer.class)
public class MixinBackgroundRenderer {

	@Redirect(method = {
			"render(Lnet/minecraft/client/render/Camera;FLnet/minecraft/client/world/ClientWorld;IF)V",
			"applyFog(Lnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/BackgroundRenderer$FogType;FZ)V"},
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z"))
	private static boolean hasStatusEffect(LivingEntity entity, StatusEffect effect) {
		if (effect == StatusEffects.BLINDNESS && ((NoRender) ModuleManager.getModule("NoRender")).shouldRemoveOverlay(0)) {
			return false;
		}

		return entity.hasStatusEffect(effect);
	}
}