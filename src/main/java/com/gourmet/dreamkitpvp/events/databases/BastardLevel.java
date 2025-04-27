package com.gourmet.dreamkitpvp.events.databases;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.managers.DatabaseManager;
import com.gourmet.dreamkitpvp.others.managers.DatabaseManager.PlayerStats;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class BastardLevel implements Listener {

    private final DatabaseManager databaseManager = DreamKitPvP.getDatabaseManager();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {

        if(!databaseManager.getIsOnline()){
            return;
        }

        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        if (killer == null || killer.equals(victim)) return;

        double killerPower = calculateGearPower(killer);
        double victimPower = calculateGearPower(victim);

        if (killerPower > victimPower + 1) {
            int infamyGain = (int) Math.floor((killerPower - victimPower) / 2);
            if (infamyGain < 1) infamyGain = 1;

            PlayerStats stats = databaseManager.getStatistics(killer.getName());
            if (stats != null) {
                int newBastard = stats.bastard + infamyGain;
                databaseManager.updateStatistics(
                        killer.getName(),
                        stats.kills,
                        stats.deaths,
                        stats.kd,
                        stats.bounty,
                        stats.streak,
                        newBastard
                );
            }
        }
    }

    private double calculateGearPower(Player p) {
        double power = 0.0;

        ItemStack weapon = p.getItemInHand();
        if (weapon != null && weapon.getType() == Material.DIAMOND_SWORD) {
            int sharp = weapon.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
            int fire  = weapon.getEnchantmentLevel(Enchantment.FIRE_ASPECT);
            power += sharp * 1.5 + fire * 1.0;
        }

        for (ItemStack piece : p.getInventory().getArmorContents()) {
            if (piece != null) {
                int prot = piece.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
                power += prot * 1.2;
            }
        }

        return power;
    }

}
