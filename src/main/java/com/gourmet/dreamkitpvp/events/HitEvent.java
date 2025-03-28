package com.gourmet.dreamkitpvp.events;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class HitEvent implements Listener {

    //Todo: Avvolte manda storto (fix)
    @EventHandler
    public void onBowBoost(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Arrow arrow = (Arrow) event.getDamager();
        Player player = (Player) event.getEntity();

        if (arrow.getShooter() instanceof Player && arrow.getShooter().equals(player)) {
            ItemStack bow = player.getInventory().getItemInHand();
            if (bow.getType() == Material.BOW && bow.getEnchantmentLevel(org.bukkit.enchantments.Enchantment.KNOCKBACK) > 0) {
                applyBowBoost(player, arrow.getVelocity());
            }
        }
    }

    private void applyBowBoost(Player player, Vector arrowVelocity) {
        Vector boost = arrowVelocity.multiply(1.5);
        player.setVelocity(player.getVelocity().add(boost));
    }
}
