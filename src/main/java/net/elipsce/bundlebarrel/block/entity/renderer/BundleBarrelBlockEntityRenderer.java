package net.elipsce.bundlebarrel.block.entity.renderer;

import net.elipsce.bundlebarrel.block.entity.custom.BundleBarrelBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import java.util.Objects;

public class BundleBarrelBlockEntityRenderer implements BlockEntityRenderer<BundleBarrelBlockEntity> {
    public BundleBarrelBlockEntityRenderer(BlockEntityRendererFactory.Context context) {

    }

    @Override
    public void render(BundleBarrelBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        ItemStack black = new ItemStack (Items.BLACK_BUNDLE);
        ItemStack red = new ItemStack (Items.RED_BUNDLE);

        matrices.push();
        matrices.translate(0.5f, 1.0f, 0.5f);
        matrices.scale(0.5f, 0.5f, 0.5f);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(270));
        if(entity.UP) {
            itemRenderer.renderItem(red, ModelTransformationMode.GUI, getLightLevel(Objects.requireNonNull(entity.getWorld()), entity.getPos().add(new Vec3i(0, 1, 0))), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
        } else {
            itemRenderer.renderItem(black, ModelTransformationMode.GUI, getLightLevel(Objects.requireNonNull(entity.getWorld()), entity.getPos().add(new Vec3i(0, 1, 0))), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
        }
        matrices.pop();

        matrices.push();
        matrices.translate(0.5f, 0.0f, 0.5f);
        matrices.scale(0.5f, 0.5f, 0.5f);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
        if(entity.DOWN) {
            itemRenderer.renderItem(red, ModelTransformationMode.GUI, getLightLevel(Objects.requireNonNull(entity.getWorld()), entity.getPos().add(new Vec3i(0, -1, 0))), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
        } else {
            itemRenderer.renderItem(black, ModelTransformationMode.GUI, getLightLevel(Objects.requireNonNull(entity.getWorld()), entity.getPos().add(new Vec3i(0, -1, 0))), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
        }
        matrices.pop();

        matrices.push();
        matrices.translate(0.0f, 0.5f, 0.5f);
        matrices.scale(0.5f, 0.5f, 0.5f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(270));
        if(entity.WEST) {
            itemRenderer.renderItem(red, ModelTransformationMode.GUI, getLightLevel(Objects.requireNonNull(entity.getWorld()), entity.getPos().add(new Vec3i(-1, 0, 0))), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
        } else {
            itemRenderer.renderItem(black, ModelTransformationMode.GUI, getLightLevel(Objects.requireNonNull(entity.getWorld()), entity.getPos().add(new Vec3i(-1, 0, 0))), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
        }
        matrices.pop();

        matrices.push();
        matrices.translate(1.0f, 0.5f, 0.5f);
        matrices.scale(0.5f, 0.5f, 0.5f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
        if(entity.EAST) {
            itemRenderer.renderItem(red, ModelTransformationMode.GUI, getLightLevel(Objects.requireNonNull(entity.getWorld()), entity.getPos().add(new Vec3i(1, 0, 0))), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
        } else {
            itemRenderer.renderItem(black, ModelTransformationMode.GUI, getLightLevel(Objects.requireNonNull(entity.getWorld()), entity.getPos().add(new Vec3i(1, 0, 0))), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
        }
        matrices.pop();

        matrices.push();
        matrices.translate(0.5f, 0.5f, 0.0f);
        matrices.scale(0.5f, 0.5f, 0.5f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
        if(entity.NORTH) {
            itemRenderer.renderItem(red, ModelTransformationMode.GUI, getLightLevel(Objects.requireNonNull(entity.getWorld()), entity.getPos().add(new Vec3i(0, 0, -1))), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
        } else {
            itemRenderer.renderItem(black, ModelTransformationMode.GUI, getLightLevel(Objects.requireNonNull(entity.getWorld()), entity.getPos().add(new Vec3i(0, 0, -1))), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
        }
        matrices.pop();

        matrices.push();
        matrices.translate(0.5f, 0.5f, 1.0f);
        matrices.scale(0.5f, 0.5f, 0.5f);
        if(entity.SOUTH) {
            itemRenderer.renderItem(red, ModelTransformationMode.GUI, getLightLevel(Objects.requireNonNull(entity.getWorld()), entity.getPos().add(new Vec3i(0, 0, 1))), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
        } else {
            itemRenderer.renderItem(black, ModelTransformationMode.GUI, getLightLevel(Objects.requireNonNull(entity.getWorld()), entity.getPos().add(new Vec3i(0, 0, 1))), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
        }
        matrices.pop();
    }

    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }
}
