package com.gourmet.dreamkitpvp.events.databases;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.managers.DatabaseManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class JoinerDataEvent implements Listener {

    private final DatabaseManager databaseManager = DreamKitPvP.getDatabaseManager();
    private final HashMap<Player, DatabaseManager.PlayerData> playersData = DreamKitPvP.getDatabaseManager().getPlayersData();

    @EventHandler
    public void onJoin(PlayerJoinEvent event){

        if(!databaseManager.getIsOnline()){
            return;
        }

        DatabaseManager.PlayerData newPlayer = new DatabaseManager.PlayerData(event.getPlayer(), databaseManager);
        playersData.put(event.getPlayer(), newPlayer);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){

        if(!databaseManager.getIsOnline()){
            return;
        }

        playersData.remove(event.getPlayer());
    }
}
