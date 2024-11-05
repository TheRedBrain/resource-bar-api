# Resource Bar API

A small API mod that allows mods to easily set up a complete resource bar HUD element.

This can be for example a mana bar.

This API does not do all the work. All this API does is provide a method that draws the HUD element according to the given parameters.

It does not decide under which conditions the element should be rendered. That is the job of the mod using this API.
This decision was made to ensure maximum flexibility when using this API.