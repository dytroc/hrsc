package io.github.dytroInc.hrsc.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import io.github.dytroInc.hrsc.HRSCMod;
import io.github.dytroInc.hrsc.enums.ChatFilter;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;

public class ConfigUtils {

    private Path configPath;

    public void load() {
        this.configPath = FabricLoader.getInstance().getConfigDir().resolve("hrsc.json");
        File file = configPath.toFile();
        JsonObject jsonObject = null;

        if (!file.exists()) try {
            file.createNewFile();
            jsonObject = new JsonObject();
        } catch (IOException exception) {
            HRSCMod.getLogger().error(exception.getMessage(), exception);
        }

        if (jsonObject == null) {
            try {
                jsonObject = new Gson().fromJson(new JsonReader(new FileReader(file)), JsonObject.class);
            } catch (FileNotFoundException | RuntimeException exception) {
                HRSCMod.getLogger().error(exception.getMessage(), exception);
            }
        }

        if (jsonObject != null) {
            JsonElement filter = jsonObject.get("chat_filter");
            HRSCMod.setChatFilter(ChatFilter.fromString(filter == null ? null : filter.getAsString()));
        }
    }

    public void save() {
        JsonObject object = new JsonObject();

        object.add("chat_filter", JsonParser.parseString(HRSCMod.getChatFilter().asString()));

        try {
            FileWriter writer = new FileWriter(this.configPath.toFile());
            writer.write(new Gson().toJson(object));
            writer.flush();
        } catch (IOException exception) {
            HRSCMod.getLogger().error(exception.getMessage(), exception);
        }
    }
}
