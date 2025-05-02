package com.gourmet.dreamkitpvp.events.functional;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.managers.ConfigManager;
import org.bukkit.entity.Arrow;
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
            }.runTaskLater(DreamKitPvP.getInstance(), 60L);
        }
    }

}
