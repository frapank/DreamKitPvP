package com.gourmet.dreamkitpvp.events.functional;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.Utils;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ArrowRemovalEvent implements Listener {

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();

        if (projectile instanceof Arrow) {
            Arrow arrow = (Arrow) projectile;

            new BukkitRunnable() {
                @Override
                public void run() {
                    arrow.remove();
                }
            }.runTaskLater(DreamKitPvP.getInstance(), 40L);
        }
    }

    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Arrow arrow = (Arrow) event.getDamager();
        Player hitPlayer = (Player) event.getEntity();

        new BukkitRunnable() {
            @Override
            public void run() {
                arrow.remove();
                Utils.removeArrowsOnPlayer(hitPlayer);
            }
        }.runTaskLater(DreamKitPvP.getInstance(), 40L);
    }



}
