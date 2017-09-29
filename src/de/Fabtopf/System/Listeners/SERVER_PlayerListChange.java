package de.Fabtopf.System.Listeners;

import de.Fabtopf.System.API.Manager.SpielerManager;
import de.Fabtopf.System.Utilities.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Fabi on 16.09.2017.
 */
public class SERVER_PlayerListChange implements Listener {

    public SERVER_PlayerListChange() {
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        SpielerManager.registerSpieler(e.getPlayer().getUniqueId().toString());
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent e) {
        SpielerManager.unregisterSpieler(e.getPlayer().getUniqueId().toString());
    }

}
