package me.flaymed.islands.kits.classes;

import me.flaymed.islands.kits.IslandsPlayer;
import me.flaymed.islands.kits.skills.archer.Barrage;
import me.flaymed.islands.kits.skills.archer.*;
import org.bukkit.entity.Player;

public class Archer extends IslandsPlayer {
    public Archer(Player player) {
        super(player, "Archer", Barrage.class, SkillSelect.class);
    }
}
