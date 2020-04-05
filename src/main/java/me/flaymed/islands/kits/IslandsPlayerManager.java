package me.flaymed.islands.kits;

import me.flaymed.islands.Main;
import org.bukkit.plugin.java.JavaPlugin;

public class IslandsPlayerManager {

    private static volatile IslandsPlayerManager ipm;
    private JavaPlugin plugin = Main.getInstance();

    public static IslandsPlayerManager getInstance() {
        return ipm;
    }
}
