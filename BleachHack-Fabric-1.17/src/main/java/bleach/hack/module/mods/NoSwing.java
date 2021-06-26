package bleach.hack.module.mods;

import bleach.hack.eventbus.BleachSubscribe;
import bleach.hack.event.events.EventSendPacket;
import bleach.hack.event.events.EventSwing;
import bleach.hack.module.ModuleCategory;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingToggle;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;

public class NoSwing extends Module {

    public NoSwing() {
        super("NoSwing", KEY_UNBOUND, ModuleCategory.MISC, "Makes you not swing your hand",
                new SettingToggle("Client", true).withDesc("Makes you not swing your hand clientside"),
                new SettingToggle("Server", true).withDesc("Makes you not send hand swing packets"));
    }

    @BleachSubscribe
    public void onSwingHand(EventSwing event) {
        if (getSetting(0).asToggle().state) {
            event.setCancelled(true);
        }
    }

    @BleachSubscribe
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof HandSwingC2SPacket && getSetting(1).asToggle().state) {
            event.setCancelled(true);
        }
    }
}