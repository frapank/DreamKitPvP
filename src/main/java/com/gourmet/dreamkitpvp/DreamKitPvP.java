package com.gourmet.dreamkitpvp;

import com.gourmet.dreamkitpvp.commands.BuildCMD;
import com.gourmet.dreamkitpvp.commands.ReloadCMD;
import com.gourmet.dreamkitpvp.commands.SpawnCMD;
import com.gourmet.dreamkitpvp.commands.SpawnSetCMD;
import com.gourmet.dreamkitpvp.events.cosmetic.DamagerIndicator;
import com.gourmet.dreamkitpvp.events.functional.*;
import com.gourmet.dreamkitpvp.events.pvp.CooldownItems;
import com.gourmet.dreamkitpvp.events.pvp.AxeHitEvent;
import com.gourmet.dreamkitpvp.events.pvp.CombactLogEvent;
import com.gourmet.dreamkitpvp.events.pvp.BowBoostEvent;
import com.gourmet.dreamkitpvp.events.pvp.RoadPvPEvent;
import com.gourmet.dreamkitpvp.others.*;
import com.gourmet.dreamkitpvp.others.managers.CombatManager;
import com.gourmet.dreamkitpvp.others.managers.ConfigManager;
import com.gourmet.dreamkitpvp.others.managers.CooldownManager;
import com.gourmet.dreamkitpvp.others.managers.DatabaseManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public final class DreamKitPvP extends JavaPlugin {

    @Getter private static DreamKitPvP instance;
    @Getter private static HashSet<Player> buildModePlayer;
    @Getter private static ConfigManager configManager;
    @Getter private static CombatManager combatManager;
    @Getter private static CooldownManager cooldownManager;
    @Getter private static DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        Logger.watch("&fLoading...");

        saveDefaultConfig();
        setupInstance();
        initPlaceHolderApi();
        initCommandsAndEvents();

        Logger.info("&aDreamKitPvP Started!");
    }

    private void setupInstance(){
        instance = this;
        combatManager = new CombatManager();
        cooldownManager = new CooldownManager();
        buildModePlayer = new HashSet<>();
        configManager = new ConfigManager();
        databaseManager = new DatabaseManager();

        databaseManager.setupDatabase();
        databaseManager.initDatabase();
    }

    private void initPlaceHolderApi(){
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new KitPlaceHolders().register();
        } else {
            Logger.warning("PlaceHolder API not found");
        }
    }

    private void initCommandsAndEvents() {
        getCommand("build").setExecutor(new BuildCMD());
        getCommand("kit-reload").setExecutor(new ReloadCMD());
        getCommand("setspawn").setExecutor(new SpawnSetCMD());
        getCommand("spawn").setExecutor(new SpawnCMD());

        List<Listener> events = Arrays.asList(
                new DataBaseJoinEvent(),
                new RoadPvPEvent(),
                new LeaveEvent(),
                new InstantRespawnEvent(),
                new CooldownItems(),
                new CombactLogEvent(),
                new RemoveDropEvent(),
                new BowBoostEvent(),
                new PlaceBlockEvent(),
                new BreakBlockEvent(),
                new AxeHitEvent(),
                new DespawnBlockEvent(),
                new DamagerIndicator(),
                new SpawnJoinEvent());
        events.forEach(event -> {
            getServer().getPluginManager().registerEvents(event, this);
        });

    }

}
