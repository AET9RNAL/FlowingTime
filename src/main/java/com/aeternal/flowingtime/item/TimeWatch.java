package com.aeternal.flowingtime.item;

import com.aeternal.flowingtime.ModConfig;
import com.aeternal.flowingtime.api.item.IItemCharge;
import com.aeternal.flowingtime.api.item.IModeChanger;
import com.aeternal.flowingtime.api.item.IPedestalItem;
import com.aeternal.flowingtime.block.entity.FLPedestalBlockEntity;
import com.aeternal.flowingtime.util.ItemHelper;
import com.aeternal.flowingtime.util.WorldHelper;
import com.google.common.collect.Sets;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TimeWatch extends Item implements IModeChanger, IPedestalItem, IItemCharge {

    public static final String TAG_ACTIVE = "Active";
    public static final String TAG_MODE = "Mode";

    private static final Set<String> internalBlacklist = Sets.newHashSet(
            "com.aeternal.flowingtime.block.entity.FLPedestalBlockEntity"
    );

    public TimeWatch(Properties properties) {
        super(properties);
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            byte current = getTimeBoost(stack);
            setTimeBoost(stack, (byte) (current == 2 ? 0 : current + 1));
            player.sendSystemMessage(Component.translatable("fl.timewatch.mode_switch",
                    Component.translatable(getTimeName(stack)).getString()));
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (!(entity instanceof Player) || slotId > 8) {
            return;
        }

        byte timeControl = getTimeBoost(stack);

        if (level.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)) {
            if (timeControl == 1) {
                long newTime = level.getDayTime() + ((long) (getCharge(stack) + 1) * 4);
                if (newTime < level.getDayTime()) {
                    newTime = Long.MAX_VALUE;
                }
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.setDayTime(newTime);
                }
            } else if (timeControl == 2) {
                long newTime = level.getDayTime() - ((long) (getCharge(stack) + 1) * 4);
                if (newTime < 0) {
                    newTime = 0;
                }
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.setDayTime(newTime);
                }
            }
        }

        if (level.isClientSide || !ItemHelper.getOrCreateCompound(stack).getBoolean(TAG_ACTIVE)) {
            return;
        }

        Player player = (Player) entity;
        int charge = this.getCharge(stack);
        int bonusTicks;
        float mobSlowdown;

        if (charge == 0) {
            bonusTicks = 8;
            mobSlowdown = 0.25F;
        } else if (charge == 1) {
            bonusTicks = 12;
            mobSlowdown = 0.16F;
        } else if (charge == 2) {
            bonusTicks = 24;
            mobSlowdown = 0.10F;
        } else {
            bonusTicks = 30;
            mobSlowdown = 0.8F;
        }

        AABB bBox = player.getBoundingBox().inflate(8);

        if (ModConfig.allowSpeedUpRandomTicksOnMode) {
            speedUpRandomTicks(level, bonusTicks, bBox);
        } else {
            speedUpRandomTicks(level, 8, bBox);
        }

        slowMobs(level, bBox, mobSlowdown);
    }

    private void slowMobs(Level level, AABB bBox, float mobSlowdown) {
        if (bBox == null) return;
        for (Mob mob : level.getEntitiesOfClass(Mob.class, bBox)) {
            Vec3 motion = mob.getDeltaMovement();
            if (motion.x != 0 || motion.z != 0) {
                mob.setDeltaMovement(motion.x * mobSlowdown, motion.y, motion.z * mobSlowdown);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void speedUpBlockEntities(Level level, int bonusTicks, AABB bBox) {
        if (bBox == null || bonusTicks == 0) return;

        List<String> blacklist = ModConfig.timeWatchTEBlacklist;
        List<BlockEntity> list = WorldHelper.getBlockEntitiesWithinAABB(level, bBox);

        for (int i = 0; i < bonusTicks; i++) {
            for (BlockEntity be : list) {
                if (be.isRemoved()) continue;
                ResourceLocation beKey = ForgeRegistries.BLOCK_ENTITY_TYPES.getKey(be.getType());
                if (beKey != null && blacklist.contains(beKey.toString())) continue;
                if (internalBlacklist.contains(be.getClass().getName())) continue;

                BlockState state = level.getBlockState(be.getBlockPos());
                if (state.getBlock() instanceof EntityBlock entityBlock) {
                    BlockEntityTicker<BlockEntity> ticker =
                            (BlockEntityTicker<BlockEntity>) entityBlock.getTicker(level, state, be.getType());
                    if (ticker != null) {
                        ticker.tick(level, be.getBlockPos(), state, be);
                    }
                }
            }
        }
    }

    private void speedUpRandomTicks(Level level, int bonusTicks, AABB bBox) {
        if (bBox == null || bonusTicks == 0) return;
        if (!(level instanceof ServerLevel serverLevel)) return;

        List<String> blacklist = ModConfig.timeWatchBlockBlacklist;
        for (BlockPos pos : WorldHelper.getPositionsFromBox(bBox)) {
            for (int i = 0; i < bonusTicks; i++) {
                BlockState state = level.getBlockState(pos);
                Block block = state.getBlock();
                if (state.isRandomlyTicking()
                        && !blacklist.contains(ForgeRegistries.BLOCKS.getKey(block).toString())
                        && !(block instanceof LiquidBlock)
                        && !(block instanceof IPlantable)) {
                    state.randomTick(serverLevel, pos, serverLevel.random);
                }
            }
        }
    }

    private String getTimeName(ItemStack stack) {
        byte mode = getTimeBoost(stack);
        switch (mode) {
            case 0: return "fl.timewatch.off";
            case 1: return "fl.timewatch.ff";
            case 2: return "fl.timewatch.rw";
            default: return "ERROR_INVALID_MODE";
        }
    }

    private byte getTimeBoost(ItemStack stack) {
        return ItemHelper.getOrCreateCompound(stack).getByte("TimeMode");
    }

    private void setTimeBoost(ItemStack stack, byte time) {
        ItemHelper.getOrCreateCompound(stack).putByte("TimeMode", (byte) Mth.clamp(time, 0, 2));
    }

    @Override
    public byte getMode(@Nonnull ItemStack stack) {
        return ItemHelper.getOrCreateCompound(stack).getBoolean(TAG_ACTIVE) ? (byte) 1 : 0;
    }

    @Override
    public boolean changeMode(@Nonnull Player player, @Nonnull ItemStack stack, @Nullable InteractionHand hand) {
        CompoundTag tag = ItemHelper.getOrCreateCompound(stack);
        tag.putBoolean(TAG_ACTIVE, !tag.getBoolean(TAG_ACTIVE));
        return true;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level,
                                @Nonnull List<Component> list, @Nonnull TooltipFlag flags) {
        list.add(Component.translatable("fl.timewatch.tooltip1"));
        list.add(Component.translatable("fl.timewatch.tooltip2"));
        if (stack.hasTag()) {
            list.add(Component.translatable("fl.timewatch.mode",
                    Component.translatable(getTimeName(stack))));
        }
    }

    @Override
    public void updateInPedestal(@Nonnull Level level, @Nonnull BlockPos pos) {
        if (!level.isClientSide) {
            BlockEntity te = level.getBlockEntity(pos);
            if (te instanceof FLPedestalBlockEntity pedestal) {
                AABB bBox = pedestal.getEffectBounds();
                int bonusTicks = ModConfig.timePedBonus;
                if (bonusTicks > 0) {
                    speedUpBlockEntities(level, bonusTicks, bBox);
                    speedUpRandomTicks(level, bonusTicks, bBox);
                }
                if (ModConfig.timePedMobSlowness < 1.0) {
                    slowMobs(level, bBox, (float) ModConfig.timePedMobSlowness);
                }
            }
        }
    }

    @Nonnull
    @Override
    public List<Component> getPedestalDescription() {
        List<Component> list = new ArrayList<>();
        if (ModConfig.timePedBonus > 0) {
            list.add(Component.translatable("fl.timewatch.pedestal1", ModConfig.timePedBonus)
                    .withStyle(ChatFormatting.BLUE));
        }
        return list;
    }

    public static void blacklist(Class<? extends BlockEntity> clazz) {
        internalBlacklist.add(clazz.getName());
    }

    @Override
    public int getNumCharges(@Nonnull ItemStack stack) {
        return 4;
    }

    @Override
    public boolean isBarVisible(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(@Nonnull ItemStack stack) {
        return Math.round((float) getCharge(stack) / getNumCharges(stack) * 13.0F);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, @Nonnull ItemStack newStack, boolean slotChanged) {
        if (oldStack.getItem() != newStack.getItem()) return true;

        boolean diffActive = oldStack.hasTag() && newStack.hasTag()
                && oldStack.getTag().contains(TAG_ACTIVE) && newStack.getTag().contains(TAG_ACTIVE)
                && oldStack.getTag().getBoolean(TAG_ACTIVE) != newStack.getTag().getBoolean(TAG_ACTIVE);

        boolean diffMode = oldStack.hasTag() && newStack.hasTag()
                && oldStack.getTag().contains(TAG_MODE) && newStack.getTag().contains(TAG_MODE)
                && oldStack.getTag().getByte(TAG_MODE) != newStack.getTag().getByte(TAG_MODE);

        return diffActive || diffMode;
    }
}