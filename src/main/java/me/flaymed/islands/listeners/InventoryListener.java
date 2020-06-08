package me.flaymed.islands.listeners;

import com.podcrash.api.game.GameManager;
import com.podcrash.api.game.GameState;
import com.podcrash.api.kits.KitPlayer;
import com.podcrash.api.kits.KitPlayerManager;
import com.podcrash.api.listeners.ListenerBase;
import com.podcrash.api.sound.SoundPlayer;
import me.flaymed.islands.inventory.IslandsInventoryItem;
import me.flaymed.islands.inventory.IslandsInventoryManager;
import me.flaymed.islands.kits.classes.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class InventoryListener extends ListenerBase {

    private Inventory kitSelectInventory;

    public InventoryListener(JavaPlugin plugin) {
        super(plugin);

        this.kitSelectInventory = createKitSelectInventory();
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        if (GameManager.getGame() == null) return;
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK || block == null) return;
        if (block.getType().equals(Material.ENCHANTMENT_TABLE) && GameManager.getGame().getGameState() == GameState.LOBBY) {
            e.setCancelled(true);
            openKitGui(e.getPlayer());
        }
    }

    @EventHandler
    public void playerClickInventory(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        Inventory inventory = e.getInventory();
        InventoryAction action = e.getAction();
        ItemStack item = e.getCurrentItem();
        KitPlayerManager kpm = KitPlayerManager.getInstance();
        if (GameManager.getGame() == null) return;
        if (inventory == null || action == null || GameManager.getGame().getGameState() != GameState.LOBBY) return;
        if (inventory != IslandsInventoryManager.getKitSelectInventory()) return;

        if (item == IslandsInventoryItem.BERSERKER_AXE_SELECT.getItem())
            equipKit(player, new Berserker(player), kpm);
        if (item == IslandsInventoryItem.BRAWLER_SWORD_SELECT.getItem())
            equipKit(player, new Brawler(player), kpm);
        if (item == IslandsInventoryItem.ARCHER_BOW_SELECT.getItem())
            equipKit(player, new Archer(player), kpm);
        if (item == IslandsInventoryItem.MEDIC_POTION_SELECT.getItem())
            equipKit(player, new Medic(player), kpm);
        if (item == IslandsInventoryItem.MINER_PICKAXE_SELECT.getItem())
            equipKit(player, new Miner(player), kpm);
        if (item == IslandsInventoryItem.BOMBER_TNT_SELECT.getItem())
            equipKit(player, new Bomber(player), kpm);

    }

    @EventHandler
    public void playerCloseInventory(InventoryCloseEvent e) {
        Inventory inventory = e.getInventory();
        Player player = (Player) e.getPlayer();
        KitPlayerManager kpm = KitPlayerManager.getInstance();
        KitPlayer kitPlayer = kpm.getKitPlayer(player);

        if (GameManager.getGame() == null) return;
        if (GameManager.getGame().getGameState() != GameState.LOBBY) return;
        if (inventory == null) return;
        if (inventory == IslandsInventoryManager.getKitSelectInventory() && kitPlayer == null)
            openKitGui(player);
    }

    private void equipKit(Player player, KitPlayer kitPlayer, KitPlayerManager kpm) {
        kpm.addKitPlayer(kitPlayer);
        SoundPlayer.sendSound(player, "note.pling", .9F, 50);
    }

    private void openKitGui(Player player) {
        player.openInventory(IslandsInventoryManager.getKitSelectInventory());
    }

    private Inventory createKitSelectInventory() {
        Inventory inventory = Bukkit.createInventory(null, 6, "Select a kit");
        ItemStack berserkerAxe = new ItemStack(Material.STONE_AXE);
        ItemStack brawlerSword = new ItemStack(Material.IRON_SWORD);
        ItemStack archerBow = new ItemStack(Material.BOW);
        ItemStack medicPotion = new ItemStack(Material.POTION);
        ItemStack bomberTnt = new ItemStack(Material.TNT);
        ItemStack minerPickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
        return inventory;
    }

}
