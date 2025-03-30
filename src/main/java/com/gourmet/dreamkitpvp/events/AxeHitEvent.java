package com.gourmet.dreamkitpvp.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AxeHitEvent implements Listener {
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {

        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player))
            return;

        Player player = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();
        ItemStack damagerItem = damager.getInventory().getItemInHand();
        Inventory inventory = player.getInventory();

        if (isAxe(damagerItem.getType())) {
            damage(inventory.getItem(39));
            damage(inventory.getItem(38));
            damage(inventory.getItem(37));
            damage(inventory.getItem(36));
        }

    }

    private void damage(ItemStack item) {
        if (item == null | item.getType() == Material.AIR) return;

        short newDurability = (short) (item.getDurability() + 5);

        if (newDurability >= item.getType().getMaxDurability()) {
            item.setType(Material.AIR);
        } else {
            item.setDurability(newDurability);
        }
    }


    private boolean isAxe(Material material) {
        return material == Material.IRON_AXE || material == Material.DIAMOND_AXE;
    }

}
