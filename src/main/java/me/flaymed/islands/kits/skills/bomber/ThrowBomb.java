package me.flaymed.islands.kits.skills.bomber;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.packetwrapper.abstractpackets.WrapperPlayServerWorldParticles;
import com.podcrash.api.damage.DamageApplier;
import com.podcrash.api.effect.particle.ParticleGenerator;
import com.podcrash.api.game.GTeam;
import com.podcrash.api.game.GameManager;
import com.podcrash.api.game.TeamEnum;
import com.podcrash.api.kits.enums.ItemType;
import com.podcrash.api.kits.iskilltypes.action.ICharge;
import com.podcrash.api.kits.iskilltypes.action.IPassiveTimer;
import com.podcrash.api.kits.skilltypes.Passive;
import com.podcrash.api.plugin.PodcrashSpigot;
import com.podcrash.api.time.TimeHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ThrowBomb extends Passive implements ICharge, IPassiveTimer {
    private final Set<Integer> bomberMap = new HashSet<>();
    private final int MAX_TNT = 2;

    @Override
    public void start() {
        if (getPlayer() != null) TimeHandler.repeatedTimeAsync(/*30L * 20L*/20L, 0L, this);
    }

    @Override
    public void addCharge() {

        if (getTNTCount() >= MAX_TNT) {

        } else {
            getPlayer().getInventory().addItem(new ItemStack(Material.TNT, 1));
            getPlayer().updateInventory();
        }
    }

    @Override
    public int getCurrentCharges() {
        return getTNTCount();
    }

    @Override
    public int getMaxCharges() {
        return MAX_TNT;
    }

    @Override
    public String getName() {
        return "TNT Throw";
    }

    @Override
    public ItemType getItemType() {
        return null;
    }

    @Override
    public void task() {
        addCharge();
    }

    @Override
    public boolean cancel() {
        return false;
    }

    @Override
    public void cleanup() {
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent e) {
        if (getPlayer() != e.getPlayer()) return;

        if (!getPlayer().getItemInHand().getType().equals(Material.TNT))
            return;
        if (getTNTCount() == 0) {
            getPlayer().sendMessage(getNoChargeMessage());
            return;
        }

        e.setCancelled(true);
        removeItemFromHand(getPlayer());
        getPlayer().updateInventory();


        Vector tntV = getPlayer().getLocation().getDirection().multiply(0.8);
        TNTPrimed tnt = (TNTPrimed) getPlayer().getWorld().spawnEntity(getPlayer().getLocation().add(0, 1, 0), EntityType.PRIMED_TNT);
        tnt.setVelocity(tntV);
        bomberMap.add(tnt.getEntityId());

        GTeam team = GameManager.getGame().getTeam(getPlayer());
        TeamEnum teamEnum = team.getTeamEnum();

        float[] RGB = new float[] {
            teamEnum.getRed()/255F - 1F,
            teamEnum.getGreen()/255F,
            teamEnum.getBlue()/255F
        };
        PodcrashSpigot.debugLog(Arrays.toString(RGB));
        WrapperPlayServerWorldParticles particle = ParticleGenerator.createParticle(tnt.getLocation().toVector(),
                EnumWrappers.Particle.REDSTONE, new int[]{}, 0,
                RGB[0], RGB[1], RGB[2]);

        particle.setParticleData(1F);
        ParticleGenerator.generateEntity(tnt, particle, null);
        PodcrashSpigot.debugLog(particle.toString());


        getPlayer().sendMessage(getUsedMessage());
    }

    private int getTNTCount() {
        int i = 0;
        for (ItemStack stack : getPlayer().getInventory()) {
            if (stack == null) continue;
            boolean isTNT = stack.getType() == Material.TNT;
            if (!isTNT) continue;
            i += stack.getAmount();
        }
        return i;
    }
    @EventHandler
    public void explode(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;
        if (!bomberMap.contains(e.getDamager().getEntityId())) return;

        e.setCancelled(true);
        double dmg = e.getDamage();
        PodcrashSpigot.debugLog("destruction");
        DamageApplier.damage((LivingEntity) e.getEntity(), getPlayer(), dmg, this, false);
    }

    @EventHandler
    public void explode(EntityExplodeEvent e) {
        if (!bomberMap.contains(e.getEntity().getEntityId())) return;
        e.setYield(1.0F);
    }

    private void removeItemFromHand(Player player) {
        ItemStack item = player.getItemInHand();
        int slot = player.getInventory().getHeldItemSlot();
        int amnt = item.getAmount();
        if(amnt > 1) {
            item.setAmount(amnt - 1);
        }else {
            PlayerInventory inventory = player.getInventory();
            Bukkit.getScheduler().scheduleSyncDelayedTask(PodcrashSpigot.getInstance(), () -> inventory.clear(slot), 1);
        }
        player.updateInventory();
    }

}
