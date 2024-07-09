package net.ztags;

import net.ztags.tagHandlingMenus.ListTagModMenu;
import net.ztags.tagHandlingMenus.ModTagMenu;
import net.ztags.tagHandlingMenus.TagsMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

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
                return true;
            }

            if (args[0].equalsIgnoreCase("tag")) {
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /ztags tag <list|create|modify|remove|import|export> <name>");
                    return true;
                }

                String subCommand = args[1].toLowerCase();
                switch (subCommand) {
                    case "list":
                        handleListTags(player);
                        break;
                    case "create":
                        handleCreateTag(player, args);
                        break;
                    case "modify":
                        handleModifyTag(player, args);
                        break;
                    case "remove":
                        handleRemoveTag(player, args);
                        break;
                    case "import":
                        handleImportTag(player, args);
                        break;
                    case "export":
                        handleExportTag(player, args);
                        break;
                    default:
                        player.sendMessage("§cUsage: /ztags tag <list|create|modify|remove|import|export> <name>");
                        break;
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

        if (ZTags.database == null) {
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
            }
        } else {
            List<Tag> tags = ZTags.database.getAllTags();

            boolean tagExists = tags.stream().anyMatch(tag -> tag.getID().equals(tagID));
            if (tagExists) {
                player.sendMessage("§cThis tag already exists");
            } else {
                Tag tag = new Tag(tagID, tagID, "", "", 0);
                ZTags.database.addOrUpdateTag(tag);
                ModTagMenu.openMenu(player, tagID);
            }
        }
    }

    private void handleModifyTag(Player player, String[] args) {
        if (args.length < 3 || args[2].isEmpty()) {
            player.sendMessage("§cPlease enter the tag name");
            return;
        }

        String tagName = args[2];

        if (ZTags.database == null) {
            ConfigurationSection tagsSection = ZTags.tagsConfig.getConfigurationSection("tags");
            if (tagsSection != null && tagsSection.contains(tagName)) {
                ModTagMenu.openMenu(player, tagName);
            } else {
                player.sendMessage("§cThis tag doesn't exist");
            }
        } else {
            List<Tag> tags = ZTags.database.getAllTags();
            boolean tagExists = false;
            for (Tag tag : tags) {
                if (tag.getID().equals(tagName)) {
                    tagExists = true;
                    break;
                }
            }
            if (tagExists) {
                ModTagMenu.openMenu(player, tagName);
            } else {
                player.sendMessage("§cThis tag doesn't exist");
            }
        }
    }

    private void handleRemoveTag(Player player, String[] args) {
        if (args.length < 3 || args[2].isEmpty()) {
            player.sendMessage("§cPlease enter the tag name");
            return;
        }

        String tagName = args[2];
        if (ZTags.database == null) {
            ConfigurationSection tagsSection = ZTags.tagsConfig.getConfigurationSection("tags");
            if (tagsSection != null && tagsSection.contains(tagName)) {
                ZTags.tagsConfig.set("tags." + tagName, null);
                ZTags.getPlugin(ZTags.class).saveYmlTags();
                player.sendMessage("§aTag removed successfully");
            } else {
                player.sendMessage("§cThis tag doesn't exist");
            }
        } else {
            List<Tag> tags = ZTags.database.getAllTags();
            boolean tagExists = false;
            for (Tag tag : tags) {
                if (tag.getID().equals(tagName)) {
                    tagExists = true;
                    break;
                }
            }

            if (tagExists) {
                ZTags.database.deleteTag(tagName);
                player.sendMessage("§aTag removed successfully");
            } else {
                player.sendMessage("§cThis tag doesn't exist");
            }
        }
    }

    private void handleImportTag(Player player, String[] args) {
        if (ZTags.database != null) {
            ZTags.getPlugin(ZTags.class).loadYmlTags();


            for (Tag tag : ZTags.database.getAllTags()) {
                ZTags.tagsConfig.set("tags."+tag.getID()+".name", tag.getName());
                ZTags.tagsConfig.set("tags."+tag.getID()+".prefix", tag.getPrefix());
                ZTags.tagsConfig.set("tags."+tag.getID()+".suffix", tag.getSuffix());
                ZTags.tagsConfig.set("tags."+tag.getID()+".weight", tag.getWeight());
                ZTags.tagsConfig.set("tags."+tag.getID()+".weight", tag.getWeight());
            }
            ZTags.getPlugin(ZTags.class).saveYmlTags();
        } else {
            player.sendMessage("§c§lNot connected to a Database!");
        }
    }

    private void handleExportTag(Player player, String[] args) {
        if (ZTags.database != null) {
            ZTags.getPlugin(ZTags.class).loadYmlTags();
            Set<String> tagKeys = ZTags.tagsConfig.getConfigurationSection("tags").getKeys(false);
            for (String tagKey : tagKeys) {
                ConfigurationSection tagSection = ZTags.tagsConfig.getConfigurationSection("tags." + tagKey);
                if (tagSection != null && player.hasPermission("ztags.tag." + tagKey)) {
                    String name = tagSection.getString("name");
                    String prefix = tagSection.getString("prefix");
                    String suffix = tagSection.getString("suffix");
                    int weight = tagSection.getInt("weight");
                    ZTags.database.addOrUpdateTag(new Tag(tagKey, name, prefix, suffix, weight));
                }
            }
        } else {
            player.sendMessage("§c§lNot connected to a Database!");
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
                completions.add("import");
                completions.add("export");
            } else if (args.length == 3 && args[0].equalsIgnoreCase("tag")
                    && !args[1].equalsIgnoreCase("list")
                    && !args[1].equalsIgnoreCase("import")
                    && !args[1].equalsIgnoreCase("export")
                    && !args[1].equalsIgnoreCase("create")) {

                Set<String> tagKeys;
                if (ZTags.database == null) {
                    ConfigurationSection tagsSection = ZTags.tagsConfig.getConfigurationSection("tags");
                    if (tagsSection != null) {
                        tagKeys = tagsSection.getKeys(false);
                    } else {
                        tagKeys = new HashSet<>();
                    }
                } else {
                    List<Tag> tags = ZTags.database.getAllTags();
                    tagKeys = new HashSet<>();
                    for (Tag tag : tags) {
                        tagKeys.add(tag.getID());
                    }
                }

                completions.addAll(tagKeys);
            }
        }

        return completions;
    }

}