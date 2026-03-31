package com.aeternal.flowingtime.registry;

import com.aeternal.flowingtime.FlowingTime;
import com.aeternal.flowingtime.block.entity.FLPedestalBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
@SuppressWarnings("removal")
public class ModBlockEntities {
    public static final RegistryObject<BlockEntityType<FLPedestalBlockEntity>> FL_PEDESTAL = RegistryObject.create(new ResourceLocation(FlowingTime.MOD_ID, "flpedestal"), ForgeRegistries.BLOCK_ENTITY_TYPES);
}
