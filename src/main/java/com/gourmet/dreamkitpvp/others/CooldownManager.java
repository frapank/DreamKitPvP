package com.gourmet.dreamkitpvp.others;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class CooldownManager {

    private final HashMap<UUID, BukkitTask> combatTasks;
    private final HashMap<UUID, Long> combatEndTimes;

    public CooldownManager() {
        combatTasks = new HashMap<>();
        combatEndTimes = new HashMap<>();
    }

    public void startCooldown(Player player) {
        UUID playerId = player.getUniqueId();

        if (combatTasks.containsKey(playerId)) {
            combatTasks.get(playerId).cancel();
        }

        long endTime = System.currentTimeMillis() + 10000;
        combatEndTimes.put(playerId, endTime);

        BukkitTask task = Bukkit.getScheduler().runTaskLater(DreamKitPvP.getInstance(), () -> {
            combatTasks.remove(playerId);
            combatEndTimes.remove(playerId);
        }, 200L);

        combatTasks.put(playerId, task);
        if (combatTasks.containsKey(playerId)) {
        }
    }

    public long getRemainingCombatTime(Player player) {
        UUID playerId = player.getUniqueId();

        if (!combatEndTimes.containsKey(playerId)) {
            return 0;
        }

        long remaining = combatEndTimes.get(playerId) - System.currentTimeMillis();
        return remaining > 0 ? remaining : 0;
    }

    public boolean isPlayerInCooldown(Player player){
        return combatTasks.containsKey(player.getUniqueId());
    }
}
