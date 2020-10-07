package me.flaymed.islands.kits.classes;

import com.podcrash.gamecore.kits.Kit;
import me.flaymed.islands.kits.skills.archer.Barrage;
import me.flaymed.islands.kits.skills.archer.SkillSelect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import java.util.List;

public class Archer extends Kit {

    public Archer() {
        //Only add SkillSelect and Barrage because other abilities are added through SkillSelect.
        super("Archer", Barrage.class, SkillSelect.class);
    }

    @Override
    public String getPermission() {
        return "islands.archer";
    }

    //TODO: description
    @Override
    public List<String> getDescription() {
        return null;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.BOW);
    }

    @Override
    public ItemStack[] getArmor() {
        return new ItemStack[0];
    }

    @Override
    public ItemStack getWeapon() {
        return null;
    }
}
