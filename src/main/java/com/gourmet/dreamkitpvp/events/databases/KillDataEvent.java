package com.gourmet.dreamkitpvp.events.databases;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.Utils;
import com.gourmet.dreamkitpvp.others.managers.DatabaseManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;

public class KillDataEvent implements Listener {

    private final DatabaseManager databaseManager = DreamKitPvP.getDatabaseManager();
    private final HashMap<Player, DatabaseManager.PlayerData> playersData = DreamKitPvP.getDatabaseManager().getPlayersData();

    @EventHandler
    public void onKill(PlayerDeathEvent event){

        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if(!(killer instanceof Player)){
            return;
        }

        updateDatabase(victim, killer);
        updateLocalData(victim, killer);

    }

    private void updateDatabase(Player victim, Player killer){

        DatabaseManager.PlayerStats victimStats = databaseManager.getStatistics(victim.getName());
        DatabaseManager.PlayerStats killerStats = databaseManager.getStatistics(killer.getName());

        victimStats.deaths++;
        killerStats.kills++;

        killerStats.streak++;
        victimStats.streak = 0;

        double newKillerKD = (killerStats.deaths == 0) ? killerStats.kills : (double) killerStats.kills / killerStats.deaths;
        double newVictimKD = (victimStats.deaths == 0) ? victimStats.kills : (double) victimStats.kills / victimStats.deaths;

        databaseManager.updateStatistics(victim.getName(), victimStats.kills, victimStats.deaths, newVictimKD, victimStats.bounty, victimStats.streak);
        databaseManager.updateStatistics(killer.getName(), killerStats.kills, killerStats.deaths, newKillerKD, killerStats.bounty, killerStats.streak);
    }

    private void updateLocalData(Player victim, Player killer){

        DatabaseManager.PlayerData victimData = playersData.get(victim);
        DatabaseManager.PlayerData killerData = playersData.get(killer);

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
