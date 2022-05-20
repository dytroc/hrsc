package io.github.dytroInc.hrsc;

import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.dytroInc.hrsc.enums.ChatFilter;
import io.github.dytroInc.hrsc.gui.ChatFilterGUI;
import io.github.dytroInc.hrsc.utils.ConfigUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HRSCMod implements ClientModInitializer {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final List<String> ROIN_ADDRESS = Arrays.asList("roin.tv", "roin.kr", "158.247.218.69");
    private static final Logger LOGGER = LoggerFactory.getLogger("hrcs");

    private static String modVersion;
    private static ChatFilter chatFilter = ChatFilter.BLOCK_NOTHING;
    private static ConfigUtils config;

    KeyBinding toggleChatKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.hrsc.chat",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "category.hrsc.keys"
    ));

    KeyBinding menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.hrsc.menu",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_M,
            "category.hrsc.keys"
    ));

    KeyBinding shopKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.hrsc.shop",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_BRACKET,
            "category.hrsc.keys"
    ));

    private final boolean isChatHidden = true;

    public HRSCMod() {
        instance = this;
    }

    private static HRSCMod instance;

    public static HRSCMod getInstance() {
        return instance;
    }

    public static MinecraftClient getClient() {
        return CLIENT;
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static String getModVersion() {
        return modVersion;
    }

    public static void setChatFilter(ChatFilter chatFilter) {
        HRSCMod.chatFilter = chatFilter;
    }

    public static ChatFilter getChatFilter() {
        return chatFilter;
    }

    @Override
    public void onInitializeClient() {
        config = new ConfigUtils();
        modVersion = FabricLoader.getInstance().getModContainer("hrsc").get().getMetadata().getVersion().getFriendlyString();

        config.load();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleChatKey.wasPressed()) {
                if (isRoinSurvival()) {
                    client.setScreen(new CottonClientScreen(new ChatFilterGUI()));
                }
            }

            while (menuKey.wasPressed()) {
                if (isRoinSurvival()) {
                    assert client.player != null;
                    client.player.sendChatMessage("/메뉴");
                }
            }

            while (shopKey.wasPressed()) {
                if (isRoinSurvival()) {
                    assert client.player != null;
                    client.player.sendChatMessage("/ds");
                }
            }
        });
    }

    public static void onStop() {
        config.save();
    }

    public boolean handleChat(Text text) {
        if (isChatHidden && isRoinSurvival()) {
            if (chatFilter == ChatFilter.BLOCK_NOTHING) return false;
            if (chatFilter == ChatFilter.BLOCK_NON_ADMINS) {
                for (int i = 0; i < text.getSiblings().size(); i++) {
                    for (Text sibling : text.getSiblings().get(i).getSiblings()) {
                        if (isPlaytimeComponent(sibling.asString())) return true;
                    }
                }
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean isRoinSurvival() {
        if (CLIENT.getCurrentServerEntry() == null) return false;
        if (CLIENT.world == null) return false;

        List<ScoreboardObjective> objectives = new ArrayList<>(CLIENT.world.getScoreboard().getObjectives());
        if (objectives.size() != 1) return false;
        StringBuilder builder = new StringBuilder();
        for (Text sibling : objectives.get(0).getDisplayName().getSiblings()) {
            builder.append(sibling.asString());
        }
        if ("".equals(builder.toString())) builder.append(objectives.get(0).getDisplayName().asString());
        return ROIN_ADDRESS.contains(CLIENT.getCurrentServerEntry().address.split(":")[0]) && "ROIN SURVIVAL".equals(builder.toString());
    }

    private static boolean isPlaytimeComponent(String text) {
        return text.endsWith("h") && text.contains(".");
    }
}
