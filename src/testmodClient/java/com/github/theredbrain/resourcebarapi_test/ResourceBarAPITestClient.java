package com.github.theredbrain.resourcebarapi_test;

import com.github.theredbrain.resourcebarapi.ResourceBarAPIClient;
import com.github.theredbrain.resourcebarapi_test.config.ClientConfig;
import com.github.theredbrain.resourcebarapi_test.config.ClientConfigWrapper;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class ResourceBarAPITestClient implements ClientModInitializer {
	public static ClientConfig clientConfig;

	private static final Identifier[] TEST_TEXTURES = {
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/horizontal_stamina_background_left_end.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/horizontal_stamina_background_middle_segment.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/horizontal_stamina_background_right_end.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/vertical_stamina_background_top_end.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/vertical_stamina_background_middle_segment.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/vertical_stamina_background_bottom_end.png"),

			ResourceBarAPITest.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_left_end.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_middle_segment.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_right_end.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/vertical_stamina_progress_top_end.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/vertical_stamina_progress_middle_segment.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/vertical_stamina_progress_bottom_end.png"),

			ResourceBarAPITest.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_decrease_animation_left_end.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_decrease_animation_middle_segment.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_decrease_animation_right_end.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/vertical_stamina_progress_decrease_animation_top_end.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/vertical_stamina_progress_decrease_animation_middle_segment.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/vertical_stamina_progress_decrease_animation_bottom_end.png"),

			ResourceBarAPITest.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_increase_value_left_end.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_increase_value_middle_segment.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_increase_value_right_end.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/vertical_stamina_progress_increase_value_top_end.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/vertical_stamina_progress_increase_value_middle_segment.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/vertical_stamina_progress_increase_value_bottom_end.png"),

			ResourceBarAPITest.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_increase_animation_left_end.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_increase_animation_middle_segment.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/horizontal_stamina_progress_increase_animation_right_end.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/vertical_stamina_progress_increase_animation_top_end.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/vertical_stamina_progress_increase_animation_middle_segment.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/vertical_stamina_progress_increase_animation_bottom_end.png"),

			ResourceBarAPITest.identifier("textures/gui/sprites/hud/horizontal_stamina_reserved_left_end.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/horizontal_stamina_reserved_middle_segment.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/horizontal_stamina_reserved_right_end.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/vertical_stamina_reserved_top_end.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/vertical_stamina_reserved_middle_segment.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/vertical_stamina_reserved_bottom_end.png"),

			ResourceBarAPITest.identifier("textures/gui/sprites/hud/horizontal_stamina_overlay.png"),
			ResourceBarAPITest.identifier("textures/gui/sprites/hud/vertical_stamina_overlay.png")
	};
	@Override
	public void onInitializeClient() {
		// Config
		AutoConfig.register(ClientConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
		clientConfig = ((ClientConfigWrapper) AutoConfig.getConfigHolder(ClientConfigWrapper.class).getConfig()).client;

		HudRenderCallback.EVENT.register((matrixStack, delta) -> {
			MinecraftClient minecraftClient = MinecraftClient.getInstance();
			PlayerEntity playerEntity = minecraftClient.player;
			ClientConfig clientConfig = ResourceBarAPITestClient.clientConfig;

			if (clientConfig.show_resource_bar && playerEntity != null) {

				int currentValue = clientConfig.current_value;
				int maxValue = clientConfig.max_value;

				if (maxValue > 0 && (currentValue < maxValue || clientConfig.show_full_resource_bar)) {

					ResourceBarAPIClient.drawResourceBar(
							minecraftClient,
							minecraftClient.textRenderer,
							matrixStack,
							ResourceBarAPITest.MOD_ID + ":test",
							new int[]{-1, -1, 0},
							currentValue,
							maxValue,
							clientConfig.current_value_reduction,
							clientConfig.current_value_reservation,
							clientConfig.origin,
							clientConfig.offset_x,
							clientConfig.offset_y,
							clientConfig.is_centered,
							TEST_TEXTURES,
							clientConfig.fill_direction,
							clientConfig.background_middle_segment_amount,
							clientConfig.horizontal_background_left_end_width,
							clientConfig.horizontal_background_middle_segment_width,
							clientConfig.horizontal_background_right_end_width,
							clientConfig.horizontal_background_height,
							clientConfig.vertical_background_width,
							clientConfig.vertical_background_top_end_height,
							clientConfig.vertical_background_middle_segment_height,
							clientConfig.vertical_background_bottom_end_height,
							clientConfig.progress_offset_x,
							clientConfig.progress_offset_y,
							clientConfig.progress_middle_segment_amount,
							clientConfig.horizontal_progress_left_end_width,
							clientConfig.horizontal_progress_middle_segment_width,
							clientConfig.horizontal_progress_right_end_width,
							clientConfig.horizontal_progress_height,
							clientConfig.vertical_progress_width,
							clientConfig.vertical_progress_top_end_height,
							clientConfig.vertical_progress_middle_segment_height,
							clientConfig.vertical_progress_bottom_end_height,
							clientConfig.reserved_offset_x,
							clientConfig.reserved_offset_y,
							clientConfig.reserved_middle_segment_amount,
							clientConfig.horizontal_reserved_left_end_width,
							clientConfig.horizontal_reserved_middle_segment_width,
							clientConfig.horizontal_reserved_right_end_width,
							clientConfig.horizontal_reserved_height,
							clientConfig.vertical_reserved_width,
							clientConfig.vertical_reserved_top_end_height,
							clientConfig.vertical_reserved_middle_segment_height,
							clientConfig.vertical_reserved_bottom_end_height,
							clientConfig.show_current_value_overlay,
							clientConfig.overlay_offset_x,
							clientConfig.overlay_offset_y,
							clientConfig.horizontal_overlay_width,
							clientConfig.horizontal_overlay_height,
							clientConfig.vertical_overlay_width,
							clientConfig.vertical_overlay_height,
							clientConfig.enable_smooth_animation,
							clientConfig.animation_interval,
							clientConfig.max_value_change_is_animated,
							clientConfig.show_number,
							clientConfig.show_max_value,
							clientConfig.number_offset_x,
							clientConfig.number_offset_y,
							clientConfig.number_color
					);
				}
			}
		});
	}
}
