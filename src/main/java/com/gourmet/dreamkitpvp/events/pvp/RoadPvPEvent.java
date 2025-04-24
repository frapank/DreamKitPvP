package com.gourmet.dreamkitpvp.events.pvp;

import com.gourmet.dreamkitpvp.others.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.UUID;

public class RoadPvPEvent implements Listener {

    private final HashMap<UUID, Integer> hitCount = new HashMap<>();

    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.FISHING_ROD) return;

        int currLevel = Utils.getDurabilityLevel(item);
        if (currLevel <= 1) {
            event.setCancelled(true);
            return;
        }

        event.setCancelled(true);
        Utils.setDurabilityByLevel(item, currLevel - 1);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack inHand = player.getItemInHand();
        if (inHand != null && inHand.getType() == Material.FISHING_ROD) {
            int level = Utils.getDurabilityLevel(inHand);
            if (level <= 1) {
                Utils.sendActionBar(player, Utils.color("&cTroppo rotta"));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();

        ItemStack inHand = player.getItemInHand();
        if (inHand == null) return;


        UUID uuid = player.getUniqueId();
        int count = hitCount.getOrDefault(uuid, 0) + 1;
        if (count >= 3) {
            count = 0;
            boostRodDurability(player);
        }
        hitCount.put(uuid, count);
    }

    private void boostRodDurability(Player player) {
        PlayerInventory inv = player.getInventory();
        for (int slot = 0; slot < inv.getSize(); slot++) {
            ItemStack item = inv.getItem(slot);
            if (item != null && item.getType() == Material.FISHING_ROD) {
                int current = Utils.getDurabilityLevel(item);
                int next = Math.min(current + 1, 4);
                Utils.setDurabilityByLevel(item, next);
                break;
            }
        }
    }
}
