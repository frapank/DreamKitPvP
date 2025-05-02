package com.gourmet.dreamkitpvp;

import com.gourmet.dreamkitpvp.commands.*;
import com.gourmet.dreamkitpvp.events.cosmetic.DamagerIndicator;
import com.gourmet.dreamkitpvp.events.cosmetic.DeathMessageEvent;
import com.gourmet.dreamkitpvp.events.databases.BastardLevelEvent;
import com.gourmet.dreamkitpvp.events.databases.BastardTask;
import com.gourmet.dreamkitpvp.events.databases.JoinerDataEvent;
import com.gourmet.dreamkitpvp.events.databases.KillDataEvent;
import com.gourmet.dreamkitpvp.events.functional.*;
import com.gourmet.dreamkitpvp.events.pvp.*;
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
    @Getter private static BastardTask bastardTask;

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
        bastardTask = new BastardTask(databaseManager);

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
        getCommand("stats").setExecutor(new StatsCMD());

        List<Listener> events = Arrays.asList(
                new ArrowRemovalEvent(),
                new DeathMessageEvent(),
                new BastardLevelEvent(),
                new JoinerDataEvent(),
                new KillDataEvent(),
                new FoodLostEvent(),
                new DamageRegenEvent(),
                new DataBaseJoinEvent(),
                new RoadPvPEvent(),
                new LeaveEvent(),
                new InstantRespawnEvent(),
                new CooldownItems(),
                new CombactLogEvent(),
                new DropKillRemoveEvent(),
                new BowBoostEvent(),
                new BuildControllerEvent(),
                new AxeHitEvent(),
                new DespawnBlockEvent(),
                new DamagerIndicator(),
                new SpawnTeleportEvent());
        events.forEach(event -> {
            getServer().getPluginManager().registerEvents(event, this);
        });

    }

}
