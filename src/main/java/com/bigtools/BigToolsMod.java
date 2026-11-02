package com.bigtools;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class BigToolsMod implements ModInitializer {
	public static final String ID = "bigtools";
	public static final Identifier TOOL_ACTION = new Identifier(ID, "tool_action");

	@Override
	public void onInitialize() {
		ServerPlayNetworking.registerGlobalReceiver(TOOL_ACTION, (server, player, handler, buf, responseSender) -> {
			byte action = buf.readByte();
			server.execute(() -> {
				if (action == 0) ToolRack.cycle(player);
				else if (action == 1) ToolRack.stow(player);
			});
		});
	}
}
