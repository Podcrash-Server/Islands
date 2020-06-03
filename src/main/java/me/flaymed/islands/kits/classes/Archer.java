package me.flaymed.islands.kits.classes;

import com.podcrash.api.kits.Skill;
import me.flaymed.islands.kits.IslandsPlayer;
import me.flaymed.islands.kits.skills.archer.Barrage;
import me.flaymed.islands.kits.skills.archer.*;
import org.bukkit.entity.Player;

public class Archer extends IslandsPlayer {
    //I could argue removing the Barrage part?
    //it will still work though with or without
    //just one less thing to register
    public Archer(Player player) {
        super(player, "Archer", Barrage.class, SkillSelect.class);
    }

    public Archer(Player player, Class<? extends Skill>... skills) {
        super(player, "Archer", skills);
    }
}
