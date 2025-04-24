package com.gourmet.dreamkitpvp.others.managers;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class HealthRegenManager {

    public void regenPlayerHealth(final Player player, int health) {
        new BukkitRunnable() {
            private int remaining = health;

            @Override
            public void run() {
                if (remaining <= 0) {
                    this.cancel();
                    return;
                }

                double current = player.getHealth();
                double max = player.getMaxHealth();
                double toSet = Math.min(current + 1.0, max);

                player.setHealth(toSet);
                remaining--;
            }
        }.runTaskTimer(DreamKitPvP.getInstance(), 0L, 10L);
    }
}
