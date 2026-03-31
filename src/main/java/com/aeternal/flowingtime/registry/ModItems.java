package com.aeternal.flowingtime.registry;

import com.aeternal.flowingtime.FlowingTime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
@SuppressWarnings("removal")
public class ModItems {
    public static final RegistryObject<Item> TIME_WATCH = RegistryObject.create(new ResourceLocation(FlowingTime.MOD_ID, "timewatch"), ForgeRegistries.ITEMS);
    public static final RegistryObject<Item> FL_PEDESTAL_ITEM = RegistryObject.create(new ResourceLocation(FlowingTime.MOD_ID, "flpedestal"), ForgeRegistries.ITEMS);
}
