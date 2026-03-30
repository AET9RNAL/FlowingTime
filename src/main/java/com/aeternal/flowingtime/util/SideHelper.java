package com.aeternal.flowingtime.util;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public final class SideHelper {

    public static <T> T runOnClient(Supplier<Callable<T>> clientWork) {
        return DistExecutor.unsafeCallWhenOn(Dist.CLIENT, clientWork);
    }

    public static void executeOnClient(Supplier<Runnable> clientWork) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, clientWork);
    }

    public static boolean isClient() {
        return FMLEnvironment.dist == Dist.CLIENT;
    }

    public static boolean isServer() {
        return FMLEnvironment.dist == Dist.DEDICATED_SERVER;
    }

    private SideHelper() {}
}