package com.gourmet.dreamkitpvp.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.*;

public class DropEvent implements Listener {

    private final List<Material> blackListedMaterials =Arrays.asList(
            Material.IRON_SWORD,
            Material.DIAMOND_BOOTS,
            Material.DIAMOND_LEGGINGS,
            Material.DIAMOND_CHESTPLATE,
            Material.DIAMOND_HELMET
    );

    @EventHandler
    public void onKill(PlayerDeathEvent event){
        event.getDrops().removeIf(
                item -> blackListedMaterials.contains(item.getType())
        );
    }

}
