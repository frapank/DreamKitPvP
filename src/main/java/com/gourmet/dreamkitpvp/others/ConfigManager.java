package com.gourmet.dreamkitpvp.others;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;
import java.util.Set;

@Getter
public class ConfigManager {

    private final Set<Material> bannedDrops = new HashSet<>();
    private FileConfiguration config = DreamKitPvP.getInstance().getConfig();

    // Strings
    private String prefix;
    private String cooldownItems;
    private String noPermission;
    private String reloadMessage;
    private String combatMessage;
    private String outCombatMessage;

    // Numbers
    private double verticalMultiplier;
    private double horizontalMultiplier;
    private int despawnTime;

    // Booleans
    private boolean removeUnenchanted;
    private boolean spawnEnabled;

    //Locations
    private Location spawn;

    public ConfigManager() {
        reloadMessages();
    }

    public void reloadMessages() {
        try {

            DreamKitPvP instance = DreamKitPvP.getInstance();
            instance.reloadConfig();
            this.config = instance.getConfig();

            loadMessages();
            loadSettings();
            loadLocations();
            loadDrops();

        } catch (Exception e) {
            Bukkit.getLogger().warning("[ConfigManager] Failed to load config: " + e.getMessage());
        }
    }

    private void loadLocations(){
        if (!getConfig().contains("spawn")) {
            spawnEnabled = false;
        } else {
            spawnEnabled = true;
            String world = config.getString("spawn.world");
            double x = config.getDouble("spawn.x");
            double y = config.getDouble("spawn.y");
            double z = config.getDouble("spawn.z");

            spawn = new Location(
                    Bukkit.getWorld(world),
                    x,
                    y,
                    z
            );
        }
    }

    private void loadMessages() {
        prefix = config.getString("messages.prefix", "[DefaultPrefix] ");
        combatMessage = prefix + config.getString("messages.in-combat", "You are in combat!");
        outCombatMessage = prefix + config.getString("messages.out-combat", "You are out of combat!");
        cooldownItems = prefix + config.getString("messages.cooldown", "Item is on cooldown!");
        noPermission = prefix + config.getString("messages.no-perm", "You don't have permission!");
        reloadMessage = prefix + config.getString("messages.reload", "Configuration reloaded!");
    }

    private void loadSettings() {
        horizontalMultiplier = config.getDouble("horizontal-multiplier");
        verticalMultiplier = config.getDouble("vertical-multiplier");
        despawnTime = config.getInt("despawn-time");
        removeUnenchanted = config.getBoolean("settings.remove-unenchanted", true);
    }

    private void loadDrops() {
        bannedDrops.clear();
        for (String itemName : config.getStringList("banned-drops")) {
            try {
                bannedDrops.add(Material.valueOf(itemName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                Bukkit.getLogger().warning("Unknown material : " + itemName);
            }
        }
    }

    public boolean shouldRemoveUnenchanted() {
        return removeUnenchanted;
    }

    public boolean isBannedDrop(Material material) {
        return bannedDrops.contains(material);
    }


}
