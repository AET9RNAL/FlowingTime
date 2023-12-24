package com.aeternal.flowingtime;

import net.minecraftforge.common.config.Configuration;

import java.io.File;


public final class Config {
    public static int timePedBonus = 18;
    public static float timePedMobSlowness = 0.10F;
    public static boolean allowspeedUpRandomTicksOnMode;
    public static String[] timeWatchBlockBlacklist = {};
    public static String[] timeWatchTEBlacklist = {
            "flowingtime:flpedestal"
    };
    public static void loadNormalConfig(final File configFile) {
        final Configuration config = new Configuration(configFile);
        try {
            config.load();
            timePedBonus = config.getInt("TimePedestalBonus", "Pedestal",18, 0,256, "Bonus ticks given by the Watch of Flowing Time while in the pedestal. 0 = effectively no bonus.");
            timePedMobSlowness = config.getInt("TimePedestalMobSlowness", "Pedestal", (int) 0.11F, 0,1, "Factor the Watch of Flowing Time slows down mobs by while in the pedestal. Set to 1.0 for no slowdown.");
            allowspeedUpRandomTicksOnMode = config.getBoolean("Allow change RandomTicks on mode","Watch", false,"The default setting disables random ticks acceleration, which is capped at (8) depending on the mode. Enabling this feature by setting the value to true permits acceleration by the watch mode, with an upper limit of up to 30. WARNING: MAY CAUSE TPS DROP!");
        } catch (Exception e) {
            FlowingTime.log.fatal("Fatal error reading config file.", e);
            throw new RuntimeException(e);
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }

}





































