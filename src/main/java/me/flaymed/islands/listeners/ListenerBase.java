package me.flaymed.islands.listeners;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class ListenerBase implements Listener {

    public ListenerBase(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getLogger().info("[LISTENER] Registered Listener: " + this.getClass().getSimpleName());
    }

}
