package de.Fabtopf.System.Commands;

import de.Fabtopf.System.API.*;
import de.Fabtopf.System.API.Enum.MessageType;
import de.Fabtopf.System.API.Manager.ModuleManager;
import de.Fabtopf.System.API.Manager.PermissionManager;
import de.Fabtopf.System.API.Manager.SpielerManager;
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
 * Created by Fabi on 29.09.2017.
 */
public class COMMAND_Mute implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Module mod = ModuleManager.getModule("MuteModule");
        ModCommand cmd = mod.getCommand("mute");

        HashMap<String, String> converts = new HashMap<String, String>();
        converts.put("%PLUGIN_NAME%", Main.getInstance().getDescription().getName());
        converts.put("%COMMAND_USAGE%", cmd.getUsage());
        converts.put("%OUTSTANDING_TIME%", Cache.messages.get(MessageType.TimeManager_perm));
        if(args.length > 0) converts.put("%MUTED_PLAYER%", args[0]);

        if(mod.isEnabled() && (!mod.isDevmode() || (mod.isDevmode() && Cache.devmode))) {

            Player p = null;
            if(sender instanceof Player) { p = (Player) sender; }

            if((p != null && PermissionManager.check(p, cmd.getPermission(), true)) || p == null) {
                if(args.length >= 2) {
                    if(MySQL_Utils.getPlayerExists(Bukkit.getOfflinePlayer(args[0]))) {
                        if((Bukkit.getOfflinePlayer(args[0]).isOnline() && !PermissionManager.check(Bukkit.getPlayer(args[0]), "contray.system.mutemodule.exempt", true)) || (!Bukkit.getOfflinePlayer(args[0]).isOnline() &&
                                !Bukkit.getOfflinePlayer(args[0]).isOp() && (Bukkit.getPluginManager().getPlugin("PermissionsEx") != null && Bukkit.getPluginManager().getPlugin("PermissionsEx").isEnabled() && !PermissionsEx.getUser(args[0]).has("contray.system.mutemodule.exempt")))) {
                            int playerId = MySQL_Utils.getPlayerID(Bukkit.getOfflinePlayer(args[0]));
                            if (!MySQL_Utils.getPlayerMuted(playerId)) {
                                String reason = "";
                                for (int i = 1; i < args.length; i++) {
                                    reason = reason + " " + args[i];
                                }
                                reason = ChatColor.translateAlternateColorCodes('&', reason.replaceFirst(" ", ""));
                                final String r = reason;

                                MySQL_Utils.mutePlayer(playerId, reason, -1);
                                Messager.sendMessage(MessageType.MuteModule_SuccessfullyMuted, p, converts);

                                if (Bukkit.getOfflinePlayer(args[0]).isOnline()) {
                                    Spieler s = SpielerManager.getSpieler(Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString());
                                    s.setMuted(true);
                                    s.setMuteTime(-1);
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
