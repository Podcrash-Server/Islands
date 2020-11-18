package me.flaymed.islands.world;

import me.flaymed.islands.Islands;
import me.flaymed.islands.location.BlockUtil;
import me.flaymed.islands.location.IDPoint2Point;
import me.flaymed.islands.location.Point;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.material.Wool;
import org.bukkit.util.Vector;
import java.util.*;

public class IslandsWorldScanner {
    //this is currently a limitation:
    private final Map<Integer, IDPoint2Point> tempBridgeMap = new TreeMap<>();
    private ConfigurationSection worldData;

    public void scanWorld(Player player, String name) {
        World world = player.getWorld();

        //Check if world name is in the config
        List<String> worldNames = Islands.getIsConfig().getWorldNames();
        if (worldNames.contains(name)) this.worldData = Islands.getIsConfig().getWorldData(name);
        else {
            ConfigurationSection maps = Islands.getIsConfig().getMapsConfigurationSection();
            maps.createSection(name);
            this.worldData = maps.getConfigurationSection(name);
        }
        worldData.set("worldName", name);

        //Scan the chunks
        Chunk[] chunks = world.getLoadedChunks();
        List<Material> skipBlocks = Arrays.asList(Material.AIR, Material.WATER, Material.STATIONARY_WATER, Material.BEDROCK);
        for (Chunk chunk : chunks) {
            int chunkX = chunk.getX() << 4;
            int chunkZ = chunk.getZ() << 4;
            //scan the blocks
            for(int y = 0; y < world.getMaxHeight(); y++) {
                for (int x = chunkX; x < chunkX + 16; x++) {
                    for (int z = chunkZ; z < chunkZ + 16; z++) {
                        Block block = world.getBlockAt(x, y, z);
                        //maps contain lots of air/water blocks, will save a lof time not having to run the scan method on the majority of the blocks.
                        if (skipBlocks.contains(block.getType())) continue;
                        scan(block);
                    }
                }
            }
        }
    }

    public void scan(Block block) {

        processSpawn(block);
        processAuthor(block);

        processMiddle(block);
        processOres(block);
        processChests(block);
        processBridges(block);
        processBridgeType(block);
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


        switch (color) {
            case RED:
                List<String> redSpawns = worldData.getStringList("RedSpawns");
                redSpawns.addAll(Arrays.asList(String.valueOf(point.getX()), String.valueOf(point.getY()), String.valueOf(point.getZ())));
                worldData.set("RedSpawns", redSpawns);
                break;
            case BLUE:
                List<String> blueSpawns = worldData.getStringList("BlueSpawns");
                blueSpawns.addAll(Arrays.asList(String.valueOf(point.getX()), String.valueOf(point.getY()), String.valueOf(point.getZ())));
                worldData.set("BlueSpawns", blueSpawns);
                break;
            case GREEN:
                List<String> greenSpawns = worldData.getStringList("GreenSpawns");
                greenSpawns.addAll(Arrays.asList(String.valueOf(point.getX()), String.valueOf(point.getY()), String.valueOf(point.getZ())));
                worldData.set("GreenSpawns", greenSpawns);
                break;
            case YELLOW:
                List<String> yellowSpawns = worldData.getStringList("YellowSpawns");
                yellowSpawns.addAll(Arrays.asList(String.valueOf(point.getX()), String.valueOf(point.getY()), String.valueOf(point.getZ())));
                worldData.set("YellowSpawns", yellowSpawns);
                break;
        }
    }

    protected void processAuthor(Block block) {
        if(block.getType() != Material.SIGN_POST && block.getType() != Material.WALL_SIGN) return;
        Sign sign = (Sign) block.getState();
        //if it doesn't contain, it's a normal sign
        if(!sign.getLine(0).contains("[Authors]")) return;
        for(int i = 1; i < 4; i++) {
            String line = sign.getLine(i);
            if(line.isEmpty()) continue;
            worldData.set("author", worldData.getString("author") + "," + line);
        }
    }

    private void processOres(Block block) {
        Material oreType = block.getType();
        Point point = Point.convertVector2Point(block.getLocation().toVector());
        switch (oreType) {
            case GOLD_ORE:
                List<String> yellowOre = worldData.getStringList("YellowOre");
                yellowOre.addAll(Arrays.asList(String.valueOf(point.getX()), String.valueOf(point.getY()), String.valueOf(point.getZ())));
                worldData.set("YellowOre", yellowOre);
                break;
            case REDSTONE_ORE:
                List<String> redOre = worldData.getStringList("RedOre");
                redOre.addAll(Arrays.asList(String.valueOf(point.getX()), String.valueOf(point.getY()), String.valueOf(point.getZ())));
                worldData.set("RedOre", redOre);
                break;
            case LAPIS_ORE:
                List<String> blueOre = worldData.getStringList("BlueOre");
                blueOre.addAll(Arrays.asList(String.valueOf(point.getX()), String.valueOf(point.getY()), String.valueOf(point.getZ())));
                worldData.set("BlueOre", blueOre);
                break;
            case EMERALD_ORE:
                List<String> greenOre = worldData.getStringList("GreenOre");
                greenOre.addAll(Arrays.asList(String.valueOf(point.getX()), String.valueOf(point.getY()), String.valueOf(point.getZ())));
                worldData.set("GreenOre", greenOre);
                break;
        }

    }


    private void processChests(Block block) {
        if (block.getType() != Material.CHEST)
            return;

        Point point = Point.convertVector2Point(block.getLocation().toVector());
        List<String> chestPoints = worldData.getStringList("Chests");
        chestPoints.add(String.valueOf(point.getX()));
        chestPoints.add(String.valueOf(point.getY()));
        chestPoints.add(String.valueOf(point.getZ()));
        worldData.set("Chests", chestPoints);
    }

    private void processMiddle(Block block) {
        if(block.getType() != Material.FURNACE)
            return;
        Block plate = block.getRelative(BlockFace.UP);
        if(plate.getType() != Material.STONE_PLATE)
            return;

        Point point = Point.convertVector2Point(block.getLocation().toVector().add(new Vector(0.5, 0, 0.5)));
        worldData.set("middle", Arrays.asList(String.valueOf(point.getX()), String.valueOf(point.getY()), String.valueOf(point.getZ())));
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


            List<String> bridgePoints = worldData.getStringList("BridgePoints");
            for (IDPoint2Point y : ya) {
                bridgePoints.addAll(Arrays.asList(String.valueOf(y.getId()), String.valueOf(y.getPoint1().getX()), String.valueOf(y.getPoint1().getY()), String.valueOf(y.getPoint1().getZ()), String.valueOf(y.getId()), String.valueOf(y.getPoint2().getX()), String.valueOf(y.getPoint2().getY()), String.valueOf(y.getPoint2().getZ())));
            }
            worldData.set("BridgePoints", bridgePoints);
        }
    }

    private void processBridgeType(Block block) {
        if(!BlockUtil.isSign(block)) return;
        Sign signState = (Sign) block.getState();
        if(!signState.getLine(0).contains("BRIDGETYPE")) return;
        String type = signState.getLine(1);
        worldData.set("bridgetype", type);
    }
}