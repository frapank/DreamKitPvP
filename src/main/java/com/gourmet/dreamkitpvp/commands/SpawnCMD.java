package com.gourmet.dreamkitpvp.commands;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SpawnCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(!(commandSender instanceof Player)){
            return false;
        }

        Player player = (Player) commandSender;

        if(DreamKitPvP.getCombatManager().isPlayerInCombat(player)){
            player.sendMessage(Utils.color("&cNon puoi sei in combact"));
            return true;
        }

        player.teleport(
                DreamKitPvP.getConfigManager().getSpawn()
        );

        player.sendMessage(Utils.color("Sei tornato allo spawn"));


        return false;
    }
}
