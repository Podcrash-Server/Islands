package me.flaymed.islands.util;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.packetwrapper.abstractpackets.WrapperPlayServerWorldParticles;
import com.podcrash.api.effect.particle.ParticleGenerator;
import com.podcrash.api.game.GTeam;
import com.podcrash.api.game.GameManager;
import com.podcrash.api.game.TeamEnum;
import com.podcrash.api.util.PacketUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

//final bc you're not supposed to init an object
public final class IslandsParticleGenerator {


    /**
     * @param player  player to put particles 2 blocks above their head (color of their team)
     **/
    public static void particleOverPlayer(Player player) {
        GTeam team = GameManager.getGame().getTeam(player);
        TeamEnum teamEnum = team.getTeamEnum();
        Location playerLoc = player.getEyeLocation();

        Vector v = playerLoc.add(0, 2, 0).toVector();

        float[] RGB = new float[] {
                teamEnum.getRed()/255F - 1F,
                teamEnum.getGreen()/255F,
                teamEnum.getBlue()/255F
        };
        WrapperPlayServerWorldParticles particle = ParticleGenerator.createParticle(v,
                EnumWrappers.Particle.REDSTONE, new int[]{}, 0,
                RGB[0], RGB[1], RGB[2]);

        particle.setParticleData(1F);
        PacketUtil.asyncSend(particle, GameManager.getGame().getBukkitPlayers());
    }
}
