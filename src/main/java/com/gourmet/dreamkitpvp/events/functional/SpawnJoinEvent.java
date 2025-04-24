package com.gourmet.dreamkitpvp.events.functional;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SpawnJoinEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();

        if(!DreamKitPvP.getConfigManager().isSpawnEnabled()){
            if(player.hasPermission("kitpvp.admin")){
                player.sendMessage(Utils.color("&cPlease set the spawn"));
            }
            return;
        }

        player.teleport(
                DreamKitPvP.getConfigManager().getSpawn()
        );
    }
}
