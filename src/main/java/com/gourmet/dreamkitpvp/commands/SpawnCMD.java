package com.gourmet.dreamkitpvp.commands;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCMD implements CommandExecutor {

    private final ConfigManager configManager = DreamKitPvP.getConfigManager();
    private final double spawnX = configManager.getSpawnX();
    private final double spawnY = configManager.getSpawnY();
    private final double spawnZ = configManager.getSpawnZ();
    private final String messaggiotp = configManager.spawnTeleportMessage;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {

        Player player = (Player) sender;
        Location loc = new Location(Bukkit.getWorld("world"), spawnX, spawnY, spawnZ, 0, 0);
        player.teleport(loc);
        player.sendMessage(messaggiotp);

        return true;
    }
}
