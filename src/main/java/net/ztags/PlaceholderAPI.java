package net.ztags;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPI extends PlaceholderExpansion {

    private final ZTags plugin;

    public PlaceholderAPI(ZTags plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "ztags";
    }

    @Override
    public @NotNull String getAuthor() {
        return "vtr0_o";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {

        if (identifier.equals("prefix")) {
            String tagID = ZTags.playerDataConfig.getString(player.getUniqueId().toString());
            return ZTags.tagsConfig.getString("tags."+tagID+".prefix");
        }

        if (identifier.equals("suffix")) {
            String tagID = ZTags.playerDataConfig.getString(player.getUniqueId().toString());
            return ZTags.tagsConfig.getString("tags."+tagID+".suffix");
        }

        if (identifier.equals("name")) {
            String tagID = ZTags.playerDataConfig.getString(player.getUniqueId().toString());
            return ZTags.tagsConfig.getString("tags."+tagID+".name");
        }

        if (identifier.equals("weight")) {
            String tagID = ZTags.playerDataConfig.getString(player.getUniqueId().toString());
            return ZTags.tagsConfig.getString("tags."+tagID+".weight");
        }

        return null;
    }

}