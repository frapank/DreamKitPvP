package com.gourmet.dreamkitpvp.events;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.ConfigManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;

public class DespawnBlockEvent implements Listener {

    private final HashSet<Player> builderPlayers = DreamKitPvP.getBuildModePlayer();
    private final int despawnTime = DreamKitPvP.getConfigManager().getDespawnTime();

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (builderPlayers.contains(e.getPlayer())) {
            return;
        }
        Block block = e.getBlock();
        Material type = block.getType();
        if (type == Material.OBSIDIAN || type == Material.WEB) {
            scheduleBlockRemoval(block);
        }
    }

    private void scheduleBlockRemoval(Block block) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (block.getType() == Material.OBSIDIAN || block.getType() == Material.WEB) {
                    block.setType(Material.AIR);
                }
            }
        }.runTaskLater(DreamKitPvP.getInstance(), despawnTime * 20L);
    }

}
