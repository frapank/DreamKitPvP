package com.gourmet.dreamkitpvp.others;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

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

    public void removeAllArmorStand() {
        try {
            String configName = ChatColor.stripColor(Utils.color(DreamKitPvP.getConfigManager().getAirDropName()));

            for (World world : Bukkit.getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (!(entity instanceof ArmorStand)) continue;

                    ArmorStand armorStand = (ArmorStand) entity;
                    String name = armorStand.getCustomName();
                    if (name == null) continue;

                    String strippedName = ChatColor.stripColor(name);

                    if (strippedName.equalsIgnoreCase(configName) || strippedName.equalsIgnoreCase("Meteora")) {
                        armorStand.remove();
                    }
                }
            }

        } catch (Exception e) {
            Logger.warning("Errore nella rimozione degli ArmorStand: " + e.getMessage());
        }
    }



    List<ArmorStand> armorStands = (List<ArmorStand>) DreamKitPvP.getConfigManager().getSpawn().getWorld().getEntitiesByClass(ArmorStand.class);
    @SneakyThrows
    public void spawnParticle(Location location, @NonNull EnumParticle particle) {

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

    public void setDurabilityByLevel(@NonNull ItemStack item, int level) {
        if (item.getType() == Material.AIR) return;
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

    public int getDurabilityLevel(@NonNull ItemStack item) {
        if (item.getType() == Material.AIR) {
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

    public boolean isInNoHitZone(@NonNull Player player) {

        RegionManager rm = DreamKitPvP.getWg().getRegionManager(player.getWorld());
        if (rm == null) {
            return false;
        }

        Location loc = player.getLocation();
        ApplicableRegionSet set = rm.getApplicableRegions(loc);

        for (ProtectedRegion region : set) {
            StateFlag.State state = region.getFlag(DefaultFlag.PVP);
            if (state == StateFlag.State.DENY) {
                return true;
            }
        }
        return false;
    }

    public ItemStack skullItem(String id) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        String value = id;
        profile.getProperties().put("textures", new Property("textures", value));

        try {
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }

        skull.setItemMeta(meta);
        return skull;
    }

    public static ItemStack addNBT(ItemStack item, String key, String value) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = (nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound());

        tag.setString(key, value);
        nmsItem.setTag(tag);

        return CraftItemStack.asBukkitCopy(nmsItem);
    }


    public static boolean hasNBT(ItemStack item, String key) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        if (!nmsItem.hasTag()) return false;

        return nmsItem.getTag().hasKey(key);
    }


}
