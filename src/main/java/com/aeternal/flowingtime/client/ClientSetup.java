package com.aeternal.flowingtime.client;

import com.aeternal.flowingtime.FlowingTime;
import com.aeternal.flowingtime.client.render.FLPedestalRenderer;
import com.aeternal.flowingtime.registry.ModBlockEntities;
import com.aeternal.flowingtime.registry.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = FlowingTime.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    public static void init(IEventBus modEventBus) {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(ModItems.TIME_WATCH.get(),
                    new ResourceLocation(FlowingTime.MOD_ID, "active"),
                    (stack, level, entity, seed) ->
                            stack.hasTag() && stack.getTag().getBoolean("Active") ? 1F : 0F);
        });
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.FL_PEDESTAL.get(), FLPedestalRenderer::new);
    }
}