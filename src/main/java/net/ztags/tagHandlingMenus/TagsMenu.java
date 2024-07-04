package net.ztags.tagHandlingMenus;

import net.ztags.Tag;
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

    public static void openMenu(Player player) {
        Inventory menu = Bukkit.createInventory(new TagsMenuHolder(), 54, "§6§lTags");

        List<Tag> tags = new ArrayList<>();

        if (ZTags.database == null) {
            Set<String> tagKeys = ZTags.tagsConfig.getConfigurationSection("tags").getKeys(false);
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
        } else {
            tags = ZTags.database.getAllTags();
        }

        tags.sort(Comparator.comparingInt(Tag::getWeight));

        for (Tag tag : tags) {
            if (player.hasPermission("ztags.tag."+tag.getID())) {
                ItemStack tagItem = new ItemStack(Material.NAME_TAG, 1);
                ItemMeta tagMeta = tagItem.getItemMeta();
                tagMeta.setDisplayName(ZTags.translateColorCodes(tag.getName()));
                List<String> tagLore = new ArrayList<>();
                if (!Objects.equals(tag.getPrefix(), "")) {
                    tagLore.add("§7prefix: " + ZTags.translateColorCodes(tag.getPrefix()));
                }
                if (!Objects.equals(tag.getSuffix(), "")) {
                    tagLore.add("§7suffix: " + ZTags.translateColorCodes(tag.getSuffix()));
                }
                tagMeta.setLore(tagLore);
                tagItem.setItemMeta(tagMeta);

                menu.addItem(tagItem);
            }
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
                List<Tag> tags = new ArrayList<>();

                if (ZTags.database == null) {
                    Set<String> tagKeys = ZTags.tagsConfig.getConfigurationSection("tags").getKeys(false);
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
                } else {
                    tags = ZTags.database.getAllTags();
                }

                tags.sort(Comparator.comparingInt(Tag::getWeight));

                for (Tag tag : tags) {
                    ItemStack tagItem = new ItemStack(Material.NAME_TAG, 1);
                    ItemMeta tagMeta = tagItem.getItemMeta();
                    tagMeta.setDisplayName(ZTags.translateColorCodes(tag.getName()));
                    List<String> tagLore = new ArrayList<>();
                    if (!Objects.equals(tag.getPrefix(), "")) {
                        tagLore.add("§7prefix: " + ZTags.translateColorCodes(tag.getPrefix()));
                    }
                    if (!Objects.equals(tag.getSuffix(), "")) {
                        tagLore.add("§7suffix: " + ZTags.translateColorCodes(tag.getSuffix()));
                    }
                    tagMeta.setLore(tagLore);
                    tagItem.setItemMeta(tagMeta);

                    if (clickedItem.equals(tagItem)) {
                        if (player.hasPermission("ztags.tag."+tag.getID())) {
                            player.closeInventory();
                            if (ZTags.database == null) {
                                ZTags.playerDataConfig.set(player.getUniqueId().toString(), tag.getID());
                            } else {
                                ZTags.database.addOrUpdateUserData(player.getUniqueId().toString(), tag.getID());
                            }
                        }
                    }
                }
            }
        }
    }

}