package me.flaymed.islands.commands;

import me.flaymed.islands.util.ore.OreVeinSetting;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OreSettingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player))
            return true;
        if (!sender.hasPermission("pdc.islands.ore"))
            return true;
        Player p = (Player) sender;
        if (args.length == 0) {
            for (OreVeinSetting veinSetting : OreVeinSetting.details()) {
                p.sendMessage(veinSetting.toString());
            }

            p.sendMessage("To change: /ore ORE MIN MAX CHANCE");
            p.sendMessage("To change: /ore COAL 4 8 0.6");

        }else if (args.length == 4) {
            OreVeinSetting setting = OreVeinSetting.findByName(args[0]);
            if (setting == null)
                return true;

            setting.setMin(Integer.parseInt(args[1]));
            setting.setMax(Integer.parseInt(args[2]));
            setting.setContinueChance(Double.parseDouble(args[3]));
        }else sender.sendMessage("Something went wrong!");
        return true;
    }
}
