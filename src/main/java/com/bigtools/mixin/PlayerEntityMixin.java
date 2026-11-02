package com.bigtools.mixin;

import com.bigtools.ToolRack;
import com.bigtools.ToolRackHolder;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Gives every player a tool rack inventory that is saved with the player and dropped on death. */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements ToolRackHolder {
	@Unique
	private SimpleInventory bigtools$rack;

	@Unique
	private int bigtools$cursor;

	// Lazy: the PlayerScreenHandler is built inside PlayerEntity's constructor and asks for the rack.
	@Override
	public SimpleInventory bigtools_getRack() {
		if (this.bigtools$rack == null) {
			this.bigtools$rack = new SimpleInventory(ToolRack.SIZE);
		}
		return this.bigtools$rack;
	}

	@Override
	public int bigtools_getCursor() {
		return this.bigtools$cursor;
	}

	@Override
	public void bigtools_setCursor(int cursor) {
		this.bigtools$cursor = cursor;
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void bigtools$write(NbtCompound nbt, CallbackInfo ci) {
		nbt.put("BigToolsRack", this.bigtools_getRack().toNbtList());
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	private void bigtools$read(NbtCompound nbt, CallbackInfo ci) {
		SimpleInventory rack = this.bigtools_getRack();
		rack.clear();
		if (nbt.contains("BigToolsRack", 9)) {
			rack.readNbtList(nbt.getList("BigToolsRack", 10));
		}
	}

	@Inject(method = "dropInventory", at = @At("TAIL"))
	private void bigtools$drop(CallbackInfo ci) {
		PlayerEntity self = (PlayerEntity) (Object) this;
		if (self.getWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) return;
		SimpleInventory rack = this.bigtools_getRack();
		for (int i = 0; i < rack.size(); i++) {
			ItemStack stack = rack.removeStack(i);
			if (stack.isEmpty() || EnchantmentHelper.hasVanishingCurse(stack)) continue;
			self.dropItem(stack, true, false);
		}
	}
}
