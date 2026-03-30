package com.aeternal.flowingtime.network;

import com.aeternal.flowingtime.api.item.IItemCharge;
import com.aeternal.flowingtime.api.item.IModeChanger;
import com.aeternal.flowingtime.util.FLKeybind;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class KeyPressPKT {

    private final FLKeybind key;

    public KeyPressPKT(FLKeybind key) {
        this.key = key;
    }

    public static void encode(KeyPressPKT msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.key.ordinal());
    }

    public static KeyPressPKT decode(FriendlyByteBuf buf) {
        return new KeyPressPKT(FLKeybind.values()[buf.readInt()]);
    }

    public static void handle(KeyPressPKT msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player == null) return;

            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack stack = player.getItemInHand(hand);
                if (stack.isEmpty()) continue;

                switch (msg.key) {
                    case CHARGE:
                        if (stack.getItem() instanceof IItemCharge charge
                                && charge.changeCharge(player, stack, hand)) {
                            return;
                        }
                        break;
                    case MODE:
                        if (stack.getItem() instanceof IModeChanger changer
                                && changer.changeMode(player, stack, hand)) {
                            return;
                        }
                        break;
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}