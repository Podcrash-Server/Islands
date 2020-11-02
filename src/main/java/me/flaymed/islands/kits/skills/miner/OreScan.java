package me.flaymed.islands.kits.skills.miner;

import com.podcrash.gamecore.GameCore;
import com.podcrash.gamecore.kits.Ability;
import com.podcrash.gamecore.kits.abilitytype.Cooldown;
import com.podcrash.gamecore.kits.abilitytype.Interact;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import java.util.Arrays;
import java.util.List;

public class OreScan extends Ability implements Cooldown, Interact {
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

    @Override
    public String getName() {
        return "Ore Scan";
    }

    @Override
    public ItemStack getItem() {
        return null;
    }

    @Override
    public void doAbility() {
        Player player = getKitPlayer().getPlayer();
        if (onCooldown()) {
            player.sendMessage(getCooldownMessage());
            return;
        }
        Material inHand = player.getItemInHand().getType();
        double playerX = player.getLocation().getX();
        double playerY = player.getLocation().getY();
        double playerZ = player.getLocation().getZ();

        int diagRadius = (int) (radius * 1.414);
        int vertRadius = (int) (radius * 1.732);

        double bottomX = playerX - diagRadius;
        double  higherX = playerX + diagRadius;

        double  bottomY = playerY - vertRadius;
        double higherY = playerY + vertRadius;

        double bottomZ = playerZ - diagRadius;
        double higherZ = playerZ + diagRadius;

        double offset = 0.5;

        boolean canceled = false;

        for (double  x = bottomX; x <= higherX; x++) {
            if (canceled) {
                canceled = false;
                break;
            }
            for (double  y = bottomY; y <= higherY; y++) {
                if (canceled) {
                    break;
                }
                for (double  z = bottomZ; z <= higherZ; z++) {
                    if (canceled) {
                        break;
                    }
                    //we may need this so that we find the actual closest block to the player instead of finding the first block we find.
                    //double distance = FastMath.pow2(playerX - x) + FastMath.pow2(playerY - y) + FastMath.pow2(playerZ - z);

                    Block block = player.getWorld().getBlockAt((int) x, (int) y, (int) z);

                    boolean searchConcluded = x + 1 >= higherX && y + 1 >= higherY && z + 1 >= higherZ;
                    OreData data = findByPickaxe(inHand);
                    if (data == null)
                        break; //this should not happen unless it's a wooden pickaxe
                    if (block.getType().equals(data.ore)) {
                        Location target = new Location(player.getWorld(), x + offset, y + offset, z + offset);
                        Vector dir = target.clone().subtract(player.getEyeLocation()).toVector();
                        Location loc = player.getLocation().setDirection(dir);
                        player.teleport(loc);
                        player.sendMessage(String.format("%s Success, located %s%s ore%s! ", GameCore.getKitPrefix(), data.color, data.name, ChatColor.GRAY));
                        canceled = true;
                    } else if (searchConcluded) {
                        player.sendMessage(String.format("%s No %s ore found ;-;", GameCore.getKitPrefix(), data.name));
                        canceled = true;
                    }
                }
            }
        }
    }

    @Override
    public List<Action> getActions() {
        return Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK);
    }

    @Override
    public float getCooldown() {
        return 30;
    }

    private OreData findByPickaxe(Material material) {
        for (OreData data : oreArray) {
            if (data.pickaxe.equals(material))
                return data;
        }
        return null;
    }

}
