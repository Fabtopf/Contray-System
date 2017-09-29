package de.Fabtopf.System.Utilities;

import de.Fabtopf.System.API.Config;
import de.Fabtopf.System.API.Connector;
import de.Fabtopf.System.API.Enum.MessageType;
import de.Fabtopf.System.API.Enum.ServerSetting;
import de.Fabtopf.System.API.Manager.ModuleManager;
import de.Fabtopf.System.API.Manager.SpielerManager;
import de.Fabtopf.System.API.Messager;
import de.Fabtopf.System.API.Module;
import de.Fabtopf.System.Commands.COMMAND_Ban;
import de.Fabtopf.System.Commands.COMMAND_TempBan;
import de.Fabtopf.System.Commands.COMMAND_Unban;
import de.Fabtopf.System.Listeners.BANMODULE_PlayerListChange;
import de.Fabtopf.System.Listeners.SERVER_PlayerListChange;
import de.Fabtopf.System.Utilities.MySQL.MySQL_Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

/**
 * Created by Fabi on 15.09.2017.
 */
public class Functions {

    /*
     *  Plugin-State Management
     */

    public static void enablePlugin() {

        HashMap<String, String> converts = new HashMap<String, String>();
        converts.put("%PLUGIN_NAME%", Main.getInstance().getDescription().getName());

        if(!Cache.shutdown) setupConfigs();
        if(!Cache.shutdown && Cache.configured) setupMySQL();

        new BukkitRunnable() {
            @Override
            public void run() {
                if(!Cache.shutdown && Cache.configured) loadCache();
                if(!Cache.shutdown && Cache.configured) registerModules();
                if(!Cache.shutdown && Cache.configured) registerValues();
            }
        }.runTaskLater(Main.getInstance(), 2);

        if(!Cache.configured) {
            Messager.sendMessage(MessageType.System_PluginNotConfigured, null, converts);
            shutdownPlugin();
            return;
        }

        if(Cache.shutdown) {
            Messager.sendMessage(MessageType.System_PluginShutdown, null, converts);
            shutdownPlugin();
            return;
        }

        Messager.sendMessage(MessageType.System_PluginEnabled, null, converts);

    }

    public static void disablePlugin() {

        HashMap<String, String> converts = new HashMap<String, String>();
        converts.put("%PLUGIN_NAME%", Main.getInstance().getDescription().getName());

        Messager.sendMessage(MessageType.System_PluginDisabled, null, converts);

    }

    public static void shutdownPlugin() {
        Bukkit.getPluginManager().disablePlugin(Main.getInstance());
        return;
    }

    /*
     *  Registering Plugin-Features
     */

    private static void setupConfigs() {

        Config settings = new Config("plugins/ContrayPlugins/System", "Settings.yml");
        Config mysql = new Config("plugins/ContrayPlugins/System", "MySQL.yml");
        Config messages = new Config("plugins/ContrayPlugins/System", "Messages.yml");

        Cache.c_settings = settings;
        Cache.c_mysql = mysql;
        Cache.c_messages = messages;

        mysql.create("password", "pass");
        mysql.create("username", "root");
        mysql.create("database", "ContraySystem");
        mysql.create("port", 3306);
        mysql.create("host", "localhost");

        Cache.mysql_database = mysql.getString("database");
        Cache.mysql_host = mysql.getString("host");
        Cache.mysql_password = mysql.getString("password");
        Cache.mysql_username = mysql.getString("username");
        Cache.mysql_port = mysql.getInt("port");

        for(MessageType type : MessageType.values()) {
            messages.create(type.getPath(), type.getDefaultMessage());
            Cache.messages.put(type, messages.getString(type.getPath()));
        }

        settings.create("Configured", false);
        settings.create("Devmode", false);
        settings.create("Prefix", "&8[&cContray-System&8] &c");
        settings.create("DatenbankPrefix", "default");
        settings.create("BungeeCord.use", false);
        settings.create("BungeeCord.online_mode", true);

        Cache.prefix = settings.getString("Prefix");
        Cache.devmode = settings.getBoolean("Devmode");
        Cache.configured = settings.getBoolean("Configured");
        Cache.mysql_prefix = settings.getString("DatenbankPrefix");
        Cache.usingBungeecord = settings.getBoolean("BungeeCord.use");
        Cache.bungee_mode = settings.getBoolean("BungeeCord.online_mode");

    }

    private static void setupMySQL() {

        String host = Cache.mysql_host;
        String database = Cache.mysql_database;
        String username = Cache.mysql_username;
        String password = Cache.mysql_password;
        int port = Cache.mysql_port;

        Connector mysql = new Connector(host, port, database, username, password);
        Cache.mysql = mysql;
        mysql.connect();

        if(Cache.shutdown) return;
        MySQL_Utils.createTables();

    }

    public static void loadCache() {
        if(Bukkit.getServer().getOnlineMode()) Cache.online_mode = true;
        if(!Bukkit.getServer().getOnlineMode() && !Cache.usingBungeecord) Cache.online_mode = false;
        if(!Bukkit.getServer().getOnlineMode() && Cache.usingBungeecord && Cache.bungee_mode) Cache.online_mode = true;
        if(!Bukkit.getServer().getOnlineMode() && Cache.usingBungeecord && !Cache.bungee_mode) Cache.online_mode = false;

        for(ServerSetting setting : ServerSetting.values()) {
            if(MySQL_Utils.serverSettingExists(setting.getPath())) {
                Cache.serversettings.put(setting, MySQL_Utils.getServerSetting(setting.getPath()));
            } else {
                Cache.serversettings.put(setting, setting.getDefaultMessage());
                MySQL_Utils.registerServerSetting(setting.getPath(), setting.getDefaultMessage());
            }
        }
    }

    public static void registerModules() {

        new SERVER_PlayerListChange();

        ModuleManager.registerModule("BanModule");
        new BukkitRunnable() {
            @Override
            public void run() {
                Module mod = ModuleManager.getModule("BanModule");
                mod.setDevmode(false);

                mod.registerCommand("ban", "Command to ban a player", "contray.system.banmodule.ban", "/ban <Player> <Reason>", new COMMAND_Ban());
                mod.registerCommand("unban", "Command to unban a player", "contray.system.banmodule.unban", "/unban <Player>", new COMMAND_Unban());
                mod.registerCommand("tempban", "Command to tempban a player", "contray.system.banmodule.tempban", "/tempban <Player> <Timestamp> <Reason>", new COMMAND_TempBan());
                mod.registerListener("PlayerListChange", new BANMODULE_PlayerListChange());
            }
        }.runTaskLater(Main.getInstance(), 6);

    }

    private static void registerValues() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            SpielerManager.registerSpieler(p.getUniqueId().toString());
        }
    }

}
