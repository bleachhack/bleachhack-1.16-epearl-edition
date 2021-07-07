/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDrinker420/BleachHack/).
 * Copyright (c) 2021 Bleach and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package bleach.hack.module.mods;

import bleach.hack.eventbus.BleachSubscribe;
import bleach.hack.event.events.EventTick;
import bleach.hack.module.ModuleCategory;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingMode;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

public class AutoSprint extends Module {

	public AutoSprint() {
		super("AutoSprint", KEY_UNBOUND, ModuleCategory.MOVEMENT, "Makes the you automatically sprint",
				new SettingMode("Mode", "Legit", "Rage", "MultiDirect").withDesc("Sprinting mode"));
	}

	@BleachSubscribe
	public void onTick(EventTick event) {
		if (getSetting(0).asMode().mode == 0) {
			if (mc.player.forwardSpeed > 0) {
				if (!mc.player.isSneaking() && !mc.player.handSwinging && !mc.player.horizontalCollision) {
					if (!mc.player.isSprinting())
						mc.player.setSprinting(true);
				}
			}
		}
		else if (getSetting(0).asMode().mode == 1) {
			if (mc.player.forwardSpeed > 0 || mc.player.sidewaysSpeed > 0) {
				mc.player.setSprinting(true);
			}
		}

		else if (getSetting(0).asMode().mode == 2) {
			if ((mc.player.input.movementForward > 0 || mc.player.input.movementSideways > 0))
				mc.player.setSprinting(true);
			mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
		}

	}
}


