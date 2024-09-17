package de.tnttastisch.yuija;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class YuijaSystem {

    private File configFile;
    private ConfigurationData configurationData;
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
        File config = new File(path, name);
        if (!config.exists()) {
            config.getParentFile().mkdirs();
            config.createNewFile();
        }

        this.configFile = config;
        load();
        save();
        this.apiHandler = new ApiHandler(configurationData.API_URL, configurationData.AUTH_TOKEN);
    }

    private void save() {
        try (FileWriter writer = new FileWriter(configFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(configurationData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load() {
        try (FileReader reader = new FileReader(configFile)) {
            Gson gson = new Gson();
            configurationData = gson.fromJson(reader, ConfigurationData.class);
        } catch (FileNotFoundException e) {
            configurationData = new ConfigurationData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ApiHandler getApiHandler() {
        return apiHandler;
    }

    public static class ConfigurationData {
        public String API_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent";
        public String AUTH_TOKEN = "AUTHENICATION_TOKEN";

    }
}
