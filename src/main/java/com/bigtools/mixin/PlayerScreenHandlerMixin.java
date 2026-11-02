package com.bigtools.mixin;

import com.bigtools.BigLayout;
import com.bigtools.ToolRack;
import com.bigtools.ToolRackHolder;
import com.bigtools.ToolSlot;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Re-maps the vanilla player screen handler: armor/offhand slots point at their new inventory
 * indices, 29 extra inventory slots and the tool rack slots are appended, and everything is
 * repositioned for the wide layout. Shift-click is rewritten for the new slot ids.
 */
@Mixin(PlayerScreenHandler.class)
public abstract class PlayerScreenHandlerMixin extends ScreenHandler {
	protected PlayerScreenHandlerMixin(ScreenHandlerType<?> type, int syncId) {
		super(type, syncId);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void bigtools$init(PlayerInventory inventory, boolean onServer, PlayerEntity owner, CallbackInfo ci) {
		// armor (ids 5-8) and offhand (id 45): inventory index shifts because main is now 65 long
		for (int id : new int[]{5, 6, 7, 8, 45}) {
			Slot slot = this.slots.get(id);
			((SlotAccessor) slot).bigtools$setIndex(slot.getIndex() + BigLayout.EXTRA_SHIFT);
		}

		// extra inventory slots: ids 46-74 = inventory indices 36-64
		for (int idx = 36; idx < BigLayout.INV_SIZE; idx++) {
			this.addSlot(new Slot(inventory, idx, 0, 0));
		}

		// tool rack: ids 75-82
		SimpleInventory rack = ((ToolRackHolder) (Object) owner).bigtools_getRack();
		for (int i = 0; i < ToolRack.SIZE; i++) {
			this.addSlot(new ToolSlot(rack, i, 0, 0));
		}

		for (Slot slot : this.slots) {
			int[] p = BigLayout.position(slot.id);
			SlotAccessor acc = (SlotAccessor) slot;
			acc.bigtools$setX(p[0]);
			acc.bigtools$setY(p[1]);
		}
	}

	@Inject(method = "quickMove", at = @At("HEAD"), cancellable = true)
	private void bigtools$quickMove(PlayerEntity player, int index, CallbackInfoReturnable<ItemStack> cir) {
		Slot slot = this.slots.get(index);
		if (slot == null || !slot.hasStack()) {
			cir.setReturnValue(ItemStack.EMPTY);
			return;
		}
		ItemStack stack = slot.getStack();
		ItemStack original = stack.copy();

		boolean inStorage = (index >= 9 && index <= 35) || (index >= 46 && index <= 70);
		boolean inHotRow = (index >= 36 && index <= 44) || (index >= 71 && index <= 74);
		boolean moved;

		if (index == 0) {
			moved = this.bigtools$toInventory(stack, true);
			if (moved) slot.onQuickTransfer(stack, original);
		} else if (!inStorage && !inHotRow) {
			// crafting grid, armor, offhand, tool rack -> inventory
			moved = this.bigtools$toInventory(stack, false);
		} else {
			moved = false;
			EquipmentSlot equip = MobEntity.getPreferredEquipmentSlot(stack);
			if (equip.getType() == EquipmentSlot.Type.ARMOR && !this.slots.get(8 - equip.getEntitySlotId()).hasStack()) {
				int armorId = 8 - equip.getEntitySlotId();
				moved = this.insertItem(stack, armorId, armorId + 1, false);
			}
			if (!moved && ToolRack.isTool(stack)) {
				moved = this.insertItem(stack, BigLayout.TOOL_FIRST, BigLayout.TOOL_FIRST + ToolRack.SIZE, false);
			}
			if (!moved && equip == EquipmentSlot.OFFHAND && !this.slots.get(45).hasStack()) {
				moved = this.insertItem(stack, 45, 46, false);
			}
			if (!moved) {
				if (inStorage) {
					boolean a = this.insertItem(stack, 36, 45, false);
					boolean b = this.insertItem(stack, 71, 75, false);
					moved = a || b;
				} else {
					boolean a = this.insertItem(stack, 9, 36, false);
					boolean b = this.insertItem(stack, 46, 71, false);
					moved = a || b;
				}
			}
		}

		if (!moved) {
			cir.setReturnValue(ItemStack.EMPTY);
			return;
		}
		if (stack.isEmpty()) {
			slot.setStack(ItemStack.EMPTY);
		} else {
			slot.markDirty();
		}
		if (stack.getCount() == original.getCount()) {
			cir.setReturnValue(ItemStack.EMPTY);
			return;
		}
		slot.onTakeItem(player, stack);
		if (index == 0) {
			player.dropItem(stack, false);
		}
		cir.setReturnValue(original);
	}

	private boolean bigtools$toInventory(ItemStack stack, boolean fromLast) {
		boolean a = this.insertItem(stack, 36, 45, fromLast);
		boolean b = this.insertItem(stack, 71, 75, fromLast);
		boolean c = this.insertItem(stack, 9, 36, fromLast);
		boolean d = this.insertItem(stack, 46, 71, fromLast);
		return a || b || c || d;
	}
}
