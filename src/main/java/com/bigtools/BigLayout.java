package com.bigtools;

/**
 * Shared layout + slot-id map for the big player screen handler.
 *
 * Slot ids:
 *   0        crafting result
 *   1-4      crafting grid
 *   5-8      armor
 *   9-35     inventory indices 9-35        (vanilla main storage)
 *   36-44    inventory indices 0-8         (hotbar, the only keys 1-9 can reach)
 *   45       offhand
 *   46-74    inventory indices 36-64       (added by this mod)
 *   75-82    tool rack
 *
 * Inventory indices 9-60 are drawn as a 13x4 storage block, indices 0-8 plus 61-64
 * are drawn as the 13-wide bottom row (only the first 9 are the real hotbar).
 */
public final class BigLayout {
	public static final int WIDTH = 250;
	public static final int HEIGHT = 200;
	public static final int COLS = 13;

	public static final int INV_SIZE = 65;
	/** Armor/offhand inventory indices move from 36.. to 65.. when main grows from 36 to 65. */
	public static final int EXTRA_SHIFT = INV_SIZE - 36;

	public static final int TOOL_FIRST = 75;
	public static final int TOOL_COUNT = ToolRack.SIZE;

	private BigLayout() {}

	public static int invIndex(int id) {
		if (id >= 46) return id - 10;
		if (id >= 36 && id <= 44) return id - 36;
		return id;
	}

	public static int[] position(int id) {
		if (id == 0) return new int[]{154, 28};
		if (id <= 4) {
			int n = id - 1;
			return new int[]{98 + (n % 2) * 18, 18 + (n / 2) * 18};
		}
		if (id <= 8) return new int[]{8, 8 + (id - 5) * 18};
		if (id == 45) return new int[]{77, 62};
		if (id >= TOOL_FIRST) {
			int t = id - TOOL_FIRST;
			return new int[]{192 + (t % 2) * 18, 18 + (t / 2) * 18};
		}
		int idx = invIndex(id);
		if (idx <= 8) return new int[]{8 + idx * 18, 174};
		if (idx >= 61) return new int[]{8 + (9 + idx - 61) * 18, 174};
		int n = idx - 9;
		return new int[]{8 + (n % COLS) * 18, 98 + (n / COLS) * 18};
	}
}
