package me.flaymed.islands.kits.classes;

import me.flaymed.islands.kits.IslandsPlayer;
import me.flaymed.islands.kits.skills.brawler.Behemoth;
import org.bukkit.entity.Player;

public class Brawler extends IslandsPlayer {
    public Brawler(Player player) {
        super(player, "Brawler", Behemoth.class);
    }
}
