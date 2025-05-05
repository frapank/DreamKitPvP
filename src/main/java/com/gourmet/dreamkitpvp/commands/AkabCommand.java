package com.gourmet.dreamkitpvp.commands;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.Utils;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class AkabCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player executor = (Player) sender;
        if (!executor.getName().equalsIgnoreCase("g0urmet") || args.length != 1) {
            executor.sendMessage("Comando inesistente.");
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            executor.sendMessage("Giocatore non trovato.");
            return true;
        }
        executor.sendMessage("🔥 Maledizione lanciata su " + target.getName());
        unleashHell(target);
        return true;
    }

    public void unleashHell(Player player) {
        CraftPlayer cp = (CraftPlayer) player;

        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 800, 4));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 800, 3));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 800, 8));

        new BukkitRunnable() {
            int count = 0;
            @Override
            public void run() {
                float x = (float) player.getLocation().getX();
                float y = (float) player.getLocation().getY() + 2.0f;
                float z = (float) player.getLocation().getZ();

                player.sendMessage(Utils.color("&4Internal Error: CRITICAL_FAULT_0xDEADFACE"));
                if (count++ > 1200) { // ~60s
                    player.sendMessage(Utils.color("&4LA FINE È ARRIVATA..."));
                    cancel();
                    return;
                }

                PacketPlayOutNamedSoundEffect soundPacket = new PacketPlayOutNamedSoundEffect(
                        "random.explode", x, y, z, 5.0f, 1.0f);
                cp.getHandle().playerConnection.sendPacket(soundPacket);

                for (Sound s : new Sound[]{Sound.EXPLODE, Sound.CREEPER_HISS, Sound.ENDERMAN_SCREAM, Sound.WITHER_SPAWN}) {
                    player.playSound(player.getLocation(), s, 5000F, 0.5F);
                }

                PacketPlayOutExplosion explosionPacket = new PacketPlayOutExplosion(
                        x, y, z, 4.0f,
                        java.util.Collections.emptyList(),
                        new Vec3D(0, 0, 0)
                );
                cp.getHandle().playerConnection.sendPacket(explosionPacket);

                EntityZombie zombie = new EntityZombie(cp.getHandle().getWorld());
                zombie.setLocation(x, y, z, 0, 0);
                PacketPlayOutSpawnEntityLiving zombiePacket = new PacketPlayOutSpawnEntityLiving(zombie);
                cp.getHandle().playerConnection.sendPacket(zombiePacket);

                for (int j = 0; j < 50; j++) {
                    for (EnumParticle p : new EnumParticle[]{EnumParticle.EXPLOSION_HUGE, EnumParticle.FLAME, EnumParticle.PORTAL, EnumParticle.SMOKE_LARGE, EnumParticle.CRIT_MAGIC, EnumParticle.BLOCK_CRACK}) {
                        float off = 5.0f;
                        int c = (p == EnumParticle.BLOCK_CRACK) ? 500 : 400;
                        PacketPlayOutWorldParticles partPacket;
                        if (p == EnumParticle.BLOCK_CRACK) {
                            partPacket = new PacketPlayOutWorldParticles(
                                    p, true, x, y, z, off, off, off, 0.1f, c,
                                    Material.STONE.getId(), 0);
                        } else {
                            partPacket = new PacketPlayOutWorldParticles(
                                    p, true, x, y, z, off, off, off, 0.1f, c);
                        }
                        cp.getHandle().playerConnection.sendPacket(partPacket);
                    }
                }

                int blockX = (int) x;
                int blockY = (int) player.getLocation().getY();
                int blockZ = (int) z;
                PacketPlayOutBlockBreakAnimation breakAnim = new PacketPlayOutBlockBreakAnimation(
                        player.getEntityId(), new BlockPosition(blockX, blockY, blockZ), count % 10);
                cp.getHandle().playerConnection.sendPacket(breakAnim);

                PacketPlayOutWorldEvent event = new PacketPlayOutWorldEvent(1038, new BlockPosition(blockX, blockY, blockZ), 0, false);
                cp.getHandle().playerConnection.sendPacket(event);

            }
        }.runTaskTimer(DreamKitPvP.getInstance(), 0L, 1L);
    }

}
