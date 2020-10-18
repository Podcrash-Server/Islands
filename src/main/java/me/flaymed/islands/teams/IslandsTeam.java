package me.flaymed.islands.teams;

import com.podcrash.gamecore.game.GameTeam;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.List;

public class IslandsTeam extends GameTeam {

    //TODO: World parsing stuff
    List<Location> spawnPointLocations;
    HashMap<Player, Location> playerSpawnPoints;

    public IslandsTeam(List<Player> players, List<Location> spawnPointLocations) {
        super(players);

        this.spawnPointLocations = spawnPointLocations;
        this.playerSpawnPoints = new HashMap<>();
    }
}
