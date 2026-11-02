package com.bigtools.client;

import com.bigtools.BigLayout;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

/**
 * Wide vanilla-looking player screen. Everything is drawn with filled rects (no texture asset)
 * so the bevels match the vanilla inventory panel and slot style.
 */
public class BigInventoryScreen extends HandledScreen<PlayerScreenHandler> {
	public BigInventoryScreen(PlayerEntity player) {
		super(player.playerScreenHandler, player.getInventory(), Text.translatable("container.crafting"));
		this.backgroundWidth = BigLayout.WIDTH;
		this.backgroundHeight = BigLayout.HEIGHT;
	}

	@Override
	protected void drawForeground(DrawContext ctx, int mouseX, int mouseY) {
		ctx.drawText(this.textRenderer, Text.translatable("container.crafting"), 98, 6, 0x404040, false);
		ctx.drawText(this.textRenderer, Text.translatable("gui.bigtools.tools"), 192, 6, 0x404040, false);
	}

	@Override
	protected void drawBackground(DrawContext ctx, float delta, int mouseX, int mouseY) {
		int x = this.x;
		int y = this.y;
		int w = this.backgroundWidth;
		int h = this.backgroundHeight;

		// panel: black outline, white top-left bevel, dark bottom-right bevel, gray face
		ctx.fill(x, y, x + w, y + h, 0xFF000000);
		ctx.fill(x + 1, y + 1, x + w - 2, y + h - 2, 0xFFFFFFFF);
		ctx.fill(x + 3, y + 3, x + w - 1, y + h - 1, 0xFF555555);
		ctx.fill(x + 3, y + 3, x + w - 3, y + h - 3, 0xFFC6C6C6);

		// slot frames
		for (Slot slot : this.handler.slots) {
			int sx = x + slot.x - 1;
			int sy = y + slot.y - 1;
			ctx.fill(sx, sy, sx + 18, sy + 18, 0xFFFFFFFF);
			ctx.fill(sx, sy, sx + 17, sy + 17, 0xFF373737);
			ctx.fill(sx + 1, sy + 1, sx + 17, sy + 17, 0xFF8B8B8B);
		}

		// player preview box
		int px = x + 26;
		int py = y + 8;
		ctx.fill(px - 1, py - 1, px + 52, py + 72, 0xFFFFFFFF);
		ctx.fill(px - 1, py - 1, px + 51, py + 71, 0xFF373737);
		ctx.fill(px, py, px + 51, py + 71, 0xFF000000);
		if (this.client != null && this.client.player != null) {
			InventoryScreen.drawEntity(ctx, x + 51, y + 75, 30,
				(float) (x + 51) - mouseX, (float) (y + 75 - 50) - mouseY, this.client.player);
		}

		// crafting arrow
		int ax = x + 134;
		int ay = y + 33;
		ctx.fill(ax, ay + 3, ax + 12, ay + 9, 0xFF8B8B8B);
		for (int i = 0; i < 6; i++) {
			ctx.fill(ax + 12 + i, ay + i, ax + 13 + i, ay + 12 - i, 0xFF8B8B8B);
		}
	}

	@Override
	public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
		super.render(ctx, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(ctx, mouseX, mouseY);
	}
}
