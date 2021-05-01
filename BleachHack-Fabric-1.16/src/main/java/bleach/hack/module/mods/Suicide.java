package bleach.hack.module.mods;

import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingSlider;

public class Suicide extends Module {

    public Suicide() {
        super ("Suicide", KEY_UNBOUND, Category.PLAYER, "Automatically types /kill in chat (ONLY WORKS ON ANARCHY SERVER)");

    }

    @Override
    public void onEnable(){
        mc.player.sendChatMessage("/kill");
        super.setEnabled(false);
    }
}
