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

    public static long getTimeFromTimeStampString(String timestamp) {

        int day_pos = -1;
        int hour_pos = -1;
        int minute_pos = -1;
        int second_pos = -1;

        int seconds = 0;
        int minutes = 0;
        int hours = 0;
        int days = 0;

        timestamp = timestamp.toLowerCase();

        String charCheck = timestamp.replaceFirst("s", "").replaceFirst("m", "").replaceFirst("h", "").replaceFirst("d", "");
        if(charCheck.contains("s") || charCheck.contains("m") || charCheck.contains("h") || charCheck.contains("d")) {
            return -2;
        } else if(charCheck.equals("")) {
            return 0;
        } else {
            try {
                Integer.parseInt(charCheck);
            } catch(Exception e) {
                return -3;
            }
        }

        HashMap<Integer, Integer> numbers = new HashMap<Integer, Integer>();

        for(int i = 0; i < timestamp.getBytes().length; i++) {
            if(timestamp.charAt(i) == 's') second_pos = i;
            if(timestamp.charAt(i) == 'm') minute_pos = i;
            if(timestamp.charAt(i) == 'h') hour_pos = i;
            if(timestamp.charAt(i) == 'd') day_pos = i;
            if(timestamp.charAt(i) == '0' || timestamp.charAt(i) == '1' || timestamp.charAt(i) == '2' || timestamp.charAt(i) == '3' || timestamp.charAt(i) == '4' || timestamp.charAt(i) == '5' || timestamp.charAt(i) == '6' || timestamp.charAt(i) == '7' || timestamp.charAt(i) == '8' || timestamp.charAt(i) == '9')
                numbers.put(i, Integer.parseInt(Character.toString(timestamp.charAt(i))));
        }

        if(day_pos == -1 && hour_pos == -1 && minute_pos == -1 && second_pos == -1) {
            second_pos = timestamp.length();
        }

        if(second_pos != -1) {
            int pos = 0;
            for(int i = second_pos-1; numbers.containsKey(i); i--) {
                seconds += numbers.get(i) * Math.pow(10, pos);
                pos++;
            }
        }

        if(minute_pos != -1) {
            int pos = 0;
            for(int i = minute_pos-1; numbers.containsKey(i); i--) {
                minutes += numbers.get(i) * Math.pow(10, pos);
                pos++;
            }
        }

        if(hour_pos != -1) {
            int pos = 0;
            for(int i = hour_pos-1; numbers.containsKey(i); i--) {
                hours += numbers.get(i) * Math.pow(10, pos);
                pos++;
            }
        }

        if(day_pos != -1) {
            int pos = 0;
            for(int i = day_pos-1; numbers.containsKey(i); i--) {
                days += numbers.get(i) * Math.pow(10, pos);
                pos++;
            }
        }

        if(numbers.containsKey(timestamp.length() -1)) {
            int pos = 0;
            for(int i = timestamp.length()-1; numbers.containsKey(i); i--) {
                seconds += numbers.get(i) * Math.pow(10, pos);
                pos++;
            }
        }

        return new TimeManager(seconds + 1, minutes, hours, days).getUnformedSeconds();

    }

}
