package me.flaymed.islands.command;

import me.flaymed.islands.util.ore.OreVeinSetting;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class OreSettingCommand extends BukkitCommand {
    public OreSettingCommand() {
        super("ore");
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args) {
        if (!(sender instanceof Player))
            return true;
        Player p = (Player) sender;
        if (args.length == 0) {
            for (OreVeinSetting veinSetting : OreVeinSetting.details()) {
                p.sendMessage(veinSetting.toString());
            }

            p.sendMessage("To change: /ore ORE MIN MAX");
            p.sendMessage("To change: /ore COAL 4 8");

        }else if (args.length == 3) {
            OreVeinSetting setting = OreVeinSetting.findByName(args[0]);
            if (setting == null)
                return true;

            setting.setMin(Integer.parseInt(args[1]));
            setting.setMax(Integer.parseInt(args[2]));
        }
        return true;
    }
}
