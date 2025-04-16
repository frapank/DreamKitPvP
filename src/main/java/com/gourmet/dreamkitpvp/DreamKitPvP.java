package com.gourmet.dreamkitpvp;

import com.gourmet.dreamkitpvp.commands.BuildCMD;
import com.gourmet.dreamkitpvp.commands.ReloadCMD;
import com.gourmet.dreamkitpvp.commands.SpawnCMD;
import com.gourmet.dreamkitpvp.commands.SpawnSetCMD;
import com.gourmet.dreamkitpvp.events.*;
import com.gourmet.dreamkitpvp.others.CombatManager;
import com.gourmet.dreamkitpvp.others.ConfigManager;
import com.gourmet.dreamkitpvp.others.CooldownManager;
import com.gourmet.dreamkitpvp.others.KitPlaceHolders;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public final class DreamKitPvP extends JavaPlugin {

    @Getter
    private static DreamKitPvP instance;
    @Getter
    private static HashSet<Player> buildModePlayer;
    @Getter
    private static ConfigManager configManager;
    @Getter
    private static CombatManager combatManager;
    @Getter
    private static CooldownManager cooldownManager;

    @Override
    public void onEnable() {
        getLogger().info("DreamKitPvP Starting...");

        saveDefaultConfig();
        instance = this;
        combatManager = new CombatManager();
        cooldownManager = new CooldownManager();
        buildModePlayer = new HashSet<>();
        configManager = new ConfigManager();

        initPlaceHolderApi();
        initCommandsAndEvents();
        getLogger().info("DreamKitPvP Started!");
    }

    private void initPlaceHolderApi(){
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new KitPlaceHolders().register();
        }
    }

    private void initCommandsAndEvents() {
        getCommand("build").setExecutor(new BuildCMD());
        getCommand("kit-reload").setExecutor(new ReloadCMD());
        getCommand("setspawn").setExecutor(new SpawnSetCMD());
        getCommand("spawn").setExecutor(new SpawnCMD());

        List<Listener> events = Arrays.asList(
                new LeaveEvent(),
                new InstantRespawnEvent(),
                new CooldownItems(),
                new CombactLogEvent(),
                new DropEvent(),
                new HitEvent(),
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
