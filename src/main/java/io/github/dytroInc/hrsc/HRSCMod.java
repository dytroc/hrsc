package io.github.dytroInc.hrsc;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.List;

public class HRSCMod implements ClientModInitializer {

    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final List<String> roinAddress = Arrays.asList("roin.tv", "roin.kr", "158.247.218.69");

    KeyBinding toggleChatKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.hrsc.togglechat",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "category.hrsc.chat"
    ));

    private boolean isChatHidden = true;

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
        if (isChatHidden && isRoinServer()) {
            if (text.getSiblings().size() < 1) return false;
            for (int i = 0; i < text.getSiblings().size(); i++) {
                for (Text sibling : text.getSiblings().get(i).getSiblings()) {
                    if (isPlaytimeComponent(sibling.asString())) return true;
                }
            }
        }
        return false;
    }

    private boolean isRoinServer() {
        if (client.getCurrentServerEntry() == null) return false;
        return roinAddress.contains(client.getCurrentServerEntry().address);
    }

    private static boolean isPlaytimeComponent(String text) {
        return text.endsWith("h") && text.contains(".");
    }
}
