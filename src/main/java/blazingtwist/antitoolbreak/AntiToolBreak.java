package blazingtwist.antitoolbreak;

import blazingtwist.antitoolbreak.config.AntiToolBreakConfig;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class AntiToolBreak implements ModInitializer {
	private static final Item[] woodenItems = {
			Items.WOODEN_AXE,
			Items.WOODEN_HOE,
			Items.WOODEN_PICKAXE,
			Items.WOODEN_SHOVEL,
			Items.WOODEN_SWORD
	};

	private static final Item[] stoneItems = {
			Items.STONE_AXE,
			Items.STONE_HOE,
			Items.STONE_PICKAXE,
			Items.STONE_SHOVEL,
			Items.STONE_SWORD
	};

	private static final Item[] ironItems = {
			Items.IRON_AXE,
			Items.IRON_HOE,
			Items.IRON_PICKAXE,
			Items.IRON_SHOVEL,
			Items.IRON_SWORD
	};

	private static final Item[] goldItems = {
			Items.GOLDEN_AXE,
			Items.GOLDEN_HOE,
			Items.GOLDEN_PICKAXE,
			Items.GOLDEN_SHOVEL,
			Items.GOLDEN_SWORD
	};

	private static final Item[] diamondItems = {
			Items.DIAMOND_AXE,
			Items.DIAMOND_HOE,
			Items.DIAMOND_PICKAXE,
			Items.DIAMOND_SHOVEL,
			Items.DIAMOND_SWORD
	};

	private static final Item[] netheriteItems = {
			Items.NETHERITE_AXE,
			Items.NETHERITE_HOE,
			Items.NETHERITE_PICKAXE,
			Items.NETHERITE_SHOVEL,
			Items.NETHERITE_SWORD
	};

	private static ConfigHolder<AntiToolBreakConfig> configHolder;
	private static HashMap<ATB_ToolMaterial, List<Integer>> materialRawIDs = null;
	private static int lastCheckedID = -1;
	private static boolean lastCheckedStatus = false;

	public static AntiToolBreakConfig getConfig() {
		return configHolder.getConfig();
	}

	/**
	 * Caches the outcome of AntiToolBreakConfig::isMaterialProtected.
	 *
	 * @param config config-instance
	 * @param item   the item whose material should be checked
	 * @return true if ATB is configured to protect the given Item Material
	 */
	public static boolean isItemMaterialProtected(AntiToolBreakConfig config, Item item) {
		int rawId = Item.getRawId(item);
		if (!isLastCheckedID(rawId)) {
			ATB_ToolMaterial itemMaterial = AntiToolBreak.findItemMaterial(rawId);
			boolean materialProtected = config.isMaterialProtected(itemMaterial);
			AntiToolBreak.setLastCheckedID(rawId, materialProtected);
		}
		return getLastCheckedStatus();
	}

	private static HashMap<ATB_ToolMaterial, List<Integer>> getMaterialRawIDs() {
		if (materialRawIDs == null) {
			materialRawIDs = new HashMap<>();
			materialRawIDs.put(ATB_ToolMaterial.Wood, Arrays.stream(woodenItems).map(Item::getRawId).collect(Collectors.toList()));
			materialRawIDs.put(ATB_ToolMaterial.Stone, Arrays.stream(stoneItems).map(Item::getRawId).collect(Collectors.toList()));
			materialRawIDs.put(ATB_ToolMaterial.Iron, Arrays.stream(ironItems).map(Item::getRawId).collect(Collectors.toList()));
			materialRawIDs.put(ATB_ToolMaterial.Gold, Arrays.stream(goldItems).map(Item::getRawId).collect(Collectors.toList()));
			materialRawIDs.put(ATB_ToolMaterial.Diamond, Arrays.stream(diamondItems).map(Item::getRawId).collect(Collectors.toList()));
			materialRawIDs.put(ATB_ToolMaterial.Netherite, Arrays.stream(netheriteItems).map(Item::getRawId).collect(Collectors.toList()));
		}
		return materialRawIDs;
	}

	private static ATB_ToolMaterial findItemMaterial(int rawID) {
		return AntiToolBreak.getMaterialRawIDs().entrySet().stream()
				.filter(materialEntry -> materialEntry.getValue().contains(rawID))
				.findFirst()
				.map(Map.Entry::getKey).orElse(null);
	}

	private static void setLastCheckedID(int rawID, boolean status) {
		lastCheckedID = rawID;
		lastCheckedStatus = status;
	}

	private static boolean isLastCheckedID(int rawID) {
		return lastCheckedID >= 0 && lastCheckedID == rawID;
	}

	private static boolean getLastCheckedStatus() {
		return lastCheckedStatus;
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		AutoConfig.register(AntiToolBreakConfig.class, GsonConfigSerializer::new);

		configHolder = AutoConfig.getConfigHolder(AntiToolBreakConfig.class);
	}
}
