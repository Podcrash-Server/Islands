package me.flaymed.islands.listeners;

import com.podcrash.api.mc.listeners.ListenerBase;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class BorderListener extends ListenerBase {
    public BorderListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void playerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Location to = e.getTo();

    }
}
