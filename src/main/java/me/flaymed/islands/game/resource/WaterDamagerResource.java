package me.flaymed.islands.game.resource;

import com.packetwrapper.abstractpackets.WrapperPlayServerEntityStatus;
import com.podcrash.api.damage.DamageApplier;
import com.podcrash.api.damage.DamageQueue;
import com.podcrash.api.events.DamageApplyEvent;
import com.podcrash.api.events.DropDeathLootEvent;
import com.podcrash.api.game.resources.GameResource;
import com.podcrash.api.plugin.PodcrashSpigot;
import com.podcrash.api.sound.SoundPlayer;
import com.podcrash.api.time.resources.TimedTask;
import com.podcrash.api.util.PacketUtil;
import com.podcrash.api.util.ReflectionUtil;
import me.flaymed.islands.game.IslandsGame;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WaterDamagerResource extends GameResource {
    private final TimedTask waterTask;

    public WaterDamagerResource(int gameID) {
        super(gameID);

        this.waterTask = new TimedTask(1000) {
            @Override
            public void action() {
                game.consumeBukkitPlayer(WaterDamagerResource.this::waterDmg);
            }
        };
        this.waterTask.setAsync(false);
    }

    private void waterDmg(Player player) {
        boolean isInWater = player.getLocation().getBlock().getType() == Material.WATER || player.getLocation().getBlock().getType() == Material.STATIONARY_WATER;
        if (!isInWater)
            return;
        if (game.isRespawning(player))
            return;
        final double damage = 1;
        DamageQueue.artificialAddHistory(player, damage, IslandsGame.WATER_DAMAGE);
        damage(player, damage);
    }

    /**
     * TODO: make a util method because it copies from MapMaintainListener#damage
     * Send animation packets as well as sound
     * (For some reason, the animation packet is also supposed to send sound as well
     * but it doesn't?)
     * @param victim The player that is taking the damage.
     * @param damage How much damage the player must take
     */
    private boolean damage(LivingEntity victim, double damage) {

        double health = victim.getHealth() - damage;
        boolean isDead = (health <= 0);
        if (isDead)
            fakeDie((Player) victim);
        victim.setHealth(isDead ? 20 : health);

        WrapperPlayServerEntityStatus packet = new WrapperPlayServerEntityStatus();
        packet.setEntityId(victim.getEntityId());
        packet.setEntityStatus(WrapperPlayServerEntityStatus.Status.ENTITY_HURT);

        PacketUtil.syncSend(packet, victim.getWorld().getPlayers());

        EntityLiving craftLiving = ((CraftLivingEntity) victim).getHandle();
        String hurtSound = ReflectionUtil.runMethod(craftLiving, craftLiving.getClass().getName(),"bo", String.class);
        SoundPlayer.sendSound(victim.getLocation(), hurtSound, 1, 75);
        return isDead;
    }

    private void fakeDie(Player p) {
        DamageQueue.artificialDie(p);
        DropDeathLootEvent e = new DropDeathLootEvent(p);
        PodcrashSpigot.debugLog("Dropping items...");
        Bukkit.getPluginManager().callEvent(e);
        if (e.isCancelled())
            return;
        PlayerInventory inventory = (p).getInventory();
        List<ItemStack> drops = new ArrayList<>(Arrays.asList(inventory.getContents()));
        drops.addAll(Arrays.asList(inventory.getArmorContents()));
        PodcrashSpigot.debugLog("Dropping items success! " + drops.size());
        World world = p.getWorld();
        Location location = p.getLocation();
        for (ItemStack stack : drops) {
            if (stack == null || stack.getType() == Material.AIR) continue;
            world.dropItemNaturally(location, stack);
        }
    }

    @Override
    public void init() {
        waterTask.start();
    }

    @Override
    public void stop() {
        waterTask.unregister();
    }
}
