package bleach.hack.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import bleach.hack.module.ModuleManager;
import bleach.hack.module.mods.Xray;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

/**
 * Blocks are still tesselated even if they're transparent because Minecraft's
 * rendering engine is retarded.
 */
@Mixin(BlockRenderManager.class)
public class MixinBlockRenderManager {

    @Inject(method = "renderBlock", at = @At("HEAD"), cancellable = true)
    private void renderBlock(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrix, VertexConsumer vertexConsumer, boolean cull, Random random, CallbackInfoReturnable<Boolean> ci) {
        if (!((Xray) ModuleManager.getModule("Xray")).isVisible(state.getBlock())) {
            ci.setReturnValue(false);
        }
    }

}