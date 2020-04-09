package me.flaymed.islands.kits.classes;

import com.podcrash.api.mc.sound.SoundWrapper;
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
        setSound(new SoundWrapper("ambient.cave.cave", 0.95f, 115));
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


}
