package com.bigtools.client;

import com.bigtools.BigLayout;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

/**
 * Wide vanilla-looking player screen. It extends the vanilla InventoryScreen so the recipe book,
 * the creative-mode switch and the status effect display all keep working; only the size, the
 * background and the labels are replaced. Everything is drawn with filled rects (no texture asset).
 */
public class BigInventoryScreen extends InventoryScreen {
	private float lookX;
	private float lookY;

	public BigInventoryScreen(PlayerEntity player) {
		super(player);
		this.backgroundWidth = BigLayout.WIDTH;
		this.backgroundHeight = BigLayout.HEIGHT;
	}

	@Override
	public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
		this.lookX = mouseX;
		this.lookY = mouseY;
		super.render(ctx, mouseX, mouseY, delta);
	}

	@Override
	protected void drawForeground(DrawContext ctx, int mouseX, int mouseY) {
		ctx.drawText(this.textRenderer, Text.translatable("container.crafting"), 108, 8, 0x404040, false);
		ctx.drawText(this.textRenderer, Text.translatable("gui.bigtools.tools"), 206, 8, 0x404040, false);
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

		// player preview box (same size and look as vanilla)
		int px = x + 26;
		int py = y + 8;
		ctx.fill(px - 1, py - 1, px + 52, py + 72, 0xFFFFFFFF);
		ctx.fill(px - 1, py - 1, px + 51, py + 71, 0xFF373737);
		ctx.fill(px, py, px + 51, py + 71, 0xFF000000);
		if (this.client != null && this.client.player != null) {
			InventoryScreen.drawEntity(ctx, x + 51, y + 75, 30,
				(float) (x + 51) - this.lookX, (float) (y + 75 - 50) - this.lookY, this.client.player);
		}

		// crafting arrow, centered between the 2x2 grid and the result slot
		int ax = x + 148;
		int ay = y + 32;
		ctx.fill(ax, ay + 3, ax + 10, ay + 9, 0xFF8B8B8B);
		for (int i = 0; i < 6; i++) {
			ctx.fill(ax + 10 + i, ay + i, ax + 11 + i, ay + 12 - i, 0xFF8B8B8B);
		}
	}
}
