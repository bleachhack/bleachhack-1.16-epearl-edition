package bleach.hack.module.mods;

import bleach.hack.event.events.EventSendPacket;
import bleach.hack.eventbus.BleachSubscribe;
import bleach.hack.mixin.CloseHandledScreenC2SPacketAccessor;
import bleach.hack.module.ModuleCategory;
import bleach.hack.module.Module;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;

/**
 * @author <a href="https://github.com/HerraVp">Vp</a>
 */

public class XCarry extends Module {
    private boolean invOpened;

    public XCarry() {
        super("XCarry", KEY_UNBOUND, ModuleCategory.PLAYER, "Allows you to store extra items in your crafting slots");
    }

    @BleachSubscribe
    public void onSendPacket(EventSendPacket event){
        if (!(event.getPacket() instanceof CloseHandledScreenC2SPacket))
            return;
        if (((CloseHandledScreenC2SPacketAccessor) event.getPacket()).getSyncId() == mc.player.playerScreenHandler.syncId) {
            invOpened = true;
            event.setCancelled(true);
        }
    }
}





