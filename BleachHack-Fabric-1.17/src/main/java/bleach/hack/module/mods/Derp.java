package bleach.hack.module.mods;

import java.util.Random;

import bleach.hack.eventbus.BleachSubscribe;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import bleach.hack.module.ModuleCategory;
import bleach.hack.module.Module;
import bleach.hack.event.events.EventTick;

/**
 * @author <a href="https://github.com/HerraVp">Vp</a>
 */

public class Derp extends Module {
    public Derp(){ super("Derp", KEY_UNBOUND, ModuleCategory.FUN, "Randomly moves ur head around server side");
    }

    private final Random random = new Random();

    @BleachSubscribe
    public void onTick(EventTick event)
    {
        float yaw = mc.player.getYaw() + random.nextFloat() * 360F - 180F;
        float pitch = random.nextFloat() * 180F - 90F;

        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(
                yaw, pitch, mc.player.isOnGround()));
    }

    @Override
    public void onDisable(){

        super.onDisable();
    }
}