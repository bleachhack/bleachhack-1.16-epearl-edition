package bleach.hack.module.mods;

import bleach.hack.event.events.EventTick;
import bleach.hack.eventbus.BleachSubscribe;
import bleach.hack.module.Module;
import bleach.hack.module.ModuleCategory;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.util.BleachLogger;
import com.google.common.collect.Streams;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.stream.Collectors;


public class AutoAnchor extends Module {
    int ticksPassed;
    Entity targetPlayer;
    int anchorSlot = -1;
    int glowStoneSlot = -1;
    int oldSlot = -1;
    int anchor;
    int glowstone;

    public AutoAnchor() {
        super("AutoAnchor", KEY_UNBOUND, ModuleCategory.COMBAT, "Automatically place and explodes respawn anchors",
                new SettingSlider("Delay", 0, 20, 10, 0),
                new SettingSlider("Range", 0, 10, 5, 2));
    }

    @Override
    public void onEnable() {
        super.onEnable();
        assert mc.player != null;
        ticksPassed = 0;
        if (mc.player == null) {
            super.setEnabled(false);
            return;
        }

        anchor = -1;
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem().equals(Items.RESPAWN_ANCHOR)) {
                anchor = i;
                break;
            }
        }

        glowstone = -1;
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem().equals(Items.GLOWSTONE)) {
                glowstone = i;
                break;
            }
        }

        if (anchor == -1) {
            BleachLogger.infoMessage("No anchors in hotbar!");
            this.setEnabled(false);
            return;
        }

        if (glowstone == -1) {
            BleachLogger.infoMessage("No glowstone in hotbar!");
            this.setEnabled(false);
            return;
        }
    }


    @BleachSubscribe
    public void onTick(EventTick event) {
        if (mc.world == null || mc.player == null) {
            onDisable();
            return;
        }

        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem().equals(Items.RESPAWN_ANCHOR)) {
                anchorSlot = i;
            } else if (mc.player.getInventory().getStack(i).getItem().equals(Items.GLOWSTONE)) {
                glowStoneSlot = i;
            }
        }
        if (anchorSlot == -1 || glowStoneSlot == -1) {
            onDisable();
            return;
        }
        if (ticksPassed != getSetting(0).asSlider().getValue()) {
            ticksPassed++;
            return;
        } else ticksPassed = 0;
        try {
            PlayerEntity player = (PlayerEntity) Streams.stream(mc.world.getEntities()).filter(e -> e instanceof PlayerEntity && mc.player.distanceTo(e) <= getSetting(1).asSlider().getValue() && e != mc.player).collect(Collectors.toList()).get(0);
            for (Direction direction : Direction.values()) {
                BlockPos blockPos = null;
                if (player.getBlockPos().getSquaredDistance(mc.player.getX(), mc.player.getY(), mc.player.getZ(), true) < 6.0f && !(mc.world.getBlockState(player.getBlockPos()).getBlock() != Blocks.RESPAWN_ANCHOR && mc.world.getBlockState(player.getBlockPos()).getBlock() != Blocks.AIR && mc.world.getBlockState(player.getBlockPos()).getMaterial().isReplaceable()))
                    blockPos = player.getBlockPos();
                else if (player.getBlockPos().offset(direction).getSquaredDistance(mc.player.getX(), mc.player.getY(), mc.player.getZ(), true) < 6.0f && (mc.world.getBlockState(player.getBlockPos().offset(direction)).getBlock() == Blocks.RESPAWN_ANCHOR || mc.world.getBlockState(player.getBlockPos().offset(direction)).getMaterial().isReplaceable()))
                    blockPos = player.getBlockPos().offset(direction);
                if (blockPos == null) continue;
                assert mc.interactionManager != null;
                mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(Vec3d.of(blockPos), Direction.DOWN, blockPos, false));
                if (mc.world.getBlockState(blockPos).getBlock().equals(Blocks.RESPAWN_ANCHOR)) {
                    mc.player.getInventory().selectedSlot = glowStoneSlot;
                    mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(Vec3d.of(blockPos), Direction.DOWN, blockPos, true));
                    mc.interactionManager.interactBlock(mc.player, mc.world, Hand.OFF_HAND, new BlockHitResult(Vec3d.of(blockPos), Direction.DOWN, blockPos, true));
                }
                mc.player.getInventory().selectedSlot = oldSlot;
                break;
            }
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        targetPlayer = null;
        ticksPassed = 0;
    }
}

