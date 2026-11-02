package com.bigtools.client;

import com.bigtools.ToolRack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;

import java.util.Arrays;

/**
 * In-game HUD: a vertical column of the 6 tool rack slots next to the hotbar, on the side
 * opposite the offhand slot's neighbor (right side for right-handed players). The slot that
 * the "equip next tool" key will pick is outlined. Nothing is drawn while the rack is empty.
 */
public final class ToolRackHud {
	public static volatile ItemStack[] stacks = empty();
	public static volatile int cursor;

	private ToolRackHud() {}

	private static ItemStack[] empty() {
		ItemStack[] a = new ItemStack[ToolRack.SIZE];
		Arrays.fill(a, ItemStack.EMPTY);
		return a;
	}

	public static void clear() {
		stacks = empty();
		cursor = 0;
	}

	public static void render(DrawContext ctx, float tickDelta) {
		MinecraftClient mc = MinecraftClient.getInstance();
		if (mc.player == null || mc.options.hudHidden || mc.player.isSpectator()) return;

		ItemStack[] st = stacks;
		boolean any = false;
		for (ItemStack s : st) {
			if (!s.isEmpty()) {
				any = true;
				break;
			}
		}
		if (!any) return;

		int w = ctx.getScaledWindowWidth();
		int h = ctx.getScaledWindowHeight();
		boolean rightHanded = mc.options.getMainArm().getValue() == Arm.RIGHT;
		int x = rightHanded ? w / 2 + 91 + 30 : w / 2 - 91 - 30 - 20;

		int next = -1;
		for (int k = 0; k < ToolRack.SIZE; k++) {
			int i = (cursor + k) % ToolRack.SIZE;
			if (!st[i].isEmpty()) {
				next = i;
				break;
			}
		}

		for (int i = 0; i < ToolRack.SIZE; i++) {
			int y = h - 21 - (ToolRack.SIZE - 1 - i) * 22;
			ctx.fill(x, y, x + 20, y + 20, 0xCC000000);
			ctx.fill(x + 1, y + 1, x + 19, y + 19, 0x99555555);
			if (i == next) {
				ctx.fill(x - 1, y - 1, x + 21, y, 0xFFFFFFFF);
				ctx.fill(x - 1, y + 20, x + 21, y + 21, 0xFFFFFFFF);
				ctx.fill(x - 1, y, x, y + 20, 0xFFFFFFFF);
				ctx.fill(x + 20, y, x + 21, y + 20, 0xFFFFFFFF);
			}
			ItemStack s = st[i];
			if (!s.isEmpty()) {
				ctx.drawItem(s, x + 2, y + 2);
				ctx.drawItemInSlot(mc.textRenderer, s, x + 2, y + 2);
			}
		}
	}
}
