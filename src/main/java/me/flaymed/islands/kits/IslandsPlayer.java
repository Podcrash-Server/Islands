package me.flaymed.islands.kits;

import com.podcrash.gamecore.kits.KitPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class IslandsPlayer extends KitPlayer {

    public IslandsPlayer(Player player) {
        super(player);
    }

    @Override
    public void equip() {
        getPlayer().getInventory().clear();
        getPlayer().getEquipment().clear();
        if (getActiveKit() == null) return;
        getPlayer().getEquipment().setArmorContents(getActiveKit().getArmor());

        String weaponName = getActiveKit().getWeapon().getType().toString().toLowerCase();
        if (weaponName.contains("axe") || weaponName.contains("sword")) getPlayer().getInventory().setItem(0, getActiveKit().getWeapon());

        if (getActiveKit().getItem().getType() != getActiveKit().getWeapon().getType() && !itemIsArmor(getActiveKit().getItem())) {
            getPlayer().getInventory().setItem(1, new ItemStack(getActiveKit().getItem().getType(), 1));
            getPlayer().getInventory().setItem(2, new ItemStack(Material.COMPASS, 1));
        } else {
            getPlayer().getInventory().setItem(1, new ItemStack(Material.COMPASS, 1));
        }

        getPlayer().updateInventory();
    }


}
