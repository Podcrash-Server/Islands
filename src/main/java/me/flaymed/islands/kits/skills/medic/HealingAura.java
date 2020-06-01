package me.flaymed.islands.kits.skills.medic;

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
import com.podcrash.api.kits.skilltypes.Drop;
import me.flaymed.islands.util.IslandsParticleGenerator;
import net.jafama.FastMath;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.util.Vector;

import java.util.List;

public class HealingAura extends Drop implements ICooldown {
    @Override
    public float getCooldown() {
        return 30;
    }

    @Override
    public boolean drop(PlayerDropItemEvent e) {
        if (e.getPlayer() != getPlayer()) return false;
        if (onCooldown()) {
            getPlayer().sendMessage(getCooldownMessage());
            return false;
        }
        this.setLastUsed(System.currentTimeMillis());

        List<LivingEntity> entities = getPlayer().getWorld().getLivingEntities();
        StatusApplier.getOrNew(getPlayer()).applyStatus(Status.REGENERATION, 5, 1, true);

        for (LivingEntity entity : entities) {
            if (!(entity instanceof Player))
                continue;
            Player player1 = (Player) entity;
            Player player2 = getPlayer();

            if (distance(player1.getLocation().getX(), player1.getLocation().getZ(), player2.getLocation().getX(), player2.getLocation().getZ()) > 5.0)
                continue;
            if (!isAlly(player1))
                continue;
            StatusApplier.getOrNew(player1).applyStatus(Status.REGENERATION, 5, 1, true);
            IslandsParticleGenerator.particleOverPlayer(player1);
        }
        getPlayer().sendMessage(getUsedMessage());
        return true;
    }

    @Override
    public String getName() {
        return "Healing Aura";
    }

    @Override
    public ItemType getItemType() {
        return ItemType.NULL;
    }

    private double distance(double x1, double z1, double x2, double z2) {
        // Calculating distance
        return FastMath.sqrt(FastMath.pow2(x2 - x1) + FastMath.pow2(z2 - z1));
    }

}
