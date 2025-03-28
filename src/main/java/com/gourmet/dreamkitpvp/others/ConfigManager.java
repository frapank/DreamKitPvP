package com.gourmet.dreamkitpvp.others;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public class ConfigManager {

    private final FileConfiguration config = DreamKitPvP.getInstance().getConfig();

    private String cooldownItems;
    private String noPermission;

    public ConfigManager(){
        reloadMessages();
    }

    public void reloadMessages(){
        DreamKitPvP.getInstance().reloadConfig();
        try {
            cooldownItems = config.getString("messages.cooldown");
            noPermission = config.getString("messages.no-perm");
        } catch (Exception e){
            Bukkit.getLogger().warning("Failed to load config: " + e);
        }
    }


}
