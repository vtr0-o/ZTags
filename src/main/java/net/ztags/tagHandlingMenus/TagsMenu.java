package net.ztags.tagHandlingMenus;

import net.ztags.ZTags;
import net.ztags.tagHandlingMenus.holders.TagsMenuHolder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class TagsMenu implements Listener {

    private static class Tag {
        String key;
        String name;
        String prefix;
        String suffix;
        int weight;

        Tag(String key, String name, String prefix, String suffix, int weight) {
            this.key = key;
            this.name = name;
            this.prefix = prefix;
            this.suffix = suffix;
            this.weight = weight;
        }
    }

    public static void openMenu(Player player) {
        Inventory menu = Bukkit.createInventory(new TagsMenuHolder(), 54, "§6§lTags");

        Set<String> tagKeys = ZTags.tagsConfig.getConfigurationSection("tags").getKeys(false);
        List<Tag> tags = new ArrayList<>();

        for (String tagKey : tagKeys) {
            ConfigurationSection tagSection = ZTags.tagsConfig.getConfigurationSection("tags." + tagKey);
            if (tagSection != null && player.hasPermission("ztags.tag." + tagKey)) {
                String name = tagSection.getString("name");
                String prefix = tagSection.getString("prefix");
                String suffix = tagSection.getString("suffix");
                int weight = tagSection.getInt("weight");
                tags.add(new Tag(tagKey, name, prefix, suffix, weight));
            }
        }

        tags.sort(Comparator.comparingInt(tag -> tag.weight));

        for (Tag tag : tags) {
            ItemStack tagItem = new ItemStack(Material.NAME_TAG, 1);
            ItemMeta tagMeta = tagItem.getItemMeta();
            tagMeta.setDisplayName(ZTags.translateColorCodes(tag.name));
            List<String> tagLore = new ArrayList<>();
            if (!Objects.equals(tag.prefix, "")) {
                tagLore.add("§7prefix: " + ZTags.translateColorCodes(tag.prefix));
            }
            if (!Objects.equals(tag.suffix, "")) {
                tagLore.add("§7suffix: " + ZTags.translateColorCodes(tag.suffix));
            }
            tagMeta.setLore(tagLore);
            tagItem.setItemMeta(tagMeta);

            menu.addItem(tagItem);
        }

        player.openInventory(menu);
    }

    @EventHandler
    public void menuClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().getHolder() instanceof TagsMenuHolder) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                Set<String> tagKeys = ZTags.tagsConfig.getConfigurationSection("tags").getKeys(false);

                for (String tagKey : tagKeys) {
                    ConfigurationSection tagSection = ZTags.tagsConfig.getConfigurationSection("tags." + tagKey);
                    if (tagSection != null && player.hasPermission("ztags.tag." + tagKey)) {
                        String name = tagSection.getString("name");
                        String prefix = tagSection.getString("prefix");
                        String suffix = tagSection.getString("suffix");
                        ItemStack tag = new ItemStack(Material.NAME_TAG, 1);
                        ItemMeta tagMeta = tag.getItemMeta();
                        tagMeta.setDisplayName(ZTags.translateColorCodes(name));
                        List<String> tagLore = new ArrayList<>();
                        if (!Objects.equals(prefix, "")) {
                            tagLore.add("§7prefix: " + ZTags.translateColorCodes(prefix));
                        }
                        if (!Objects.equals(suffix, "")) {
                            tagLore.add("§7suffix: " + ZTags.translateColorCodes(suffix));
                        }
                        tagMeta.setLore(tagLore);
                        tag.setItemMeta(tagMeta);

                        if (clickedItem.equals(tag)) {
                            player.closeInventory();
                            ZTags.playerDataConfig.set(player.getUniqueId().toString(), tagKey);
                        }
                    }
                }
            }
        }
    }

}