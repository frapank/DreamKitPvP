package com.gourmet.dreamkitpvp.custom.blackmarket;

import com.gourmet.dreamkitpvp.others.Utils;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

@UtilityClass
public class BlackMarketGui {

    private final String PERK_NBT  = "perk";
    private final String SPECIAL_NBT  = "special";

    public void openBlackMarket(Player player) {
        Inventory gui = Bukkit.createInventory(null, 36, Utils.color("&0&lBlack Market"));

        ItemStack grayPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta grayMeta = grayPane.getItemMeta();
        grayMeta.setDisplayName(" ");
        grayPane.setItemMeta(grayMeta);

        ItemStack blackPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemMeta blackMeta = blackPane.getItemMeta();
        blackMeta.setDisplayName(" ");
        blackPane.setItemMeta(blackMeta);

        for (int i = 0; i < gui.getSize(); i++) {
            if (i >= 27) {
                gui.setItem(i, blackPane);
            } else {
                gui.setItem(i, grayPane);
            }
        }

        gui.setItem(11, perkIcon());
        gui.setItem(15, specialIcon());

        player.openInventory(gui);
    }

    private ItemStack perkIcon() {
        ItemStack skull = Utils.createHeadItem(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWIwMGIzNGZlOGIzYzY4YTJlZDVjNTVhYzg0YmYxMWMxN2VlMDI3MDE2OGJiMzMwMTJkMTVhYTU3NDMyNmFiIn19fQ=="
        );

        ItemMeta meta = skull.getItemMeta();
        meta.setDisplayName(Utils.color("&d&lPerk Token"));
        meta.setLore(Arrays.asList(
                Utils.color("&8Usalo per sbloccare"),
                Utils.color("&8un potenziamento speciale"),
                Utils.color(""),
                Utils.color("&eClick per aprire la lista")
        ));
        skull.setItemMeta(meta);

        skull = Utils.addNbtToItem(skull, PERK_NBT, "0");

        return skull;
    }

    private ItemStack specialIcon() {
        ItemStack item = Utils.createHeadItem(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWZkNDlhNGJhMTRlMTMwMzZhOGRhNjFlNWY1NjEzZmIzOWYxZDg4N2Y4Yjg2YjY1ZjY5ZjQ0NTM4MzNlYzE3ZCJ9fX0="
        );

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.color("&b&lSpecial Item"));
        meta.setLore(Arrays.asList(
                Utils.color("&8Un oggetto unico e raro"),
                Utils.color("&8disponibile per tempo limitato"),
                Utils.color(""),
                Utils.color("&eClicca per acquistare")
        ));
        item.setItemMeta(meta);

        item = Utils.addNbtToItem(item, SPECIAL_NBT, "0");

        return item;
    }
}
