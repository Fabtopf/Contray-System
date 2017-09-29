package de.Fabtopf.System.Listeners;

import de.Fabtopf.System.API.Converter;
import de.Fabtopf.System.API.Manager.ModuleManager;
import de.Fabtopf.System.API.Module;
import de.Fabtopf.System.Utilities.Cache;
import de.Fabtopf.System.Utilities.MySQL.MySQL_Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

/**
 * Created by Fabi on 16.09.2017.
 */
public class BANMODULE_PlayerListChange implements Listener {

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent e) {
        String name = e.getName();
        OfflinePlayer op = Bukkit.getOfflinePlayer(name);

        Module mod = ModuleManager.getModule("BanModule");

        if(mod.isEnabled() && (!mod.isDevmode() || (mod.isDevmode() && Cache.devmode))) {
            if (MySQL_Utils.getPlayerExists(op)) {
                int playerId = MySQL_Utils.getPlayerID(op);

                if (MySQL_Utils.getPlayerBanned(playerId)) {
                    long time = MySQL_Utils.getPlayerBanTime(playerId);
                    if(time == -1 || time > System.currentTimeMillis()) {
                        e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Converter.getBanScreen(playerId));
                        return;
                    } else {
                        MySQL_Utils.unbanPlayer(playerId);
                        return;
                    }
                }
            }
        }
    }

}
