package com.aeternal.flowingtime.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.aeternal.flowingtime.Config;
import com.aeternal.flowingtime.api.item.IItemCharge;
import com.aeternal.flowingtime.api.item.IModeChanger;
import com.aeternal.flowingtime.api.item.IPedestalItem;
import com.aeternal.flowingtime.common.item.ItemMod;
import com.aeternal.flowingtime.gameObjs.tiles.FLPedestalTile;
import com.aeternal.flowingtime.utils.ItemHelper;
import com.aeternal.flowingtime.utils.WorldHelper;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class TimeWatch extends ItemMod implements IModeChanger, IBauble, IPedestalItem, IItemCharge

{
    private int bonusTicks;
    private boolean allowspeedUpRandomTicksOnMode = Config.allowspeedUpRandomTicksOnMode;
    private static final Set<String> internalBlacklist = Sets.newHashSet(
            "Reika.ChromatiCraft.TileEntity.AOE.TileEntityAccelerator",
            "com.sci.torcherino.tile.TileTorcherino",
            "com.sci.torcherino.tile.TileCompressedTorcherino",
            "thaumcraft.common.tiles.crafting.TileSmelter",
            "com.aeternal.flowingtime.gameObjs.tiles.FLPedestalTile"
    );

    public TimeWatch() {
        super("timewatch");
        this.setMaxStackSize(1);
        this.setNoRepair();
        this.addPropertyOverride(ACTIVE_NAME, ACTIVE_GETTER);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote)
        {
            byte current = getTimeBoost(stack);

            setTimeBoost(stack, (byte) (current == 2 ? 0 : current + 1));

            player.sendMessage(new TextComponentTranslation("fl.timewatch.mode_switch", new TextComponentTranslation(getTimeName(stack)).getUnformattedComponentText()));
        }

        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int invSlot, boolean isHeld)
    {
        super.onUpdate(stack, world, entity, invSlot, isHeld);

        if (!(entity instanceof EntityPlayer) || invSlot > 8)
        {
            return;
        }
        byte timeControl = getTimeBoost(stack);

        if (world.getGameRules().getBoolean("doDaylightCycle")) {
            if (timeControl == 1)
            {
                if (world.getWorldTime() + ((getCharge(stack) + 1) * 4) > Long.MAX_VALUE)
                {
                    world.setWorldTime(Long.MAX_VALUE);
                }
                else
                {
                    world.setWorldTime((world.getWorldTime() + ((getCharge(stack) + 1) * 4)));
                }
            }
            else if (timeControl == 2)
            {
                if (world.getWorldTime() - ((getCharge(stack) + 1) * 4) < 0)
                {
                    world.setWorldTime(0);
                }
                else
                {
                    world.setWorldTime((world.getWorldTime() - ((getCharge(stack) + 1) * 4)));
                }
            }
        }

        if (world.isRemote || !ItemHelper.getOrCreateCompound(stack).getBoolean(TAG_ACTIVE))
        {
            return;
        }

        EntityPlayer player = (EntityPlayer) entity;
        int charge = this.getCharge(stack);
     //   int bonusTicks;
        float mobSlowdown;

        if (charge == 0)
        {
            bonusTicks = 8;
            mobSlowdown = 0.25F;
        }
        else if (charge == 1)
        {
            bonusTicks = 12;
            mobSlowdown = 0.16F;
        }
        else if (charge == 2)
        {
            bonusTicks = 24;
            mobSlowdown = 0.10F;
        }
        else
        {
            bonusTicks = 30;
            mobSlowdown = 0.8F;
        }

        AxisAlignedBB bBox = player.getEntityBoundingBox().grow(8);

        if (allowspeedUpRandomTicksOnMode) {
            speedUpRandomTicks(world, bonusTicks, bBox);
        } else {
            int limitedBonusTicks = 8;
            speedUpRandomTicks(world, limitedBonusTicks, bBox);
        }

       /* speedUpRandomTicks(world, bonusTicks, bBox);*/
        slowMobs(world, bBox, mobSlowdown);
    }

    private void slowMobs(World world, AxisAlignedBB bBox, float mobSlowdown)
    {
        if (bBox == null) // Sanity check for chunk unload weirdness
        {
            return;
        }
        for (Object obj : world.getEntitiesWithinAABB(EntityLiving.class, bBox))
        {
            Entity ent = (Entity) obj;

            if (ent.motionX != 0)
            {
                ent.motionX *= mobSlowdown;
            }

            if (ent.motionZ != 0)
            {
                ent.motionZ *= mobSlowdown;
            }
        }
    }

    private void speedUpTileEntities(World world, int bonusTicks, AxisAlignedBB bBox)
    {
        if (bBox == null || bonusTicks == 0) // Sanity check the box for chunk unload weirdness
        {
            return;
        }

        List<String> blacklist = Arrays.asList(Config.timeWatchTEBlacklist);
        List<TileEntity> list = WorldHelper.getTileEntitiesWithinAABB(world, bBox);
        for (int i = 0; i < bonusTicks; i++)
        {
            for (TileEntity tile : list)
            {
                if (!tile.isInvalid() && tile instanceof ITickable
                        && !internalBlacklist.contains(tile.getClass().toString())
                        && !blacklist.contains(TileEntity.getKey(tile.getClass()).toString()))
                {
                    ((ITickable) tile).update();
                }
            }
        }
    }


    private void speedUpRandomTicks(World world, int bonusTicks, AxisAlignedBB bBox)
    {
        if (bBox == null || bonusTicks == 0) // Sanity check the box for chunk unload weirdness
        {
            return;
        }

        List<String> blacklist = Arrays.asList(Config.timeWatchBlockBlacklist);
        for (BlockPos pos : WorldHelper.getPositionsFromBox(bBox))
        {
            for (int i = 0; i < bonusTicks; i++)
            {
                IBlockState state = world.getBlockState(pos);
                Block block = state.getBlock();
                if (block.getTickRandomly()
                        && !blacklist.contains(block.getRegistryName().toString())
                        && !(block instanceof BlockLiquid) // Don't speed vanilla non-source blocks - dupe issues
                        && !(block instanceof BlockFluidBase) // Don't speed Forge fluids - just in case of dupes as well
                        && !(block instanceof IGrowable)
                        && !(block instanceof IPlantable)) // All plants should be sped using Harvest Goddess
                {
                    block.updateTick(world, pos, state, itemRand);
                }
            }
        }
    }

    private String getTimeName(ItemStack stack)
    {
        byte mode = getTimeBoost(stack);
        switch (mode)
        {
            case 0:
                return "fl.timewatch.off";
            case 1:
                return "fl.timewatch.ff";
            case 2:
                return "fl.timewatch.rw";
            default:
                return "ERROR_INVALID_MODE";
        }
    }

    private byte getTimeBoost(ItemStack stack)
    {
        return ItemHelper.getOrCreateCompound(stack).getByte("TimeMode");
    }

    private void setTimeBoost(ItemStack stack, byte time)
    {
        ItemHelper.getOrCreateCompound(stack).setByte("TimeMode", (byte) MathHelper.clamp(time, 0, 2));
    }


    @Override
    public byte getMode(@Nonnull ItemStack stack)
    {
        return ItemHelper.getOrCreateCompound(stack).getBoolean(TAG_ACTIVE) ? (byte) 1 : 0;
    }

    @Override
    public boolean changeMode(@Nonnull EntityPlayer player, @Nonnull ItemStack stack, EnumHand hand)
    {
        NBTTagCompound tag = ItemHelper.getOrCreateCompound(stack);
        tag.setBoolean(TAG_ACTIVE, !tag.getBoolean(TAG_ACTIVE));
        return true;
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flags)
    {
        list.add(I18n.format("fl.timewatch.tooltip1"));
        list.add(I18n.format("fl.timewatch.tooltip2"));

        if (stack.hasTagCompound())
        {
            list.add(I18n.format("fl.timewatch.mode",
                    I18n.format(getTimeName(stack))));
        }
    }

    @Override
    @Optional.Method(modid = "baubles")
    public baubles.api.BaubleType getBaubleType(ItemStack itemstack)
    {
        return BaubleType.BELT;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public void onWornTick(ItemStack stack, EntityLivingBase player)
    {
        this.onUpdate(stack, player.getEntityWorld(), player, 0, false);
    }

    @Override
    @Optional.Method(modid = "baubles")
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {}

    @Override
    @Optional.Method(modid = "baubles")
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {}

    @Override
    @Optional.Method(modid = "baubles")
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player)
    {
        return true;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player)
    {
        return true;
    }

    @Override
    public void updateInPedestal(@Nonnull World world, @Nonnull BlockPos pos) {
        // Use the stored bonusTicks value here
        if (!world.isRemote) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof FLPedestalTile) {
                AxisAlignedBB bBox = ((FLPedestalTile) te).getEffectBounds();
                if (bonusTicks > 0) { // Use the class-level variable here
                    speedUpTileEntities(world, bonusTicks, bBox);
                    speedUpRandomTicks(world, bonusTicks, bBox);
                }

                if (Config.timePedMobSlowness < 1.0F) {
                    slowMobs(world, bBox, Config.timePedMobSlowness);
                }
            }
        }
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    @Override
    public List<String> getPedestalDescription()
    {
        List<String> list = new ArrayList<>();
        if (bonusTicks > 0) {
            list.add(TextFormatting.BLUE + I18n.format("fl.timewatch.pedestal1", bonusTicks));
        }
        if (Config.timePedMobSlowness < 1.0F)
        {
            list.add(TextFormatting.BLUE + I18n.format("fl.timewatch.pedestal2", Config.timePedMobSlowness));
        }
        return list;
    }

    public static void blacklist(Class<? extends TileEntity> clazz)
    {
        internalBlacklist.add(clazz.getName());
    }

    @Override
    public int getNumCharges(@Nonnull ItemStack stack)
    {
        return 4;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack)
    {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        return 1.0D - (double) getCharge(stack) / getNumCharges(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

}
