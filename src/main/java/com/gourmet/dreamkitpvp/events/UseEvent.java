package com.gourmet.dreamkitpvp.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class UseEvent implements Listener {

    private Map<Player, Long> lastUsed = new HashMap<>();
    private static final long COOLDOWN_TIME = 10000;

    @EventHandler
    public void onPlayerUseItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        ItemStack item = event.getItem();
        if (item == null) return;

        if (item.getType() == Material.GOLDEN_APPLE || item.getType() == Material.ENDER_PEARL) {
            long currentTime = System.currentTimeMillis();

            if (lastUsed.containsKey(player)) {
                long lastUsedTime = lastUsed.get(player);

                if (currentTime - lastUsedTime < COOLDOWN_TIME) {
                    long timeLeft = COOLDOWN_TIME - (currentTime - lastUsedTime);
                    player.sendMessage("Puoi usare di nuovo questo oggetto tra " + (timeLeft / 1000) + " secondi");
                    event.setCancelled(true);
                    return;
                }
            }

            lastUsed.put(player, currentTime);

            player.sendMessage("Oggetto utilizzato! Puoi usarlo di nuovo tra 10 secondi");
        }
    }
}
