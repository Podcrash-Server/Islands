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
        if (e.getPlayer() == getPlayer()) {
          if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {

              if (!onCooldown()) {

                  this.setLastUsed(System.currentTimeMillis());

                  List<LivingEntity> entities = getPlayer().getWorld().getLivingEntities();

                  StatusApplier.getOrNew(getPlayer()).applyStatus(Status.REGENERATION, 5, 1, true);

                  for (LivingEntity entity : entities) {
                      if (entity instanceof Player) {
                          Player player1 = (Player) entity;
                          Player player2 = getPlayer();

                          if (distance(player1.getLocation().getBlockX(), player1.getLocation().getBlockZ(), player2.getLocation().getBlockX(), player2.getLocation().getBlockZ()) <= 5.0) {

                              if (isAlly(player1)) StatusApplier.getOrNew(player1).applyStatus(Status.REGENERATION, 5, 1, true);

                              IslandsParticleGenerator.particleOverPlayer(player1);
                          }


                      }
                  }

                  getPlayer().sendMessage(getUsedMessage());


              } else {
                  getPlayer().sendMessage(getCooldownMessage());
              }

          }
        }
    }

    private double distance(int x1, int z1, int x2, int z2) {
        // Calculating distance
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(z2 - z1, 2));
    }

}
