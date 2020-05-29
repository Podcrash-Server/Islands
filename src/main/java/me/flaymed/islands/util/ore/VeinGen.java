package me.flaymed.islands.util.ore;

import com.podcrash.api.plugin.PodcrashSpigot;
import com.podcrash.api.world.BlockUtil;
import net.jafama.FastMath;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class VeinGen {
    private final Random random = new Random();
    private final double continueChance;
    private final int tries, min, max;
    private final Vector[] possDir;
    private final Object minerLock;

    private Location cursor;
    private boolean cursorSet;
    private boolean stillValid;
    private MaterialData ore;
    private int oreGenerated;
    private Vector lastVector;
    private int lastAir = 0;

    public static VeinGen fromOreSetting(OreVeinSetting setting) {
        VeinBuilder builder = new VeinBuilder()
                .setContinueChance(setting.getContinueChance())
                .setTries(setting.getTries())
                .setMin(setting.getMin())
                .setMax(setting.getMax())
                .setOre(setting.getOre());
        return fromBuilder(builder);
    }
    public static VeinGen fromBuilder(VeinBuilder builder) {
        return new VeinGen(builder);
    }
    private VeinGen(VeinBuilder builder) {
        this.minerLock = new Object();
        this.continueChance = builder.continueChance;
        this.possDir = builder.allowedDirections.toArray(new Vector[0]);
        this.tries = builder.tries;
        this.min = builder.min;
        this.max = builder.max;
        this.ore = new MaterialData(builder.ore);
        this.cursorSet = false;
        this.oreGenerated = 0;
        this.stillValid = true;
    }

    /**
     * The assumption is that the location has a material of that ore.
     * @param location
     */
    public void startLocation(Location location) {
        if (this.cursorSet) throw new IllegalStateException("Please run a new VeinGen for each individual ore.");
        this.cursor = location;
        this.cursorSet = true;
    }

    public void generate() {
        if (!this.cursorSet)
            throw new IllegalStateException("Please set a start location using startLocation!");
        //place the block initially
        //BlockUtil.setBlock(cursor, ore.getItemType());

        //find a randomized amount between the max and min to give ores.
        int maxOreGenerated = min + random.nextInt(max - min + 1);
        while(getOreGenerated() < maxOreGenerated) {
            if (!generateVein())
                break;
        }
    }

    /**
     * Generate a vein
     * @return true if there is a place to generate the vein. False if there is no place to generate a vein
     */
    public boolean generateVein() {
        synchronized (minerLock) {
            if (!this.stillValid) return false;
            Location currentCursor = cursor.clone();
            //find a random direction.
            Vector randomDir = findRandomVector();
            Location newLoc = currentCursor.add(randomDir);

            //todo: use material data instead
            Block block = newLoc.getBlock();
            Material type = block.getType();
            if (type == ore.getItemType())
                return true;
            if (type != Material.STONE) {//if the chosen block is ore itself or not stone, cancel
                lastAir++;
                return lastAir < 5;
            }

            BlockUtil.setBlock(newLoc, ore.getItemType());
            oreGenerated++;

            this.cursor = currentCursor;
            lastAir = 0;
            return true;
        }
    }

    /**
     * First, find a random vector using random.nextInt
     * if it's the same as the last opposite vector, rerun the algorithm.
     * This is to prevent the vein going back on itself.
     * @return a random vector
     */
    private Vector findRandomVector() {
        Vector r = possDir[random.nextInt(possDir.length)];
        if (r == lastVector)
            return findRandomVector();
        this.lastVector = r.clone().multiply(-1);
        return r;
    }

    public boolean hasNext() {
        return this.stillValid;
    }

    public int getOreGenerated() {
        return oreGenerated;
    }
}
