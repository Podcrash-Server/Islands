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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PeaceKeeper extends Passive implements IConstruct {
    //add more if needed.
    private final Set<Material> DIAMOND_MATERIALS = new HashSet<>(
        Arrays.asList(Material.DIAMOND_AXE, Material.DIAMOND_SWORD,
                Material.DIAMOND_LEGGINGS, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_HELMET, Material.DIAMOND_BOOTS));
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

        if (player != getPlayer())
            return;
        if (!DIAMOND_MATERIALS.contains(e.getCurrentItem().getType())) return;
        e.setCancelled(true);
        player.sendMessage(noUseMessage);
    }

    @EventHandler
    public void pickupItem(PlayerPickupItemEvent e) {
        Player player = e.getPlayer();
        Item item = e.getItem();

        if (player != getPlayer())
            return;
        if (!DIAMOND_MATERIALS.contains(item.getType()))
            return;
        e.setCancelled(true);
        player.sendMessage(noUseMessage);
    }

    @EventHandler
    public void playerClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();
        Material holding = player.getItemInHand().getType();
        if (player != getPlayer()) return;
        if (!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!DIAMOND_MATERIALS.contains(holding)) return;

        e.setCancelled(true);
        player.sendMessage(noUseMessage);
        player.updateInventory();
    }
}
