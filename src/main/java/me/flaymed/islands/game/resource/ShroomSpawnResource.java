package me.flaymed.islands.game.resource;

import com.podcrash.api.db.pojos.PojoHelper;
import com.podcrash.api.db.pojos.map.GameMap;
import com.podcrash.api.db.pojos.map.Point;
import com.podcrash.api.game.resources.GameResource;
import com.podcrash.api.time.resources.TimedTask;
import com.podcrash.api.world.BlockUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import java.util.*;

public class ShroomSpawnResource extends GameResource {
    private final TimedTask timedTask;
    public ShroomSpawnResource(int gameID) {
        super(gameID);
        final Map<String, List<Point>> spawns = ((GameMap) game.getMap()).getSpawns();
        final World world = game.getGameWorld();

        // I hope this part does not get GCed away
        final List<Vector> spawnVectors = new ArrayList<>();
        List<Material> possibleMaterialTypes = Arrays.asList(Material.RED_MUSHROOM, Material.BROWN_MUSHROOM);
        for (List<Point> spawnList : spawns.values()) {
            for (Point spawn : spawnList) {
                spawnVectors.add(PojoHelper.convertPoint2Vector(spawn));
            }
        }
        //for 30 seconds?
        timedTask = new TimedTask(30L * 1000L) {
            @Override
            public void action() {
                Random random = new Random();
                for (Vector vector : spawnVectors) {
                    Location location = vector.toLocation(world);
                    int index = random.nextInt(possibleMaterialTypes.size());
                    Location setBlockLoc = BlockUtil.getHighestAbove(location);
                    if (possibleMaterialTypes.contains(setBlockLoc.getBlock().getType()))
                        continue;
                    BlockUtil.setBlock(setBlockLoc, possibleMaterialTypes.get(index));
                }
            }
        };
        timedTask.setAsync(false);
        timedTask.action();
    }

    @Override
    public void init() {
        timedTask.start();
    }

    @Override
    public void stop() {
        timedTask.unregister();
    }
}
