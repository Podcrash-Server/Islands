package me.flaymed.islands.kits.classes;

import com.podcrash.gamecore.kits.Ability;
import com.podcrash.gamecore.kits.Kit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import java.util.List;

public class Brawler extends Kit {

    public Brawler(Class<? extends Ability>... abilities) {
        super("Brawler", abilities);
    }

    @Override
    public String getPermission() {
        return "islands.brawler";
    }

    @Override
    public List<String> getDescription() {
        return null;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.IRON_SWORD);
    }

    @Override
    public ItemStack[] getArmor() {
        return new ItemStack[0];
    }

    @Override
    public ItemStack getWeapon() {
        return new ItemStack(Material.IRON_SWORD);
    }
}
