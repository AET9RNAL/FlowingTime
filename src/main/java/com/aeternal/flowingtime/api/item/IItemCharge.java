package com.aeternal.flowingtime.api.item;

import com.aeternal.flowingtime.registry.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IItemCharge {
    String KEY = "Charge";

    int getNumCharges(@Nonnull ItemStack stack);

    default int getCharge(@Nonnull ItemStack stack) {
        return stack.getOrCreateTag().getInt(KEY);
    }

    default boolean changeCharge(@Nonnull Player player, @Nonnull ItemStack stack, @Nullable InteractionHand hand) {
        int currentCharge = getCharge(stack);
        int numCharges = getNumCharges(stack);
        CompoundTag tag = stack.getOrCreateTag();

        if (player.isShiftKeyDown()) {
            if (currentCharge > 0) {
                player.level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        ModSounds.UNCHARGE.get(), SoundSource.PLAYERS,
                        1.0F, 0.5F + ((0.5F / (float) numCharges) * currentCharge));
                tag.putInt(KEY, currentCharge - 1);
                return true;
            }
        } else if (currentCharge < numCharges) {
            player.level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    ModSounds.CHARGE.get(), SoundSource.PLAYERS,
                    1.0F, 0.5F + ((0.5F / (float) numCharges) * currentCharge));
            tag.putInt(KEY, currentCharge + 1);
            return true;
        }

        return false;
    }
}