package bleach.hack.module.mods;

import bleach.hack.module.ModuleCategory;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingSlider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;

public class CustomFOV extends Module {
    public double prevFov;

    public CustomFOV() {
        super ("CustomFOV", KEY_UNBOUND, ModuleCategory.RENDER, "Gamer fov",
                new SettingSlider("FOV", 0, 150, 100, 0));
    }

    @Override
    public void onEnable() {
        prevFov = mc.options.fov;
        MinecraftClient mc = MinecraftClient.getInstance();
        GameOptions options = mc.options;
        if (mc.world != null) {
            options.fov = (0 + getSetting(0).asSlider().getValue());
        }
    }

    @Override
    public void onDisable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        GameOptions options = mc.options;
        options.fov = prevFov;
    }
}