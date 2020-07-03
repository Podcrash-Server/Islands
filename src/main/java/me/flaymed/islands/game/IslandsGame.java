package me.flaymed.islands.game;

import com.google.common.reflect.ClassPath;
import com.podcrash.api.annotations.GameData;
import com.podcrash.api.damage.DamageSource;
import com.podcrash.api.db.pojos.map.IslandsMap;
import com.podcrash.api.db.pojos.map.Point;
import com.podcrash.api.game.*;
import com.podcrash.api.game.objects.ItemObjective;
import com.podcrash.api.game.objects.WinObjective;
import com.podcrash.api.listeners.DeathHandler;
import com.podcrash.api.plugin.PodcrashSpigot;
import com.podcrash.api.world.BlockUtil;
import me.flaymed.islands.annotations.BridgeType;
import me.flaymed.islands.bridges.maker.BridgeGenerator;
import me.flaymed.islands.events.GameStageEvent;
import me.flaymed.islands.util.ChestGen;
import me.flaymed.islands.util.ore.OreVeinSetting;
import me.flaymed.islands.util.ore.VeinGen;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;

@GameData(name = "Islands")
public class IslandsGame extends Game {
    public static final DamageSource WATER_DAMAGE = () -> "Water Damage";
    private GameStage stage;
    private BridgeGenerator bridgeGenerator;
    private String bridgeType;

    public IslandsGame(int id, String name) {
        super(id, name, GameType.DOM);
        DeathHandler.setAllowPlayerDrops(true);
    }

    @Override
    public int getAbsoluteMinPlayers() {
        return 6;
    }

    @Override
    public void leaveCheck() {

    }

    @Override
    public Class<? extends com.podcrash.api.db.pojos.map.GameMap> getMapClass() {
        return IslandsMap.class;
    }

    @Override
    public TeamSettings getTeamSettings() {
        return new TeamSettings.Builder()
            .setTeamColors(TeamEnum.RED, TeamEnum.BLUE)
            .setMax(12)
            .setMin(6)
            .build();
    }

    @Override
    public String getMode() {
        return "Islands";
    }

    public GameStage getStage() {
        return stage;
    }

    @Override
    public String getPresentableResult() {
        return null;
    }

    public void setStage(GameStage stage) {
        this.stage = stage;
        Bukkit.getPluginManager().callEvent(new GameStageEvent(this, this.stage));
    }
    @Override
    public List<WinObjective> getWinObjectives() {
        return Collections.emptyList();
    }

    @Override
    public List<ItemObjective> getItemObjectives() {
        return Collections.emptyList();
    }

    /**
     * Uses a consumer to avoid the creation of an array to loop through. (these bois are pretty big)
     * @param blockConsumer
     */
    public void consumeChest(Consumer<Chest> blockConsumer) {
        List<Point> chestPoints = ((IslandsMap) this.getMap()).getChests();
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

    public void generateOres() {
        IslandsMap map = (IslandsMap) getMap();
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
                BlockUtil.setBlock(block.getLocation(), Material.STONE);
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
        runnable.runTask(PodcrashSpigot.getInstance());
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
}
