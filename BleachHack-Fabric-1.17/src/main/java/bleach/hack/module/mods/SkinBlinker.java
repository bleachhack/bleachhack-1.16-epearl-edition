package bleach.hack.module.mods;

import bleach.hack.eventbus.BleachSubscribe;
import bleach.hack.module.ModuleCategory;
import bleach.hack.module.Module;
import bleach.hack.event.events.EventTick;
import java.util.Random;

import net.minecraft.client.render.entity.PlayerModelPart;

/**
 * @author <a href="https://github.com/HerraVp">Vp</a>
 */

public class SkinBlinker extends Module {
    public SkinBlinker() {
        super("SkinBlinker", KEY_UNBOUND, ModuleCategory.FUN, "Makes ur skins body parts blink");
    }

    private final Random random = new Random();

    @BleachSubscribe
    public void onTick(EventTick event)
    { if(random.nextInt(4) != 0)
        return;

        for(PlayerModelPart part : PlayerModelPart.values())
            mc.options.togglePlayerModelPart(part,
                    !mc.options.isPlayerModelPartEnabled(part));
    }

    @Override
    public void onDisable() {
    for(PlayerModelPart part : PlayerModelPart.values())
            mc.options.togglePlayerModelPart(part, true);
    super.onDisable();
    }
}


