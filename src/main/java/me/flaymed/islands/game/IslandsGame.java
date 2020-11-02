package me.flaymed.islands.game;

import com.google.common.reflect.ClassPath;
import com.podcrash.gamecore.game.Game;
import com.podcrash.gamecore.kits.KitPlayer;
import com.podcrash.gamecore.kits.KitPlayerManager;
import com.podcrash.gamecore.utils.MathUtil;
import me.flaymed.islands.Islands;
import me.flaymed.islands.annotations.BridgeType;
import me.flaymed.islands.bridges.maker.BridgeGenerator;
import me.flaymed.islands.events.GameStageEvent;
import me.flaymed.islands.kits.classes.LobbyKit;
import me.flaymed.islands.location.Point;
import me.flaymed.islands.teams.IslandsTeam;
import me.flaymed.islands.util.ChestGen;
import me.flaymed.islands.util.ore.OreVeinSetting;
import me.flaymed.islands.util.ore.VeinGen;
import me.flaymed.islands.world.IslandsMap;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.material.Mushroom;
import org.bukkit.scheduler.BukkitRunnable;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;

public class IslandsGame extends Game {
    private int waterDmgTaskId;
    private int entitySpawnTaskId;
    private GameStage stage;
    private BridgeGenerator bridgeGenerator;
    private String bridgeType;
    //TODO: GAMEWORLMAP AND MAP STUFF
    private World gameWorld;
    private IslandsMap map;
    private long endTime;

    public IslandsGame() {
        super("Islands", 20, 48, 60);
    }

    public GameStage getStage() {
        return stage;
    }

    public void setStage(GameStage stage) {
        this.stage = stage;
        Bukkit.getPluginManager().callEvent(new GameStageEvent(this, stage));
    }

    public long getEndTime() {
        return endTime;
    }

    /**
     * Uses a consumer to avoid the creation of an array to loop through. (these bois are pretty big)
     * @param blockConsumer
     */
    public void consumeChest(Consumer<Chest> blockConsumer) {
        List<Point> chestPoints = getMap().getChests();
        for (Point point : chestPoints) {
            Block block = getGameWorld().getBlockAt((int) point.getX(), (int) point.getY(), (int) point.getZ());
            if (!(block.getState() instanceof Chest))
                throw new IllegalStateException("the block found is not a chest! " + point);
            blockConsumer.accept((Chest) block.getState());
        }
    }

    /**
     * Uses a consumer to avoid the creation of an array to loop through. (these bois are pretty big)
     * @param blockConsumer
     */
    public void consumeOre(List<Point> orePoints, Consumer<Block> blockConsumer) {
        final Set<Material> ores = new HashSet<>(Arrays.asList(Material.DIAMOND_ORE, Material.REDSTONE_ORE, Material.EMERALD_ORE, Material.GOLD_ORE));
        for (Point point : orePoints) {
            Block block = getGameWorld().getBlockAt((int) point.getX(), (int) point.getY(), (int) point.getZ());
            Chunk chunk = getGameWorld().getChunkAt(block);
            //make this part better
            if (!chunk.isLoaded()) chunk.load();
            Material possOre = block.getType();
            if (!ores.contains(possOre))
                continue;
            blockConsumer.accept(block);
        }
    }

    public void setIslandsMap(IslandsMap map) {
        this.map = map;
    }

    public IslandsMap getMap() {
        return map;
    }

    public void setGameWorldName(World world) {
        this.gameWorld = world;
    }

    public World getGameWorld() {
        return gameWorld;
    }

    public void generateOres() {
        IslandsMap map = getMap();
        //will use the sam random seed to make sure the ores are roughly the same per island
        final Random random = new Random();
        Consumer<Block> genBlockOre = block -> {
            //get a random ore
            double guess = random.nextDouble();

            OreVeinSetting rng = null;
            for (OreVeinSetting setting : OreVeinSetting.details()) {
                double chance = setting.getContinueChance();
                if (guess < chance) {
                    rng = setting;
                    break;
                }
            }
            if (rng == null)
                throw new IllegalStateException("the rng ore must not be null! Chance configuration must have went wrong.");
            if (rng == OreVeinSetting.STONE) {
                block.getLocation().getWorld().getBlockAt(block.getLocation()).setType(Material.STONE);
                return;
            }
            VeinGen generator = VeinGen.fromOreSetting(rng);
            generator.startLocation(block.getLocation());
            generator.generate();
        };

        Consumer<Chest> chestConsumer = ChestGen::generateItem;
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                consumeOre(map.getRedOres(), genBlockOre);
                consumeOre(map.getBlueOres(), genBlockOre);
                consumeOre(map.getGreenOres(), genBlockOre);
                consumeOre(map.getYellowOres(), genBlockOre);
                consumeChest(chestConsumer);
            }
        };
        runnable.runTask(Islands.getInstance());
    }


    public String getBridgeType() {
        return bridgeType;
    }

    public void setBridgeType(String bridgeType) {
        this.bridgeType = bridgeType;
        try {
            makeBridgeGenerator(bridgeType);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Use reflection to find which bridge generator to use.
     * @param bridgeType
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void makeBridgeGenerator(String bridgeType) throws IOException, ClassNotFoundException {
        String path = "me.flaymed.islands.bridges.maker";
        ClassPath cp = ClassPath.from(BridgeGenerator.class.getClassLoader());
        Set<ClassPath.ClassInfo> bridgeClassSet = cp.getTopLevelClasses(path);

        for (ClassPath.ClassInfo bridgeInfo : bridgeClassSet) {
            Class<?> bridgeClass = Class.forName(bridgeInfo.getName());
            if (!bridgeClass.isAnnotationPresent(BridgeType.class))
                continue;
            BridgeType annotation = bridgeClass.getAnnotation(BridgeType.class);
            boolean isSameType = annotation.type().equalsIgnoreCase(bridgeType);
            if (!isSameType)
                continue;
            this.bridgeGenerator = (BridgeGenerator) emptyConstructor(bridgeClass);
            break;
        }
    }

    public void fallBridge(int delay) {
        //Bukkit.broadcastMessage("fall the bridges!");
        World world;
        if ((world = getGameWorld()) == null)
            return;
        bridgeGenerator.generate(world, delay);
    }
    public void destroyBridge(int delay) {
        //Bukkit.broadcastMessage("destroy the bridges!");
        World world;
        if ((world = getGameWorld()) == null)
            return;
        bridgeGenerator.destroy(world, delay);
    }

    public BridgeGenerator getBridgeGenerator() {
        return bridgeGenerator;
    }

    private static <T> T emptyConstructor(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getConstructor();
            return constructor.newInstance();
        }catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void startWaterTask() {
        int taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(Islands.getInstance(), () -> {
            for (Player player : KitPlayerManager.getPlayers()) {
                if (player.getGameMode() == GameMode.SPECTATOR) continue;
                Material m = player.getLocation().getBlock().getType();
                if (m == Material.STATIONARY_WATER || m == Material.WATER) {
                    player.damage(1);
                    player.playSound(player.getLocation(), Sound.WATER, 2, 3);
                }

            }
        },0, 10);

        this.waterDmgTaskId = taskid;
    }

    private void startEntityTask() {
        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Islands.getInstance(), () -> {

            List<EntityType> allowedEntities = Arrays.asList(EntityType.COW, EntityType.PIG, EntityType.CHICKEN);
            List<Material> allowedMushroomTypes = Arrays.asList(Material.RED_MUSHROOM, Material.BROWN_MUSHROOM);
            List<Location> locations = getMap().getEntitySpawnLocations();

            for (Location location : locations) {
                getGameWorld().spawnEntity(location, allowedEntities.get(MathUtil.randomInt(allowedEntities.size())));
                Block possibleShroom = getGameWorld().getBlockAt(location);
                if (possibleShroom.getType() != Material.AIR || possibleShroom.getType() != null) return;
                possibleShroom.setType(allowedMushroomTypes.get(MathUtil.randomInt(allowedMushroomTypes.size())));
            }


        }, 40, 2 * 60 * 20); //120 seconds between spawn

        this.entitySpawnTaskId = taskId;
    }

    @Override
    public void start() {
        generateOres();
        setStage(GameStage.PREPARE);
        destroyBridge(5);
        this.endTime = System.currentTimeMillis() + 8L * 60L * 1000L;
        startWaterTask();
        startEntityTask();
        for (IslandsTeam team : Islands.getInstance().getTeams()) {
            HashMap<Player, Location> spawnPoints = team.getPlayerSpawnPoints();
            for (Map.Entry<Player, Location> spawnPoint: spawnPoints.entrySet()) {
                Player player = spawnPoint.getKey();
                Location location = spawnPoint.getValue();
                player.teleport(location);
            }
        }
        KitPlayerManager.gameStarts();
    }

    @Override
    public void stop() {
        //TODO: Change this?
        setStage(GameStage.LOBBY);
        for (KitPlayer kitPlayer : KitPlayerManager.getKitPlayers()) {
            kitPlayer.selectKit(new LobbyKit());
            kitPlayer.getPlayer().setGameMode(GameMode.ADVENTURE);
            kitPlayer.equip();
            //TODO: tp them to lobby
        }
        Bukkit.getScheduler().cancelTask(this.waterDmgTaskId);
        Bukkit.getScheduler().cancelTask(this.entitySpawnTaskId);
    }
}
