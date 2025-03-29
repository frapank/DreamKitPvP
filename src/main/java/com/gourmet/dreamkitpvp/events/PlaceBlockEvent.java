package com.gourmet.dreamkitpvp.events;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceBlockEvent implements Listener {
    @EventHandler
    public void placeBlockEvent(BlockPlaceEvent event){
        Block block = event.getBlock();
        if(block.getType() == Material.OBSIDIAN || block.getType() == Material.WEB)
            return;

        if(!DreamKitPvP.getBuildModePlayer().contains(event.getPlayer()))
            event.setCancelled(true);

    }
}
