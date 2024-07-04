package net.ztags.tagHandlingMenus;

import net.ztags.Tag;
import net.ztags.ZTags;
import net.ztags.tagHandlingMenus.holders.ModTagMenuHolder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ModTagMenu implements Listener {

    private HashMap<UUID, Integer> waitingForInput = new HashMap<>();
    private HashMap<UUID, String[]> playerInputs = new HashMap<>();

    public static void openMenu(Player player, String tagID) {
        Inventory menu = Bukkit.createInventory(new ModTagMenuHolder(tagID), 54, "§6Modify tag '§r" + tagID + "§6'");

        Tag tag;

        if (ZTags.database == null) {
            String tagName = ZTags.tagsConfig.getString("tags." + tagID + ".name");
            String tagPrefix = ZTags.tagsConfig.getString("tags." + tagID + ".prefix");
            String tagSuffix = ZTags.tagsConfig.getString("tags." + tagID + ".suffix");
            int tagWeight = ZTags.tagsConfig.getInt("tags." + tagID + ".weight");
            tag = new Tag(tagID, tagName, tagPrefix, tagSuffix, tagWeight);
        } else {
            tag = ZTags.database.getTag(tagID);
        }

        ItemStack tagNameItemStack = new ItemStack(Material.NAME_TAG, 1);
        ItemMeta tagNameMeta = tagNameItemStack.getItemMeta();
        tagNameMeta.setDisplayName("§aClick here to modify the tag name");
        List<String> tagNameMetaLore = new ArrayList<>();
        tagNameMetaLore.add("§7Current tag name: §r" + ZTags.translateColorCodes(tag.getName()));
        tagNameMeta.setLore(tagNameMetaLore);
        tagNameItemStack.setItemMeta(tagNameMeta);

        ItemStack tagPrefixItemStack = new ItemStack(Material.NAME_TAG, 1);
        ItemMeta tagPrefixMeta = tagPrefixItemStack.getItemMeta();
        tagPrefixMeta.setDisplayName("§aClick here to modify the tag prefix");
        List<String> tagPrefixMetaLore = new ArrayList<>();
        tagPrefixMetaLore.add("§7Current tag prefix: " + ZTags.translateColorCodes(tag.getPrefix()));
        tagPrefixMeta.setLore(tagPrefixMetaLore);
        tagPrefixItemStack.setItemMeta(tagPrefixMeta);

        ItemStack tagSuffixItemStack = new ItemStack(Material.NAME_TAG, 1);
        ItemMeta tagSuffixMeta = tagSuffixItemStack.getItemMeta();
        tagSuffixMeta.setDisplayName("§aClick here to modify the tag suffix");
        List<String> tagSuffixMetaLore = new ArrayList<>();
        tagSuffixMetaLore.add("§7Current tag suffix: " + ZTags.translateColorCodes(tag.getSuffix()));
        tagSuffixMeta.setLore(tagSuffixMetaLore);
        tagSuffixItemStack.setItemMeta(tagSuffixMeta);

        ItemStack tagWeightItemStack = new ItemStack(Material.ANVIL, 1);
        ItemMeta tagWeightMeta = tagWeightItemStack.getItemMeta();
        tagWeightMeta.setDisplayName("§aClick here to modify the tag weight");
        List<String> tagWeightMetaLore = new ArrayList<>();
        tagWeightMetaLore.add("§7Current tag weight: " + tag.getWeight());
        tagWeightMeta.setLore(tagWeightMetaLore);
        tagWeightItemStack.setItemMeta(tagWeightMeta);

        ItemStack saveBtn = new ItemStack(Material.WOOL, 1, (short) 5);
        ItemMeta saveBtnMeta = saveBtn.getItemMeta();
        saveBtnMeta.setDisplayName("§aSave changes");
        saveBtn.setItemMeta(saveBtnMeta);

        ItemStack cancelBtn = new ItemStack(Material.WOOL, 1, (short) 14);
        ItemMeta cancelBtnMeta = cancelBtn.getItemMeta();
        cancelBtnMeta.setDisplayName("§cDiscard changes");
        cancelBtn.setItemMeta(cancelBtnMeta);

        menu.setItem(10, tagNameItemStack);
        menu.setItem(11, tagWeightItemStack);
        menu.setItem(15, tagPrefixItemStack);
        menu.setItem(16, tagSuffixItemStack);

        menu.setItem(45, saveBtn);
        menu.setItem(53, cancelBtn);

        player.openInventory(menu);
    }

    @EventHandler
    public void clickInv(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        if (event.getInventory().getHolder() instanceof ModTagMenuHolder) {
            String tagID = ((ModTagMenuHolder) event.getInventory().getHolder()).getTagID();
            event.setCancelled(true);
            if (slot == 10) {
                openChatInput(player, tagID, 1);
            } else if (slot == 11) {
                openChatInput(player, tagID, 2);
            } else if (slot == 15) {
                openChatInput(player, tagID, 3);
            } else if (slot == 16) {
                openChatInput(player, tagID, 4);
            }

            if (slot == 45) {
                player.closeInventory();
            } else if (slot == 53) {
                player.closeInventory();
            }
        }
    }

    private void openChatInput(Player player, String tagID, int inputNumber) {
        String[] inputData = {tagID, String.valueOf(inputNumber)};
        playerInputs.put(player.getUniqueId(), inputData);
        waitingForInput.put(player.getUniqueId(), inputNumber);
        player.closeInventory();
        player.sendMessage("§aType '§cCancel§a' to cancel the action");
    }


    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (waitingForInput.containsKey(playerUUID)) {
            event.setCancelled(true);
            int inputNumber = waitingForInput.get(playerUUID);
            String message = event.getMessage();

            if (message.equals("Cancel")) {
                waitingForInput.remove(playerUUID);
                return;
            }

            if (inputNumber <= 4) {
                String tagID = playerInputs.get(playerUUID)[0];
                if (ZTags.database == null) {
                    if (inputNumber == 1) {
                        ZTags.tagsConfig.set("tags." + tagID + ".name", message);
                        player.sendMessage("§aSuccessfuly changed the tag name");
                    } else if (inputNumber == 2) {
                        ZTags.tagsConfig.set("tags." + tagID + ".weight", Integer.parseInt(message));
                        player.sendMessage("§aSuccessfuly changed the tag weight");
                    } else if (inputNumber == 3) {
                        ZTags.tagsConfig.set("tags." + tagID + ".prefix", message);
                        player.sendMessage("§aSuccessfuly changed the tag prefix");
                    } else if (inputNumber == 4) {
                        ZTags.tagsConfig.set("tags." + tagID + ".suffix", message);
                        player.sendMessage("§aSuccessfuly changed the tag suffix");
                    }
                } else {
                    Tag tag = ZTags.database.getTag(tagID);
                    if (inputNumber == 1) {
                        tag.setName(message);
                        ZTags.database.addOrUpdateTag(tag);
                    } else if (inputNumber == 2) {
                        tag.setWeight(Integer.parseInt(message));
                        ZTags.database.addOrUpdateTag(tag);
                    } else if (inputNumber == 3) {
                        tag.setPrefix(message);
                        ZTags.database.addOrUpdateTag(tag);
                    } else if (inputNumber == 4) {
                        tag.setSuffix(message);
                        ZTags.database.addOrUpdateTag(tag);
                    }
                }
            }
            waitingForInput.remove(playerUUID);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        String playerUUID = event.getPlayer().getUniqueId().toString();
        if (ZTags.database == null) {
            if (!ZTags.playerDataConfig.contains(playerUUID)) {
                String tagMin = ZTags.getPlugin(ZTags.class).getTagWithMinimumWeight().getID();
                ZTags.playerDataConfig.set(playerUUID, tagMin);
            }
        } else {
            String tagId = ZTags.database.getTagForUser(playerUUID);
            if (tagId == null) {
                String tagMin = ZTags.getPlugin(ZTags.class).getTagWithMinimumWeight().getID();
                ZTags.database.addOrUpdateUserData(playerUUID, tagMin);
            }
        }
    }

}