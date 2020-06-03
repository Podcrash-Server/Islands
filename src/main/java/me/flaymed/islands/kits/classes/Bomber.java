package me.flaymed.islands.kits.classes;

import com.podcrash.api.kits.Skill;
import me.flaymed.islands.kits.IslandsPlayer;
import me.flaymed.islands.kits.skills.bomber.ThrowBomb;
import org.bukkit.entity.Player;

public class Bomber extends IslandsPlayer {
    public Bomber(Player player) {
        super(player, "Bomber", ThrowBomb.class);
    }
}
