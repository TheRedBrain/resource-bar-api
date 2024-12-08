package com.github.theredbrain.resourcebarapi;

import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedMap;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceBarAPIClient implements ClientModInitializer {

	private static final int CACHED_VALUE_ARRAY_LENGTH = 15;
	private static final int CACHED_TEXTURE_ID_ARRAY_LENGTH = 8;

	public static Map<String, double[]> CACHED_RESOURCE_BAR_VALUES = new HashMap<>();
	public static Map<String, Identifier[]> CACHED_RESOURCE_BAR_TEXTURE_IDS = new HashMap<>();

	@Override
	public void onInitializeClient() {
	}

	public static void clearCache(String identifier_string, double[] cached_values_default, Identifier[] cached_textures_default) {
		CACHED_RESOURCE_BAR_VALUES.put(identifier_string, cached_values_default);
		CACHED_RESOURCE_BAR_TEXTURE_IDS.put(identifier_string, cached_textures_default);
	}

	public static void drawResourceBar(
			MinecraftClient client,
			TextRenderer textRenderer,
			DrawContext context,
			String identifier_string,
			double[] cached_values_default,
			Identifier[] cached_texture_ids_default,
			boolean should_resource_bar_be_rendered,
			double current_value,
			double max_value,
			int current_value_reduction,
			double current_unreserved_value,
			ResourceBarAPI.ResourceBarOrigin resource_bar_origin,
			ValidatedMap<Integer, Integer> element_offsets_x,
			ValidatedMap<Integer, Integer> element_offsets_y,
			int additional_offset_x,
			int additional_offset_y,
			ResourceBarAPI.ResourceBarFillDirection resource_bar_fill_direction,
			ValidatedMap<Integer, Integer> background_texture_heights,
			ValidatedMap<Integer, Integer> background_texture_widths,
			ValidatedMap<Integer, Identifier> background_texture_ids,
			int progress_offset_x,
			int progress_offset_y,
			ValidatedMap<Integer, Integer> progress_texture_heights,
			ValidatedMap<Integer, Integer> progress_texture_widths,
			ValidatedMap<Integer, Identifier> progress_decrease_animation_texture_ids,
			ValidatedMap<Integer, Identifier> progress_increase_animation_texture_ids,
			ValidatedMap<Integer, Identifier> progress_increase_value_texture_ids,
			ValidatedMap<Integer, Identifier> progress_texture_ids,
			int reserved_offset_x,
			int reserved_offset_y,
			ValidatedMap<Integer, Integer> reserved_texture_heights,
			ValidatedMap<Integer, Integer> reserved_texture_widths,
			ValidatedMap<Integer, Identifier> reserved_texture_ids,
			boolean show_current_value_overlay,
			int overlay_offset_x,
			int overlay_offset_y,
			ValidatedMap<Integer, Integer> overlay_texture_heights,
			ValidatedMap<Integer, Integer> overlay_texture_widths,
			ValidatedMap<Integer, Identifier> overlay_texture_ids,
			boolean show_icon,
			int icon_offset_x,
			int icon_offset_y,
			ValidatedMap<Integer, Integer> icon_texture_heights,
			ValidatedMap<Integer, Integer> icon_texture_widths,
			ValidatedMap<Integer, Identifier> icon_texture_ids,
			boolean enable_smooth_animation,
			int animation_interval,
			boolean max_value_change_is_animated,
			boolean show_number,
			boolean show_max_value,
			int number_offset_x,
			int number_offset_y,
			int resource_bar_number_color
	) {

		if (cached_values_default.length != CACHED_VALUE_ARRAY_LENGTH) {
			if (ResourceBarAPI.SERVER_CONFIG.show_debug_log) {
				ResourceBarAPI.LOGGER.info("wrong default cached values array length, needs to be " + CACHED_VALUE_ARRAY_LENGTH + ", is: " + cached_values_default.length);
			}
			return;
		}
		if (cached_texture_ids_default.length != CACHED_TEXTURE_ID_ARRAY_LENGTH) {
			if (ResourceBarAPI.SERVER_CONFIG.show_debug_log) {
				ResourceBarAPI.LOGGER.info("wrong default cached values array length, needs to be " + CACHED_TEXTURE_ID_ARRAY_LENGTH + ", is: " + cached_texture_ids_default.length);
			}
			return;
		}

		int progressBarLength;
		int reservedBarLength;

		//region origin
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
		//endregion origin

		//region cache
		double[] cachedValues = CACHED_RESOURCE_BAR_VALUES.getOrDefault(identifier_string, cached_values_default);
		if (cachedValues.length != CACHED_VALUE_ARRAY_LENGTH) {
			cachedValues = cached_values_default;
		}

		Identifier[] cachedTextureIds = CACHED_RESOURCE_BAR_TEXTURE_IDS.getOrDefault(identifier_string, cached_texture_ids_default);
		if (cachedTextureIds.length != CACHED_TEXTURE_ID_ARRAY_LENGTH) {
			cachedTextureIds = cached_texture_ids_default;
		}

		double oldMaxBuildUp = cachedValues[0];
		int oldNormalizedResourceRatio = (int) cachedValues[1];
		int resourceBarAnimationCounter = (int) cachedValues[2];
		int element_offset_x = (int) cachedValues[3];
		int element_offset_y = (int) cachedValues[4];
		int background_texture_height = (int) cachedValues[5];
		int background_texture_width = (int) cachedValues[6];
		int progress_texture_height = (int) cachedValues[7];
		int progress_texture_width = (int) cachedValues[8];
		int reserved_texture_height = (int) cachedValues[9];
		int reserved_texture_width = (int) cachedValues[10];
		int overlay_texture_height = (int) cachedValues[11];
		int overlay_texture_width = (int) cachedValues[12];
		int icon_texture_height = (int) cachedValues[13];
		int icon_texture_width = (int) cachedValues[14];

		Identifier background_texture_id = cachedTextureIds[0];
		Identifier progress_decrease_animation_texture_id = cachedTextureIds[1];
		Identifier progress_increase_animation_texture_id = cachedTextureIds[2];
		Identifier progress_increase_value_texture_id = cachedTextureIds[3];
		Identifier progress_texture_id = cachedTextureIds[4];
		Identifier reserved_texture_id = cachedTextureIds[5];
		Identifier overlay_texture_id = cachedTextureIds[6];
		Identifier icon_texture_id = cachedTextureIds[7];

		boolean recalculate_cache = false;

		// recalculating cache
		if (oldMaxBuildUp != max_value) {
			oldMaxBuildUp = max_value;
			recalculate_cache = true;

			int current_threshold = 0;

			List<Integer> list = new ArrayList<>(element_offsets_x.keySet());
			for (Integer threshold : list) {
				if (max_value >= threshold && threshold >= current_threshold) {
					element_offset_x = element_offsets_x.getOrDefault(threshold, (int) cached_values_default[3]);
					current_threshold = threshold;
				}
			}

			list = new ArrayList<>(element_offsets_y.keySet());
			current_threshold = 0;
			for (Integer threshold : list) {
				if (max_value >= threshold && threshold >= current_threshold) {
					element_offset_y = element_offsets_y.getOrDefault(threshold, (int) cached_values_default[4]);
					current_threshold = threshold;
				}
			}

			list = new ArrayList<>(background_texture_heights.keySet());
			current_threshold = 0;
			for (Integer threshold : list) {
				if (max_value >= threshold && threshold >= current_threshold) {
					background_texture_height = background_texture_heights.getOrDefault(threshold, (int) cached_values_default[5]);
					current_threshold = threshold;
				}
			}

			list = new ArrayList<>(background_texture_widths.keySet());
			current_threshold = 0;
			for (Integer threshold : list) {
				if (max_value >= threshold && threshold >= current_threshold) {
					background_texture_width = background_texture_widths.getOrDefault(threshold, (int) cached_values_default[6]);
					current_threshold = threshold;
				}
			}

			list = new ArrayList<>(progress_texture_heights.keySet());
			current_threshold = 0;
			for (Integer threshold : list) {
				if (max_value >= threshold && threshold >= current_threshold) {
					progress_texture_height = progress_texture_heights.getOrDefault(threshold, (int) cached_values_default[7]);
					current_threshold = threshold;
				}
			}

			list = new ArrayList<>(progress_texture_widths.keySet());
			current_threshold = 0;
			for (Integer threshold : list) {
				if (max_value >= threshold && threshold >= current_threshold) {
					progress_texture_width = progress_texture_widths.getOrDefault(threshold, (int) cached_values_default[8]);
					current_threshold = threshold;
				}
			}

			list = new ArrayList<>(reserved_texture_heights.keySet());
			current_threshold = 0;
			for (Integer threshold : list) {
				if (max_value >= threshold && threshold >= current_threshold) {
					reserved_texture_height = reserved_texture_heights.getOrDefault(threshold, (int) cached_values_default[9]);
					current_threshold = threshold;
				}
			}

			list = new ArrayList<>(reserved_texture_widths.keySet());
			current_threshold = 0;
			for (Integer threshold : list) {
				if (max_value >= threshold && threshold >= current_threshold) {
					reserved_texture_width = reserved_texture_widths.getOrDefault(threshold, (int) cached_values_default[10]);
					current_threshold = threshold;
				}
			}

			list = new ArrayList<>(overlay_texture_heights.keySet());
			current_threshold = 0;
			for (Integer threshold : list) {
				if (max_value >= threshold && threshold >= current_threshold) {
					overlay_texture_height = overlay_texture_heights.getOrDefault(threshold, (int) cached_values_default[11]);
					current_threshold = threshold;
				}
			}

			list = new ArrayList<>(overlay_texture_widths.keySet());
			current_threshold = 0;
			for (Integer threshold : list) {
				if (max_value >= threshold && threshold >= current_threshold) {
					overlay_texture_width = overlay_texture_widths.getOrDefault(threshold, (int) cached_values_default[12]);
					current_threshold = threshold;
				}
			}

			list = new ArrayList<>(icon_texture_heights.keySet());
			current_threshold = 0;
			for (Integer threshold : list) {
				if (max_value >= threshold && threshold >= current_threshold) {
					icon_texture_height = icon_texture_heights.getOrDefault(threshold, (int) cached_values_default[13]);
					current_threshold = threshold;
				}
			}

			list = new ArrayList<>(icon_texture_widths.keySet());
			current_threshold = 0;
			for (Integer threshold : list) {
				if (max_value >= threshold && threshold >= current_threshold) {
					icon_texture_width = icon_texture_widths.getOrDefault(threshold, (int) cached_values_default[14]);
					current_threshold = threshold;
				}
			}

			list = new ArrayList<>(background_texture_ids.keySet());
			current_threshold = 0;
			for (Integer threshold : list) {
				if (max_value >= threshold && threshold >= current_threshold) {
					background_texture_id = background_texture_ids.getOrDefault(threshold, cached_texture_ids_default[0]);
					current_threshold = threshold;
				}
			}

			list = new ArrayList<>(progress_decrease_animation_texture_ids.keySet());
			current_threshold = 0;
			for (Integer threshold : list) {
				if (max_value >= threshold && threshold >= current_threshold) {
					progress_decrease_animation_texture_id = progress_decrease_animation_texture_ids.getOrDefault(threshold, cached_texture_ids_default[1]);
					current_threshold = threshold;
				}
			}

			list = new ArrayList<>(progress_increase_animation_texture_ids.keySet());
			current_threshold = 0;
			for (Integer threshold : list) {
				if (max_value >= threshold && threshold >= current_threshold) {
					progress_increase_animation_texture_id = progress_increase_animation_texture_ids.getOrDefault(threshold, cached_texture_ids_default[2]);
					current_threshold = threshold;
				}
			}

			list = new ArrayList<>(progress_increase_value_texture_ids.keySet());
			current_threshold = 0;
			for (Integer threshold : list) {
				if (max_value >= threshold && threshold >= current_threshold) {
					progress_increase_value_texture_id = progress_increase_value_texture_ids.getOrDefault(threshold, cached_texture_ids_default[3]);
					current_threshold = threshold;
				}
			}

			list = new ArrayList<>(progress_texture_ids.keySet());
			current_threshold = 0;
			for (Integer threshold : list) {
				if (max_value >= threshold && threshold >= current_threshold) {
					progress_texture_id = progress_texture_ids.getOrDefault(threshold, cached_texture_ids_default[4]);
					current_threshold = threshold;
				}
			}

			list = new ArrayList<>(reserved_texture_ids.keySet());
			current_threshold = 0;
			for (Integer threshold : list) {
				if (max_value >= threshold && threshold >= current_threshold) {
					reserved_texture_id = reserved_texture_ids.getOrDefault(threshold, cached_texture_ids_default[5]);
					current_threshold = threshold;
				}
			}

			list = new ArrayList<>(overlay_texture_ids.keySet());
			current_threshold = 0;
			for (Integer threshold : list) {
				if (max_value >= threshold && threshold >= current_threshold) {
					overlay_texture_id = overlay_texture_ids.getOrDefault(threshold, cached_texture_ids_default[6]);
					current_threshold = threshold;
				}
			}

			list = new ArrayList<>(icon_texture_ids.keySet());
			current_threshold = 0;
			for (Integer threshold : list) {
				if (max_value >= threshold && threshold >= current_threshold) {
					icon_texture_id = icon_texture_ids.getOrDefault(threshold, cached_texture_ids_default[7]);
					current_threshold = threshold;
				}
			}
		}

		// region variable calculation
		if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.BOTTOM_TO_TOP || resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.TOP_TO_BOTTOM) {
			progressBarLength = progress_texture_height;
			reservedBarLength = reserved_texture_height;
		} else {
			progressBarLength = progress_texture_width;
			reservedBarLength = reserved_texture_width;
		}

		int normalizedResourceRatio = (int) ((current_value / Math.max(max_value, 1)) * (progressBarLength));
		double currentReservedValue = max_value - current_unreserved_value;
		int normalizedReservedResourceRatio = (int) ((currentReservedValue / Math.max(max_value, 1)) * (reservedBarLength));

		if (recalculate_cache && !max_value_change_is_animated) {
			oldNormalizedResourceRatio = normalizedResourceRatio;
		}

		resourceBarAnimationCounter = resourceBarAnimationCounter + Math.max(1, current_value_reduction);
		boolean reduceOldRatio = oldNormalizedResourceRatio > normalizedResourceRatio;
		if (oldNormalizedResourceRatio != normalizedResourceRatio && resourceBarAnimationCounter > Math.max(0, animation_interval)) {
			oldNormalizedResourceRatio = oldNormalizedResourceRatio + (reduceOldRatio ? -1 : 1);
			resourceBarAnimationCounter = 0;
		}
		// endregion variable calculation

		CACHED_RESOURCE_BAR_VALUES.put(identifier_string, new double[]{
				oldMaxBuildUp,
				oldNormalizedResourceRatio,
				resourceBarAnimationCounter,
				element_offset_x,
				element_offset_y,
				background_texture_height,
				background_texture_width,
				progress_texture_height,
				progress_texture_width,
				reserved_texture_height,
				reserved_texture_width,
				overlay_texture_height,
				overlay_texture_width,
				icon_texture_height,
				icon_texture_width
		});

		CACHED_RESOURCE_BAR_TEXTURE_IDS.put(identifier_string, new Identifier[]{
				background_texture_id,
				progress_decrease_animation_texture_id,
				progress_increase_animation_texture_id,
				progress_increase_value_texture_id,
				progress_texture_id,
				reserved_texture_id,
				overlay_texture_id,
				icon_texture_id
		});
		//endregion cache

		if (should_resource_bar_be_rendered) {
			int elementX = originX + element_offset_x + additional_offset_x;
			int elementY = originY + element_offset_y + additional_offset_y;

			// background
			if (background_texture_id != null) {
				if (ResourceBarAPI.SERVER_CONFIG.show_debug_log) {
					ResourceBarAPI.LOGGER.info("background texture id == null");
				}
			} else if (background_texture_width > 0 && background_texture_height > 0) {
				if (ResourceBarAPI.SERVER_CONFIG.show_debug_log) {
					ResourceBarAPI.LOGGER.info("invalid background texture dimensions");
				}
			} else {

				client.getProfiler().push(identifier_string + "_background");
				context.drawTexture(
						background_texture_id,
						elementX,
						elementY,
						0,
						0,
						background_texture_width,
						background_texture_height,
						background_texture_width,
						background_texture_height
				);
			}
			client.getProfiler().pop();

			// progress
			int progressElementX = elementX + progress_offset_x;
			int progressElementY = elementY + progress_offset_y;
			if (enable_smooth_animation) {
				if (reduceOldRatio) {

					// animation layer
					if (oldNormalizedResourceRatio > 0) {
						drawResourceBarDynamicFourDirectionalLayer(
								client,
								context,
								progress_decrease_animation_texture_id,
								identifier_string + "_reduce_animation",
								resource_bar_fill_direction,
								progressElementX,
								progressElementY,
								progress_texture_width,
								progress_texture_height,
								normalizedResourceRatio,
								oldNormalizedResourceRatio
						);
					}

					// current value layer
					if (normalizedResourceRatio > 0) {
						drawResourceBarDynamicFourDirectionalLayer(
								client,
								context,
								progress_texture_id,
								identifier_string + "_reduce_value",
								resource_bar_fill_direction,
								progressElementX,
								progressElementY,
								progress_texture_width,
								progress_texture_height,
								0,
								normalizedResourceRatio
						);
					}
				} else {

					// current value layer
					if (normalizedResourceRatio > 0) {
						drawResourceBarDynamicFourDirectionalLayer(
								client,
								context,
								progress_increase_value_texture_id,
								identifier_string + "_increase_value",
								resource_bar_fill_direction,
								progressElementX,
								progressElementY,
								progress_texture_width,
								progress_texture_height,
								oldNormalizedResourceRatio,
								normalizedResourceRatio
						);
					}

					// animation layer
					if (oldNormalizedResourceRatio > 0) {
						drawResourceBarDynamicFourDirectionalLayer(
								client,
								context,
								progress_increase_animation_texture_id,
								identifier_string + "_increase_animation",
								resource_bar_fill_direction,
								progressElementX,
								progressElementY,
								progress_texture_width,
								progress_texture_height,
								0,
								oldNormalizedResourceRatio
						);
					}
				}
			} else {
				if (normalizedResourceRatio > 0) {
					drawResourceBarDynamicFourDirectionalLayer(
							client,
							context,
							progress_texture_id,
							identifier_string + "_progress_no_animation",
							resource_bar_fill_direction,
							progressElementX,
							progressElementY,
							progress_texture_width,
							progress_texture_height,
							0,
							normalizedResourceRatio
					);
				}
			}

			// reserved
			if (normalizedReservedResourceRatio > 0) {
				client.getProfiler().swap(identifier_string + "_reserved");
				int reservedElementX = elementX + reserved_offset_x;
				int reservedElementY = elementY + reserved_offset_y;
				drawResourceBarDynamicFourDirectionalLayer(
						client,
						context,
						reserved_texture_id,
						identifier_string + "_reserved",
						getOppositeFillDirection(resource_bar_fill_direction),
						reservedElementX,
						reservedElementY,
						reserved_texture_width,
						reserved_texture_height,
						normalizedReservedResourceRatio,
						reservedBarLength
				);
			}

			// overlay
			if (show_current_value_overlay && normalizedResourceRatio > 0) {
				if (overlay_texture_id == null) {
					if (ResourceBarAPI.SERVER_CONFIG.show_debug_log) {
						ResourceBarAPI.LOGGER.info("overlay texture id == null");
					}
				} else if (overlay_texture_width <= 0 || overlay_texture_height <= 0) {
					if (ResourceBarAPI.SERVER_CONFIG.show_debug_log) {
						ResourceBarAPI.LOGGER.info("invalid overlay texture dimensions");
					}
				} else {
					client.getProfiler().push(identifier_string + "_overlay");
					int overlayElementX = progressElementX + overlay_offset_x;
					int overlayElementY = progressElementY + overlay_offset_y;
					if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.BOTTOM_TO_TOP) {
						// 1: bottom to top
						if (current_value > 0 && current_value < max_value) {
							context.drawTexture(overlay_texture_id, overlayElementX, overlayElementY + progressBarLength - normalizedResourceRatio, 0, 0, overlay_texture_width, overlay_texture_height, overlay_texture_width, overlay_texture_height);
						}
					} else if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.RIGHT_TO_LEFT) {
						// 2: right to left
						if (current_value > 0 && current_value < max_value) {
							context.drawTexture(overlay_texture_id, overlayElementX + progressBarLength - normalizedResourceRatio, overlayElementY, 0, 0, overlay_texture_width, overlay_texture_height, overlay_texture_width, overlay_texture_height);
						}
					} else if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.TOP_TO_BOTTOM) {
						// 3: top to bottom
						if (current_value > 0 && current_value < max_value) {
							context.drawTexture(overlay_texture_id, overlayElementX, overlayElementY + normalizedResourceRatio, 0, 0, overlay_texture_width, overlay_texture_height, overlay_texture_width, overlay_texture_height);
						}
					} else {
						// 0: left to right
						if (current_value > 0 && current_value < max_value) {
							context.drawTexture(overlay_texture_id, overlayElementX + normalizedResourceRatio, overlayElementY, 0, 0, overlay_texture_width, overlay_texture_height, overlay_texture_width, overlay_texture_height);
						}
					}
					client.getProfiler().pop();
				}
			}
		}

		if (show_icon) {
			if (icon_texture_id == null) {
				if (ResourceBarAPI.SERVER_CONFIG.show_debug_log) {
					ResourceBarAPI.LOGGER.info("icon texture id == null");
				}
			} else if (icon_texture_width <= 0 || icon_texture_height <= 0) {
				if (ResourceBarAPI.SERVER_CONFIG.show_debug_log) {
					ResourceBarAPI.LOGGER.info("invalid icon texture dimensions");
				}
			} else {
				client.getProfiler().push(identifier_string + "_icon");
				context.drawTexture(icon_texture_id, originX + icon_offset_x, originY + icon_offset_y, 0, 0, icon_texture_width, icon_texture_height, icon_texture_width, icon_texture_height);
				client.getProfiler().pop();
			}
		}

		if (show_number) {
			int displayed_current_value = (int) Math.round(current_value);
			int displayed_max_value = (int) Math.round(max_value);
			int displayed_current_unreserved_value = (int) Math.round(current_unreserved_value);
			String resourceBarNumberString = show_max_value ? (current_unreserved_value > 0 ? displayed_current_value + "/" + displayed_current_unreserved_value + " (" + displayed_max_value + ")" : displayed_current_value + "/" + displayed_max_value) : String.valueOf(displayed_current_value);
			int resourceBarNumberX = originX - (textRenderer.getWidth(resourceBarNumberString) / 2) + number_offset_x;
			int resourceBarNumberY = originY + number_offset_y;

			client.getProfiler().push(identifier_string + "_number");

			context.drawText(textRenderer, resourceBarNumberString, resourceBarNumberX + 1, resourceBarNumberY, 0, false);
			context.drawText(textRenderer, resourceBarNumberString, resourceBarNumberX - 1, resourceBarNumberY, 0, false);
			context.drawText(textRenderer, resourceBarNumberString, resourceBarNumberX, resourceBarNumberY + 1, 0, false);
			context.drawText(textRenderer, resourceBarNumberString, resourceBarNumberX, resourceBarNumberY - 1, 0, false);
			context.drawText(textRenderer, resourceBarNumberString, resourceBarNumberX, resourceBarNumberY, resource_bar_number_color, false);

			client.getProfiler().pop();
		}
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

	private static void drawResourceBarDynamicFourDirectionalLayer(
			MinecraftClient client,
			DrawContext context,
			Identifier texture_id,
			String identifier_string,
			ResourceBarAPI.ResourceBarFillDirection resource_bar_fill_direction,
			int layer_x,
			int layer_y,
			int texture_width,
			int texture_height,
			int start_display,
			int end_display

	) {

		if (texture_id == null) {
			if (ResourceBarAPI.SERVER_CONFIG.show_debug_log) {
				ResourceBarAPI.LOGGER.info("texture id == null");
			}
			return;
		}

		if (texture_width <= 0 || texture_height <= 0) {
			if (ResourceBarAPI.SERVER_CONFIG.show_debug_log) {
				ResourceBarAPI.LOGGER.info("invalid texture dimensions");
			}
			return;
		}

		client.getProfiler().push(identifier_string);

		if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.BOTTOM_TO_TOP) {
			// 1: bottom to top

			context.drawTexture(texture_id, layer_x, layer_y + end_display, 0, end_display, texture_width, texture_height - end_display - start_display, texture_width, texture_height);

		} else if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.RIGHT_TO_LEFT) {
			// 2: right to left

			context.drawTexture(texture_id, layer_x + end_display, layer_y, end_display, 0, texture_width - end_display - start_display, texture_height, texture_width, texture_height);

		} else if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.TOP_TO_BOTTOM) {
			// 3: top to bottom

			context.drawTexture(texture_id, layer_x, layer_y + start_display, 0, start_display, texture_width, texture_height - start_display - end_display, texture_width, texture_height);

		} else {
			// 0: left to right

			context.drawTexture(texture_id, layer_x + start_display, layer_y, start_display, 0, texture_width - start_display - end_display, texture_height, texture_width, texture_height);

		}
		client.getProfiler().pop();
	}
}