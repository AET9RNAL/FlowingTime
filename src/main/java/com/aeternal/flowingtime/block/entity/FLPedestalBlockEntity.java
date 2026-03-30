package com.aeternal.flowingtime.block.entity;

import com.aeternal.flowingtime.api.item.IPedestalItem;
import com.aeternal.flowingtime.registry.ModBlockEntities;
import com.aeternal.flowingtime.registry.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class FLPedestalBlockEntity extends BlockEntity {

    private static final int RANGE = 4;
    private boolean isActive = false;
    private final ItemStackHandler inventory = new ItemStackHandler(1);
    private final LazyOptional<IItemHandlerModifiable> inventoryCapability = LazyOptional.of(() -> inventory);
    private int particleCooldown = 10;
    private int activityCooldown = 0;
    public boolean previousRedstoneState = false;

    public FLPedestalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FL_PEDESTAL.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FLPedestalBlockEntity be) {
        if (be.getActive()) {
            if (!be.inventory.getStackInSlot(0).isEmpty()) {
                Item item = be.inventory.getStackInSlot(0).getItem();
                if (item instanceof IPedestalItem pedestalItem) {
                    pedestalItem.updateInPedestal(level, pos);
                }
                if (be.particleCooldown <= 0) {
                    be.spawnParticles();
                    be.particleCooldown = 10;
                } else {
                    be.particleCooldown--;
                }
            } else {
                be.setActive(false);
            }
        }
    }

    private void spawnParticles() {
        if (level == null || !level.isClientSide) return;

        int x = worldPosition.getX();
        int y = worldPosition.getY();
        int z = worldPosition.getZ();
        Random rand = new Random();

        // Flame particles around the base
        level.addParticle(ParticleTypes.FLAME, x + 0.2, y + 0.3, z + 0.2, 0, 0, 0);
        level.addParticle(ParticleTypes.FLAME, x + 0.2, y + 0.3, z + 0.5, 0, 0, 0);
        level.addParticle(ParticleTypes.FLAME, x + 0.2, y + 0.3, z + 0.8, 0, 0, 0);
        level.addParticle(ParticleTypes.FLAME, x + 0.5, y + 0.3, z + 0.2, 0, 0, 0);
        level.addParticle(ParticleTypes.FLAME, x + 0.5, y + 0.3, z + 0.8, 0, 0, 0);
        level.addParticle(ParticleTypes.FLAME, x + 0.8, y + 0.3, z + 0.2, 0, 0, 0);
        level.addParticle(ParticleTypes.FLAME, x + 0.8, y + 0.3, z + 0.5, 0, 0, 0);
        level.addParticle(ParticleTypes.FLAME, x + 0.8, y + 0.3, z + 0.8, 0, 0, 0);

        // Enchantment table particles
        for (int i = 0; i < 5; ++i) {
            double motionX = rand.nextGaussian() * 0.02;
            double motionY = -1.0 * rand.nextDouble();
            double motionZ = rand.nextGaussian() * 0.02;
            level.addParticle(ParticleTypes.ENCHANT,
                    x + rand.nextDouble(), worldPosition.getY() + 1.5, z + rand.nextDouble(),
                    motionX, motionY, motionZ);
        }

        // Portal particles
        for (int i = 0; i < 3; ++i) {
            int j = rand.nextInt(2) * 2 - 1;
            int k = rand.nextInt(2) * 2 - 1;
            double d0 = worldPosition.getX() + 0.5D + 0.25D * j;
            double d1 = worldPosition.getY() + rand.nextFloat();
            double d2 = worldPosition.getZ() + 0.5D + 0.25D * k;
            double d3 = rand.nextFloat() * j;
            double d4 = (rand.nextFloat() - 0.5D) * 0.125D;
            double d5 = rand.nextFloat() * k;
            level.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
        }
    }

    public void setActive(boolean newState) {
        if (newState != this.isActive && level != null) {
            if (newState) {
                level.playSound(null, worldPosition, ModSounds.CHARGE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                if (level.isClientSide) {
                    double centerX = worldPosition.getX() + 0.5;
                    double centerY = worldPosition.getY() + 1;
                    double centerZ = worldPosition.getZ() + 0.5;
                    double circleRadius = 0.5;
                    for (int i = 0; i < 360; i += 10) {
                        double angleRadians = Math.toRadians(i);
                        double offsetX = Math.cos(angleRadians) * circleRadius;
                        double offsetZ = Math.sin(angleRadians) * circleRadius;
                        level.addParticle(ParticleTypes.DRAGON_BREATH,
                                centerX + offsetX, centerY, centerZ + offsetZ,
                                offsetX * 0.1, 0, offsetZ * 0.1);
                    }
                }
            } else {
                level.playSound(null, worldPosition, ModSounds.UNCHARGE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                if (level.isClientSide) {
                    Random rand = new Random();
                    double centeredX = worldPosition.getX() + 0.5;
                    double centeredZ = worldPosition.getZ() + 0.5;
                    for (int i = 0; i < rand.nextInt(35) + 10; ++i) {
                        level.addParticle(ParticleTypes.SMOKE,
                                centeredX + rand.nextGaussian() * 0.13,
                                worldPosition.getY() + 1 + rand.nextGaussian() * 0.13,
                                centeredZ + rand.nextGaussian() * 0.13,
                                0.0, 0.0, 0.0);
                    }
                }
            }
        }
        this.isActive = newState;
        setChanged();
    }

    public boolean getActive() {
        return isActive;
    }

    public IItemHandlerModifiable getInventory() {
        return inventory;
    }

    public AABB getEffectBounds() {
        return new AABB(worldPosition.offset(-RANGE, -RANGE, -RANGE), worldPosition.offset(RANGE, RANGE, RANGE));
    }

    @Override
    public void load(@Nonnull CompoundTag tag) {
        super.load(tag);
        inventory.deserializeNBT(tag.getCompound("inventory"));
        isActive = tag.getBoolean("isActive");
        activityCooldown = tag.getInt("activityCooldown");
        previousRedstoneState = tag.getBoolean("powered");
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("inventory", inventory.serializeNBT());
        tag.putBoolean("isActive", isActive);
        tag.putInt("activityCooldown", activityCooldown);
        tag.putBoolean("powered", previousRedstoneState);
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return inventoryCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        inventoryCapability.invalidate();
    }
}
