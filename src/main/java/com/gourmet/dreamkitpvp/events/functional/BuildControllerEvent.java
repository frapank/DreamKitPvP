package com.gourmet.dreamkitpvp.events.functional;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.Utils;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BuildControllerEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void placeBlockEvent(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.OBSIDIAN || block.getType() == Material.WEB) return;

        if (!DreamKitPvP.getBuildModePlayer().contains(event.getPlayer())) {
            spawnParticle(block);
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreakBlock(BlockBreakEvent event) {
        if (!DreamKitPvP.getBuildModePlayer().contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    private void spawnParticle(Block block) {
        if (block != null && block.getLocation() != null) {
            Location loc = new Location(block.getWorld(), block.getX() + 0.500, block.getY() + 0.500, block.getZ() + 0.500);
            Utils.spawnParticle(loc, EnumParticle.CLOUD);
        }
    }
}

