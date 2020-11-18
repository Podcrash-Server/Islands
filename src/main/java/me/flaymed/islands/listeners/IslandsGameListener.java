package me.flaymed.islands.listeners;

import me.flaymed.islands.events.GameStageEvent;
import me.flaymed.islands.game.GameStage;
import me.flaymed.islands.game.IslandsGame;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class IslandsGameListener extends ListenerBase {
    public IslandsGameListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void gameStageChange(GameStageEvent e) {
        if (e.getStage() == GameStage.FALLEN) {
            ((IslandsGame) e.getGame()).fallBridge(5);
            Bukkit.broadcastMessage("Bridges are falling!");
            //TODO: Fancy message
        }
    }
}
