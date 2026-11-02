package com.bigtools;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

/**
 * Keeps each client's copy of its own tool rack up to date so the HUD can draw it.
 * Once per server tick every player's rack is compared with what was last sent,
 * and a packet goes out only when something changed.
 */
public final class ToolRackSync {
	public static final Identifier SYNC = new Identifier(BigToolsMod.ID, "rack_sync");

	public static final class State {
		public final ItemStack[] stacks = new ItemStack[ToolRack.SIZE];
		public int cursor;
	}

	private ToolRackSync() {}

	public static void tick(MinecraftServer server) {
		for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
			ToolRackHolder holder = (ToolRackHolder) (Object) player;
			SimpleInventory rack = holder.bigtools_getRack();
			State last = holder.bigtools_getSyncState();
			int cursor = Math.floorMod(holder.bigtools_getCursor(), ToolRack.SIZE);

			boolean changed = last == null || last.cursor != cursor;
			if (!changed) {
				for (int i = 0; i < ToolRack.SIZE; i++) {
					if (!ItemStack.areEqual(last.stacks[i], rack.getStack(i))) {
						changed = true;
						break;
					}
				}
			}
			if (!changed) continue;

			State now = new State();
			now.cursor = cursor;
			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeInt(cursor);
			for (int i = 0; i < ToolRack.SIZE; i++) {
				now.stacks[i] = rack.getStack(i).copy();
				buf.writeItemStack(now.stacks[i]);
			}
			holder.bigtools_setSyncState(now);
			ServerPlayNetworking.send(player, SYNC, buf);
		}
	}
}
