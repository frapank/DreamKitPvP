package com.gourmet.dreamkitpvp.commands;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.managers.CombatManager;
import com.gourmet.dreamkitpvp.others.managers.ConfigManager;
import com.gourmet.dreamkitpvp.others.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCMD implements CommandExecutor {

    private final ConfigManager configManager = DreamKitPvP.getConfigManager();
    private final CombatManager combatManager = DreamKitPvP.getCombatManager();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(!(commandSender instanceof Player)){
            return false;
        }

        Player player = (Player) commandSender;

        if(combatManager.isPlayerInCombat(player)){
            player.sendMessage(Utils.color(configManager.getCantSpawnInCombat()));
            return true;
        }

        if(!DreamKitPvP.getConfigManager().isSpawnEnabled()){
            return false;
        }

        player.teleport(
                DreamKitPvP.getConfigManager().getSpawn()
        );

        player.sendMessage(Utils.color(configManager.getTeleportedToSpawn()));


        return false;
    }
}
