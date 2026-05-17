package me.FrostFizzie.itemscooldown.client.mixin.Accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net.minecraft.world.item.ItemCooldowns$CooldownInstance")
public interface CooldownInstanceAccessor {
    @Accessor("startTime")
    int getStartTime();
    @Accessor("endTime")
    int getEndTime();
}
