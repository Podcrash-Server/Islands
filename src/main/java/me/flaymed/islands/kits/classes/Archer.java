package me.flaymed.islands.kits.classes;

import com.podcrash.api.kits.Skill;
import me.flaymed.islands.kits.IslandsPlayer;
import me.flaymed.islands.kits.skills.archer.Barrage;
import me.flaymed.islands.kits.skills.archer.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Archer extends IslandsPlayer {
    //I could argue removing the Barrage part?
    //it will still work though with or without
    //just one less thing to register
    public Archer(Player player) {
        super(player, "Archer", Barrage.class, SkillSelect.class);
    }

    public void addSkill(Skill skill) {
        this.skills.add(skill);
    }
    public Archer(Player player, Class<? extends Skill>... skills) {
        super(player, "Archer", skills);
        ItemStack air = new ItemStack(Material.AIR);
        setDefaultHotbar(new ItemStack[] {new ItemStack(Material.BOW), air.clone(), air.clone(), air.clone(), air.clone(), air.clone(), air.clone(), air.clone(), air.clone()});
    }
}
