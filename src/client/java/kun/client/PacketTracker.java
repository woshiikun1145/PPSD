// src/main/java/kun/client/PacketTracker.java
package kun.client;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class PacketTracker {
    private static final int MAX_RECENT_SAMPLES = 60;
    private static final Deque<PingSample> pingHistory = new ConcurrentLinkedDeque<>();
    private static final Deque<SprSample> sprHistory = new ConcurrentLinkedDeque<>();
    private static final List<PingSample> allPingHistory = new ArrayList<>();
    private static final List<SprSample> allSprHistory = new ArrayList<>();

    // SPR 实时计数：最近1秒内的数据包时间戳队列
    private static final Deque<Long> packetTimestampQueue = new ConcurrentLinkedDeque<>();
    private static double currentSpr = 0.0;
    private static long lastSampleTime = 0;
    private static boolean connected = false;

    public static void init() {
        // 无需额外初始化，Mixin 会调用 onPacketReceived
    }

    // 由 Mixin 调用：每次收到数据包时记录
    public static void onPacketReceived() {
        if (!connected) return;
        long now = System.currentTimeMillis();
        packetTimestampQueue.add(now);
        // 清理超过1秒的旧时间戳
        while (!packetTimestampQueue.isEmpty() && packetTimestampQueue.peekFirst() < now - 1000) {
            packetTimestampQueue.pollFirst();
        }
        currentSpr = packetTimestampQueue.size();
    }

    public static double getCurrentSpr() {
        return currentSpr;
    }

    public static void addSample(int ping, double spr) {
        long now = System.currentTimeMillis();
        PingSample pSample = new PingSample(now, ping);
        SprSample sSample = new SprSample(now, spr);

        pingHistory.add(pSample);
        if (pingHistory.size() > MAX_RECENT_SAMPLES) pingHistory.pollFirst();
        sprHistory.add(sSample);
        if (sprHistory.size() > MAX_RECENT_SAMPLES) sprHistory.pollFirst();

        allPingHistory.add(pSample);
        allSprHistory.add(sSample);
    }

    public static void reset() {
        pingHistory.clear();
        sprHistory.clear();
        allPingHistory.clear();
        allSprHistory.clear();
        packetTimestampQueue.clear();
        currentSpr = 0.0;
        connected = true;
        lastSampleTime = System.currentTimeMillis();
    }

    public static void onDisconnect() {
        connected = false;
        pingHistory.clear();
        sprHistory.clear();
        allPingHistory.clear();
        allSprHistory.clear();
        packetTimestampQueue.clear();
        currentSpr = 0.0;
    }

    public static long getLastSampleTime() {
        return lastSampleTime;
    }

    public static void updateLastSampleTime(long time) {
        lastSampleTime = time;
    }

    public static boolean isConnected() {
        return connected;
    }

    // 统计辅助方法
    public static Stats calculatePingStats(Collection<PingSample> samples) {
        if (samples.isEmpty()) return new Stats(0, 0, 0);
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE, sum = 0;
        for (PingSample s : samples) {
            int v = s.value;
            min = Math.min(min, v);
            max = Math.max(max, v);
            sum += v;
        }
        return new Stats(sum / (double) samples.size(), min, max);
    }

    public static Stats calculateSprStats(Collection<SprSample> samples) {
        if (samples.isEmpty()) return new Stats(0, 0, 0);
        double min = Double.MAX_VALUE, max = Double.MIN_VALUE, sum = 0;
        for (SprSample s : samples) {
            double v = s.value;
            min = Math.min(min, v);
            max = Math.max(max, v);
            sum += v;
        }
        return new Stats(sum / samples.size(), min, max);
    }

    public static Collection<PingSample> getRecentPingSamples() {
        return pingHistory;
    }

    public static Collection<SprSample> getRecentSprSamples() {
        return sprHistory;
    }

    public static List<PingSample> getAllPingSamples() {
        return allPingHistory;
    }

    public static List<SprSample> getAllSprSamples() {
        return allSprHistory;
    }

    // 数据容器
    public static class PingSample {
        public final long timestamp;
        public final int value;
        public PingSample(long ts, int v) { timestamp = ts; value = v; }
    }

    public static class SprSample {
        public final long timestamp;
        public final double value;
        public SprSample(long ts, double v) { timestamp = ts; value = v; }
    }

    public static class Stats {
        public final double avg;
        public final double min;
        public final double max;
        public Stats(double avg, double min, double max) {
            this.avg = avg;
            this.min = min;
            this.max = max;
        }
    }
}