package me.flaymed.islands.bridges.maker;

import com.podcrash.api.db.pojos.map.BridgePoint;
import com.podcrash.api.db.pojos.map.BridgeSection;
import com.podcrash.api.db.pojos.map.IslandsMap;
import com.podcrash.api.db.pojos.map.Point;
import com.podcrash.api.plugin.PodcrashSpigot;
import me.flaymed.islands.annotations.BridgeType;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static net.jafama.FastMath.*;

@BridgeType(type="radius")
public class RadiusGenerator extends BridgeGenerator {
    private double midX;
    private double midZ;

    private double maxRadius;
    private double minRadius;

    private final Map<String, BridgeSection> sectionMap;
    public RadiusGenerator() {
        this.sectionMap = new LinkedHashMap<>();
    }

    @Override
    public void setUp(IslandsMap islandsMap) {
        List<BridgePoint> bridgePoints = islandsMap.getBridgeData();
        Point midBlock = islandsMap.getMiddle();
        this.midX = midBlock.getX();
        this.midZ = midBlock.getZ();

        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for (BridgePoint bridgePoint : bridgePoints) {
            List<BridgeSection> sections = bridgePoint.getSections();
            for (BridgeSection section : sections) {
                int x = (int) section.getX();
                int z = (int) section.getZ();
                String key = x + ":" + z;
                sectionMap.put(key, section);
                double dist = sqrt( pow2(midX - x) + pow2(midZ - z));
                max = max(dist, max);
                min = min(dist, min);
            }
        }

        this.maxRadius = (int) ceil(max) + 1;
        this.minRadius = (int) floor(min) - 1;
    }

    @Override
    public void generate(World world, int delay) {
        final double[] currentRadius = {maxRadius};

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (currentRadius[0] < minRadius) {
                    cancel();
                    PodcrashSpigot.debugLog("Finished building the bridge!");
                    return;
                }

                int startX = (int) (midX - currentRadius[0]);
                int startZ = (int) (midZ - currentRadius[0]);

                int endX = (int) (midX + currentRadius[0]);
                int endZ = (int) (midZ + currentRadius[0]);

                //this is so there is a limit on which part of the bridge is actually being built at a time
                final double borderFactor = 2;
                for (int x = startX; x <= endX; x++) {
                    double xSquared = pow2(x);
                    double shortXQuared = pow2(x - 2);
                    for (int z = startZ; z <= endZ; z++) {
                        double distSquared = xSquared + pow2(z);
                        double shortDistSquared = shortXQuared + pow2( z - borderFactor);
                        if (distSquared < shortDistSquared)
                            continue;
                        String possKey = x + ":" + z;
                        BridgeSection section = sectionMap.get(possKey);
                        if (section == null)
                            continue;
                        placeSection(world, section);
                        sectionMap.remove(possKey);
                    }
                }
                PodcrashSpigot.debugLog("Placed at " + currentRadius[0]);
                currentRadius[0]--;
            }
        };
        runnable.runTaskTimer(PodcrashSpigot.getInstance(), 0, delay);
    }
}
