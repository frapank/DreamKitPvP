package com.gourmet.dreamkitpvp.events;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakBlockEvent implements Listener {

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event){
        if(!DreamKitPvP.getBuildModePlayer().contains(event.getPlayer())){
            event.setCancelled(true);
        }
    }
}
