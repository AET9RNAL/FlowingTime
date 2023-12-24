package com.aeternal.flowingtime.events;


import com.aeternal.flowingtime.Constants;
import com.aeternal.flowingtime.network.packets.KeyPressPKT;
import com.aeternal.flowingtime.network.packets.PacketHandler;
import com.aeternal.flowingtime.utils.ClientKeyHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Constants.MOD_ID)
public class KeyPressEvent
{
    @SubscribeEvent
    public static void keyPress(KeyInputEvent event)
    {
        for (KeyBinding k : ClientKeyHelper.mcToPe.keySet())
        {
            if (k.isPressed())
            {
                PacketHandler.sendToServer(new KeyPressPKT(ClientKeyHelper.mcToPe.get(k)));
            }
        }
    }
}
