package dev.tarvos.xyz.listeners;

import dev.tarvos.xyz.Core;
import dev.tarvos.xyz.blockdata.TreeData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

/**
 * Created by Nyvil, 04/03/2022 at 18:35
 *
 * @author Nyvil
 * @copyright Nyvil 2022 under Apache License 2.0, unless stated otherwise
 */

public class TreeBreakHandler implements Listener {

    private final Random r = new Random();

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e) {
        final TreeData treeData = new TreeData(e.getBlock());

        if (!treeData.checkConfig(e.getBlock().getLocation())) return;
        if (!treeData.isValid()) return;
        if (!treeData.hasAxe(e.getPlayer())) return;

        treeData.executeTimber();
        if (!Core.getCore().getCache().containsKey(treeData)) {
            Core.getCore().getCache().put(treeData, System.currentTimeMillis() + r.nextInt(1000 * 10));
        }

        if(shouldDrop(10)) {
            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), elderWood(false));
        } else {
            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), elderWood(true));
        }

    }

    private ItemStack elderWood(boolean dead) {
        ItemStack item = new ItemStack(Material.OAK_WOOD, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(dead ? ChatColor.DARK_GRAY + "Dead Elder Wood" : ChatColor.GREEN + "Charged Elder Wood");
        item.setItemMeta(meta);
        if (!dead)
            addGlow(item);
        return item;
    }

    private boolean shouldDrop(int dropChance) {
        return r.nextInt(100) <= dropChance;
    }

    private void addGlow(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        stack.setItemMeta(meta);
    }
}