package net.ztags;

import net.ztags.tagHandlingMenus.ModTagMenu;
import net.ztags.tagHandlingMenus.TagsMenu;
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

        if (cmd.getName().equalsIgnoreCase("tags") || cmd.getName().equalsIgnoreCase("tag")) {
            TagsMenu.openMenu(player);
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("ztags")) {
            if (args.length == 0) {
                player.sendMessage("§cUsage: /ztags tag <create|modify|remove> <name>");
                return true;
            }

            if (args[0].equalsIgnoreCase("tag")) {
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /ztags tag <create|modify|remove> <name>");
                    return true;
                }

                String subCommand = args[1].toLowerCase();
                if (subCommand.equals("create")) {
                    handleCreateTag(player, args);
                } else if (subCommand.equals("modify")) {
                    handleModifyTag(player, args);
                } else if (subCommand.equals("remove")) {
                    handleRemoveTag(player, args);
                } else {
                    player.sendMessage("§cUsage: /ztags tag <create|modify|remove> <name>");
                }
                return true;
            }
        }

        return false;
    }

    private void handleCreateTag(Player player, String[] args) {
        if (args.length < 3 || args[2].isEmpty()) {
            player.sendMessage("§cPlease enter the tag name");
        } else {
            String tagName = args[2];
            player.sendMessage("Creating tag with name: " + tagName);

            Set<String> tagKeys = ZTags.tagsConfig.getConfigurationSection("tags").getKeys(false);

            for (String tagKey : tagKeys) {
                ConfigurationSection tagSection = ZTags.tagsConfig.getConfigurationSection("tags." + tagKey);
                if (tagSection != null) {
                    if (tagKey.equals(tagName)) {
                        player.sendMessage("§cThis tag already exists");
                    } else {
                        ModTagMenu.openMenu(player, tagName);
                    }
                }
            }
        }
    }

    private void handleModifyTag(Player player, String[] args) {
        if (args.length < 3 || args[2].isEmpty()) {
            player.sendMessage("§cPlease enter the tag name");
        } else {
            String tagName = args[2];
            player.sendMessage("Modifying tag with name: " + tagName);
            // Add logic to modify the tag
        }
    }

    private void handleRemoveTag(Player player, String[] args) {
        if (args.length < 3 || args[2].isEmpty()) {
            player.sendMessage("§cPlease enter the tag name");
        } else {
            String tagName = args[2];
            player.sendMessage("Removing tag with name: " + tagName);
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
                completions.add("create");
                completions.add("modify");
                completions.add("remove");
            } else if (args.length == 3 && args[0].equalsIgnoreCase("tag")) {
                completions.add("<tag_name>");
            }
        }

        return completions;
    }

}