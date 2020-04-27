package me.flaymed.islands.game.scoreboard;

import com.podcrash.api.game.GameType;
import com.podcrash.api.game.scoreboard.GameScoreboard;

public class IslandsScoreboard extends GameScoreboard {
    public IslandsScoreboard(int gameId) {
        super(8, gameId, GameType.DOM);
    }

    @Override
    public void update() {

    }

    @Override
    public void startScoreboardTimer() {

    }
}
