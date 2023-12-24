package com.aeternal.flowingtime.api;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public final class FLSounds
{
    public static final SoundEvent CHARGE = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("flowingtime", "item.pecharge"));
    public static final SoundEvent UNCHARGE = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("flowingtime", "item.peuncharge"));

    private FLSounds() {}

}
