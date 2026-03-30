package com.aeternal.flowingtime.registry;

import com.aeternal.flowingtime.FlowingTime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, FlowingTime.MOD_ID);

    public static final RegistryObject<SoundEvent> CHARGE = SOUNDS.register("item.pecharge",
            () -> new SoundEvent(new ResourceLocation(FlowingTime.MOD_ID, "item.pecharge")));

    public static final RegistryObject<SoundEvent> UNCHARGE = SOUNDS.register("item.peuncharge",
            () -> new SoundEvent(new ResourceLocation(FlowingTime.MOD_ID, "item.peuncharge")));
}