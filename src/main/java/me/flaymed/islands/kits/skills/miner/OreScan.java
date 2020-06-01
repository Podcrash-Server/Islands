package me.flaymed.islands.kits.skills.miner;

import com.podcrash.api.kits.enums.ItemType;
import com.podcrash.api.kits.iskilltypes.action.ICooldown;
import com.podcrash.api.kits.skilltypes.Instant;
import com.podcrash.api.util.MathUtil;
import net.jafama.FastMath;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.util.Vector;

public class OreScan extends Instant implements ICooldown {
    private final double radius = 8;
    private final OreData[] oreArray;

    private enum OreData {
        DIAMOND(Material.DIAMOND_PICKAXE, Material.DIAMOND_ORE, ChatColor.AQUA, "diamond"),
        GOLD(Material.GOLD_PICKAXE, Material.GOLD_ORE, ChatColor.YELLOW, "gold"),
        IRON(Material.IRON_PICKAXE, Material.IRON_ORE, ChatColor.WHITE, "iron"),
        COAL(Material.STONE_PICKAXE, Material.COAL_ORE, ChatColor.DARK_GRAY, "coal");

        private Material pickaxe;
        private Material ore;
        private ChatColor color;
        private String name;

        OreData(Material pickaxe, Material ore, ChatColor color, String name) {
            this.ore = ore;
            this.color = color;
            this.name = name;
            this.pickaxe = pickaxe;
        }
    }
    public OreScan() {
        this.oreArray = OreData.values();
    }

    private OreData findByPickaxe(Material material) {
        for (OreData data : oreArray) {
            if (data.pickaxe.equals(material))
                return data;
        }
        return null;
    }
    @Override
    protected void doSkill(PlayerEvent event, Action action) {
        Player player = event.getPlayer();
        if (onCooldown()) {
            player.sendMessage(getCooldownMessage());
            return;
        }
        Material inHand = player.getItemInHand().getType();
        int playerX = player.getLocation().getBlockX();
        int playerY = player.getLocation().getBlockY();
        int playerZ = player.getLocation().getBlockZ();

        int diagRadius = (int) (radius * 1.414);
        int vertRadius = (int) (radius * 1.732);

        int bottomX = playerX - diagRadius;
        int higherX = playerX + diagRadius;

        int bottomY = playerY - vertRadius;
        int higherY = playerY + vertRadius;

        int bottomZ = playerZ - diagRadius;
        int higherZ = playerZ + diagRadius;

        double offset = 0.5;

        boolean canceled = false;

        setLastUsed(System.currentTimeMillis());
        for (int x = bottomX; x <= higherX; x++) {
            if (canceled) {
                canceled = false;
                break;
            }
            for (int y = bottomY; y <= higherY; y++) {
                if (canceled) {
                    break;
                }
                for (int z = bottomZ; z <= higherZ; z++) {
                    if (canceled) {
                        break;
                    }
                    double distance = FastMath.pow2(playerX - x) + FastMath.pow2(playerY - y) + FastMath.pow2(playerZ - z);
                    
                    Block block = player.getWorld().getBlockAt(x, y, z);

                    boolean searchConcluded = x + 1 >= higherX && y + 1 >= higherY && z + 1 >= higherZ;
                    OreData data = findByPickaxe(inHand);
                    if (data == null)
                        break; //this should not happen unless it's a wooden pickaxe
                    if (block.getType().equals(data.ore)) {
                        Location target = new Location(player.getWorld(), x + offset, y + offset, z + offset);
                        Vector dir = target.clone().subtract(player.getEyeLocation()).toVector();
                        Location loc = player.getLocation().setDirection(dir);
                        player.teleport(loc);
                        player.sendMessage(String.format(ChatColor.BLUE + "Skill>%s Success, located %s%s ore%s! ", ChatColor.GRAY, data.color, data.name, ChatColor.GRAY));
                        canceled = true;
                    } else if (searchConcluded) {
                        player.sendMessage(String.format(ChatColor.BLUE + "Skill>%s No %s ore found ;-;", ChatColor.GRAY, data.name));
                        canceled = true;
                    }
                }
            }
        }
    }

    @Override
    public String getName() {
        return "Ore Scan";
    }

    @Override
    public float getCooldown() {
        return 30;
    }

    @Override
    public ItemType getItemType() {
        return ItemType.PICKAXE;
    }
}
