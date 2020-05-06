package me.flaymed.islands.kits.classes;

import me.flaymed.islands.kits.IslandsPlayer;
import me.flaymed.islands.kits.skills.medic.HealingAura;
import me.flaymed.islands.kits.skills.medic.PeaceKeeper;
import org.bukkit.entity.Player;

public class Medic extends IslandsPlayer {
    public Medic(Player player) {
        super(player, "Medic", PeaceKeeper.class, HealingAura.class);
    }
}
