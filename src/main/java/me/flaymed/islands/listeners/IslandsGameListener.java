package me.flaymed.islands.listeners;

import com.podcrash.api.db.pojos.map.IslandsMap;
import com.podcrash.api.events.game.GameMapLoadEvent;
import com.podcrash.api.events.game.GameStartEvent;
import com.podcrash.api.listeners.ListenerBase;
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
    public void loadMap(GameMapLoadEvent e) {
        IslandsGame game = (IslandsGame) e.getGame();
        IslandsMap map = (IslandsMap) game.getMap();
        game.setBridgeType(map.getBridgeType());
        game.getBridgeGenerator().setUp(map);
    }

    @EventHandler
    public void start(GameStartEvent e) {
        IslandsGame game = (IslandsGame) e.getGame();
        game.setStage(GameStage.PREPARE);

        //destroy bridges:
        game.destroyBridge(10);
    }
    @EventHandler
    public void stageChange(GameStageEvent e) {
        switch (e.getStage()) {
            case PREPARE:
                break;
            case FALLEN:
                ((IslandsGame) e.getGame()).fallBridge(10);
                break;
        }
    }
}
