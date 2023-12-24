/*
package com.aeternal.flowingtime.utils;

import baubles.api.BaublesApi;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreCriteriaReadOnly;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

*/
/**
 * Helper class for player-related methods.
 * Notice: Please try to keep methods tidy and alphabetically ordered. Thanks!
 *//*

public final class PlayerHelper
{

    public static boolean checkedPlaceBlock(EntityPlayerMP player, BlockPos pos, IBlockState state)
    {
        return checkedPlaceBlock(player, pos, state, EnumHand.MAIN_HAND);
    }

    */
/**
     * Tries placing a block and fires an event for it.
     * @return Whether the block was successfully placed
     *//*

    public static boolean checkedPlaceBlock(EntityPlayerMP player, BlockPos pos, IBlockState state, EnumHand hand)
    {
        if (!hasEditPermission(player, pos))
        {
            return false;
        }
        World world = player.getEntityWorld();
        BlockSnapshot before = BlockSnapshot.getBlockSnapshot(world, pos);
        world.setBlockState(pos, state);
        BlockEvent.PlaceEvent evt = new BlockEvent.PlaceEvent(before, Blocks.AIR.getDefaultState(), player, EnumHand.MAIN_HAND);
        MinecraftForge.EVENT_BUS.post(evt);
        if (evt.isCanceled())
        {
            world.restoringBlockSnapshots = true;
            before.restore(true, false);
            world.restoringBlockSnapshots = false;
            //PELogger.logInfo("Checked place block got canceled, restoring snapshot.");
            return false;
        }
        //PELogger.logInfo("Checked place block passed!");
        return true;
    }

    public static boolean checkedReplaceBlock(EntityPlayerMP player, BlockPos pos, IBlockState state)
    {
        return checkedReplaceBlock(player, pos, state, EnumHand.MAIN_HAND);
    }

    public static boolean checkedReplaceBlock(EntityPlayerMP player, BlockPos pos, IBlockState state, EnumHand hand)
    {
        return hasBreakPermission(player, pos) && checkedPlaceBlock(player, pos, state, hand);
    }


    public static boolean hasBreakPermission(EntityPlayerMP player, BlockPos pos)
    {
        return hasEditPermission(player, pos)
                && ForgeHooks.onBlockBreakEvent(player.getEntityWorld(), player.interactionManager.getGameType(), player, pos) != -1;
    }

    public static boolean hasEditPermission(EntityPlayerMP player, BlockPos pos)
    {
        if (FMLCommonHandler.instance().getMinecraftServerInstance().isBlockProtected(player.getEntityWorld(), pos, player))
        {
            return false;
        }

        for (EnumFacing e : EnumFacing.VALUES)
        {
            if (!player.canPlayerEdit(pos, e, ItemStack.EMPTY))
            {
                return false;
            }
        }

        return true;
    }



}
*/
