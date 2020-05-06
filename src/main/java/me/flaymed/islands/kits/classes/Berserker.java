package me.flaymed.islands.kits.classes;

import me.flaymed.islands.kits.IslandsPlayer;
import me.flaymed.islands.kits.skills.berserker.Leap;
import me.flaymed.islands.kits.skills.berserker.Takedown;
import org.bukkit.entity.Player;

public class Berserker extends IslandsPlayer {
    public Berserker(Player player) {
        super(player, "Berserker", Leap.class, Takedown.class);
    }
}
