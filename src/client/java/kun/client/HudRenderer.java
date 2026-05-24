// src/client/java/kun/client/HudRenderer.java
package kun.client;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Colors;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class HudRenderer implements HudRenderCallback {
    private static final int PING_WHITE = 150;
    private static final int PING_YELLOW = 300;
    private static final int PING_ORANGE = 1000;

    private static final double SPR_RED = 2.0;
    private static final double SPR_ORANGE = 5.0;
    private static final double SPR_YELLOW = 15.0;

    // 方块尺寸
    private static final int BAR_WIDTH = 2;
    private static final int BAR_HEIGHT_LOW = 2;
    private static final int BAR_HEIGHT_MID = 4;
    private static final int BAR_HEIGHT_HIGH = 6;
    private static final int BAR_SPACING = 0;

    // 方块Y轴偏移（正值向下移动，使方块相对于文字更靠下）
    private static final int BAR_Y_OFFSET = 6;

    @Override
    public void onHudRender(@NotNull DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.getNetworkHandler() == null) return;
        if (!PacketTracker.isConnected()) return;

        int ping = getCurrentPing(client);
        double spr = PacketTracker.getCurrentSpr();
        String playerName = client.player.getName().getString();

        int pingColor = getPingColor(ping);
        int sprColor = getSprColor(spr);
        int idColor = Colors.WHITE;

        String pingText = String.format("Ping: %d ms", ping);
        String sprText = String.format("SPR: %.1f pkt/s", spr);
        String idText = String.format("ID: %s", playerName);

        TextRenderer tr = client.textRenderer;
        int textY = client.getWindow().getScaledHeight() - tr.fontHeight - 5; // 文字基线
        int currentX = 5;

        // 绘制三个竖条，底部相对于文字基线向下偏移 BAR_Y_OFFSET
        int barBottomY = textY + BAR_Y_OFFSET;
        drawBar(drawContext, currentX, barBottomY, BAR_HEIGHT_LOW);
        currentX += BAR_WIDTH + BAR_SPACING;
        drawBar(drawContext, currentX, barBottomY, BAR_HEIGHT_MID);
        currentX += BAR_WIDTH + BAR_SPACING;
        drawBar(drawContext, currentX, barBottomY, BAR_HEIGHT_HIGH);
        currentX += BAR_WIDTH + BAR_SPACING + 2;

        // 绘制文字
        drawContext.drawText(tr, pingText, currentX, textY, pingColor, true);
        currentX += tr.getWidth(pingText) + 4;
        drawContext.drawText(tr, sprText, currentX, textY, sprColor, true);
        currentX += tr.getWidth(sprText) + 4;
        drawContext.drawText(tr, idText, currentX, textY, idColor, true);
    }

    private void drawBar(DrawContext ctx, int x, int bottomY, int height) {
        ctx.fill(x, bottomY - height, x + BAR_WIDTH, bottomY, Colors.WHITE);
    }

    private int getPingColor(int ping) {
        if (ping < PING_WHITE) return Colors.WHITE;
        if (ping < PING_YELLOW) return 0xFFD632;
        if (ping < PING_ORANGE) return 0xFF890C;
        return Colors.RED;
    }

    private int getSprColor(double spr) {
        if (spr <= SPR_RED) return Colors.RED;
        if (spr <= SPR_ORANGE) return 0xFF890C;
        if (spr <= SPR_YELLOW) return 0xFFD632;
        return Colors.WHITE;
    }

    private int getCurrentPing(MinecraftClient client) {
        if (client.getNetworkHandler() == null || client.player == null) return 0;
        var entry = client.getNetworkHandler().getPlayerListEntry(client.player.getUuid());
        return entry != null ? entry.getLatency() : 0;
    }
}