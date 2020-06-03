package me.flaymed.islands.listeners;

import com.podcrash.api.db.pojos.map.IslandsMap;
import com.podcrash.api.events.DamageApplyEvent;
import com.podcrash.api.events.game.GameMapLoadEvent;
import com.podcrash.api.events.game.GameResurrectEvent;
import com.podcrash.api.events.game.GameStartEvent;
import com.podcrash.api.game.Game;
import com.podcrash.api.game.GameManager;
import com.podcrash.api.game.GameState;
import com.podcrash.api.listeners.ListenerBase;
import me.flaymed.islands.events.GameStageEvent;
import me.flaymed.islands.game.GameStage;
import me.flaymed.islands.game.IslandsGame;
import me.flaymed.islands.game.resource.AnimalSpawnResource;
import me.flaymed.islands.game.resource.ShroomSpawnResource;
import me.flaymed.islands.game.resource.WaterDamagerResource;
import me.flaymed.islands.game.scoreboard.IslandsScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class IslandsGameListener extends ListenerBase {
    private Set<String> died = new HashSet<>();
    public IslandsGameListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void waterDamage(DamageApplyEvent e) {
        Game game = GameManager.getGame();
        if (game == null || game.getGameState() == GameState.LOBBY) return;
        if (e.containsSource(IslandsGame.WATER_DAMAGE)) {
            e.setArmorValue(0);
        }
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
        died.clear();
        IslandsGame game = (IslandsGame) e.getGame();
        game.generateOres();
        game.setStage(GameStage.PREPARE);
        game.setScoreboardInput(new IslandsScoreboard(game, game.getGameScoreboard()));
        game.registerResources(
            new AnimalSpawnResource(game.getId()),
            new ShroomSpawnResource(game.getId()),
            new WaterDamagerResource(game.getId()));
        //destroy bridges:
        game.destroyBridge(5);
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

    @EventHandler
    public void resurect(GameResurrectEvent e) {
        IslandsGame game = (IslandsGame) e.getGame();
        if (game.getStage() != GameStage.PREPARE)
            return;
        if (!died.add(e.getWho().getName())) {
            e.setCancelled(true);
            game.addSpectator(e.getWho());
        }
    }
}
