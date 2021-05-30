package bleach.hack.module.mods;

import bleach.hack.module.ModuleCategory;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.setting.base.SettingToggle;

//Made by cupzyy

public class BetterCamera extends Module {

    public BetterCamera() {
        super("BetterCamera", KEY_UNBOUND, ModuleCategory.RENDER, "Improves the 3rd person camera",
                new SettingToggle("CameraClip", true).withDesc("Makes the camera clip into walls"),
                new SettingToggle("Distance", true).withDesc("Sets a custom camera distance").withChildren(
                        new SettingSlider("Distance", 0.5, 15, 4, 1).withDesc("The desired camera distance")));
    }

    // Login handled in MixinCamera::clipToSpace
}