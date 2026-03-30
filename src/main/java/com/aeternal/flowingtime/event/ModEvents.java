package com.aeternal.flowingtime.event;

import com.aeternal.flowingtime.FlowingTime;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;

@Mod.EventBusSubscriber(modid = FlowingTime.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {

    @SubscribeEvent
    public static void onInterModEnqueue(InterModEnqueueEvent event) {
    }

    @SubscribeEvent
    public static void onInterModProcess(InterModProcessEvent event) {
    }
}
