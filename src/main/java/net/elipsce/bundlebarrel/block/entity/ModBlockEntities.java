package net.elipsce.bundlebarrel.block.entity;

import net.elipsce.bundlebarrel.BundleLoadingBarrel;
import net.elipsce.bundlebarrel.block.ModBlocks;
import net.elipsce.bundlebarrel.block.entity.custom.BundleBarrelBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<BundleBarrelBlockEntity> BUNDLE_BARREL_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(BundleLoadingBarrel.MOD_ID, "bundle_barrel_be"),
                    FabricBlockEntityTypeBuilder.create(BundleBarrelBlockEntity::new, ModBlocks.BUNDLE_BARREL).build());

    public static void registerBlockEntities() {
        BundleLoadingBarrel.LOGGER.info("Registering Block Entities for " + BundleLoadingBarrel.MOD_ID);
    }
}
