package com.aeternal.flowingtime.gameObjs;

import com.aeternal.flowingtime.Constants;
import com.aeternal.flowingtime.common.item.ItemFLPedestal;
import com.aeternal.flowingtime.common.lib.LibBlockNames;
import com.aeternal.flowingtime.gameObjs.blocks.Pedestal;
import com.aeternal.flowingtime.gameObjs.tiles.FLPedestalTile;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects;


@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public final class ObjHandler {
    public static final Block flPedestal = new Pedestal();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> evt) {
        IForgeRegistry<Block> r = evt.getRegistry();
        r.register(flPedestal);
    }
    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> evt) {
        IForgeRegistry<Item> r = evt.getRegistry();
        Item itemBlock = new ItemFLPedestal(flPedestal).setRegistryName(Objects.requireNonNull(flPedestal.getRegistryName()));
        r.register(itemBlock);
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            ModelLoader.setCustomModelResourceLocation(itemBlock, 0, new ModelResourceLocation(flPedestal.getRegistryName(), "inventory"));
        }
        initTileEntities();
    }
    private static void initTileEntities() {
        registerTile(FLPedestalTile.class, LibBlockNames.FLPEDESTAL);
    }
    private static void registerTile(Class<? extends TileEntity> clazz, String key) {
        GameRegistry.registerTileEntity(clazz, Constants.PREFIX_MOD + key);
    }

}
