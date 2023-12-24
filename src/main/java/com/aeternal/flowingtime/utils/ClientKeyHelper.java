package com.aeternal.flowingtime.utils;

import com.aeternal.flowingtime.Constants;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class ClientKeyHelper
{
    public static ImmutableBiMap<KeyBinding, FLKeybind> mcToPe;
    private static ImmutableBiMap<FLKeybind, KeyBinding> peToMc;

    public static void registerMCBindings()
    {
        ImmutableBiMap.Builder<KeyBinding, FLKeybind> builder = ImmutableBiMap.builder();
        for (FLKeybind k : FLKeybind.values())
        {
            KeyBinding mcK = new KeyBinding(k.keyName, k.defaultKeyCode, Constants.MOD_ID);
            builder.put(mcK, k);
            ClientRegistry.registerKeyBinding(mcK);
        }
        mcToPe = builder.build();
        peToMc = mcToPe.inverse();
    }

    public static String getKeyName(FLKeybind k)
    {
        return getKeyName(peToMc.get(k));
    }

    public static String getKeyName(KeyBinding k)
    {
        int keyCode = k.getKeyCode();
        if (keyCode > Keyboard.getKeyCount() || keyCode < 0)
        {
            return k.getDisplayName();
        }
        return Keyboard.getKeyName(keyCode);
    }
}
