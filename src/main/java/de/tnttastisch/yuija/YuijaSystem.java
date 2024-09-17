package de.tnttastisch.yuija;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;

public class YuijaSystem {

    private final Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    private final HashMap<String, String> configurationData = new HashMap<>();
    private File config;
    private ApiHandler apiHandler;

    public YuijaSystem(String path, String name) throws IOException {
        initializeConstructor(path, name);
    }

    public YuijaSystem() throws IOException {
        initializeConstructor("./", "ai.json");
    }

    public YuijaSystem(File config) throws IOException {
        initializeConstructor(config.getParentFile().getAbsolutePath(), config.getName());
    }

    private void initializeConstructor(String path, String name) throws IOException {
        this.config = new File(path, name);
        if (!config.exists()) {
            config.getParentFile().mkdirs();
            config.createNewFile();

            configurationData.put("API_KEY", "SECRETAPIKEY");
            configurationData.put("API_URI", "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent");
            save();
        }
        load();
        this.apiHandler = new ApiHandler(configurationData.get("API_URI"), configurationData.get("API_KEY"));
    }

    public void save() {
        try (FileWriter writer = new FileWriter(config)) {
            gson.toJson(configurationData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load() {
        try (FileReader reader = new FileReader(config)) {
            Type type = new TypeToken<HashMap<String, String>>() {}.getType();
            configurationData.clear();
            HashMap<String, String> loadedData = gson.fromJson(reader, type);
            if (loadedData != null) {
                configurationData.putAll(loadedData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ApiHandler getApiHandler() {
        return apiHandler;
    }
}
