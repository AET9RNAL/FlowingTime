package com.aeternal.flowingtime;

import com.aeternal.flowingtime.integration.IntegrationManager;
import com.aeternal.flowingtime.network.PacketHandler;
import com.aeternal.flowingtime.registry.ModBlockEntities;
import com.aeternal.flowingtime.registry.ModBlocks;
import com.aeternal.flowingtime.registry.ModItems;
import com.aeternal.flowingtime.registry.ModSounds;
import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(FlowingTime.MOD_ID)
public class FlowingTime {

    public static final String MOD_ID = "flowingtime";
    public static final Logger LOGGER = LogUtils.getLogger();

    public FlowingTime(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        // ProjectE conflict check
        if (Constants.isProjectELoaded()) {
            throw new RuntimeException("To prevent potential issues and misuse, refrain from using " +
                    "Flowing Time Mod simultaneously with ProjectE. ProjectE provides the same functionality, " +
                    "and Flowing Time was designed to be standalone.");
        }

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModSounds.SOUNDS.register(modEventBus);


        modEventBus.addListener(this::commonSetup);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            com.aeternal.flowingtime.client.ClientSetup.init(modEventBus);
        });

        MinecraftForge.EVENT_BUS.register(this);

        context.registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, ModConfig.SPEC);

        // Initialize integrations
        IntegrationManager.init(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("{} common setup", MOD_ID);
        event.enqueueWork(PacketHandler::register);
    }
}
