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
public class ItemGui {

    private final String ROCK_ITEM = "rock";

    public void openBlackMarketItems(Player player) {
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

        gui.setItem(11, rockItem());

        player.openInventory(gui);
    }

    private ItemStack rockItem() {
        ItemStack skull = Utils.skullItem(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjhkODE4ZTY1ZWUzNmNkY2FiNmUzMTVmNDk5ZDNmNWRkZTIzMjNiZjJkZjU2ZjFlOTE0MDg5Y2ViZWZhMzdmNiJ9fX0="
        );

        ItemMeta meta = skull.getItemMeta();
        meta.setDisplayName(Utils.color("&d&lSasso"));
        meta.setLore(Arrays.asList(
                Utils.color("&8Un item lancabbile che infligge"),
                Utils.color("&8del danno senza curarsi della armatura"),
                Utils.color("&8Costo: &e4 frammenti"),
                Utils.color(""),
                Utils.color("&eClick per comprare")
        ));
        skull.setItemMeta(meta);

        skull = Utils.addNBT(skull, ROCK_ITEM, "0");

        return skull;
    }



}
