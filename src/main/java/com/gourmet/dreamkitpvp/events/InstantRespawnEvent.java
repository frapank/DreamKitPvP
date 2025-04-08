package com.gourmet.dreamkitpvp.events;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class InstantRespawnEvent implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        new BukkitRunnable() {
            @Override
            public void run() {
                ((CraftPlayer) player).getHandle().playerConnection.a(new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN));
            }
        }.runTaskLater(DreamKitPvP.getInstance(), 2L);
    }

}
