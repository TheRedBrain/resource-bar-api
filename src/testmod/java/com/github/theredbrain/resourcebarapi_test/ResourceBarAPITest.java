package com.github.theredbrain.resourcebarapi_test;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceBarAPITest implements ModInitializer {
	public static final String MOD_ID = "resourcebarapi_test";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
	public void onInitialize() {
		LOGGER.info("Testing Resource Bar API!");
	}
	public static Identifier identifier(String path) {
		return Identifier.of(MOD_ID, path);
	}
}