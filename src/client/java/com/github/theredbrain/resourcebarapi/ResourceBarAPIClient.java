package com.github.theredbrain.resourcebarapi;

import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedMap;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.tuple.MutablePair;

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

	@Deprecated(forRemoval = true)
	public static void drawIconResourceBar(
			Minecraft client,
			GuiGraphics context,
			String identifier_string,
			double current_value,
			double max_value,
			Identifier container_texture_id,
			Identifier full_texture_id,
			Identifier half_texture_id,
			List<ResourceBarAPI.AdditionalIconType> additional_affix_values,
			List<ResourceBarAPI.AdditionalIconType> additional_prefix_values,
			int origin_x,
			int origin_y,
			int offset_x,
			int offset_y,
			ResourceBarAPI.ResourceBarFillDirection resource_bar_fill_direction,
			boolean reverse_stack_direction,
			int max_icon_amount_per_bar
	) {

		/* TODO - have pre and suffix value lists
		    	they can be used for absorption, reserved resources, etc
		    	the list elements contain value, texture ids
		 */
//		int arraySize = additional_prefix_values.size() + 1 + additional_affix_values.size();
//		int[] array = new int[arraySize];
//		int[] array2 = new int[]{additional_prefix_values.size(), additional_prefix_values.size() + 1, arraySize};
//		int offset = 0;
//		int pivot = additional_prefix_values.size();
//		for (int k = 0; k < arraySize; k++) {
//			if (k + 1 - offset <= pivot) {
//
//			}
//		}
		int i = (int) (max_value + 0.5F) / 2; // total icon amount iterator
		if (i != 0) {
			int bar_y = origin_y + offset_y;
			int bar_x = origin_x + offset_x;

			if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.LEFT_TO_RIGHT) {

				int m = bar_y;
				int n = 0; // offset for additional bars beyond the first

				while (i > 0) {
					int o = Math.min(i, max_icon_amount_per_bar); // current bar value
					i -= o;

					for (int p = 0; p < o; ++p) {
						int q = bar_x + p * 8;

						context.blitSprite(RenderPipelines.GUI_TEXTURED, container_texture_id, q, m, 9, 9);
						if (p * 2 + 1 + n < current_value) {
							context.blitSprite(RenderPipelines.GUI_TEXTURED, full_texture_id, q, m, 9, 9);
						}

						if (p * 2 + 1 + n == current_value) {
							context.blitSprite(RenderPipelines.GUI_TEXTURED, half_texture_id, q, m, 9, 9);
						}
					}

					if (reverse_stack_direction) {
						m -= 10;
					} else {
						m += 10;
					}
					n += max_icon_amount_per_bar * 2;
				}

			} else if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.RIGHT_TO_LEFT) {

				int m = bar_y;
				int n = 0;

				while (i > 0) {
					int o = Math.min(i, max_icon_amount_per_bar);
					i -= o;

					for (int p = 0; p < o; ++p) {
						int q = bar_x - p * 8 - 9;

						context.blitSprite(RenderPipelines.GUI_TEXTURED, container_texture_id, q, m, 9, 9);
						if (p * 2 + 1 + n < current_value) {
							context.blitSprite(RenderPipelines.GUI_TEXTURED, full_texture_id, q, m, 9, 9);
						}

						if (p * 2 + 1 + n == current_value) {
							context.blitSprite(RenderPipelines.GUI_TEXTURED, half_texture_id, q, m, 9, 9);
						}
					}

					if (reverse_stack_direction) {
						m -= 10;
					} else {
						m += 10;
					}
					n += max_icon_amount_per_bar * 2;
				}

			} else if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.TOP_TO_BOTTOM) {

				int n = bar_x;
				int m = 0;

				while (i > 0) {
					int o = Math.min(i, max_icon_amount_per_bar);
					i -= o;

					for (int p = 0; p < o; ++p) {
						int q = bar_y + p * 8;

						context.blitSprite(RenderPipelines.GUI_TEXTURED, container_texture_id, n, q, 9, 9);
						if (p * 2 + 1 + m < current_value) {
							context.blitSprite(RenderPipelines.GUI_TEXTURED, full_texture_id, n, q, 9, 9);
						}

						if (p * 2 + 1 + m == current_value) {
							context.blitSprite(RenderPipelines.GUI_TEXTURED, half_texture_id, n, q, 9, 9);
						}
					}

					if (reverse_stack_direction) {
						n -= 10;
					} else {
						n += 10;
					}
					m += max_icon_amount_per_bar * 2;
				}

			} else if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.BOTTOM_TO_TOP) {

				int n = bar_x;
				int m = 0;

				while (i > 0) {
					int o = Math.min(i, max_icon_amount_per_bar);
					i -= o;

					for (int p = 0; p < o; ++p) {
						int q = bar_y - p * 8 - 9;

						context.blitSprite(RenderPipelines.GUI_TEXTURED, container_texture_id, n, q, 9, 9);
						if (p * 2 + 1 + m < current_value) {
							context.blitSprite(RenderPipelines.GUI_TEXTURED, full_texture_id, n, q, 9, 9);
						}

						if (p * 2 + 1 + m == current_value) {
							context.blitSprite(RenderPipelines.GUI_TEXTURED, half_texture_id, n, q, 9, 9);
						}
					}

					if (reverse_stack_direction) {
						n -= 10;
					} else {
						n += 10;
					}
					m += max_icon_amount_per_bar * 2;
				}
			}
		}
	}

	public static void drawIconResourceBar(
			GuiGraphics context,
			List<ResourceBarAPI.ResourceBarIconType> icon_types,
			int origin_x,
			int origin_y,
			int offset_x,
			int offset_y,
			ResourceBarAPI.ResourceBarFillDirection resource_bar_fill_direction,
			boolean reverse_stack_direction,
			int max_icon_amount_per_bar
	) {

		double maxValue = 0;
		for (ResourceBarAPI.ResourceBarIconType resourceBarIconType : icon_types) {
			maxValue += resourceBarIconType.max_value();
		}
		if (maxValue != 0) {

			// global values
			int bar_y = origin_y + offset_y;
			int bar_x = origin_x + offset_x;
			boolean firstIconType = true;

			// global values depending on resource_bar_fill_direction
			boolean rotateStackAndFillAxis;
			int currentStackAxisPosition;
			int fillAxisStartingPosition;
			int fillAxisIterationMultiplier;
			int currentFillAxisPositionOffset;

			if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.RIGHT_TO_LEFT) {
				rotateStackAndFillAxis = false;
				currentStackAxisPosition = bar_y;
				fillAxisStartingPosition = bar_x;
				fillAxisIterationMultiplier = -8;
				currentFillAxisPositionOffset = -9;

			} else if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.TOP_TO_BOTTOM) {
				rotateStackAndFillAxis = true;
				currentStackAxisPosition = bar_x;
				fillAxisStartingPosition = bar_y;
				fillAxisIterationMultiplier = 8;
				currentFillAxisPositionOffset = 0;

			} else if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.BOTTOM_TO_TOP) {
				rotateStackAndFillAxis = true;
				currentStackAxisPosition = bar_x;
				fillAxisStartingPosition = bar_y;
				fillAxisIterationMultiplier = -8;
				currentFillAxisPositionOffset = -9;
			} else {
				rotateStackAndFillAxis = false;
				currentStackAxisPosition = bar_y;
				fillAxisStartingPosition = bar_x;
				fillAxisIterationMultiplier = 8;
				currentFillAxisPositionOffset = 0;
			}

			// local variables per icon type (reset when icon type uses a new line)
			int iconsInAdditionalBarsOffset = 0;
			int iconsInAdditionalStacksOffset = 0;
			int fillAxisIteratorStart = 0;
			boolean continueLastBar = false;
			int remainingIconAmount;

			for (ResourceBarAPI.ResourceBarIconType resourceBarIconType : icon_types) {

				remainingIconAmount = Mth.ceil(resourceBarIconType.max_value() / 2.0);

				if (firstIconType) {
					firstIconType = false;
				} else {
					if (resourceBarIconType.continuationType() == ResourceBarAPI.ContinuationType.NEW_LINE) {
						fillAxisIteratorStart = 0;
						iconsInAdditionalBarsOffset = iconsInAdditionalStacksOffset;
					} else if (continueLastBar) {

						if (reverse_stack_direction) {
							currentStackAxisPosition += 10;
						} else {
							currentStackAxisPosition -= 10;
						}
						iconsInAdditionalBarsOffset -= max_icon_amount_per_bar;
					} else {
						iconsInAdditionalBarsOffset = iconsInAdditionalStacksOffset;

					}
					continueLastBar = false;
				}

				while (remainingIconAmount > 0) {
					int currentBarIconAmount = Math.min(remainingIconAmount, max_icon_amount_per_bar - fillAxisIteratorStart);
					remainingIconAmount -= currentBarIconAmount;

					for (int p = fillAxisIteratorStart; p < currentBarIconAmount + fillAxisIteratorStart; ++p) {
						int currentFillAxisPosition = fillAxisStartingPosition + (p * fillAxisIterationMultiplier) + currentFillAxisPositionOffset;
						int currentValueCheck = (int) (resourceBarIconType.current_value() + iconsInAdditionalStacksOffset * 2);
						int iterativeValue = p * 2 + 1 + iconsInAdditionalBarsOffset * 2;

						context.blitSprite(RenderPipelines.GUI_TEXTURED, resourceBarIconType.container_texture_id(), rotateStackAndFillAxis ? currentStackAxisPosition : currentFillAxisPosition, rotateStackAndFillAxis ? currentFillAxisPosition : currentStackAxisPosition, 9, 9);
						if (iterativeValue < currentValueCheck) {
							context.blitSprite(RenderPipelines.GUI_TEXTURED, resourceBarIconType.full_texture_id(), rotateStackAndFillAxis ? currentStackAxisPosition : currentFillAxisPosition, rotateStackAndFillAxis ? currentFillAxisPosition : currentStackAxisPosition, 9, 9);
						} else if (iterativeValue == currentValueCheck) {
							context.blitSprite(RenderPipelines.GUI_TEXTURED, resourceBarIconType.half_texture_id(), rotateStackAndFillAxis ? currentStackAxisPosition : currentFillAxisPosition, rotateStackAndFillAxis ? currentFillAxisPosition : currentStackAxisPosition, 9, 9);
						}
					}

					if (currentBarIconAmount < max_icon_amount_per_bar - fillAxisIteratorStart) {
						fillAxisIteratorStart = currentBarIconAmount;
						continueLastBar = true;
					} else {
						fillAxisIteratorStart = 0;
						continueLastBar = false;
					}

					if (reverse_stack_direction) {
						currentStackAxisPosition -= 10;
					} else {
						currentStackAxisPosition += 10;
					}
					iconsInAdditionalBarsOffset += max_icon_amount_per_bar;
				}
				iconsInAdditionalStacksOffset += Mth.ceil(resourceBarIconType.max_value() / 2.0);
			}
		}
	}

	public static void drawSmoothResourceBar(
			Minecraft client,
			GuiGraphics context,
			String identifier_string,
			double[] cached_values_default,
			Identifier[] cached_texture_ids_default,
			double current_value,
			double max_value,
			int current_value_reduction,
			double current_unreserved_value,
			int origin_x,
			int origin_y,
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
			boolean max_value_change_is_animated
	) {

		if (cached_values_default.length != CACHED_VALUE_ARRAY_LENGTH) {
			if (ResourceBarAPI.SERVER_CONFIG.show_debug_log) {
				ResourceBarAPI.LOGGER.info("wrong default cached values array length, needs to be " + CACHED_VALUE_ARRAY_LENGTH + ", is: " + cached_values_default.length);
			}
			return;
		}
		if (cached_texture_ids_default.length != CACHED_TEXTURE_ID_ARRAY_LENGTH) {
			if (ResourceBarAPI.SERVER_CONFIG.show_debug_log) {
				ResourceBarAPI.LOGGER.info("wrong default cached texture ids array length, needs to be " + CACHED_TEXTURE_ID_ARRAY_LENGTH + ", is: " + cached_texture_ids_default.length);
			}
			return;
		}

		int progressBarLength;
		int reservedBarLength;

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

		int elementX = origin_x + element_offset_x + additional_offset_x;
		int elementY = origin_y + element_offset_y + additional_offset_y;

		// background
		if (background_texture_id == null) {
			if (ResourceBarAPI.SERVER_CONFIG.show_debug_log) {
				ResourceBarAPI.LOGGER.info("background texture id == null");
			}
		} else if (background_texture_width <= 0 && background_texture_height <= 0) {
			if (ResourceBarAPI.SERVER_CONFIG.show_debug_log) {
				ResourceBarAPI.LOGGER.info("invalid background texture dimensions");
			}
		} else {

			context.blit(
					RenderPipelines.GUI_TEXTURED,
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
			int reservedElementX = elementX + reserved_offset_x;
			int reservedElementY = elementY + reserved_offset_y;
			drawResourceBarDynamicFourDirectionalLayer(
					client,
					context,
					reserved_texture_id,
					identifier_string + "_reserved",
					resource_bar_fill_direction,
					reservedElementX,
					reservedElementY,
					reserved_texture_width,
					reserved_texture_height,
					reservedBarLength - normalizedReservedResourceRatio,
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
				int overlayElementX = progressElementX + overlay_offset_x;
				int overlayElementY = progressElementY + overlay_offset_y;
				if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.BOTTOM_TO_TOP) {
					// 1: bottom to top
					if (current_value > 0 && current_value < max_value) {
						context.blit(RenderPipelines.GUI_TEXTURED, overlay_texture_id, overlayElementX, overlayElementY + progressBarLength - normalizedResourceRatio, 0, 0, overlay_texture_width, overlay_texture_height, overlay_texture_width, overlay_texture_height);
					}
				} else if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.RIGHT_TO_LEFT) {
					// 2: right to left
					if (current_value > 0 && current_value < max_value) {
						context.blit(RenderPipelines.GUI_TEXTURED, overlay_texture_id, overlayElementX + progressBarLength - normalizedResourceRatio, overlayElementY, 0, 0, overlay_texture_width, overlay_texture_height, overlay_texture_width, overlay_texture_height);
					}
				} else if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.TOP_TO_BOTTOM) {
					// 3: top to bottom
					if (current_value > 0 && current_value < max_value) {
						context.blit(RenderPipelines.GUI_TEXTURED, overlay_texture_id, overlayElementX, overlayElementY + normalizedResourceRatio, 0, 0, overlay_texture_width, overlay_texture_height, overlay_texture_width, overlay_texture_height);
					}
				} else {
					// 0: left to right
					if (current_value > 0 && current_value < max_value) {
						context.blit(RenderPipelines.GUI_TEXTURED, overlay_texture_id, overlayElementX + normalizedResourceRatio, overlayElementY, 0, 0, overlay_texture_width, overlay_texture_height, overlay_texture_width, overlay_texture_height);
					}
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
				context.blit(RenderPipelines.GUI_TEXTURED, icon_texture_id, origin_x + icon_offset_x, origin_y + icon_offset_y, 0, 0, icon_texture_width, icon_texture_height, icon_texture_width, icon_texture_height);
			}
		}
	}

	public static void drawResourceNumber(
			Minecraft client,
			Font textRenderer,
			GuiGraphics context,
			String identifier_string,
			double current_value,
			double max_value,
			double current_unreserved_value,
			int origin_x,
			int origin_y,
			boolean show_max_value,
			int number_offset_x,
			int number_offset_y,
			int resource_bar_number_color
	) {

		int displayed_current_value = (int) Math.round(current_value);
		int displayed_max_value = (int) Math.round(max_value);
		int displayed_current_unreserved_value = (int) Math.round(current_unreserved_value);
		String resourceBarNumberString = show_max_value ? (current_unreserved_value < max_value ? displayed_current_value + "/" + displayed_current_unreserved_value + " (" + displayed_max_value + ")" : displayed_current_value + "/" + displayed_max_value) : String.valueOf(displayed_current_value);
		int resourceBarNumberX = origin_x - (textRenderer.width(resourceBarNumberString) / 2) + number_offset_x;
		int resourceBarNumberY = origin_y + number_offset_y;

		context.drawString(textRenderer, resourceBarNumberString, resourceBarNumberX + 1, resourceBarNumberY, 0, false);
		context.drawString(textRenderer, resourceBarNumberString, resourceBarNumberX - 1, resourceBarNumberY, 0, false);
		context.drawString(textRenderer, resourceBarNumberString, resourceBarNumberX, resourceBarNumberY + 1, 0, false);
		context.drawString(textRenderer, resourceBarNumberString, resourceBarNumberX, resourceBarNumberY - 1, 0, false);
		context.drawString(textRenderer, resourceBarNumberString, resourceBarNumberX, resourceBarNumberY, resource_bar_number_color, false);
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

	public static MutablePair<Integer, Integer> getOriginPos(GuiGraphics drawContext, ResourceBarAPI.ResourceBarOrigin origin) {
		int originX;
		int originY;
		if (origin == ResourceBarAPI.ResourceBarOrigin.TOP_MIDDLE) {
			originX = drawContext.guiWidth() / 2;
			originY = 0;
		} else if (origin == ResourceBarAPI.ResourceBarOrigin.TOP_RIGHT) {
			originX = drawContext.guiWidth();
			originY = 0;
		} else if (origin == ResourceBarAPI.ResourceBarOrigin.MIDDLE_LEFT) {
			originX = 0;
			originY = drawContext.guiHeight() / 2;
		} else if (origin == ResourceBarAPI.ResourceBarOrigin.MIDDLE_MIDDLE) {
			originX = drawContext.guiWidth() / 2;
			originY = drawContext.guiHeight() / 2;
		} else if (origin == ResourceBarAPI.ResourceBarOrigin.MIDDLE_RIGHT) {
			originX = drawContext.guiWidth();
			originY = drawContext.guiHeight() / 2;
		} else if (origin == ResourceBarAPI.ResourceBarOrigin.BOTTOM_LEFT) {
			originX = 0;
			originY = drawContext.guiHeight();
		} else if (origin == ResourceBarAPI.ResourceBarOrigin.BOTTOM_MIDDLE) {
			originX = drawContext.guiWidth() / 2;
			originY = drawContext.guiHeight();
		} else if (origin == ResourceBarAPI.ResourceBarOrigin.BOTTOM_RIGHT) {
			originX = drawContext.guiWidth();
			originY = drawContext.guiHeight();
		} else {
			originX = 0;
			originY = 0;
		}
		return new MutablePair<>(originX, originY);
	}

	private static void drawResourceBarDynamicFourDirectionalLayer(
			Minecraft client,
			GuiGraphics context,
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

		if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.BOTTOM_TO_TOP) {
			// 1: bottom to top

			context.blit(RenderPipelines.GUI_TEXTURED, texture_id, layer_x, layer_y + texture_height - end_display, 0, texture_height - end_display, texture_width, end_display - start_display, texture_width, texture_height);

		} else if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.RIGHT_TO_LEFT) {
			// 2: right to left

			context.blit(RenderPipelines.GUI_TEXTURED, texture_id, layer_x + texture_width - end_display, layer_y, texture_width - end_display, 0, end_display - start_display, texture_height, texture_width, texture_height);

		} else if (resource_bar_fill_direction == ResourceBarAPI.ResourceBarFillDirection.TOP_TO_BOTTOM) {
			// 3: top to bottom

			context.blit(RenderPipelines.GUI_TEXTURED, texture_id, layer_x, layer_y + start_display, 0, start_display, texture_width, end_display - start_display, texture_width, texture_height);

		} else {
			// 0: left to right

			context.blit(RenderPipelines.GUI_TEXTURED, texture_id, layer_x + start_display, layer_y, start_display, 0, end_display - start_display, texture_height, texture_width, texture_height);

		}
	}
}