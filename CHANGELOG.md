# 2.7.0

- added icon resource bars, similar to the vanilla resource bars. This first iteration does not support multiple icon types per bar yet (eg absorption hearts). This will (hopefully) be implemented at a later stage.
- several API methods were renamed and split up

# 2.6.2

- fixed some other wrong calculations

# 2.6.1

- fixed some wrong calculations

# 2.6.0

This is a pretty fundamental rewrite. The initial idea of splitting a bar into three textures per "layer" to allow for easy change of the bar length, came with the cost of massive FPS drops.
With this rewrite the bar size is no longer changeable with a simple config option. Each 'layer' consists of only 1 texture (which can have configurable dimensions).
In addition, the texture can be dynamically replaced by another texture (with configurable dimensions) depending on the max value.
The result of the rewrite is a much, MUCH more optimised API.
- removed 'is_centered' option, as it was redundant and unnecessarily complicated the calculations.
- 'fill_direction' no longer chooses between a horizontal and a vertical 'texture set', it only determines the direction from which the bar is filled. This means that changing between horizontal and vertical resource bars also requires a texture change.
- added the option to display an icon (with configurable texture id and dimensions). This can be toggled independently of the bar and the number.
- added a server config option to enable debug logs

# 2.5.0

- added API method for clearing the resource bar value cache of a given resource bar
- resource number rendering is now independent of the resource bar

# 2.4.1

- fixed reserved value display

# 2.4.0

- added dependency on Fzzy Config

# 2.3.0

- added dynamic element length based on max value (with individual values for background, progress and reserved layer)
- added dynamic element offsets based on max value
- added additional static offsets
- fixed overlay not respecting "is_centered" config option

# 2.2.0

- changed "drawGuiTexture" back to "drawTexture" method calls, texture identifiers need to reflect this

# 2.1.1

Reading can help!

# 2.1.0

- moved "ResourceBarFillDirection" and "ResourceBarOrigin" enums to main source set
- changed "drawTexture" to "drawGuiTexture" method calls, texture identifiers need to reflect this

# 2.0.0

- updated to 1.21.1

# 1.0.0

First release!

#