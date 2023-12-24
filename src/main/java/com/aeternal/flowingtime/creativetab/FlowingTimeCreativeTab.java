package com.aeternal.flowingtime.creativetab;

import com.aeternal.flowingtime.Constants;
import com.aeternal.flowingtime.common.item.ModItems;
import com.aeternal.flowingtime.gameObjs.ObjHandler;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

public final class FlowingTimeCreativeTab extends CreativeTabs {

    public static final FlowingTimeCreativeTab INSTANCE = new FlowingTimeCreativeTab();

    NonNullList<ItemStack> list;

    public FlowingTimeCreativeTab() {
        super(Constants.MOD_ID);
        setNoTitle();

    }
    @Override
    public ItemStack getTabIconItem()
    {
        return new ItemStack(ModItems.timewatch);
    }

    @Override
    public void displayAllRelevantItems(@Nonnull NonNullList<ItemStack> list) {
        this.list = list;
        addItem(ModItems.timewatch);
        addBlock(ObjHandler.flPedestal);
    }

    private void addItem(Item item) {
        item.getSubItems(this, list);
    }

    private void addBlock(Block block) {
        ItemStack stack = new ItemStack(block);
        block.getSubBlocks(this, list);

    }

}