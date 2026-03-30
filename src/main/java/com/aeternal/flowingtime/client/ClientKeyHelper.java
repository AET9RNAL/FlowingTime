package com.aeternal.flowingtime.client;

import com.aeternal.flowingtime.FlowingTime;
import com.aeternal.flowingtime.util.FLKeybind;
import com.google.common.collect.ImmutableBiMap;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FlowingTime.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientKeyHelper {

    public static ImmutableBiMap<KeyMapping, FLKeybind> mcToFl;

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        ImmutableBiMap.Builder<KeyMapping, FLKeybind> builder = ImmutableBiMap.builder();
        for (FLKeybind k : FLKeybind.values()) {
            KeyMapping mapping = new KeyMapping(
                    k.keyName,
                    InputConstants.Type.KEYSYM,
                    k.defaultKeyCode,
                    FlowingTime.MOD_ID
            );
            builder.put(mapping, k);
            event.register(mapping);
        }
        mcToFl = builder.build();
    }
}