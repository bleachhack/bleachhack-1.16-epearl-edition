package bleach.hack.module.mods;

import bleach.hack.event.events.EventTick;
import bleach.hack.module.Module;
import bleach.hack.module.ModuleCategory;
import bleach.hack.setting.base.SettingSlider;
import com.google.common.collect.Streams;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.BedBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BedItem;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Vp
 * https://github.com/HerraVp
 */

public class NewBedAura extends Module {
    int ticksPassed;
    Entity targetPlayer;
    BlockPos targetPlayerPos;
    int bed;
    int currentSlot;

    public NewBedAura(){
        super("BedAura", KEY_UNBOUND, ModuleCategory.COMBAT, "Automatically places beds on targets and explodes em",
                new SettingSlider("Delay", 0, 10, 5, 2),
                new SettingSlider("Range", 0, 20, 10, 0));
    }

   int bedSlot = -1;
    int oldSlot = -1;

    int ticks = 0;

    @Override
    public void onEnable() {
        super.onEnable();
        assert mc.player != null;
        ticksPassed = 0;
        if (mc.player == null) {
            super.setEnabled(false);
            return;
        }
    }

    @Subscribe
    public void onTick(EventTick event) {
        if (mc.world == null || mc.player == null) {onDisable(); return;}
        for (int i = 0; i < 9; i++) {if (mc.player.inventory.getStack(i).getItem() instanceof BedItem) {bedSlot = i;}}
        if (bedSlot == -1) {onDisable(); return;}
        if (ticks != getSetting(0).asSlider().getValue()) {ticks++; return;}
        else ticks = 0;
        if (mc.player == null || mc.world == null) {onDisable(); return;}
        List<Entity> players = Streams.stream(mc.world.getEntities()).filter(e -> e instanceof PlayerEntity && mc.player.distanceTo(e) <= getSetting(1).asSlider().getValue() && e != mc.player).collect(Collectors.toList());
        if (players.isEmpty()) {return;}
        PlayerEntity player = (PlayerEntity)players.get(0);
        ArrayList<Pair<BlockPos, Direction>> positions = new ArrayList<>();
        positions.add(new Pair<>(player.getBlockPos().north().up(), Direction.SOUTH));
        positions.add(new Pair<>(player.getBlockPos().east().up(), Direction.WEST));
        positions.add(new Pair<>(player.getBlockPos().south().up(), Direction.NORTH));
        positions.add(new Pair<>(player.getBlockPos().west().up(), Direction.EAST));
        positions.sort(Comparator.comparing(object -> object.getLeft().getSquaredDistance(mc.player.getX(), mc.player.getY(), mc.player.getZ(), true)));
        for (Pair<BlockPos, Direction> pair : positions) {
            BlockPos blockPos = pair.getLeft();
            Direction direction = pair.getRight();
            oldSlot = mc.player.inventory.selectedSlot;
            mc.player.inventory.selectedSlot = bedSlot;
            if (!(mc.world.getBlockState(blockPos)).getMaterial().isReplaceable()) continue;
            if (mc.world.getBlockState(blockPos.offset(direction)).getBlock() instanceof BedBlock) mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(Vec3d.of(blockPos.offset(direction)), Direction.DOWN, blockPos.offset(direction), true));
            if (!(mc.world.getBlockState(blockPos).getBlock() instanceof BedBlock))  {
                if (direction == Direction.NORTH) mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookOnly(-180f, mc.player.pitch, true));
                if (direction == Direction.EAST) mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookOnly(-90f, mc.player.pitch, true));
                if (direction == Direction.SOUTH) mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookOnly(0f, mc.player.pitch, true));
                if (direction == Direction.WEST) mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookOnly(90f, mc.player.pitch, true));
                mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(Vec3d.of(blockPos), Direction.DOWN, blockPos, false));
            }
            //todo maybe do it on another tick
            if (mc.world.getBlockState(blockPos).getBlock() instanceof BedBlock) mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(Vec3d.of(blockPos), Direction.DOWN, blockPos, true));
            mc.player.inventory.selectedSlot = oldSlot;
            break;
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        targetPlayer = null;
        ticksPassed = 0;
    }

}