package net.ztags;

import net.ztags.tagHandlingMenus.ListTagModMenu;
import net.ztags.tagHandlingMenus.ModTagMenu;
import net.ztags.tagHandlingMenus.TagsMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.sql.SQLException;
import java.util.Set;

public class ZTags extends JavaPlugin {

    private static JavaPlugin instance;

    public static JavaPlugin getInstance() {
        return instance;
    }
    
    private File configFile;
    private YamlConfiguration config;

    public static File tagsFile;
    public static YamlConfiguration tagsConfig;

    public static File playerDataFile;
    public static YamlConfiguration playerDataConfig;

    public static MySQLDatabase database;

    @Override
    public void onEnable() {
        instance = this;
        loadYmlConfig();

        String databaseUrl = config.getString("mysql.url");
        int databasePort = config.getInt("mysql.port");
        String databaseName = config.getString("mysql.name");
        String databaseUser = config.getString("mysql.user");
        String databasePass = config.getString("mysql.pass");
        if (!databaseUrl.equals("localhost")) {
            database = new MySQLDatabase(databaseUrl, databasePort, databaseName, databaseUser, databasePass);
            try {
                database.connect();
                getLogger().info("Connected to MySQL/MariaDB!");
                if (database.getAllTags().isEmpty()) {
                    getLogger().warning("No tag in database, creating!");
                    database.addOrUpdateTag(new Tag("default", "&7Member", "&7[Member]", "", 1));
                }
            } catch (Exception e) {
                getLogger().severe("Failed to connect to MySQL/MariaDB: " + e.getMessage());
            }
        } else {
            loadYmlTags();
            loadYmlPlayerData();
        }

        getServer().getPluginManager().registerEvents(new TagsMenu(), this);
        getServer().getPluginManager().registerEvents(new ModTagMenu(), this);
        getServer().getPluginManager().registerEvents(new ListTagModMenu(), this);
        getCommand("tags").setExecutor(new CommandHandler());
        getCommand("tag").setExecutor(new CommandHandler());
        getCommand("ztags").setExecutor(new CommandHandler());
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPI(this).register();
        } else {
            getLogger().warning("§c§lCould not find PlaceholderAPI! Unloading ZTags.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if (database != null) {
            try {
                database.disconnect();
                getLogger().info("Disconnected from MySQL/MariaDB!");
            } catch (SQLException e) {
                getLogger().severe("Failed to disconnect from MySQL/MariaDB: " + e.getMessage());
                saveYmlTags();
                saveYmlPlayerData();
            }
        } else {
            saveYmlTags();
            saveYmlPlayerData();
        }
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

    public Tag getTagWithMinimumWeight() {
        Tag minWeightTag = null;
        int minWeight = Integer.MAX_VALUE;
        if (database == null) {
            if (tagsConfig.isConfigurationSection("tags")) {
                Set<String> tags = tagsConfig.getConfigurationSection("tags").getKeys(false);
                for (String tagKey : tags) {
                    Tag tag = new Tag(tagsConfig.getString("tags."+tagKey),
                            tagsConfig.getString("tags."+tagKey+".name"),
                            tagsConfig.getString("tags."+tagKey+".prefix"),
                            tagsConfig.getString("tags."+tagKey+".suffix"),
                            tagsConfig.getInt("tags."+tagKey+".weight"));
                    if (tag.getWeight() < minWeight) {
                        minWeight = tag.getWeight();
                        minWeightTag = tag;
                    }
                }
            }
        } else {
            for (Tag tag : database.getAllTags()) {
                if (tag.getWeight() < minWeight) {
                    minWeight = tag.getWeight();
                    minWeightTag = tag;
                }
            }
        }
        return minWeightTag;
    }


}