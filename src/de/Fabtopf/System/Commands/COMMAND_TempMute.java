package de.Fabtopf.System.Commands;

import de.Fabtopf.System.API.*;
import de.Fabtopf.System.API.Enum.MessageType;
import de.Fabtopf.System.API.Manager.ModuleManager;
import de.Fabtopf.System.API.Manager.PermissionManager;
import de.Fabtopf.System.API.Manager.SpielerManager;
import de.Fabtopf.System.API.Manager.TimeManager;
import de.Fabtopf.System.Utilities.Cache;
import de.Fabtopf.System.Utilities.Main;
import de.Fabtopf.System.Utilities.MySQL.MySQL_Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.HashMap;

/**
 * Created by Fabi on 02.10.2017.
 */
public class COMMAND_TempMute implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Module mod = ModuleManager.getModule("MuteModule");
        ModCommand cmd = mod.getCommand("tempmute");

        HashMap<String, String> converts = new HashMap<String, String>();
        converts.put("%PLUGIN_NAME%", Main.getInstance().getDescription().getName());
        converts.put("%COMMAND_USAGE%", cmd.getUsage());
        if(args.length > 0) converts.put("%MUTED_PLAYER%", args[0]);

        if(mod.isEnabled() && (!mod.isDevmode() || (mod.isDevmode() && Cache.devmode))) {

            Player p = null;
            if(sender instanceof Player) { p = (Player) sender; }

            if((p != null && PermissionManager.check(p, cmd.getPermission(), true)) || p == null) {
                if(args.length >= 3) {
                    if(MySQL_Utils.getPlayerExists(Bukkit.getOfflinePlayer(args[0]))) {
                        if((Bukkit.getOfflinePlayer(args[0]).isOnline() && !PermissionManager.check(Bukkit.getPlayer(args[0]), "contray.system.banmodule.exempt", true)) || (!Bukkit.getOfflinePlayer(args[0]).isOnline() &&
                                !Bukkit.getOfflinePlayer(args[0]).isOp() && (Bukkit.getPluginManager().getPlugin("PermissionsEx") != null && Bukkit.getPluginManager().getPlugin("PermissionsEx").isEnabled() && !PermissionsEx.getUser(args[0]).has("contray.system.banmodule.exempt")))) {
                            int playerId = MySQL_Utils.getPlayerID(Bukkit.getOfflinePlayer(args[0]));
                            if (!MySQL_Utils.getPlayerMuted(playerId)) {

                                String reason = "";
                                for (int i = 2; i < args.length; i++) {
                                    reason = reason + " " + args[i];
                                }
                                reason = ChatColor.translateAlternateColorCodes('&', reason.replaceFirst(" ", ""));

                                long mutetime = Converter.getTimeFromTimeStampString(args[1]);

                                if (mutetime == 0) {
                                    Messager.sendMessage(MessageType.TimeManager_InvaliedTimeStamp_3, p, converts);
                                    return true;
                                } else if (mutetime == -2) {
                                    Messager.sendMessage(MessageType.TimeManager_InvaliedTimeStamp_1, p, converts);
                                    return true;
                                } else if (mutetime == -3) {
                                    Messager.sendMessage(MessageType.TimeManager_InvaliedTimeStamp_2, p, converts);
                                    return true;
                                }

                                mutetime -= 1;

                                final String r = reason;
                                final long mtime = (mutetime * 1000) + System.currentTimeMillis();

                                MySQL_Utils.mutePlayer(playerId, reason, mtime);
                                Messager.sendMessage(MessageType.MuteModule_SuccessfullyMuted, p, converts);

                                if (Bukkit.getOfflinePlayer(args[0]).isOnline()) {
                                    String timeInfo = Cache.messages.get(MessageType.MuteModule_MuteInfo_TimeStamp);
                                    TimeManager muteTime = new TimeManager(mutetime);
                                    timeInfo = timeInfo.replace("%DAYS%", Long.toString(muteTime.getFormedDays())).replace("%DAYS_SHORT%", Cache.messages.get(MessageType.TimeManager_Days_short)).replace("%DAYS_LONG%", Cache.messages.get(MessageType.TimeManager_Days_long));
                                    timeInfo = timeInfo.replace("%HOURS%", Long.toString(muteTime.getFormedHours())).replace("%HOURS_SHORT%", Cache.messages.get(MessageType.TimeManager_Hours_short)).replace("%HOURS_LONG%", Cache.messages.get(MessageType.TimeManager_Hours_long));
                                    timeInfo = timeInfo.replace("%MINUTES%", Long.toString(muteTime.getFormedMinutes())).replace("%MINUTES_SHORT%", Cache.messages.get(MessageType.TimeManager_Minutes_short)).replace("%MINUTES_LONG%", Cache.messages.get(MessageType.TimeManager_Minutes_long));
                                    timeInfo = timeInfo.replace("%SECONDS%", Long.toString(muteTime.getFormedSeconds())).replace("%SECONDS_SHORT%", Cache.messages.get(MessageType.TimeManager_Seconds_short)).replace("%SECONDS_LONG%", Cache.messages.get(MessageType.TimeManager_Seconds_long));
                                    converts.put("%OUTSTANDING_TIME%", timeInfo);

                                    Spieler s = SpielerManager.getSpieler(Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString());
                                    s.setMuted(true);
                                    s.setMuteTime(mtime);
                                    Messager.sendMessage(MessageType.MuteModule_MuteInfo_GotMuted, Bukkit.getPlayer(args[0]), converts);
                                }
                                return true;
                            } else {
                                Messager.sendMessage(MessageType.MuteModule_AlreadyMuted, p, converts);
                                return true;
                            }
                        } else {
                            Messager.sendMessage(MessageType.MuteModule_NotMuteable, p, converts);
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
                Messager.sendMessage(MessageType.Command_NoPerm, p, converts);
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
