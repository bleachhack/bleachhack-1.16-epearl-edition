package bleach.hack.module.mods;

import com.google.common.eventbus.Subscribe;

import bleach.hack.event.events.EventTick;
import bleach.hack.mixin.AccessorHeldItemRenderer;
import bleach.hack.module.ModuleCategory;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingSlider;

public class HandProgress extends Module {

	public HandProgress() {
		super("HandProgress", KEY_UNBOUND, ModuleCategory.RENDER, "Smaller view of mainhand/offhand",
				new SettingSlider("Mainhand", 0.1, 1.0, 1.0, 1).withDesc("Main hand size"), // 0
				new SettingSlider("Offhand", 0.1, 1.0, 1.0, 1).withDesc("Offhand size") // 1
				);
	}

	@Subscribe
	public void tick(EventTick event) {
		AccessorHeldItemRenderer accessor = (AccessorHeldItemRenderer) mc.gameRenderer.firstPersonRenderer;

		// Refresh the item held in hand every tick
		accessor.setItemStackMainHand(mc.player.getMainHandStack());
		accessor.setItemStackOffHand(mc.player.getOffHandStack());

		// Set the item render height
		accessor.setEquippedProgressMainHand((float) this.getSetting(0).asSlider().getValue());
		accessor.setEquippedProgressOffHand((float) this.getSetting(1).asSlider().getValue());
	}
}
