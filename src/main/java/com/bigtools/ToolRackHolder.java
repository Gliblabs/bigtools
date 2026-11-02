package com.bigtools;

import net.minecraft.inventory.SimpleInventory;

/** Duck interface implemented onto PlayerEntity by PlayerEntityMixin. */
public interface ToolRackHolder {
	SimpleInventory bigtools_getRack();

	int bigtools_getCursor();

	void bigtools_setCursor(int cursor);
}
