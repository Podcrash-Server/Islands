package me.flaymed.islands.kits.classes;

import me.flaymed.islands.kits.IslandsPlayer;
import me.flaymed.islands.kits.Skill;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Miner extends IslandsPlayer {

    private List<Skill> skills;

    public Miner (Player player, List<Skill> skills) {
        super(player);
        this.skills = skills;
    }

    @Override
    public int getHP() {
        return 20;
    }

    @Override
    public void equip() {
        getPlayer().getInventory().setItem(0, new ItemStack(Material.STONE_PICKAXE));
    }

    @Override
    public void applyEffects() {

    }


}
