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
package bleach.hack.command.commands;

import java.util.Locale;

import bleach.hack.command.Command;
import bleach.hack.command.CommandCategory;
import bleach.hack.command.CommandManager;
import bleach.hack.util.BleachLogger;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class CmdHelp extends Command {

	public CmdHelp() {
		super("help", "Displays all the commands.", "help | help <command>", CommandCategory.MISC);
	}

	@Override
	public void onCommand(String alias, String[] args) throws Exception {
		String cmd = args.length == 0 ? "" : args[0];

		if (cmd.isEmpty()) {
			BleachLogger.infoMessage("Commands:");
		} else {
			BleachLogger.infoMessage("Syntax for " + PREFIX + cmd.toLowerCase(Locale.ENGLISH) + ":");
		}

		for (Command c : CommandManager.getCommands()) {
			if (!cmd.isEmpty() && !cmd.equalsIgnoreCase(c.getAliases()[0]))
				continue;

			MutableText text = new LiteralText("\u00a7b" + PREFIX + c.getAliases()[0] + " - \u00a7f" + c.getDescription());
			Text tooltip = new LiteralText(
					"\u00a72Category: " + c.getCategory()
							+ "\n\u00a7bAliases: \u00a7f" + PREFIX + String.join(" \u00a77/\u00a7f " + PREFIX, c.getAliases())
							+ "\n\u00a7bUsage: \u00a7f" + c.getSyntax()
							+ "\n\u00a7bDesc: \u00a7f" + c.getDescription());

			BleachLogger.noPrefixMessage(
					text.styled(style -> style
							.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tooltip))));
		}
	}

}