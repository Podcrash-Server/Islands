package me.flaymed.islands.game.bridges;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.podcrash.api.mc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class SpawnBridges {

    private ProtocolManager pm = ProtocolLibrary.getProtocolManager();

    public void dropBridges(World world) {
        List<Player> players = GameManager.getGame().getBukkitPlayers();

        Bukkit.broadcastMessage(String.format("%sWARNING! %s Water is very hot/cold on this map!", ChatColor.RED, ChatColor.WHITE));
        //Play enderdragon sound to show bridges are spawning
        for (Player player : players) {
            Location loc = player.getLocation();
            PacketContainer packet = pm.createPacket(PacketType.Play.Server.NAMED_SOUND_EFFECT);
            packet.getModifier().writeDefaults();
            packet.getStrings().write(0, "mob.enderdragon.end");
            packet.getIntegers().write(0, loc.getBlockX()).write(1, loc.getBlockY()).write(2, loc.getBlockZ()).write(3, 10);
            packet.getFloat().write(0, (float) 1);
            try {
                pm.sendServerPacket(player, packet);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        summonBridges(world);

    }

    private void summonBridges(World world) {

    }


}
