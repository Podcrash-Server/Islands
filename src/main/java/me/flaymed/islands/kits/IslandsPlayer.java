package me.flaymed.islands.kits;

import com.podcrash.gamecore.kits.KitPlayer;
import me.flaymed.islands.Islands;
import me.flaymed.islands.game.GameStage;
import me.flaymed.islands.kits.classes.LobbyKit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class IslandsPlayer extends KitPlayer {

    private int deaths;

    public IslandsPlayer(Player player) {
        super(player);
        this.deaths = 0;
    }

    public void addDeath() {
        this.deaths++;
    }

    public boolean canRevive() {
        return deaths >= 1 && Islands.getInstance().getGame().getStage() == GameStage.PREPARE;
    }

    @Override
    public void equip() {
        getPlayer().getInventory().clear();
        getPlayer().getEquipment().clear();
        if (getActiveKit() == null) return;
        if (getActiveKit().getClass().getSimpleName() == LobbyKit.class.getSimpleName() && Islands.getInstance().getGame().getStage() == GameStage.LOBBY) {
            ItemStack kitSelect = getActiveKit().getAbilities().get(0).getItem();
            ItemStack teamSelect = getActiveKit().getAbilities().get(1).getItem();

            getPlayer().getInventory().setItem(3, kitSelect);
            getPlayer().getInventory().setItem(5, teamSelect);
            getPlayer().updateInventory();
            return;
        }
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
