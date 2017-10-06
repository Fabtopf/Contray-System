package de.Fabtopf.System.Commands;

import de.Fabtopf.System.API.BlockedCommand;
import de.Fabtopf.System.API.Enum.MessageType;
import de.Fabtopf.System.API.Manager.BlockedCommandManager;
import de.Fabtopf.System.API.Manager.ModuleManager;
import de.Fabtopf.System.API.Manager.PermissionManager;
import de.Fabtopf.System.API.Messager;
import de.Fabtopf.System.API.ModCommand;
import de.Fabtopf.System.API.Module;
import de.Fabtopf.System.Utilities.Cache;
import de.Fabtopf.System.Utilities.Main;
import de.Fabtopf.System.Utilities.MySQL.MySQL_Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created by Fabi on 03.10.2017.
 */
public class COMMAND_BlockCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Module mod = ModuleManager.getModule("BlockCommandModule");
        ModCommand cmd = mod.getCommand("blockcommand");

        HashMap<String, String> converts = new HashMap<String, String>();
        converts.put("%PLUGIN_NAME%", Main.getInstance().getDescription().getName());
        converts.put("%COMMAND_USAGE%", cmd.getUsage());

        if(mod.isEnabled() && (!mod.isDevmode() || (mod.isDevmode() && Cache.devmode))) {

            Player p = null;
            if (sender instanceof Player) {
                p = (Player) sender;
            }

            if ((p != null && PermissionManager.check(p, cmd.getPermission(), true)) || p == null) {

                if(args.length == 1 && args[0].equalsIgnoreCase("list")) {

                    String replacement = "";
                    for(BlockedCommand b_cmd : BlockedCommandManager.getBlockedCommands()) {
                        replacement += Cache.messages.get(MessageType.BlockCommandModule_CommandDesign).replace("%BLOCKED_COMMAND%", "/" + b_cmd.getCommand()).replace("%EXEMPT_PERMISSION%", (b_cmd.getPermission() != null ? b_cmd.getPermission() : "-")).replace("%TRIGGER_ON_CONTAIN%", (b_cmd.isTriggerIfContains() ? "true" : "false"));
                    }

                    converts.put("%BLOCKED_COMMANDS%", (replacement.equals("") ? Cache.messages.get(MessageType.BlockCommandModule_NothingBlocked) : replacement));
                    Messager.sendMessage(MessageType.BlockCommandModule_CommandList, p, converts);
                    return true;

                } else if(args.length == 2 && args[0].equalsIgnoreCase("remove")) {

                    String b_cmd = args[1].toLowerCase().replaceFirst("/", "");
                    converts.put("%UNBLOCKED_COMMAND%", args[1]);
                    if(BlockedCommandManager.getBlockedCommand(b_cmd) != null) {
                        BlockedCommandManager.unregisterBlockedCommand(b_cmd);
                        MySQL_Utils.unregisterBlockedCommand(b_cmd);
                        Messager.sendMessage(MessageType.BlockCommandModule_SuccessfullyUnblocked, p, converts);
                        return true;
                    } else {
                        Messager.sendMessage(MessageType.BlockCommandModule_CommandNotBlocked, p, converts);
                        return true;
                    }

                } else if(args.length == 2 && args[0].equalsIgnoreCase("add")) {

                    String b_cmd = args[1].toLowerCase().replaceFirst("/", "");
                    converts.put("%BLOCKED_COMMAND%", args[1]);
                    if(BlockedCommandManager.getBlockedCommand(b_cmd) == null) {
                        BlockedCommandManager.registerBlockedCommand(b_cmd, null, false);
                        MySQL_Utils.registerBlockedCommand(b_cmd, null, false);
                        Messager.sendMessage(MessageType.BlockCommandModule_SuccessfullyBlocked, p, converts);
                        return true;
                    } else {
                        Messager.sendMessage(MessageType.BlockCommandModule_CommandAlreadyBlocked, p, converts);
                        return true;
                    }

                } else if(args.length == 3 && args[0].equalsIgnoreCase("add")) {

                    String b_cmd = args[1].toLowerCase().replaceFirst("/", "");
                    converts.put("%BLOCKED_COMMAND%", args[1]);

                    boolean bool = false;
                    String perm = null;

                    if(args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false")) {
                        bool = (args[2].equalsIgnoreCase("true") ? true : false);
                    } else {
                        perm = args[2].toLowerCase();
                    }

                    if(BlockedCommandManager.getBlockedCommand(b_cmd) == null) {
                        BlockedCommandManager.registerBlockedCommand(b_cmd, perm, bool);
                        MySQL_Utils.registerBlockedCommand(b_cmd, perm, bool);
                        Messager.sendMessage(MessageType.BlockCommandModule_SuccessfullyBlocked, p, converts);
                        return true;
                    } else {
                        Messager.sendMessage(MessageType.BlockCommandModule_CommandAlreadyBlocked, p, converts);
                        return true;
                    }

                } else if(args.length == 4 && args[0].equalsIgnoreCase("add")) {

                    String b_cmd = args[1].toLowerCase().replaceFirst("/", "");
                    converts.put("%BLOCKED_COMMAND%", args[1]);

                    boolean bool = (args[2].equalsIgnoreCase("true") ? true : false);
                    String perm = args[3].toLowerCase();

                    if(BlockedCommandManager.getBlockedCommand(b_cmd) == null) {
                        BlockedCommandManager.registerBlockedCommand(b_cmd, perm, bool);
                        MySQL_Utils.registerBlockedCommand(b_cmd, perm, bool);
                        Messager.sendMessage(MessageType.BlockCommandModule_SuccessfullyBlocked, p, converts);
                        return true;
                    } else {
                        Messager.sendMessage(MessageType.BlockCommandModule_CommandAlreadyBlocked, p, converts);
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
