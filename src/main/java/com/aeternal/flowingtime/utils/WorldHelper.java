package com.aeternal.flowingtime.utils;

import com.google.common.collect.Lists;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Helper class for anything that touches a World.
 * Notice: Please try to keep methods tidy and alphabetically ordered. Thanks!
 */
public final class WorldHelper {
    private static final List<Class<? extends EntityLiving>> peacefuls = Lists.newArrayList(
            EntitySheep.class, EntityPig.class, EntityCow.class,
            EntityMooshroom.class, EntityChicken.class, EntityBat.class,
            EntityVillager.class, EntitySquid.class, EntityOcelot.class,
            EntityWolf.class, EntityHorse.class, EntityRabbit.class,
            EntityDonkey.class, EntityMule.class, EntityPolarBear.class,
            EntityLlama.class, EntityParrot.class
    );

    private static final List<Class<? extends EntityLiving>> mobs = Lists.newArrayList(
            EntityZombie.class, EntitySkeleton.class, EntityCreeper.class,
            EntitySpider.class, EntityEnderman.class, EntitySilverfish.class,
            EntityPigZombie.class, EntityGhast.class, EntityBlaze.class,
            EntitySlime.class, EntityWitch.class, EntityRabbit.class, EntityEndermite.class,
            EntityStray.class, EntityWitherSkeleton.class, EntitySkeletonHorse.class, EntityZombieHorse.class,
            EntityZombieVillager.class, EntityHusk.class, EntityGuardian.class,
            EntityEvoker.class, EntityVex.class, EntityVindicator.class, EntityShulker.class
    );

/*    public static void freezeInBoundingBox(World world, AxisAlignedBB box, EntityPlayer player, boolean random) {
        for (BlockPos pos : getPositionsFromBox(box)) {
            Block b = world.getBlockState(pos).getBlock();

            if ((b == Blocks.WATER || b == Blocks.FLOWING_WATER) && (!random || world.rand.nextInt(128) == 0)) {
                if (player != null) {
                    PlayerHelper.checkedReplaceBlock(((EntityPlayerMP) player), pos, Blocks.ICE.getDefaultState());
                } else {
                    world.setBlockState(pos, Blocks.ICE.getDefaultState());
                }
            } else if (b.isSideSolid(world.getBlockState(pos), world, pos, EnumFacing.UP)) {
                BlockPos up = pos.up();
                IBlockState stateUp = world.getBlockState(up);
                IBlockState newState = null;

                if (stateUp.getBlock().isAir(stateUp, world, up) && (!random || world.rand.nextInt(128) == 0)) {
                    newState = Blocks.SNOW_LAYER.getDefaultState();
                } else if (stateUp.getBlock() == Blocks.SNOW_LAYER && stateUp.getValue(BlockSnow.LAYERS) < 8
                        && world.rand.nextInt(512) == 0) {
                    newState = stateUp.withProperty(BlockSnow.LAYERS, stateUp.getValue(BlockSnow.LAYERS) + 1);
                }

                if (newState != null) {
                    if (player != null) {
                        PlayerHelper.checkedReplaceBlock(((EntityPlayerMP) player), up, newState);
                    } else {
                        world.setBlockState(up, newState);
                    }
                }
            }
        }
    }

    public static Map<EnumFacing, TileEntity> getAdjacentTileEntitiesMapped(final World world, final TileEntity tile) {
        Map<EnumFacing, TileEntity> ret = new EnumMap<>(EnumFacing.class);

        for (EnumFacing dir : EnumFacing.VALUES) {
            TileEntity candidate = world.getTileEntity(tile.getPos().offset(dir));
            if (candidate != null) {
                ret.put(dir, candidate);
            }
        }

        return ret;
    }

    public static List<ItemStack> getBlockDrops(World world, EntityPlayer player, IBlockState state, ItemStack stack, BlockPos pos) {
        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0 && state.getBlock().canSilkHarvest(world, pos, state, player)) {
            return Lists.newArrayList(new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state)));
        }

        return state.getBlock().getDrops(world, pos, state, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack));
    }

    *//**
     * Gets an AABB for AOE digging operations. The offset increases both the breadth and depth of the box.
     *//*
    public static AxisAlignedBB getBroadDeepBox(BlockPos pos, EnumFacing direction, int offset) {
        switch (direction) {
            case EAST:
                return new AxisAlignedBB(pos.getX() - offset, pos.getY() - offset, pos.getZ() - offset, pos.getX(), pos.getY() + offset, pos.getZ() + offset);
            case WEST:
                return new AxisAlignedBB(pos.getX(), pos.getY() - offset, pos.getZ() - offset, pos.getX() + offset, pos.getY() + offset, pos.getZ() + offset);
            case UP:
                return new AxisAlignedBB(pos.getX() - offset, pos.getY() - offset, pos.getZ() - offset, pos.getX() + offset, pos.getY(), pos.getZ() + offset);
            case DOWN:
                return new AxisAlignedBB(pos.getX() - offset, pos.getY(), pos.getZ() - offset, pos.getX() + offset, pos.getY() + offset, pos.getZ() + offset);
            case SOUTH:
                return new AxisAlignedBB(pos.getX() - offset, pos.getY() - offset, pos.getZ() - offset, pos.getX() + offset, pos.getY() + offset, pos.getZ());
            case NORTH:
                return new AxisAlignedBB(pos.getX() - offset, pos.getY() - offset, pos.getZ(), pos.getX() + offset, pos.getY() + offset, pos.getZ() + offset);
            default:
                return new AxisAlignedBB(0, 0, 0, 0, 0, 0);
        }
    }

    *//**
     * Returns in AABB that is always 3x3 orthogonal to the side hit, but varies in depth in the direction of the side hit
     *//*
    public static AxisAlignedBB getDeepBox(BlockPos pos, EnumFacing direction, int depth) {
        switch (direction) {
            case EAST:
                return new AxisAlignedBB(pos.getX() - depth, pos.getY() - 1, pos.getZ() - 1, pos.getX(), pos.getY() + 1, pos.getZ() + 1);
            case WEST:
                return new AxisAlignedBB(pos.getX(), pos.getY() - 1, pos.getZ() - 1, pos.getX() + depth, pos.getY() + 1, pos.getZ() + 1);
            case UP:
                return new AxisAlignedBB(pos.getX() - 1, pos.getY() - depth, pos.getZ() - 1, pos.getX() + 1, pos.getY(), pos.getZ() + 1);
            case DOWN:
                return new AxisAlignedBB(pos.getX() - 1, pos.getY(), pos.getZ() - 1, pos.getX() + 1, pos.getY() + depth, pos.getZ() + 1);
            case SOUTH:
                return new AxisAlignedBB(pos.getX() - 1, pos.getY() - 1, pos.getZ() - depth, pos.getX() + 1, pos.getY() + 1, pos.getZ());
            case NORTH:
                return new AxisAlignedBB(pos.getX() - 1, pos.getY() - 1, pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + depth);
            default:
                return new AxisAlignedBB(0, 0, 0, 0, 0, 0);
        }
    }

    *//**
     * Gets an AABB for AOE digging operations. The charge increases only the breadth of the box.
     * Y level remains constant. As such, a direction hit is unneeded.
     *//*
    public static AxisAlignedBB getFlatYBox(BlockPos pos, int offset) {
        return new AxisAlignedBB(pos.getX() - offset, pos.getY(), pos.getZ() - offset, pos.getX() + offset, pos.getY(), pos.getZ() + offset);
    }*/



    /**
     * Wrapper around BlockPos.getAllInBox() with an AABB
     * Note that this is inclusive of all positions in the AABB!
     */
    public static Iterable<BlockPos> getPositionsFromBox(AxisAlignedBB box) {
        return BlockPos.getAllInBox(new BlockPos(box.minX, box.minY, box.minZ), new BlockPos(box.maxX, box.maxY, box.maxZ));
    }



    public static List<TileEntity> getTileEntitiesWithinAABB(World world, AxisAlignedBB bBox) {
        List<TileEntity> list = new ArrayList<>();

        for (BlockPos pos : getPositionsFromBox(bBox)) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile != null) {
                list.add(tile);
            }
        }

        return list;
    }


}
