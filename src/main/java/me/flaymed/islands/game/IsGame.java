package me.flaymed.islands.game;

import com.podcrash.api.db.pojos.map.GameMap;
import com.podcrash.api.mc.game.Game;
import com.podcrash.api.mc.game.GameType;
import com.podcrash.api.mc.game.TeamSettings;
import com.podcrash.api.mc.game.objects.ItemObjective;
import com.podcrash.api.mc.game.objects.WinObjective;
import com.podcrash.api.mc.game.scoreboard.GameScoreboard;
import org.bukkit.Location;

import java.util.List;

public class IsGame extends Game {


    /**
     * Constructor for the game.
     *
     * @param id   The ID of the game.
     * @param name The name of the game.
     */
    public IsGame(int id, String name) {
        super(id, name, GameType.valueOf("IS"));
    }

    @Override
    public GameScoreboard getGameScoreboard() {
        return null;
    }

    @Override
    public int getAbsoluteMinPlayers() {
        return 0;
    }

    @Override
    public Location spectatorSpawn() {
        return null;
    }

    @Override
    public void leaveCheck() {

    }

    @Override
    public Class<? extends GameMap> getMapClass() {
        return null;
    }

    @Override
    public TeamSettings getTeamSettings() {
        return null;
    }

    @Override
    public String getMode() {
        return null;
    }

    @Override
    public String getPresentableResult() {
        return null;
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
