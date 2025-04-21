package com.gourmet.dreamkitpvp.events;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class RemoveDropEvent implements Listener {

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        List<ItemStack> drops = event.getDrops();
        drops.removeIf(item -> DreamKitPvP.getConfigManager().isBannedDrop(item.getType()) || (DreamKitPvP.getConfigManager().shouldRemoveUnenchanted() && item.getEnchantments().isEmpty()));
    }

}
