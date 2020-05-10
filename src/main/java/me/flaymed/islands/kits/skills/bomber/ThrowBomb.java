package me.flaymed.islands.kits.skills.bomber;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.packetwrapper.abstractpackets.WrapperPlayServerWorldParticles;
import com.podcrash.api.effect.particle.ParticleGenerator;
import com.podcrash.api.game.GTeam;
import com.podcrash.api.game.GameManager;
import com.podcrash.api.game.TeamEnum;
import com.podcrash.api.kits.enums.ItemType;
import com.podcrash.api.kits.iskilltypes.action.ICharge;
import com.podcrash.api.kits.iskilltypes.action.IPassiveTimer;
import com.podcrash.api.kits.skilltypes.Passive;
import com.podcrash.api.time.TimeHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ThrowBomb extends Passive implements ICharge, IPassiveTimer {

    private int MAX_TNT = 2;
    private int tntCount = 0;

    @Override
    public void start() {
        if (getPlayer() != null) TimeHandler.repeatedTimeAsync(20L, 0L, this);
    }

    @Override
    public void addCharge() {

        if (tntCount >= MAX_TNT) {
            tntCount = MAX_TNT;
        } else {
            tntCount++;
            getPlayer().getInventory().addItem(new ItemStack(Material.TNT, 1));
            getPlayer().updateInventory();
        }
    }

    @Override
    public int getCurrentCharges() {
        return tntCount;
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
        return ItemType.TNT;
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
        tntCount = 0;
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent e) {
        if (getPlayer() != e.getPlayer()) return;

        if (tntCount > 0) {
            if (getPlayer().getItemInHand().getType().equals(Material.TNT)) {
                getPlayer().getInventory().remove(new ItemStack(Material.TNT, 1));
                getPlayer().updateInventory();
                tntCount--;


                Vector tntV = getPlayer().getLocation().getDirection().multiply(0.8);
                Entity tnt = getPlayer().getWorld().spawnEntity(getPlayer().getLocation(), EntityType.PRIMED_TNT);
                tnt.setVelocity(tntV);

                GTeam team = GameManager.getGame().getTeam(getPlayer());
                TeamEnum teamEnum = team.getTeamEnum();

                Vector v = new Location(getPlayer().getWorld(), getPlayer().getLocation().getBlockX(), getPlayer().getLocation().getBlockY() + 2, getPlayer().getLocation().getBlockZ()).getDirection().multiply(0.8);


                WrapperPlayServerWorldParticles particle = ParticleGenerator.createParticle(v,
                        EnumWrappers.Particle.REDSTONE,1,
                        teamEnum.getRed()/255, teamEnum.getGreen()/255, teamEnum.getBlue()/255);
                particle.setParticleData(1);


                getPlayer().sendMessage(getUsedMessage());
            }
        } else {
            getPlayer().sendMessage(getNoChargeMessage());
        }
    }

}
