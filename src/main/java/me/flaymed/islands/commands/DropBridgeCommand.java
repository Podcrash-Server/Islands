package me.flaymed.islands.commands;

import com.podcrash.api.game.GameManager;
import me.flaymed.islands.Islands;
import me.flaymed.islands.game.GameStage;
import me.flaymed.islands.game.IslandsGame;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DropBridgeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("pdc.islands.dropbridge"))
            return true;
        IslandsGame game = Islands.getInstance().getGame();
        if (game.getStage() != GameStage.PREPARE) {
            sender.sendMessage("The bridges can only be dropped if the game is in the PREPARE phase!");
            return true;
        }

        game.setStage(GameStage.FALLEN);
        sender.sendMessage("Forcing the bridges to drop...");
        return true;
    }
}
