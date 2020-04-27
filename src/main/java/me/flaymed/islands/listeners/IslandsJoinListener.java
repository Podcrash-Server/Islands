package me.flaymed.islands.listeners;

import com.podcrash.api.kits.KitPlayer;
import com.podcrash.api.kits.KitPlayerManager;
import com.podcrash.api.listeners.ListenerBase;
import me.flaymed.islands.kits.classes.Brawler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class IslandsJoinListener extends ListenerBase {
    public IslandsJoinListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void join(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        Brawler brawler = new Brawler(p);
        KitPlayerManager.getInstance().addKitPlayer(brawler);

    }
}
