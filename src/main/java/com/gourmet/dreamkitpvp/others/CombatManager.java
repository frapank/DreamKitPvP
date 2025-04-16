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

    public CombatManager() {
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
            player.sendMessage(Utils.color(DreamKitPvP.getConfigManager().getOutCombatMessage()));
        }, 200L);

        combatTasks.put(playerId, task);
        player.sendMessage(Utils.color(DreamKitPvP.getConfigManager().getCombatMessage()));
    }


    public int getRemainingCombatTime(Player player) {
        UUID playerId = player.getUniqueId();

        if (!combatEndTimes.containsKey(playerId)) {
            return 0;
        }

        long remainingMillis = combatEndTimes.get(playerId) - System.currentTimeMillis();
        long remainingSeconds = remainingMillis / 1000;

        return (int) (remainingSeconds > 0 ? remainingSeconds : 0);
    }


    public boolean isPlayerInCombat(Player player) {
        return combatTasks.containsKey(player.getUniqueId());
    }
}
