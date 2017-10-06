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
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.HashMap;

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
                        if((Bukkit.getOfflinePlayer(args[0]).isOnline() && !PermissionManager.check(Bukkit.getPlayer(args[0]), "contray.system.banmodule.exempt", true)) || (!Bukkit.getOfflinePlayer(args[0]).isOnline() &&
                                !Bukkit.getOfflinePlayer(args[0]).isOp() && (Bukkit.getPluginManager().getPlugin("PermissionsEx") != null && Bukkit.getPluginManager().getPlugin("PermissionsEx").isEnabled() && !PermissionsEx.getUser(args[0]).has("contray.system.banmodule.exempt")))) {
                            int playerId = MySQL_Utils.getPlayerID(Bukkit.getOfflinePlayer(args[0]));
                            if (!MySQL_Utils.getPlayerBanned(playerId)) {

                                String reason = "";
                                for (int i = 2; i < args.length; i++) {
                                    reason = reason + " " + args[i];
                                }
                                reason = ChatColor.translateAlternateColorCodes('&', reason.replaceFirst(" ", ""));

                                long bantime = Converter.getTimeFromTimeStampString(args[1]);

                                if (bantime == 0) {
                                    Messager.sendMessage(MessageType.TimeManager_InvaliedTimeStamp_3, p, converts);
                                    return true;
                                } else if (bantime == -2) {
                                    Messager.sendMessage(MessageType.TimeManager_InvaliedTimeStamp_1, p, converts);
                                    return true;
                                } else if (bantime == -3) {
                                    Messager.sendMessage(MessageType.TimeManager_InvaliedTimeStamp_2, p, converts);
                                    return true;
                                }

                                final String r = reason;
                                final long btime = (bantime * 1000) + System.currentTimeMillis();

                                MySQL_Utils.banPlayer(playerId, reason, btime);
                                Messager.sendMessage(MessageType.MuteModule_SuccessfullyMuted, p, converts);

                                if (Bukkit.getOfflinePlayer(args[0]).isOnline())
                                    Bukkit.getPlayer(args[0]).kickPlayer(Converter.getBanScreen(playerId, btime, r));
                                return true;
                            } else {
                                Messager.sendMessage(MessageType.BanModule_AlreadyBanned, p, converts);
                                return true;
                            }
                        } else {
                            Messager.sendMessage(MessageType.BanModule_NotBanable, p, converts);
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
