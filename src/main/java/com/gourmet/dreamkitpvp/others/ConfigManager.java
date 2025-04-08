package com.gourmet.dreamkitpvp.others;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;
import java.util.Set;

@Getter
public class ConfigManager {

    private final Set<Material> bannedDrops = new HashSet<>();
    private FileConfiguration config = DreamKitPvP.getInstance().getConfig();
    private String cooldownItems;
    private String noPermission;
    private String reloadMessage;
    private double verticalMultiplier;
    private double horizontalMultiplier;
    private int despawnTime;
    private boolean removeUnenchanted;

    public ConfigManager() {
        reloadMessages();
    }

    public void reloadMessages() {
        DreamKitPvP.getInstance().reloadConfig();
        config = DreamKitPvP.getInstance().getConfig();
        try {
            cooldownItems = config.getString("messages.cooldown");
            noPermission = config.getString("messages.no-perm");
            reloadMessage = config.getString("messages.reload");
            horizontalMultiplier = config.getDouble("horizontal-multiplier", 1.0D);
            verticalMultiplier = config.getDouble("vertical-multiplier", 1.0D);
            despawnTime = config.getInt("despawn-time", 5);
            removeUnenchanted = config.getBoolean("remove-unenchanted", true);
            initDrops();
        } catch (Exception e) {
            Bukkit.getLogger().warning("Failed to load config: " + e);
            despawnTime = 5;
        }
    }

    public boolean isBannedDrop(Material material) {
        return bannedDrops.contains(material);
    }

    private void initDrops() {
        bannedDrops.clear();
        for (String itemName : config.getStringList("banned-drops")) {
            try {
                bannedDrops.add(Material.valueOf(itemName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                Bukkit.getLogger().warning("Materiale non riconosciuto : " + itemName);
            }
        }
    }

    public boolean shouldRemoveUnenchanted() {
        return removeUnenchanted;
    }


}
