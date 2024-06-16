package net.ztags;

import net.ztags.tagHandlingMenus.ListTagModMenu;
import net.ztags.tagHandlingMenus.ModTagMenu;
import net.ztags.tagHandlingMenus.TagsMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CommandHandler implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command!");
            return false;
        }

        Player player = (Player) sender;

        if (player.hasPermission("ztags.tags")) {
            if (cmd.getName().equalsIgnoreCase("tags") || cmd.getName().equalsIgnoreCase("tag")) {
                TagsMenu.openMenu(player);
                return true;
            }
        }

        if (cmd.getName().equalsIgnoreCase("ztags") && player.hasPermission("ztags.admin")) {
            if (args.length == 0) {
                player.sendMessage("§cUsage: /ztags tag <list|create|modify|remove> <name>");
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                player.sendMessage("§areloading ZTags");
                player.sendMessage("§asaving config & tags");
                ZTags.getPlugin(ZTags.class).saveYmlConfig();
                ZTags.getPlugin(ZTags.class).saveYmlTags();
                ZTags.getPlugin(ZTags.class).saveYmlPlayerData();
                player.sendMessage("§aloading config & tags");
                ZTags.getPlugin(ZTags.class).loadYmlConfig();
                ZTags.getPlugin(ZTags.class).loadYmlTags();
                ZTags.getPlugin(ZTags.class).loadYmlPlayerData();
                player.sendMessage("§aZTags reloaded successfuly");
                return true;
            }

            if (args[0].equalsIgnoreCase("tag")) {
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /ztags tag <list|create|modify|remove> <name>");
                    return true;
                }

                String subCommand = args[1].toLowerCase();
                if (subCommand.equals("list")) {
                    handleListTags(player);
                } else if (subCommand.equals("create")) {
                    handleCreateTag(player, args);
                } else if (subCommand.equals("modify")) {
                    handleModifyTag(player, args);
                } else if (subCommand.equals("remove")) {
                    handleRemoveTag(player, args);
                } else {
                    player.sendMessage("§cUsage: /ztags tag <list|create|modify|remove> <name>");
                }
                return true;
            }
        }

        return false;
    }

    private void handleListTags(Player player) {
        ListTagModMenu.openMenu(player);
    }

    private void handleCreateTag(Player player, String[] args) {
        if (args.length < 3 || args[2].isEmpty()) {
            player.sendMessage("§cPlease enter the tag name");
            return;
        }

        String tagID = args[2];
        player.sendMessage("Creating tag with name: " + tagID);

        ConfigurationSection tagsSection = ZTags.tagsConfig.getConfigurationSection("tags");
        if (tagsSection != null) {
            if (tagsSection.contains(tagID)) {
                player.sendMessage("§cThis tag already exists");
            } else {
                ZTags.tagsConfig.set("tags." + tagID + ".name", tagID);
                ZTags.tagsConfig.set("tags." + tagID + ".prefix", "");
                ZTags.tagsConfig.set("tags." + tagID + ".suffix", "");
                ZTags.tagsConfig.set("tags." + tagID + ".weight", 0);
                ZTags.getPlugin(ZTags.class).saveYmlTags();
                player.sendMessage("§aTag created successfully");
                ModTagMenu.openMenu(player, tagID);
            }
        } else {
            player.sendMessage("§cError: Tags configuration section is missing");
        }
    }

    private void handleModifyTag(Player player, String[] args) {
        if (args.length < 3 || args[2].isEmpty()) {
            player.sendMessage("§cPlease enter the tag name");
            return;
        }

        String tagName = args[2];
        ConfigurationSection tagsSection = ZTags.tagsConfig.getConfigurationSection("tags");

        if (tagsSection != null && tagsSection.contains(tagName)) {
            ModTagMenu.openMenu(player, tagName);
        } else {
            player.sendMessage("§cThis tag doesn't exist");
        }
    }

    private void handleRemoveTag(Player player, String[] args) {
        if (args.length < 3 || args[2].isEmpty()) {
            player.sendMessage("§cPlease enter the tag name");
            return;
        }

        String tagName = args[2];
        ConfigurationSection tagsSection = ZTags.tagsConfig.getConfigurationSection("tags");

        if (tagsSection != null && tagsSection.contains(tagName)) {
            ZTags.tagsConfig.set("tags." + tagName, null);
            ZTags.getPlugin(ZTags.class).saveYmlTags();
            player.sendMessage("§aTag removed successfully");
        } else {
            player.sendMessage("§cThis tag doesn't exist");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return Collections.emptyList();
        }

        List<String> completions = new ArrayList<>();

        if (cmd.getName().equalsIgnoreCase("ztags")) {
            if (args.length == 1) {
                completions.add("tag");
            } else if (args.length == 2 && args[0].equalsIgnoreCase("tag")) {
                completions.add("list");
                completions.add("create");
                completions.add("modify");
                completions.add("remove");
            } else if (args.length == 3 && args[0].equalsIgnoreCase("tag")
                    && !args[1].equalsIgnoreCase("list")
                    && !args[1].equalsIgnoreCase("create")) {
                Set<String> tagKeys = ZTags.tagsConfig.getConfigurationSection("tags").getKeys(false);

                for (String tagKey : tagKeys) {
                    completions.add(tagKey);
                }
            }
        }

        return completions;
    }

}