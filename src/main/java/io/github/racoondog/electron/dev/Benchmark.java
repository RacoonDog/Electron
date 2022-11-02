package io.github.racoondog.electron.dev;

import com.google.common.math.BigIntegerMath;
import io.github.racoondog.electron.Electron;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.commons.lang3.time.StopWatch;
import org.jetbrains.annotations.Range;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.Deque;

@Environment(EnvType.CLIENT)
public class Benchmark {
    private final StopWatch stopWatch = StopWatch.create();
    private final Deque<Long> results = new ArrayDeque<>();
    private final String identifier;
    private final int size;

    public Benchmark(String identifier, int size) {
        this.identifier = identifier;
        this.size = size;
    }

    public void run(Runnable runnable) {
        stopWatch.reset();
        stopWatch.start();
        runnable.run();
        stopWatch.stop();
        addResult(stopWatch.getNanoTime());
    }

    public LongList getResults() {
        return new LongArrayList(results);
    }

    public LongList getSortedResults() {
        LongList list = this.getResults();
        list.sort(Long::compare);
        return list;
    }

    private long mean = -1;
    public long getMean() {
        if (mean == -1) {
            BigInteger sum = BigInteger.ONE;
            results.removeLast(); // Go fuck yourself
            for (long time : results) {
                sum = sum.add(BigInteger.valueOf(time));
            }
            sum = BigIntegerMath.divide(sum, BigInteger.valueOf(results.size()), RoundingMode.HALF_UP);
            mean = sum.longValueExact();
        }
        return mean;
    }

    public long getMedian() {
        LongList list = this.getSortedResults();
        return list.getLong((results.size() / 2) - 1);
    }

    public long getPercentile(@Range(from = 1, to = 100) int i) {
        LongList list = this.getSortedResults();
        return list.getLong((int) (Math.ceil(i / 100D * results.size()) - 1));
    }

    public long getVariance() {
        long variance = 0;
        long mean = this.getMean();
        for (long time : results) {
            variance += Math.pow(time - mean, 2);
        }
        return variance / (results.size() - 1);
    }

    public double getStandardDeviation() {
        return Math.sqrt(this.getVariance());
    }

    public void log() {
        Electron.LOG.info("{} Benchmark Results:", this.identifier);
        Electron.LOG.info("Standard Deviation: {} ns", this.getStandardDeviation());
        Electron.LOG.info("Mean: {} ns", this.getMean());
        if (results.size() >= 2) Electron.LOG.info("Median: {} ns", this.getMedian());
        if (results.size() >= 10) Electron.LOG.info("90th Percentile: {} ns", this.getPercentile(90));
        if (results.size() >= 20) Electron.LOG.info("95th Percentile: {} ns", this.getPercentile(95));
        if (results.size() >= 100) Electron.LOG.info("99th Percentile: {} ns", this.getPercentile(99));
        Electron.LOG.info("Raw Values: {}", resultsToStr(this.getResults()));
    }

    public StopWatch getStopWatch() {
        return this.stopWatch;
    }

    public void addResult(long result) {
        results.addFirst(result);
        if (results.size() > size) results.removeFirst();
        mean = -1; //reset mean
    }

    private static String resultsToStr(LongList list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i <= list.size() - 1; i++) {
            sb.append(list.getLong(i));
            if (i != (list.size() - 1)) sb.append(", ");
        }
        return sb.append(']').toString();
    }

    public static void bench(String identifier, Runnable runnable) {
        StopWatch stopWatch = StopWatch.createStarted();
        runnable.run();
        stopWatch.stop();
        Electron.LOG.info("{} took {} ns.", identifier, stopWatch.getNanoTime());
    }
}
