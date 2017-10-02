package de.Fabtopf.System.API;

import de.Fabtopf.System.Utilities.Main;
import de.Fabtopf.System.Utilities.MySQL.MySQL_Utils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Created by Fabi on 24.09.2017.
 */
public class Spieler {

    private String uuid;
    private String muteReason;

    private boolean frozen;
    private boolean muted;

    private int databaseID;

    private long mutetime;

    public Spieler(String uuid) {
        this.uuid = uuid;

        new BukkitRunnable() {
            @Override
            public void run() {

                databaseID = MySQL_Utils.getPlayerID(Bukkit.getOfflinePlayer(UUID.fromString(uuid)));
                mutetime = MySQL_Utils.getPlayerMuteTime(databaseID);
                muted = MySQL_Utils.getPlayerMuted(databaseID);
                muteReason = MySQL_Utils.getMuteReason(databaseID);

            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public void joinServer() {

    }

    public void leaveServer() {

    }

    public void setMuted(boolean muted) { this.muted = muted; }
    public void setMuteTime(long time) { this.mutetime = time; }
    public void setFrozen(boolean frozen) { this.frozen = frozen; }

    public boolean getMuted() { return muted; }
    public boolean getFrozen() { return frozen; }
    public long getMuteTime() { return mutetime; }
    public int getDatabaseID() { return databaseID; }

}
