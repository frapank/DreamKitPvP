package com.gourmet.dreamkitpvp.events.blackmarket;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.custom.blackmarket.ItemGui;
import com.gourmet.dreamkitpvp.others.Logger;
import com.gourmet.dreamkitpvp.others.Utils;
import com.gourmet.dreamkitpvp.others.managers.DatabaseManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GuiEvent implements Listener {

    private final DatabaseManager databaseManager = DreamKitPvP.getDatabaseManager();

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
            ItemGui.openBlackMarketItems(player);
            return;
        }

        if (Utils.hasNBT(clicked, "rock")) {
            player.closeInventory();
            if(buyItem(player, 4)){
                return;
            }

            //Da l'oggetto

            return;
        }

    }

    private boolean buyItem(Player player, int price){
        try {
            DatabaseManager.PlayerData data = databaseManager.getPlayersData().get(player);
            int fragments = data.fragments;

            if(fragments < price){
                player.sendMessage(Utils.color("&cNon hai abbastanza soldi"));
                return false;
            }

            data.fragments -= price;
            databaseManager.updateFragments(player.getName(), data.fragments);
            return true;

        }catch (Exception e){
            Logger.warning("Can't give item to player: " + e.getMessage());
            return false;
        }
    }
}
