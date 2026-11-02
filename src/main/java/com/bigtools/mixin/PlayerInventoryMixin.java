package com.bigtools.mixin;

import com.bigtools.BigLayout;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/** Grows the main inventory list from 36 to 65 slots. Armor/offhand keep their own lists. */
@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {
	@Mutable
	@Shadow
	@Final
	public DefaultedList<ItemStack> main;

	@Mutable
	@Shadow
	@Final
	private List<DefaultedList<ItemStack>> combinedInventory;

	@Shadow
	@Final
	public DefaultedList<ItemStack> armor;

	@Shadow
	@Final
	public DefaultedList<ItemStack> offHand;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void bigtools$resize(PlayerEntity player, CallbackInfo ci) {
		this.main = DefaultedList.ofSize(BigLayout.INV_SIZE, ItemStack.EMPTY);
		this.combinedInventory = ImmutableList.of(this.main, this.armor, this.offHand);
	}
}
