package me.flaymed.islands.tracker;


import org.bukkit.entity.Player;

public interface IPlayerTrack<T> extends Tracker {
    T get(Player player);
}
