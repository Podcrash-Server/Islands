package me.flaymed.islands.kits.skills.bomber;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.packetwrapper.abstractpackets.ILocationPacket;
import com.packetwrapper.abstractpackets.WrapperPlayServerWorldParticles;
import com.podcrash.gamecore.kits.abilitytype.ChargedAbility;
import com.podcrash.gamecore.kits.abilitytype.Interact;
import me.flaymed.islands.Islands;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ThrowBomb extends ChargedAbility implements Interact {
    private final HashMap<Integer, Integer> bomberMap = new HashMap<>();
    private final int MAX_TNT = 3;

    @Override
    public void addCharge() {

        if (getTNTCount() >= MAX_TNT) {

        } else {
            getKitPlayer().getPlayer().getInventory().addItem(new ItemStack(Material.TNT, 1));
            getKitPlayer().getPlayer().updateInventory();
        }
    }

    @Override
    public void removeCharge() {

    }

    @Override
    public String getChargeName() {
        return null;
    }

    @Override
    public boolean startsWithMaxCharges() {
        return false;
    }

    @Override
    public int getSecondsBetweenCharge() {
        return 20;
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
    public boolean passivelyGainCharges() {
        return false;
    }

    @Override
    public String getName() {
        return "TNT Throw";
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.TNT);
    }

    @Override
    public void doAbility() {

        if (getTNTCount() == 0) {
            getKitPlayer().getPlayer().sendMessage(getNoChargesMessage());
            return;
        }

        removeItemFromHand(getKitPlayer().getPlayer());
        getKitPlayer().getPlayer().updateInventory();


        Vector tntV = getKitPlayer().getPlayer().getLocation().getDirection().multiply(0.8);
        TNTPrimed tnt = (TNTPrimed) getKitPlayer().getPlayer().getWorld().spawnEntity(getKitPlayer().getPlayer().getLocation().add(0, 1, 0), EntityType.PRIMED_TNT);
        tnt.setVelocity(tntV);


        //TODO team stuff

        float[] RGB = new float[] {
                teamEnum.getRed()/255F - 1F,
                teamEnum.getGreen()/255F,
                teamEnum.getBlue()/255F
        };

        WrapperPlayServerWorldParticles particle = createParticle(tnt.getLocation().toVector(),
                EnumWrappers.Particle.REDSTONE, new int[]{}, 0,
                RGB[0], RGB[1], RGB[2]);

        particle.setParticleData(1F);

        int taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(Islands.getInstance(), () -> {
            for (Player player: Bukkit.getOnlinePlayers()) {
                particle.sendPacket(player);
            }
        }, 10, 10);

        bomberMap.put(tnt.getEntityId(), taskid);
        getKitPlayer().getPlayer().sendMessage(getUsedMessage());
    }

    @Override
    public List<Action> getActions() {
        return Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK, Action.LEFT_CLICK_BLOCK, Action.LEFT_CLICK_AIR);
    }

    private int getTNTCount() {
        int i = 0;
        for (ItemStack stack : getKitPlayer().getPlayer().getInventory()) {
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
        if (!bomberMap.containsKey(e.getDamager().getEntityId())) return;

        e.setCancelled(true);
        double dmg = e.getDamage();
        ((LivingEntity) e.getEntity()).damage(dmg);
        int taskid = bomberMap.get(e.getDamager().getEntityId());
        Bukkit.getScheduler().cancelTask(taskid);
    }

    @EventHandler
    public void explode(EntityExplodeEvent e) {
        if (!bomberMap.containsKey(e.getEntity().getEntityId())) return;
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
            Bukkit.getScheduler().scheduleSyncDelayedTask(Islands.getInstance(), () -> inventory.clear(slot), 1);
        }
        player.updateInventory();
    }

    public WrapperPlayServerWorldParticles createParticle(Vector vector, EnumWrappers.Particle particle, int[] data, int particleCount, float offsetX, float offsetY, float offsetZ) {
        if (vector == null)
            vector = new Vector(0, 0,0);
        WrapperPlayServerWorldParticles packet = new WrapperPlayServerWorldParticles();
        packet.setParticleType(particle);
        packet.setX((float) vector.getX());
        packet.setY((float) vector.getY());
        packet.setZ((float) vector.getZ());
        packet.setNumberOfParticles(particleCount);
        packet.setOffsetX(offsetX);
        packet.setOffsetY(offsetY);
        packet.setOffsetZ(offsetZ);
        packet.setData(data);
        return packet;
    }

}
