package com.gourmet.dreamkitpvp.events;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceBlockEvent implements Listener {
    @EventHandler
    public void placeBlockEvent(BlockPlaceEvent event){
        if(!DreamKitPvP.getBuildModePlayer().contains(event.getPlayer())){
            event.setCancelled(true);
        }
    }
}
