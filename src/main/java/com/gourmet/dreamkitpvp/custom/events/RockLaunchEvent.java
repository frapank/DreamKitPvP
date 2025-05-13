package com.gourmet.dreamkitpvp.custom.events;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.Utils;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class RockLaunchEvent implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (!event.getAction().toString().contains("RIGHT_CLICK") ||
                player.getItemInHand().getType() != Material.IRON_INGOT) {
            return;
        }

        if(Utils.isInSafeZone(player)){
            Utils.sendActionBar(player, Utils.color("&cNon puoi qui!"));
            return;
        }

        event.setCancelled(true);

        Location start = player.getEyeLocation();
        start.setY(start.getY() - 0.5);
        Vector direction = start.getDirection().normalize().multiply(1.1);
        World world = player.getWorld();

        ArmorStand shuriken = (ArmorStand) world.spawnEntity(start, EntityType.ARMOR_STAND);
        shuriken.setVisible(false);
        shuriken.setGravity(false);
        shuriken.setMarker(false);
        shuriken.setSmall(true);
        shuriken.setHelmet(new ItemStack(Material.STONE_BUTTON));

        new BukkitRunnable() {
            final Vector velocity = direction.clone();
            int ticks = 0;

            @Override
            public void run() {
                // Duration manager
                if (ticks++ > 90 || shuriken.isDead()) {
                    shuriken.remove();
                    cancel();
                    return;
                }

                // Straight line
                Location loc = shuriken.getLocation().add(velocity);
                shuriken.teleport(loc);

                // Rotation
                double yawRad = Math.toRadians((ticks * 20) % 360);
                shuriken.setHeadPose(new EulerAngle(0, yawRad, 0));

                // Interrupt when item touch something
                if (loc.getBlock().getType().isSolid()) {
                    world.playSound(loc, Sound.ITEM_BREAK, 1f, 1f);
                    shuriken.remove();
                    cancel();
                    return;
                }

                // Damage player on hit
                for (Player target : world.getPlayers()) {
                    if (target.equals(player)) continue;
                    if (target.getLocation().distance(loc) < 1.5) {
                        target.damage(4.0, player);
                        world.playSound(target.getLocation(), Sound.HURT_FLESH, 1f, 1f);
                        Utils.spawnParticle(target.getLocation(), EnumParticle.EXPLOSION_LARGE);
                        shuriken.remove();
                        cancel();
                        return;
                    }
                }
            }
        }.runTaskTimer(DreamKitPvP.getInstance(), 0, 1);
    }
}
