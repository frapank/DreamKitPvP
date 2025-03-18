package com.gourmet.dreamkitpvp;

import com.gourmet.dreamkitpvp.events.*;
import org.bukkit.plugin.java.JavaPlugin;


/*

Ecco le vostre task, nei file che vi ho creato vi troverete approssivamente come dovete fare le cose.
Potete modificare o creare nuovi file ma mantenete sempre questo flow.
Task:

 - Respawn istantaneo.
 - Combact log ( Blocco comandi etc... ).
 - Modalità Build.
 - Ragnatele.


 */


public final class DreamKitPvP extends JavaPlugin {

    public static DreamKitPvP instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new DropEvent(), this);
        getServer().getPluginManager().registerEvents(new HitEvent(), this);
        getServer().getPluginManager().registerEvents(new PlaceBlockEvent(), this);
        getServer().getPluginManager().registerEvents(new QuitEvent(), this);
        getServer().getPluginManager().registerEvents(new RespawnEvent(), this);
        getServer().getPluginManager().registerEvents(new UseEvent(), this);
    }

}
