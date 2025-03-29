package com.gourmet.dreamkitpvp.events;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class DespawnBlockEvent implements Listener {

    private final DreamKitPvP plugin;
    private final int despawnTime = 15;

    public DespawnBlockEvent(DreamKitPvP plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
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
        }.runTaskLater(plugin, despawnTime * 20L);

    }

}
