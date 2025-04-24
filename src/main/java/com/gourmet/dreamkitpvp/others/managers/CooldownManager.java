package com.gourmet.dreamkitpvp.others.managers;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor
public class CooldownManager {

    static final long COOLDOWN_MILLIS = 10_000L;
    static final long COOLDOWN_TICKS = 200L;

    Map<UUID, BukkitTask> combatTasks = new HashMap<>();
    Map<UUID, Long> combatEndTimes = new HashMap<>();

    public void startCooldown(@NonNull Player player) {
        UUID playerId = player.getUniqueId();

        if (combatTasks.containsKey(playerId)) {
            combatTasks.get(playerId).cancel();
        }

        long endTime = System.currentTimeMillis() + COOLDOWN_MILLIS;
        combatEndTimes.put(playerId, endTime);

        BukkitTask task = Bukkit.getScheduler()
                .runTaskLater(DreamKitPvP.getInstance(), () -> {
                    combatTasks.remove(playerId);
                    combatEndTimes.remove(playerId);
                }, COOLDOWN_TICKS);

        combatTasks.put(playerId, task);
    }

    public long getRemainingCombatTime(@NonNull Player player) {
        UUID playerId = player.getUniqueId();
        Long endTime = combatEndTimes.get(playerId);
        if (endTime == null) {
            return 0;
        }
        long remaining = endTime - System.currentTimeMillis();
        return remaining > 0 ? remaining : 0;
    }

    public boolean isPlayerInCooldown(@NonNull Player player) {
        return combatTasks.containsKey(player.getUniqueId());
    }
}
