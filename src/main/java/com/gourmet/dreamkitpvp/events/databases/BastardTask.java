package com.gourmet.dreamkitpvp.events.databases;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.managers.DatabaseManager;
import org.bukkit.scheduler.BukkitRunnable;

public class BastardTask {

    public BastardTask(DatabaseManager databaseManager){

        if (!databaseManager.getIsOnline())
            return;


        long ticksPerHour = 20L * 60 * 60;
        new BukkitRunnable() {
            @Override
            public void run() {
                databaseManager.decreaseInfamyForAll(5);
            }
        }.runTaskTimerAsynchronously(DreamKitPvP.getInstance(), ticksPerHour, ticksPerHour);

    }

}
