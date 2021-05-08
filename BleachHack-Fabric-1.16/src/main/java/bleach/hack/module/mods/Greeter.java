package bleach.hack.module.mods;

import bleach.hack.event.events.EventReadPacket;
import bleach.hack.event.events.EventTick;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingMode;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.setting.base.SettingToggle;
import bleach.hack.util.file.BleachFileMang;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author <a href="https://github.com/HerraVp">Vp</a>
 */

public class Greeter extends Module {

    private final Random rand = new Random();
    private List<String> lines = new ArrayList<>();
    private List<String> lines2 = new ArrayList<>();
    private int lineCount = 0;
    private int lineCount2 = 0;
    public List<String> message_queue = new ArrayList<>();
    public String player;
    public String message;


    public Greeter() {
        super("Greeter", KEY_UNBOUND, Category.CHAT, "Welcomes and says goodbyes to players (edit in greeter.txt & goodbye.txt)",
                new SettingMode("Read", "Order", "Random"),
                new SettingSlider("Delay", 0, 20, 3, 0).withDesc("Second delay between messages to avoid spam kicks"),
                new SettingToggle("Welcome", true),
                new SettingToggle("Goodbye", true));
    }

    @Subscribe
    public void onTick(EventTick event)
    {
        if (mc.player.age % (this.getSettings().get(1).asSlider().getValue()*20) == 0 && this.isEnabled())
        {
            if(message_queue.size() > 0) {
                message = message_queue.get(0);
                mc.player.sendChatMessage(message);
                message_queue.remove(0);
            }
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (!BleachFileMang.fileExists("greeter.txt")) {
            BleachFileMang.createFile("greeter.txt");
            BleachFileMang.appendFile("Welcome back, $p", "greeter.txt");

        }
        if (!BleachFileMang.fileExists("goodbye.txt")) {
            BleachFileMang.createFile("goodbye.txt");
            BleachFileMang.appendFile("Goodbye, $p", "goodbye.txt");

        }
        lines = BleachFileMang.readFileLines("greeter.txt");
        lines2 = BleachFileMang.readFileLines("goodbye.txt");
        lineCount = 0;
        lineCount2 = 0;
    }

    @Subscribe
    public void ilovewomen(EventReadPacket event) {
        if ((event.getPacket() instanceof PlayerListS2CPacket) && (((PlayerListS2CPacket) event.getPacket()).getAction().name().equals("ADD_PLAYER")) && (getSettings().get(2).asToggle().state)) {
            player = ((PlayerListS2CPacket) event.getPacket()).getEntries().get(0).getProfile().getName();
            if (lines.isEmpty()) return;
            if (player == null) return;
            if (mc.player == null) return;
            if (player.equals(mc.player.getDisplayName().asString())) return;
            if (getSetting(0).asMode().mode == 0) {
                message_queue.add(lines.get(lineCount).replace("$p", player));
            } else if (getSetting(0).asMode().mode == 1) {
                message_queue.add(lines.get(rand.nextInt(lines.size())).replace("$p", player));
            }
            if (lineCount >= lines.size() - 1) lineCount = 0;
            else lineCount++;
            player = null;
        }
        else if ((event.getPacket() instanceof PlayerListS2CPacket) && (((PlayerListS2CPacket) event.getPacket()).getAction().name().equals("REMOVE_PLAYER")) && (getSettings().get(3).asToggle().state)) {
            player = ((PlayerListS2CPacket) event.getPacket()).getEntries().get(0).getProfile().getName();
            if (lines2.isEmpty()) return;
            if (player == null) return;
            if (mc.player == null) return;
            if (player.equals(mc.player.getDisplayName().asString())) return;
            if (getSetting(0).asMode().mode == 0) {
                message_queue.add(lines2.get(lineCount2).replace("$p", player));
            } else if (getSetting(0).asMode().mode == 1) {
                message_queue.add(lines2.get(rand.nextInt(lines.size())).replace("$p", player));
            }
            if (lineCount2 >= lines2.size() - 1) lineCount2 = 0;
            else lineCount2++;
            player = null;

        }

    }

}
