package me.flaymed.islands.kits.skills.berserker;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.packetwrapper.abstractpackets.WrapperPlayServerWorldParticles;
import com.podcrash.api.callback.sources.CollideBeforeHitGround;
import com.podcrash.api.damage.DamageApplier;
import com.podcrash.api.effect.particle.ParticleGenerator;
import com.podcrash.api.effect.status.Status;
import com.podcrash.api.effect.status.StatusApplier;
import com.podcrash.api.events.skill.SkillUseEvent;
import com.podcrash.api.game.GameManager;
import com.podcrash.api.kits.enums.ItemType;
import com.podcrash.api.kits.iskilltypes.action.IConstruct;
import com.podcrash.api.kits.iskilltypes.action.ICooldown;
import com.podcrash.api.kits.skilltypes.Instant;
import com.podcrash.api.util.PacketUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.util.Vector;

import java.util.List;

public class Takedown extends Instant implements ICooldown, IConstruct {

    private final float hitbox = 0.45f;
    private CollideBeforeHitGround hitGround;

    @Override
    public float getCooldown() {
        return 15;
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
                            DamageApplier.damage(living, getPlayer(), 8, this, false);
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
    protected void doSkill(PlayerEvent event, Action action) {
        if (onCooldown()) return;
        this.setLastUsed(System.currentTimeMillis());
        getPlayer().setFallDistance(0);
        hitGround.run();
    }

    @Override
    public String getName() {
        return "Takedown";
    }

    @Override
    public ItemType getItemType() {
        return null;
    }
}
