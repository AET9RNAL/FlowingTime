package com.aeternal.flowingtime.api.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.List;

public interface IPedestalItem {
    void updateInPedestal(@Nonnull Level level, @Nonnull BlockPos pos);

    @Nonnull
    List<Component> getPedestalDescription();
}