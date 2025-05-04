package com.gourmet.dreamkitpvp.events.blackmarket;

import com.gourmet.dreamkitpvp.others.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GuiEvent implements Listener {

    @EventHandler
    public void onBlackMarketClick(InventoryClickEvent event) {
        if (event.getInventory() == null
                || event.getCurrentItem() == null
                || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }

        String title = event.getView().getTitle();
        if (!ChatColor.stripColor(title).equals("Black Market")) {
            return;
        }

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();

        if (Utils.hasNBT(clicked, "perk")) {
            player.closeInventory();
            return;
        }

        if (Utils.hasNBT(clicked, "special")) {
            player.closeInventory();
            return;
        }

    }
}
