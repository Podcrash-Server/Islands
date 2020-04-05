package me.flaymed.islands.game.map;

import org.bukkit.util.Vector;

import java.util.List;

public interface IslandsMap {
    List<Vector> getRedSpawn();
    List<Vector> getBlueSpawn();
    List<Vector> getYellowSpawn();
    List<Vector> getGreenSpawn();
}
