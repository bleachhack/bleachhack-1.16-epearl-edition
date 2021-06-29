package bleach.hack.module.mods;

import bleach.hack.module.ModuleCategory;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingSlider;

public class Reach extends Module {
    public Reach() {
        super("Reach", KEY_UNBOUND, ModuleCategory.COMBAT, "Reach further",
                new SettingSlider("Reach", 0, 6, 4, 2).withDesc("Makes long arms"));
    }
}

//See code at MixinPlayerInteractionManager