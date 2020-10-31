package me.flaymed.islands;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Config {

    private YamlConfiguration maps;

    public Config() {
        File fileMaps = new File(Islands.getInstance().getDataFolder(), "maps.yml");
        if (!fileMaps.exists()) return;

        this.maps = YamlConfiguration.loadConfiguration(fileMaps);
        try{
            maps.load(fileMaps);
        }catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public ConfigurationSection getWorldData(String worldName) {
        return maps.getConfigurationSection("Maps." + worldName);
    }

    public List<String> getWorldNames() {
        List<String> worldNames = new ArrayList<>();
        for (String world : maps.getConfigurationSection("Maps").getKeys(false)) {
            worldNames.add(world);
        }

        return worldNames;
    }

    private void log(String msg) {
        Islands.getInstance().getLogger().log(Level.INFO, msg);
    }

    private void error(String msg) {
        Islands.getInstance().getLogger().log(Level.SEVERE, msg);
    }

}
