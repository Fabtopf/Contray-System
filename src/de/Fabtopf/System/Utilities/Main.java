package de.Fabtopf.System.Utilities;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Fabi on 15.09.2017.
 */
public class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        Functions.enablePlugin();

    }

    @Override
    public void onDisable() {

        Functions.disablePlugin();

    }

    public static Main getInstance() {
        return instance;
    }

}
