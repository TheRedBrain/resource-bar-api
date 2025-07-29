package com.github.theredbrain.resourcebarapi;

import com.github.theredbrain.resourcebarapi.config.ServerConfig;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceBarAPI implements ModInitializer {
	public static final String MOD_ID = "resourcebarapi";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ServerConfig SERVER_CONFIG;

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Resource Bar API!");
		SERVER_CONFIG = ConfigApiJava.registerAndLoadConfig(ServerConfig::new);
	}

	public static Identifier identifier(String path) {
		return Identifier.of(MOD_ID, path);
	}

	public enum ResourceBarFillDirection {
		LEFT_TO_RIGHT,
		BOTTOM_TO_TOP,
		RIGHT_TO_LEFT,
		TOP_TO_BOTTOM;

		ResourceBarFillDirection() {
		}
	}

	public enum ResourceBarOrigin {
		TOP_LEFT,
		TOP_MIDDLE,
		TOP_RIGHT,
		MIDDLE_LEFT,
		MIDDLE_MIDDLE,
		MIDDLE_RIGHT,
		BOTTOM_LEFT,
		BOTTOM_MIDDLE,
		BOTTOM_RIGHT;

		ResourceBarOrigin() {
		}
	}

	public enum ResourceBarDisplay {
		ICON,
		SMOOTH,
		NONE;

		ResourceBarDisplay() {
		}
	}

	public record AdditionalIconType(
			double current_value,
			double max_value,
			Identifier container_texture_id,
			Identifier full_texture_id,
			Identifier half_texture_id
	) {}
}