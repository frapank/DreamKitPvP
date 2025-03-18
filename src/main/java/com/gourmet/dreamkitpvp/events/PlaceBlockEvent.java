package com.gourmet.dreamkitpvp.events;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

//TODO: Non testato e manca la ragnatela che si espande e si toglie
public class PlaceBlockEvent implements Listener {

    @EventHandler
    public void placeBlockEvent(BlockPlaceEvent event){

        Block block = event.getBlock();
        Material blockMaterial = block.getType();

        switch (blockMaterial){
            case OBSIDIAN:
                runBlockRemover(block.getLocation());
                break;
            case WEB:

                break;
        }

    }

    public void runBlockRemover(Location location){
        new BukkitRunnable(){
            @Override
            public void run(){
                location.getBlock().setType(Material.AIR);
            }
        }.runTaskLaterAsynchronously(DreamKitPvP.instance, 20 * 5);
    }
}
