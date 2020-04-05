package me.flaymed.islands.kits.classes;

import com.podcrash.api.mc.sound.SoundWrapper;
import me.flaymed.islands.kits.IslandsPlayer;
import me.flaymed.islands.kits.skills.Skill;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Fisherman extends IslandsPlayer {

    private List<Skill> skills;

    public Fisherman(Player player, List<Skill> skills) {
        super(player);
        this.skills = skills;
        setSound(new SoundWrapper("ambient.weather.rain", 0.95f, 115));
    }

    @Override
    public void equip() {
        getPlayer().getInventory().setItem(0, new ItemStack(Material.FISHING_ROD));
    }

    @Override
    public int getHP() {
        return 20;
    }
}
