package net.ztags;

import java.util.UUID;

public class PlayerData {
    private UUID playerUUID;
    private String tagName;

    public PlayerData(UUID playerUUID, String tagName) {
        this.playerUUID = playerUUID;
        this.tagName = tagName;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}