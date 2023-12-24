package com.aeternal.flowingtime.api.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;


public interface IPedestalItem {

    @SideOnly(Side.CLIENT)
    String TOOLTIPDISABLED = TextFormatting.RED + I18n.format("pe.pedestal.item_disabled");


    void updateInPedestal(@Nonnull World world, @Nonnull BlockPos pos);

    @SideOnly(Side.CLIENT)
    @Nonnull List<String> getPedestalDescription();
}
