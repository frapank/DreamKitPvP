package com.gourmet.dreamkitpvp.commands;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.ConfigManager;
import com.gourmet.dreamkitpvp.others.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class ReloadCMD implements CommandExecutor {

    private final ConfigManager configManager = DreamKitPvP.getConfigManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if (!player.hasPermission("dreamkitpvp.reload")) {
            player.sendMessage(Utils.color(configManager.getNoPermission()));
            return true;
        }

        player.sendMessage(Utils.color(configManager.getReloadMessage()));
        configManager.reloadMessages();

        return true;

    }

}
