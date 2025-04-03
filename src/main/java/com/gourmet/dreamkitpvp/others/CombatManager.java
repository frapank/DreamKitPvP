package com.gourmet.dreamkitpvp.others;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class CombatManager {

    private final HashMap<UUID, BukkitTask> combatTasks;
    private final HashMap<UUID, Long> combatEndTimes;

    public CombatManager(){
        combatTasks = new HashMap<>();
        combatEndTimes = new HashMap<>();
    }

    public void startCombat(Player player) {
        UUID playerId = player.getUniqueId();

        if (combatTasks.containsKey(playerId)) {
            combatTasks.get(playerId).cancel();
        }

        long endTime = System.currentTimeMillis() + 10000;
        combatEndTimes.put(playerId, endTime);

        BukkitTask task = Bukkit.getScheduler().runTaskLater(DreamKitPvP.getInstance(), () -> {
            combatTasks.remove(playerId);
            combatEndTimes.remove(playerId);
            player.sendMessage("Sei fuori dal combattimento.");
        }, 200L);

        combatTasks.put(playerId, task);
        player.sendMessage("Sei in combattimento!");
    }

    public long getRemainingCombatTime(Player player) {
        UUID playerId = player.getUniqueId();

        if (!combatEndTimes.containsKey(playerId)) {
            return 0;
        }

        long remaining = combatEndTimes.get(playerId) - System.currentTimeMillis();
        return remaining > 0 ? remaining : 0;
    }
}
