package com.gourmet.dreamkitpvp.events;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.CooldownManager;
import com.gourmet.dreamkitpvp.others.Utils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class CooldownItems implements Listener {

    private final CooldownManager cooldownManager = DreamKitPvP.getCooldownManager();

    @EventHandler
    public void onPlayerInteraction(PlayerInteractEvent event) {
        if (event.getItem() == null) {
            return;
        }

        if (event.getItem().getType() != Material.ENDER_PEARL) {
            return;
        }

        if (cooldownManager.isPlayerInCooldown(event.getPlayer())) {
            event.setCancelled(true);
            long remainingTime = cooldownManager.getRemainingCombatTime(event.getPlayer()) / 1000;
            event.getPlayer().sendMessage(Utils.color("&eDevi aspettare " + remainingTime + "s")); //TODO: Da mettere nel config
            return;
        }

        cooldownManager.startCooldown(event.getPlayer());
    }
}
