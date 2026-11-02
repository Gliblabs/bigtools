package com.bigtools;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BowItem;
import net.minecraft.item.BrushItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.TridentItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

public final class ToolRack {
	public static final int SIZE = 6;

	private ToolRack() {}

	/** Pickaxes, axes, shovels, hoes, swords (all ToolItem) plus a few other tool-like items. */
	public static boolean isTool(ItemStack stack) {
		Item i = stack.getItem();
		return i instanceof ToolItem
			|| i instanceof ShearsItem
			|| i instanceof FishingRodItem
			|| i instanceof BowItem
			|| i instanceof CrossbowItem
			|| i instanceof TridentItem
			|| i instanceof FlintAndSteelItem
			|| i instanceof BrushItem;
	}

	/** Swap the main-hand item with the next non-empty rack slot, cycling through the rack. */
	public static void cycle(ServerPlayerEntity player) {
		ToolRackHolder holder = (ToolRackHolder) (Object) player;
		SimpleInventory rack = holder.bigtools_getRack();
		ItemStack held = player.getMainHandStack();
		if (!held.isEmpty() && !isTool(held)) {
			player.sendMessage(Text.translatable("message.bigtools.hold_tool"), true);
			return;
		}
		int start = Math.floorMod(holder.bigtools_getCursor(), SIZE);
		for (int k = 0; k < SIZE; k++) {
			int i = (start + k) % SIZE;
			ItemStack inRack = rack.getStack(i);
			if (inRack.isEmpty()) continue;
			ItemStack take = inRack.copy();
			rack.setStack(i, held.copy());
			player.setStackInHand(Hand.MAIN_HAND, take);
			holder.bigtools_setCursor((i + 1) % SIZE);
			return;
		}
		player.sendMessage(Text.translatable("message.bigtools.empty_rack"), true);
	}

	/** Put the held tool into the first empty rack slot. */
	public static void stow(ServerPlayerEntity player) {
		ToolRackHolder holder = (ToolRackHolder) (Object) player;
		SimpleInventory rack = holder.bigtools_getRack();
		ItemStack held = player.getMainHandStack();
		if (held.isEmpty() || !isTool(held)) {
			player.sendMessage(Text.translatable("message.bigtools.hold_tool"), true);
			return;
		}
		for (int i = 0; i < SIZE; i++) {
			if (rack.getStack(i).isEmpty()) {
				rack.setStack(i, held.copy());
				player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
				return;
			}
		}
		player.sendMessage(Text.translatable("message.bigtools.rack_full"), true);
	}
}
