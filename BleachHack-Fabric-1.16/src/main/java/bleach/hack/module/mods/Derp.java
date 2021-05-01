package bleach.hack.module.mods;

import java.util.Random;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import bleach.hack.module.Category;
import bleach.hack.module.Module;

public class Derp extends Module {
    public Derp(){ super("Derp", KEY_UNBOUND, Category.FUN, "Randomly moves ur head around (Only other players can see it)");
    }

    private final Random random = new Random();

    @Override
    public void onEnable()
    {
        float yaw = mc.player.yaw + random.nextFloat() * 360F - 180F;
        float pitch = random.nextFloat() * 180F - 90F;

        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookOnly(
                yaw, pitch, mc.player.isOnGround()));
    }
}