package bleach.hack.module.mods;

import bleach.hack.event.events.EventTick;
import bleach.hack.eventbus.BleachSubscribe;
import bleach.hack.module.Module;
import bleach.hack.module.ModuleCategory;
import bleach.hack.setting.base.SettingSlider;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class FastLadder extends Module {
    public FastLadder() {
        super("FastLadder", KEY_UNBOUND, ModuleCategory.MOVEMENT, "Makes you climb ladders faster",
                new SettingSlider("Speed", 0, 1, 0.5, 2));
    }

    @BleachSubscribe
    public void onTick(EventTick event)
    {
        ClientPlayerEntity player = mc.player;

        if(!player.isClimbing() || !player.horizontalCollision)
            return;

        if(player.input.movementForward == 0
                && player.input.movementSideways == 0)
            return;

        Vec3d velocity = player.getVelocity();
        player.setVelocity(velocity.x, getSetting(0).asSlider().getValue(), velocity.z);
    }
}
