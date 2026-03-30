package com.aeternal.flowingtime.registry;

import com.aeternal.flowingtime.FlowingTime;
import com.aeternal.flowingtime.block.entity.FLPedestalBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, FlowingTime.MOD_ID);

    public static final RegistryObject<BlockEntityType<FLPedestalBlockEntity>> FL_PEDESTAL =
            BLOCK_ENTITIES.register("flpedestal",
                    () -> BlockEntityType.Builder.of(FLPedestalBlockEntity::new,
                            ModBlocks.FL_PEDESTAL.get()).build(null));
}
