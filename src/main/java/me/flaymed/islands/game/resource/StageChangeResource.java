package me.flaymed.islands.game.resource;

import com.podcrash.api.game.resources.TimeGameResource;
import com.podcrash.api.plugin.PodcrashSpigot;
import me.flaymed.islands.game.GameStage;
import me.flaymed.islands.game.IslandsGame;

public class StageChangeResource extends TimeGameResource {
    private final long endTime;
    public StageChangeResource(int gameID, long endTime) {
        super(gameID);
        this.endTime = endTime;
    }

    @Override
    public void task() {

    }

    @Override
    public boolean cancel() {
        return super.cancel() || System.currentTimeMillis() >= endTime;
    }

    @Override
    public void cleanup() {
        ((IslandsGame) game).setStage(GameStage.FALLEN);
    }

    @Override
    public void init() {
        runAsync(10, 0);
    }
}
