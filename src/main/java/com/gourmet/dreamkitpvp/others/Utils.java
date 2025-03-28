package com.gourmet.dreamkitpvp.others;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import static org.spigotmc.SpigotConfig.config;

public class Utils {
    public static String color(String msg) {
        return msg == null ? " " : ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static void sendActionBar(Player player, String message) {
        IChatBaseComponent chatComponent = new ChatComponentText(message);
        PacketPlayOutChat packet = new PacketPlayOutChat(chatComponent, (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }


    public static void spawnParticle(Location location, EnumParticle particle) {

        if(config.getString("particel-up").isEmpty()){
            return;
        }

        try {
            float x = (float) location.getX();
            float y = (float) location.getY();
            float z = (float) location.getZ();
            float offsetX = 0;
            float offsetY = 0;
            float offsetZ = 0;
            float speed = 0;
            int count = 1;

            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(
                    particle,
                    true,
                    x, y, z,
                    offsetX, offsetY, offsetZ,
                    speed,
                    count
            );

            for (Player user : Bukkit.getOnlinePlayers()) {
                ((CraftPlayer) user).getHandle().playerConnection.sendPacket(packet);
            }
        } catch (Exception e){
            DreamKitPvP.getInstance().getLogger().warning("Error in particel-up config:" + e);
        }
    }

}
