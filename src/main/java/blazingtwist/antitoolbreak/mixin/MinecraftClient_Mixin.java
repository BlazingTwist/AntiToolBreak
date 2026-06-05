package blazingtwist.antitoolbreak.mixin;

import blazingtwist.antitoolbreak.AntiToolBreak;
import blazingtwist.antitoolbreak.config.AntiToolBreakConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public abstract class MinecraftClient_Mixin {

	@Shadow
	public MultiPlayerGameMode gameMode;

	@Shadow
	public LocalPlayer player;

	@Unique
	private boolean shouldPreventUsage(ItemStack itemStack) {
		AntiToolBreakConfig config = AntiToolBreak.getConfig();
		if (config.sneakToBypass && player.isCrouching()) {
			return false;
		}

		if (!itemStack.isDamageableItem()) {
			return false;
		}

		int durability = itemStack.getMaxDamage() - itemStack.getDamageValue();
		if (durability > config.triggerDurability) {
			return false;
		}

		boolean isEnchanted = itemStack.isEnchanted();
		if (isEnchanted && config.triggerFilter_enchanted || !isEnchanted && config.triggerFilter_noEnchant) {
			return true;
		}

		return AntiToolBreak.isItemMaterialProtected(config, itemStack.getItem());
	}

	@Inject(method = "continueAttack(Z)V", at = @At("HEAD"), cancellable = true)
	public void onHandleBlockBreaking(boolean isBreakPressed, CallbackInfo info) {
		if (isBreakPressed && shouldPreventUsage(player.getInventory().getSelectedItem())) {
			gameMode.stopDestroyBlock();
			info.cancel();
		}
	}

	@Inject(method = "startAttack()Z", at = @At("HEAD"), cancellable = true)
	public void onDoAttack(CallbackInfoReturnable<Boolean> info) {
		if (shouldPreventUsage(player.getInventory().getSelectedItem())) {
			info.setReturnValue(false);
			info.cancel();
		}
	}

	@Inject(method = "startUseItem()V", at = @At("HEAD"), cancellable = true)
	public void onDoItemUse(CallbackInfo info) {
		if (shouldPreventUsage(player.getInventory().getSelectedItem())) {
			info.cancel();
		}
	}
}
