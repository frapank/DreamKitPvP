package com.gourmet.dreamkitpvp.commands;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.Logger;
import com.gourmet.dreamkitpvp.others.managers.DatabaseManager;
import com.gourmet.dreamkitpvp.others.managers.DatabaseManager.PlayerData;
import com.gourmet.dreamkitpvp.others.managers.DatabaseManager.PlayerStats;
import com.mysql.jdbc.log.Log;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class StatsCMD implements CommandExecutor {

    private final DatabaseManager databaseManager = DreamKitPvP.getDatabaseManager();
    private final HashMap<Player, PlayerData> playersData = databaseManager.getPlayersData();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String targetName;
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Solo i giocatori possono usare /stats senza argomenti.");
                return true;
            }
            targetName = ((Player) sender).getName();
        } else if (args.length == 1) {
            targetName = args[0];
        } else {
            sender.sendMessage(ChatColor.RED + "Uso corretto: /stats [nome]");
            return true;
        }

        PlayerStats stats = null;
        int fragments = -1;

        Player online = Bukkit.getPlayerExact(targetName);
        if (online != null && playersData.containsKey(online)) {
            PlayerData pd = playersData.get(online);
            stats = pd.stats;
            fragments = pd.fragments;
        } else {
            stats = databaseManager.getStatistics(targetName);
            fragments = databaseManager.getFragmentByName(targetName);
        }

        if (stats == null || fragments < 0) {
            sender.sendMessage(ChatColor.YELLOW + "Nessun giocatore trovato con il nome: " + targetName);
            return true;
        }

        String msg = ChatColor.GOLD + "--- Statistiche di " + targetName + " ---\n"
                + ChatColor.GREEN + "Frammenti: " + ChatColor.WHITE + fragments + "\n"
                + ChatColor.GREEN + "Kills: " + ChatColor.WHITE + stats.kills + "\n"
                + ChatColor.GREEN + "Morti: " + ChatColor.WHITE + stats.deaths + "\n"
                + ChatColor.GREEN + "KD: " + ChatColor.WHITE + String.format("%.2f", stats.kd) + "\n"
                + ChatColor.GREEN + "Taglia: " + ChatColor.WHITE + stats.bounty;

        sender.sendMessage(msg);
        return true;
    }
}
