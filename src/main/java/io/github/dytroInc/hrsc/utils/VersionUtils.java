package io.github.dytroInc.hrsc.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.dytroInc.hrsc.HRSCMod;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class VersionUtils {
    private static String latestVersion = "";

    public static boolean isLatest() {
        try {
            StringBuilder content = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new URL("https://api.github.com/repos/dytroInc/hrsc/releases/latest").openStream(), StandardCharsets.UTF_8)
            );

            String line;

            while ((line = reader.readLine()) != null) {
                content.append("\n").append(line);
            }

            latestVersion = new Gson().fromJson(content.toString(), JsonObject.class).get("tag_name").getAsString();
            return Objects.equals(latestVersion, HRSCMod.getModVersion());
        } catch (IOException e) {
            HRSCMod.getLogger().error(e.getMessage(), e);
        }
        return true;
    }

    public static void showUpdateMessage(ClientPlayerEntity player) {
        player.sendMessage(new LiteralText(
                "이 모드는 최신 버전이 아닙니다. 현재 당신이 사용하는 모드의 버전은 " + HRSCMod.getModVersion() + "이므로 " + latestVersion + "로 업데이트해주세요."
        ).setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/dytroInc/hrsc/releases")).withColor(Formatting.AQUA)), false);
    }
}
