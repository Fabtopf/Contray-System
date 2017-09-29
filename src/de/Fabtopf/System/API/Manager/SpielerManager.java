package de.Fabtopf.System.API.Manager;

import de.Fabtopf.System.API.Converter;
import de.Fabtopf.System.API.Spieler;
import de.Fabtopf.System.Utilities.Main;
import de.Fabtopf.System.Utilities.MySQL.MySQL_Utils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by Fabi on 24.09.2017.
 */
public class SpielerManager {

    private static HashMap<String, Spieler> spieler = new HashMap<String, Spieler>();

    public static void registerSpieler(String uuid) {
        if(!spieler.containsKey(uuid)) {
            if (MySQL_Utils.getPlayerExists(Bukkit.getOfflinePlayer(UUID.fromString(uuid)))) {
                spieler.put(uuid, new Spieler(uuid));
                spieler.get(uuid).joinServer();
            } else {
                MySQL_Utils.registerPlayer(Bukkit.getOfflinePlayer(UUID.fromString(uuid)));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        MySQL_Utils.registerGlobalPlayerSettings(MySQL_Utils.getPlayerID(Bukkit.getOfflinePlayer(UUID.fromString(uuid))));
                    }
                }.runTaskLater(Main.getInstance(), 2);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        spieler.put(uuid, new Spieler(uuid));
                        spieler.get(uuid).joinServer();
                    }
                }.runTaskLater(Main.getInstance(), 5);
            }
        }
    }

    public static void unregisterSpieler(String uuid) {
        if(spieler.containsKey(uuid)) {
            spieler.get(uuid).leaveServer();
            spieler.remove(uuid);
        }
    }

    public static Spieler getSpieler(String uuid) {
        if(spieler.containsKey(uuid)) {
            return spieler.get(uuid);
        } else {
            return null;
        }
    }

    public static List<Spieler> getSpielerListe() {
        return (List<Spieler>) Converter.collectionToList(spieler.values());
    }

}
