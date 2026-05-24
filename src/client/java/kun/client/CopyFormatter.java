// src/client/java/kun/client/CopyFormatter.java
package kun.client;

import net.minecraft.client.MinecraftClient;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CopyFormatter {
    public static String formatSimple(MinecraftClient client) {
        int ping = getCurrentPing(client);
        double spr = PacketTracker.getCurrentSpr();
        String playerName = client.player != null ? client.player.getName().getString() : "Unknown";
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
        return String.format("Ping: %d ms  SPR: %.1f pkt/s Player Name: %s Time: %s",
                ping, spr, playerName, time);
    }

    public static String formatDetailed(MinecraftClient client) {
        String serverAddress = client.getCurrentServerEntry() != null ?
                client.getCurrentServerEntry().address : "Unknown";
        String playerName = client.player != null ? client.player.getName().getString() : "Unknown";
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));

        int pingNow = getCurrentPing(client);
        double sprNow = PacketTracker.getCurrentSpr();

        PacketTracker.Stats pingRecent = PacketTracker.calculatePingStats(PacketTracker.getRecentPingSamples());
        PacketTracker.Stats sprRecent = PacketTracker.calculateSprStats(PacketTracker.getRecentSprSamples());
        PacketTracker.Stats pingAll = PacketTracker.calculatePingStats(PacketTracker.getAllPingSamples());
        PacketTracker.Stats sprAll = PacketTracker.calculateSprStats(PacketTracker.getAllSprSamples());

        return String.format("""
                        Server: %s
                        Player Name: %s
                        Time: %s
                        Ping:
                            Now %d ms
                            In 1 minute
                                avg %.1f ms max %.0f ms min %.0f ms
                            Since I joined the server
                                avg %.1f ms max %.0f ms min %.0f ms
                        SPR:
                            Now %.1f pkt/s
                            In 1 minute
                                avg %.1f pkt/s max %.1f pkt/s min %.1f pkt/s
                            Since I joined the server
                                avg %.1f pkt/s max %.1f pkt/s min %.1f pkt/s""",
                serverAddress, playerName, time,
                pingNow,
                pingRecent.avg, pingRecent.max, pingRecent.min,
                pingAll.avg, pingAll.max, pingAll.min,
                sprNow,
                sprRecent.avg, sprRecent.max, sprRecent.min,
                sprAll.avg, sprAll.max, sprAll.min);
    }

    private static int getCurrentPing(MinecraftClient client) {
        if (client.getNetworkHandler() == null || client.player == null) return 0;
        var entry = client.getNetworkHandler().getPlayerListEntry(client.player.getUuid());
        return entry != null ? entry.getLatency() : 0;
    }
}