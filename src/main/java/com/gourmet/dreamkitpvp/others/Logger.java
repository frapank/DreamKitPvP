package com.gourmet.dreamkitpvp.others;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

@UtilityClass
public class Logger {

    private final String prefix = Utils.color("&d[DreamKitPvP]: ");

    public void info(String text){
        sendText("&f" + text);
    }

    public void warning(String text){
        sendText("&c" + text);
    }

    public void watch(String text){
        sendText("&k" + text);
    }

    private void sendText(String text){
        Bukkit.getConsoleSender().sendMessage(Utils.color(prefix + text));
    }

}
