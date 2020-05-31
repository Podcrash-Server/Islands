package me.flaymed.islands.bridges.maker;

import com.google.common.collect.Maps;
import com.podcrash.api.db.pojos.map.BridgePoint;
import com.podcrash.api.db.pojos.map.BridgeSection;
import com.podcrash.api.db.pojos.map.IslandsMap;
import com.podcrash.api.db.pojos.map.Point;
import com.podcrash.api.plugin.PodcrashSpigot;
import me.flaymed.islands.annotations.BridgeType;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static net.jafama.FastMath.*;

/**
 * Using a circle of the entire bridge, place blocks closer and closer to the middle.
 */
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

    /**
     * Split up the sections to a map to have a better way than just looping through the entire xz plane to find the section we want.
     * Courtesy of noid's code.
     *
     * Find the minimum and maximum radii. This will dictate which blocks to place within the bridge.
     * @param islandsMap - the map used that stores the data.
     */
    @Override
    public void ready(IslandsMap islandsMap) {
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
                //create the key which is formatted like so: %:%, x,z
                String key = x + ":" + z;
                sectionMap.put(key, section);
                double dist = sqrt( pow2(midX - x) + pow2(midZ - z));
                max = max(dist, max);
                min = min(dist, min);
            }

            PodcrashSpigot.debugLog("Loaded bridge " + bridgePoint.getBridgeID() + " " + sectionMap.size() + " size");
        }

        this.maxRadius = (int) ceil(max) + 1;
        this.minRadius = (int) floor(min) - 1;
        Bukkit.broadcastMessage(sectionMap.size() + " size");
    }

    /**
     * Every delay ticks or so, place blocks in a circlular fashion.
     * @param world
     * @param delay
     */
    @Override
    public void generate(World world, int delay) {
        doStuff(world, delay, this::placeSection);
    }

    private void doStuff(final World world, final int delay, final BiConsumer<World, BridgeSection> worldBridgeSectionBiConsumer) {
        final double[] currentRadius = {maxRadius};
        final Map<String, BridgeSection> cloneMap = new HashMap<>(sectionMap);
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
                //2 is default (from noid's code)
                final double borderFactor = 2;

                int blocksPlaced = 0;
                final double currRadiusSquared = pow2(currentRadius[0]);
                final double shortDistSquared = pow2(currentRadius[0] - borderFactor);
                for (int x = startX; x <= endX; x++) {
                    //save up some extra calculations here
                    double xSquared = pow2(x - midX);
                    for (int z = startZ; z <= endZ; z++) {
                        double distSquared = xSquared + pow2(z - midZ);


                        boolean inequality = shortDistSquared <= distSquared && distSquared <= currRadiusSquared;
                        // if it's larger, then don't go past
                        // if it's shorter, then don't go past
                        //inequality: shortDistSquared <= distSquared <= currRadiusSquared
                        //contra: distSquared > currDistSquared
                        // and shortDistSquared > distSquared
                        if (!inequality)
                            continue;
                        String possKey = x + ":" + z;
                        BridgeSection section = cloneMap.get(possKey);
                        if (section == null)
                            continue;
                        worldBridgeSectionBiConsumer.accept(world, section);
                        blocksPlaced++;
                        cloneMap.remove(possKey);
                    }
                }
                PodcrashSpigot.debugLog("Placed " + blocksPlaced + " blocks");
                //decrease the radius little by little
                currentRadius[0]--;
            }
        };
        runnable.runTaskTimerAsynchronously(PodcrashSpigot.getInstance(), 0, delay);
    }

    /**
     * Copy and pasting :^)
     * @param world
     * @param delay
     */
    @Override
    public void destroy(World world, int delay) {
        doStuff(world, delay, this::removeSection);
    }
}
