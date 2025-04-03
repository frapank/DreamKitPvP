package com.gourmet.dreamkitpvp.events;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.CombatManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CombactLogEvent implements Listener {

    private final CombatManager combatManager = DreamKitPvP.getCombatManager();

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player attacked = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();
            combatManager.startCombat(attacked);
            combatManager.startCombat(damager);
        }
    }

}
