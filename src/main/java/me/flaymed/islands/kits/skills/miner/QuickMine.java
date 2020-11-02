package me.flaymed.islands.kits.skills.miner;

import com.podcrash.gamecore.GameCore;
import com.podcrash.gamecore.kits.Ability;
import com.podcrash.gamecore.kits.abilitytype.AfterConstruct;
import com.podcrash.gamecore.kits.abilitytype.Cooldown;
import com.podcrash.gamecore.kits.abilitytype.Interact;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.Arrays;
import java.util.List;

public class QuickMine extends Ability implements Cooldown, Interact, AfterConstruct {

    @Override
    public String getName() {
        return "QuickMine";
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.IRON_INGOT, 1);
    }

    @Override
    public void doAbility() {
        Player player = getPlayer();
        if (onCooldown()) {
            getPlayer().sendMessage(getCooldownMessage());
            return;
        }
        if (!player.getItemInHand().getType().equals(Material.IRON_INGOT))
            return;
        getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 10 * 20, 3));
        player.getInventory().removeItem(getItem());
        player.sendMessage(String.format("%s You used %sQuick mine%s!", GameCore.getKitPrefix(), ChatColor.BLUE, ChatColor.GRAY));

    }

    @Override
    public List<Action> getActions() {
        return Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK);
    }

    @Override
    public float getCooldown() {
        return 60;
    }

    @Override
    public void afterConstruct() {
        getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1));
    }

    @Override
    public Player getPlayer() {
        return getKitPlayer().getPlayer();
    }

}
