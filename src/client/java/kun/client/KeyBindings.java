// src/client/java/kun/client/KeyBindings.java
package kun.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    private static KeyBinding copyKey;

    public static void register() {
        copyKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pingdisplay.copy",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_C,          // 你可以改成其他键，例如 GLFW.GLFW_KEY_K
                "category.pingdisplay"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.currentScreen != null) return;
            if (copyKey.wasPressed()) {
                if (!PacketTracker.isConnected()) {
                    System.out.println("[PingDisplay] Not connected, copy ignored");
                    return;
                }

                // 检测修饰键组合
                long handle = client.getWindow().getHandle();
                boolean leftCtrlAlt = InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_CONTROL) &&
                        InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_ALT);
                boolean rightCtrlAlt = InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_RIGHT_CONTROL) &&
                        InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_RIGHT_ALT);

                String text = null;
                if (leftCtrlAlt) {
                    text = CopyFormatter.formatSimple(client);
                    System.out.println("[PingDisplay] Simple copy: " + text);
                } else if (rightCtrlAlt) {
                    text = CopyFormatter.formatDetailed(client);
                    System.out.println("[PingDisplay] Detailed copy: " + text);
                } else {
                    // 可选：无修饰键时也复制简单版
                    text = CopyFormatter.formatSimple(client);
                    System.out.println("[PingDisplay] No modifiers, simple copy: " + text);
                }

                if (text != null && !text.isEmpty()) {
                    setClipboard(text);
                    // 可选：在动作栏显示提示
                    client.player.sendMessage(Text.literal("§a[PingDisplay] 已复制网络信息"), true);
                }
            }
        });
    }

    // 双重保障的剪贴板设置方法
    private static void setClipboard(String text) {
        // 首先尝试 Minecraft 自带剪贴板
        try {
            MinecraftClient.getInstance().keyboard.setClipboard(text);
            System.out.println("[PingDisplay] Copied with Minecraft clipboard");
            return;
        } catch (Exception e) {
            System.out.println("[PingDisplay] Minecraft clipboard failed: " + e);
        }

        // 备用：使用 AWT 剪贴板
        try {
            java.awt.Toolkit.getDefaultToolkit().getSystemClipboard()
                    .setContents(new java.awt.datatransfer.StringSelection(text), null);
            System.out.println("[PingDisplay] Copied with AWT clipboard");
        } catch (Exception e) {
            System.err.println("[PingDisplay] Both clipboard methods failed!");
            e.printStackTrace();
        }
    }
}