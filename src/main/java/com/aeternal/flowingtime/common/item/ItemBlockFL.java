package com.aeternal.flowingtime.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemBlockFL extends ItemBlock {

    public ItemBlockFL(Block block) {
        super(block);
    }

    @Nonnull
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "tile." + this.getRegistryName().toString();

    }
}
