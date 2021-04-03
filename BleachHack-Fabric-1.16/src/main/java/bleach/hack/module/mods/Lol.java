package bleach.hack.module.mods;

import bleach.hack.BleachHack;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import net.minecraft.util.Util;
import net.minecraft.entity.player.PlayerEntity;

public class Lol extends Module {
    public Lol() {
        super("Lol", KEY_UNBOUND, Category.FUN, "LolLOLLOLOLOLOLLLOLOL");
    }
    public void onEnable() {
        mc.player.sendChatMessage("I just got rick rolled by BleachHack-VpEdition" + " " + BleachHack.VERSION);
        try {
            Util.getOperatingSystem().open("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
