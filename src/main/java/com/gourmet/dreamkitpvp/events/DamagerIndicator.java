package com.gourmet.dreamkitpvp.events;


import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class DamagerIndicator implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();

            new BukkitRunnable() {
                @Override
                public void run() {
                    double newHealth = Math.max(0, victim.getHealth());
                    String formattedHealth = String.format("%.1f", newHealth);

                    Utils.sendActionBar(damager, Utils.color("&f" + victim.getDisplayName() + ": " + formattedHealth + "&c❤"));
                }
            }.runTaskLater(DreamKitPvP.getInstance(), 1L);
        }
    }

}