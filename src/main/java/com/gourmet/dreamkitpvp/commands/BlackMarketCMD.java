package com.gourmet.dreamkitpvp.commands;

import com.gourmet.dreamkitpvp.custom.blackmarket.BlackMarketGui;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class BlackMarketCMD implements CommandExecutor {

    private static final String PERMISSION = "dreamkitpvp.blackmarket";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length >= 1) {
                if (!player.hasPermission(PERMISSION)) {
                    player.sendMessage(ChatColor.RED + "Non hai il permesso per aprire la Black Market ad altri.");
                    return true;
                }
                Player target = Bukkit.getPlayerExact(args[0]);
                if (target == null || !target.isOnline()) {
                    player.sendMessage(ChatColor.RED + "Giocatore '" + args[0] + "' non trovato o non online.");
                    return true;
                }
                BlackMarketGui.openBlackMarket(target);
                player.sendMessage(ChatColor.GREEN + "Hai aperto la Black Market per " + target.getName() + ".");
                return true;
            }

            if (!player.hasPermission(PERMISSION)) {
                player.sendMessage(ChatColor.RED + "Non hai il permesso di usare questo comando.");
                return true;
            }
            BlackMarketGui.openBlackMarket(player);
            return true;
        }

        if (sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender) {
            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED + "Uso: /blackmarket <giocatore>");
                return true;
            }
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null || !target.isOnline()) {
                sender.sendMessage(ChatColor.RED + "Giocatore '" + args[0] + "' non trovato o non online.");
                return true;
            }
            BlackMarketGui.openBlackMarket(target);
            sender.sendMessage(ChatColor.GREEN + "Black Market aperta per " + target.getName() + ".");
            return true;
        }

        sender.sendMessage(ChatColor.RED + "Solo giocatori con permesso o la console possono eseguire questo comando.");
        return true;
    }

}
