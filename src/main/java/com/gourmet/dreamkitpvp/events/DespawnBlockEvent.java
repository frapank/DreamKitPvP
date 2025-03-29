package com.gourmet.dreamkitpvp.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class DespawnBlockEvent implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Block block = e.getBlock();
        Material type = block.getType();
        if (type == Material.OBSIDIAN || type == Material.WEB) {
            scheduleBlockRemoval(block);
        }
    }

    private void scheduleBlockRemoval(Block block) {}

}
