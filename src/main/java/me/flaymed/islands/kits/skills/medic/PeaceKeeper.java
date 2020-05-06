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
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PeaceKeeper extends Passive implements IConstruct {

    private Material chestplate = Material.DIAMOND_CHESTPLATE;
    private Material leggings = Material.DIAMOND_LEGGINGS;
    private Material sword = Material.DIAMOND_SWORD;
    private String kit = "Medic";
    private String noUseMessage = String.format("%s Skill> %sYou cannot use this item as a %s!", ChatColor.BLUE, ChatColor.GRAY, kit);

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

            if (e.getCurrentItem().getType().equals(chestplate) || e.getCurrentItem().getType().equals(leggings) || e.getCurrentItem().getType().equals(sword)) {
                e.setCancelled(true);
                player.sendMessage(noUseMessage);
            }

        }

    }

    @EventHandler
    public void pickupItem(PlayerPickupItemEvent e) {
        Player player = e.getPlayer();
        Item item = e.getItem();

        if (player == getPlayer()) {
            if (item.getType().equals(chestplate) || item.getType().equals(leggings) || item.getType().equals(sword)) {
                e.setCancelled(true);
                player.sendMessage(noUseMessage);
            }
        }

    }

    @EventHandler
    public void playerClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();
        Material holding = player.getItemInHand().getType();

        if (player == getPlayer()) {
            if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
                if (holding.equals(chestplate) || holding.equals(leggings) || holding.equals(sword)) {
                    e.setCancelled(true);
                    player.sendMessage(noUseMessage);
                }
            }
        }

    }

}
