package eu.vortexgg.rpg.util;

import eu.vortexgg.rpg.RPG;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@Getter
@Setter
public class FlatFile {

    protected FileConfiguration config;
    protected File file;
    protected final RPG plugin;
    protected final String fileName;

    public FlatFile(String fileName, RPG plugin) {
        this.plugin = plugin;
        this.fileName = fileName;
        createFile();
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void createFile() {
        if (file == null)
            file = new File(plugin.getDataFolder(), fileName + ".yml");
        reload();
    }

}
