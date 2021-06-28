package bleach.hack.module.mods;

import bleach.hack.BleachHack;
import bleach.hack.event.events.EventTick;
import bleach.hack.eventbus.BleachSubscribe;
import bleach.hack.module.Module;
import bleach.hack.module.ModuleCategory;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.util.BleachLogger;
import com.google.common.collect.Streams;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Blocks;
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

        bed = -1;
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem() instanceof BedItem) {
                bed = i;
                break;
            }
        }

        if (bed == -1) {
            BleachLogger.infoMessage("No beds in hotbar!");
            this.setEnabled(false);
            return;
        }
    }

    @BleachSubscribe
    public void onTick(EventTick event) {
        if (!mc.player.isAlive()) {
            return;
        }
        if (!this.isEnabled()) {return;}
        ticksPassed++;
        if(ticksPassed < getSetting(0).asSlider().getValue())
            return;
        ticksPassed = 0;

        targetPlayer = Streams.stream(mc.world.getEntities())
                .filter(e -> e instanceof PlayerEntity)
                .filter(e -> !(BleachHack.friendMang.has(e.getName().asString())) && e != mc.player)
                .filter(e -> e.getBlockPos() != mc.player.getBlockPos())
                .filter(e -> mc.player.distanceTo(e) < getSetting(1).asSlider().getValue())
                .filter(e -> !((PlayerEntity) e).isDead())
                .filter(e -> (mc.world.getBlockState(((PlayerEntity) e).getBlockPos()).getBlock() == Blocks.AIR||mc.world.getBlockState(((PlayerEntity) e).getBlockPos()).getBlock() == Blocks.LAVA))
                .sorted((a, b) -> Float.compare(a.distanceTo(mc.player), b.distanceTo(mc.player)))
                .findFirst()
                .orElse(null);
        if(targetPlayer == null)
            return;
        if(targetPlayer.isInvulnerable()) {
            BleachLogger.infoMessage(targetPlayer.getDisplayName().asString()+" target is invulnerable");
            return;
        }

        currentSlot = mc.player.getInventory().selectedSlot;


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
            currentSlot = mc.player.getInventory().selectedSlot;
            mc.player.getInventory().selectedSlot = currentSlot;
            if (!(mc.world.getBlockState(blockPos)).getMaterial().isReplaceable()) continue;
            if (mc.world.getBlockState(blockPos.offset(direction)).getBlock() instanceof BedBlock) mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(Vec3d.of(blockPos.offset(direction)), Direction.DOWN, blockPos.offset(direction), true));
            if (!(mc.world.getBlockState(blockPos).getBlock() instanceof BedBlock))  {
                if (direction == Direction.NORTH) mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(-180f, mc.player.getPitch(), true));
                if (direction == Direction.EAST) mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(-90f, mc.player.getPitch(), true));
                if (direction == Direction.SOUTH) mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(0f, mc.player.getPitch(), true));
                if (direction == Direction.WEST) mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(90f, mc.player.getPitch(), true));
                mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(Vec3d.of(blockPos), Direction.DOWN, blockPos, false));
            }
            //todo maybe do it on another tick
            if (mc.world.getBlockState(blockPos).getBlock() instanceof BedBlock) mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(Vec3d.of(blockPos), Direction.DOWN, blockPos, true));
            mc.player.getInventory().selectedSlot = currentSlot;
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