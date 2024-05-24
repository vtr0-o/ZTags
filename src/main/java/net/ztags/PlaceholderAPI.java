package net.ztags;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
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
            return "A";
        }

        if (identifier.equals("suffix")) {
            return "B";
        }

        return null;
    }

}