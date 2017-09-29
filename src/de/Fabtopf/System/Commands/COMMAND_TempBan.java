package de.Fabtopf.System.Commands;

import de.Fabtopf.System.API.Converter;
import de.Fabtopf.System.API.Enum.MessageType;
import de.Fabtopf.System.API.Manager.ModuleManager;
import de.Fabtopf.System.API.Manager.PermissionManager;
import de.Fabtopf.System.API.Manager.TimeManager;
import de.Fabtopf.System.API.Messager;
import de.Fabtopf.System.API.ModCommand;
import de.Fabtopf.System.API.Module;
import de.Fabtopf.System.Utilities.Cache;
import de.Fabtopf.System.Utilities.Main;
import de.Fabtopf.System.Utilities.MySQL.MySQL_Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Fabi on 24.09.2017.
 */
public class COMMAND_TempBan implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Module mod = ModuleManager.getModule("BanModule");
        ModCommand cmd = mod.getCommand("tempban");

        HashMap<String, String> converts = new HashMap<String, String>();
        converts.put("%PLUGIN_NAME%", Main.getInstance().getDescription().getName());
        converts.put("%COMMAND_USAGE%", cmd.getUsage());
        if(args.length > 0) converts.put("%BANNED_PLAYER%", args[0]);

        if(mod.isEnabled() && (!mod.isDevmode() || (mod.isDevmode() && Cache.devmode))) {

            Player p = null;
            if(sender instanceof Player) { p = (Player) sender; }

            if((p != null && PermissionManager.check(p, cmd.getPermission(), true)) || p == null) {
                if(args.length >= 3) {
                    if(MySQL_Utils.getPlayerExists(Bukkit.getOfflinePlayer(args[0]))) {
                        int playerId = MySQL_Utils.getPlayerID(Bukkit.getOfflinePlayer(args[0]));
                        if(!MySQL_Utils.getPlayerBanned(playerId)) {
                            String reason = "";
                            for(int i = 2; i < args.length; i++) {
                                reason = reason + " " + args[i];
                            }
                            reason = ChatColor.translateAlternateColorCodes('&', reason.replaceFirst(" ", ""));

                            int day_pos = -1;
                            int hour_pos = -1;
                            int minute_pos = -1;
                            int second_pos = -1;

                            int seconds = 0;
                            int minutes = 0;
                            int hours = 0;
                            int days = 0;

                            String charCheck = args[1].replaceFirst("s", "").replaceFirst("m", "").replaceFirst("h", "").replaceFirst("d", "");
                            if(charCheck.contains("s") || charCheck.contains("m") || charCheck.contains("h") || charCheck.contains("d")) {
                                Messager.sendMessage(MessageType.BanModule_InvaliedTimeStamp_1, p, converts);
                                return true;
                            } else {
                                try {
                                    Integer.parseInt(charCheck);
                                } catch(Exception e) {
                                    Messager.sendMessage(MessageType.BanModule_InvaliedTimeStamp_2, p, converts);
                                    return true;
                                }
                            }

                            HashMap<Integer, Integer> numbers = new HashMap<Integer, Integer>();

                            for(int i = 0; i < args[1].getBytes().length; i++) {
                                if(args[1].charAt(i) == 's') second_pos = i;
                                if(args[1].charAt(i) == 'm') minute_pos = i;
                                if(args[1].charAt(i) == 'h') hour_pos = i;
                                if(args[1].charAt(i) == 'd') day_pos = i;
                                if(args[1].charAt(i) == '0' || args[1].charAt(i) == '1' || args[1].charAt(i) == '2' || args[1].charAt(i) == '3' || args[1].charAt(i) == '4' || args[1].charAt(i) == '5' || args[1].charAt(i) == '6' || args[1].charAt(i) == '7' || args[1].charAt(i) == '8' || args[1].charAt(i) == '9')
                                    numbers.put(i, Integer.parseInt(Character.toString(args[1].charAt(i))));
                            }

                            if(day_pos == -1 && hour_pos == -1 && minute_pos == -1 && second_pos == -1) {
                                second_pos = args[1].length();
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

                            long bantime = new TimeManager(seconds + 1, minutes, hours, days).getUnformedSeconds();

                            if(bantime == 0) {
                                Messager.sendMessage(MessageType.BanModule_InvaliedTimeStamp_3, p, converts);
                                return true;
                            }

                            final String r = reason;
                            final long btime = (bantime * 1000) + System.currentTimeMillis();

                            MySQL_Utils.banPlayer(playerId, reason, btime);

                            Bukkit.getPlayer(args[0]).kickPlayer(Converter.getBanScreen(playerId, btime, r));
                            return true;
                        } else {
                            Messager.sendMessage(MessageType.BanModule_AlreadyBanned, p, converts);
                            return true;
                        }
                    } else {
                        Messager.sendMessage(MessageType.Database_PlayerDoesntExist, p, converts);
                        return true;
                    }
                } else {
                    Messager.sendMessage(MessageType.Command_ShowUsage, p, converts);
                    return true;
                }
            } else {
                Messager.sendMessage(MessageType.Command_NoPerm, p, null);
                return true;
            }

        } else {
            if(sender instanceof Player) {
                Messager.sendMessage(MessageType.Command_Disabled, (Player) sender, converts);
                return true;
            } else {
                Messager.sendMessage(MessageType.Command_Disabled, null, converts);
                return true;
            }
        }

    }

}
