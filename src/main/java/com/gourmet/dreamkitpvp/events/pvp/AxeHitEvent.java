package com.gourmet.dreamkitpvp.events.pvp;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AxeHitEvent implements Listener {

    private final int[] ARMOR_SLOTS = {36, 37, 38, 39};

    //This will increase the damage if the weapons is an axe
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {

        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) return;

        Player victim = (Player) e.getEntity();
        Player attacker = (Player) e.getDamager();
        ItemStack attackerItem = attacker.getInventory().getItemInHand();
        Inventory inventory = victim.getInventory();

        if (isAxe(attackerItem.getType())) {
            for (int slot : ARMOR_SLOTS) {
                damage(inventory.getItem(slot));
            }
        }

    }

    private void damage(@NotNull ItemStack item) {
        if (item.getType() == Material.AIR) return;

        short newDurability = (short) (item.getDurability() + 5);

        if (newDurability >= item.getType().getMaxDurability()) {
            item.setType(Material.AIR);
        } else {
            item.setDurability(newDurability);
        }
    }


    private boolean isAxe(Material material) {
        return material == Material.DIAMOND_AXE;
    }

}
