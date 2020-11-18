package me.flaymed.islands.kits.skills.lobbykit;

import com.podcrash.gamecore.kits.Ability;
import com.podcrash.gamecore.kits.Kit;
import com.podcrash.gamecore.kits.KitPlayer;
import com.podcrash.gamecore.kits.abilitytype.Interact;
import me.flaymed.islands.inventory.KitSelectGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class KitSelect extends Ability implements Interact {
    @Override
    public String getName() {
        return "Kit Select";
    }

    @Override
    public ItemStack getItem() {
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        ItemMeta swordMeta = sword.getItemMeta();
        swordMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Kit Selector");
        sword.setItemMeta(swordMeta);
        return sword;
    }

    @Override
    public void doAbility() {
        getKitPlayer().getPlayer().openInventory(KitSelectGUI.getInstance().getInventory());
    }

    @EventHandler
    public void playerSelectTeam(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        KitPlayer kitPlayer = getKitPlayer();
        if (kitPlayer.getPlayer().getUniqueId() != player.getUniqueId()) return;
        if (e.getInventory() != KitSelectGUI.getInstance().getInventory()) return;
        if (item == null || item.getType() == Material.AIR) return;
        for (Kit kit : KitSelectGUI.getInstance().getKits()) {
            if (item != kit.getItem()) continue;
            kitPlayer.selectKit(kit);
            player.sendMessage(String.format("%sIslands>%s You selected %s%s Kit%s!", ChatColor.BLUE, ChatColor.GRAY, ChatColor.GREEN, kit.getName(), ChatColor.GRAY));
            player.closeInventory();
            break;
        }

    }

    @Override
    public List<Action> getActions() {
        return Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK, Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK);
    }
}
