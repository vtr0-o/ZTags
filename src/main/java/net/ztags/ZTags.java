package net.ztags;

import net.ztags.tagHandlingMenus.ListTagModMenu;
import net.ztags.tagHandlingMenus.ModTagMenu;
import net.ztags.tagHandlingMenus.TagsMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.Set;

public class ZTags extends JavaPlugin {

    private File configFile;
    private YamlConfiguration config;

    public static File tagsFile;
    public static YamlConfiguration tagsConfig;

    public static File playerDataFile;
    public static YamlConfiguration playerDataConfig;

    @Override
    public void onEnable() {
        loadYmlConfig();
        loadYmlTags();
        loadYmlPlayerData();

        getServer().getPluginManager().registerEvents(new TagsMenu(), this);
        getServer().getPluginManager().registerEvents(new ModTagMenu(), this);
        getServer().getPluginManager().registerEvents(new ListTagModMenu(), this);
        getCommand("tags").setExecutor(new CommandHandler());
        getCommand("tag").setExecutor(new CommandHandler());
        getCommand("ztags").setExecutor(new CommandHandler());
        getLogger().info("OK");
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPI(this).register();
        } else {
            getLogger().warning("§c§lCould not find PlaceholderAPI! Unloading ZTags.");
            Bukkit.getPluginManager().disablePlugin(ZTags.getPlugin(ZTags.class));
        }
    }

    @Override
    public void onDisable() {
        saveYmlConfig();
        saveYmlTags();
        saveYmlPlayerData();
    }

    public void loadYmlConfig() {
        configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void loadYmlTags() {
        tagsFile = new File(getDataFolder(), "tags.yml");
        if (!tagsFile.exists()) {
            tagsFile.getParentFile().mkdirs();
            saveResource("tags.yml", false);
        }

        tagsConfig = YamlConfiguration.loadConfiguration(tagsFile);
    }

    public void loadYmlPlayerData() {
        playerDataFile = new File(getDataFolder(), "player-data.yml");
        if (!playerDataFile.exists()) {
            playerDataFile.getParentFile().mkdirs();
            saveResource("player-data.yml", false);
        }

        playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
    }

    public void saveYmlConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            getLogger().warning("Could not save config.yml");
        }
    }

    public void saveYmlTags() {
        try {
            tagsConfig.save(tagsFile);
        } catch (IOException e) {
            getLogger().warning("Could not save tags.yml");
        }
    }

    public void saveYmlPlayerData() {
        try {
            playerDataConfig.save(playerDataFile);
        } catch (IOException e) {
            getLogger().warning("Could not save player-data.yml");
        }
    }

    public static String translateColorCodes(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String getTagWithMinimumWeight() {
        String minWeightTag = null;
        int minWeight = Integer.MAX_VALUE;
        if (tagsConfig.isConfigurationSection("tags")) {
            Set<String> tags = tagsConfig.getConfigurationSection("tags").getKeys(false);
            for (String tag : tags) {
                int weight = tagsConfig.getInt("tags." + tag + ".weight", Integer.MAX_VALUE);
                if (weight < minWeight) {
                    minWeight = weight;
                    minWeightTag = tag;
                }
            }
        }
        return minWeightTag;
    }


}