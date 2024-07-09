package net.ztags;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class Buttons {

    public static ItemStack getAddBtn(String name) {
        ItemStack addBtnItem = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta addBtnMeta = (SkullMeta) addBtnItem.getItemMeta();
        GameProfile addBtnProfile = new GameProfile(UUID.randomUUID(), null);
        String addBtnTexture = "ewogICJ0aW1lc3RhbXAiIDogMTcyMDE0NDAxMDQ0NywKICAicHJvZmlsZUlkIiA6ICJmYzFhOTdlNTgxM2Y0NDI2YTNmZTI4ZjJiNDc1ZjA4ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJnZXRPbmxpbmVQbGF5ZXJzIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzZiNzQ0MDhiZDU3N2I4NGUzNDkyMjg1MDIxNWEyZGYyMmVmMjBkZmI1ODBmYzViZWYzYjQwZDFlMDRkOGY1NTEiCiAgICB9CiAgfQp9";
        String addBtnSignature = "DiPV5sXYmWfdpcvqSmIgfnZA2nuFqYGyLYwGK/bQRCMY7BJGWZPpvF0EkRrkUNI0pXSqAlgeSVLpYBZtiW6qKw0gmG4IPtgTFqIYC7J+hoLGTkKXegeAYcRo00/DkLzTtdIsDtXOWfrfKnyZtTdYsrMFfAaqgusZEt3H4AyPmQF7SiDwkVqktsX0u7BbzIAiGKrqeiUwMfMaWgrgEKa7dljHNFKyVY8lSvs3QqFJe9Me91ApeC9LziyfoUhIY+W3paP0Au6fqzu/Nrmy66+t1GmEDY642yziYoLh16sLHjKE6K/MkoV5hNEvUHYJrPL/QRvouXYi9Rf3jc6cjxElCaC0RYEnaIGdaLTwBI82KT42XMm+B6jtSu2j9I9YPxJvcq3rUkWgqdqFO/ZtCXSKV4FTaZekf4vkazDUhfjPh5x8RV4SIl/shN3hbry437j3IaN4aysmLgxCzwcWGeXUA4I00dH6XBJ/bYXVPotek8kg6S+CvCLWGDpQ5o9qBQ6aeuFmgLsRBZitQPdLymNDNvKg/xrkigtbV1TTZz3mMLjD6P0AUBtVW0ReJk38Ic/RiIOqNVh25MHj6UMSkfokQpZTMmJebVcdhm1pODp+Jd4DU+FrOKBysDF87lNa1p+N7TTZIX5FFiqp/WjNl7b67IXGbUN5uCtEydeseTtrKA0=";
        addBtnProfile.getProperties().put("textures", new Property("textures", addBtnTexture, addBtnSignature));
        try {
            Field addBtnProfileField = addBtnMeta.getClass().getDeclaredField("profile");
            addBtnProfileField.setAccessible(true);
            addBtnProfileField.set(addBtnMeta, addBtnProfile);
        } catch (Exception ignored) {}
        addBtnMeta.setDisplayName(name);
        addBtnItem.setItemMeta(addBtnMeta);
        return addBtnItem;
    }

    public static ItemStack getSubBtn(String name) {
        ItemStack addBtnItem = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta addBtnMeta = (SkullMeta) addBtnItem.getItemMeta();
        GameProfile addBtnProfile = new GameProfile(UUID.randomUUID(), null);
        String addBtnTexture = "ewogICJ0aW1lc3RhbXAiIDogMTcyMDE0NDAzMDU2NCwKICAicHJvZmlsZUlkIiA6ICI5OWY1MzhjMDhlN2E0NTg3YmU4MGJjNGVmNzU0ZmQyMSIsCiAgInByb2ZpbGVOYW1lIiA6ICJTb2xvV1MyIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2Q1MWUxNzZhNWNlOTNhNDdmODRjMGJkMmFlYWVmNmY1NjIxYTBhYTU3NzY4YjIzZWFlZTY5NTRkNjYyYjkzNGEiCiAgICB9CiAgfQp9";
        String addBtnSignature = "LBWrhix5SUcvibDyW0SpJ9Md2CtQB19hbAhcRJCqNPufxla6XkiAAFXSOpQUmEkVEjv82MIqosc8e5KfbosWBR21NN3K9G6ZOjHXcqCWcgmXwLOexFUkmx1FaJrSW1cAZWFkpkMGZrg022OidmqHU58k3FVcyAd8LkkhgpYOHB73ZhAMARWN9K1guzp7TsJ7yAJIuwXFVt5UCf/B76hFyMpLnG+Xb9Ih1UpdfhP8Q6uDYGphQecpRsnB1tmf32IuqhSbfPLD232Jk/FOeJIY9w07SRkynV4NROs5E+77tDGoqnTLAblUq6zV9TvuYaDzKF3G1icnBL5COc/lA6V3k5gndnY1FCtvwqA+7tu/J1Gr8L6hndh/qdkehXADsrx/VLd5M4K5QnDPTe1ldtn3A3AhtrQ2xCtB0tdHQtbqsBKxi86W9ogocU5Py916VBHfPgJvwejR3XMvvSqhhU5h14f6M0otrxetOZjWWyacemfb10qyvK8FAR21JuNs269O2k1LElJHti7EAiwj52NcOPrYxgFSRSoHZ5uETcFjzQ0ZGylh8z5v4mE6ql7I8u+lNT+0uLnHzReVye65hAoh/nYkkLiFN4Oesc6RFtydkILlKWvwYfaq0xr7Fxx9X8PVRgY1fKtROKlbLgCxrxFPt9QD88NZgXjFDWsigU4pk/I=";
        addBtnProfile.getProperties().put("textures", new Property("textures", addBtnTexture, addBtnSignature));
        try {
            Field addBtnProfileField = addBtnMeta.getClass().getDeclaredField("profile");
            addBtnProfileField.setAccessible(true);
            addBtnProfileField.set(addBtnMeta, addBtnProfile);
        } catch (Exception ignored) {}
        addBtnMeta.setDisplayName(name);
        addBtnItem.setItemMeta(addBtnMeta);
        return addBtnItem;
    }

    public static ItemStack getCloseBtn(String name) {
        ItemStack addBtnItem = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta addBtnMeta = (SkullMeta) addBtnItem.getItemMeta();
        GameProfile addBtnProfile = new GameProfile(UUID.randomUUID(), null);
        String addBtnTexture = "ewogICJ0aW1lc3RhbXAiIDogMTcyMDM5MjUzMDc4NiwKICAicHJvZmlsZUlkIiA6ICJhMmI1ZjhlM2MxZDI0ZmUzYTlkMzNiZTFhNzEzYTUwYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJOYXBpb0dvZCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lNmE1ZTUxNWY5MGZmNWZiMThjZjc2NTlkNmNkYmZiODcxNTlkMjhkYzdhNTE3NmQ0NzczMjg4ZGJiMDEyNjI4IgogICAgfQogIH0KfQ==";
        String addBtnSignature = "LH7e7okoMQsvLrsUQC6QYsatZNOdH3Fm5jnsHBiiA6fHT3RecCR1u/vZdM2IjFyP49ACzUhb/byEItEs5CKAQpQ53D1CL1coDFA4Hrk98G0seckOzCH0Gp2RkDffyo92I6hx87pDRXdgPg6ODXdSkWBthoK2E6fVhQ/v1iBHP5hF5VaL9SA96j9H4bwDd9hz5Bia7qLj8gbbRLMLmIJR6qJKUIKmtvlm0RAsIki72MdFRWI6QSnJKtetY3ufinj6yOKDfBRI1pKnzeF3QflfUnCzOBwjXI65s0dl6CGKIPSZo5YifNB4IYHQvRGI1Ue1JTKAzG7dATav+fYPxa/Mt77RGQGzMeiqUTEj3u0B67D7XQA6jpRIz65P23DcOEdb3YO2WJTL1X/3jGakhaQ/zTcd2kE1k9kypvU74PGB9VHtnQFfvsUZEtW06q6R+JZp8/EFa3FL7PG7v5XnLdjYOpUDO6tnboomdNL2YZcoopP31NttKL8mTbubGZd9x41pAncW8z0JWyyvecfF4WRpL0vkl5xRarCn1UyY5vPpL1lAoT1ySQnNRqIQi1UqwQ4SXa8On6mJPEVwrCl71x+UXEVcUaeES/LcTcI9JfAUL/s+NVa3oIlhwLukZ1qWzePX8AfN5e0jGbUj1Y8bqPSH+/9KlC5KBaxATnM3DH8D+yY=";
        addBtnProfile.getProperties().put("textures", new Property("textures", addBtnTexture, addBtnSignature));
        try {
            Field addBtnProfileField = addBtnMeta.getClass().getDeclaredField("profile");
            addBtnProfileField.setAccessible(true);
            addBtnProfileField.set(addBtnMeta, addBtnProfile);
        } catch (Exception ignored) {}
        addBtnMeta.setDisplayName(name);
        addBtnItem.setItemMeta(addBtnMeta);
        return addBtnItem;
    }

    public static ItemStack getBackBtn(String name) {
        ItemStack addBtnItem = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta addBtnMeta = (SkullMeta) addBtnItem.getItemMeta();
        GameProfile addBtnProfile = new GameProfile(UUID.randomUUID(), null);
        String addBtnTexture = "ewogICJ0aW1lc3RhbXAiIDogMTcyMDM5MjUwODM3NCwKICAicHJvZmlsZUlkIiA6ICIxNDU1MDNhNDRjZmI0NzcwYmM3NWNjMTRjYjUwMDE4NyIsCiAgInByb2ZpbGVOYW1lIiA6ICJMaWtlbHlFcmljIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzlhOTFlNDcyYzVhMzIwZDIxMTIzZmUzODhhM2RlMTY0MzBkNjI5ZTVjMzI5YTYzMmQyZTdlZWQ0YTc3ZmU5NiIKICAgIH0KICB9Cn0=";
        String addBtnSignature = "skbYAxoe555CexD6wO4TUBhVw/nlyRnkzfncv9lImd0E4DJSBHO7keZX3VhlZEmd+xyPPSB29QIIkqHE4M3/25cV4aCQn0TL1O98jhk66MbGe6IpRXiPdaY5as5WBohV/syXbW/jBZ9LktrNjTBdNTy6fsIzAUaXwb6i2PklC/dxMca22HoDzogfaP8isP0IdnCnbjbYI0IxarDLoj/5WpH+wMOXKQf04uYCqvX6mawGgJoQ+rtGHKIN+2uTRoy9PqzzquMXNziVb08CpyXgy9kER8r8y4m6LnRC3vHSsIx7dUTqyKdUaI0nD6Dv4Utr1S9OaWEZKJ4isFeF5HJ6WpXS6+xVUgCf7R5lf4Br54obkPZcaJIgmYJ6OFcfHVJwnQKiNDJoQiJBdn0qQnDRyvqPe7CL/8PM4U+wmQ7nhDWgryp2vEXgDgTCj56FYEFPoXHFHvL09T7Qb++d7Q9gcxxCDaIrIzycMIaqlHAuS3wex1rFyYy127IHzK+5jyPW0G7HWs7nsk9pJOFaugwBtpM+gfOhccv8iOrG+Xo2KAlz/yZMBWT8DHcQ14iNiM0+T+VgTjWKXt7C0rz//b4rkqZNvZGYKSZ6kwmk4r3dQo/g4cu+LyY+4G2iBREBUX6I8sUXkGvIX8HPDsKZuMkT2qc3OQutlkW3D4tuXy/bdMU=";
        addBtnProfile.getProperties().put("textures", new Property("textures", addBtnTexture, addBtnSignature));
        try {
            Field addBtnProfileField = addBtnMeta.getClass().getDeclaredField("profile");
            addBtnProfileField.setAccessible(true);
            addBtnProfileField.set(addBtnMeta, addBtnProfile);
        } catch (Exception ignored) {}
        addBtnMeta.setDisplayName(name);
        addBtnItem.setItemMeta(addBtnMeta);
        return addBtnItem;
    }

}