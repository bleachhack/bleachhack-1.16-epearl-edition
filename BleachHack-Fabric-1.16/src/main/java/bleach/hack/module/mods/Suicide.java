package bleach.hack.module.mods;

import bleach.hack.module.ModuleCategory;
import bleach.hack.module.Module;

public class Suicide extends Module {

    public Suicide() {
        super ("Suicide", KEY_UNBOUND, ModuleCategory.PLAYER, "Automatically types /kill in chat (ONLY WORKS ON ANARCHY SERVERS)");

    }

    @Override
    public void onEnable(){
        mc.player.sendChatMessage("/kill");
        super.setEnabled(false);
    }
}
