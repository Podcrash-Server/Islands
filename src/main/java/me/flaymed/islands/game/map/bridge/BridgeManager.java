package me.flaymed.islands.game.map.bridge;

import com.podcrash.api.db.pojos.map.IslandsMap;
import me.raindance.chunk.WorldScanner;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class BridgeManager {

    private int duration = 1600;

    public void initiateBridgesMap(World world) {
        WorldScanner.scanWorldSync("islands", world);
    }

    public void generateBridgeSections(World world) {
        IslandsMap map = (IslandsMap) WorldScanner.get(world.getName());

        HashMap<Location, Location> bridgeParts = map.getBridgeParts();

        for (Map.Entry bridgePart : bridgeParts.entrySet()) {

            Location piece1 = (Location) bridgePart.getKey();
            Location piece2 = (Location) bridgePart.getValue();

            //Y Difference not needed
            double xDifference = Math.abs(piece1.getBlockX() - piece2.getBlockX());
            double zDifference = Math.abs(piece1.getBlockZ() - piece2.getBlockZ());

            //Only need width for direction
            double bridgeLength;

            if (xDifference > zDifference)
                bridgeLength = xDifference;
            else
                bridgeLength = zDifference;

            int highestX = Math.max(piece1.getBlockX(), piece2.getBlockX());
            int lowestX = Math.min(piece1.getBlockX(), piece2.getBlockX());

            int highestY = Math.max(piece1.getBlockY(), piece2.getBlockY());
            int lowestY = Math.min(piece1.getBlockY(), piece2.getBlockY());

            int highestZ = Math.max(piece1.getBlockZ(), piece2.getBlockZ());
            int lowestZ = Math.min(piece1.getBlockZ(), piece2.getBlockZ());

            LinkedList<Object> section = new LinkedList<>();
            for (int i = 0; i < bridgeLength; i++) {

                //Z is the width of the bridge
                if (xDifference > zDifference) {

                    int startingPos = highestX - 1;
                    for (int x = startingPos; x > startingPos - bridgeLength; x--) {
                        for (int y = lowestY; y < highestY; y++) {
                            for (int z = lowestZ; z < highestZ; z++) {
                                if (world.getBlockAt(x, y, z).getType().equals(Material.AIR))
                                    continue;
                                section.add(new Location(world, x, y, z));
                                section.add(world.getBlockAt(x, y, z));
                                map.addBridgePiece(i, section);
                            }
                        }
                        section.clear();
                    }

                //X is the width of the bridge
                } else {
                    int startingPos = highestZ - 1;
                    for (int z = startingPos; z > lowestZ; z--) {
                        for (int y = lowestY; y < highestY; y++) {
                            for (int x = lowestX; x < highestX; x++) {
                                if (world.getBlockAt(x, y, z).getType().equals(Material.AIR))
                                    continue;
                                section.add(new Location(world, x, y, z));
                                section.add(world.getBlockAt(x, y, z));
                                map.addBridgePiece(i, section);
                            }
                        }
                        section.clear();
                    }
                }
            }

        }
    }

}
