package blazingtwist.antitoolbreak.mixin;

import blazingtwist.antitoolbreak.AntiToolBreak;
import blazingtwist.antitoolbreak.config.AntiToolBreakConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

		return AntiToolBreak.isItemMaterialProtected(config, itemStack.getItem());
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
	@Inject(method = "doAttack()Z", at = @At("HEAD"), cancellable = true)
	public void onDoAttack(CallbackInfoReturnable<Boolean> info) {
		if (shouldPreventUsage(player.getInventory().getMainHandStack())) {
			info.setReturnValue(false);
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
