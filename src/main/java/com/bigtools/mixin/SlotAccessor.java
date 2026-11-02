package com.bigtools.mixin;

import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Slot.class)
public interface SlotAccessor {
	@Mutable
	@Accessor("index")
	void bigtools$setIndex(int index);

	@Mutable
	@Accessor("x")
	void bigtools$setX(int x);

	@Mutable
	@Accessor("y")
	void bigtools$setY(int y);
}
