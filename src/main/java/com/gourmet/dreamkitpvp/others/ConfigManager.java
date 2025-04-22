package com.gourmet.dreamkitpvp.others;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private String cantSpawnInCombat;
    private String teleportedToSpawn;
    private String cantWhileCombat;

    // Numbers
    private double verticalMultiplier;
    private double horizontalMultiplier;
    private int despawnTime;

    // Booleans
    private boolean removeUnenchanted;
    private boolean spawnEnabled;

    //Locations
    private Location spawn;

    //List
    private List<String> blockedCommands;

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
            loadStrings();

        } catch (Exception e) {
            Bukkit.getLogger().warning("[ConfigManager] Failed to load config: " + e.getMessage());
        }
    }

    private void loadLocations(){
        if (!getConfig().contains("spawn")) {
            spawnEnabled = false;
            return;
        }
        spawnEnabled = true;
        World world = Bukkit.getWorld(config.getString("spawn.world"));
        double x = config.getDouble("spawn.x");
        double y = config.getDouble("spawn.y");
        double z = config.getDouble("spawn.z");

        spawn = new Location(world, x, y, z);
    }

    private void loadMessages() {
        prefix = config.getString("messages.prefix", "[ErrorPrefix] ");
        combatMessage = getPrefixedMessage("messages.in-combat");
        outCombatMessage = getPrefixedMessage("messages.out-combat");
        cooldownItems = getPrefixedMessage("messages.cooldown");
        noPermission = getPrefixedMessage("messages.no-perm");
        reloadMessage = getPrefixedMessage("messages.reload");
        cantSpawnInCombat = getPrefixedMessage("messages.combat-spawn");
        teleportedToSpawn = getPrefixedMessage("messages.spawn-teleported");
        cantWhileCombat = getPrefixedMessage("messages.cant-while-combat");
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

    private void loadStrings() {
        blockedCommands = config.getStringList("blocked-commands");
    }

    private String getPrefixedMessage(String path){
        return prefix + config.getString(path, "Config Error");
    }

    public boolean shouldRemoveUnenchanted() {
        return removeUnenchanted;
    }

    public boolean isBannedDrop(Material material) {
        return bannedDrops.contains(material);
    }




}
