package com.github.theredbrain.resourcebarapi;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ResourceBarAPIClient implements ClientModInitializer {

	public static Map<String, int[]> CACHED_RESOURCE_BAR_VALUES = new HashMap<>();

	@Override
	public void onInitializeClient() {
	}

	public static void drawResourceBar(
			MinecraftClient client,
			TextRenderer textRenderer,
			DrawContext context,
			String identifier_string,
			int[] cached_values_default,
			int current_value,
			int max_value,
			int current_value_reduction,
			int current_value_reservation,
			ResourceBarAPI.ResourceBarOrigin resource_bar_origin,
			int element_offset_x,
			int element_offset_y,
			boolean is_centered,
			Identifier[] texture_ids,
			ResourceBarAPI.ResourceBarFillDirection resource_bar_fill_direction,
			int background_additional_middle_segment_amount,
			int horizontal_background_left_end_width,
			int horizontal_background_middle_segment_width,
			int horizontal_background_right_end_width,
			int horizontal_background_height,
			int vertical_background_width,
			int vertical_background_top_end_height,
			int vertical_background_middle_segment_height,
			int vertical_background_bottom_end_height,
			int progress_offset_x,
			int progress_offset_y,
			int progress_additional_middle_segment_amount,
			int horizontal_progress_left_end_width,
			int horizontal_progress_middle_segment_width,
			int horizontal_progress_right_end_width,
			int horizontal_progress_height,
			int vertical_progress_width,
			int vertical_progress_top_end_height,
			int vertical_progress_middle_segment_height,
			int vertical_progress_bottom_end_height,
			int reserved_offset_x,
			int reserved_offset_y,
			int reserved_additional_middle_segment_amount,
			int horizontal_reserved_left_end_width,
			int horizontal_reserved_middle_segment_width,
			int horizontal_reserved_right_end_width,
			int horizontal_reserved_height,
			int vertical_reserved_width,
			int vertical_reserved_top_end_height,
			int vertical_reserved_middle_segment_height,
			int vertical_reserved_bottom_end_height,
			boolean show_current_value_overlay,
			int overlay_offset_x,
			int overlay_offset_y,
			int horizontal_overlay_width,
			int horizontal_overlay_height,
			int vertical_overlay_width,
			int vertical_overlay_height,
			boolean enable_smooth_animation,
			int animation_interval,
			boolean max_value_change_is_animated,
			boolean show_number,
			boolean show_max_value,
			int number_offset_x,
			int number_offset_y,
			int resource_bar_number_color
	) {

		if (texture_ids.length != 38) {
			ResourceBarAPI.LOGGER.info("wrong texture id array length, needs to be 32, is: " + texture_ids.length);
			return;
		}

		int progressBarLength;
		int reservedBarLength;
		int progressMiddleSectionLength;
		int reservedMiddleSectionLength;
		int originX;
		int originY;
		if (resource_bar_origin == ResourceBarAPI.ResourceBarOrigin.TOP_MIDDLE) {
			originX = context.getScaledWindowWidth() / 2;
			originY = 0;
		} else if (resource_bar_origin == ResourceBarAPI.ResourceBarOrigin.TOP_RIGHT) {
			originX = context.getScaledWindowWidth();
			originY = 0;
		} else if (resource_bar_origin == ResourceBarAPI.ResourceBarOrigin.MIDDLE_LEFT) {
			originX = 0;
			originY = context.getScaledWindowHeight() / 2;
		} else if (resource_bar_origin == ResourceBarAPI.ResourceBarOrigin.MIDDLE_MIDDLE) {
			originX = context.getScaledWindowWidth() / 2;
			originY = context.getScaledWindowHeight() / 2;
		} else if (resource_bar_origin == ResourceBarAPI.ResourceBarOrigin.MIDDLE_RIGHT) {
			originX = context.getScaledWindowWidth();
			originY = context.getScaledWindowHeight() / 2;
		} else if (resource_bar_origin == ResourceBarAPI.ResourceBarOrigin.BOTTOM_LEFT) {
			originX = 0;
			originY = context.getScaledWindowHeight();
		} else if (resource_bar_origin == ResourceBarAPI.ResourceBarOrigin.BOTTOM_MIDDLE) {
			originX = context.getScaledWindowWidth() / 2;
			originY = context.getScaledWindowHeight();
		} else if (resource_bar_origin == ResourceBarAPI.ResourceBarOrigin.BOTTOM_RIGHT) {
			originX = context.getScaledWindowWidth();
			originY = context.getScaledWindowHeight();
		} else {
			originX = 0;
			originY = 0;
		}
		int elementX = originX + element_offset_x;
		int elementY = originY + element_offset_y;

		// region variable calculation
		if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.BOTTOM_TO_TOP || resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.TOP_TO_BOTTOM) {
			progressMiddleSectionLength = progress_additional_middle_segment_amount * vertical_progress_middle_segment_height;
			reservedMiddleSectionLength = progress_additional_middle_segment_amount * vertical_progress_middle_segment_height;
			progressBarLength = vertical_progress_top_end_height + progressMiddleSectionLength + vertical_progress_bottom_end_height;
			reservedBarLength = vertical_reserved_top_end_height + reservedMiddleSectionLength + vertical_reserved_bottom_end_height;
		} else {
			progressMiddleSectionLength = progress_additional_middle_segment_amount * horizontal_progress_middle_segment_width;
			reservedMiddleSectionLength = reserved_additional_middle_segment_amount * horizontal_reserved_middle_segment_width;
			progressBarLength = horizontal_progress_left_end_width + progressMiddleSectionLength + horizontal_progress_right_end_width;
			reservedBarLength = horizontal_reserved_left_end_width + reservedMiddleSectionLength + horizontal_reserved_right_end_width;
		}
		// endregion variable calculation

		int normalizedResourceRatio = (int) (((double) current_value / Math.max(max_value, 1)) * (progressBarLength));
		int normalizedReservedResourceRatio = (int) (((double) current_value_reservation / Math.max(max_value, 1)) * (reservedBarLength));

		int[] cachedValues = CACHED_RESOURCE_BAR_VALUES.getOrDefault(identifier_string, cached_values_default);

		int oldMaxBuildUp = cachedValues[0];
		int oldNormalizedResourceRatio = cachedValues[1];
		int resourceBarAnimationCounter = cachedValues[2];

		if (oldMaxBuildUp != max_value) {
			oldMaxBuildUp = max_value;
			if (!max_value_change_is_animated) {
				oldNormalizedResourceRatio = normalizedResourceRatio;
			}
		}

		resourceBarAnimationCounter = resourceBarAnimationCounter + Math.max(1, current_value_reduction);
		boolean reduceOldRatio = oldNormalizedResourceRatio > normalizedResourceRatio;
		if (oldNormalizedResourceRatio != normalizedResourceRatio && resourceBarAnimationCounter > Math.max(0, animation_interval)) {
			oldNormalizedResourceRatio = oldNormalizedResourceRatio + (reduceOldRatio ? -1 : 1);
			resourceBarAnimationCounter = 0;
		}

		CACHED_RESOURCE_BAR_VALUES.put(identifier_string, new int[]{oldMaxBuildUp, oldNormalizedResourceRatio, resourceBarAnimationCounter});

		client.getProfiler().push(identifier_string + "_bar");

		// background
		drawStaticTwoDirectionalLayer(
				context,
				Arrays.copyOfRange(texture_ids, 0, 6),
				resource_bar_fill_direction,
				elementX,
				elementY,
				is_centered,
				background_additional_middle_segment_amount,
				horizontal_background_left_end_width,
				horizontal_background_middle_segment_width,
				horizontal_background_right_end_width,
				horizontal_background_height,
				vertical_background_width,
				vertical_background_top_end_height,
				vertical_background_middle_segment_height,
				vertical_background_bottom_end_height
		);

		int progressElementX = elementX + progress_offset_x;
		int progressElementY = elementY + progress_offset_y;
		if (enable_smooth_animation) {
			if (reduceOldRatio) {

				// animation layer
				if (oldNormalizedResourceRatio > 0) {
					drawResourceBarFourDirectionalLayer(
							context,
							Arrays.copyOfRange(texture_ids, 12, 18),
							resource_bar_fill_direction,
							progressElementX,
							progressElementY,
							is_centered,
							progress_additional_middle_segment_amount,
							horizontal_progress_left_end_width,
							horizontal_progress_middle_segment_width,
							horizontal_progress_right_end_width,
							horizontal_progress_height,
							vertical_progress_width,
							vertical_progress_top_end_height,
							vertical_progress_middle_segment_height,
							vertical_progress_bottom_end_height,
							oldNormalizedResourceRatio
					);
				}

				// current value layer
				if (normalizedResourceRatio > 0) {
					drawResourceBarFourDirectionalLayer(
							context,
							Arrays.copyOfRange(texture_ids, 6, 12),
							resource_bar_fill_direction,
							progressElementX,
							progressElementY,
							is_centered,
							progress_additional_middle_segment_amount,
							horizontal_progress_left_end_width,
							horizontal_progress_middle_segment_width,
							horizontal_progress_right_end_width,
							horizontal_progress_height,
							vertical_progress_width,
							vertical_progress_top_end_height,
							vertical_progress_middle_segment_height,
							vertical_progress_bottom_end_height,
							normalizedResourceRatio
					);
				}
			} else {

				// current value layer
				if (normalizedResourceRatio > 0) {
					drawResourceBarFourDirectionalLayer(
							context,
							Arrays.copyOfRange(texture_ids, 18, 24),
							resource_bar_fill_direction,
							progressElementX,
							progressElementY,
							is_centered,
							progress_additional_middle_segment_amount,
							horizontal_progress_left_end_width,
							horizontal_progress_middle_segment_width,
							horizontal_progress_right_end_width,
							horizontal_progress_height,
							vertical_progress_width,
							vertical_progress_top_end_height,
							vertical_progress_middle_segment_height,
							vertical_progress_bottom_end_height,
							normalizedResourceRatio
					);
				}

				// animation layer
				if (oldNormalizedResourceRatio > 0) {
					drawResourceBarFourDirectionalLayer(
							context,
							Arrays.copyOfRange(texture_ids, 24, 30),
							resource_bar_fill_direction,
							progressElementX,
							progressElementY,
							is_centered,
							progress_additional_middle_segment_amount,
							horizontal_progress_left_end_width,
							horizontal_progress_middle_segment_width,
							horizontal_progress_right_end_width,
							horizontal_progress_height,
							vertical_progress_width,
							vertical_progress_top_end_height,
							vertical_progress_middle_segment_height,
							vertical_progress_bottom_end_height,
							oldNormalizedResourceRatio
					);
				}
			}
		} else {
			// progress
//			int displayRatio = enable_smooth_animation ? oldNormalizedResourceRatio : normalizedResourceRatio;
			if (normalizedResourceRatio > 0) {
				drawResourceBarFourDirectionalLayer(
						context,
						Arrays.copyOfRange(texture_ids, 6, 12),
						resource_bar_fill_direction,
						progressElementX,
						progressElementY,
						is_centered,
						progress_additional_middle_segment_amount,
						horizontal_progress_left_end_width,
						horizontal_progress_middle_segment_width,
						horizontal_progress_right_end_width,
						horizontal_progress_height,
						vertical_progress_width,
						vertical_progress_top_end_height,
						vertical_progress_middle_segment_height,
						vertical_progress_bottom_end_height,
						normalizedResourceRatio
				);
			}
		}

		// reserved
		if (normalizedReservedResourceRatio > 0) {
			int reservedElementX = elementX + reserved_offset_x;
			int reservedElementY = elementY + reserved_offset_y;
			drawResourceBarFourDirectionalLayer(
					context,
					Arrays.copyOfRange(texture_ids, 30, 36),
					getOppositeFillDirection(resource_bar_fill_direction),
					reservedElementX,
					reservedElementY,
					is_centered,
					reserved_additional_middle_segment_amount,
					horizontal_reserved_left_end_width,
					horizontal_reserved_middle_segment_width,
					horizontal_reserved_right_end_width,
					horizontal_reserved_height,
					vertical_reserved_width,
					vertical_reserved_top_end_height,
					vertical_reserved_middle_segment_height,
					vertical_reserved_bottom_end_height,
					normalizedReservedResourceRatio
			);
		}

		// overlay
		if (show_current_value_overlay && normalizedResourceRatio > 0) {
			int overlayElementX = progressElementX + overlay_offset_x;
			int overlayElementY = progressElementY + overlay_offset_y;
			if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.BOTTOM_TO_TOP) {
				// 1: bottom to top
				if (current_value > 0 && current_value < max_value) {
					context.drawGuiTexture(texture_ids[37], overlayElementX, overlayElementY + progressBarLength - normalizedResourceRatio, 0, 0, vertical_overlay_width, vertical_overlay_height, vertical_overlay_width, horizontal_overlay_height);
				}
			} else if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.RIGHT_TO_LEFT) {
				// 2: right to left
				if (current_value > 0 && current_value < max_value) {
					context.drawGuiTexture(texture_ids[36], overlayElementX + progressBarLength - normalizedResourceRatio, overlayElementY, 0, 0, horizontal_overlay_width, horizontal_overlay_height, horizontal_overlay_width, horizontal_overlay_height);
				}
			} else if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.TOP_TO_BOTTOM) {
				// 3: top to bottom
				if (current_value > 0 && current_value < max_value) {
					context.drawGuiTexture(texture_ids[37], overlayElementX, overlayElementY + normalizedResourceRatio, 0, 0, vertical_overlay_width, vertical_overlay_height, vertical_overlay_width, horizontal_overlay_height);
				}
			} else {
				// 0: left to right
				if (current_value > 0 && current_value < max_value) {
					context.drawGuiTexture(texture_ids[36], overlayElementX + normalizedResourceRatio, overlayElementY, 0, 0, horizontal_overlay_width, horizontal_overlay_height, horizontal_overlay_width, horizontal_overlay_height);
				}
			}
		}

		if (show_number) {
			String resourceBarNumberString = show_max_value ? current_value + "/" + max_value : String.valueOf(current_value);
			int resourceBarNumberX = originX - (textRenderer.getWidth(resourceBarNumberString) / 2) + number_offset_x;
			int resourceBarNumberY = originY + number_offset_y;

			client.getProfiler().swap(identifier_string + "_number");

			context.drawText(textRenderer, resourceBarNumberString, resourceBarNumberX + 1, resourceBarNumberY, 0, false);
			context.drawText(textRenderer, resourceBarNumberString, resourceBarNumberX - 1, resourceBarNumberY, 0, false);
			context.drawText(textRenderer, resourceBarNumberString, resourceBarNumberX, resourceBarNumberY + 1, 0, false);
			context.drawText(textRenderer, resourceBarNumberString, resourceBarNumberX, resourceBarNumberY - 1, 0, false);
			context.drawText(textRenderer, resourceBarNumberString, resourceBarNumberX, resourceBarNumberY, resource_bar_number_color, false);
		}


		client.getProfiler().pop();
	}

	private static ResourceBarAPI.ResourceBarFillDirection getOppositeFillDirection(ResourceBarAPI.ResourceBarFillDirection fillDirection) {
		if (fillDirection == ResourceBarAPI.ResourceBarFillDirection.BOTTOM_TO_TOP) {
			return ResourceBarAPI.ResourceBarFillDirection.TOP_TO_BOTTOM;
		} else if (fillDirection == ResourceBarAPI.ResourceBarFillDirection.RIGHT_TO_LEFT) {
			return ResourceBarAPI.ResourceBarFillDirection.LEFT_TO_RIGHT;
		} else if (fillDirection == ResourceBarAPI.ResourceBarFillDirection.TOP_TO_BOTTOM) {
			return ResourceBarAPI.ResourceBarFillDirection.BOTTOM_TO_TOP;
		} else {
			return ResourceBarAPI.ResourceBarFillDirection.RIGHT_TO_LEFT;
		}
	}

	private static void drawStaticTwoDirectionalLayer(
			DrawContext context,
			Identifier[] texture_ids,
			ResourceBarAPI.ResourceBarFillDirection resource_bar_fill_direction,
			int layer_x,
			int layer_y,
			boolean is_centered,
			int additional_middle_segment_amount,
			int horizontal_left_end_width,
			int horizontal_middle_segment_width,
			int horizontal_right_end_width,
			int horizontal_height,
			int vertical_width,
			int vertical_top_end_height,
			int vertical_middle_segment_height,
			int vertical_bottom_end_height
	) {

		if (texture_ids.length != 6) {
			ResourceBarAPI.LOGGER.info("wrong texture id array length, needs to be 6, is: " + texture_ids.length);
			return;
		}

		int barLength;
		int middleSectionLength;

		if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.BOTTOM_TO_TOP || resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.TOP_TO_BOTTOM) {
			middleSectionLength = additional_middle_segment_amount * vertical_middle_segment_height;
			barLength = vertical_top_end_height + middleSectionLength + vertical_bottom_end_height;
			if (is_centered) {
				layer_x -= vertical_width / 2;
				layer_y -= barLength / 2;
			}

			context.drawGuiTexture(texture_ids[3], layer_x, layer_y, 0, 0, vertical_width, vertical_top_end_height, vertical_width, vertical_top_end_height);
			if (additional_middle_segment_amount > 0) {
				for (int i = 0; i < additional_middle_segment_amount; i++) {
					context.drawGuiTexture(texture_ids[4], layer_x, layer_y + vertical_top_end_height + (i * vertical_middle_segment_height), 0, 0, vertical_width, vertical_middle_segment_height, vertical_width, vertical_middle_segment_height);
				}
			}
			context.drawGuiTexture(texture_ids[5], layer_x, layer_y + vertical_top_end_height + middleSectionLength, 0, 0, vertical_width, vertical_bottom_end_height, vertical_width, vertical_bottom_end_height);
		} else {
			middleSectionLength = additional_middle_segment_amount * horizontal_middle_segment_width;
			barLength = vertical_top_end_height + middleSectionLength + vertical_bottom_end_height;
			if (is_centered) {
				layer_x -= barLength / 2;
				layer_y -= horizontal_height / 2;
			}

			context.drawGuiTexture(texture_ids[0], layer_x, layer_y, 0, 0, horizontal_left_end_width, horizontal_height, horizontal_left_end_width, horizontal_height);
			if (additional_middle_segment_amount > 0) {
				for (int i = 0; i < additional_middle_segment_amount; i++) {
					context.drawGuiTexture(texture_ids[1], layer_x + horizontal_left_end_width + (i * horizontal_middle_segment_width), layer_y, 0, 0, horizontal_middle_segment_width, horizontal_height, horizontal_middle_segment_width, horizontal_height);
				}
			}
			context.drawGuiTexture(texture_ids[2], layer_x + horizontal_left_end_width + middleSectionLength, layer_y, 0, 0, horizontal_right_end_width, horizontal_height, horizontal_right_end_width, horizontal_height);
		}

	}

	private static void drawResourceBarFourDirectionalLayer(
			DrawContext context,
			Identifier[] texture_ids,
			ResourceBarAPI.ResourceBarFillDirection resource_bar_fill_direction,
			int layer_x,
			int layer_y,
			boolean is_centered,
			int additional_middle_segment_amount,
			int horizontal_left_end_width,
			int horizontal_middle_segment_width,
			int horizontal_right_end_width,
			int horizontal_height,
			int vertical_width,
			int vertical_top_end_height,
			int vertical_middle_segment_height,
			int vertical_bottom_end_height,
			int display_ratio

	) {

		if (texture_ids.length != 6) {
			ResourceBarAPI.LOGGER.info("wrong texture id array length, needs to be 6, is: " + texture_ids.length);
			return;
		}

		int barLength;
		int middleSectionLength;
		int ratioFirstPart;
		int ratioLastPart;

		if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.BOTTOM_TO_TOP || resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.TOP_TO_BOTTOM) {
			middleSectionLength = additional_middle_segment_amount * vertical_middle_segment_height;
			barLength = vertical_top_end_height + middleSectionLength + vertical_bottom_end_height;

			if (is_centered) {
				layer_x -= vertical_width / 2;
				layer_y -= barLength / 2;
			}
		} else {
			middleSectionLength = additional_middle_segment_amount * horizontal_middle_segment_width;
			barLength = horizontal_left_end_width + middleSectionLength + horizontal_right_end_width;

			if (is_centered) {
				layer_x -= barLength / 2;
				layer_y -= horizontal_height / 2;
			}
		}

		if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.BOTTOM_TO_TOP) {
			// 1: bottom to top

			ratioFirstPart = Math.min(vertical_bottom_end_height, display_ratio);
			ratioLastPart = Math.min(vertical_top_end_height, display_ratio - vertical_bottom_end_height - middleSectionLength);

			// bottom
			context.drawGuiTexture(texture_ids[5], layer_x, layer_y + barLength - ratioFirstPart, 0, vertical_bottom_end_height - ratioFirstPart, vertical_width, ratioFirstPart, vertical_width, vertical_bottom_end_height);

			// middle
			if (display_ratio > vertical_bottom_end_height && additional_middle_segment_amount > 0) {
				boolean breakDisplay = false;
				int currentTextureY;
				for (int i = 0; i < additional_middle_segment_amount; i++) {
					for (int j = 1; j <= vertical_middle_segment_height; j++) {
						currentTextureY = vertical_bottom_end_height + (i * vertical_middle_segment_height) + j;
						if (currentTextureY > display_ratio) {
							breakDisplay = true;
							break;
						}
						context.drawGuiTexture(texture_ids[4], layer_x, layer_y + barLength - currentTextureY, 0, vertical_bottom_end_height + vertical_middle_segment_height - j, vertical_width, 1, vertical_width, vertical_middle_segment_height);
					}
					if (breakDisplay) {
						break;
					}
				}

			}

			// top
			if (display_ratio > (vertical_bottom_end_height + middleSectionLength)) {
				context.drawGuiTexture(texture_ids[3], layer_x, layer_y + vertical_top_end_height - ratioLastPart, 0, vertical_top_end_height - ratioLastPart, vertical_width, ratioLastPart, vertical_width, vertical_top_end_height);
			}
		} else if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.RIGHT_TO_LEFT) {
			// 2: right to left

			ratioFirstPart = Math.min(horizontal_right_end_width, display_ratio);
			ratioLastPart = Math.min(horizontal_left_end_width, display_ratio - horizontal_right_end_width - middleSectionLength);

			context.drawGuiTexture(texture_ids[2], layer_x + barLength - ratioFirstPart, layer_y, vertical_width - ratioFirstPart, 0, ratioFirstPart, horizontal_height, vertical_width, horizontal_height);
			if (display_ratio > horizontal_right_end_width && additional_middle_segment_amount > 0) {
				boolean breakDisplay = false;
				int currentTextureX;
				for (int i = 0; i < additional_middle_segment_amount; i++) {
					for (int j = 1; j <= horizontal_middle_segment_width; j++) {
						currentTextureX = horizontal_left_end_width + (i * horizontal_middle_segment_width) + j;
						if (currentTextureX > display_ratio) {
							breakDisplay = true;
							break;
						}
						context.drawGuiTexture(texture_ids[1], layer_x + barLength - currentTextureX, layer_y, horizontal_middle_segment_width - j, 0, 1, horizontal_height, horizontal_middle_segment_width, horizontal_height);
					}
					if (breakDisplay) {
						break;
					}
				}

			}

			// left
			if (display_ratio > (horizontal_right_end_width + middleSectionLength)) {
				context.drawGuiTexture(texture_ids[0], layer_x + horizontal_left_end_width - ratioLastPart, layer_y, horizontal_left_end_width - ratioLastPart, 0, ratioLastPart, horizontal_height, horizontal_left_end_width, horizontal_height);
			}
		} else if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.TOP_TO_BOTTOM) {
			// 3: top to bottom

			ratioFirstPart = Math.min(vertical_top_end_height, display_ratio);
			ratioLastPart = Math.min(vertical_bottom_end_height, display_ratio - vertical_top_end_height - middleSectionLength);

			// top
			context.drawGuiTexture(texture_ids[3], layer_x, layer_y, 0, 0, vertical_width, ratioFirstPart, vertical_width, vertical_top_end_height);

			// middle
			if (display_ratio > vertical_top_end_height && additional_middle_segment_amount > 0) {
				boolean breakDisplay = false;
				int currentTextureY;
				for (int i = 0; i < additional_middle_segment_amount; i++) {
					for (int j = 0; j < vertical_middle_segment_height; j++) {
						currentTextureY = vertical_top_end_height + (i * vertical_middle_segment_height) + j;
						if (currentTextureY > display_ratio) {
							breakDisplay = true;
							break;
						}
						context.drawGuiTexture(texture_ids[4], layer_x, layer_y + currentTextureY, 0, j, vertical_width, 1, vertical_width, vertical_middle_segment_height);
					}
					if (breakDisplay) {
						break;
					}
				}
			}

			// bottom
			if (display_ratio > (vertical_top_end_height + middleSectionLength)) {
				context.drawGuiTexture(texture_ids[5], layer_x, layer_y + vertical_top_end_height + middleSectionLength, 0, 0, vertical_width, ratioLastPart, vertical_width, vertical_bottom_end_height);
			}
		} else {
			// 0: left to right

			ratioFirstPart = Math.min(horizontal_left_end_width, display_ratio);
			ratioLastPart = Math.min(horizontal_right_end_width, display_ratio - horizontal_left_end_width - middleSectionLength);

			// left
			context.drawGuiTexture(texture_ids[0], layer_x, layer_y, 0, 0, ratioFirstPart, horizontal_height, horizontal_left_end_width, horizontal_height);

			// middle
			if (display_ratio > horizontal_left_end_width && additional_middle_segment_amount > 0) {
				boolean breakDisplay = false;
				int currentTextureX;
				for (int i = 0; i < additional_middle_segment_amount; i++) {
					for (int j = 0; j < horizontal_middle_segment_width; j++) {
						currentTextureX = horizontal_left_end_width + (i * horizontal_middle_segment_width) + j;
						if (currentTextureX > display_ratio) {
							breakDisplay = true;
							break;
						}
						context.drawGuiTexture(texture_ids[1], layer_x + currentTextureX, layer_y, horizontal_left_end_width + j, 0, 1, horizontal_height, horizontal_middle_segment_width, horizontal_height);
					}
					if (breakDisplay) {
						break;
					}
				}
			}

			// right
			if (display_ratio > (horizontal_left_end_width + middleSectionLength)) {
				context.drawGuiTexture(texture_ids[2], layer_x + horizontal_left_end_width + middleSectionLength, layer_y, 0, 0, ratioLastPart, horizontal_height, horizontal_right_end_width, horizontal_height);
			}
		}
	}
}