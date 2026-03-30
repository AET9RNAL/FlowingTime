package com.aeternal.flowingtime.integration;

import com.aeternal.flowingtime.FlowingTime;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.ArrayList;
import java.util.List;

public class IntegrationManager {

    private static final List<IIntegrationModule> activeModules = new ArrayList<>();

    public static void init(IEventBus modEventBus) {
        List<IIntegrationModule> allModules = registerModules();

        for (IIntegrationModule module : allModules) {
            if (ModList.get().isLoaded(module.getTargetModId()) && module.isConfigEnabled()) {
                FlowingTime.LOGGER.info("Enabling integration: {}", module.getTargetModId());
                module.init(modEventBus);
                activeModules.add(module);
            } else {
                FlowingTime.LOGGER.debug("Skipping integration: {} (loaded={}, configEnabled={})",
                        module.getTargetModId(),
                        ModList.get().isLoaded(module.getTargetModId()),
                        module.isConfigEnabled());
            }
        }

        modEventBus.addListener(IntegrationManager::onCommonSetup);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            modEventBus.addListener(IntegrationManager::onClientSetup);
        });
    }

    private static List<IIntegrationModule> registerModules() {
        List<IIntegrationModule> modules = new ArrayList<>();
        return modules;
    }

    private static void onCommonSetup(FMLCommonSetupEvent event) {
        for (IIntegrationModule module : activeModules) {
            module.commonSetup();
        }
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        for (IIntegrationModule module : activeModules) {
            module.clientSetup();
        }
    }

    public static boolean isIntegrationActive(String targetModId) {
        return activeModules.stream().anyMatch(m -> m.getTargetModId().equals(targetModId));
    }
}
