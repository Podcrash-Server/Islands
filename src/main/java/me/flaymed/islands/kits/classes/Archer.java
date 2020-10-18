package me.flaymed.islands.kits.classes;

import com.podcrash.gamecore.kits.Kit;
import me.flaymed.islands.kits.skills.archer.Barrage;
import me.flaymed.islands.kits.skills.archer.RopedArrow;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import java.util.List;

public class Archer extends Kit {

    public Archer() {
        //Add SkillSelect later, going with just barrage and roped arrow for now.
        super("Archer", Barrage.class, RopedArrow.class);
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
