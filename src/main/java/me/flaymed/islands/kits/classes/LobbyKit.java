package me.flaymed.islands.kits.classes;

import com.podcrash.gamecore.kits.Kit;
import me.flaymed.islands.kits.skills.lobbykit.KitSelect;
import me.flaymed.islands.kits.skills.lobbykit.TeamSelect;
import org.bukkit.inventory.ItemStack;

import java.util.List;

//This is the base kit that all islands players get.
public class LobbyKit extends Kit {
    public LobbyKit() {
        super("Lobby", KitSelect.class, TeamSelect.class);
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public List<String> getDescription() {
        return null;
    }

    @Override
    public ItemStack getItem() {
        return null;
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
