package de.Fabtopf.System.API;

import de.Fabtopf.System.API.Enum.MessageType;
import de.Fabtopf.System.Utilities.Cache;
import de.Fabtopf.System.Utilities.Main;
import de.Fabtopf.System.Utilities.MySQL.MySQL_Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Fabi on 15.09.2017.
 */
public class Module {

    private String name;

    private boolean devmode;
    private boolean enabled;

    private HashMap<String, ModCommand> commands = new HashMap<String, ModCommand>();
    private HashMap<String, Listener> listeners = new HashMap<String, Listener>();

    public Module(String name) {
        this.name = name;
        this.devmode = MySQL_Utils.getModuleDevmode(name);
        this.enabled = MySQL_Utils.getModuleEnabled(name);
    }

    public void registerCommand(String name, String description, String permission, String usage, CommandExecutor executor) {
        String permissionMessage = ChatColor.translateAlternateColorCodes('&', Cache.messages.get(MessageType.Command_NoPerm).replace("%PLUGIN_NAME%", Main.getInstance().getDescription().getName()));
        commands.put(name, new ModCommand(name, description, permission, permissionMessage, usage, executor));
    }

    public void registerListener(String name, Listener listener) {
        Main.getInstance().getServer().getPluginManager().registerEvents(listener, Main.getInstance());
        listeners.put(name, listener);
    }

    public ModCommand getCommand(String name) {
        return commands.get(name);
    }

    public Listener getListener(String name) {
        return listeners.get(name);
    }

    public List<ModCommand> getCommands() {
        return (List<ModCommand>) Converter.hashMapValuesToList(commands);
    }

    public List<Listener> getListeners() {
        return (List<Listener>) Converter.hashMapValuesToList(listeners);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isDevmode() {
        return devmode;
    }

    public void setEnabled(boolean enabled) {
        if(enabled == this.enabled) return;
        this.enabled = enabled;
        MySQL_Utils.updateModuleEnabled(name, enabled);
    }

    public void setDevmode(boolean devmode) {
        if(devmode == this.devmode) return;
        this.devmode = devmode;
        MySQL_Utils.updateModuleDevmode(name, devmode);
    }

}
