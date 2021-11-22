package blazingtwist.antitoolbreak.mixin;

import blazingtwist.antitoolbreak.ATB_ToolMaterial;
import blazingtwist.antitoolbreak.AntiToolBreak;
import blazingtwist.antitoolbreak.config.AntiToolBreakConfig;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class MinecraftClient_Mixin {

	@Shadow
	public ClientPlayerInteractionManager interactionManager;

	@Shadow
	public ClientPlayerEntity player;

	private boolean shouldPreventUsage(ItemStack itemStack) {
		AntiToolBreakConfig config = AntiToolBreak.getConfig();
		if (config.sneakToBypass && player.isSneaking()) {
			return false;
		}

		if (!itemStack.isDamageable()) {
			return false;
		}

		int durability = itemStack.getMaxDamage() - itemStack.getDamage();
		if (durability > config.triggerDurability) {
			return false;
		}

		boolean isEnchanted = itemStack.hasEnchantments();
		if (isEnchanted && config.triggerFilter_enchanted || !isEnchanted && config.triggerFilter_noEnchant) {
			return true;
		}

		int rawId = Item.getRawId(itemStack.getItem());
		boolean materialMatch;
		// Try to do material checking only once
		if(!AntiToolBreak.isLastCheckedID(rawId)){
			ATB_ToolMaterial material = null;
			for (Map.Entry<ATB_ToolMaterial, List<Integer>> materialEntry : AntiToolBreak.getMaterialRawIDs().entrySet()) {
				if(materialEntry.getValue().contains(rawId)){
					material = materialEntry.getKey();
					break;
				}
			}

			materialMatch = config.triggerFilter_other && material == null
					|| config.triggerFilter_wood && material == ATB_ToolMaterial.Wood
					|| config.triggerFilter_stone && material == ATB_ToolMaterial.Stone
					|| config.triggerFilter_iron && material == ATB_ToolMaterial.Iron
					|| config.triggerFilter_gold && material == ATB_ToolMaterial.Gold
					|| config.triggerFilter_diamond && material == ATB_ToolMaterial.Diamond
					|| config.triggerFilter_netherite && material == ATB_ToolMaterial.Netherite;
			AntiToolBreak.setLastCheckedID(rawId, materialMatch);
		}else{
			materialMatch = AntiToolBreak.getLastCheckedStatus(rawId);
		}

		return materialMatch;
	}

	// private void handleBlockBreaking(boolean bl) {
	@Inject(method = "handleBlockBreaking(Z)V", at = @At("HEAD"), cancellable = true)
	public void onHandleBlockBreaking(boolean isBreakPressed, CallbackInfo info) {
		if (isBreakPressed && shouldPreventUsage(player.getInventory().getMainHandStack())) {
			interactionManager.cancelBlockBreaking();
			info.cancel();
		}
	}

	//private void doAttack() {
	@Inject(method = "doAttack()V", at = @At("HEAD"), cancellable = true)
	public void onDoAttack(CallbackInfo info) {
		if (shouldPreventUsage(player.getInventory().getMainHandStack())) {
			info.cancel();
		}
	}

	//private void doItemUse() {
	@Inject(method = "doItemUse()V", at = @At("HEAD"), cancellable = true)
	public void onDoItemUse(CallbackInfo info) {
		if (shouldPreventUsage(player.getInventory().getMainHandStack())) {
			info.cancel();
		}
	}
}
