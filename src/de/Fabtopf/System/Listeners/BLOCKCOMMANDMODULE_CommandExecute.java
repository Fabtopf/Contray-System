package de.Fabtopf.System.Listeners;

import de.Fabtopf.System.API.BlockedCommand;
import de.Fabtopf.System.API.Enum.MessageType;
import de.Fabtopf.System.API.Manager.BlockedCommandManager;
import de.Fabtopf.System.API.Manager.ModuleManager;
import de.Fabtopf.System.API.Manager.PermissionManager;
import de.Fabtopf.System.API.Messager;
import de.Fabtopf.System.API.Module;
import de.Fabtopf.System.Utilities.Cache;
import de.Fabtopf.System.Utilities.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashMap;

/**
 * Created by Fabi on 03.10.2017.
 */
public class BLOCKCOMMANDMODULE_CommandExecute implements Listener {

    @EventHandler
    public void onPlayerExecuteCommand(PlayerCommandPreprocessEvent e) {

        Module mod = ModuleManager.getModule("BlockCommandModule");

        HashMap<String, String> converts = new HashMap<String, String>();
        converts.put("%PLUGIN_NAME%", Main.getInstance().getDescription().getName());

        if(mod.isEnabled() && (!mod.isDevmode() || (mod.isDevmode() && Cache.devmode))) {

            Player p = e.getPlayer();
            String cmd = e.getMessage().split(" ")[0].replace("/", "").toLowerCase();

            if (BlockedCommandManager.getBlockedCommand(cmd) != null) {
                BlockedCommand b_cmd = BlockedCommandManager.getBlockedCommand(cmd);
                if (!(PermissionManager.check(p, "contray.system.blockcommandmodule.exempt", true) || (!b_cmd.getPermission().equals("-") && PermissionManager.check(p, b_cmd.getPermission(), true)))) {
                    e.setCancelled(true);
                    Messager.sendMessage(MessageType.BlockCommandModule_CommandBlocked, p, converts);
                }
            } else {
                for (BlockedCommand b_cmd : BlockedCommandManager.getBlockedCommands()) {
                    if (b_cmd.isTriggerIfContains() && cmd.contains(b_cmd.getCommand())) {
                        if (!(PermissionManager.check(p, "contray.system.blockcommandmodule.exempt", true) || (!b_cmd.getPermission().equals("-") && PermissionManager.check(p, b_cmd.getPermission(), true)))) {
                            e.setCancelled(true);
                            Messager.sendMessage(MessageType.BlockCommandModule_CommandBlocked, p, converts);
                        }
                    }
                }
            }

        }

    }

}
