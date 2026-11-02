package com.bigtools.mixin;

import com.bigtools.client.BigInventoryScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** Swap the vanilla survival inventory screen for the wide one. */
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
	@Shadow
	public ClientPlayerEntity player;

	@ModifyVariable(method = "setScreen", at = @At("HEAD"), argsOnly = true)
	private Screen bigtools$replaceInventoryScreen(Screen screen) {
		if (screen != null && screen.getClass() == InventoryScreen.class && this.player != null) {
			return new BigInventoryScreen(this.player);
		}
		return screen;
	}
}
