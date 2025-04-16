package com.gourmet.dreamkitpvp.commands;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.Utils;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SpawnSetCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(!(commandSender instanceof Player)){
            return false;
        }

        FileConfiguration config = DreamKitPvP.getInstance().getConfig();
        Player player = (Player) commandSender;

        if(!player.hasPermission("kitpvp.admin")){
            return false;
        }

        World world = player.getWorld();
        double x = player.getLocation().getX();
        double y = player.getLocation().getY();
        double z = player.getLocation().getZ();

        config.set("spawn.world", world.getName());
        config.set("spawn.x", x);
        config.set("spawn.y", y);
        config.set("spawn.z", z);
        DreamKitPvP.getInstance().saveConfig();

        DreamKitPvP.getConfigManager().reloadMessages();
        player.sendMessage(Utils.color("&eSpawn settato con successo"));

        return false;
    }
}
