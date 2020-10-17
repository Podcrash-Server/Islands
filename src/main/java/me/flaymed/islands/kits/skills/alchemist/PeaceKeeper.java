package me.flaymed.islands.kits.skills.alchemist;

import com.podcrash.gamecore.GameCore;
import com.podcrash.gamecore.kits.Ability;
import com.podcrash.gamecore.kits.abilitytype.AfterConstruct;
import com.podcrash.gamecore.kits.abilitytype.Passive;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PeaceKeeper extends Ability implements Passive, AfterConstruct {
    //add more if needed.
    private final Set<Material> DIAMOND_MATERIALS = new HashSet<>(
        Arrays.asList(Material.DIAMOND_AXE, Material.DIAMOND_SWORD,
                Material.DIAMOND_LEGGINGS, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_HELMET, Material.DIAMOND_BOOTS));
    private String kit = "Alchemist";
    private String noUseMessage = String.format("%s You cannot use this item as a %s!", GameCore.getKitPrefix(), kit);


    @Override
    public String getName() {
        return "Peace Keeper";
    }

    @Override
    public ItemStack getItem() {
        return null;
    }

    @Override
    public Player getPlayer() {
        return getKitPlayer().getPlayer();
    }

    @Override
    public void afterConstruct() {
        getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 5));
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
