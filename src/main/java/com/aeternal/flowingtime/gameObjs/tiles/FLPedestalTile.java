package com.aeternal.flowingtime.gameObjs.tiles;

import com.aeternal.flowingtime.api.FLSounds;
import com.aeternal.flowingtime.api.item.IPedestalItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.Random;

public class FLPedestalTile extends TileEntity implements ITickable {
    private static final int RANGE = 4;
    private boolean isActive = false;
    private ItemStackHandler inventory = new ItemStackHandler(1);
    private int particleCooldown = 10;
    private int activityCooldown = 0;
    public boolean previousRedstoneState = false;
    public double centeredX, centeredY, centeredZ;

    @Override
    public void update() {
        centeredX = pos.getX() + 0.5;
        centeredY = pos.getY() + 0.5;
        centeredZ = pos.getZ() + 0.5;

        if (getActive()) {
            if (!inventory.getStackInSlot(0).isEmpty()) {
                Item item = inventory.getStackInSlot(0).getItem();
                // Replace IPedestalItem with your own interface if needed
                if (item instanceof IPedestalItem) {
                    ((IPedestalItem) item).updateInPedestal(world, getPos());
                }
                if (particleCooldown <= 0) {
                    spawnParticles();
                    particleCooldown = 10;
                } else {
                    particleCooldown--;
                }
            } else {
                setActive(false);
            }
        }
    }

    private void spawnParticles() {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        Random rand = world.rand;

        // Spawning flame particles around the base
        world.spawnParticle(EnumParticleTypes.FLAME, x + 0.2, y + 0.3, z + 0.2, 0, 0, 0);
        world.spawnParticle(EnumParticleTypes.FLAME, x + 0.2, y + 0.3, z + 0.5, 0, 0, 0);
        world.spawnParticle(EnumParticleTypes.FLAME, x + 0.2, y + 0.3, z + 0.8, 0, 0, 0);
        world.spawnParticle(EnumParticleTypes.FLAME, x + 0.5, y + 0.3, z + 0.2, 0, 0, 0);
        world.spawnParticle(EnumParticleTypes.FLAME, x + 0.5, y + 0.3, z + 0.8, 0, 0, 0);
        world.spawnParticle(EnumParticleTypes.FLAME, x + 0.8, y + 0.3, z + 0.2, 0, 0, 0);
        world.spawnParticle(EnumParticleTypes.FLAME, x + 0.8, y + 0.3, z + 0.5, 0, 0, 0);
        world.spawnParticle(EnumParticleTypes.FLAME, x + 0.8, y + 0.3, z + 0.8, 0, 0, 0);

        for (int i = 0; i < 5; ++i) {
            double motionX = rand.nextGaussian() * 0.02;
            double motionY = -1.0 * rand.nextDouble();
            double motionZ = rand.nextGaussian() * 0.02;

            world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE,
                    x + rand.nextDouble(), pos.getY() + 1.5, z + rand.nextDouble(),
                    motionX, motionY, motionZ);
        }

        for (int i = 0; i < 3; ++i) {
            int j = rand.nextInt(2) * 2 - 1;
            int k = rand.nextInt(2) * 2 - 1;
            double d0 = (double) pos.getX() + 0.5D + 0.25D * (double) j;
            double d1 = (double) ((float) pos.getY() + rand.nextFloat());
            double d2 = (double) pos.getZ() + 0.5D + 0.25D * (double) k;
            double d3 = (double) (rand.nextFloat() * (float) j);
            double d4 = ((double) rand.nextFloat() - 0.5D) * 0.125D;
            double d5 = (double) (rand.nextFloat() * (float) k);
            world.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        inventory.deserializeNBT(tag.getCompoundTag("inventory"));
        setActive(tag.getBoolean("isActive"));
        activityCooldown = tag.getInteger("activityCooldown");
        previousRedstoneState = tag.getBoolean("powered");
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);
        tag.setTag("inventory", inventory.serializeNBT());
        tag.setBoolean("isActive", getActive());
        tag.setInteger("activityCooldown", activityCooldown);
        tag.setBoolean("powered", previousRedstoneState);
        return tag;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, -1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
    }
    public void setActive(boolean newState) {
        if (newState != this.getActive() && world != null) {
            if (newState) {
                world.playSound(null, pos, FLSounds.CHARGE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                double centerX = pos.getX() + 0.5;
                double centerY = pos.getY() + 1;
                double centerZ = pos.getZ() + 0.5;

                double circleRadius = 0.5;

                for (int i = 0; i < 360; i += 10) {
                    double angleRadians = Math.toRadians(i);
                    double offsetX = Math.cos(angleRadians) * circleRadius;
                    double offsetY = 0;
                    double offsetZ = Math.sin(angleRadians) * circleRadius;

                    double motionX = offsetX * 0.1;
                    double motionY = offsetY * 0.1;
                    double motionZ = offsetZ * 0.1;

                    this.getWorld().spawnParticle(EnumParticleTypes.DRAGON_BREATH,
                            centerX + offsetX, centerY + offsetY, centerZ + offsetZ,
                            motionX, motionY, motionZ);
                }
            } else
            {
                world.playSound(null, pos, FLSounds.UNCHARGE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                for (int i = 0; i < world.rand.nextInt(35) + 10; ++i)
                {
                    this.getWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, centeredX + world.rand.nextGaussian() * 0.12999999523162842D,
                            getPos().getY() + 1 + world.rand.nextGaussian() * 0.12999999523162842D,
                            centeredZ + world.rand.nextGaussian() * 0.12999999523162842D,
                            0.0D, 0.0D, 0.0D);
                }

            }
        }
        this.isActive = newState;
    }
    public boolean getActive() {
        return isActive;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> cap, EnumFacing side) {
        return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(cap, side);
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> cap, EnumFacing side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
        }
        return super.getCapability(cap, side);
    }

    public IItemHandlerModifiable getInventory() {
        return inventory;
    }

    public int getActivityCooldown() {
        return activityCooldown;
    }

    public void setActivityCooldown(int i) {
        activityCooldown = i;
    }

    public void decrementActivityCooldown() {
        activityCooldown--;
    }

    public AxisAlignedBB getEffectBounds() {
        return new AxisAlignedBB(getPos().add(-RANGE, -RANGE, -RANGE), getPos().add(RANGE, RANGE, RANGE));
    }
    @Override
    public final NBTTagCompound getUpdateTag()
    {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, @Nonnull IBlockState state, @Nonnull IBlockState newState)
    {
        return state.getBlock() != newState.getBlock();
    }
}
