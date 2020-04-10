package me.flaymed.islands.commands;

import me.flaymed.islands.game.map.bridge.BridgeManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BridgesCommand extends CommandBase {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player && sender.hasPermission("islands.developer")) {
            BridgeManager.dropBridges(((Player) sender).getWorld());
        } else {
            sender.sendMessage(String.format("%sIslands> %sYou have insufficient permissions to use that command.", ChatColor.BLUE, ChatColor.GRAY));
        }

        return true;
    }
}
