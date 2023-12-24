package com.aeternal.flowingtime.common.item;

import com.aeternal.flowingtime.Constants;
import com.aeternal.flowingtime.items.TimeWatch;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;


@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public final class ModItems {
    public static final Item timewatch = new TimeWatch();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> evt) {
        IForgeRegistry<Item> r = evt.getRegistry();
        r.register(timewatch);
    }



}

