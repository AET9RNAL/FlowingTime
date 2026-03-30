package com.aeternal.flowingtime.api.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IModeChanger {
    byte getMode(@Nonnull ItemStack stack);
    boolean changeMode(@Nonnull Player player, @Nonnull ItemStack stack, @Nullable InteractionHand hand);
}