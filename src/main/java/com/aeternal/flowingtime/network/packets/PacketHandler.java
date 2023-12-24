package com.aeternal.flowingtime.network.packets;

import com.aeternal.flowingtime.Constants;
import com.aeternal.flowingtime.network.packets.KeyPressPKT;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

public final class PacketHandler
{
    private static final SimpleNetworkWrapper HANDLER = NetworkRegistry.INSTANCE.newSimpleChannel(Constants.MOD_ID);

    public static void register()
    {
        int disc = 0;
        HANDLER.registerMessage(KeyPressPKT.Handler.class, KeyPressPKT.class, disc++, Side.SERVER);
    }


    public static void sendNonLocal(IMessage msg, EntityPlayerMP player) {
        if (!player.getEntityWorld().isRemote) {
            HANDLER.sendTo(msg, player);
        }
    }
    /**
     * Sends a packet to the server.<br>
     * Must be called Client side.
     */
    public static void sendToServer(IMessage msg)
    {
        HANDLER.sendToServer(msg);
    }

    /**
     * Sends a packet to all the clients.<br>
     * Must be called Server side.
     */
    public static void sendToAll(IMessage msg)
    {
        HANDLER.sendToAll(msg);
    }

    /**
     * Send a packet to all players around a specific point.<br>
     * Must be called Server side.
     */
    public static void sendToAllAround(IMessage msg, TargetPoint point)
    {
        HANDLER.sendToAllAround(msg, point);
    }

    /**
     * Send a packet to a specific player.<br>
     * Must be called Server side.
     */
    public static void sendTo(IMessage msg, EntityPlayerMP player)
    {
        if (!(player instanceof FakePlayer))
        {
            HANDLER.sendTo(msg, player);
        }
    }

    /**
     * Send a packet to all the players in the specified dimension.<br>
     *  Must be called Server side.
     */
    public static void sendToDimension(IMessage msg, int dimension)
    {
        HANDLER.sendToDimension(msg, dimension);
    }
}
