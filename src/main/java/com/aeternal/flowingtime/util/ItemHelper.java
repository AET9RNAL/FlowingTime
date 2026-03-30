package com.aeternal.flowingtime.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public final class ItemHelper {

    public static CompoundTag getOrCreateCompound(ItemStack stack) {
        return stack.getOrCreateTag();
    }

    private ItemHelper() {}
}