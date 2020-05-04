package me.flaymed.islands.kits.classes;

import me.flaymed.islands.kits.IslandsPlayer;
import me.flaymed.islands.kits.skills.miner.OreScan;
import org.bukkit.entity.Player;

public class Miner extends IslandsPlayer {
    public Miner(Player player) {
        super(player, "Miner", OreScan.class);
    }

}
