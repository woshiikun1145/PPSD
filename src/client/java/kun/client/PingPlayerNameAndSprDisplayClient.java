// src/main/java/kun/client/PingPlayerNameAndSprDisplayClient.java
package kun.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class PingPlayerNameAndSprDisplayClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // 初始化数据追踪器（用于注册包监听 Mixin）
        PacketTracker.init();

        // 注册 HUD 渲染（忽略弃用警告，或改用 RenderEvents）
        HudRenderCallback.EVENT.register(new HudRenderer());

        // 注册热键
        KeyBindings.register();

        // 连接事件
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> PacketTracker.reset());
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> PacketTracker.onDisconnect());

        // 每秒采样
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.getNetworkHandler() == null) return;
            long now = System.currentTimeMillis();
            if (now - PacketTracker.getLastSampleTime() >= 1000) {
                int ping = getCurrentPing(client);
                double spr = PacketTracker.getCurrentSpr();
                PacketTracker.addSample(ping, spr);
                PacketTracker.updateLastSampleTime(now);
            }
        });
    }

    private int getCurrentPing(MinecraftClient client) {
        var entry = client.getNetworkHandler().getPlayerListEntry(client.player.getUuid());
        return entry != null ? entry.getLatency() : 0;
    }
}