package me.flaymed.islands.util;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.packetwrapper.abstractpackets.WrapperPlayServerWorldParticles;
import com.podcrash.api.game.GTeam;
import com.podcrash.api.game.GameManager;
import com.podcrash.api.game.TeamEnum;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class IslandsParticleGenerator {


    /**
     * @param player  player to put particles 2 blocks above their head (color of their team)
     **/
    public static void particleOverPlayer(Player player) {
        GTeam team = GameManager.getGame().getTeam(player);
        TeamEnum teamEnum = team.getTeamEnum();
        Location playerLoc = player.getLocation();

        Vector v = new Location(player.getWorld(), playerLoc.getBlockX(), playerLoc.getBlockY() + 2, playerLoc.getBlockZ()).getDirection();


        WrapperPlayServerWorldParticles particle = com.podcrash.api.effect.particle.ParticleGenerator.createParticle(v,
                EnumWrappers.Particle.REDSTONE,1,
                teamEnum.getRed()/255, teamEnum.getGreen()/255, teamEnum.getBlue()/255);
        particle.setParticleData(1);
    }

}
