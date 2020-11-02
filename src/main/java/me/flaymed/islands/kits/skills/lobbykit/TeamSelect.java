package me.flaymed.islands.kits.skills.lobbykit;

import com.podcrash.gamecore.kits.Ability;
import com.podcrash.gamecore.kits.abilitytype.Interact;
import me.flaymed.islands.Islands;
import me.flaymed.islands.teams.IslandsTeam;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class TeamSelect extends Ability implements Interact {
    @Override
    public String getName() {
        return "Team Select";
    }

    @Override
    public ItemStack getItem() {
        ItemStack feather = new ItemStack(Material.FEATHER, 1);
        ItemMeta featherMeta = feather.getItemMeta();
        featherMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Team Selector");
        feather.setItemMeta(featherMeta);
        return feather;
    }

    @Override
    public void doAbility() {
        getKitPlayer().getPlayer().openInventory(Islands.getInstance().getTeamSelectInventory());
    }

    @EventHandler
    public void playerSelectTeam(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        if (getKitPlayer().getPlayer().getUniqueId() != player.getUniqueId()) return;
        if (e.getInventory() != Islands.getInstance().getTeamSelectInventory()) return;
        if (item == null || item.getType() == Material.AIR) return;
        for (IslandsTeam team : Islands.getInstance().getTeams()) {
            if (team.getPlayers().contains(player)) {
                team.removePlayer(player);
            }
            if (item != team.getItem()) continue;
            if (team.isFull()) {
                player.sendMessage(String.format("%sIslands>%s The team you tried to join is full!", ChatColor.BLUE, ChatColor.GRAY));
                break;
            }
            team.addPlayer(player);
            getKitPlayer().setTeam(team);
            player.sendMessage(String.format("%sIslands>%s You have joined %s%s Team%s!", ChatColor.BLUE, ChatColor.GRAY, team.getColor(), team.getColor().toString(), ChatColor.GRAY));
            player.closeInventory();
            break;
        }

    }

    @Override
    public List<Action> getActions() {
        return Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK, Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK);
    }
}
