package me.flaymed.islands.location;

import net.jafama.FastMath;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.IBlockData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.material.Gate;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import java.util.*;

public final class BlockUtil {
    private static final double RADIAN = Math.PI/180D;
    private static final Material[] passables = new Material[]{
            Material.AIR,
            Material.SAPLING,
            Material.WATER,
            Material.STATIONARY_WATER,
            Material.LAVA,
            Material.STATIONARY_LAVA,
            Material.POWERED_RAIL,
            Material.DETECTOR_RAIL,
            Material.WEB,
            Material.LONG_GRASS,
            Material.DEAD_BUSH,
            Material.YELLOW_FLOWER,
            Material.RED_ROSE,
            Material.BROWN_MUSHROOM,
            Material.RED_MUSHROOM,
            Material.TORCH,
            Material.FIRE,
            Material.REDSTONE_WIRE,
            Material.CROPS,
            Material.SIGN_POST,
            Material.RAILS,
            Material.WALL_SIGN,
            Material.LEVER,
            Material.STONE_PLATE,
            Material.WOOD_PLATE,
            Material.REDSTONE_TORCH_OFF,
            Material.REDSTONE_TORCH_ON,
            Material.STONE_BUTTON,
            Material.SNOW,
            Material.LADDER,
            Material.SUGAR_CANE_BLOCK,
            Material.PORTAL,
            Material.PUMPKIN_STEM,
            Material.MELON_STEM,
            Material.VINE,
            Material.NETHER_WARTS,
            Material.TRIPWIRE_HOOK,
            Material.TRIPWIRE,
            Material.CARROT,
            Material.POTATO,
            Material.WOOD_BUTTON,
            Material.GOLD_PLATE,
            Material.IRON_PLATE,
            Material.REDSTONE_COMPARATOR_OFF,
            Material.REDSTONE_COMPARATOR_ON,
            Material.DIODE_BLOCK_OFF,
            Material.DIODE_BLOCK_ON,
            Material.CARPET,
            Material.ACTIVATOR_RAIL,
            Material.DOUBLE_PLANT,
            Material.STANDING_BANNER,
            Material.WALL_BANNER
    };

    private static final Material[] fenceGates = new Material[]{
            Material.FENCE_GATE,
            Material.SPRUCE_FENCE_GATE,
            Material.BIRCH_FENCE_GATE,
            Material.JUNGLE_FENCE_GATE,
            Material.DARK_OAK_FENCE_GATE,
            Material.ACACIA_FENCE_GATE
    };

    public static boolean isSign(Block block) {
        Material signType = block.getType();
        return signType == Material.WALL_SIGN || signType == Material.SIGN_POST;
    }


    public static double get2dDistanceSquared(Vector location1, Vector location2) {
        double x1 = location1.getX();
        double x2 = location2.getX();
        double z1 = location1.getZ();
        double z2 = location2.getZ();

        double xS = x2 - x1;
        double zS = z2 - z1;

        return xS * xS + zS * zS;
    }

    /**
     * Update a block and refresh it really fast
     * @param world
     * @param x
     * @param y
     * @param z
     * @param material
     * @param data
     */
    public static void setBlockFast(World world, int x, int y, int z, Material material, byte data) {
        int combined = material.getId() + (data << 12);
        setBlockFast(world, x, y, z, combined);
    }

    public static void setBlockFast(World world, int x, int y, int z, int combined) {
        world.getChunkAtAsync(x >> 4, z >> 4, bukkitChunk -> {
            net.minecraft.server.v1_8_R3.Chunk chunk = ((CraftChunk) bukkitChunk).getHandle();

            final BlockPosition bp = new BlockPosition(x, y, z);
            final IBlockData ibd = net.minecraft.server.v1_8_R3.Block.getByCombinedId(combined);
            chunk.a(bp, ibd);
            chunk.world.notify(bp);
        });
    }

    public static void setBlock(Location location, Material material) {
        CraftBlockUpdater updater = CraftBlockUpdater.getMassBlockUpdater(location.getWorld());
        updater.setBlock(location.getBlockX(), location.getBlockY(), location.getBlockZ(), material);
    }

    public static void setBlock(Location location, int blockID) {
        CraftBlockUpdater updater = CraftBlockUpdater.getMassBlockUpdater(location.getWorld());
        updater.setBlock(location.getBlockX(), location.getBlockY(), location.getBlockZ(), blockID);
    }

    public static void setBlock(Location location, Material material, byte data) {
        CraftBlockUpdater updater = CraftBlockUpdater.getMassBlockUpdater(location.getWorld());
        updater.setBlock(location.getBlockX(), location.getBlockY(), location.getBlockZ(), material, data);
    }
    public static void replaceBlock(Location location, Material material, int data, boolean physics){
        if (data < -128 || data > 127)
            throw new IllegalArgumentException("data must be between -128 and 127. was " + data);
        CraftWorld craftWorld = ((CraftWorld) location.getWorld());
        /*
        BlockPosition blockPosition = new BlockPosition(location.getX(), location.getY(), location.getZ());
        IBlockData blockData = craftWorld.getHandle().getType(blockPosition);
        craftWorld.getHandle().setTypeAndData(blockPosition, blockData.getBlock().fromLegacyData())
        */

        CraftBlock craftBlock = (CraftBlock) location.getBlock();
        craftBlock.setTypeIdAndData(material.getId(), (byte) data, physics);
    }

}