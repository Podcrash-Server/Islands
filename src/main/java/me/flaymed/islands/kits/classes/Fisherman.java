package me.flaymed.islands.kits.classes;

import com.podcrash.api.mc.sound.SoundWrapper;
import me.flaymed.islands.kits.IslandsPlayer;
import me.flaymed.islands.kits.Skill;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Fisherman extends IslandsPlayer {

    private List<Skill> skills;

    public Fisherman(Player player, List<Skill> skills) {
        super(player);
        this.skills = skills;
    }

    @Override
    public void equip() {
        getPlayer().getInventory().setItem(0, new ItemStack(Material.FISHING_ROD));
    }

    @Override
    public void applyEffects() {

    }

    @Override
    public int getHP() {
        return 20;
    }


}
