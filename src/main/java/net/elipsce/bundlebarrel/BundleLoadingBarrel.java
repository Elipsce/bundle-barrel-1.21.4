package net.elipsce.bundlebarrel;

import net.elipsce.bundlebarrel.block.ModBlocks;
import net.elipsce.bundlebarrel.block.entity.ModBlockEntities;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BundleLoadingBarrel implements ModInitializer {
	public static final String MOD_ID = "bundlebarrel";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();
	}
}