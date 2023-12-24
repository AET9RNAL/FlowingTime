package com.aeternal.flowingtime.utils;

import com.aeternal.flowingtime.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public final class SoundHandler
{
    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> evt)
    {
        registerSound(evt.getRegistry(), "item.pecharge");
        registerSound(evt.getRegistry(), "item.peuncharge");
    }

    private static void registerSound(IForgeRegistry<SoundEvent> registry, String soundName)
    {
        ResourceLocation name = new ResourceLocation(Constants.MOD_ID, soundName);
        registry.register(new SoundEvent(name).setRegistryName(name));
    }

    private SoundHandler() {}
}
