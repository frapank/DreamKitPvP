package com.gourmet.dreamkitpvp.others;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public class ConfigManager {

    private FileConfiguration config = DreamKitPvP.getInstance().getConfig();

    private String cooldownItems;
    private String noPermission;
    private String reloadMessage;
    private double verticalMultiplier;
    private double horizontalMultiplier;
    private int despawnTime;

    public ConfigManager(){
        reloadMessages();
    }

    public void reloadMessages(){
        DreamKitPvP.getInstance().reloadConfig();
        config = DreamKitPvP.getInstance().getConfig();
        try {
            cooldownItems = config.getString("messages.cooldown");
            noPermission = config.getString("messages.no-perm");
            reloadMessage = config.getString("messages.reload");
            horizontalMultiplier = config.getDouble("horizontal-multiplier", 1.0D);
            verticalMultiplier = config.getDouble("vertical-multiplier", 1.0D);
            despawnTime = config.getInt("despawn-time");
        } catch (Exception e){
            Bukkit.getLogger().warning("Failed to load config: " + e);
        }
    }


}
