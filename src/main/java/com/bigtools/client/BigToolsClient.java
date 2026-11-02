package com.bigtools.client;

import com.bigtools.BigToolsMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import org.lwjgl.glfw.GLFW;

public class BigToolsClient implements ClientModInitializer {
	private static KeyBinding cycleKey;
	private static KeyBinding stowKey;

	@Override
	public void onInitializeClient() {
		cycleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.bigtools.cycle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, "key.categories.bigtools"));
		stowKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.bigtools.stow", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B, "key.categories.bigtools"));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			boolean canSend = client.player != null && client.currentScreen == null;
			while (cycleKey.wasPressed()) {
				if (canSend) send((byte) 0);
			}
			while (stowKey.wasPressed()) {
				if (canSend) send((byte) 1);
			}
		});
	}

	private static void send(byte action) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeByte(action);
		ClientPlayNetworking.send(BigToolsMod.TOOL_ACTION, buf);
	}
}
