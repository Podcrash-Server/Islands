package me.flaymed.islands.util.ore;

import com.podcrash.api.plugin.PodcrashSpigot;
import com.podcrash.api.world.BlockUtil;
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
    private final double oreChance;
    private final double continueChance;
    private final int tries;
    private final Vector[] possDir;
    private final Object minerLock;

    private Location cursor;
    private boolean cursorSet;
    private boolean stillValid;
    private MaterialData ore;
    private int oreGenerated;

    public static VeinGen fromBuilder(VeinBuilder builder) {
        return new VeinGen(builder);
    }
    private VeinGen(VeinBuilder builder) {
        this.minerLock = new Object();
        this.oreChance = builder.oreChance;
        this.continueChance = builder.continueChance;
        this.possDir = builder.allowedDirections.toArray(new Vector[0]);
        this.tries = builder.tries;
        this.cursorSet = false;
        this.oreGenerated = 0;
        this.stillValid = true;
    }

    public void startLocation(Location location, Material ore) {
        this.startLocation(location, new MaterialData(ore));
    }
    /**
     * The assumption is that the location has a material of that ore.
     * @param location
     * @param ore
     */
    public void startLocation(Location location, MaterialData ore) {
        if (this.cursorSet) throw new IllegalStateException("Please run a new VeinGen for each individual ore.");
        this.cursor = location;
        this.cursorSet = true;
        this.ore = ore;
    }

    public boolean next() {
        synchronized (minerLock) {
            if (!this.stillValid) return false;
            for (int i = 0; i < tries; i++) {
                Location currentCursor = cursor.clone();
                //if the chance to continue is unlucky, stop
                if (random.nextDouble() > this.continueChance) {
                    break;
                }
                //otherwise find a random direction.
                Vector randomDir = possDir[random.nextInt(possDir.length)];
                Location newLoc = currentCursor.add(randomDir);

                //todo: use material data instead
                Block block = newLoc.getBlock();
                Material type = block.getType();
                if (random.nextDouble() > this.oreChance)
                    break;
                if (type == Material.AIR) {
                    stillValid = false;
                    break;
                }
                if (type != Material.STONE)
                    break;
                BlockUtil.setBlock(newLoc, ore.getItemType());

                oreGenerated++;
                PodcrashSpigot.debugLog(currentCursor.clone().add(cursor.clone().multiply(-1)).toVector().toString());
                this.cursor = currentCursor;
                return true;
            }
        }
        return false;
    }

    public boolean hasNext() {
        return this.stillValid;
    }

    public int getOreGenerated() {
        return oreGenerated;
    }
}
