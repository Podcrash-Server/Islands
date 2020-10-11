package me.flaymed.islands.teams;

import com.podcrash.gamecore.game.GameSide;
import org.bukkit.ChatColor;

public class IslandsTeamSide implements GameSide {

    private String displayName;
    private ChatColor color;
    private int side;

    public IslandsTeamSide(String displayName, ChatColor color, int side) {
        this.displayName = displayName;
        this.color = color;
        this.side = side;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public ChatColor getColor() {
        return color;
    }

    @Override
    public int side() {
        return side;
    }
}
