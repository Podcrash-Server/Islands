package me.flaymed.islands;

import com.comphenix.protocol.ProtocolLibrary;
import com.podcrash.gamecore.game.GameTeam;
import me.flaymed.islands.commands.OreSettingCommand;
import me.flaymed.islands.commands.DropBridgeCommand;
import me.flaymed.islands.game.IslandsGame;
import me.flaymed.islands.listeners.*;
import me.flaymed.islands.teams.IslandsTeamSide;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Islands extends JavaPlugin {
    private static Islands INSTANCE;
    private List<GameTeam> teams;
    private IslandsGame game;

    @Override
    public void onEnable() {
        INSTANCE = this;


        game = new IslandsGame();

        registerCommands();
        registerListeners();
        createTeams();
    }

    public void registerListeners() {
        new InventoryListener(this);
        new IslandsJoinListener(this);
        new KitApplyListener(this);
        new IslandsGameListener(this);
        new IslandsPlayerRestrict(this);
        new CompassTrackerListener(this);
        new SoupListener(this);
    }

    private void createTeams() {
        IslandsTeamSide redSide = new IslandsTeamSide("Red Team", ChatColor.RED, 0);
        GameTeam redTeam = new GameTeam(new ArrayList<>());
        redTeam.setSide(redSide);

        IslandsTeamSide blueSide = new IslandsTeamSide("Blue Team", ChatColor.BLUE, 1);
        GameTeam blueTeam = new GameTeam(new ArrayList<>());
        blueTeam.setSide(blueSide);

        IslandsTeamSide greenSide = new IslandsTeamSide("Green Team", ChatColor.GREEN, 2);
        GameTeam greenTeam = new GameTeam(new ArrayList<>());
        greenTeam.setSide(greenSide);

        IslandsTeamSide yellowSide = new IslandsTeamSide("Yellow Team", ChatColor.YELLOW, 3);
        GameTeam yellowTeam = new GameTeam(new ArrayList<>());
        yellowTeam.setSide(yellowSide);

        this.teams = Arrays.asList(redTeam, blueTeam, greenTeam, yellowTeam);
    }

    private void registerCommands() {
        getCommand("ore").setExecutor(new OreSettingCommand());
        getCommand("bridges").setExecutor(new DropBridgeCommand());
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelAllTasks();
        ProtocolLibrary.getProtocolManager().removePacketListeners(this);
    }

    public IslandsGame getGame() {
        return game; //no npe check because you'll never need to get game instance before plugin is enabled.
    }

    public static Islands getInstance() {
        return INSTANCE;
    }
}
