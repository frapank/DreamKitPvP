package com.gourmet.dreamkitpvp;

import com.gourmet.dreamkitpvp.commands.BuildCMD;
import com.gourmet.dreamkitpvp.commands.ReloadCMD;
import com.gourmet.dreamkitpvp.commands.StatsCMD;
import com.gourmet.dreamkitpvp.events.*;
import com.gourmet.dreamkitpvp.others.ConfigManager;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.xml.bind.Unmarshaller;
import java.util.*;

public final class DreamKitPvP extends JavaPlugin {

    @Getter private static DreamKitPvP instance;
    @Getter private static HashSet<Player> buildModePlayer;
    @Getter private static ConfigManager configManager;

    @Override
    public void onEnable() {
        getLogger().info("DreamKitPvP Starting...");
        saveDefaultConfig();
        instance = this;
        buildModePlayer = new HashSet<>();
        configManager = new ConfigManager();

        initCommandsAndEvents();
        getLogger().info("DreamKitPvP Started!");
    }

    private void initCommandsAndEvents(){
        getCommand("build").setExecutor(new BuildCMD());
        getCommand("stats").setExecutor(new StatsCMD());
        getCommand("kit-reload").setExecutor(new ReloadCMD());

        List<Listener> events = Arrays.asList(new InstantRespawnEvent(),
                new DropEvent(),
                new HitEvent(),
                new PlaceBlockEvent(),
                new BreakBlockEvent(),
                new AxeHitEvent(),
                new DespawnBlockEvent(),
                new DamagerIndicator());
        events.forEach(event -> {getServer().getPluginManager().registerEvents(event, this);});

    }

}
