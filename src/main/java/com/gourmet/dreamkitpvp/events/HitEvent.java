package com.gourmet.dreamkitpvp.events;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.ConfigManager;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;

public class HitEvent implements Listener {

    private final ConfigManager configManager = DreamKitPvP.getConfigManager();

    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        LivingEntity livingEntity = e.getEntity();
        Vector dir = livingEntity.getLocation().getDirection();
        Arrow a = (Arrow)e.getProjectile();
        double speed = a.getVelocity().length();
        Vector vel = dir.multiply(speed);
        a.setVelocity(vel);
    }

    @EventHandler
    public void onPlayerVelocity(PlayerVelocityEvent e) {
        Player p = e.getPlayer();
        Vector initialVelocity = e.getVelocity();
        EntityDamageEvent event = p.getLastDamageCause();

        if (event == null || event.isCancelled() || !(event instanceof EntityDamageByEntityEvent)) {
            return;
        }

        Entity damager = ((EntityDamageByEntityEvent)event).getDamager();
        if (damager instanceof Arrow) {
            Arrow a = (Arrow)damager;
            if (a.getShooter().equals(p)) {
                double horizontalMultiplier = configManager.getHorizontalMultiplier();
                double verticalMultiplier = configManager.getVerticalMultiplier();
                double horizontalSpeed = Math.sqrt(initialVelocity.getX() * initialVelocity.getX() + initialVelocity.getZ() * initialVelocity.getZ()) * horizontalMultiplier;
                double verticalSpeed = initialVelocity.getY() * verticalMultiplier;
                Vector dir = a.getLocation().getDirection().normalize();
                Vector newVelocity = new Vector(-dir.getX() * horizontalSpeed, initialVelocity.getY() * verticalSpeed, dir.getZ() * horizontalSpeed);
                e.setVelocity(newVelocity);
            }
        }
    }
}
