/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDrinker420/bleachhack-1.14/).
 * Copyright (c) 2019 Bleach.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bleach.hack.command;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.google.gson.JsonElement;

import bleach.hack.BleachHack;
import bleach.hack.command.commands.*;
import bleach.hack.command.exception.CmdSyntaxException;
import bleach.hack.util.BleachLogger;
import bleach.hack.util.file.BleachFileHelper;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class CommandManager {

	public static boolean allowNextMsg = false;

	private static List<Command> commands = Arrays.asList(
			new CmdBind(),
			new CmdCI(),
			new CmdClickGui(),
			new CmdClip(),
			new CmdCustomChat(),
			new CmdCustomSign(),
			new CmdDupe(),
			new CmdEnchant(),
			new CmdEntityMenu(),
			new CmdEntityStats(),
			new CmdFriends(),
			new CmdGamemode(),
			new CmdGive(),
			new CmdHelp(),
			new CmdInvPeek(),
			new CmdNBT(),
			new CmdNotebot(),
			new CmdOpenFolder(),
			new CmdPeek(),
			new CmdPrefix(),
			new CmdRbook(),
			new CmdRename(),
			new CmdRpc(),
			new CmdSay(),
			new CmdSetting(),
			new CmdSkull(),
			new CmdSpammer(),
			new CmdTerrain(),
			new CmdToggle());

	public static List<Command> getCommands() {
		return commands;
	}

	public static void readPrefix() {
		JsonElement prefix = BleachFileHelper.readMiscSetting("prefix");

		if (prefix != null) {
			Command.PREFIX = prefix.getAsString();
		}
	}

	public static void callCommand(String input) {
		String[] split = input.split(" ");
		BleachHack.logger.info("Running command: " + Arrays.toString(split));

		for (Command c : getCommands()) {
			if (c.hasAlias(split[0])) {
				try {
					c.onCommand(split[0], ArrayUtils.subarray(split, 1, split.length));
				} catch (CmdSyntaxException e) {
					BleachLogger.errorMessage((MutableText) e.getTextMessage());

					MutableText text = new LiteralText("\u00a7b" + Command.PREFIX + c.getAliases()[0] + " - \u00a7f" + c.getDescription());
					Text tooltip = new LiteralText(
							"\u00a72Category: " + c.getCategory()
									+ "\n\u00a7bAliases: \u00a7f" + Command.PREFIX + String.join(" \u00a77/\u00a7f " + Command.PREFIX, c.getAliases())
									+ "\n\u00a7bUsage: \u00a7f" + c.getSyntax()
									+ "\n\u00a7bDesc: \u00a7f" + c.getDescription());

					BleachLogger.infoMessage(
							text.styled(style -> style
									.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tooltip))));
				} catch (Exception e) {
					e.printStackTrace();

					BleachLogger.errorMessage("\u00a7l> " + e.getClass().getSimpleName());
					BleachLogger.errorMessage("\u00a7l> \u00a7c" + e.getMessage());

					int i = 0;
					for (StackTraceElement st: e.getStackTrace()) {
						if (i >= 8) break;

						String[] bruh = st.getClassName().split("\\.");
						BleachLogger.errorMessage(bruh[bruh.length - 1] + "." + st.getMethodName() + "():" + st.getLineNumber());
						i++;
					}
				}

				return;
			}
		}

		BleachLogger.errorMessage("Command Not Found, Maybe Try " + Command.PREFIX + "help");
	}
}