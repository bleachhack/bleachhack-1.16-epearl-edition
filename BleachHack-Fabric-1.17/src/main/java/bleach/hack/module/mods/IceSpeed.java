package bleach.hack.module.mods;

import bleach.hack.event.events.EventTick;
import bleach.hack.eventbus.BleachSubscribe;
import bleach.hack.module.Module;
import bleach.hack.module.ModuleCategory;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.util.FabricReflect;
import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.util.Set;

public class IceSpeed extends Module {

    private static final Set<Block> ICE_BLOCKS = Sets.newHashSet(Blocks.ICE, Blocks.PACKED_ICE, Blocks.FROSTED_ICE, Blocks.BLUE_ICE);

    public IceSpeed() {
        super("IceSpeed", KEY_UNBOUND, ModuleCategory.MOVEMENT, "Move faster on ice",
                new SettingSlider("Speed", 0, 9, 4, 2).withDesc("How fast to move when on ice"));
    }

    @BleachSubscribe
    public void onTick(EventTick event) {
        setSlipperiness(1f - getSetting(0).asSlider().getValueFloat() / 10f);
    }

    @Override
    public void onDisable() {
        setSlipperiness(0.989f);
        super.onDisable();
    }

    private void setSlipperiness(float slipperiness) {
        for (Block block: ICE_BLOCKS) {
            if (block.getSlipperiness() != slipperiness) {
                FabricReflect.writeField(block, slipperiness, "slipperiness", "field_23163");
            }
        }
    }
}