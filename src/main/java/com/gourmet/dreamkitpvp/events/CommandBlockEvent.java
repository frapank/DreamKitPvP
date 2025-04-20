package com.gourmet.dreamkitpvp.events;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class CommandBlockEvent implements Listener {

    private final List<String> blockedCommands = DreamKitPvP.getConfigManager().getBlockedCommands();

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();

        String msg = e.getMessage().substring(1);
        String cmd = msg.contains(" ") ? msg.split(" ")[0].toLowerCase() : msg.toLowerCase();

        if(blockedCommands.contains(cmd)){
            player.sendMessage(Utils.color(DreamKitPvP.getConfigManager().getCantWhileCombat()));
            e.setCancelled(true);
        }

    }
}
