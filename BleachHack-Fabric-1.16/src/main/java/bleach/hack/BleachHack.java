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
package bleach.hack;

import com.google.common.eventbus.EventBus;
import com.google.gson.JsonElement;

import bleach.hack.command.CommandManager;
import bleach.hack.command.CommandSuggestor;
import bleach.hack.gui.title.BleachTitleScreen;
import bleach.hack.module.ModuleManager;
import bleach.hack.module.mods.ClickGui;
import bleach.hack.util.FriendManager;
import bleach.hack.util.file.BleachFileHelper;
import bleach.hack.util.file.BleachFileMang;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BleachHack implements ModInitializer {

	private static BleachHack instance = null;
	public static Logger logger;

	public static final String VERSION = "1.3-Alpha";
	public static final int INTVERSION = 29;

	public static final EventBus eventBus = new EventBus();

	public static FriendManager friendMang;

	//private BleachFileMang bleachFileManager;

	public static BleachHack getInstance() {
		return instance;
	}

	public BleachHack() {
		if (instance != null) {
			throw new RuntimeException("A BleachHack-VpEdition instance already exists.");
		}
	}

	@Override
	public void onInitialize() {
		long initStartTime = System.currentTimeMillis();

		if (instance != null) {
			throw new RuntimeException("BleachHack-VpEdition has already been initialized.");
		}

		instance = this;
		logger = LogManager.getFormatterLogger("BleachHack-VpEdition");

		//TODO base-rewrite
		//this.eventBus = new EventBus();
		//this.bleachFileManager = new BleachFileMang();
		BleachFileMang.init();
		ModuleManager.loadModules(this.getClass().getClassLoader().getResourceAsStream("bleachhack.modules.json"));
		BleachFileHelper.readModules();

		ClickGui.clickGui.initWindows();
		BleachFileHelper.readClickGui();
		BleachFileHelper.readFriends();
		BleachFileHelper.readUI();

		CommandManager.readPrefix();
		CommandSuggestor.init();

		JsonElement mainMenu = BleachFileHelper.readMiscSetting("customTitleScreen");
		if (mainMenu != null && !mainMenu.getAsBoolean()) {
			BleachTitleScreen.customTitleScreen = false;
		}

		logger.log(Level.INFO, "Loaded BleachHack-VpEdition in %d ms.", System.currentTimeMillis() - initStartTime);
	}
}