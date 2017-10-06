package de.Fabtopf.System.Listeners;

import de.Fabtopf.System.API.Enum.MessageType;
import de.Fabtopf.System.API.Manager.ModuleManager;
import de.Fabtopf.System.API.Manager.SpielerManager;
import de.Fabtopf.System.API.Messager;
import de.Fabtopf.System.API.Module;
import de.Fabtopf.System.API.Spieler;
import de.Fabtopf.System.Utilities.Cache;
import de.Fabtopf.System.Utilities.Main;
import de.Fabtopf.System.Utilities.MySQL.MySQL_Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;

/**
 * Created by Fabi on 29.09.2017.
 */
public class MUTEMODULE_PlayerChat implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {

        Module mod = ModuleManager.getModule("MuteModule");

        if(mod.isEnabled() && (!mod.isDevmode() || (mod.isDevmode() && Cache.devmode))) {

            Player p = e.getPlayer();
            OfflinePlayer op = Bukkit.getOfflinePlayer(p.getUniqueId());
            Spieler s = SpielerManager.getSpieler(p.getUniqueId().toString());

            if(s.getMuted() && (s.getMuteTime() == -1 || s.getMuteTime() > System.currentTimeMillis())) {

                if(p.hasPermission("contray.system.mutemodule.exempt")) {
                    s.setMuted(false);
                    s.setMuteTime(0);
                    MySQL_Utils.unmutePlayer(s.getDatabaseID());
                    return;
                }

                HashMap<String, String> converts = new HashMap<String, String>();
                converts.put("%PLUGIN_NAME%", Main.getInstance().getDescription().getName());

                e.setCancelled(true);
                Messager.sendMessage(MessageType.MuteModule_MuteInfo_CannotSendMessage, p, converts);
            } else if(s.getMuted() && s.getMuteTime() < System.currentTimeMillis()) {
                MySQL_Utils.unmutePlayer(s.getDatabaseID());
                s.setMuteTime(0);
                s.setMuted(false);
            }

        }

    }

}
