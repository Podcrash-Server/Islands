package me.flaymed.islands.game.scoreboard;

import com.podcrash.api.game.GTeam;
import com.podcrash.api.game.Game;
import com.podcrash.api.game.GameState;
import com.podcrash.api.game.TeamEnum;
import com.podcrash.api.scoreboard.CustomScoreboard;
import com.podcrash.api.scoreboard.ScoreboardInput;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class GameTimerInput extends ScoreboardInput {
    private final Game game;
    private long startTime;

    public GameTimerInput(Game game, CustomScoreboard scoreboard) {
        super(scoreboard);
        this.game = game;
    }

    public void setUp() {
        this.startTime = System.currentTimeMillis();
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
        long timeLeft = currentTimeMillis - startTime;

        int minutes = (int) ((timeLeft / 1000D) / 60D);
        int seconds = (int) ((timeLeft / 1000D) % 60);
        lines.add(formatTime(minutes, seconds));
        setLines(lines);
    }

    private String fancyTeamHeader(TeamEnum teamEnum) {
        return teamEnum.getChatColor() + teamEnum.getName() + " Team";
    }

    private String formatTime(int minutes, int seconds) {
        StringBuilder builder = new StringBuilder(minutes);
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
