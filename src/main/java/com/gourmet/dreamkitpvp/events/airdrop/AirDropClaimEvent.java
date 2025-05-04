package com.gourmet.dreamkitpvp.events.airdrop;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.Logger;
import com.gourmet.dreamkitpvp.others.Utils;
import com.gourmet.dreamkitpvp.others.managers.ConfigManager;
import com.gourmet.dreamkitpvp.others.managers.DatabaseManager;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

import javax.xml.crypto.Data;
import java.util.Random;

public class AirDropClaimEvent implements Listener {

    private final ConfigManager config = DreamKitPvP.getConfigManager();
    private final DatabaseManager databaseManager = DreamKitPvP.getDatabaseManager();

    @EventHandler
    public void armorStandEvent(PlayerArmorStandManipulateEvent event) {
        ArmorStand armorStand = event.getRightClicked();
        if(armorStand.getCustomName() != null && armorStand.getCustomName().equals(Utils.color(config.getAirDropName()))){
            armorStand.remove();
            playExplosionEffect(armorStand.getLocation());
            executeCommand(event.getPlayer());
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onArmorStandInteract(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof ArmorStand && event.getDamager() instanceof Player) {
            ArmorStand armorStand = (ArmorStand) event.getEntity();
            Player player = (Player) event.getDamager();
            if (armorStand.getCustomName() != null && armorStand.getCustomName().equals(Utils.color(config.getAirDropName()))){
                String soundClaim = config.getAirDropExplode();
                try{
                    armorStand.getWorld().playSound(armorStand.getLocation(), Sound.valueOf(soundClaim), 1.0f, 1.0f);
                } catch (IllegalArgumentException e){
                    Logger.warning("Config error sound <claim-sound> :" + e);
                }

                armorStand.remove();
                executeCommand(player);
                playExplosionEffect(armorStand.getLocation());
            }
        }
    }


    private void playExplosionEffect(Location location) {


        try {
            EnumParticle paricella = EnumParticle.valueOf(config.getClaimEffect());
            float x = (float) location.getX();
            float y = (float) location.getY();
            float z = (float) location.getZ();
            float offsetX = 0;
            float offsetY = 0;
            float offsetZ = 0;
            float speed = 1;
            int count = 100;


            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(
                    paricella,
                    true,
                    x, y, z,
                    offsetX, offsetY, offsetZ,
                    speed,
                    count
            );

            for (Player player : Bukkit.getOnlinePlayers()) {
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        } catch (Exception e){
            Logger.warning("Error in claim-effect config:" + e);
        }
    }

    private void executeCommand(Player player){
        Random random = new Random();
        int randomNumber = random.nextInt(10) + 1;
        if(randomNumber <= 3){

            try {
                int currentFragments = databaseManager.getFragmentByName(player.getName());
                int newFragments = currentFragments++;

                databaseManager.updateFragments(player.getName(), newFragments);
                databaseManager.getPlayersData().get(player).fragments = newFragments;
            } catch (Exception e){
                Logger.warning("Cant give fragments to player: " + e);
            }


            Utils.sendActionBar(player, Utils.color("&9Hai ottenuto 1 frammento"));
            return;
        }

        String str = "eco give %player% 500";
        str = str.replace("%player%", player.getName());
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), str);
        Utils.sendActionBar(player, Utils.color("&9Hai ottenuto un pò di soldi"));
    }
}
