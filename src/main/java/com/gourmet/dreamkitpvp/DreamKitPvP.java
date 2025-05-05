package com.gourmet.dreamkitpvp;

import com.gourmet.dreamkitpvp.commands.*;
import com.gourmet.dreamkitpvp.custom.events.RockLaunchEvent;
import com.gourmet.dreamkitpvp.events.airdrop.AirDropClaimEvent;
import com.gourmet.dreamkitpvp.events.blackmarket.GuiEvent;
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
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
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
    @Getter private static WorldEditPlugin we;
    @Getter private static WorldGuardPlugin wg;


    @Override
    public void onEnable() {
        Logger.watch("&fLoading...");

        saveDefaultConfig();
        initWorldEdit();
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

        Utils.removeAllArmorStand();
    }

    private void initWorldEdit(){
        we = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");
        wg = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
        if (we == null || wg == null) {
            Logger.warning("WorldEdit and WorldGuard not found");
            getServer().getPluginManager().disablePlugin(this);
        }
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
        getCommand("blk").setExecutor(new BlackMarketCMD());
        getCommand("akab").setExecutor(new AkabCommand());

        List<Listener> events = Arrays.asList(
                new GuiEvent(),
                new AirDropClaimEvent(),
                new RockLaunchEvent(),
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
