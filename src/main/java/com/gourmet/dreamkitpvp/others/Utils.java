package com.gourmet.dreamkitpvp.others;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

@UtilityClass
public class Utils {

    public String color(String msg) {
        return msg == null ? " " : ChatColor.translateAlternateColorCodes('&', msg);
    }

    public void sendActionBar(Player player, String message) {
        IChatBaseComponent chatComponent = new ChatComponentText(message);
        PacketPlayOutChat packet = new PacketPlayOutChat(chatComponent, (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public void removeStuckArrows(Player player) {
        EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();

        nmsPlayer.getDataWatcher().watch(9, (byte) 0);

        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(
                nmsPlayer.getId(), nmsPlayer.getDataWatcher(), true
        );

        for (Player online : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
        }
    }


    @SneakyThrows
    public void spawnParticle(Location location, EnumParticle particle) {

        float x = (float) location.getX();
        float y = (float) location.getY();
        float z = (float) location.getZ();
        float offsetX = 0;
        float offsetY = 0;
        float offsetZ = 0;
        float speed = 0;
        int count = 1;

        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true, x, y, z, offsetX, offsetY, offsetZ, speed, count);

        for (Player user : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) user).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public void setDurabilityByLevel(ItemStack item, int level) {
        if (item == null || item.getType() == Material.AIR) return;
        if (level < 1) level = 1;
        if (level > 4) level = 4;

        short maxDurability = item.getType().getMaxDurability();

        if (maxDurability <= 0) return;

        short damage;
        switch (level) {
            case 1: damage = (short) (maxDurability * 0.75); break;
            case 2: damage = (short) (maxDurability * 0.5); break;
            case 3: damage = (short) (maxDurability * 0.25); break;
            case 4: default: damage = 0; break;
        }

        item.setDurability(damage);
    }

    public int getDurabilityLevel(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return 0;
        }

        short maxDurability = item.getType().getMaxDurability();
        if (maxDurability <= 0) {
            return 0;
        }

        short damage = item.getDurability();
        double healthRatio = (maxDurability - damage) / (double) maxDurability;

        if (healthRatio <= 0.25) {
            return 1;
        } else if (healthRatio <= 0.50) {
            return 2;
        } else if (healthRatio <= 0.75) {
            return 3;
        } else {
            return 4;
        }
    }
}
