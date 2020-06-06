package me.flaymed.islands.game.scoreboard;

import com.podcrash.api.game.*;
import com.podcrash.api.game.scoreboard.GameScoreboard;
import com.podcrash.api.scoreboard.CustomScoreboard;
import com.podcrash.api.scoreboard.ScoreboardInput;
import me.flaymed.islands.game.GameStage;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PrepareInput extends ScoreboardInput {
    private final Game game;
    private long endTime;

    public PrepareInput(Game game, CustomScoreboard scoreboard) {
        super(scoreboard);
        this.game = game;
    }

    public void setUp(long endTime) {
        this.endTime = endTime;
    }
    @Override
    public void update() {

        List<String> lines = new ArrayList<>();
        lines.add(ChatColor.LIGHT_PURPLE + "Islands");
        lines.add("");
        for (GTeam team : game.getTeams()) {
            lines.add(fancyTeamHeader(team.getTeamEnum()));
            lines.add(team.teamSize() + " Alive");
            lines.add("");
        }
        lines.add(ChatColor.BOLD + "Bridges in:");
        long currentTimeMillis = System.currentTimeMillis();
        long timeLeft = endTime - currentTimeMillis;

        int minutes = (int) ((timeLeft / 1000D) / 60D);
        int seconds = (int) ((timeLeft / 1000D) % 60);
        lines.add(formatTime(minutes, seconds));
        setLines(lines);
    }

    private String fancyTeamHeader(TeamEnum teamEnum) {
        return teamEnum.getChatColor() + teamEnum.getName() + " Team";
    }

    private String formatTime(int minutes, int seconds) {
        StringBuilder builder = new StringBuilder(Integer.toString(minutes));
        builder.append(":");
        if (seconds < 10L)
            builder.append(0);
        builder.append(seconds);

        return builder.toString();

    }
    @Override
    public boolean cancel() {
        return game.getGameState() == GameState.LOBBY;
    }
}
