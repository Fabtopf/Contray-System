package de.Fabtopf.System.API;

import de.Fabtopf.System.Utilities.Main;
import org.bukkit.command.CommandExecutor;

/**
 * Created by Fabi on 15.09.2017.
 */
public class ModCommand {

    private String name;
    private String description;
    private String permission;
    private String permissionMessage;
    private String usage;

    private CommandExecutor executor;

    public ModCommand(String name, String description, String permission, String permissionMessage, String usage, CommandExecutor executor) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.permissionMessage = permissionMessage;
        this.usage = usage;
        this.executor = executor;

        register();
    }

    private void register() {
        Main.getInstance().getCommand(name).setExecutor(executor);
        Main.getInstance().getCommand(name).setDescription(description);
        Main.getInstance().getCommand(name).setPermissionMessage(permissionMessage);
        Main.getInstance().getCommand(name).setUsage(usage);
        Main.getInstance().getCommand(name).setExecutor(executor);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPermission() {
        return permission;
    }

    public String getPermissionMessage() {
        return permissionMessage;
    }

    public String getUsage() {
        return usage;
    }

    public CommandExecutor getExecutor() {
        return executor;
    }
}
