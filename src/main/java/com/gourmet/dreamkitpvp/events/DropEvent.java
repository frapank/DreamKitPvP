package com.gourmet.dreamkitpvp.events;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.ConfigManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

//TODO: Da mettere tramite config
public class DropEvent implements Listener {

    @EventHandler
    public void onKill(PlayerDeathEvent event){
        ConfigManager config = DreamKitPvP.getInstance().getConfigManager();
        List<ItemStack> drops = event.getDrops();

        drops.removeIf(item ->
                config.isBannedDrop(item.getType()) || (config.shouldRemoveUnenchanted() && item.getEnchantments().isEmpty())
        );
    }

}
