package me.flaymed.islands.bridges.maker;

import com.podcrash.api.db.pojos.map.BridgePoint;
import com.podcrash.api.db.pojos.map.BridgeSection;
import com.podcrash.api.db.pojos.map.IslandsMap;
import com.podcrash.api.world.BlockUtil;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;
import java.util.Map;

public abstract class BridgeGenerator {

    public abstract void setUp(IslandsMap islandsMap);

    public abstract void generate(World world, int delay);

    /**
     * Take a given section and place blocks onto the world from the section's coordinates
     * @param world
     * @param section
     */
    protected void placeSection(World world, BridgeSection section) {
        Map<String, Object> blockMap = section.getBlockMap();
        for(Map.Entry<String, Object> entry : blockMap.entrySet()) {
            double y = Double.parseDouble(entry.getKey());
            int blockID = Integer.parseInt((String) entry.getValue());
            BlockUtil.setBlock(new Location(world, section.getX(), y, section.getZ()), blockID);
        }
    }
}
