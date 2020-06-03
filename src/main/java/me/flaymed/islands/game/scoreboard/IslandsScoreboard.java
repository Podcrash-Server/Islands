package me.flaymed.islands.game.scoreboard;

import com.podcrash.api.game.Game;
import com.podcrash.api.game.GameType;
import com.podcrash.api.game.scoreboard.GameScoreboard;
import com.podcrash.api.scoreboard.CustomScoreboard;
import com.podcrash.api.scoreboard.ScoreboardInput;

public class IslandsScoreboard extends ScoreboardInput {
    private final Game game;
    public IslandsScoreboard(Game game, CustomScoreboard scoreboard) {
        super(scoreboard);
        this.game = game;
    }

    @Override
    public void update() {

    }

    @Override
    public boolean cancel() {
        return false;
    }
}
