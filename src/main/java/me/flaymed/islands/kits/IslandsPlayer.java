package me.flaymed.islands.kits;

import com.podcrash.api.mc.game.GameManager;
import com.podcrash.api.mc.sound.SoundWrapper;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class IslandsPlayer {

    private Player player;
    private double fallDamage = 0;
    private SoundWrapper sound; //sound when player gets hit

    public IslandsPlayer (Player player) {
        this.player = player;
    }

    public abstract int getHP();
    public abstract void equip();

    public boolean isInGame() {
        return GameManager.hasPlayer(this.player);
    }

    public Player getPlayer() {
        return player;
    }
    public CraftPlayer getCraftPlayer() {
        return (CraftPlayer) player;
    }
    public EntityPlayer getEntityCraftPlayer() {
        return this.getCraftPlayer().getHandle();
    }

    public ItemStack[] getArmor() {
        return this.player.getEquipment().getArmorContents();
    }
    public double getArmorValue(double halfHearts) {
        return (halfHearts - getHP())/(-0.04*getHP());
    }
    public double getArmorValue() {
        return getArmorValue(20);
    }
    public Inventory getInventory() {
        return this.player.getInventory();
    }

    public double getFallDamage() {
        return fallDamage;
    }
    public void setFallDamage(double fallDamage) {
        this.fallDamage = fallDamage;
    }

    public SoundWrapper getSound() {
        return sound;
    }
    public void setSound(SoundWrapper sound) {
        this.sound = sound;
    }

}
