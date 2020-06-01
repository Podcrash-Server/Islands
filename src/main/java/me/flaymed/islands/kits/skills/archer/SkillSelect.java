package me.flaymed.islands.kits.skills.archer;

import com.podcrash.api.events.game.GameStartEvent;
import com.podcrash.api.kits.KitPlayerManager;
import com.podcrash.api.kits.iskilltypes.action.IConstruct;
import com.podcrash.api.kits.skilltypes.Passive;
import me.flaymed.islands.kits.classes.Archer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SkillSelect extends Passive implements IConstruct {

    private final  Inventory inv = Bukkit.createInventory(null, 27, "Select a Skill to use");
    private final  ItemStack ropedArrow = new ItemStack(Material.LEASH, 1);
    private final  ItemStack quickShot = new ItemStack(Material.LEASH, 1);
    private final ItemStack healingShot = new ItemStack(Material.LEASH, 1);
    private boolean skillEquipped = false;

    public SkillSelect() {

    }

    @Override
    public String getName() {
        return "Skill Select";
    }

    @EventHandler
    public void gameStart(GameStartEvent e) {
        getPlayer().openInventory(inv);
        //no need for below, you have a player instance (lol)
        /*
        List<Player> players = e.getPlayers();

        for (Player player: players) {
            if (player == getPlayer()) {
                player.openInventory(inv);
            }
        }
         */
    }

    @EventHandler
    public void playerClickInventory(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        String skillName = null;
        if (e.getInventory() == inv) {
            if (e.getCurrentItem().equals(ropedArrow)) {
                skillEquipped = true;
                Archer archer = new Archer(player, Barrage.class, RopedArrow.class);
                KitPlayerManager.getInstance().addKitPlayer(archer);
                skillName = "Roped Arrow";
                player.closeInventory();
            }

            if (e.getCurrentItem().equals(quickShot)) {
                skillEquipped = true;
                Archer archer = new Archer(player, Barrage.class, QuickShot.class);
                KitPlayerManager.getInstance().addKitPlayer(archer);
                skillName = "Quick Shot";
                player.closeInventory();
            }

            if (e.getCurrentItem().equals(healingShot)) {
                skillEquipped = true;
                Archer archer = new Archer(player, Barrage.class, HealingShot.class);
                KitPlayerManager.getInstance().addKitPlayer(archer);
                skillName = "Healing Shot";
                player.closeInventory();
            }

            String skillMessage = String.format("%sSkill> %sYou equipped skill %s%s%s!", ChatColor.BLUE, ChatColor.GRAY, ChatColor.GREEN, skillName, ChatColor.GRAY);
            if (skillEquipped)
                player.sendMessage(skillMessage);
        }

    }

    @EventHandler
    public void inventoryClose(InventoryCloseEvent e) {
        if (e.getPlayer() == getPlayer())
            if (!skillEquipped && e.getInventory() == inv)
                getPlayer().openInventory(inv);
    }


    @Override
    public void afterConstruction() {

        ItemMeta rAMeta = ropedArrow.getItemMeta();
        rAMeta.setDisplayName(ChatColor.RED + "Roped Arrow");
        List<String> lore = new ArrayList<>();
        lore.add("Punch with bow in hand to activate roped arrow.");
        lore.add("Immediately shooting an arrow, the roped arrow pulls you forward a small amount");
        lore.add("\nCool down: 25 Seconds");
        rAMeta.setLore(lore);
        ropedArrow.setItemMeta(rAMeta);
        lore.clear();

        ItemMeta qSMeta = quickShot.getItemMeta();
        qSMeta.setDisplayName(ChatColor.RED + "Quick Shot");
        lore.add("Punch with your bow to activate quick shot.");
        lore.add("Shooting on arrow instantly forward, on direct hit, it will do the same damage as a fully charged arrow.");
        lore.add("\nCool down: 25 Seconds");
        qSMeta.setLore(lore);
        quickShot.setItemMeta(qSMeta);
        lore.clear();

        ItemMeta hSMeta = healingShot.getItemMeta();
        hSMeta.setDisplayName(ChatColor.RED + "Healing Shot");
        lore.add("Punch with your bow to activate healing shot.");
        lore.add("Once an arrow is shot, a healing pulse will be spread out from where the arrow lands,");
        lore.add("any teammates within a 6 block radius of that arrow will receive 2 HP (1 Heart).");
        lore.add("\nCool down: 25 Seconds");
        hSMeta.setLore(lore);
        healingShot.setItemMeta(hSMeta);
        lore.clear();

        inv.setItem(11, ropedArrow);
        inv.setItem(13, quickShot);
        inv.setItem(15, healingShot);
    }
}
