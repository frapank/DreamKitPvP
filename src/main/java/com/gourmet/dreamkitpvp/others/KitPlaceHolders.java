package com.gourmet.dreamkitpvp.others;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class KitPlaceHolders extends PlaceholderExpansion {
    @Override
    @NotNull
    public String getAuthor() {
        return "Gourmet";
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "kitpvp";
    }

    @Override
    @NotNull
    public String getVersion() {
        return "1.0.0";
    }


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

        return "";
    }


}
