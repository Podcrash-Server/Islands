package me.flaymed.islands.kits.classes;

import com.podcrash.gamecore.kits.Kit;
import me.flaymed.islands.kits.skills.alchemist.Rush;
import me.flaymed.islands.kits.skills.alchemist.PeaceKeeper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import java.util.List;

public class Alchemist extends Kit {

    public Alchemist() {
        super("Alchemist", PeaceKeeper.class, Rush.class);
    }

    @Override
    public String getPermission() {
        return "islands.alchemist";
    }

    @Override
    public List<String> getDescription() {
        return null;
    }

    @Override
    public ItemStack getItem() {
        return new Potion(PotionType.INSTANT_HEAL, 2).toItemStack(1);
    }

    @Override
    public ItemStack[] getArmor() {
        return new ItemStack[0];
    }

    @Override
    public ItemStack getWeapon() {
        return new Potion(PotionType.INSTANT_HEAL, 2).toItemStack(1);
    }
}
