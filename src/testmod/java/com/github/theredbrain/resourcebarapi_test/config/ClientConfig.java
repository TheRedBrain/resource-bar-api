package com.github.theredbrain.resourcebarapi_test.config;

import com.github.theredbrain.resourcebarapi.ResourceBarAPI;
import com.github.theredbrain.resourcebarapi_test.ResourceBarAPITest;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedMap;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.resources.Identifier;
import java.util.HashMap;

public class ClientConfig extends Config {
	public ClientConfig() {
		super(ResourceBarAPITest.identifier("client"));
	}

	public boolean show_full_resource_bar = true;

	public int current_value = 5;
	public int max_value = 10;
	public int current_value_reduction = 0;
	public int current_unreserved_value = 8;
	public int absorption_value = 2;

	public ResourceBarAPI.ResourceBarDisplay resource_bar_display = ResourceBarAPI.ResourceBarDisplay.NONE;

	public ResourceBarAPI.ResourceBarOrigin origin = ResourceBarAPI.ResourceBarOrigin.BOTTOM_MIDDLE;

	public int icon_bar_offset_x = 0;
	public int icon_bar_offset_y = 0;
	public boolean reverse_stack_direction = true;
	public int max_icon_amount_per_bar = 10;

	public ValidatedMap<Integer, Integer> offsets_x = new ValidatedMap<>(new HashMap<>() {{
		put(0, -91);
	}}, new ValidatedInt(), new ValidatedInt());
	public ValidatedMap<Integer, Integer> offsets_y = new ValidatedMap<>(new HashMap<>() {{
		put(0, -45);
	}}, new ValidatedInt(), new ValidatedInt());

	public ResourceBarAPI.ResourceBarFillDirection fill_direction = ResourceBarAPI.ResourceBarFillDirection.LEFT_TO_RIGHT;

	public ValidatedMap<Integer, Integer> background_texture_heights = new ValidatedMap<>(new HashMap<>() {{
		put(0, 5);
	}}, new ValidatedInt(), new ValidatedInt());
	public ValidatedMap<Integer, Integer> background_texture_widths = new ValidatedMap<>(new HashMap<>() {{
		put(0, 182);
	}}, new ValidatedInt(), new ValidatedInt());

	public ValidatedMap<Integer, Identifier> background_texture_ids = new ValidatedMap<>(new HashMap<>() {{
		put(0, Identifier.fromNamespaceAndPath("resourcebarapi_test", "textures/gui/sprites/hud/horizontal_stamina_background.png"));
	}}, new ValidatedInt(), new ValidatedIdentifier());

	public int progress_offset_x = 0;
	public int progress_offset_y = 0;

	public ValidatedMap<Integer, Integer> progress_texture_heights = new ValidatedMap<>(new HashMap<>() {{
		put(0, 5);
	}}, new ValidatedInt(), new ValidatedInt());
	public ValidatedMap<Integer, Integer> progress_texture_widths = new ValidatedMap<>(new HashMap<>() {{
		put(0, 182);
	}}, new ValidatedInt(), new ValidatedInt());

	public ValidatedMap<Integer, Identifier> progress_decrease_animation_texture_ids = new ValidatedMap<>(new HashMap<>() {{
		put(0, Identifier.fromNamespaceAndPath("resourcebarapi_test", "textures/gui/sprites/hud/horizontal_stamina_progress_decrease_animation.png"));
	}}, new ValidatedInt(), new ValidatedIdentifier());

	public ValidatedMap<Integer, Identifier> progress_increase_animation_texture_ids = new ValidatedMap<>(new HashMap<>() {{
		put(0, Identifier.fromNamespaceAndPath("resourcebarapi_test", "textures/gui/sprites/hud/horizontal_stamina_progress_increase_animation.png"));
	}}, new ValidatedInt(), new ValidatedIdentifier());

	public ValidatedMap<Integer, Identifier> progress_increase_value_texture_ids = new ValidatedMap<>(new HashMap<>() {{
		put(0, Identifier.fromNamespaceAndPath("resourcebarapi_test", "textures/gui/sprites/hud/horizontal_stamina_progress_increase_value.png"));
	}}, new ValidatedInt(), new ValidatedIdentifier());

	public ValidatedMap<Integer, Identifier> progress_texture_ids = new ValidatedMap<>(new HashMap<>() {{
		put(0, Identifier.fromNamespaceAndPath("resourcebarapi_test", "textures/gui/sprites/hud/horizontal_stamina_progress.png"));
	}}, new ValidatedInt(), new ValidatedIdentifier());

	public int reserved_offset_x = 0;
	public int reserved_offset_y = 0;

	public ValidatedMap<Integer, Integer> reserved_texture_heights = new ValidatedMap<>(new HashMap<>() {{
		put(0, 5);
	}}, new ValidatedInt(), new ValidatedInt());
	public ValidatedMap<Integer, Integer> reserved_texture_widths = new ValidatedMap<>(new HashMap<>() {{
		put(0, 182);
	}}, new ValidatedInt(), new ValidatedInt());

	public ValidatedMap<Integer, Identifier> reserved_texture_ids = new ValidatedMap<>(new HashMap<>() {{
		put(0, Identifier.fromNamespaceAndPath("resourcebarapi_test", "textures/gui/sprites/hud/horizontal_stamina_reserved.png"));
	}}, new ValidatedInt(), new ValidatedIdentifier());

	public boolean show_current_value_overlay = false;
	public int overlay_offset_x = -2;
	public int overlay_offset_y = 0;

	public ValidatedMap<Integer, Integer> overlay_texture_heights = new ValidatedMap<>(new HashMap<>() {{
		put(0, 5);
	}}, new ValidatedInt(), new ValidatedInt());
	public ValidatedMap<Integer, Integer> overlay_texture_widths = new ValidatedMap<>(new HashMap<>() {{
		put(0, 5);
	}}, new ValidatedInt(), new ValidatedInt());

	public ValidatedMap<Integer, Identifier> overlay_texture_ids = new ValidatedMap<>(new HashMap<>() {{
		put(0, Identifier.fromNamespaceAndPath("resourcebarapi_test", "textures/gui/sprites/hud/horizontal_stamina_overlay.png"));
	}}, new ValidatedInt(), new ValidatedIdentifier());

	public boolean show_icon = false;
	public int icon_offset_x = 0;
	public int icon_offset_y = 0;

	public ValidatedMap<Integer, Integer> icon_texture_heights = new ValidatedMap<>(new HashMap<>() {{
		put(0, 16);
	}}, new ValidatedInt(), new ValidatedInt());
	public ValidatedMap<Integer, Integer> icon_texture_widths = new ValidatedMap<>(new HashMap<>() {{
		put(0, 16);
	}}, new ValidatedInt(), new ValidatedInt());

	public ValidatedMap<Integer, Identifier> icon_texture_ids = new ValidatedMap<>(new HashMap<>() {
	}, new ValidatedInt(), new ValidatedIdentifier());

	public boolean enable_smooth_animation = true;
	public int animation_interval = 1;
	public boolean max_value_change_is_animated = false;

	public boolean show_number = true;
	public boolean show_max_value = false;
	public int number_offset_x = 0;
	public int number_offset_y = -46;
	public int number_color = -6250336;
}