package de.Fabtopf.System.API.Manager;

import de.Fabtopf.System.API.BlockedCommand;
import de.Fabtopf.System.API.Converter;
import de.Fabtopf.System.Utilities.MySQL.MySQL_Utils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Fabi on 03.10.2017.
 */
public class BlockedCommandManager {

    private static HashMap<String, BlockedCommand> blockedCommands = new HashMap<String, BlockedCommand>();

    public static void registerBlockedCommand(String command, String permission, boolean triggerIfConains) {
        if(!blockedCommands.containsKey(command)) {
            if (!MySQL_Utils.blockedCommandExists(command)) MySQL_Utils.registerBlockedCommand(command, permission, triggerIfConains);
            blockedCommands.put(command, new BlockedCommand(command, permission, triggerIfConains));
        }
    }

    public static void unregisterBlockedCommand(String command) {
        if(blockedCommands.containsKey(command)) {
            MySQL_Utils.unregisterBlockedCommand(command);
            blockedCommands.remove(command);
        }
    }

    public static BlockedCommand getBlockedCommand(String command) {
        return blockedCommands.get(command);
    }

    public static List<BlockedCommand> getBlockedCommands() {
        return (List<BlockedCommand>) Converter.hashMapValuesToList(blockedCommands);
    }

}
