package me.flaymed.islands.world;

import me.flaymed.islands.Islands;
import me.flaymed.islands.bridges.data.BridgePoint;
import me.flaymed.islands.location.IDPoint2Point;
import me.flaymed.islands.location.Point;
import me.flaymed.islands.teams.IslandsTeam;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IslandsMap {
    private List<Point> redBorder;
    private List<Point> blueBorder;
    private List<Point> greenBorder;
    private List<Point> yellowBorder;
    private List<Location> entitySpawnLocations;
    private List<Point> greenOres;
    private List<Point> yellowOres;
    private List<Point> blueOres;
    private List<Point> redOres;
    private List<Point> chests;
    private List<BridgePoint> bridgeData;
    private Point middle;
    private String bridgeType;
    private List<IDPoint2Point> bridges;
    private World world;
    private Double[] worldBorderCords; //X,Z,X2,Z2 [Y value doesn't matter]

    public IslandsMap(World world) {
        super();
        this.redBorder = new ArrayList<>();
        this.blueBorder = new ArrayList<>();
        this.greenBorder = new ArrayList<>();
        this.yellowBorder = new ArrayList<>();
        this.entitySpawnLocations = new ArrayList<>();
        this.greenOres = new ArrayList<>();
        this.yellowOres = new ArrayList<>();
        this.blueOres = new ArrayList<>();
        this.redOres = new ArrayList<>();
        this.chests = new ArrayList<>();
        this.bridges = new ArrayList<>();
        this.world = world;

        fillData();
    }

    public boolean playerIsInBound(Player player) {
        if (worldBorderCords.length == 0) return true;
        double playerX = player.getLocation().getX();
        double playerZ = player.getLocation().getZ();

        double maxX = worldBorderCords[0] > worldBorderCords[2] ? worldBorderCords[0] : worldBorderCords[2];
        double minX = worldBorderCords[0] > worldBorderCords[2] ? worldBorderCords[2] : worldBorderCords[0];
        double maxZ = worldBorderCords[1] > worldBorderCords[3] ? worldBorderCords[1] : worldBorderCords[3];
        double minZ = worldBorderCords[1] > worldBorderCords[3] ? worldBorderCords[3] : worldBorderCords[1];

        return playerX >= maxX || playerX <= minX || playerZ >= maxZ || playerZ <= minZ;
    }

    public List<Location> getEntitySpawnLocations() {
        return entitySpawnLocations;
    }

    public List<Point> getGreenOres() {
        return greenOres;
    }

    public void setGreenOres(List<Point> greenOres) {
        this.greenOres = greenOres;
    }

    public List<Point> getYellowOres() {
        return yellowOres;
    }

    public void setYellowOres(List<Point> yellowOres) {
        this.yellowOres = yellowOres;
    }

    public List<Point> getBlueOres() {
        return blueOres;
    }

    public void setBlueOres(List<Point> blueOres) {
        this.blueOres = blueOres;
    }

    public List<Point> getRedOres() {
        return redOres;
    }

    public void setRedOres(List<Point> redOres) {
        this.redOres = redOres;
    }

    public List<Point> getChests() {
        return chests;
    }

    public void setChests(List<Point> chests) {
        this.chests = chests;
    }

    public Point getMiddle() {
        return middle;
    }

    public void setMiddle(Point middle) {
        this.middle = middle;
    }

    public List<IDPoint2Point> getBridges() {
        return bridges;
    }

    public void setBridges(List<IDPoint2Point> bridges) {
        this.bridges = bridges;
    }

    public List<BridgePoint> getBridgeData() {
        return bridgeData;
    }

    public void setBridgeData(List<BridgePoint> bridgeData) {
        this.bridgeData = bridgeData;
    }

    public String getBridgeType() {
        return bridgeType;
    }

    public void setBridgeType(String bridgeType) {
        this.bridgeType = bridgeType;
    }

    private void fillData() {
        ConfigurationSection worldData = Islands.getIsConfig().getWorldData(world.getName());

        String bridgeType = worldData.getString("bridgetype");
        setBridgeType(bridgeType);
        Islands.getInstance().getGame().setBridgeType(bridgeType);

        parseBridgeData(worldData);
        parseWorldBorder(worldData);
        parseOres(worldData);
        parseChestLocations(worldData);
        parseAnimalSpawns(worldData);
        parseTeamBorder(worldData);
        parsePlayerSpawns(worldData);
        parseMiddle(worldData);

        Islands.getInstance().getGame().getBridgeGenerator().setUp(this);
    }

    private void parseBridgeData(ConfigurationSection worldData) {
        HashMap<Integer, IDPoint2Point> tempBridgePoints = new HashMap<>();
        List<String> bridgePoints = worldData.getStringList("BridgePoints");
        for (String bridgePoint: bridgePoints) {
            String[] bridgeData = bridgePoint.split(",");
            int bridgeID;
            try {
                bridgeID = Integer.parseInt(bridgeData[0]);
            }catch (NumberFormatException e) {
                return;
            }
            Location loc = new Location(world, Double.parseDouble(bridgeData[1]), Double.parseDouble(bridgeData[2]), Double.parseDouble(bridgeData[3]));
            Point point = Point.convertVector2Point(loc.toVector());

            IDPoint2Point questionPoint = tempBridgePoints.get(bridgeID);
            if (questionPoint == null) {
                questionPoint = new IDPoint2Point();
                questionPoint.setId(bridgeID);
                tempBridgePoints.put(bridgeID, questionPoint);
            }

            if (questionPoint.getPoint1() != null && questionPoint.getPoint2() != null) return;
            if (questionPoint.getPoint1() == null) questionPoint.setPoint1(point);
            else if (questionPoint.getPoint2() == null) questionPoint.setPoint2(point);

        }
        List<IDPoint2Point> bridges = new ArrayList<>(tempBridgePoints.values());
        setBridges(bridges);

    }

    private void parseWorldBorder(ConfigurationSection worldData) {
        String[] worldBorderData = worldData.getString("worldBorder").split(",");
        worldBorderCords[0] = Double.parseDouble(worldBorderData[0]);
        worldBorderCords[1] = Double.parseDouble(worldBorderData[1]);
        worldBorderCords[2] = Double.parseDouble(worldBorderData[2]);
        worldBorderCords[3] = Double.parseDouble(worldBorderData[3]);
    }

    private void parseOres(ConfigurationSection worldData) {
        List<String> redOres = worldData.getStringList("RedOre");
        List<String> blueOres = worldData.getStringList("BlueOre");
        List<String> greenOres = worldData.getStringList("GreenOre");
        List<String> yellowOres = worldData.getStringList("YellowOre");

        //Red
        List<Point> redOrePoints = new ArrayList<>();
        for (String redOre : redOres) {
            String[] oreValues = redOre.split(",");
            Point orePoint = new Point(Double.parseDouble(oreValues[0]), Double.parseDouble(oreValues[1]), Double.parseDouble(oreValues[2]));
            redOrePoints.add(orePoint);
        }
        setRedOres(redOrePoints);

        //Blue
        List<Point> blueOrePoints = new ArrayList<>();
        for (String blueOre : blueOres) {
            String[] oreValues = blueOre.split(",");
            Point orePoint = new Point(Double.parseDouble(oreValues[0]), Double.parseDouble(oreValues[1]), Double.parseDouble(oreValues[2]));
            blueOrePoints.add(orePoint);
        }
        setBlueOres(blueOrePoints);

        //Green
        List<Point> greenOrePoints = new ArrayList<>();
        for (String greenOre : greenOres) {
            String[] oreValues = greenOre.split(",");
            Point orePoint = new Point(Double.parseDouble(oreValues[0]), Double.parseDouble(oreValues[1]), Double.parseDouble(oreValues[2]));
            greenOrePoints.add(orePoint);
        }
        setGreenOres(greenOrePoints);

        //Yellow
        List<Point> yellowOrePoints = new ArrayList<>();
        for (String yellowOre : yellowOres) {
            String[] oreValues = yellowOre.split(",");
            Point orePoint = new Point(Double.parseDouble(oreValues[0]), Double.parseDouble(oreValues[1]), Double.parseDouble(oreValues[2]));
            yellowOrePoints.add(orePoint);
        }
        setYellowOres(yellowOrePoints);

    }

    private void parseTeamBorder(ConfigurationSection worldData) {
        List<String> redBorderPoints = worldData.getStringList("RedSpawns");
        List<String> blueBorderPoints = worldData.getStringList("BlueSpawns");
        List<String> greenBorderPoints = worldData.getStringList("GreenSpawns");
        List<String> yellowBorderPoints = worldData.getStringList("YellowSpawns");

        //Red
        for (String redBorderPoint : redBorderPoints) {
            String[] borderPointParts = redBorderPoint.split(",");
            if (borderPointParts[0].toLowerCase() != "border") continue;
            Point borderPoint = new Point(Double.parseDouble(borderPointParts[1]), Double.parseDouble(borderPointParts[2]), Double.parseDouble(borderPointParts[3]));
            redBorder.add(borderPoint);
        }

        //Blue
        for (String blueBorderPoint : blueBorderPoints) {
            String[] borderPointParts = blueBorderPoint.split(",");
            if (borderPointParts[0].toLowerCase() != "border") continue;
            Point borderPoint = new Point(Double.parseDouble(borderPointParts[1]), Double.parseDouble(borderPointParts[2]), Double.parseDouble(borderPointParts[3]));
            blueBorder.add(borderPoint);
        }

        //Green
        for (String greenBorderPoint : greenBorderPoints) {
            String[] borderPointParts = greenBorderPoint.split(",");
            if (borderPointParts[0].toLowerCase() != "border") continue;
            Point borderPoint = new Point(Double.parseDouble(borderPointParts[1]), Double.parseDouble(borderPointParts[2]), Double.parseDouble(borderPointParts[3]));
            greenBorder.add(borderPoint);
        }

        //Yellow
        for (String yellowBorderPoint : yellowBorderPoints) {
            String[] borderPointParts = yellowBorderPoint.split(",");
            if (borderPointParts[0].toLowerCase() != "border") continue;
            Point borderPoint = new Point(Double.parseDouble(borderPointParts[1]), Double.parseDouble(borderPointParts[2]), Double.parseDouble(borderPointParts[3]));
            yellowBorder.add(borderPoint);
        }

    }

    private void parseChestLocations(ConfigurationSection worldData) {
        List<String> chestSpawnPoints = worldData.getStringList("Chests");
        List<Point> chestPoints = new ArrayList<>();
        for (String chestSpawnPoint : chestSpawnPoints) {
            String[] chestPointParts = chestSpawnPoint.split(",");
            Point chestPoint = new Point(Double.parseDouble(chestPointParts[0]), Double.parseDouble(chestPointParts[1]), Double.parseDouble(chestPointParts[2]));
            chestPoints.add(chestPoint);
        }
        setChests(chestPoints);
    }

    private void parseAnimalSpawns(ConfigurationSection worldData) {
        List<String> entitySpawnPoints = worldData.getStringList("EntitySpawns");
        for (String entitySpawnPoint : entitySpawnPoints) {
            String[] spawnPointParts = entitySpawnPoint.split(",");
            Location spawnLocation = new Location(world, Double.parseDouble(spawnPointParts[0]), Double.parseDouble(spawnPointParts[1]), Double.parseDouble(spawnPointParts[2]));
            this.entitySpawnLocations.add(spawnLocation);
        }

    }

    private void parsePlayerSpawns(ConfigurationSection worldData) {
        Islands islands = Islands.getInstance();
        List<String> redSpawnPoints = worldData.getStringList("RedSpawns");
        List<String> blueSpawnPoints = worldData.getStringList("BlueSpawns");
        List<String> greenSpawnPoints = worldData.getStringList("GreenSpawns");
        List<String> yellowSpawnPoints = worldData.getStringList("YellowSpawns");

        //Red
        for (String redSpawnPoint : redSpawnPoints) {
            String[] spawnPointParts = redSpawnPoint.split(",");
            if (spawnPointParts[0].toLowerCase() == "border") continue;
            IslandsTeam redTeam = islands.getTeamBySide(0);
            Location spawnLocation = new Location(world, Double.parseDouble(spawnPointParts[0]), Double.parseDouble(spawnPointParts[1]), Double.parseDouble(spawnPointParts[2]));
            redTeam.addSpawnPointLocation(spawnLocation);
        }

        //Blue
        for (String blueSpawnPoint : blueSpawnPoints) {
            String[] spawnPointParts = blueSpawnPoint.split(",");
            if (spawnPointParts[0].toLowerCase() == "border") continue;
            IslandsTeam blueTeam = islands.getTeamBySide(1);
            Location spawnLocation = new Location(world, Double.parseDouble(spawnPointParts[0]), Double.parseDouble(spawnPointParts[1]), Double.parseDouble(spawnPointParts[2]));
            blueTeam.addSpawnPointLocation(spawnLocation);
        }

        //Green
        for (String greenSpawnPoint : greenSpawnPoints) {
            String[] spawnPointParts = greenSpawnPoint.split(",");
            if (spawnPointParts[0].toLowerCase() == "border") continue;
            IslandsTeam greenTeam = islands.getTeamBySide(2);
            Location spawnLocation = new Location(world, Double.parseDouble(spawnPointParts[0]), Double.parseDouble(spawnPointParts[1]), Double.parseDouble(spawnPointParts[2]));
            greenTeam.addSpawnPointLocation(spawnLocation);
        }

        //Yellow
        for (String yellowSpawnPoint : yellowSpawnPoints) {
            String[] spawnPointParts = yellowSpawnPoint.split(",");
            if (spawnPointParts[0].toLowerCase() == "border") continue;
            IslandsTeam yellowTeam = islands.getTeamBySide(3);
            Location spawnLocation = new Location(world, Double.parseDouble(spawnPointParts[0]), Double.parseDouble(spawnPointParts[1]), Double.parseDouble(spawnPointParts[2]));
            yellowTeam.addSpawnPointLocation(spawnLocation);
        }
    }

    private void parseMiddle(ConfigurationSection worldData) {
        String middleData = worldData.get("middle").toString();
        String[] cords = middleData.split(",");
        Point middlePoint = new Point(Double.parseDouble(cords[0]), Double.parseDouble(cords[1]), Double.parseDouble(cords[2]));
        setMiddle(middlePoint);
    }
}
