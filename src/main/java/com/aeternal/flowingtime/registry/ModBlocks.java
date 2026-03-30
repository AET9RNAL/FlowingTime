package com.aeternal.flowingtime.registry;

import com.aeternal.flowingtime.FlowingTime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final RegistryObject<Block> FL_PEDESTAL = RegistryObject.create(new ResourceLocation(FlowingTime.MOD_ID, "flpedestal"), ForgeRegistries.BLOCKS);
}
