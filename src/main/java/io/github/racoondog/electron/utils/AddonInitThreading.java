package io.github.racoondog.electron.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.github.racoondog.electron.Electron;
import io.github.racoondog.electron.ElectronSystem;
import io.github.racoondog.electron.api.PostAddonInitEvent;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.addons.AddonManager;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.commons.lang3.time.StopWatch;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Environment(EnvType.CLIENT)
public final class AddonInitThreading {
    private static boolean initialized = false;
    private static int THREADS;
    private static @Nullable ExecutorService EXECUTOR;

    static {
        if (ElectronSystem.get().asynchronousAddonInit.get()) {
            THREADS = ElectronSystem.get().addonInitThreads.get();
            if (THREADS > 1) {
                ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("Addon Initializater %s").build();
                EXECUTOR = Executors.newFixedThreadPool(THREADS, factory);
            }
        }
    }

    public static void initializeAddons() throws InterruptedException {
        if (initialized) throw new RuntimeException("Already initialized addons.");
        initialized = true;

        StopWatch stopWatch = StopWatch.createStarted();

        boolean success = THREADS == 1 ? synchronous() : asynchronous();

        Electron.LOG.info(success ? "Initialized addons in {} ms." : "Failed to initialize addons in {} ms.", stopWatch.getTime(TimeUnit.MILLISECONDS));

        MeteorClient.EVENT_BUS.post(PostAddonInitEvent.get());
    }

    private static boolean asynchronous() throws InterruptedException {
        ThreadUtils.initLock = true;

        for (var addon : AddonManager.ADDONS) {
            EXECUTOR.execute(addon::onInitialize);
        }

        EXECUTOR.shutdown(); //Prevent executing additional tasks
        boolean success = EXECUTOR.awaitTermination(4, TimeUnit.MINUTES); //Wait for addon initialization to finish

        //post init
        ThreadUtils.registerObjects();
        ThreadUtils.initLock = false;

        return success;
    }

    private static boolean synchronous() {
        AddonManager.ADDONS.forEach(MeteorAddon::onInitialize);
        return true;
    }
}
