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
package bleach.hack.module.mods;

import bleach.hack.setting.base.SettingMode;
import com.google.common.eventbus.Subscribe;

import bleach.hack.event.events.EventTick;
import bleach.hack.module.ModuleCategory;
import bleach.hack.module.Module;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

public class Sprint extends Module {

	public Sprint() {
		super("Sprint", KEY_UNBOUND, ModuleCategory.MOVEMENT, "Makes the player automatically sprint.",
		new SettingMode("Mode", "Vanilla", "MultiDirect").withDesc("Mode for sprint"));
	}

	@Subscribe
	public void onTick(EventTick event) {
		if (getSetting(0).asMode().mode == 0) {
		mc.player.setSprinting(mc.player.input.movementForward > 0 && mc.player.input.movementSideways != 0 ||
				mc.player.input.movementForward > 0 && !mc.player.isSneaking()); }

		else if (getSetting(0).asMode().mode == 1) {
			if ((mc.player.input.movementForward > 0 || mc.player.input.movementSideways > 0))
				if (!mc.player.isSneaking())
					return;
				if (!mc.player.isSprinting())
				mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));

		}

   }
}

