package me.flaymed.islands.game;

import com.podcrash.api.game.*;
import com.podcrash.api.game.objects.ItemObjective;
import com.podcrash.api.game.objects.WinObjective;
import me.flaymed.islands.events.GameStageEvent;
import me.flaymed.islands.game.scoreboard.IslandsScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.List;

public class IslandsGame extends Game {
    private GameStage stage;
    public IslandsGame(int id, String name) {
        super(id, name, GameType.DOM);
        this.board = new IslandsScoreboard(id);
        setStage(GameStage.PREPARE);
    }

    @Override
    public int getAbsoluteMinPlayers() {
        return 6;
    }

    @Override
    public void leaveCheck() {

    }

    @Override
    public Class<? extends com.podcrash.api.db.pojos.map.GameMap> getMapClass() {
        return null;
    }

    @Override
    public TeamSettings getTeamSettings() {
        return new TeamSettings.Builder()
            .setTeamColors(TeamEnum.RED, TeamEnum.BLUE)
            .setMax(12)
            .setMin(6)
            .build();
    }

    @Override
    public String getMode() {
        return "Islands";
    }

    public GameStage getStage() {
        return stage;
    }

    @Override
    public String getPresentableResult() {
        return null;
    }

    public void setStage(GameStage stage) {
        this.stage = stage;
        Bukkit.getPluginManager().callEvent(new GameStageEvent(this, this.stage));
    }
    @Override
    public List<WinObjective> getWinObjectives() {
        return null;
    }

    @Override
    public List<ItemObjective> getItemObjectives() {
        return null;
    }
}
