package me.flaymed.islands.kits.skills.berserker;

import com.podcrash.gamecore.GameCore;
import com.podcrash.gamecore.kits.abilitytype.ChargedAbility;
import com.podcrash.gamecore.kits.abilitytype.Cooldown;
import com.podcrash.gamecore.kits.abilitytype.Interact;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import java.util.Arrays;
import java.util.List;

public class Leap extends ChargedAbility implements Cooldown, Interact {

    private final int MAX_CHARGES = 8;
    private int charges = MAX_CHARGES;
    private int hits = 0;

    @EventHandler
    public void playerHit(EntityDamageByEntityEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        if (!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) return;
        if (((Player) e.getDamager()).getPlayer().getUniqueId() != getKitPlayer().getPlayer().getUniqueId()) return;
        hits++;
        if(hits >= 3) {
            addCharge();
            hits = 0;
        }
    }

    @Override
    public String getName() {
        return "Berserker Leap";
    }

    @Override
    public ItemStack getItem() {
        return null;
    }

    @Override
    public void doAbility() {
        Player player = getKitPlayer().getPlayer();

        if (getCurrentCharges() == 0) {
            player.sendMessage(String.format("%s You have used all of your available leaps!", GameCore.getKitPrefix()));
            return;
        }
        Material m = player.getLocation().getBlock().getType();
        Vector v;

        if (m.equals(Material.STATIONARY_WATER) || m.equals(Material.WATER)) v = player.getLocation().getDirection().multiply(1.75);
        else v = player.getLocation().getDirection().multiply(1.5);

        player.setVelocity(v);
        getKitPlayer().getPlayer().setFallDistance(0);
        player.getWorld().playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 1, 1);
        charges--;

        player.sendMessage(getCurrentChargeMessage());
    }

    @Override
    public List<Action> getActions() {
        return Arrays.asList(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR);
    }

    @Override
    public void addCharge() {
        charges++;
        if (charges > MAX_CHARGES) {
            charges = MAX_CHARGES;
        }
        getKitPlayer().getPlayer().sendMessage((getCurrentChargeMessage()));
    }

    @Override
    public void removeCharge() {
        if (getCurrentCharges() == 0) return;
        if (getCurrentCharges() < 0) {
            charges = 0;
            return;
        }
        charges--;
    }

    @Override
    public String getChargeName() {
        return "Leap";
    }

    @Override
    public boolean startsWithMaxCharges() {
        return true;
    }

    @Override
    public int getSecondsBetweenCharge() {
        return 0;
    }

    @Override
    public int getCurrentCharges() {
        return charges;
    }

    @Override
    public int getMaxCharges() {
        return 8;
    }

    @Override
    public boolean passivelyGainCharges() {
        return false;
    }

    @Override
    public float getCooldown() {
        return 14;
    }
}
