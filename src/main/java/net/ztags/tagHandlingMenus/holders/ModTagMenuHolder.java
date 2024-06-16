package net.ztags.tagHandlingMenus.holders;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ModTagMenuHolder implements InventoryHolder {

    private String tagID;

    public ModTagMenuHolder (String tagID) {
        this.tagID = tagID;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

    public String getTagID() {
        return this.tagID;
    }

}