package dev.tarvos.xyz.commands;

import dev.tarvos.xyz.Core;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

/**
 * Created by Nyvil, 05/03/2022 at 17:01
 *
 * @author Nyvil
 * @copyright Nyvil 2022 under Apache License 2.0, unless stated otherwise
 */

public class AddTreeLocation implements CommandExecutor {

    private TreeType formatTreeType(String input) {
        for (TreeType type : TreeType.values()) {
            if (type.toString().toLowerCase().equalsIgnoreCase(input))
                return type;
        }

        return null;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!(commandSender instanceof Player)) {
            Bukkit.getLogger().log(Level.INFO, "Only Players can execute the addtreelocation command!");
            return true;
        }
        Player p = (Player) commandSender;
        if(!p.hasPermission("tarvos.addtreelocation")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo Permission."));
            return true;
        }

        if (args.length != 2) {
            p.sendMessage(ChatColor.GRAY + "Usage: /addtreelocation <tree type> <tree name>");
            return true;
        }

        if (formatTreeType(args[0]) == null) {
            p.sendMessage(ChatColor.RED + args[0] + " isn't a valid tree type.");
            return true;
        }

        if (Core.getCore().getTreeConfig().exists(args[1])) {
            p.sendMessage(ChatColor.RED + args[1] + " already has a location saved to it.");
            return true;
        }

        Location save = p.getLocation().getBlock().getLocation();

        Core.getCore().getTreeConfig().saveLocation(args[1], save);
        p.sendMessage(ChatColor.GREEN + "Saved tree location at XYZ: " + save.getX() + ", " + save.getY() + ", " + save.getZ());
        p.getWorld().generateTree(save, formatTreeType(args[0]));
        return true;
    }
}
