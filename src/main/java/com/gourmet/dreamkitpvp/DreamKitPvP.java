package com.gourmet.dreamkitpvp;

import com.gourmet.dreamkitpvp.commands.BuildCMD;
import com.gourmet.dreamkitpvp.commands.ReloadCMD;
import com.gourmet.dreamkitpvp.commands.StatsCMD;
import com.gourmet.dreamkitpvp.events.*;
import com.gourmet.dreamkitpvp.others.ConfigManager;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;

public final class DreamKitPvP extends JavaPlugin {

    @Getter private static DreamKitPvP instance;
    @Getter private static HashSet<Player> buildModePlayer;
    @Getter private static ConfigManager configManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        buildModePlayer = new HashSet<>();
        configManager = new ConfigManager();

        getCommand("build").setExecutor(new BuildCMD());
        getCommand("stats").setExecutor(new StatsCMD());
        getCommand("kit-reload").setExecutor(new ReloadCMD());
        getServer().getPluginManager().registerEvents(new DropEvent(), this);
        getServer().getPluginManager().registerEvents(new HitEvent(), this);
        getServer().getPluginManager().registerEvents(new PlaceBlockEvent(), this);
        getServer().getPluginManager().registerEvents(new BreakBlockEvent(), this);
        getServer().getPluginManager().registerEvents(new DespawnBlockEvent(), this);
    }

}
