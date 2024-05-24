package net.ztags.tagHandlingMenus;

import net.ztags.ZTags;
import net.ztags.tagHandlingMenus.holders.ModTagMenuHolder;
import net.ztags.tagHandlingMenus.holders.TagsMenuHolder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ModTagMenu implements Listener {

    private Map<UUID, String> tagInputMap = new HashMap<>();

    public static void openMenu (Player player, String tagName) {
        Inventory menu = Bukkit.createInventory(new ModTagMenuHolder(), 54, "§6Modify tag '§r" + tagName + "§6'");

        ItemStack tagNameItemStack = new ItemStack(Material.NAME_TAG, 1);
        ItemMeta tagNameMeta = tagNameItemStack.getItemMeta();
        tagNameMeta.setDisplayName("§aClick here to modify the tag name");
        List<String> tagNameMetaLore = new ArrayList<>();
        tagNameMetaLore.add("§7Current tag name: §r" + ZTags.translateColorCodes(tagName));
        tagNameMeta.setLore(tagNameMetaLore);
        tagNameItemStack.setItemMeta(tagNameMeta);

        menu.setItem(10, tagNameItemStack);

        player.openInventory(menu);
    }

    @EventHandler
    public void clickInv(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack a = event.getCurrentItem();
        if (event.getInventory().getHolder() instanceof ModTagMenuHolder) {
            player.sendMessage(a.toString());
        }
    }

}