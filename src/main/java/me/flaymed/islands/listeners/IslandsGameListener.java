package me.flaymed.islands.listeners;

import com.podcrash.api.db.pojos.map.IslandsMap;
import com.podcrash.api.events.game.GameMapChangeEvent;
import com.podcrash.api.events.game.GameMapLoadEvent;
import com.podcrash.api.listeners.ListenerBase;
import me.flaymed.islands.game.IslandsGame;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class IslandsGameListener extends ListenerBase {
    public IslandsGameListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void loadMap(GameMapLoadEvent e) {
        IslandsGame game = (IslandsGame) e.getGame();
        IslandsMap map = (IslandsMap) game.getMap();
        game.setBridgeType(map.getBridgeType());
        game.getBridgeGenerator().setUp(map);
    }
}
