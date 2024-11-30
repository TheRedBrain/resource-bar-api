package com.github.theredbrain.resourcebarapi_test.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

public class ClientConfigWrapper extends PartitioningSerializer.GlobalData {
	@ConfigEntry.Category("client")
	@ConfigEntry.Gui.TransitiveObject
	public ClientConfig client = new ClientConfig();

	public ClientConfigWrapper() {
	}
}
