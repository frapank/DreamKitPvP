package com.gourmet.dreamkitpvp.events.pvp;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.Utils;
import com.gourmet.dreamkitpvp.others.managers.CombatManager;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class CombatEvent implements Listener {

    private final CombatManager combatManager = DreamKitPvP.getCombatManager();

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player damaged = (Player) event.getEntity();

        //Check if the player is in a safe zone trough worldguard
        if(Utils.isInSafeZone(damaged)){
            Utils.sendActionBar(damaged.getKiller(), Utils.color("&cNon puoi qui"));
            return;
        }

        //This will start the combat, even if the player was hit by an arrow
        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player) {
                Player shooter = (Player) arrow.getShooter();
                if (!shooter.getUniqueId().equals(damaged.getUniqueId())) {
                    combatManager.startCombat(damaged);
                    combatManager.startCombat(shooter);
                }
            }
        } else if (event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            combatManager.startCombat(damaged);
            combatManager.startCombat(damager);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        combatManager.stopCombat(event.getEntity());
    }

}
