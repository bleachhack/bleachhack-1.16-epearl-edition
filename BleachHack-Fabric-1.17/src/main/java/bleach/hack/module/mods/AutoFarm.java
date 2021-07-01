package bleach.hack.module.mods;

import bleach.hack.eventbus.BleachSubscribe;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import bleach.hack.event.events.EventTick;
import bleach.hack.module.ModuleCategory;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.setting.base.SettingToggle;
import bleach.hack.util.InventoryUtils;
import bleach.hack.util.world.WorldUtils;
import net.minecraft.block.AzaleaBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.CocoaBlock;
import net.minecraft.block.CropBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.GourdBlock;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SoulSandBlock;
import net.minecraft.block.StemBlock;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.feature.TreeFeature;
import bleach.hack.module.Module;

public class AutoFarm extends Module {

    private Map<BlockPos, Integer> mossMap = new HashMap<>();

    public AutoFarm() {
        super("AutoFarm", KEY_UNBOUND, ModuleCategory.PLAYER, "Wurst exclusive",
                new SettingSlider("Range", 1, 6, 4.5, 1).withDesc("Farming reach"),
                new SettingToggle("Till", true).withDesc("Tills dirt around you"),
                new SettingToggle("Harvest", true).withDesc("Harvests grown crop").withChildren(
                        new SettingToggle("Crops", true).withDesc("Harvests wheat, carrots, potato & beetroot"),
                        new SettingToggle("StemCrops", true).withDesc("Harvests melons/pumpkins"),
                        new SettingToggle("NetherWart", true).withDesc("Harvests nether wart"),
                        new SettingToggle("Cocoa", true).withDesc("Harvests cocoa beans"),
                        new SettingToggle("Berries", false).withDesc("Harvests sweet berries"),
                        new SettingToggle("SugarCane", false).withDesc("Harvests sugar canes"),
                        new SettingToggle("Cactus", false).withDesc("Harvests cactuses")),
                new SettingToggle("Plant", true).withDesc("Plants crop around you").withChildren(
                        new SettingToggle("Crops", true).withDesc("Plants wheat, carrots, potato & beetroot"),
                        new SettingToggle("StemCrops", true).withDesc("Plants melon/pumpkin stems"),
                        new SettingToggle("NetherWart", true).withDesc("Plants nether wart")),
                new SettingToggle("Bonemeal", true).withDesc("Bonemeals ungrown crop").withChildren(
                        new SettingToggle("Crops", true).withDesc("Bonemeals crops"),
                        new SettingToggle("StemCrops", true).withDesc("Bonemeals melon/pumpkin stems"),
                        new SettingToggle("Cocoa", true).withDesc("Bonemeals cocoa beans"),
                        new SettingToggle("Berries", false).withDesc("Bonemeals sweet berries"),
                        new SettingToggle("Mushrooms", false).withDesc("Bonemeals mushrooms"),
                        new SettingToggle("Saplings", false).withDesc("Bonemeals saplings"),
                        new SettingToggle("Moss", false).withDesc("Bonemeals moss")));
    }

    @Override
    public void onDisable() {
        mossMap.clear();

        super.onDisable();
    }

    @BleachSubscribe
    public void onTick(EventTick event) {
        mossMap.entrySet().removeIf(e -> e.setValue(e.getValue() - 1) == 0);

        double range = getSetting(0).asSlider().getValue();
        int ceilRange = MathHelper.ceil(range);
        SettingToggle harvestSetting = getSetting(2).asToggle();
        SettingToggle plantSetting = getSetting(3).asToggle();
        SettingToggle bonemealSetting = getSetting(4).asToggle();

        // Special case for moss to maximize efficiency
        if (bonemealSetting.state && bonemealSetting.getChild(6).asToggle().state) {
            int slot = InventoryUtils.getSlot(true, i -> mc.player.getInventory().getStack(i).getItem() == Items.BONE_MEAL);
            if (slot != -1) {
                BlockPos bestBlock = BlockPos.streamOutwards(new BlockPos(mc.player.getEyePos()), ceilRange, ceilRange, ceilRange)
                        .filter(b -> mc.player.getEyePos().distanceTo(Vec3d.ofCenter(b)) <= range && !mossMap.containsKey(b))
                        .map(b -> Pair.of(b.toImmutable(), getMossSpots(b)))
                        .filter(p -> p.getRight() > 10)
                        .map(Pair::getLeft)
                        .sorted(Comparator.reverseOrder())
                        .findFirst().orElse(null);

                if (bestBlock != null) {
                    if (!mc.world.isAir(bestBlock.up())) {
                        mc.interactionManager.updateBlockBreakingProgress(bestBlock.up(), Direction.UP);
                    }

                    Hand hand = InventoryUtils.selectSlot(slot);
                    mc.interactionManager.interactBlock(mc.player, mc.world, hand,
                            new BlockHitResult(Vec3d.ofCenter(bestBlock, 1), Direction.UP, bestBlock, false));
                    mossMap.put(bestBlock, 100);
                    return;
                }
            }
        }

        for (BlockPos pos: BlockPos.iterateOutwards(new BlockPos(mc.player.getEyePos()), ceilRange, ceilRange, ceilRange)) {
            if (mc.player.getEyePos().distanceTo(Vec3d.ofCenter(pos)) > range)
                continue;

            BlockState state = mc.world.getBlockState(pos);
            Block block = state.getBlock();
            if (harvestSetting.state) {
                if ((harvestSetting.getChild(0).asToggle().state && block instanceof CropBlock && ((CropBlock) block).isMature(state))
                        || (harvestSetting.getChild(1).asToggle().state && block instanceof GourdBlock)
                        || (harvestSetting.getChild(2).asToggle().state && block instanceof NetherWartBlock && state.get(NetherWartBlock.AGE) >= 3)
                        || (harvestSetting.getChild(3).asToggle().state && block instanceof CocoaBlock && state.get(CocoaBlock.AGE) >= 2)
                        || (harvestSetting.getChild(4).asToggle().state && block instanceof SweetBerryBushBlock && state.get(SweetBerryBushBlock.AGE) >= 3)
                        || (harvestSetting.getChild(5).asToggle().state && shouldHarvestTallCrop(pos, block, SugarCaneBlock.class))
                        || (harvestSetting.getChild(6).asToggle().state && shouldHarvestTallCrop(pos, block, CactusBlock.class))) {
                    mc.interactionManager.updateBlockBreakingProgress(pos, Direction.UP);
                    return;
                }
            }

            if (plantSetting.state) {
                if (block instanceof FarmlandBlock && mc.world.isAir(pos.up())) {
                    int slot = InventoryUtils.getSlot(true, i -> {
                        Item item = mc.player.getInventory().getStack(i).getItem();

                        if (plantSetting.getChild(0).asToggle().state && (item == Items.WHEAT_SEEDS || item == Items.CARROT || item == Items.POTATO || item == Items.BEETROOT)) {
                            return true;
                        }

                        return plantSetting.getChild(1).asToggle().state && (item == Items.PUMPKIN_SEEDS || item == Items.MELON_SEEDS);
                    });

                    if (slot != -1) {
                        WorldUtils.placeBlock(pos.up(), slot, 0, false, false, true);
                        return;
                    }
                }

                if (block instanceof SoulSandBlock && mc.world.isAir(pos.up()) && plantSetting.getChild(2).asToggle().state) {
                    int slot = InventoryUtils.getSlot(true, i -> mc.player.getInventory().getStack(i).getItem() == Items.NETHER_WART);

                    if (slot != -1) {
                        WorldUtils.placeBlock(pos.up(), slot, 0, false, false, true);
                        return;
                    }
                }
            }

            if (bonemealSetting.state) {
                int slot = InventoryUtils.getSlot(true, i -> mc.player.getInventory().getStack(i).getItem() == Items.BONE_MEAL);

                if (slot != -1) {
                    if ((bonemealSetting.getChild(0).asToggle().state && block instanceof CropBlock && !((CropBlock) block).isMature(state))
                            || (bonemealSetting.getChild(1).asToggle().state && block instanceof StemBlock && state.get(StemBlock.AGE) < StemBlock.MAX_AGE)
                            || (bonemealSetting.getChild(2).asToggle().state && block instanceof CocoaBlock && state.get(CocoaBlock.AGE) < 2)
                            || (bonemealSetting.getChild(3).asToggle().state && block instanceof SweetBerryBushBlock && state.get(SweetBerryBushBlock.AGE) < 3)
                            || (bonemealSetting.getChild(4).asToggle().state && block instanceof MushroomPlantBlock)
                            || (bonemealSetting.getChild(5).asToggle().state && (block instanceof SaplingBlock || block instanceof AzaleaBlock) && canPlaceSapling(pos))) {
                        Hand hand = InventoryUtils.selectSlot(slot);
                        mc.interactionManager.interactBlock(mc.player, mc.world, hand,
                                new BlockHitResult(Vec3d.ofCenter(pos, 1), Direction.UP, pos, false));
                        return;
                    }
                }
            }
        }
    }

    private boolean shouldHarvestTallCrop(BlockPos pos, Block posBlock, Class<? extends Block> blockClass) {
        return posBlock.getClass().equals(blockClass)
                && mc.world.getBlockState(pos.down()).getBlock().getClass().equals(blockClass)
                && !mc.world.getBlockState(pos.down(2)).getBlock().getClass().equals(blockClass);
    }

    private int getMossSpots(BlockPos pos) {
        if (mc.world.getBlockState(pos).getBlock() != Blocks.MOSS_BLOCK
                || mc.world.getBlockState(pos.up()).getHardness(mc.world, pos) != 0f) {
            return 0;
        }

        return (int) BlockPos.streamOutwards(pos, 3, 4, 3)
                .filter(b -> isMossGrowableOn(mc.world.getBlockState(b).getBlock()) && mc.world.isAir(b.up()))
                .count();
    }

    private boolean isMossGrowableOn(Block block) {
        return block == Blocks.STONE || block == Blocks.GRANITE || block == Blocks.ANDESITE || block == Blocks.DIORITE
                || block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.MYCELIUM || block == Blocks.GRASS_BLOCK
                || block == Blocks.PODZOL || block == Blocks.ROOTED_DIRT;
    }

    private boolean canPlaceSapling(BlockPos pos) {
        return BlockPos.stream(pos.getX() - 1, pos.getY() + 1, pos.getZ() - 1, pos.getX() + 1, pos.getY() + 5, pos.getZ() + 1)
                .allMatch(b -> TreeFeature.canTreeReplace(mc.world, b));
    }
}