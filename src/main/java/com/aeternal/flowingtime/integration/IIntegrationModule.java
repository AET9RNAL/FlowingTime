package com.aeternal.flowingtime.integration;

import net.minecraftforge.eventbus.api.IEventBus;

public interface IIntegrationModule {

    String getTargetModId();

    default boolean isConfigEnabled() {
        return true;
    }

    void init(IEventBus modEventBus);

    default void commonSetup() {}

    default void clientSetup() {}
}
