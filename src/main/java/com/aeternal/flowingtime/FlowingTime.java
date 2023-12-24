package com.aeternal.flowingtime;

import com.aeternal.flowingtime.client.render.FLPedestalRender;
import com.aeternal.flowingtime.gameObjs.ObjHandler;
import com.aeternal.flowingtime.gameObjs.tiles.FLPedestalTile;
import com.aeternal.flowingtime.network.packets.PacketHandler;
import com.aeternal.flowingtime.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@EventBusSubscriber
@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MOD_VERSION, dependencies = "")

public class FlowingTime
{
    @Mod.Instance(Constants.MOD_ID)
    public static FlowingTime instance;
    public static Logger log;
    @SidedProxy(serverSide = "com.aeternal.flowingtime.proxy.CommonProxy", clientSide = "com.aeternal.flowingtime.proxy.ClientProxy")
    public static CommonProxy proxy;

    public static final Logger LOG = LogManager.getLogger(Constants.MOD_ID);

    @EventHandler
    public void preInit(FMLPreInitializationEvent e)
    {
        if (Loader.isModLoaded("projecte")) {
            throw new RuntimeException("To prevent potential issues and misuse, refrain from using Flowing Time Mod simultaneously with ProjectE." +
                    "ProjectE provides the same functionality, and Flowing Time was designed to be standalone.");
        }
        MinecraftForge.EVENT_BUS.register(proxy);
        proxy.preInit();
        proxy.registerModels();
        PacketHandler.register();
        proxy.registerKeyBinds();
        Config.loadNormalConfig(e.getSuggestedConfigurationFile());
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            ClientRegistry.bindTileEntitySpecialRenderer(FLPedestalTile.class, new FLPedestalRender());
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent e)
    {
        proxy.init();
    }


    @EventHandler
    public void postinit(FMLPostInitializationEvent e){
        proxy.postInit();
    }
    @EventHandler
    public void loadComplete(FMLLoadCompleteEvent e)
    {

    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> e)
    {

    }
}