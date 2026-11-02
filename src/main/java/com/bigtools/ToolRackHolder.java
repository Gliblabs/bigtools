package com.bigtools;

import net.minecraft.inventory.SimpleInventory;

/** Duck interface implemented onto PlayerEntity by PlayerEntityMixin. */
public interface ToolRackHolder {
	SimpleInventory bigtools_getRack();

	int bigtools_getCursor();

	void bigtools_setCursor(int cursor);

	/** What the client was last told about this player's rack (server side only), or null if never sent. */
	ToolRackSync.State bigtools_getSyncState();

	void bigtools_setSyncState(ToolRackSync.State state);
}
