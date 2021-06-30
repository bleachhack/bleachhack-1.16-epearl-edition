package bleach.hack.module.mods;

import bleach.hack.event.events.EventTick;
import bleach.hack.eventbus.BleachSubscribe;
import bleach.hack.module.Module;
import bleach.hack.module.ModuleCategory;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.setting.base.SettingToggle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.Hand;


public class AutoBreed extends Module {

    public AutoBreed() {
        super("AutoBreed", KEY_UNBOUND, ModuleCategory.WORLD, "Automatically breeds nearby animals",
                new SettingSlider("Range", 0, 10, 5, 2),
                new SettingToggle("FeedBabies", false).withDesc("Feeds babies"));
    }

    @BleachSubscribe
    public void onTick(EventTick event) {
        assert mc.world != null;
        for (Entity e : mc.world.getEntities()) {
            if (e != null && e instanceof AnimalEntity) {
                final AnimalEntity animal = (AnimalEntity) e;
                if (animal.getHealth() > 0) {
                    if (!animal.isBaby() || getSetting(1).asToggle().state && !animal.isInLove() && mc.player.distanceTo(animal) <= getSetting(0).asSlider().getValue() && animal.isBreedingItem(mc.player.getInventory().getMainHandStack())) {
                        mc.interactionManager.interactEntity(mc.player, animal, Hand.MAIN_HAND);
                    }
                }
            }
        }
    }
}



