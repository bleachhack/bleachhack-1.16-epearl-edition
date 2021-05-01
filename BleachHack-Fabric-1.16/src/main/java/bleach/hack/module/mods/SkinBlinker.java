package bleach.hack.module.mods;

import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.event.events.EventTick;
import java.util.Random;
import java.util.Set;

import com.google.common.eventbus.Subscribe;
import net.minecraft.client.render.entity.PlayerModelPart;

/**
 * @author <a href="https://github.com/HerraVp">Vp</a>
 */

public class SkinBlinker extends Module {
    public SkinBlinker() {
        super("SkinBlinker", KEY_UNBOUND, Category.FUN, "Makes ur skins body parts blink");
    }

    private final Random random = new Random();

    @Subscribe
    public void onTick(EventTick event)
    { if(random.nextInt(4) != 0)
            return;

        Set<PlayerModelPart> activeParts =
                mc.options.getEnabledPlayerModelParts();

        for(PlayerModelPart part : PlayerModelPart.values())
            mc.options.setPlayerModelPart(part, !activeParts.contains(part));
    }

    @Override
    public void onDisable()
    {
        for(PlayerModelPart part : PlayerModelPart.values())
            mc.options.setPlayerModelPart(part, true);

        super.onDisable();
    }

}


