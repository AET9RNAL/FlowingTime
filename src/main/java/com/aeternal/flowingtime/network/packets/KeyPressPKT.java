package com.aeternal.flowingtime.network.packets;

import com.aeternal.flowingtime.utils.FLKeybind;
import com.aeternal.flowingtime.api.item.IItemCharge;
import com.aeternal.flowingtime.api.item.IModeChanger;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import static com.aeternal.flowingtime.utils.FLKeybind.MODE;

public class KeyPressPKT implements IMessage {
    private FLKeybind key;

    public KeyPressPKT() {}

    public KeyPressPKT(FLKeybind key) {
        this.key = key;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        key = FLKeybind.values()[buf.readInt()];
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(key.ordinal());
    }

    public static class Handler implements IMessageHandler<KeyPressPKT, IMessage> {
        @Override
        public IMessage onMessage(final KeyPressPKT message, final MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                for (EnumHand hand : EnumHand.values()) {
                    ItemStack stack = player.getHeldItem(hand);
                    switch (message.key) {
                        case CHARGE:
                            if (!stack.isEmpty()
                                    && stack.getItem() instanceof IItemCharge
                                    && ((IItemCharge) stack.getItem()).changeCharge(player, stack, hand)) {
                                return;
                            }
                            break;
                        case MODE:
                            if (!stack.isEmpty()
                                    && stack.getItem() instanceof IModeChanger
                                    && ((IModeChanger) stack.getItem()).changeMode(player, stack, hand)) {
                                return;
                            }
                            break;
                    }
                }
            });
            return null;
        }
    }
}
