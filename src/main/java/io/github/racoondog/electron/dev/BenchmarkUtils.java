package io.github.racoondog.electron.dev;

import io.github.racoondog.electron.Electron;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.commons.lang3.time.StopWatch;

@Environment(EnvType.CLIENT)
public class BenchmarkUtils {
    public static void bench(String identifier, Runnable runnable) {
        StopWatch stopWatch = StopWatch.createStarted();
        runnable.run();
        stopWatch.stop();
        Electron.LOG.info("{} took {} ns.", identifier, stopWatch.getNanoTime());
    }
}
