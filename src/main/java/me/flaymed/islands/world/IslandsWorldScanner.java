package me.flaymed.islands.world;

import me.flaymed.islands.location.BlockUtil;
import me.flaymed.islands.location.IDPoint2Point;
import me.flaymed.islands.location.Point;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.material.Wool;
import org.bukkit.util.Vector;

import java.util.*;

public class IslandsWorldScanner {
    //this is currently a limitation:
    private final Map<Integer, IDPoint2Point> tempBridgeMap = new TreeMap<>();

    public void scanWorld(World world) {
        Chunk[] chunks = world.getLoadedChunks();
        for (Chunk chunk : chunks) {
            int chunkX = chunk.getX() << 4;
            int chunkZ = chunk.getZ() << 4;
            //scan the blocks
            for(int y = 0; y < world.getMaxHeight(); y++) {
                for (int x = chunkX; x < chunkX + 16; x++) {
                    for (int z = chunkZ; z < chunkZ + 16; z++) {
                        Block block = world.getBlockAt(x, y, z);
                        //maps contain lots of air/water blocks, will save a lof time not having to run the scan method on the majority of the blocks.
                        if (block.getType() == Material.AIR || block.getType() ==  Material.WATER || block.getType() == Material.STATIONARY_WATER) continue;
                        scan(block);
                    }
                }
            }
        }
    }

    public void scan(Block block) {

        processMapName(block);
        processSpawn(block);
        processAuthor(block);

        processMiddle(block);
        processOres(block);
        processChests(block);
        processBridges(block);
        processBridgeType(block);
    }


    protected void processMapName(Block block) {
        if(!BlockUtil.isSign(block)) return;
        Sign signState = (Sign) block.getState();
        if(!signState.getLine(0).contains("DATA")) return;
        String name = signState.getLine(1);
        //TODO: config this
    }

    protected void processSpawn(Block block) {
        if(block.getType() != Material.WOOL) return;
        Block plate = block.getRelative(BlockFace.UP);
        if(plate.getType() != Material.STONE_PLATE) return;


        Location loc = block.getLocation();
        //we add 0.5 because block coords return whole numbers
        //whole number coords usually make people suffocate into a wall.
        //(0.5, 0, 0.5) will make players stand in the middle of that block.
        Point point = Point.convertVector2Point(loc.toVector().add(new Vector(0.5, 0, 0.5)));
        Wool woolData = (Wool) block.getState().getData();

        ChatColor color = ChatColor.valueOf(woolData.getColor().name());

        //TODO: config this
    }

    protected void processAuthor(Block block) {
        if(block.getType() != Material.SIGN_POST && block.getType() != Material.WALL_SIGN) return;
        Sign sign = (Sign) block.getState();
        //if it doesn't contain, it's a normal sign
        if(!sign.getLine(0).contains("[Authors]")) return;
        for(int i = 1; i < 4; i++) {
            String line = sign.getLine(i);
            if(line.isEmpty()) continue;
            //TODO: config this
        }
    }

    private void processOres(Block block) {
        Material oreType = block.getType();
        Point point = Point.convertVector2Point(block.getLocation().toVector());
        //TODO: config this
        switch (oreType) {
            case DIAMOND_ORE:
                map.getBlueOres().add(point);
                break;
            case REDSTONE_ORE:
                map.getRedOres().add(point);
                break;
            case EMERALD_ORE:
                map.getGreenOres().add(point);
                break;
            case LAPIS_ORE:
                map.getYellowOres().add(point);
                break;
        }

    }


    private void processChests(Block block) {
        if (block.getType() != Material.CHEST)
            return;

        Point point = Point.convertVector2Point(block.getLocation().toVector());
        //TODO: config this.
    }

    private void processMiddle(Block block) {
        if(block.getType() != Material.FURNACE)
            return;
        Block plate = block.getRelative(BlockFace.UP);
        if(plate.getType() != Material.STONE_PLATE)
            return;

        Point point = Point.convertVector2Point(block.getLocation().toVector().add(new Vector(0.5, 0, 0.5)));
        //TODO: config this

    }

    private void processBridges(Block block) {
        if (!BlockUtil.isSign(block))
            return;
        Sign sign = (Sign) block.getState();
        String firstLine = sign.getLine(0);
        if (!firstLine.equals("BRIDGE"))
            return;

        Point point = Point.convertVector2Point(block.getRelative(((org.bukkit.material.Sign) sign.getData()).getAttachedFace()).getLocation().toVector());
        int bridgeID;
        try {
            String secondLine = sign.getLine(1);
            bridgeID = Integer.parseInt(secondLine);
        }catch (NumberFormatException e) {
            return;
        }

        synchronized (tempBridgeMap) {
            IDPoint2Point questionPoint = tempBridgeMap.get(bridgeID);
            if (questionPoint == null) {
                questionPoint = new IDPoint2Point();
                questionPoint.setId(bridgeID);
                tempBridgeMap.put(bridgeID, questionPoint);
            }

            if (questionPoint.getPoint1() != null && questionPoint.getPoint2() != null) return;

            if (questionPoint.getPoint1() == null) {
                questionPoint.setPoint1(point);
            } else if (questionPoint.getPoint2() == null) {
                questionPoint.setPoint2(point);
            }
            List<IDPoint2Point> ya = new ArrayList<>(tempBridgeMap.values());


            //todo: config this
        }
    }

    private void processBridgeType(Block block) {
        if(!BlockUtil.isSign(block)) return;
        Sign signState = (Sign) block.getState();
        if(!signState.getLine(0).contains("BRIDGETYPE")) return;
        String type = signState.getLine(1);
        //TODO: config this
    }
}