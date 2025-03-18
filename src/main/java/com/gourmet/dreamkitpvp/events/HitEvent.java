package com.gourmet.dreamkitpvp.events;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Objects;

//TODO: Fate un qui la parte dove non ci si puo mettere in combat da soli
public class HitEvent implements Listener {


    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof LivingEntity)) return;

        Player attacker = (Player) event.getDamager();
        LivingEntity target = (LivingEntity) event.getEntity();
        ItemStack weapon = attacker.getInventory().getItemInHand();

        if (Objects.requireNonNull(weapon.getType()) == Material.DIAMOND_AXE) {
            event.setDamage(event.getDamage() * 0.7);
            damageArmor(target, 2);
        }
    }

    private void damageArmor(LivingEntity entity, int multiplier) {
        if (!(entity instanceof Player)) return;
        Player player = (Player) entity;

        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor != null && armor.getType() != Material.AIR) {
                armor.setDurability((short) (armor.getDurability() + multiplier));
            }
        }
    }


    //Todo: Avvolte manda storto
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
