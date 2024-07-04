package net.ztags.tagHandlingMenus;

import net.ztags.Tag;
import net.ztags.ZTags;
import net.ztags.tagHandlingMenus.holders.ListModTagMenuHolder;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ListTagModMenu implements Listener {

    public static void openMenu(Player player) {
        Inventory menu = Bukkit.createInventory(new ListModTagMenuHolder(),  54, "§aModify Tags");

        if (ZTags.database == null) {
            Set<String> tagKeys = ZTags.tagsConfig.getConfigurationSection("tags").getKeys(false);
            for (String tagKey : tagKeys) {
                ConfigurationSection tagSection = ZTags.tagsConfig.getConfigurationSection("tags." + tagKey);
                if (tagSection != null && player.hasPermission("ztags.tag." + tagKey)) {
                    String name = tagSection.getString("name");
                    String prefix = tagSection.getString("prefix");
                    String suffix = tagSection.getString("suffix");
                    int weight = tagSection.getInt("weight");
                    ItemStack tag = new ItemStack(Material.NAME_TAG, 1);
                    ItemMeta tagMeta = tag.getItemMeta();
                    tagMeta.setDisplayName(ZTags.translateColorCodes(name));
                    List<String> tagLore = new ArrayList<>();
                    tagLore.add("§7prefix: " + ZTags.translateColorCodes(prefix));
                    tagLore.add("§7suffix: " + ZTags.translateColorCodes(suffix));
                    tagLore.add("§7tag id: §f" + tagKey);
                    tagLore.add("§7tag weight: §f" + weight);
                    tagMeta.setLore(tagLore);
                    tag.setItemMeta(tagMeta);

                    menu.addItem(tag);
                }
            }
        } else {
            for (Tag tag : ZTags.database.getAllTags()) {
                ItemStack tagI = new ItemStack(Material.NAME_TAG, 1);
                ItemMeta tagMeta = tagI.getItemMeta();
                tagMeta.setDisplayName(ZTags.translateColorCodes(tag.getName()));
                List<String> tagLore = new ArrayList<>();
                tagLore.add("§7prefix: " + ZTags.translateColorCodes(tag.getPrefix()));
                tagLore.add("§7suffix: " + ZTags.translateColorCodes(tag.getSuffix()));
                tagLore.add("§7tag id: §f" + tag.getID());
                tagLore.add("§7tag weight: §f" + tag.getWeight());
                tagMeta.setLore(tagLore);
                tagI.setItemMeta(tagMeta);

                menu.addItem(tagI);
            }
        }

        player.openInventory(menu);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().getHolder() instanceof ListModTagMenuHolder) {
            ItemStack clickedItem = event.getCurrentItem();
            event.setCancelled(true);
            if (clickedItem != null && clickedItem.getType() != Material.AIR) {

                if (ZTags.database == null) {
                    Set<String> tagKeys = ZTags.tagsConfig.getConfigurationSection("tags").getKeys(false);
                    for (String tagKey : tagKeys) {
                        ConfigurationSection tagSection = ZTags.tagsConfig.getConfigurationSection("tags." + tagKey);
                        if (tagSection != null && player.hasPermission("ztags.admin")) {
                            String name = tagSection.getString("name");
                            String prefix = tagSection.getString("prefix");
                            String suffix = tagSection.getString("suffix");
                            int weight = tagSection.getInt("weight");
                            ItemStack tag = new ItemStack(Material.NAME_TAG, 1);
                            ItemMeta tagMeta = tag.getItemMeta();
                            tagMeta.setDisplayName(ZTags.translateColorCodes(name));
                            List<String> tagLore = new ArrayList<>();
                            tagLore.add("§7prefix: " + ZTags.translateColorCodes(prefix));
                            tagLore.add("§7suffix: " + ZTags.translateColorCodes(suffix));
                            tagLore.add("§7tag id: §f" + tagKey);
                            tagLore.add("§7tag weight: §f" + weight);
                            tagMeta.setLore(tagLore);
                            tag.setItemMeta(tagMeta);

                            if (clickedItem.equals(tag)) {
                                player.closeInventory();
                                ModTagMenu.openMenu(player, tagKey);
                            }
                        }
                    }
                } else {
                    for (Tag tag : ZTags.database.getAllTags()) {
                        if (player.hasPermission("ztags.admin")) {
                            ItemStack tagI = new ItemStack(Material.NAME_TAG, 1);
                            ItemMeta tagMeta = tagI.getItemMeta();
                            tagMeta.setDisplayName(ZTags.translateColorCodes(tag.getName()));
                            List<String> tagLore = new ArrayList<>();
                            tagLore.add("§7prefix: " + ZTags.translateColorCodes(tag.getPrefix()));
                            tagLore.add("§7suffix: " + ZTags.translateColorCodes(tag.getSuffix()));
                            tagLore.add("§7tag id: §f" + tag.getID());
                            tagLore.add("§7tag weight: §f" + tag.getWeight());
                            tagMeta.setLore(tagLore);
                            tagI.setItemMeta(tagMeta);

                            if (clickedItem.equals(tagI)) {
                                player.closeInventory();
                                ModTagMenu.openMenu(player, tag.getID());
                            }
                        }
                    }
                }
            }
        }
    }

}