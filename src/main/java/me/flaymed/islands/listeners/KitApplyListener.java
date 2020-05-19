package me.flaymed.islands.listeners;

import com.podcrash.api.events.skill.ApplyKitEvent;
import com.podcrash.api.kits.KitPlayer;
import com.podcrash.api.listeners.ListenerBase;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class KitApplyListener extends ListenerBase {
    public KitApplyListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void kit(ApplyKitEvent e) {
        KitPlayer kitPlayer = e.getKitPlayer();
        Player p = kitPlayer.getPlayer();
        
        p.sendMessage("You are a " + kitPlayer.getName());
    }
}
