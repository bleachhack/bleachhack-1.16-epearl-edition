package bleach.hack.module.mods;

import bleach.hack.event.events.EventTick;
import bleach.hack.eventbus.BleachSubscribe;
import bleach.hack.module.ModuleCategory;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingMode;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.setting.base.SettingToggle;
import bleach.hack.util.InvUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.BedItem;
import net.minecraft.item.Items;
import net.minecraft.item.SnowballItem;
import net.minecraft.screen.slot.SlotActionType;

public class AutoReplenish extends Module {

    public AutoReplenish() {
        super("AutoReplenish", KEY_UNBOUND, ModuleCategory.COMBAT, "Automatically equips beds/anchors",
                new SettingToggle("Override", true).withDesc("Equips a item even if theres another item in the offhand"),
                new SettingSlider("Slot: ", 0, 8, 8, 0),
                new SettingMode("Item: ", "Bed", "Anchor","Snowball", "Packed Ice"));
    }

    @BleachSubscribe
    public void onTick(EventTick event) {
        int slot = (int) getSetting(1).asSlider().getValue();
        if (getSetting(2).asMode().mode == 0) {
            if (mc.player.getInventory().getStack(slot).getItem() instanceof BedItem)
                return;
            if (mc.currentScreen instanceof InventoryScreen || mc.currentScreen == null) {
                for (int i = 9; i < 45; i++) {
                    if (mc.player.getInventory().getStack(i >= 36 ? i - 36 : i).getItem() instanceof BedItem)
                    {
                        moveItems(i, slot, false);
                        return;
                    }
                }
            }
        } else if (getSetting(2).asMode().mode == 1) {
            if (mc.player.getInventory().getStack(slot).getItem().equals(Items.RESPAWN_ANCHOR))
                return;
            if (mc.currentScreen instanceof InventoryScreen || mc.currentScreen == null) {
                for (int i = 9; i < 45; i++) {
                    if (mc.player.getInventory().getStack(i >= 36 ? i - 36 : i).getItem() == Items.RESPAWN_ANCHOR)
                    {
                        moveItems(i, slot, false);
                        return;
                    }
                }
            }
        } else if (getSetting(2).asMode().mode == 3) {
            if (mc.player.getInventory().getStack(slot).getItem() instanceof SnowballItem)
                return;
            if (mc.currentScreen instanceof InventoryScreen || mc.currentScreen == null) {
                for (int i = 9; i < 45; i++) {
                    if (mc.player.getInventory().getStack(i >= 36 ? i - 36 : i).getItem() instanceof SnowballItem)
                    {
                        moveItems(i, slot, false);
                        return;
                    }
                }
            }
        } else if (getSetting(2).asMode().mode == 4) {
            if (mc.player.getInventory().getStack(slot).getItem() == Items.PACKED_ICE)
                return;
            if (mc.currentScreen instanceof InventoryScreen || mc.currentScreen == null) {
                for (int i = 9; i < 45; i++) {
                    if (mc.player.getInventory().getStack(i >= 36 ? i - 36 : i).getItem() == Items.PACKED_ICE)
                    {
                        moveItems(i, slot, false);
                        return;
                    }
                }
            }
        }
    }
    private void moveItems(int from, int to, boolean stackable) {
        InvUtils.clickSlot(InvUtils.invIndexToSlotId(from), 0, SlotActionType.PICKUP);
        InvUtils.clickSlot(InvUtils.invIndexToSlotId(to), 0, SlotActionType.PICKUP);
        if (stackable) InvUtils.clickSlot(InvUtils.invIndexToSlotId(from), 0, SlotActionType.PICKUP);
    }

}