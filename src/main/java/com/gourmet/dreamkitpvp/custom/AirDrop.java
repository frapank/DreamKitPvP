package com.gourmet.dreamkitpvp.custom;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.Logger;
import com.gourmet.dreamkitpvp.others.Utils;
import com.gourmet.dreamkitpvp.others.managers.ConfigManager;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
public class AirDrop {

    private final ConfigManager config = DreamKitPvP.getConfigManager();
    private ArmorStand armorStand;
    private BukkitRunnable effectTask;

    public void spawnDrop(Location location) {

        World world = location.getWorld();
        if (world == null) return;

        armorStand = world.spawn(location, ArmorStand.class);
        armorStand.setGravity(false);
        armorStand.setVisible(false);
        armorStand.setSmall(true);

        if (config.isUseAirDropHead()) {
            ItemStack skull = Utils.skullItem(config.getAirDropHeadUUID());
            armorStand.setHelmet(skull);
        } else {
            String materialName = config.getAirDropBlockID();
            armorStand.setHelmet(new ItemStack(Material.valueOf(materialName.toUpperCase())));
        }

        armorStand.setCustomName(Utils.color(config.getAirDropName()));
        armorStand.setCustomNameVisible(true);

        String soundFall = config.getAirDropFallSound();
        String soundEnd = config.getAirDropLevelUp();

        new BukkitRunnable() {
            @Override
            public void run() {
                Location currentLocation = armorStand.getLocation();

                if (currentLocation.getBlock().getType() != Material.AIR || armorStand.isDead()) {
                    try {
                        armorStand.getWorld().playSound(currentLocation, Sound.valueOf(soundEnd), 1.0f, 1.0f);
                    } catch (IllegalArgumentException e) {
                        Logger.warning("Config error sound <end-sound>: " + e);
                    }

                    knockbackEntities(currentLocation, 5, 2);
                    startLavaParticleEffect();

                    cleanup();
                    this.cancel();
                    return;
                }

                if (!soundFall.isEmpty()) {
                    try {
                        armorStand.getWorld().playSound(currentLocation, Sound.valueOf(soundFall), 1.0f, 1.0f);
                    } catch (IllegalArgumentException e) {
                        Logger.warning("Config error sound <fall-sound>: " + e);
                        cleanup();
                        this.cancel();
                        return;
                    }
                }

                double deltaX = 0.1;
                double deltaZ = 0.1;
                double deltaY = 0.6;

                Location newLocation = currentLocation.add(deltaX, -deltaY, deltaZ);
                newLocation.setYaw(armorStand.getLocation().getYaw() + 10);

                armorStand.getWorld().spigot().playEffect(newLocation, Effect.LARGE_SMOKE, 0, 0, 0.1f, 0.3f, 0.1f, 0.05f, 10, 64);
                armorStand.getWorld().spigot().playEffect(newLocation, Effect.FLAME, 0, 0, 0.1f, 0.3f, 0.1f, 0.05f, 5, 64);

                Utils.spawnParticle(newLocation, EnumParticle.FLAME);
                armorStand.teleport(newLocation);
            }
        }.runTaskTimer(DreamKitPvP.getInstance(), 0, 5);
    }

    private void startLavaParticleEffect() {
        effectTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (armorStand == null || armorStand.isDead() || armorStand.getLocation().getBlock().getType() == Material.AIR) {
                    this.cancel();
                    return;
                }

                Location location = armorStand.getLocation();
                World world = location.getWorld();
                if (world != null) {
                    world.spigot().playEffect(location, Effect.LAVA_POP, 0, 0, 0.5f, 0.5f, 0.5f, 0.05f, 10, 64);
                }
            }
        };
        effectTask.runTaskTimer(DreamKitPvP.getInstance(), 0, 20);
    }

    private void cleanup() {
        if (effectTask != null) {
            effectTask.cancel();
        }

        if (armorStand != null && !armorStand.isDead()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    armorStand.remove();
                }
            }.runTaskLater(DreamKitPvP.getInstance(), 100L);
        }

        armorStand = null;
        effectTask = null;
    }

    private void knockbackEntities(Location location, double radius, double strength) {
        List<Entity> entities = location.getWorld().getEntities();
        for (Entity entity : entities) {
            if (entity.getLocation().distance(location) <= radius) {
                Vector knockback = entity.getLocation().toVector().subtract(location.toVector()).normalize().multiply(strength);
                entity.setVelocity(knockback);
            }
        }
    }
}
