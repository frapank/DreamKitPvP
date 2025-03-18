package com.gourmet.dreamkitpvp.events;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;

public class QuitEvent implements Listener {

    //TODO: Da rivedere appena sara implementato il combat system
    //@EventHandler
    //public void onQuit(PlayerQuitEvent event) {
    //    Player player = event.getPlayer();
    //
    //    if (!playersInCombat.contains(player)) {
    //        return;
    //    }
    //
    //    Location deathLocation = player.getLocation();
    //
    //    for (ItemStack item : player.getInventory().getContents()) {
    //        if (item != null && item.getType() != Material.AIR) {
    //            deathLocation.getWorld().dropItemNaturally(deathLocation, item);
    //        }
    //    }
    //    for (ItemStack armor : player.getInventory().getArmorContents()) {
    //        if (armor != null && armor.getType() != Material.AIR) {
    //            deathLocation.getWorld().dropItemNaturally(deathLocation, armor);
    //        }
    //    }
    //
    //    player.getInventory().clear();
    //    playersInCombat.remove(player);
    //    player.setHealth(0);
    //}
}
