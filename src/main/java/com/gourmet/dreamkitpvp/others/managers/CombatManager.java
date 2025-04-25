package com.gourmet.dreamkitpvp.others.managers;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor
public class CombatManager {

    HashMap<UUID, BukkitTask> combatTasks = new HashMap<>();
    HashMap<UUID, Long> combatEndTimes = new HashMap<>();

    public void startCombat(@NonNull Player player) {
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
    }


    public int getRemainingCombatTime(@NonNull Player player) {
        UUID playerId = player.getUniqueId();

        if (!combatEndTimes.containsKey(playerId)) {
            return 0;
        }

        long remainingMillis = combatEndTimes.get(playerId) - System.currentTimeMillis();
        long remainingSeconds = remainingMillis / 1000;

        return (int) (remainingSeconds > 0 ? remainingSeconds : 0);
    }


    public boolean isPlayerInCombat(@NonNull Player player) {
        return combatTasks.containsKey(player.getUniqueId());
    }
}
