package net.ztags;

import net.ztags.tagHandlingMenus.ModTagMenu;
import net.ztags.tagHandlingMenus.TagsMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public class ZTags extends JavaPlugin {

    private File configFile;
    private YamlConfiguration config;

    public static File tagsFile;
    public static YamlConfiguration tagsConfig;

    @Override
    public void onEnable() {
        loadYmlConfig();
        loadYmlTags();

        getServer().getPluginManager().registerEvents(new TagsMenu(), this);
        getServer().getPluginManager().registerEvents(new ModTagMenu(), this);
        getCommand("tags").setExecutor(new CommandHandler());
        getCommand("tag").setExecutor(new CommandHandler());
        getCommand("ztags").setExecutor(new CommandHandler());
        getLogger().info("OK");
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPI(this).register();
        } else {
            getLogger().warning("Could not find PlaceholderAPI! Skipping.");
        }
    }

    @Override
    public void onDisable() {
        saveYmlConfig();
        saveYmlTags();
    }

    private void loadYmlConfig() {
        configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    private void loadYmlTags() {
        tagsFile = new File(getDataFolder(), "tags.yml");
        if (!tagsFile.exists()) {
            tagsFile.getParentFile().mkdirs();
            saveResource("tags.yml", false);
        }

        tagsConfig = YamlConfiguration.loadConfiguration(tagsFile);
    }

    private void saveYmlConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            getLogger().warning("Could not save config.yml");
        }
    }

    private void saveYmlTags() {
        try {
            tagsConfig.save(tagsFile);
        } catch (IOException e) {
            getLogger().warning("Could not save tags.yml");
        }
    }

    public static String translateColorCodes(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}