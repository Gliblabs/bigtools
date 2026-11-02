package com.bigtools;

/**
 * Shared layout + slot-id map for the big player screen handler.
 *
 * Slot ids:
 *   0        crafting result
 *   1-4      crafting grid
 *   5-8      armor
 *   9-35     inventory indices 9-35        (vanilla main storage)
 *   36-44    inventory indices 0-8         (the 9-slot hotbar, same as vanilla)
 *   45       offhand
 *   46-70    inventory indices 36-60       (added by this mod)
 *   71-78    tool rack
 *
 * Inventory indices 9-60 are drawn as a 13x4 storage block; indices 0-8 are the
 * normal 9-slot hotbar, centered under it.
 */
public final class BigLayout {
	public static final int WIDTH = 250;
	public static final int HEIGHT = 206;
	public static final int COLS = 13;

	public static final int INV_SIZE = 9 + COLS * 4; // 61
	/** Armor/offhand inventory indices move from 36.. to 61.. when main grows from 36 to 61. */
	public static final int EXTRA_SHIFT = INV_SIZE - 36;

	public static final int EXTRA_FIRST_ID = 46;
	public static final int TOOL_FIRST = EXTRA_FIRST_ID + (INV_SIZE - 36); // 71
	public static final int TOOL_COUNT = ToolRack.SIZE;

	private static final int STORAGE_Y = 104;
	private static final int HOTBAR_Y = STORAGE_Y + 4 * 18 + 4; // 180
	private static final int HOTBAR_X = 8 + ((COLS - 9) * 18) / 2; // centered under storage

	private BigLayout() {}

	public static int invIndex(int id) {
		if (id >= EXTRA_FIRST_ID) return id - 10;
		if (id >= 36 && id <= 44) return id - 36;
		return id;
	}

	public static int[] position(int id) {
		if (id == 0) return new int[]{170, 29};
		if (id <= 4) {
			int n = id - 1;
			return new int[]{108 + (n % 2) * 18, 20 + (n / 2) * 18};
		}
		if (id <= 8) return new int[]{8, 8 + (id - 5) * 18};
		if (id == 45) return new int[]{77, 62};
		if (id >= TOOL_FIRST) {
			int t = id - TOOL_FIRST;
			return new int[]{206 + (t % 2) * 18, 20 + (t / 2) * 18};
		}
		int idx = invIndex(id);
		if (idx <= 8) return new int[]{HOTBAR_X + idx * 18, HOTBAR_Y};
		int n = idx - 9;
		return new int[]{8 + (n % COLS) * 18, STORAGE_Y + (n / COLS) * 18};
	}
}
