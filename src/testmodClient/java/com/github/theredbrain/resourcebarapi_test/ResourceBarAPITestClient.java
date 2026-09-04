package com.github.theredbrain.resourcebarapi_test;

import com.github.theredbrain.resourcebarapi.ResourceBarAPI;
import com.github.theredbrain.resourcebarapi.ResourceBarAPIClient;
import com.github.theredbrain.resourcebarapi_test.config.ClientConfig;
import me.fzzyhmstrs.fzzy_config.api.ConfigApi;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.List;

public class ResourceBarAPITestClient implements ClientModInitializer {
	public static ClientConfig clientConfig;
	private static final String RESOURCE_BAR_IDENTIFIER_STRING = ResourceBarAPITest.MOD_ID + ":test";

	@Override
	public void onInitializeClient() {
		clientConfig = ConfigApiJava.registerAndLoadConfig(ClientConfig::new, RegisterType.CLIENT);

		HudElementRegistry.attachElementAfter(VanillaHudElements.HEALTH_BAR, ResourceBarAPITest.identifier("test"), ((guiGraphicsExtractor, deltaTracker) -> {
			Minecraft minecraftClient = Minecraft.getInstance();
			Player playerEntity = Minecraft.getInstance().player;
			if (playerEntity != null && !minecraftClient.gui.hud.isHidden()) {
				int currentValue = clientConfig.current_value;
				int maxValue = clientConfig.max_value;
				int unreservedValue = clientConfig.current_unreserved_value;
				int absorptionValue = clientConfig.absorption_value;

				if (!playerEntity.isCreative() && maxValue + absorptionValue > 0) {

					MutablePair<Integer, Integer> originPos = ResourceBarAPIClient.getOriginPos(guiGraphicsExtractor, clientConfig.origin);

					if (clientConfig.resource_bar_display == ResourceBarAPI.ResourceBarDisplay.ICON && (currentValue < maxValue || clientConfig.show_full_resource_bar)) {
						List<ResourceBarAPI.ResourceBarIconType> list = new ArrayList<>();
						list.add(new ResourceBarAPI.ResourceBarIconType(
								currentValue,
								unreservedValue,
								Identifier.withDefaultNamespace("hud/heart/container"),
								Identifier.withDefaultNamespace("hud/heart/full"),
								Identifier.withDefaultNamespace("hud/heart/half"),
								ResourceBarAPI.ContinuationType.NEW_ICON
						));
						// reserved value
						list.add(new ResourceBarAPI.ResourceBarIconType(
								maxValue - unreservedValue,
								maxValue - unreservedValue,
								Identifier.withDefaultNamespace("hud/heart/container_blinking"),
								Identifier.withDefaultNamespace("hud/heart/full_blinking"),
								Identifier.withDefaultNamespace("hud/heart/half_blinking"),
								ResourceBarAPI.ContinuationType.NEW_ICON
						));
						// absorption value
						list.add(new ResourceBarAPI.ResourceBarIconType(
								absorptionValue,
								absorptionValue,
								Identifier.withDefaultNamespace("hud/heart/container"),
								Identifier.withDefaultNamespace("hud/heart/absorbing_full"),
								Identifier.withDefaultNamespace("hud/heart/absorbing_half"),
								ResourceBarAPI.ContinuationType.NEW_LINE
						));
						ResourceBarAPIClient.drawIconResourceBar(
								guiGraphicsExtractor,
								list,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.icon_bar_offset_x,
								clientConfig.icon_bar_offset_y,
								clientConfig.fill_direction,
								clientConfig.reverse_stack_direction,
								clientConfig.max_icon_amount_per_bar
						);
					} else if (clientConfig.resource_bar_display == ResourceBarAPI.ResourceBarDisplay.SMOOTH && (currentValue < maxValue || clientConfig.show_full_resource_bar)) {
						ResourceBarAPIClient.drawSmoothResourceBar(
								minecraftClient,
								guiGraphicsExtractor,
								RESOURCE_BAR_IDENTIFIER_STRING,
								new double[]{
										-1,
										-1,
										0,
										-91,
										-45,
										5,
										182,
										5,
										182,
										5,
										182,
										5,
										5,
										16,
										16
								},
								new Identifier[]{
										Identifier.fromNamespaceAndPath("resourcebarapi_test", "textures/gui/sprites/hud/horizontal_stamina_background.png"),
										Identifier.fromNamespaceAndPath("resourcebarapi_test", "textures/gui/sprites/hud/horizontal_stamina_progress_decrease_animation.png"),
										Identifier.fromNamespaceAndPath("resourcebarapi_test", "textures/gui/sprites/hud/horizontal_stamina_progress_increase_animation.png"),
										Identifier.fromNamespaceAndPath("resourcebarapi_test", "textures/gui/sprites/hud/horizontal_stamina_progress_increase_value.png"),
										Identifier.fromNamespaceAndPath("resourcebarapi_test", "textures/gui/sprites/hud/horizontal_stamina_progress.png"),
										Identifier.fromNamespaceAndPath("resourcebarapi_test", "textures/gui/sprites/hud/horizontal_stamina_reserved.png"),
										Identifier.fromNamespaceAndPath("resourcebarapi_test", "textures/gui/sprites/hud/horizontal_stamina_overlay.png"),
										null
								},
								currentValue,
								maxValue,
								clientConfig.current_value_reduction,
								unreservedValue,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.offsets_x,
								clientConfig.offsets_y,
								0,
								0,
								clientConfig.fill_direction,
								clientConfig.background_texture_heights,
								clientConfig.background_texture_widths,
								clientConfig.background_texture_ids,
								clientConfig.progress_offset_x,
								clientConfig.progress_offset_y,
								clientConfig.progress_texture_heights,
								clientConfig.progress_texture_widths,
								clientConfig.progress_decrease_animation_texture_ids,
								clientConfig.progress_increase_animation_texture_ids,
								clientConfig.progress_increase_value_texture_ids,
								clientConfig.progress_texture_ids,
								clientConfig.reserved_offset_x,
								clientConfig.reserved_offset_y,
								clientConfig.reserved_texture_heights,
								clientConfig.reserved_texture_widths,
								clientConfig.reserved_texture_ids,
								clientConfig.show_current_value_overlay,
								clientConfig.overlay_offset_x,
								clientConfig.overlay_offset_y,
								clientConfig.overlay_texture_heights,
								clientConfig.overlay_texture_widths,
								clientConfig.overlay_texture_ids,
								clientConfig.show_icon && maxValue > 0,
								clientConfig.icon_offset_x,
								clientConfig.icon_offset_y,
								clientConfig.icon_texture_heights,
								clientConfig.icon_texture_widths,
								clientConfig.icon_texture_ids,
								clientConfig.enable_smooth_animation,
								clientConfig.animation_interval,
								clientConfig.max_value_change_is_animated
						);
					}
					if (clientConfig.show_number && (currentValue < maxValue || clientConfig.show_full_resource_bar)) {
						ResourceBarAPIClient.drawResourceNumber(
								minecraftClient,
								minecraftClient.font,
								guiGraphicsExtractor,
								RESOURCE_BAR_IDENTIFIER_STRING,
								currentValue,
								maxValue,
								unreservedValue,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.show_max_value,
								clientConfig.number_offset_x,
								clientConfig.number_offset_y,
								clientConfig.number_color
						);
					}
				}
			}
		}));
		ConfigApi.event().onUpdateClient((identifier, config) -> {
			if (identifier.equals(Identifier.fromNamespaceAndPath(ResourceBarAPITest.MOD_ID, "client"))) {
				ResourceBarAPIClient.clearCache(
						RESOURCE_BAR_IDENTIFIER_STRING,
						new double[]{
								-1,
								-1,
								0,
								-91,
								-45,
								5,
								182,
								5,
								182,
								5,
								182,
								5,
								5,
								16,
								16
						},
						new Identifier[]{
								Identifier.fromNamespaceAndPath("resourcebarapi_test", "textures/gui/sprites/hud/horizontal_stamina_background.png"),
								Identifier.fromNamespaceAndPath("resourcebarapi_test", "textures/gui/sprites/hud/horizontal_stamina_progress_decrease_animation.png"),
								Identifier.fromNamespaceAndPath("resourcebarapi_test", "textures/gui/sprites/hud/horizontal_stamina_progress_increase_animation.png"),
								Identifier.fromNamespaceAndPath("resourcebarapi_test", "textures/gui/sprites/hud/horizontal_stamina_progress_increase_value.png"),
								Identifier.fromNamespaceAndPath("resourcebarapi_test", "textures/gui/sprites/hud/horizontal_stamina_progress.png"),
								Identifier.fromNamespaceAndPath("resourcebarapi_test", "textures/gui/sprites/hud/horizontal_stamina_reserved.png"),
								Identifier.fromNamespaceAndPath("resourcebarapi_test", "textures/gui/sprites/hud/horizontal_stamina_overlay.png"),
								null
						});
			}
		});
	}
}