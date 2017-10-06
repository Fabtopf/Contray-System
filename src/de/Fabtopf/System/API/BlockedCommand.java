package de.Fabtopf.System.API;

/**
 * Created by Fabi on 03.10.2017.
 */
public class BlockedCommand {

    private String command;
    private String permission;

    private boolean triggerIfContains;

    public BlockedCommand(String command, String permission, boolean triggerIfContains) {
        this.command = command;
        this.permission = permission;
        this.triggerIfContains = triggerIfContains;
    }

    public String getCommand() { return command; }
    public String getPermission() { return permission; }
    public boolean isTriggerIfContains() { return triggerIfContains; }

}
