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
/*

#OLD
package bleach.hack.module;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.glfw.GLFW;

import com.google.common.eventbus.Subscribe;

import bleach.hack.event.events.EventKeyPress;
import bleach.hack.module.mods.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;

public class ModuleManager {

	private static List<Module> mods = Arrays.asList(
			new Ambience(),
			new AntiChunkBan(),
			new AntiHunger(),
			new ArrowJuke(),
			new AutoArmor(),
			new AutoBedBomb(),
			new AutoBuild(),
			new AutoDonkeyDupe(),
			new AutoFish(),
			new AutoParkour(),
			new AutoReconnect(),
			new AutoRespawn(),
			new AutoAlign(),
			new AutoSign(),
			new AutoSteal(),
			new AutoThrow(),
			new AutoTool(),
			new AutoTotem(),
			new AutoTrap(),
			new AutoWalk(),
			new BedReplenish(),
			new BetterPortal(),
			new BetterTablist(),
			new BlockParty(),
			new BookCrash(),
			new BowBot(),
			new ClickGui(),
			new ClickTp(),
			new ColorSigns(),
			new Criticals(),
			new CrystalAura(),
			new CustomFOV(),
			new CustomChat(),
			new DiscordRPCMod(),
			new Dispenser32k(),
			new ElytraFly(),
			new EntityControl(),
			new ElytraReplace(),
			new ESP(),
			new FabritoneFIX(),
			new FakeLag(),
//			new FakePlayer(),
			new FastUse(),
			new Flight(),
			new Freecam(),
			new Fullbright(),
			new Ghosthand(),
			new HandProgress(),
			new HoleESP(),
			new HoleTP(),
			new Jesus(),
			new Killaura(),
			new LogoutSpot(),
			new Lol(),
			new MountBypass(),
			new MouseFriend(),
			new Nametags(),
		//TODO 	new NewBedAura
			new Nofall(),
			new NoKeyBlock(),
			new NoRender(),
			new NoSlow(),
			new Notebot(),
			new NotebotStealer(),
			new NoVelocity(),
			new Nuker(),
			new OffhandCrash(),
			new PacketFly(),
			new Peek(),
			new PlayerCrash(),
			new PopCounter(),
			new RotationSnap(),
			new SafeWalk(),
			new Scaffold(),
			new Search(),
			new ShaderRender(),
			new Spammer(),
			new Speed(),
			new SpeedMine(),
			new Sprint(),
			new StarGithub(),
			new StashFinder(),
			new Step(),
			new StorageESP(),
			new Surround(),
			new Timer(),
			new Tracers(),
			new Trail(),
			new Trajectories(),
			new UI(),
			new VoidESP(),
			new Welcomer(),
			new Xray(),
			new Zoom());


	public static List<Module> getModules() {
		return mods;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getModule(Class<T> clazz) {
		for (Module module : mods) {
			if (clazz.isInstance(module)) {
				return (T) module;
			}
		}

		return null;
	}

	public static Module getModuleByName(String name) {
		for (Module m : mods) {
			if (name.equalsIgnoreCase(m.getName()))
				return m;
		}
		return null;
	}

	public static List<Module> getModulesInCat(Category cat) {
		return mods.stream().filter(m -> m.getCategory().equals(cat)).collect(Collectors.toList());
	}

	@Subscribe
	public static void handleKeyPress(EventKeyPress eventKeyPress) {
		if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_F3))
			return;

		mods.stream().filter(m -> m.getKey() == eventKeyPress.getKey()).forEach(Module::toggle);
	}
}
*/
package bleach.hack.module;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import org.lwjgl.glfw.GLFW;

import com.google.gson.Gson;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;

public class ModuleManager {

	private static final Gson moduleGson = new Gson();

	private static final Map<String, Module> modules = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	public static Map<String, Module> getModuleMap() {
		return modules;
	}

	public static Iterable<Module> getModules() {
		return modules.values();
	}

	public static void loadModules(InputStream jsonInputStream) {
		ModuleListJson json = moduleGson.fromJson(new InputStreamReader(jsonInputStream), ModuleListJson.class);

		for (String moduleString : json.getModules()) {
			try {
				Class<?> moduleClass = Class.forName(String.format("%s.%s", json.getPackage(), moduleString));

				if (Module.class.isAssignableFrom(moduleClass)) {
					try {
						Module module = (Module) moduleClass.getConstructor().newInstance();

						loadModule(module);
					} catch (Exception exception) {
						System.err.printf("Failed to load module %s: could not instantiate.%n", moduleClass);
						exception.printStackTrace();
					}
				} else {
					System.err.printf("Failed to load module %s: not a descendant of Module.%n", moduleClass);
				}
			} catch (Exception exception) {
				System.err.printf("Failed to load module %s.%n", moduleString);
				exception.printStackTrace();
			}
		}
	}

	public static void loadModule(Module module) {
		if (modules.containsValue(module)) {
			System.err.printf("Failed to load module %s: a module with this name is already loaded.%n", module.getName());
		} else {
			modules.put(module.getName(), module);
			// TODO: Setup init system for modules
		}
	}

	@Deprecated
	@SuppressWarnings("unchecked")
	public static <T extends Module> T getModuleByClass(Class<T> clazz) {
		return (T) modules.values().stream().filter(clazz::isInstance).findFirst().orElse(null);
	}

	public static Module getModule(String name) {
		return modules.get(name);
	}

	public static List<Module> getModulesInCat(Category cat) {
		return modules.values().stream().filter(m -> m.getCategory().equals(cat)).collect(Collectors.toList());
	}

	// This is slightly improved, but still need to setup an input handler with a map of keys to modules/commands/whatever else
	public static void handleKeyPress(int key) {
		if (!InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_F3)) {
			modules.values().stream().filter(m -> m.getKey() == key).forEach(Module::toggle);
		}
	}
}