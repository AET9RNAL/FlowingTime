
package com.aeternal.flowingtime.common.item;


import com.aeternal.flowingtime.Constants;
import com.aeternal.flowingtime.client.render.IModelRegister;
import com.aeternal.flowingtime.creativetab.FlowingTimeCreativeTab;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public abstract class ItemMod extends Item implements IModelRegister  {

    public static final String TAG_ACTIVE = "Active";
    public static final String TAG_MODE = "Mode";
    protected static final ResourceLocation ACTIVE_NAME = new ResourceLocation(Constants.MOD_ID, "active");
    protected static final IItemPropertyGetter ACTIVE_GETTER = (stack, world, entity) -> stack.hasTagCompound() && stack.getTagCompound().getBoolean(TAG_ACTIVE) ? 1F : 0F;
    public ItemMod(String name) {
        setCreativeTab(FlowingTimeCreativeTab.INSTANCE);
        setRegistryName(new ResourceLocation(Constants.MOD_ID, name));
    }

    @Nonnull
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item." + this.getRegistryName().toString();
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        if (oldStack.getItem() != newStack.getItem())
            return true;

        boolean diffActive = oldStack.hasTagCompound() && newStack.hasTagCompound()
                && oldStack.getTagCompound().hasKey(TAG_ACTIVE) && newStack.getTagCompound().hasKey(TAG_ACTIVE)
                && !oldStack.getTagCompound().getTag(TAG_ACTIVE).equals(newStack.getTagCompound().getTag(TAG_ACTIVE));

        boolean diffMode = oldStack.hasTagCompound() && newStack.hasTagCompound()
                && oldStack.getTagCompound().hasKey(TAG_MODE) && newStack.getTagCompound().hasKey(TAG_MODE)
                && !oldStack.getTagCompound().getTag(TAG_MODE).equals(newStack.getTagCompound().getTag(TAG_MODE));

        return diffActive || diffMode;
    }

@SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

}
