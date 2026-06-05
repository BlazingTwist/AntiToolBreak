# Anti Tool Break

A Minecraft [Fabric](https://fabricmc.net/) mod stops you from breaking your tools.  
Built for version 26.1

[Curseforge release](https://www.curseforge.com/minecraft/mc-mods/anti-tool-break-fabric)  
[Modrinth release](https://modrinth.com/mod/anti-tool-break)

# Dependencies

Requires [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api), [Cloth Config](https://www.curseforge.com/minecraft/mc-mods/cloth-config) and optionally [Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu)

For easy installation, check the [supported versions](https://github.com/BlazingTwist/AntiToolBreak/wiki/Tested-Versions).

# Configuration
The configuration allows you to adjust the following options:
* Min-Durability to start preventing tool usage
* Bypass break prevention by sneaking
* Filters - these are opt-in, if an item matches any of the enabled filters ATB will stop you from breaking it
  * Non-Enchanted (any item without enchants)
  * Enchanted (any item with enchants)
  * Material `[Wood, Stone, Copper, Iron, Gold, Diamond, Netherite]` (any tool of that material)
  * Other (any item that does not belong to any of the materials above (e.g. `flint_and_steel`))

# Contribute

**Issues** - Feature requests and bug reports are welcome.

**Pull requests** - You fixed an issue or added a feature? Very cool, thank you!

[**Kofi**](https://ko-fi.com/blazingtwist0016) - My work is free for everyone, so if you want to help me pay my bills (and can afford it),
you can support me directly here.