package me.flaymed.islands.kits.skills.miner;

import com.podcrash.api.kits.enums.ItemType;
import com.podcrash.api.kits.iskilltypes.action.ICooldown;
import com.podcrash.api.kits.skilltypes.Instant;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.util.Vector;

public class OreScan extends Instant implements ICooldown {

    public OreScan() {

    }

    @Override
    protected void doSkill(PlayerEvent event, Action action) {
        if(!onCooldown() && this.getPlayer() == event.getPlayer()) {
            Player player = event.getPlayer();
            Material inHand = player.getItemInHand().getType();
            int playerX = player.getLocation().getBlockX();
            int playerY = player.getLocation().getBlockY();
            int playerZ = player.getLocation().getBlockZ();

            int radius = 8;

            int diagRadius = (int) (radius * 1.414);
            int vertRadius = (int) (radius * 1.732);

            int bottomX = playerX - diagRadius;
            int higherX = playerX + diagRadius;

            int bottomY = playerY - vertRadius;
            int higherY = playerY + vertRadius;

            int bottomZ = playerZ - diagRadius;
            int higherZ = playerZ + diagRadius;

            boolean canceled = false;

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


                        Block block = player.getWorld().getBlockAt(x, y, z);

                        boolean searchConcluded = x + 1 >= higherX && y + 1 >= higherY && z + 1 >= higherZ;
                        if (inHand.equals(Material.DIAMOND_PICKAXE)) {

                            if (block.getType().equals(Material.DIAMOND_ORE)) {
                                Location target = new Location(player.getWorld(), x, y, z);
                                Vector dir = target.clone().subtract(player.getEyeLocation()).toVector();
                                Location loc = player.getLocation().setDirection(dir);
                                player.teleport(loc);
                                player.sendMessage(String.format(ChatColor.BLUE + "Skill>%s Success, located %sdiamond ore%s! ", ChatColor.GRAY, ChatColor.AQUA, ChatColor.GRAY));
                                canceled = true;
                            } else if (searchConcluded) {
                                player.sendMessage(String.format(ChatColor.BLUE + "Skill>%S No diamond ore found ;-;", ChatColor.GRAY));
                                canceled = true;
                            }
                            continue;
                        }

                        if (inHand.equals(Material.GOLD_PICKAXE)) {
                            if(block.getType().equals(Material.GOLD_ORE)) {
                                Location target = new Location(player.getWorld(), x, y, z);
                                Vector dir = target.clone().subtract(player.getEyeLocation()).toVector();
                                Location loc = player.getLocation().setDirection(dir);
                                player.teleport(loc);
                                player.sendMessage(String.format(ChatColor.BLUE + "Skill>%s Success, located %sgold ore%s! ", ChatColor.GRAY, ChatColor.YELLOW, ChatColor.GRAY));
                                canceled = true;
                            } else if (searchConcluded) {
                                player.sendMessage(String.format(ChatColor.BLUE + "Skill>%S No gold ore found ;-;", ChatColor.GRAY));
                                canceled = true;
                            }
                            continue;
                        }

                        if (inHand.equals(Material.IRON_PICKAXE)) {
                            if (block.getType().equals(Material.IRON_ORE)) {
                                Location target = new Location(player.getWorld(), x, y, z);
                                Vector dir = target.clone().subtract(player.getEyeLocation()).toVector();
                                Location loc = player.getLocation().setDirection(dir);
                                player.teleport(loc);
                                player.sendMessage(String.format(ChatColor.BLUE + "Skill>%s Success, located %siron ore%s! ", ChatColor.GRAY, ChatColor.WHITE, ChatColor.GRAY));
                                canceled = true;
                            } else if (searchConcluded) {
                                player.sendMessage(String.format(ChatColor.BLUE + "Skill>%S No iron ore found ;-;", ChatColor.GRAY));
                                canceled = true;
                            }
                            continue;
                        }

                        if (inHand.equals(Material.STONE_PICKAXE)) {
                            if (block.getType().equals(Material.COAL_ORE)) {
                                Location target = new Location(player.getWorld(), x, y, z);
                                Vector dir = target.clone().subtract(player.getEyeLocation()).toVector();
                                Location loc = player.getLocation().setDirection(dir);
                                player.teleport(loc);
                                player.sendMessage(String.format(ChatColor.BLUE + "Skill>%s Success, located %scoal ore%s! ", ChatColor.GRAY, ChatColor.DARK_GRAY, ChatColor.GRAY));
                                canceled = true;
                            } else if (searchConcluded) {
                                player.sendMessage(String.format(ChatColor.BLUE + "Skill>%S No coal ore found ;-;", ChatColor.GRAY));
                                canceled = true;
                            }
                            continue;
                        }


                    }
                }
            }

            setLastUsed(System.currentTimeMillis());

        } else this.getPlayer().sendMessage(getCooldownMessage());
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
