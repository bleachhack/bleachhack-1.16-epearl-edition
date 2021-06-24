package bleach.hack.module.mods;

import bleach.hack.event.events.EventSendPacket;
import bleach.hack.event.events.EventSwing;
import bleach.hack.eventbus.BleachSubscribe;
import bleach.hack.module.ModuleCategory;
import bleach.hack.module.Module;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;

public class NoSwing extends Module {
    public NoSwing() {
        super("NoSwing", KEY_UNBOUND, ModuleCategory.PLAYER, "Makes you not swing server side");
    }

    @BleachSubscribe
    public void onSwing(EventSwing event) {
        event.setCancelled(true);
    }

    @BleachSubscribe
    public void onPacket(EventSendPacket event) {
        if (event.getPacket() instanceof HandSwingC2SPacket)
            event.setCancelled(true);
    }
}