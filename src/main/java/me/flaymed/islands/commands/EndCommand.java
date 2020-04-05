package me.flaymed.islands.commands;

import com.podcrash.api.mc.game.Game;
import com.podcrash.api.mc.game.GameManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EndCommand extends CommandBase {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender.hasPermission("Islands.host")){
            if (!(sender instanceof Player)) return false;
            Game game = GameManager.getGame();
            GameManager.endGame(game);
            sender.sendMessage("attempting to end game " + game.getId());
            return true;
        } else {
            sender.sendMessage(String.format("%sIslands> %sYou have insufficient permissions to use that command.", ChatColor.BLUE, ChatColor.GRAY));
        }
        return true;
    }
}
