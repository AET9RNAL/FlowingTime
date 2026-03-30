package com.aeternal.flowingtime;

import net.minecraftforge.fml.ModList;

public final class Constants {

    public static final String MOD_ID = FlowingTime.MOD_ID;

    private static Boolean cachedProjectELoaded = null;

    public static boolean isProjectELoaded() {
        if (cachedProjectELoaded == null) {
            cachedProjectELoaded = ModList.get().isLoaded("projecte");
        }
        return cachedProjectELoaded;
    }

    private static Boolean cachedCuriosLoaded = null;

    public static boolean isCuriosLoaded() {
        if (cachedCuriosLoaded == null) {
            cachedCuriosLoaded = ModList.get().isLoaded("curios");
        }
        return cachedCuriosLoaded;
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    private Constants() {}
}