package com.aeternal.flowingtime.registry;

import com.aeternal.flowingtime.FlowingTime;
import com.aeternal.flowingtime.block.PedestalBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, FlowingTime.MOD_ID);

    public static final RegistryObject<Block> FL_PEDESTAL = BLOCKS.register("flpedestal",
            () -> new PedestalBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(1.0f)
                    .lightLevel(s -> 12)
                    .noOcclusion()));
}
