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

import java.util.*;

public class AnimalSpawnResource extends GameResource {
    private final TimedTask timedTask;
    public AnimalSpawnResource(int gameID) {
        super(gameID);
        final Map<String, List<Point>> spawns = ((GameMap) game.getMap()).getSpawns();
        final World world = game.getGameWorld();

        // I hope this part does not get GCed away
        final Map<Integer, List<Vector>> spawnVectors = new HashMap<>();
        final EntityType[] possibleEntityTypes = { EntityType.COW, EntityType.CHICKEN, EntityType.PIG };
        int i = 0;
        for (List<Point> spawnList : spawns.values()) {
            List<Vector> vectors = new ArrayList<>();
            for (Point spawn : spawnList)
                vectors.add(PojoHelper.convertPoint2Vector(spawn));
            spawnVectors.put(i, vectors);
            i++;
        }
        //for 2 minutes.
        timedTask = new TimedTask(2L * 60L * 1000L) {
            @Override
            public void action() {
                Random random = new Random();
                for (List<Vector> vectors : spawnVectors.values()) {
                    for (Vector vector : vectors) {
                        if (random.nextDouble() >= 0.25D)
                            continue;
                        Location location = vector.toLocation(world);
                        int index = random.nextInt(possibleEntityTypes.length);
                        world.spawnEntity(location, possibleEntityTypes[index]);
                    }
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
