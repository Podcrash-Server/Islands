package me.flaymed.islands.game.resource;

import com.podcrash.api.db.pojos.PojoHelper;
import com.podcrash.api.db.pojos.map.GameMap;
import com.podcrash.api.db.pojos.map.Point;
import com.podcrash.api.game.resources.GameResource;
import com.podcrash.api.time.resources.TimedTask;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AnimalSpawnResource extends GameResource {
    private final TimedTask timedTask;
    public AnimalSpawnResource(int gameID) {
        super(gameID);
        final Map<String, List<Point>> spawns = ((GameMap) game.getMap()).getSpawns();
        final World world = game.getGameWorld();

        // I hope this part does not get GCed away
        final List<Vector> spawnVectors = new ArrayList<>();
        final EntityType[] possibleEntityTypes = { EntityType.COW, EntityType.MUSHROOM_COW, EntityType.PIG };
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
                    int index = random.nextInt(possibleEntityTypes.length);
                    world.spawnEntity(location, possibleEntityTypes[index]);
                }
            }
        };
        timedTask.setAsync(false);
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
