package de.Fabtopf.System.API;

import de.Fabtopf.System.API.Enum.MessageType;
import de.Fabtopf.System.API.Enum.ServerSetting;
import de.Fabtopf.System.API.Manager.TimeManager;
import de.Fabtopf.System.Utilities.Cache;
import de.Fabtopf.System.Utilities.MySQL.MySQL_Utils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Fabi on 15.09.2017.
 */
public class Converter {

    public static List<?> hashMapValuesToList(HashMap<?, ?> map) {
        List<Object> list = new ArrayList<>();

        for(Object obj : map.values()) {
            list.add(obj);
        }

        return list;
    }

    public static List<?> collectionToList(Collection<?> col) {
        List<Object> list = new ArrayList<>();

        for(Object obj : col) {
            list.add(obj);
        }

        return list;
    }

    public static String getBanScreen(int databaseID) {

        long bantime = MySQL_Utils.getPlayerBanTime(databaseID);
        String timestamp = Cache.messages.get(MessageType.BanModule_BanScreen_TimeStamp).replace("%DAYS_LONG%", Cache.messages.get(MessageType.TimeManager_Days_long)).replace("%DAYS_SHORT%", Cache.messages.get(MessageType.TimeManager_Days_short)).replace("%HOURS_LONG%", Cache.messages.get(MessageType.TimeManager_Hours_long)).replace("%HOURS_SHORT%", Cache.messages.get(MessageType.TimeManager_Hours_short)).replace("%MINUTES_LONG%", Cache.messages.get(MessageType.TimeManager_Minutes_long)).replace("%MINUTES_SHORT%", Cache.messages.get(MessageType.TimeManager_Minutes_short)).replace("%SECONDS_LONG%", Cache.messages.get(MessageType.TimeManager_Seconds_long)).replace("%SECONDS_SHORT%", Cache.messages.get(MessageType.TimeManager_Seconds_short));
        if(bantime != -1) {
            TimeManager time = new TimeManager((bantime - System.currentTimeMillis()) / 1000);
            timestamp = timestamp.replace("%SECONDS%", Integer.toString(time.getFormedSeconds())).replace("%MINUTES%", Integer.toString(time.getFormedMinutes())).replace("%HOURS%", Integer.toString(time.getFormedHours())).replace("%DAYS%", Integer.toString(time.getFormedDays()));
        }
        String banscreen = Cache.messages.get(MessageType.BanModule_BanScreen).replace("%PREFIX%", Cache.prefix).replace("%TIMETYPE%", (bantime == -1 ? Cache.messages.get(MessageType.TimeManager_perm) : Cache.messages.get(MessageType.TimeManager_temp))).replace("%OUTSTANDING_TIME%", (bantime == -1 ? Cache.messages.get(MessageType.TimeManager_perm) : timestamp)).replace("%REASON%", MySQL_Utils.getBanReason(databaseID)).replace("%UNBAN_APPLY%", (MySQL_Utils.getPlayerUnbanApplyAllowed(databaseID) ? Cache.messages.get(MessageType.System_Allowed) : Cache.messages.get(MessageType.System_Denied))).replace("%WEBSITE%", Cache.serversettings.get(ServerSetting.INFO_Website)).replace("%TEAMSPEAK%", Cache.serversettings.get(ServerSetting.INFO_Teamspeak));
        banscreen = ChatColor.translateAlternateColorCodes('&', banscreen);

        return banscreen;

    }

    public static String getBanScreen(int databaseID, long bantime, String reason) {

        String timestamp = Cache.messages.get(MessageType.BanModule_BanScreen_TimeStamp).replace("%DAYS_LONG%", Cache.messages.get(MessageType.TimeManager_Days_long)).replace("%DAYS_SHORT%", Cache.messages.get(MessageType.TimeManager_Days_short)).replace("%HOURS_LONG%", Cache.messages.get(MessageType.TimeManager_Hours_long)).replace("%HOURS_SHORT%", Cache.messages.get(MessageType.TimeManager_Hours_short)).replace("%MINUTES_LONG%", Cache.messages.get(MessageType.TimeManager_Minutes_long)).replace("%MINUTES_SHORT%", Cache.messages.get(MessageType.TimeManager_Minutes_short)).replace("%SECONDS_LONG%", Cache.messages.get(MessageType.TimeManager_Seconds_long)).replace("%SECONDS_SHORT%", Cache.messages.get(MessageType.TimeManager_Seconds_short));
        if(bantime != -1) {
            TimeManager time = new TimeManager((bantime - System.currentTimeMillis()) / 1000);
            timestamp = timestamp.replace("%SECONDS%", Integer.toString(time.getFormedSeconds())).replace("%MINUTES%", Integer.toString(time.getFormedMinutes())).replace("%HOURS%", Integer.toString(time.getFormedHours())).replace("%DAYS%", Integer.toString(time.getFormedDays()));
        }
        String banscreen = Cache.messages.get(MessageType.BanModule_BanScreen).replace("%PREFIX%", Cache.prefix).replace("%TIMETYPE%", (bantime == -1 ? Cache.messages.get(MessageType.TimeManager_perm) : Cache.messages.get(MessageType.TimeManager_temp))).replace("%OUTSTANDING_TIME%", (bantime == -1 ? Cache.messages.get(MessageType.TimeManager_perm) : timestamp)).replace("%REASON%", reason).replace("%UNBAN_APPLY%", (MySQL_Utils.getPlayerUnbanApplyAllowed(databaseID) ? Cache.messages.get(MessageType.System_Allowed) : Cache.messages.get(MessageType.System_Denied))).replace("%WEBSITE%", Cache.serversettings.get(ServerSetting.INFO_Website)).replace("%TEAMSPEAK%", Cache.serversettings.get(ServerSetting.INFO_Teamspeak));
        banscreen = ChatColor.translateAlternateColorCodes('&', banscreen);

        return banscreen;

    }

}
