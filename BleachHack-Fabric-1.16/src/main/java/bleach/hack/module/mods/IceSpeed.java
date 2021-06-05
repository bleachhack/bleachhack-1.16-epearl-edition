package bleach.hack.module.mods;

import bleach.hack.event.events.EventTick;
import bleach.hack.module.Module;
import bleach.hack.module.ModuleCategory;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.util.ReflectionHelper;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.util.Arrays;

/**
 * @Author Vp
 * https://github.com/HerraVp
 */

//This is a modified version of Ares client IceSpeed

public class IceSpeed extends Module {
//Its really scuffed but it works

    public IceSpeed() {
        super("IceSpeed", KEY_UNBOUND, ModuleCategory.MOVEMENT, "Move faster on ice",
            new SettingSlider("Speed", 0.5f, 0.2f, 0, 1 ).withDesc("Speed value (Lower = faster)"));
    }

    @Subscribe
    public void onTick(EventTick event) {
        setSlipperiness((float)getSetting(0).asSlider().getValue());
    }

    @Override
    public void onDisable() {
        setSlipperiness(0.98f);
        ReflectionHelper.setPrivateValue(AbstractBlock.class, Blocks.BLUE_ICE, 0.989f, "slipperiness", "field_23163");
        super.onDisable();
    }

    private void setSlipperiness(float speed) {
        for(Block block: Arrays.asList(Blocks.ICE, Blocks.PACKED_ICE, Blocks.FROSTED_ICE, Blocks.BLUE_ICE)){
            ReflectionHelper.setPrivateValue(AbstractBlock.class, block, speed, "slipperiness", "field_23163");
        }
    }
}