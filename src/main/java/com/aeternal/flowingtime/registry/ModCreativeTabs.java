package com.aeternal.flowingtime.registry;

import com.aeternal.flowingtime.FlowingTime;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTabs {

    public static final CreativeModeTab MOD_TAB = new CreativeModeTab(FlowingTime.MOD_ID + ".tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.TIME_WATCH.get());
        }
    };
}
