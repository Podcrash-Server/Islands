package me.flaymed.islands.bridges.maker;

import com.podcrash.api.db.pojos.PojoHelper;
import com.podcrash.api.db.pojos.map.BridgePoint;
import com.podcrash.api.db.pojos.map.BridgeSection;
import com.podcrash.api.db.pojos.map.IDPoint2Point;
import com.podcrash.api.db.pojos.map.IslandsMap;
import com.podcrash.api.world.BlockUtil;
import me.flaymed.islands.bridges.BridgeBox;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BridgeGenerator {
    private final List<BridgeBox> boxes;

    public BridgeGenerator() {
        this.boxes = new ArrayList<>();
    }

    public void setUp(IslandsMap islandsMap) {
        for (IDPoint2Point point2Point : islandsMap.getBridges()) {
            BridgeBox box = new BridgeBox(PojoHelper.convertPoint2Vector(point2Point.getPoint1()),
                    PojoHelper.convertPoint2Vector(point2Point.getPoint2()));
            boxes.add(box);
        }
        ready(islandsMap);
    }
    protected abstract void ready(IslandsMap islandsMap);
    public abstract void generate(World world, int delay);
    public abstract void destroy(World world, int delay);

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

    protected void removeSection(World world, BridgeSection section) {
        Map<String, Object> blockMap = section.getBlockMap();
        for(Map.Entry<String, Object> entry : blockMap.entrySet()) {
            double y = Double.parseDouble(entry.getKey());
            BlockUtil.setBlock(new Location(world, section.getX(), y, section.getZ()), 0);
        }
    }
}
