package com.aeternal.flowingtime.registry;

import com.aeternal.flowingtime.FlowingTime;
import com.aeternal.flowingtime.item.TimeWatch;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, FlowingTime.MOD_ID);

    public static final RegistryObject<Item> TIME_WATCH = ITEMS.register("timewatch",
            () -> new TimeWatch(new Item.Properties().tab(ModCreativeTabs.MOD_TAB).stacksTo(1)));

    public static final RegistryObject<Item> FL_PEDESTAL_ITEM = ITEMS.register("flpedestal",
            () -> new BlockItem(ModBlocks.FL_PEDESTAL.get(),
                    new Item.Properties().tab(ModCreativeTabs.MOD_TAB)));

    private static RegistryObject<Item> registerBlockItem(String name, RegistryObject<Block> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(ModCreativeTabs.MOD_TAB)));
    }
}
