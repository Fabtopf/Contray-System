package de.Fabtopf.System.API;

import de.Fabtopf.System.Utilities.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Fabi on 15.09.2017.
 */
public class Config {

    private String file_path;
    private String file_name;

    private File file;
    private FileConfiguration cfg;

    public Config(String path, String name) {
        this.file_name = name;
        this.file_path = path;

        this.file = getFile();
        this.cfg = getFileConfiguration();

        createStandartInput();
    }

    private File getFile() { return new File(file_path, file_name); }
    private FileConfiguration getFileConfiguration() { return YamlConfiguration.loadConfiguration(file); }

    private void save() {
        try {
            cfg.options().copyDefaults(true);
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createStandartInput() {
        cfg.options().copyHeader(true);
        cfg.options().header("File created by " + Main.getInstance().getDescription().getName() + " #");
        save();
    }

    public void create(String path, Object obj) {
        cfg.addDefault(path, obj);
        save();
    }

    public void set(String path, Object obj) {
        if(cfg.get(path) == null) {
            cfg.addDefault(path, obj);
        } else {
            cfg.set(path, obj);
        }
        save();
    }

    public void delete(String path) {
        cfg.set(path, null);
        save();
    }

    public String getString(String path) {
        return cfg.getString(path);
    }

    public int getInt(String path) {
        return cfg.getInt(path);
    }

    public List<?> getList(String path) {
        return cfg.getList(path);
    }

    public boolean getBoolean(String path) {
        return cfg.getBoolean(path);
    }

    public double getDouble(String path) {
        return cfg.getDouble(path);
    }

    public Object getObject(String path) {
        return cfg.get(path);
    }

}
