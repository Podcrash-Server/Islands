package me.flaymed.islands.kits.skills.archer;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.packetwrapper.abstractpackets.WrapperPlayServerWorldParticles;
import com.podcrash.api.effect.particle.ParticleGenerator;
import com.podcrash.api.effect.status.Status;
import com.podcrash.api.effect.status.StatusApplier;
import com.podcrash.api.game.GTeam;
import com.podcrash.api.game.GameManager;
import com.podcrash.api.game.TeamEnum;
import com.podcrash.api.kits.enums.ItemType;
import com.podcrash.api.kits.iskilltypes.action.ICooldown;
import com.podcrash.api.kits.skilltypes.Instant;
import com.podcrash.api.kits.skilltypes.Passive;
import me.flaymed.islands.util.IslandsParticleGenerator;
import net.jafama.FastMath;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.List;

public class HealingShot extends Passive implements ICooldown {
    @Override
    public float getCooldown() {
        return 25;
    }

    @Override
    public String getName() {
        return "Healing Shot";
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent e) {
        if (e.getPlayer() != getPlayer())
            return;
        if (e.getAction() != Action.LEFT_CLICK_AIR && e.getAction() != Action.LEFT_CLICK_BLOCK)
          return;
        if (onCooldown()) {
            getPlayer().sendMessage(getCooldownMessage());
            return;
        }
        this.setLastUsed(System.currentTimeMillis());
        List<LivingEntity> entities = getPlayer().getWorld().getLivingEntities();
        StatusApplier.getOrNew(getPlayer()).applyStatus(Status.REGENERATION, 5, 1, true);
        getPlayer().sendMessage(getUsedMessage());
        for (LivingEntity entity : entities) {
            if (!(entity instanceof Player))
                continue;
            Player player1 = (Player) entity;
            Player player2 = getPlayer();
            //the blockX returns a floored version of the regular double coordinates
            //imo I would use the doubles to get a more accurate calculation
            if (distance(player1.getLocation().getX(), player1.getLocation().getZ(), player2.getLocation().getX(), player2.getLocation().getZ()) > 5.0)
                continue;

            if (!isAlly(player1)) continue;
            StatusApplier.getOrNew(player1).applyStatus(Status.REGENERATION, 5, 1, true);
            IslandsParticleGenerator.particleOverPlayer(player1);
        }
    }

    private double distance(double x1, double z1, double x2, double z2) {
        // Calculating distance
        return FastMath.sqrt(FastMath.pow2(x2 - x1) + FastMath.pow2(z2 - z1));
    }

}
