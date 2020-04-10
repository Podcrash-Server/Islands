package me.flaymed.islands.game.map.bridge;

import com.podcrash.api.db.pojos.map.IslandsMap;
import com.podcrash.api.mc.time.TimeHandler;
import com.podcrash.api.mc.time.resources.TimeResource;
import me.raindance.chunk.WorldScanner;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.*;

public class BridgeManager {

    private BridgeManager() {

    }

    public static void dropBridges(World world) {
        IslandsMap map = (IslandsMap) WorldScanner.get(world.getName());

        HashMap<Integer, LinkedList<Object>> bridgePieces = map.getBridgePieces();



        final int[] i = {0};
        TimeHandler.repeatedTimeSeconds(1, 0, new TimeResource() {

            @Override
            public void task() {
                for (Map.Entry bridgePiece : bridgePieces.entrySet()) {
                    if ((Integer) bridgePiece.getKey() != i[0])
                        continue;

                    LinkedList<Object> list = (LinkedList<Object>) bridgePiece.getValue();
                    Location loc = (Location) list.getFirst();
                    Block block = (Block) list.getLast();

                    world.getBlockAt(loc).setType(block.getType());
                    bridgePieces.remove((Integer) bridgePiece.getKey());
                }
                i[0]++;
            }

            @Override
            public boolean cancel() {
                return i[0] == map.getBridgeLength();
            }

            @Override
            public void cleanup() {

            }
        });

    }

}
