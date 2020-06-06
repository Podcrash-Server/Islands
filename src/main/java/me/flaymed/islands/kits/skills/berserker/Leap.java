package me.flaymed.islands.kits.skills.berserker;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.packetwrapper.abstractpackets.WrapperPlayServerWorldParticles;
import com.podcrash.api.callback.sources.CollideBeforeHitGround;
import com.podcrash.api.damage.DamageApplier;
import com.podcrash.api.effect.particle.ParticleGenerator;
import com.podcrash.api.effect.status.Status;
import com.podcrash.api.effect.status.StatusApplier;
import com.podcrash.api.events.DamageApplyEvent;
import com.podcrash.api.events.skill.SkillUseEvent;
import com.podcrash.api.game.GameManager;
import com.podcrash.api.kits.enums.ItemType;
import com.podcrash.api.kits.iskilltypes.action.ICharge;
import com.podcrash.api.kits.iskilltypes.action.IConstruct;
import com.podcrash.api.kits.iskilltypes.action.ICooldown;
import com.podcrash.api.kits.skilltypes.Instant;
import com.podcrash.api.util.PacketUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.util.Vector;
import java.util.List;

public class Leap extends Instant implements ICooldown, ICharge, IConstruct {

    private final float hitbox = 0.45f;
    private CollideBeforeHitGround hitGround;
    private final int MAX_CHARGES = 8;
    private int charges = MAX_CHARGES;
    private int hits = 0;


    public Leap() {

    }

    @Override
    public void afterConstruction() {
        WrapperPlayServerWorldParticles packet = ParticleGenerator.createParticle(EnumWrappers.Particle.CRIT, 2);
        this.hitGround = new CollideBeforeHitGround(getPlayer(), 1, hitbox, hitbox, hitbox)
                .then(() -> {
                    SkillUseEvent event = new SkillUseEvent(this);
                    Bukkit.getPluginManager().callEvent(event);
                    if(event.isCancelled()) return;
                    List<Entity> entities = CollideBeforeHitGround.getValidEntitiesInRange(getPlayer(), hitbox, hitbox, hitbox);
                    if (entities.size() == 0) return;
                    getPlayer().setVelocity(new Vector(0, 0, 0));
                    for (Entity entity : entities) {
                        if (entity instanceof LivingEntity && entity != getPlayer() && !isAlly((LivingEntity) entity)) {
                            if (entity instanceof Player && GameManager.isSpectating((Player) entity)) break;
                            LivingEntity living = (LivingEntity) entity;
                            getPlayer().getWorld().playSound(getPlayer().getLocation(), Sound.ZOMBIE_WOOD, 2f, 0.2f);
                            DamageApplier.damage(living, getPlayer(), 8, () -> "Takedown", false);
                            /*
                            StatusApplier.getOrNew((Player) entity).applyStatus(Status.SLOW, effect, 3);
                            StatusApplier.getOrNew(getPlayer()).applyStatus(Status.SLOW, effect, 3);
                             */

                            StatusApplier.getOrNew(living).applyStatus(Status.GROUND, 2, 3);
                            StatusApplier.getOrNew(getPlayer()).applyStatus(Status.GROUND, 2, 3);

                            break; //only attack one player
                        }
                    }
                }).doWhile(() -> {
                    packet.setLocation(getPlayer().getLocation());
                    PacketUtil.asyncSend(packet, getPlayers());
                });
    }

    @Override
    public float getCooldown() {
        return 15;
    }

    @Override
    protected void doSkill(PlayerEvent event, Action action) {
        Player player = this.getPlayer();
        if (onCooldown()) {
            this.getPlayer().sendMessage(getCooldownMessage());
            return;
        }
        if (getCurrentCharges() == 0) {
            player.sendMessage(String.format("%sSkill>%s You have used all of your available leaps!", ChatColor.BLUE, ChatColor.GRAY));
            return;
        }
        Material m = player.getLocation().getBlock().getType();
        Vector v;

        if (m.equals(Material.STATIONARY_WATER) || m.equals(Material.WATER)) {
            v = player.getLocation().getDirection().multiply(1.75);
        } else {
            v = player.getLocation().getDirection().multiply(1.5);
        }

        player.setVelocity(v);
        getPlayer().setFallDistance(0);
        hitGround.run();
        player.getWorld().playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 8, 1);
        charges--;

        player.sendMessage(getCurrentChargeMessage());

        setLastUsed(System.currentTimeMillis());
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    @Override
    public String getName() {
        return "Berserker Leap";
    }

    @Override
    public void addCharge() {
        /*
        charges++;
        if (charges > MAX_CHARGES) {
            charges = MAX_CHARGES;
        }
        this.getPlayer().sendMessage((getCurrentChargeMessage()));

         */
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
    public ItemType getItemType() {
        return ItemType.AXE;
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
        charges = 0;
    }

    @EventHandler
    public void playerHit(DamageApplyEvent e) {
        if (e.getAttacker() == this.getPlayer()) {
            hits++;
            if(hits >= 3) {
                task();
                hits = 0;
            }
        }
    }

}
