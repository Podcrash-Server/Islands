package me.flaymed.islands.kits.skills.medic;

import com.podcrash.api.effect.status.Status;
import com.podcrash.api.effect.status.StatusApplier;
import com.podcrash.api.kits.iskilltypes.action.IConstruct;
import com.podcrash.api.kits.skilltypes.Passive;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class PeaceKeeper extends Passive implements IConstruct {

    private ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
    private ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
    private ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
    private String kit = "Medic";

    public PeaceKeeper() {

    }

    @Override
    public String getName() {
        return "Peace Keeper";
    }

    @Override
    public void afterConstruction() {
        StatusApplier.getOrNew(getPlayer()).applyStatus(Status.SATURATION, Integer.MAX_VALUE, 5, true);
    }

    @EventHandler
    public void clickItem(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (player == getPlayer()) {

            if (e.getCurrentItem() == chestplate || e.getCurrentItem() == leggings || e.getCurrentItem() == sword) {
                e.setCancelled(true);
                player.sendMessage(String.format("%s Skill> %sYou cannot use %s as a %s!", ChatColor.BLUE, ChatColor.GRAY, e.getCurrentItem().getType(), kit));
            }

        }

    }

    @EventHandler
    public void pickupItem(PlayerPickupItemEvent e) {
        Player player = e.getPlayer();
        Item item = e.getItem();

        if (player == getPlayer()) {
            if (item == chestplate || item == leggings || item == sword) {
                e.setCancelled(true);
                player.sendMessage(String.format("%s Skill> %sYou cannot use %s as a %s!", ChatColor.BLUE, ChatColor.GRAY, item.getType(), kit));
            }
        }

    }

}
