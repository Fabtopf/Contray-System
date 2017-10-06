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
 * Created by Fabi on 03.10.2017.
 */
public class COMMAND_Freeze implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Module mod = ModuleManager.getModule("FreezeModule");
        ModCommand cmd = mod.getCommand("freeze");

        HashMap<String, String> converts = new HashMap<String, String>();
        converts.put("%PLUGIN_NAME%", Main.getInstance().getDescription().getName());
        converts.put("%COMMAND_USAGE%", cmd.getUsage());

        if (mod.isEnabled() && (!mod.isDevmode() || (mod.isDevmode() && Cache.devmode))) {

            Player p = null;
            if (sender instanceof Player) {
                p = (Player) sender;
            }

            if ((p != null && PermissionManager.check(p, cmd.getPermission(), true)) || p == null) {
                if(args.length == 1) {
                    if(Bukkit.getOfflinePlayer(args[0]).isOnline()) {
                        Spieler s = SpielerManager.getSpieler(Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString());
                        converts.put("%FROZEN_PLAYER%", Bukkit.getOfflinePlayer(args[0]).getName());
                        converts.put("%UNFROZEN_PLAYER%", Bukkit.getOfflinePlayer(args[0]).getName());
                        if(s.getDatabaseID() != -1) {
                            if(s.getFrozen()) {
                                MySQL_Utils.unfreezePlayer(s.getDatabaseID());
                                s.setFrozen(false);
                                Messager.sendMessage(MessageType.FreezeModule_SuccessfullyUnfrozen, p, converts);
                                Messager.sendMessage(MessageType.FreezeModule_GotUnfrozen, Bukkit.getPlayer(args[0]), converts);
                                return true;
                            } else {
                                if(!PermissionManager.check(Bukkit.getPlayer(args[0]), "contray.system.freezemodule.exempt", true)) {
                                    MySQL_Utils.freezePlayer(s.getDatabaseID(), Bukkit.getPlayer(args[0]).getLocation());
                                    s.setFrozen(true);
                                    Messager.sendMessage(MessageType.FreezeModule_SuccessfullyUnfrozen, p, converts);
                                    Messager.sendMessage(MessageType.FreezeModule_GotUnfrozen, Bukkit.getPlayer(args[0]), converts);
                                    return true;
                                } else {
                                    Messager.sendMessage(MessageType.FreezeModule_NotFreezeable, p, converts);
                                    return true;
                                }
                            }
                        } else {
                            Messager.sendMessage(MessageType.Database_PlayerDoesntExist, p, converts);
                            return true;
                        }
                    } else {
                        Messager.sendMessage(MessageType.FreezeModule_PlayerNotOnline, p, converts);
                        return true;
                    }
                } else if(args.length == 2) {
                    if(Bukkit.getOfflinePlayer(args[0]).isOnline()) {
                        Spieler s = SpielerManager.getSpieler(Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString());
                        converts.put("%FROZEN_PLAYER%", Bukkit.getOfflinePlayer(args[0]).getName());
                        converts.put("%UNFROZEN_PLAYER%", Bukkit.getOfflinePlayer(args[0]).getName());
                        if(s.getDatabaseID() != -1) {
                            if(args[1].equalsIgnoreCase("on")) {
                                if(!PermissionManager.check(Bukkit.getPlayer(args[0]), "contray.system.freezemodule.exempt", true)) {
                                    if (!s.getFrozen()) {
                                        MySQL_Utils.freezePlayer(s.getDatabaseID(), Bukkit.getPlayer(args[0]).getLocation());
                                        s.setFrozen(true);
                                        Messager.sendMessage(MessageType.FreezeModule_SuccessfullyUnfrozen, p, converts);
                                        Messager.sendMessage(MessageType.FreezeModule_GotUnfrozen, Bukkit.getPlayer(args[0]), converts);
                                        return true;
                                    } else {
                                        Messager.sendMessage(MessageType.FreezeModule_AlreadyFrozen, p, converts);
                                        return true;
                                    }
                                } else {
                                    Messager.sendMessage(MessageType.FreezeModule_NotFreezeable, p, converts);
                                    return true;
                                }
                            } else if(args[1].equalsIgnoreCase("off")) {
                                if(s.getFrozen()) {
                                    MySQL_Utils.unfreezePlayer(s.getDatabaseID());
                                    s.setFrozen(false);
                                    Messager.sendMessage(MessageType.FreezeModule_SuccessfullyUnfrozen, p, converts);
                                    Messager.sendMessage(MessageType.FreezeModule_GotUnfrozen, Bukkit.getPlayer(args[0]), converts);
                                    return true;
                                } else {
                                    Messager.sendMessage(MessageType.FreezeModule_NotFrozen, p, converts);
                                    return true;
                                }
                            } else {
                                Messager.sendMessage(MessageType.Command_ShowUsage, p, converts);
                                return true;
                            }
                        } else {
                            Messager.sendMessage(MessageType.Database_PlayerDoesntExist, p, converts);
                            return true;
                        }
                    } else {
                        if(args[1].equalsIgnoreCase("off")) {
                            int databaseID = MySQL_Utils.getPlayerID(Bukkit.getOfflinePlayer(args[0]));
                            converts.put("%UNFROZEN_PLAYER%", Bukkit.getOfflinePlayer(args[0]).getName());
                            if(MySQL_Utils.getPlayerFrozen(databaseID)) {
                                MySQL_Utils.unfreezePlayer(databaseID);
                                Messager.sendMessage(MessageType.FreezeModule_SuccessfullyUnfrozen, p, converts);
                                return true;
                            } else {
                                Messager.sendMessage(MessageType.FreezeModule_AlreadyFrozen, p, converts);
                                return true;
                            }
                        } else {
                            Messager.sendMessage(MessageType.FreezeModule_PlayerNotOnline, p, converts);
                            return true;
                        }
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
