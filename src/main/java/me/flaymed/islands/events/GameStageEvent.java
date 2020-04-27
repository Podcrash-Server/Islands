package me.flaymed.islands.events;

import com.podcrash.api.game.Game;
import me.flaymed.islands.game.GameStage;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStageEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Game game;
    private final GameStage stage;

    public GameStageEvent(Game game, GameStage stage) {
        this.game = game;
        this.stage = stage;
    }

    public Game getGame() {
        return game;
    }

    public GameStage getStage() {
        return stage;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
