package com.aeternal.flowingtime;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber(modid = FlowingTime.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue TIME_PED_BONUS;
    private static final ForgeConfigSpec.DoubleValue TIME_PED_MOB_SLOWNESS;
    private static final ForgeConfigSpec.BooleanValue ALLOW_SPEED_UP_RANDOM_TICKS;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> TIME_WATCH_BLOCK_BLACKLIST;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> TIME_WATCH_TE_BLACKLIST;

    static {
        BUILDER.push("pedestal");
        TIME_PED_BONUS = BUILDER
                .comment("Bonus ticks given by the Watch of Flowing Time while in the pedestal. 0 = effectively no bonus.")
                .defineInRange("timePedBonus", 18, 0, 256);
        TIME_PED_MOB_SLOWNESS = BUILDER
                .comment("Factor the Watch slows down mobs by while in the pedestal. Set to 1.0 for no slowdown.")
                .defineInRange("timePedMobSlowness", 0.10, 0.0, 1.0);
        BUILDER.pop();

        BUILDER.push("watch");
        ALLOW_SPEED_UP_RANDOM_TICKS = BUILDER
                .comment("The default setting disables random ticks acceleration, which is capped at (8) depending on the mode.",
                        "Enabling this permits acceleration by the watch mode, with an upper limit of up to 30.",
                        "WARNING: MAY CAUSE TPS DROP!")
                .define("allowSpeedUpRandomTicksOnMode", false);
        BUILDER.pop();

        BUILDER.push("blacklists");
        TIME_WATCH_BLOCK_BLACKLIST = BUILDER
                .comment("Blocks to exclude from time acceleration")
                .defineListAllowEmpty(Collections.singletonList("timeWatchBlockBlacklist"),
                        ArrayList::new, o -> o instanceof String);
        TIME_WATCH_TE_BLACKLIST = BUILDER
                .comment("Block entities to exclude from time acceleration")
                .defineListAllowEmpty(Collections.singletonList("timeWatchTEBlacklist"),
                        () -> new ArrayList<>(List.of("flowingtime:flpedestal")),
                        o -> o instanceof String);
        BUILDER.pop();
    }

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int timePedBonus;
    public static double timePedMobSlowness;
    public static boolean allowSpeedUpRandomTicksOnMode;
    public static List<String> timeWatchBlockBlacklist = new ArrayList<>();
    public static List<String> timeWatchTEBlacklist = new ArrayList<>(List.of("flowingtime:flpedestal"));

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        timePedBonus = TIME_PED_BONUS.get();
        timePedMobSlowness = TIME_PED_MOB_SLOWNESS.get();
        allowSpeedUpRandomTicksOnMode = ALLOW_SPEED_UP_RANDOM_TICKS.get();
        timeWatchBlockBlacklist = new ArrayList<>(TIME_WATCH_BLOCK_BLACKLIST.get());
        timeWatchTEBlacklist = new ArrayList<>(TIME_WATCH_TE_BLACKLIST.get());
    }
}