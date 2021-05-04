package bleach.hack.command.commands;

import bleach.hack.command.Command;
import bleach.hack.command.CommandCategory;
import bleach.hack.util.BleachLogger;
import net.minecraft.client.MinecraftClient;

import java.awt.*;
import java.io.File;
import java.nio.file.Paths;
public class CmdOpenFolder extends Command {

    public CmdOpenFolder() {
        super("openfolder", "Opens BleachHack folder", "openfolder", CommandCategory.MODULES);
    }

    @Override
    public void onCommand(String command, String[] args) throws Exception {
        BleachLogger.infoMessage("Opening BleachHack folder");
        if(!GraphicsEnvironment.isHeadless()) {
            System.setProperty("java.awt.headless", "false");
        }
        Desktop.getDesktop().open(new File(String.valueOf(Paths.get(MinecraftClient.getInstance().runDirectory.getPath(), "bleach/hack/"))));
    }

}

