package com.aeternal.flowingtime.client.render;

import com.aeternal.flowingtime.block.entity.FLPedestalBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class FLPedestalRenderer implements BlockEntityRenderer<FLPedestalBlockEntity> {

    public FLPedestalRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(FLPedestalBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack stack = be.getInventory().getStackInSlot(0);
        if (stack.isEmpty()) return;
        if (be.getLevel() == null) return;

        poseStack.pushPose();

        // Position above pedestal
        poseStack.translate(0.5, 0.7, 0.5);

        // Bobbing animation
        long gameTime = be.getLevel().getGameTime();
        float bob = Mth.sin((gameTime + partialTick) / 10.0F) * 0.1F + 0.1F;
        poseStack.translate(0, bob, 0);

        // Scale
        poseStack.scale(0.75F, 0.75F, 0.75F);

        // Rotation
        float angle = (gameTime + partialTick) / 20.0F * (180F / (float) Math.PI);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(angle));

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        itemRenderer.renderStatic(stack, ItemTransforms.TransformType.GROUND,
                packedLight, packedOverlay, poseStack, bufferSource, 0);

        poseStack.popPose();
    }
}