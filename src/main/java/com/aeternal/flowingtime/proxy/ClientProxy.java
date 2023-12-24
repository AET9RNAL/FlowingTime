package com.aeternal.flowingtime.proxy;


import com.aeternal.flowingtime.client.render.FLPedestalRender;
import com.aeternal.flowingtime.gameObjs.tiles.FLPedestalTile;
import com.aeternal.flowingtime.utils.ClientKeyHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {
        super.preInit();
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void registerKeyBinds()
    {
        ClientKeyHelper.registerMCBindings();
    }

    @Override
    public void postInit() {
        super.postInit();
    }

    @SubscribeEvent
    public void onToolTipRender(RenderTooltipEvent.PostText evt)
    {
        if(evt.getStack().isEmpty())
            return;

        ItemStack stack = evt.getStack();
        Minecraft mc = Minecraft.getMinecraft();
        int width = evt.getWidth();
        int height = 3;
        int tooltipX = evt.getX();
        int tooltipY = evt.getY() - 4;
        FontRenderer font = evt.getFontRenderer();

    }

}