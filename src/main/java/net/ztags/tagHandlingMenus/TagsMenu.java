package net.ztags.tagHandlingMenus;

import net.ztags.ZTags;
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

public class TagsMenu implements Listener {

    public static void openMenu(Player player) {
        Inventory menu = Bukkit.createInventory(player, 54, "§6§lTags");

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
                tagMeta.setLore(tagLore);
                tag.setItemMeta(tagMeta);

                menu.addItem(tag);
            }
        }

        player.openInventory(menu);
    }

    @EventHandler
    public void menuClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory != null && clickedInventory.equals(player.getOpenInventory().getTopInventory())) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                if (clickedItem.getType() == Material.NAME_TAG) {
                    player.sendMessage("You clicked on the Diamond Sword!");
                }
            }
        }
    }

}