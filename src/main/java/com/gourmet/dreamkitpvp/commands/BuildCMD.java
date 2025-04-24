package com.gourmet.dreamkitpvp.commands;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.managers.ConfigManager;
import com.gourmet.dreamkitpvp.others.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class BuildCMD implements CommandExecutor {

    private final HashSet<Player> buildModePlayers = DreamKitPvP.getBuildModePlayer();
    private final ConfigManager configManager = DreamKitPvP.getConfigManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("dreamkitpvp.admin")) {
            player.sendMessage(Utils.color(configManager.getNoPermission()));
            return true;
        }

        toggleBuildMode(player);

        return true;

    }

    private void toggleBuildMode(Player player) {
        if (buildModePlayers.contains(player)) {
            Utils.sendActionBar(player, Utils.color("&cRemove build mode"));
            buildModePlayers.remove(player);
        } else {
            Utils.sendActionBar(player, Utils.color("&eAdded to build mode"));
            buildModePlayers.add(player);
        }
    }
}
