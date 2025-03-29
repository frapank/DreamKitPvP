package com.gourmet.dreamkitpvp.commands;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.ConfigManager;
import com.gourmet.dreamkitpvp.others.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;


public class StatsCMD implements CommandExecutor {

    private final FileConfiguration config = DreamKitPvP.getInstance().getConfig();
    private final ConfigManager configManager = DreamKitPvP.getConfigManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!(sender instanceof Player)){
            return false;
        }

        Player player = (Player) sender;

        if(!player.hasPermission("dreamkitpvp.stats")){
            player.sendMessage(Utils.color(configManager.getNoPermission()));
            return true;
        } else {
            player.sendMessage("Loreh non ce nessun messaggio nel config");
        }


        return true;

    }

}
