package dev.tarvos.xyz.commands;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import dev.tarvos.xyz.blockdata.TreeCommandData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

        if(args.length == 1) {
            TreeCommandData data = TreeCommandData.forName(args[0]);
            if(data != null) {
                DBObject obj = new BasicDBObject("world", p.getWorld().getName()).append("pos_x", p.getLocation().getX()).append("pos_y", p.getLocation().getY() - 1).append("pos_z", p.getLocation().getZ());
            }

        } else {
            p.sendMessage("");
        }

        return false;
    }
}
