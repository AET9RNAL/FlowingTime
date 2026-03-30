package com.aeternal.flowingtime.client;

import com.aeternal.flowingtime.FlowingTime;
import com.aeternal.flowingtime.network.KeyPressPKT;
import com.aeternal.flowingtime.network.PacketHandler;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FlowingTime.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class KeyPressEvent {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (ClientKeyHelper.mcToFl == null) return;
        for (KeyMapping k : ClientKeyHelper.mcToFl.keySet()) {
            while (k.consumeClick()) {
                PacketHandler.sendToServer(new KeyPressPKT(ClientKeyHelper.mcToFl.get(k)));
            }
        }
    }
}