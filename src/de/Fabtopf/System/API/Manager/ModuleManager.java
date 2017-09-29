package de.Fabtopf.System.API.Manager;

import de.Fabtopf.System.API.Converter;
import de.Fabtopf.System.API.Module;
import de.Fabtopf.System.Utilities.Main;
import de.Fabtopf.System.Utilities.MySQL.MySQL_Utils;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Fabi on 15.09.2017.
 */
public class ModuleManager {

    private static HashMap<String, Module> modules = new HashMap<String, Module>();

    public static void registerModule(String name) {
        if(!modules.containsKey(name)) {
            if (MySQL_Utils.moduleExists(name)) {
                modules.put(name, new Module(name));
            } else {
                MySQL_Utils.registerModule(name);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        modules.put(name, new Module(name));
                    }
                }.runTaskLater(Main.getInstance(), 5);
            }
        }
    }

    public static void unregisterModule(String name) {
        modules.remove(name);
    }

    public static Module getModule(String name) {
        return modules.get(name);
    }

    public static List<Module> getModules() {
        return (List<Module>) Converter.hashMapValuesToList(modules);
    }

}
