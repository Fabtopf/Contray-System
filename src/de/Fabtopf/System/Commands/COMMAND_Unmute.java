package de.Fabtopf.System.Commands;

import de.Fabtopf.System.API.Enum.MessageType;
import de.Fabtopf.System.API.Manager.ModuleManager;
import de.Fabtopf.System.API.Manager.PermissionManager;
import de.Fabtopf.System.API.Manager.SpielerManager;
import de.Fabtopf.System.API.Messager;
import de.Fabtopf.System.API.ModCommand;
import de.Fabtopf.System.API.Module;
import de.Fabtopf.System.API.Spieler;
import de.Fabtopf.System.Utilities.Cache;
import de.Fabtopf.System.Utilities.Main;
import de.Fabtopf.System.Utilities.MySQL.MySQL_Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created by Fabi on 29.09.2017.
 */
public class COMMAND_Unmute implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Module mod = ModuleManager.getModule("MuteModule");
        ModCommand cmd = mod.getCommand("unmute");

        HashMap<String, String> converts = new HashMap<String, String>();
        converts.put("%PLUGIN_NAME%", Main.getInstance().getDescription().getName());
        converts.put("%COMMAND_USAGE%", cmd.getUsage());
        if(args.length > 0) converts.put("%UNMUTED_PLAYER%", args[0]);

        if(mod.isEnabled() && (!mod.isDevmode() || (mod.isDevmode() && Cache.devmode))) {

            Player p = null;
            if(sender instanceof Player) { p = (Player) sender; }

            if((p != null && PermissionManager.check(p, cmd.getPermission(), true)) || p == null) {
                if(args.length == 1) {
                    if(MySQL_Utils.getPlayerExists(Bukkit.getOfflinePlayer(args[0]))) {
                        int playerId = MySQL_Utils.getPlayerID(Bukkit.getOfflinePlayer(args[0]));
                        if(MySQL_Utils.getPlayerMuted(playerId)) {
                            MySQL_Utils.unmutePlayer(playerId);
                            Messager.sendMessage(MessageType.MuteModule_SuccessfullyUnmuted, p, converts);

                            if(Bukkit.getOfflinePlayer(args[0]).isOnline()) {
                                Spieler s = SpielerManager.getSpieler(Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString());
                                s.setMuted(false);
                                s.setMuteTime(0);
                                Messager.sendMessage(MessageType.MuteModule_MuteInfo_GotUnmuted, Bukkit.getPlayer(args[0]), converts);
                            }
                            return true;
                        } else {
                            Messager.sendMessage(MessageType.MuteModule_NotMuted, p, converts);
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
