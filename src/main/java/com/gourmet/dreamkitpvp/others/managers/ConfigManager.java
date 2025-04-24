package com.gourmet.dreamkitpvp.others.managers;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.Logger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfigManager {

    final Set<Material> bannedDrops = new HashSet<>();
    FileConfiguration config = DreamKitPvP.getInstance().getConfig();

    // Strings
    String prefix;
    String cooldownItems;
    String noPermission;
    String reloadMessage;
    String combatMessage;
    String outCombatMessage;
    String cantSpawnInCombat;
    String teleportedToSpawn;
    String cantWhileCombat;

    // Numbers
    double verticalMultiplier;
    double horizontalMultiplier;
    int despawnTime;

    // Booleans
    boolean removeUnenchanted;
    boolean spawnEnabled;

    //Locations
    Location spawn;

    //List
    List<String> blockedCommands;

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
            Logger.warning("Failed to load config: &c" + e.getMessage());
        }
    }

    private void loadLocations(){
        if (!getConfig().contains("spawn")) {
            spawnEnabled = false;
            Logger.warning("Spawn not defined");
            return;
        }
        spawnEnabled = true;
        World world = Bukkit.getWorld(config.getString("spawn.world"));
        double x = config.getDouble("spawn.x");
        double y = config.getDouble("spawn.y");
        double z = config.getDouble("spawn.z");
        float yaw = (float) config.getDouble("spawn.yaw");
        float pitch = (float) config.getDouble("spawn.pitch");

        spawn = new Location(world, x, y, z, yaw, pitch);
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
                Logger.warning("Unknown material: &c" + itemName);
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
