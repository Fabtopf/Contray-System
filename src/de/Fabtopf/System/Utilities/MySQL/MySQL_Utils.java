package de.Fabtopf.System.Utilities.MySQL;

import de.Fabtopf.System.API.Connector;
import de.Fabtopf.System.API.UUID_Fetcher;
import de.Fabtopf.System.Utilities.Cache;
import org.bukkit.OfflinePlayer;

/**
 * Created by Fabi on 15.09.2017.
 */
public class MySQL_Utils {

    public static void createTables() {
        Connector mysql = Cache.mysql;

        mysql.update("CREATE TABLE IF NOT EXISTS ContraySY_ServerSettings (Einstellung VARCHAR(64) UNIQUE, Wert TEXT)");
        mysql.update("CREATE TABLE IF NOT EXISTS ContraySY_" + Cache.mysql_prefix + "_Modules (Name VARCHAR(64) UNIQUE, Enabled BOOLEAN, Devmode BOOLEAN)");
        mysql.update("CREATE TABLE IF NOT EXISTS ContraySY_Players (ID MEDIUMINT NOT NULL PRIMARY KEY AUTO_INCREMENT, Name VARCHAR(64), UUID_Online VARCHAR(64) UNIQUE, UUID_Offline VARCHAR(64) UNIQUE, IPs TEXT)");
        // mysql.update("CREATE TABLE IF NOT EXISTS ContraySY_" + Cache.mysql_prefix + "_PlayerSettings (ID MEDIUMINT UNIQUE, )");
        mysql.update("CREATE TABLE IF NOT EXISTS ContraySY_PlayerSettings (ID MEDIUMINT UNIQUE, Banned BOOLEAN, BanReason TEXT, BanTime LONG, UnbanApplyAllowed BOOLEAN)");

    }

    /*
     *  Module - Management
     */

    public static void registerModule(String name) { Cache.mysql.update("INSERT INTO ContraySY_" + Cache.mysql_prefix + "_Modules (Name, Enabled, Devmode) VALUES ('" + name + "', 0, 0)"); }
    public static void updateModuleEnabled(String name, boolean enabled) { Cache.mysql.update("UPDATE ContraySY_" + Cache.mysql_prefix + "_Modules SET Enabled='" + (enabled ? 1 : 0) + "' WHERE Name='" + name + "'"); }
    public static void updateModuleDevmode(String name, boolean devmode) { Cache.mysql.update("UPDATE ContraySY_" + Cache.mysql_prefix + "_Modules SET Devmode='" + (devmode ? 1 : 0) + "' WHERE Name='" + name + "'"); }
    public static boolean moduleExists(String name) { return Cache.mysql.getString("ContraySY_" + Cache.mysql_prefix + "_Modules", "Name", name, "Name") != null; }
    public static boolean getModuleEnabled(String name) { return Cache.mysql.getBoolean("ContraySY_" + Cache.mysql_prefix + "_Modules", "Name", name, "Enabled"); }
    public static boolean getModuleDevmode(String name) { return Cache.mysql.getBoolean("ContraySY_" + Cache.mysql_prefix + "_Modules", "Name", name, "Devmode"); }

    /*
     *  Player - Management
     */

    public static void registerPlayer(OfflinePlayer p) {
        if(Cache.online_mode) Cache.mysql.update("INSERT INTO ContraySY_Players (Name, UUID_Online, IPs) VALUES ('" + p.getName() + "','" + p.getUniqueId().toString() + "','')");
        if(!Cache.online_mode) Cache.mysql.update("INSERT INTO ContraySY_Players (Name, UUID_Online, UUID_Offline, IPs) VALUES ('" + p.getName() + "','" + UUID_Fetcher.getUUID(p.getName()).toString() + "','" + p.getUniqueId().toString() + "','')");
    }
    public static void registerLocalPlayerSettings(int id) { Cache.mysql.update("INSERT INTO ContraySY_" + Cache.mysql_prefix + "_PlayerSettings (ID) VALUES ('" + id + "')"); }
    public static void registerGlobalPlayerSettings(int id) { Cache.mysql.update("INSERT INTO ContraySY_PlayerSettings (ID, Banned, BanReason, BanTime, UnbanApplyAllowed) VALUES ('" + id + "','0','Nicht gebannt!','0','1')"); }
    public static boolean getPlayerExists(int id) { return Cache.mysql.getString("ContraySY_Players", "ID", id, "ID") != null; }
    public static boolean getPlayerExists(OfflinePlayer p) { return Cache.mysql.getString("ContraySY_Players", (Cache.online_mode ? "UUID_Online" : "UUID_Offline"), p.getUniqueId().toString(), (Cache.online_mode ? "UUID_Online" : "UUID_Offline")) != null; }
    public static int getPlayerID(OfflinePlayer p) { return Cache.mysql.getInt("ContraySY_Players", (Cache.online_mode ? "UUID_Online" : "UUID_Offline"), p.getUniqueId().toString(), "ID"); }

    /*
     *  BanModule
     */

    public static void banPlayer(int id, String reason, long time) {
        Cache.mysql.update("UPDATE ContraySY_PlayerSettings SET Banned='1' WHERE ID='" + id + "'");
        Cache.mysql.update("UPDATE ContraySY_PlayerSettings SET BanReason='" + reason + "' WHERE ID='" + id + "'");
        Cache.mysql.update("UPDATE ContraySY_PlayerSettings SET BanTime='" + time + "' WHERE ID='" + id + "'");
    }
    public static void unbanPlayer(int id) {
        Cache.mysql.update("UPDATE ContraySY_PlayerSettings SET Banned='0' WHERE ID='" + id + "'");
        Cache.mysql.update("UPDATE ContraySY_PlayerSettings SET BanReason='Nicht gebannt!' WHERE ID='" + id + "'");
        Cache.mysql.update("UPDATE ContraySY_PlayerSettings SET BanTime='0' WHERE ID='" + id + "'");
        Cache.mysql.update("UPDATE ContraySY_PlayerSettings SET UnbanApplyAllowed='1'");
    }
    public static void updatePlayerUnbanApplyAllowed(int id, boolean allowUnbanApply) { Cache.mysql.update("UPDATE ContraySY_PlayerSettings SET UnbanApplyAllowed='" + (allowUnbanApply ? 1 : 0) + "'"); }
    public static boolean getPlayerBanned(int id) { return Cache.mysql.getBoolean("ContraySY_PlayerSettings", "ID", id, "Banned"); }
    public static String getBanReason(int id) { return Cache.mysql.getString("ContraySY_PlayerSettings", "ID", id, "BanReason"); }
    public static boolean getPlayerUnbanApplyAllowed(int id) { return Cache.mysql.getBoolean("ContraySY_PlayerSettings", "ID", id, "UnbanApplyAllowed"); }
    public static long getPlayerBanTime(int id) { return Cache.mysql.getLong("ContraySY_PlayerSettings", "ID", id, "BanTime"); }

    /*
     *  Server-Einstellungen
     */

    public static void registerServerSetting(String einstellung, String wert) { Cache.mysql.update("INSERT INTO ContraySY_ServerSettings (Einstellung, Wert) VALUES ('" + einstellung + "','" + wert + "')"); }
    public static boolean serverSettingExists(String einstellung) { return Cache.mysql.getString("ContraySY_ServerSettings", "Einstellung", einstellung, "Einstellung") != null; }
    public static void updateServerSetting(String einstellung, String wert) { Cache.mysql.update("UPDATE ContraySY_ServerSettings SET Wert='" + wert + "' WHERE Einstellung='" + einstellung + "'"); }
    public static String getServerSetting(String einstellung) { return Cache.mysql.getString("ContraySY_ServerSettings", "Einstellung", einstellung, "Wert"); }

}
