package me.flaymed.islands.util;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.packetwrapper.abstractpackets.AbstractPacket;
import com.packetwrapper.abstractpackets.WrapperPlayServerWorldParticles;
import com.podcrash.gamecore.kits.KitPlayer;
import com.podcrash.gamecore.kits.KitPlayerManager;
import me.flaymed.islands.Islands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

//final bc you're not supposed to init an object
public final class IslandsParticleGenerator {


    /**
     * @param player  player to put particles 2 blocks above their head (color of their team)
     **/
    public static void particleOverPlayer(Player player) {

        KitPlayer kitPlayer = KitPlayerManager.getKitPlayerFromPlayer(player);
        if (kitPlayer == null) return;

        Location playerLoc = player.getEyeLocation();
        Vector v = playerLoc.add(0, 2, 0).toVector();

        float[] RGB = new float[0];
        ChatColor color = kitPlayer.getTeam().getSide().getColor();
        if (color == ChatColor.RED) RGB = new float[] {255/255F - 1F, 85/255F, 85/255F};
        if (color == ChatColor.BLUE) RGB = new float[] {85/255F - 1F, 85/255F, 255/255F};
        if (color == ChatColor.GREEN) RGB = new float[] {85/255F - 1F, 255/255F, 85/255F};
        if (color == ChatColor.YELLOW) RGB = new float[] {255/255F - 1F, 255/255F, 85/255F};
        WrapperPlayServerWorldParticles particle = createParticle(v, EnumWrappers.Particle.REDSTONE, new int[]{}, 0, RGB[0], RGB[1], RGB[2]);

        particle.setParticleData(1F);
        asyncSend(particle, KitPlayerManager.getPlayers());
    }

    public static void asyncSend(AbstractPacket packet, List<Player> players) {
        Bukkit.getScheduler().runTaskAsynchronously(Islands.getInstance(), () -> {
            for (Player player : players) {
                packet.sendPacket(player);
            }
        });
    }

    public static WrapperPlayServerWorldParticles createParticle(Vector vector, EnumWrappers.Particle particle, int[] data, int particleCount, float offsetX, float offsetY, float offsetZ) {
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
