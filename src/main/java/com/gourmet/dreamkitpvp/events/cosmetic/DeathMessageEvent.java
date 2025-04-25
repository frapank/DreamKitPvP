package com.gourmet.dreamkitpvp.events.cosmetic;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.Utils;
import com.gourmet.dreamkitpvp.others.managers.ConfigManager;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DeathMessageEvent implements Listener {

    ConfigManager configManager = DreamKitPvP.getConfigManager();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(null);

        if (Bukkit.getOnlinePlayers().size() < 10) {
            String message = configManager.getJoinMessage().replace("%player%", player.getName());
            sendToAll(message);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(null);

        if (Bukkit.getOnlinePlayers().size() < 10) {
            String message = configManager.getLeaveMessage().replace("%player%", player.getName());
            sendToAll(message);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        event.setDeathMessage(null);

        String message;

        if (killer != null && killer != victim) {
            message = configManager.getKilledMessage().
                    replace("%victim%", victim.getName()).
                    replace("%killer%", killer.getName());
        } else {
            message = configManager.getDeathMessage().replace("%player%", victim.getName());
        }

        sendToAll(message);
    }

    private void sendToAll(String message){
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage(Utils.color(message));
        });
    }
}
