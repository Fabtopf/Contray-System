package de.Fabtopf.System.Utilities;

import de.Fabtopf.System.API.Config;
import de.Fabtopf.System.API.Connector;
import de.Fabtopf.System.API.Enum.MessageType;
import de.Fabtopf.System.API.Enum.ServerSetting;

import java.util.HashMap;

/**
 * Created by Fabi on 15.09.2017.
 */
public class Cache {

    public static boolean devmode;
    public static boolean configured;
    public static boolean shutdown;
    public static boolean online_mode;
    public static boolean usingBungeecord;
    public static boolean bungee_mode;

    public static String prefix;
    public static String mysql_host;
    public static String mysql_database;
    public static String mysql_username;
    public static String mysql_password;
    public static String mysql_prefix;

    public static int mysql_port;

    public static HashMap<MessageType, String> messages = new HashMap<MessageType, String>();
    public static HashMap<ServerSetting, String> serversettings = new HashMap<ServerSetting, String>();

    public static Config c_settings;
    public static Config c_mysql;
    public static Config c_messages;

    public static Connector mysql;

}
