package me.flaymed.islands.game.scoreboard;

import com.podcrash.api.mc.game.GTeam;
import com.podcrash.api.mc.game.GameType;
import com.podcrash.api.mc.game.TeamEnum;
import com.podcrash.api.mc.game.scoreboard.GameScoreboard;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class IslandsScoreBoard extends GameScoreboard {
    /**
     * Constructor for the Game Scoreboard.
     *
     * @param gameId The Game ID.
     */
    public IslandsScoreBoard(int gameId) {
        super(17, gameId, GameType.valueOf("IS"));
    }

    @Override
    public void update() {
        for (GTeam team : getGame().getTeams()) updateTeamCount(team);
    }

    @Override
    public void startScoreboardTimer() {

    }

    public void updateTeamCount(GTeam team) {

        List<String> lines = getLines();

        String lowerTeam = (team.getTeamEnum().getChatColor() + "" + ChatColor.BOLD + team.getName()).toLowerCase();
        int teamSize = team.getPlayers().size();
        for (int i = 0; i < lines.size(); i++) {

            String line = lines.get(i);
            if (!line.toLowerCase().contains(lowerTeam)) continue;
            if (teamSize == 0) {
                lines.remove(i);
                lines.remove(i + 1);
            }
            setLine(i + 1, String.format(team.getTeamEnum().getChatColor() + "%s", teamSize));


        }

    }


}
