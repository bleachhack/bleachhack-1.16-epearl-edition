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

import java.util.List;

import bleach.hack.command.Command;
import bleach.hack.command.CommandCategory;
import bleach.hack.gui.NotebotScreen;
import bleach.hack.util.BleachLogger;
import bleach.hack.util.BleachQueue;
import bleach.hack.util.Midi2Notebot;
import bleach.hack.util.file.BleachFileMang;

public class CmdNotebot extends Command {

	public CmdNotebot() {
		super("notebot", "Shows the notebot gui.", "notebot | notebot convert <midi file in .minecraft/bleach/>", CommandCategory.MODULES);
	}

	@Override
	public void onCommand(String alias, String[] args) throws Exception {
		if (args.length >= 2 && args[0].equalsIgnoreCase("convert")) {
			int i = 0;
			String s = "";
			List<List<Integer>> notes = Midi2Notebot.convert(BleachFileMang.stringsToPath(args[1]));

			while (BleachFileMang.fileExists("notebot", "notebot" + i + ".txt"))
				i++;

			for (List<Integer> i1 : notes)
				s += i1.get(0) + ":" + i1.get(1) + ":" + i1.get(2) + "\n";

			BleachFileMang.appendFile(s, "notebot", "notebot" + i + ".txt");
			BleachLogger.infoMessage("Saved Song As: notebot" + i + ".txt [" + notes.size() + " Notes]");
		} else {
			BleachQueue.add(() -> mc.openScreen(new NotebotScreen()));
		}
	}

}