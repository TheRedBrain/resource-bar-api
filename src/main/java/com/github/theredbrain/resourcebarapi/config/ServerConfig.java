package com.github.theredbrain.resourcebarapi.config;

import com.github.theredbrain.resourcebarapi.ResourceBarAPI;
import me.fzzyhmstrs.fzzy_config.config.Config;

public class ServerConfig extends Config {
	public ServerConfig() {
		super(ResourceBarAPI.identifier("server"));
	}

	public boolean show_debug_log = false;
}
