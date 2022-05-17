package io.github.dytroInc.hrsc;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class HRSCMod implements ClientModInitializer {

    KeyBinding toggleChatKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.hrsc.togglechat",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "category.hrsc.chat"
    ));

    private boolean isChatHidden = true;

    private boolean isRoinServer = false;

    public void setRoinServer(boolean roinServer) {
        isRoinServer = roinServer;
    }

    public HRSCMod() {
        instance = this;
    }

    private static HRSCMod instance;

    public static HRSCMod getInstance() {
        return instance;
    }

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleChatKey.wasPressed()) {
                isChatHidden = !isChatHidden;
                assert client.player != null;
                client.player.sendMessage(new LiteralText("로인 서바이벌 전체채팅을 " + (isChatHidden ? "비활성화" : "활성화") + "했습니다."), false);
            }
        });
    }

    public boolean handleChat(Text text) {
        if (isChatHidden && isRoinServer) {
            if (text.getSiblings().size() < 1) return false;
            for (int i = 0; i <= 1; i++) {
                for (Text sibling : text.getSiblings().get(i).getSiblings()) {
                    if (isPlaytimeComponent(sibling.asString())) return true;
                }
            }
        }
        return false;
    }

    private static boolean isPlaytimeComponent(String text) {
        return text.endsWith("h") && text.contains(".");
    }
}
