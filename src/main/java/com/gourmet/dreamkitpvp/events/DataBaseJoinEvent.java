package com.gourmet.dreamkitpvp.events;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.DatabaseManager;
import com.gourmet.dreamkitpvp.others.Logger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class DataBaseJoinEvent implements Listener {

    private final DatabaseManager databaseManager = DreamKitPvP.getDatabaseManager();

    @EventHandler
    public void onJoin(PlayerJoinEvent event){

        if(!databaseManager.getIsOnline()){
            return;
        }

        Player player = event.getPlayer();
        databaseManager.createUser(player);
        Logger.info("" + databaseManager.getFragmentByName(player.getName()));

    }
}
