package com.gourmet.dreamkitpvp.events.databases;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.Logger;
import com.gourmet.dreamkitpvp.others.Utils;
import com.gourmet.dreamkitpvp.others.managers.DatabaseManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;

public class KillDataEvent implements Listener {

    private final DatabaseManager databaseManager = DreamKitPvP.getDatabaseManager();
    private final HashMap<Player, DatabaseManager.PlayerData> playersData = DreamKitPvP.getDatabaseManager().getPlayersData();

    //Memo per gourmet futuro ho cambiato priortity con combactlogevent.java
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKill(PlayerDeathEvent event){

        if(!databaseManager.getIsOnline()){
            return;
        }

        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if(killer == null){
            return;
        }

        try {
            updateDatabase(victim.getName(), killer.getName());
            updateLocalData(victim.getName(), killer);
        } catch (Exception e){
            Logger.warning("Error in database kill management: " + e);
        }

    }

    private void updateDatabase(String victimName, String killerName){

        DatabaseManager.PlayerStats victimStats = databaseManager.getStatistics(victimName);
        DatabaseManager.PlayerStats killerStats = databaseManager.getStatistics(killerName);

        victimStats.deaths++;
        killerStats.kills++;

        killerStats.streak++;
        victimStats.streak = 0;

        double newKillerKD = (killerStats.deaths == 0) ? killerStats.kills : (double) killerStats.kills / killerStats.deaths;
        double newVictimKD = (victimStats.deaths == 0) ? victimStats.kills : (double) victimStats.kills / victimStats.deaths;

        databaseManager.updateStatistics(victimName, victimStats.kills, victimStats.deaths, newVictimKD, victimStats.bounty, victimStats.streak, victimStats.bastard);
        databaseManager.updateStatistics(killerName, killerStats.kills, killerStats.deaths, newKillerKD, killerStats.bounty, killerStats.streak, killerStats.bastard);
    }

    private void updateLocalData(String victimName, Player killer){

        DatabaseManager.PlayerData victimData = playersData.get(victimName);
        DatabaseManager.PlayerData killerData = playersData.get(killer.getName());

        DatabaseManager.PlayerStats victimStats = victimData.stats;
        DatabaseManager.PlayerStats killerStats = killerData.stats;

        victimStats.deaths++;
        killerStats.kills++;

        killerStats.streak++;
        victimStats.streak = 0;

        double newKillerKD = (killerStats.deaths == 0) ? killerStats.kills : (double) killerStats.kills / killerStats.deaths;
        double newVictimKD = (victimStats.deaths == 0) ? victimStats.kills : (double) victimStats.kills / victimStats.deaths;

        victimStats.kd = newVictimKD;
        killerStats.kd = newKillerKD;

        if (killerStats.streak % 10 == 0) {
            killerStats.bounty += 500;
            Utils.sendActionBar(killer, "&fNew bounty: &6&l" + killerStats.bounty + "$");
        }

    }
}
