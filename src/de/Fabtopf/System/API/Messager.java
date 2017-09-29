package de.Fabtopf.System.API;

import de.Fabtopf.System.API.Enum.ErrorType;
import de.Fabtopf.System.API.Enum.MessageType;
import de.Fabtopf.System.Utilities.Cache;
import de.Fabtopf.System.Utilities.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

/**
 * Created by Fabi on 15.09.2017.
 */
public class Messager {

    public static void sendMessage(MessageType type, Player p, HashMap<String, String> converts) {

        String message = Cache.messages.get(type);

        if (converts != null) {
            for (String convert : converts.keySet()) {
                message = message.replace(convert, converts.get(convert));
            }
        }

        message = ChatColor.translateAlternateColorCodes('&', message);

        if(p != null) {
            p.sendMessage(message);
        } else {
            Bukkit.getConsoleSender().sendMessage(message);
        }

    }

    public static void sendDelayedMessage(MessageType[] types, Player p, HashMap<String, String> converts) {

        String[] messages = new String[types.length];
        int d = 0;

        if(types != null) {
            for(int i = 0; i < types.length; i++) {
                messages[i] = Cache.messages.get(types[i]);
            }
        }

        if (converts != null) {
            for(int i = 0; i < messages.length; i++) {
                for (String convert : converts.keySet()) {
                    messages[i] = messages[i].replace(convert, converts.get(convert));
                }
            }
        }

        for(String message : messages) {
            new BukkitRunnable() {

                @Override
                public void run() {
                    if(p != null) {
                        p.sendMessage(message);
                    } else {
                        Bukkit.getConsoleSender().sendMessage(message);
                    }
                }

            }.runTaskLater(Main.getInstance(), 5 * d);

            d++;
        }

    }

    public static void sendError(ErrorType type) {
        Bukkit.getConsoleSender().sendMessage("§8[§c" + Main.getInstance().getDescription().getName() + "§8] §7> §4Error: §c" + type.getMessage());
    }

}
