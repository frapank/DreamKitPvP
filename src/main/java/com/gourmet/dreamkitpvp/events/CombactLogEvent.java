package com.gourmet.dreamkitpvp.events;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.CombatManager;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CombactLogEvent implements Listener {

    private final CombatManager combatManager = DreamKitPvP.getCombatManager();

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player damaged = (Player) event.getEntity();

        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player) {
                Player shooter = (Player) arrow.getShooter();
                if (!shooter.getUniqueId().equals(damaged.getUniqueId())) {
                    combatManager.startCombat(damaged);
                    combatManager.startCombat(shooter);
                }
            }
        }
        else if (event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            combatManager.startCombat(damaged);
            combatManager.startCombat(damager);
        }
    }

}
