package com.aeternal.flowingtime.block;

import com.aeternal.flowingtime.api.item.IPedestalItem;
import com.aeternal.flowingtime.block.entity.FLPedestalBlockEntity;
import com.aeternal.flowingtime.api.annotation.AutoRegisterBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import com.aeternal.flowingtime.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@AutoRegisterBlock(value = "flpedestal", hasItem = true)
public class PedestalBlock extends BaseEntityBlock {

    private static final VoxelShape SHAPE = Block.box(3, 0, 3, 13, 12, 13);

    public PedestalBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL)
                .strength(1.0f)
                .lightLevel(s -> 12)
                .noOcclusion());
    }
    @SuppressWarnings("all")
    @Nonnull
    @Override
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE;
    }

    @SuppressWarnings("all")
    @Nonnull
    @Override
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.MODEL;
    }

    @SuppressWarnings("all")
    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new FLPedestalBlockEntity(pos, state);
    }

    @SuppressWarnings("all")
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.FL_PEDESTAL.get(), FLPedestalBlockEntity::tick);
    }
    
    @SuppressWarnings("all")
    @Override
    public void attack(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player) {
        if (!level.isClientSide) {
            dropItem(level, pos);
            level.sendBlockUpdated(pos, state, state, 8);
        }
    }

    @SuppressWarnings("all")
    @Nonnull
    @Override
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos,
                                 @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntity te = level.getBlockEntity(pos);
            if (!(te instanceof FLPedestalBlockEntity tile)) {
                return InteractionResult.SUCCESS;
            }

            ItemStack item = tile.getInventory().getStackInSlot(0);
            ItemStack stack = player.getItemInHand(hand);

            if (stack.isEmpty() && !item.isEmpty() && item.getItem() instanceof IPedestalItem) {
                tile.setActive(!tile.getActive());
                level.sendBlockUpdated(pos, state, state, 8);
            } else if (!stack.isEmpty() && item.isEmpty()) {
                tile.getInventory().setStackInSlot(0, stack.split(1));
                if (stack.isEmpty()) {
                    player.setItemInHand(hand, ItemStack.EMPTY);
                }
                level.sendBlockUpdated(pos, state, state, 8);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @SuppressWarnings("all")
    @Override
    public void onRemove(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos,
                         @Nonnull BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            dropItem(level, pos);
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @SuppressWarnings("all")
    @Override
    public void neighborChanged(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos,
                                @Nonnull Block neighbor, @Nonnull BlockPos neighborPos, boolean isMoving) {
        boolean powered = level.hasNeighborSignal(pos);
        BlockEntity te = level.getBlockEntity(pos);

        if (te instanceof FLPedestalBlockEntity ped) {
            if (ped.previousRedstoneState != powered) {
                if (powered && !ped.getInventory().getStackInSlot(0).isEmpty()
                        && ped.getInventory().getStackInSlot(0).getItem() instanceof IPedestalItem) {
                    ped.setActive(!ped.getActive());
                    level.sendBlockUpdated(pos, state, state, 11);
                }
                ped.previousRedstoneState = powered;
            }
        }
    }

    @SuppressWarnings("all")
    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable BlockGetter level,
                                @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
        tooltip.add(Component.translatable("fl.pedestal.tooltip1"));
        tooltip.add(Component.translatable("fl.pedestal.tooltip2"));
    }

    private void dropItem(Level level, BlockPos pos) {
        BlockEntity te = level.getBlockEntity(pos);
        if (te instanceof FLPedestalBlockEntity tile) {
            ItemStack stack = tile.getInventory().getStackInSlot(0);
            if (!stack.isEmpty()) {
                tile.getInventory().setStackInSlot(0, ItemStack.EMPTY);
                ItemEntity entity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, stack);
                level.addFreshEntity(entity);
            }
        }
    }
}