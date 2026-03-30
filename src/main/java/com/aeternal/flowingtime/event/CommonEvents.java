package com.aeternal.flowingtime.event;

import com.aeternal.flowingtime.FlowingTime;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FlowingTime.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEvents {

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        FlowingTime.LOGGER.info("{} server starting", FlowingTime.MOD_ID);
    }
}
