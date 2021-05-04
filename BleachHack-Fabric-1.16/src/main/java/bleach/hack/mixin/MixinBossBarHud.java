package bleach.hack.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import bleach.hack.module.ModuleManager;
import bleach.hack.module.mods.NoRender;
import net.minecraft.client.gui.hud.BossBarHud;

@Mixin(BossBarHud.class)
public class MixinBossBarHud {

	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void render(CallbackInfo info) {
		if (((NoRender) ModuleManager.getModule("NoRender")).shouldRemoveOverlay(6)) {
			info.cancel();
		}
	}

}