# 1.6.0

This is a pretty fundamental rewrite. The initial idea of splitting a bar into three textures per "layer" to allow for easy change of the bar length, came with the cost of massive FPS drops.
With this rewrite the bar size is no longer changeable with a simple config option. Each 'layer' consists of only 1 texture (which can have configurable dimensions).
In addition, the texture can be dynamically replaced by another texture (with configurable dimensions) depending on the max value.
The result of the rewrite is a much, MUCH more optimised API.
- removed 'is_centered' option, as it was redundant and unnecessarily complicated the calculations.
- 'fill_direction' no longer chooses between a horizontal and a vertical 'texture set', it only determines the direction from which the bar is filled. This means that changing between horizontal and vertical resource bars also requires a texture change.
- added the option to display an icon (with configurable texture id and dimensions). This can be toggled independently of the bar and the number.
- added a server config option to enable debug logs

# 1.5.0

- added API method for clearing the resource bar value cache of a given resource bar
- resource number rendering is now independent of the resource bar

# 1.4.1

- fixed reserved value display

# 1.4.0

- added dependency on Fzzy Config

# 1.3.0

- added dynamic element length based on max value (with individual values for background, progress and reserved layer)
- added dynamic element offsets based on max value
- added additional static offsets
- fixed overlay not respecting "is_centered" config option

# 1.2.0

- fixed a bunch of issues

# 1.1.0

- moved "ResourceBarFillDirection" and "ResourceBarOrigin" enums to main source set

# 1.0.0

First release!

#