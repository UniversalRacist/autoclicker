package io.github.itzispyder.autoclicker.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class AutoClickerConfig {

    private static final Gson GSON =
            new GsonBuilder().setPrettyPrinting().create();

    private static final File FILE =
            FabricLoader.getInstance()
                    .getConfigDir()
                    .resolve("autoclicker.json")
                    .toFile();

    public static AutoClickerConfig INSTANCE = new AutoClickerConfig();

    public boolean enabled = false;
    public int cps = 10;
    public String mode = "COMBAT";
    public boolean floatingButton = false;

    public static void load() {
        try {
            if (FILE.exists()) {
                INSTANCE = GSON.fromJson(
                        new FileReader(FILE),
                        AutoClickerConfig.class
                );
            } else {
                save();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            FileWriter writer = new FileWriter(FILE);
            GSON.toJson(INSTANCE, writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}