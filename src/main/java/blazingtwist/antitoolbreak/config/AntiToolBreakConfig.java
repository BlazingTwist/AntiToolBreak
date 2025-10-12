package blazingtwist.antitoolbreak.config;

import blazingtwist.antitoolbreak.ATB_ToolMaterial;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "antitoolbreak")
public class AntiToolBreakConfig implements ConfigData {
	@ConfigEntry.Category("category.antitoolbreak")
	public int triggerDurability = 5;
	@ConfigEntry.Category("category.antitoolbreak")
	public boolean sneakToBypass = false;

	@ConfigEntry.Category("category.antitoolbreak")
	public boolean triggerFilter_noEnchant = false;
	@ConfigEntry.Category("category.antitoolbreak")
	public boolean triggerFilter_enchanted = true;

	@ConfigEntry.Category("category.antitoolbreak")
	public boolean triggerFilter_wood = false;
	@ConfigEntry.Category("category.antitoolbreak")
	public boolean triggerFilter_stone = false;
	@ConfigEntry.Category("category.antitoolbreak")
	public boolean triggerFilter_copper = false;
	@ConfigEntry.Category("category.antitoolbreak")
	public boolean triggerFilter_iron = false;
	@ConfigEntry.Category("category.antitoolbreak")
	public boolean triggerFilter_gold = true;
	@ConfigEntry.Category("category.antitoolbreak")
	public boolean triggerFilter_diamond = true;
	@ConfigEntry.Category("category.antitoolbreak")
	public boolean triggerFilter_netherite = true;

	@ConfigEntry.Category("category.antitoolbreak")
	public boolean triggerFilter_other = false;

	public boolean isMaterialProtected(ATB_ToolMaterial material) {
		if (material == null) {
			return triggerFilter_other;
		}

		return switch (material) {
			case Wood -> triggerFilter_wood;
			case Stone -> triggerFilter_stone;
			case Copper -> triggerFilter_copper;
			case Iron -> triggerFilter_iron;
			case Gold -> triggerFilter_gold;
			case Diamond -> triggerFilter_diamond;
			case Netherite -> triggerFilter_netherite;
		};
	}
}
