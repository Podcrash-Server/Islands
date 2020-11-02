package me.flaymed.islands.teams;

import com.podcrash.gamecore.game.GameTeam;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IslandsTeam extends GameTeam {

    //TODO: World parsing stuff
    int MAX_PLAYERS = 12;
    ChatColor color;
    ItemStack item;
    List<Location> spawnPointLocations;
    HashMap<Player, Location> playerSpawnPoints;

    public IslandsTeam(ItemStack item, ChatColor color) {
        super (new ArrayList<>());

        this.item = item;
        this.color = color;
        this.spawnPointLocations = new ArrayList<>();
        this.playerSpawnPoints = new HashMap<>();
    }

    public void addSpawnPointLocation(Location loc) {
        this.spawnPointLocations.add(loc);
    }

    public List<Location> getSpawnPointLocations() {
        return spawnPointLocations;
    }

    public void addPlayerSpawnPoint(Player p, Location loc) {
        this.playerSpawnPoints.put(p, loc);
    }

    public HashMap<Player, Location> getPlayerSpawnPoints() {
        return playerSpawnPoints;
    }

    public ChatColor getColor() {
        return color;
    }

    public ItemStack getItem() {
        return item;
    }

    public boolean isFull() {
        return size() >= MAX_PLAYERS;
    }

}
