package me.flaymed.islands.kits.skills.archer;

import com.podcrash.gamecore.GameCore;
import com.podcrash.gamecore.kits.KitPlayer;
import com.podcrash.gamecore.kits.Ability;
import com.podcrash.gamecore.kits.abilitytype.Passive;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;

public class SkillSelect extends Ability implements Passive {

    private final Inventory inv;
    private final ItemStack ropedArrow;
    private final ItemStack quickShot;
    private final ItemStack healingShot;
    private boolean skillEquipped;

    public SkillSelect() {
        this.inv = Bukkit.createInventory(null, 27, "Select a Skill to use");
        this.ropedArrow = new ItemStack(Material.LEASH, 1);
        this.quickShot = new ItemStack(Material.ARROW, 1);
        this.healingShot = new ItemStack(Material.SPECKLED_MELON, 1);
        this.skillEquipped = false;

        setupItems();
    }

    @Override
    public String getName() {
        return "Skill Select";
    }

    //Item not needed
    @Override
    public ItemStack getItem() {
        return null;
    }

    //TODO: GameCore GameStates or Islands GameStates and do the game start listener

    @EventHandler
    public void playerClickInventory(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        String skillName = null;
        if (e.getInventory() == inv) return;
        KitPlayer archer = getKitPlayer();

        if (e.getCurrentItem().equals(ropedArrow)) {
            skillEquipped = true;
            archer.addAbilityToActiveKit(RopedArrow.class);
            skillName = "Roped Arrow";
            player.closeInventory();
        }

        if (e.getCurrentItem().equals(quickShot)) {
            skillEquipped = true;
            archer.addAbilityToActiveKit(QuickShot.class);
            skillName = "Quick Shot";
            player.closeInventory();
        }

        if (e.getCurrentItem().equals(healingShot)) {
            skillEquipped = true;
            archer.addAbilityToActiveKit(HealingShot.class);
            skillName = "Healing Shot";
            player.closeInventory();
        }

        String skillMessage = String.format("% You equipped ability: %s%s%s!", GameCore.getKitPrefix(), ChatColor.GRAY, ChatColor.GREEN, skillName, ChatColor.GRAY);
        if (skillEquipped) {
            player.sendMessage(skillMessage);
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 3, 2);
        }


    }

    @EventHandler
    public void inventoryClose(InventoryCloseEvent e) {
        if (e.getPlayer() == getKitPlayer().getPlayer())
            if (!skillEquipped && e.getInventory() == inv)
                getKitPlayer().getPlayer().openInventory(inv);
    }

    private void setupItems() {
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
