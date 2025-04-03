package net.elipsce.bundlebarrel;

import net.elipsce.bundlebarrel.block.entity.ModBlockEntities;
import net.elipsce.bundlebarrel.block.entity.renderer.BundleBarrelBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class BundleLoadingBarrelClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(ModBlockEntities.BUNDLE_BARREL_BE, BundleBarrelBlockEntityRenderer::new);
    }
}
