package com.aeternal.flowingtime.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public final class WorldHelper {

    public static Iterable<BlockPos> getPositionsFromBox(AABB box) {
        return BlockPos.betweenClosed(
                (int) box.minX, (int) box.minY, (int) box.minZ,
                (int) box.maxX, (int) box.maxY, (int) box.maxZ
        );
    }

    public static List<BlockEntity> getBlockEntitiesWithinAABB(Level level, AABB box) {
        List<BlockEntity> list = new ArrayList<>();
        for (BlockPos pos : getPositionsFromBox(box)) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be != null) {
                list.add(be);
            }
        }
        return list;
    }

    private WorldHelper() {}
}
