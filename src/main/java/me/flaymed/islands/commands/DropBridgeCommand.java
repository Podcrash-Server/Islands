package me.flaymed.islands.commands;

import com.podcrash.api.game.GameManager;
import me.flaymed.islands.game.GameStage;
import me.flaymed.islands.game.IslandsGame;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.Collections;

public class DropBridgeCommand extends BukkitCommand {
    public DropBridgeCommand() {
        super("dropbridge",
                "Drops the bridges",
                "/dropbridge",
                Collections.emptyList());
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args) {
        if (!sender.hasPermission("invicta.command.dropbridge"))
            return true;
        IslandsGame game = (IslandsGame) GameManager.getGame();
        if (game.getStage() != GameStage.PREPARE) {
            sender.sendMessage("The bridges can only be dropped if the game is in the PREPARE phase!");
            return true;
        }

        game.setStage(GameStage.FALLEN);
        sender.sendMessage("Forcing the bridges to drop...");
        return true;
    }
}
