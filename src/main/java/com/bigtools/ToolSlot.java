package com.bigtools;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

/** A rack slot that only accepts tools. */
public class ToolSlot extends Slot {
	public ToolSlot(Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}

	@Override
	public boolean canInsert(ItemStack stack) {
		return ToolRack.isTool(stack);
	}
}
