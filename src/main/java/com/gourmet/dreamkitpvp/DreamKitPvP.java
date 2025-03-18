package com.gourmet.dreamkitpvp;

import org.bukkit.plugin.java.JavaPlugin;

/*

Ecco le vostre task, nei file che vi ho creato vi troverete approssivamente come dovete fare le cose.
Potete modificare o creare nuovi file ma mantenete sempre questo flow. GL
Task:

 - Respawn istantaneo.
 - Drop di oggetti TRANNE kit base (no item non incantati, frecce ecc.)
 - Asce che danneggiano di più l'armatura. (non devono fare più danno ai giocatori)
 - BowBoost per l'arco.
 - No autocombat.
 - Timer gapple, combat e enderpearl.
 - Sistema di combat.
 - Blocco comandi in combat.
 - Modalità Build.
 - Timer obs e ragnatele.


 */


public final class DreamKitPvP extends JavaPlugin {

    public static DreamKitPvP instance;

    @Override
    public void onEnable() {
        instance = this;
    }

}
