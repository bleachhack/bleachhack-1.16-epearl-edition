/* package bleach.hack.module.mods;

import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingMode;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.setting.base.SettingBase;
import bleach.hack.util.CrystalUtils;
import bleach.hack.module.Category;
import bleach.hack.util.BleachLogger;
import com.google.common.collect.Streams;
import com.sun.applet2.preloader.event.ConfigEvent;
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
 /

public class NewBedAura extends Module {
    int ticksPassed;
    Entity targetPlayer;
    BlockPos targetPlayerPos;
    int bed;
    int currentSlot;

    public NewBedAura(){
        super("BedAura", KEY_UNBOUND, Category.COMBAT, "Automatically places beds on target",
        new SettingSlider("Range", 0, 4.0f, 0.1f, 0),
        new SettingSlider("Delay", 0, 10, 0, 40
        ));
    }

    int bedSlot = -1;
    int oldSlot = -1;

    int ticks = 0;

    @Override
    public void onEnable() {
        if (mc.world == null || mc.player == null) {onDisable(); return;}
        for (int i = 0; i < 9; i++) {if (mc.player.inventory.getStack(i).getItem() instanceof BedItem) {bedSlot = i;}}
        if (bedSlot == -1) {onDisable(); return;}
        if (ticks != delay.getValue()) {ticks++; return;}
        else ticks = 0;
        if (mc.player == null || mc.world == null) {onDisable(); return;}
        List<Entity> players = Streams.stream(mc.world.getEntities()).filter(e ->  e instanceof PlayerEntity && mc.player.distanceTo(e) && e != mc.player).collect(Collectors.toList());
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

            if (bed == -1) {
                BleachLogger.infoMessage("Out of beds!");
                this.setEnabled(false);
                return;
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
 */