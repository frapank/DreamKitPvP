package com.gourmet.dreamkitpvp.events.pvp;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.managers.HealthRegenManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

//Molto sperimentare e da testare in gruppo
public class DamageRegenEvent implements Listener {

    private final Map<UUID, Map<UUID, Double>> damageRecords = new HashMap<>();
    private final Map<String, BukkitRunnable> timeoutTasks = new HashMap<>();

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        Entity damager = e.getDamager();
        Entity victim = e.getEntity();
        if (!(damager instanceof Player) || !(victim instanceof Player)) return;

        Player atk = (Player) damager;
        Player vic = (Player) victim;
        double dmg = e.getFinalDamage();

        UUID vicId = vic.getUniqueId(), atkId = atk.getUniqueId();
        damageRecords
                .computeIfAbsent(vicId, k -> new HashMap<>())
                .merge(atkId, dmg, Double::sum);

        String key = taskKey(atkId, vicId);
        if (timeoutTasks.containsKey(key)) {
            timeoutTasks.get(key).cancel();
        }
        BukkitRunnable timeout = new BukkitRunnable() {
            @Override
            public void run() {
                Map<UUID, Double> m = damageRecords.get(vicId);
                if (m != null) {
                    m.remove(atkId);
                    if (m.isEmpty()) damageRecords.remove(vicId);
                }
                timeoutTasks.remove(key);
            }
        };
        timeout.runTaskLater(DreamKitPvP.getInstance(), 20L * 10);
        timeoutTasks.put(key, timeout);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player vic = e.getEntity();
        UUID vicId = vic.getUniqueId();
        Map<UUID, Double> m = damageRecords.remove(vicId);
        if (m == null || m.isEmpty()) return;

        m.forEach((atkId, dmg) -> {
            Player atk = Bukkit.getPlayer(atkId);
            if (atk == null || !atk.isOnline()) return;

            int hpToRegen = (int) Math.ceil(dmg);

            new HealthRegenManager().regenPlayerHealth(atk, hpToRegen);
        });

        timeoutTasks.keySet().removeIf(k -> {
            if (k.endsWith("|" + vicId.toString())) {
                timeoutTasks.get(k).cancel();
                return true;
            }
            return false;
        });
    }
    
    public DamageRegenEvent() {
        Bukkit.getPluginManager().registerEvents(this, DreamKitPvP.getInstance());
    }

    private String taskKey(UUID attacker, UUID victim) {
        return attacker.toString() + "|" + victim.toString();
    }
}
