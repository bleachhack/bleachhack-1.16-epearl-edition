package bleach.hack.module.mods;

import bleach.hack.event.events.EventTick;
import bleach.hack.eventbus.BleachSubscribe;
import bleach.hack.module.Module;
import bleach.hack.module.ModuleCategory;
import bleach.hack.setting.base.SettingSlider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.Hand;


public class AutoBreed extends Module {

    public AutoBreed() {
        super("AutoBreed", KEY_UNBOUND, ModuleCategory.WORLD, "Automatically breeds nearby animals",
                new SettingSlider("Range", 0, 10, 5, 2));
    }

    @BleachSubscribe
    public void onTick(EventTick event) {
        assert mc.world != null;
        for (Entity e : mc.world.getEntities()) {
            if (e != null && e instanceof AnimalEntity) {
                final AnimalEntity animal = (AnimalEntity) e;
                if (animal.getHealth() > 0) {
                    if (!animal.isBaby() && !animal.isInLove() && mc.player.distanceTo(animal) <= getSetting(0).asSlider().getValue() && animal.isBreedingItem(mc.player.getInventory().getMainHandStack())) {
                        mc.interactionManager.interactEntity(mc.player, animal, Hand.MAIN_HAND);
                    }
                }
            }
        }
    }
        /*
        if (ticksPassed != getSetting(0).asSlider().getValue()) {
            ticksPassed++;
            return;
        } else ticksPassed = 0;
        try {
            AnimalEntity animal = (AnimalEntity) Streams.stream(mc.world.getEntities()).filter(e -> e instanceof AnimalEntity && mc.player.distanceTo(e) <= getSetting(1).asSlider().getValue() && e != mc.player).collect(Collectors.toList()).get(0);
            if (!animal.isBaby() && !animal.isInLove()) {
                assert mc.player != null;
                if (animal.isBreedingItem(mc.player.getInventory().getMainHandStack())) {
                    mc.player.interact(animal, Hand.MAIN_HAND);
                }
            }
        } catch (Exception ignored) {}
    }*/
}



