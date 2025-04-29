package com.gourmet.dreamkitpvp.others;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.managers.DatabaseManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;


public class KitPlaceHolders extends PlaceholderExpansion {
    @Override
    public @NotNull String getAuthor() {
        return "Gourmet";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "kitpvp";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    private final HashMap<Player, DatabaseManager.PlayerData> playersData = DreamKitPvP.getDatabaseManager().getPlayersData();

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        Player gamePlayer = player.getPlayer();

        //Combat log
        if(params.equals("combat".toLowerCase())){
            if(DreamKitPvP.getCombatManager().isPlayerInCombat(gamePlayer)){
                return String.valueOf(DreamKitPvP.getCombatManager().getRemainingCombatTime(gamePlayer));
            } else {
                return "free";
            }
        }

        //Fragments
        if(params.equals("fragments".toLowerCase())){
            if(DreamKitPvP.getDatabaseManager().getIsOnline()){
                return "" + playersData.get(player).fragments;
            } else {
                return "null";
            }
        }

        //Kills
        if(params.equals("kills".toLowerCase())){
            if(DreamKitPvP.getDatabaseManager().getIsOnline()){
                return "" + playersData.get(player).stats.kills;
            } else {
                return "null";
            }
        }

        //Deaths
        if(params.equals("deaths".toLowerCase())){
            if(DreamKitPvP.getDatabaseManager().getIsOnline()){
                return "" + playersData.get(player).stats.deaths;
            } else {
                return "null";
            }
        }

        //KD
        if(params.equals("kd".toLowerCase())){
            if(DreamKitPvP.getDatabaseManager().getIsOnline()){
                return "" + Math.round(playersData.get(player).stats.kd * 10.0) / 10.0;
            } else {
                return "null";
            }
        }

        //Streak
        if(params.equals("streak".toLowerCase())){
            if(DreamKitPvP.getDatabaseManager().getIsOnline()){
                return "" + playersData.get(player).stats.streak;
            } else {
                return "null";
            }
        }

        //Bastard
        if(params.equals("bastard".toLowerCase())){
            if(DreamKitPvP.getDatabaseManager().getIsOnline()){
                return "" + playersData.get(player).stats.bastard;
            } else {
                return "null";
            }
        }

        //bounty
        if(params.equals("bounty".toLowerCase())){
            if(DreamKitPvP.getDatabaseManager().getIsOnline()){

                int bounty = playersData.get(player).stats.bounty;
                return (bounty <= 0) ? Utils.color("&6&6[" + bounty + "$]") : "";

            } else {
                return "null";
            }
        }

        return "";
    }


}
