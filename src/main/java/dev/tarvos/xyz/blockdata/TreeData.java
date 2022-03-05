package dev.tarvos.xyz.blockdata;

import dev.tarvos.xyz.Core;
import dev.tarvos.xyz.particles.TimberEffect;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
public class TreeData {

    private final Block targetBlock;
    private final TreeType treeType;
    private final Random r = new Random();
    private static int threshold = 0;

    private final Material[] AXES = {
            Material.WOODEN_AXE,
            Material.STONE_AXE,
            Material.IRON_AXE,
            Material.GOLDEN_AXE,
            Material.DIAMOND_AXE,
            Material.NETHERITE_AXE
    };

    private final Material[] LOGS = {
            Material.ACACIA_LOG,
            Material.BIRCH_LOG,
            Material.DARK_OAK_LOG,
            Material.JUNGLE_LOG,
            Material.OAK_LOG,
            Material.SPRUCE_LOG,
            Material.STRIPPED_ACACIA_LOG,
            Material.STRIPPED_BIRCH_LOG,
            Material.STRIPPED_DARK_OAK_LOG,
            Material.STRIPPED_JUNGLE_LOG,
            Material.STRIPPED_OAK_LOG,
            Material.STRIPPED_SPRUCE_LOG
    };

    private final TreeType[] TYPES = {
            TreeType.ACACIA,
            TreeType.BIRCH,
            TreeType.DARK_OAK,
            TreeType.JUNGLE,
            TreeType.TREE
    };

    public TreeData(Block block) {
        this(block, null);
    }

    public TreeData(Block block, TreeType type) {
        this.targetBlock = block;
        this.treeType = type != null ? type : TreeType.TREE;
    }


    /**
     * Method to validate whether the tree should be fully destroyed
     * @return True if the tree should be destroyed
     */
    public boolean isValid() {
        for (TreeType type : TYPES) {
            if (isLog(targetBlock) && treeType == type)
                return true;
        }
        return false;
    }

    /**
     * Check to see if the player is holding an axe
     * @param player - the player who is chopping the tree
     */
    public boolean hasAxe(Player player) {
        for (Material axe : AXES) {
            if (player.getInventory().getItemInMainHand().getType() == axe)
                return true;
        }
        return false;
    }

    /**
     * Returns whether the checked block is a log
     * @param given Block being checked
     * @return True if the given block is a tree log
     */
    private boolean isLog(Block given) {
        for (Material log : LOGS) {
            if (given.getType() == log)
                return true;
        }
        return false;
    }

    /**
     * Processes Leaves in a recursive way
     *
     * @param startBlock Starting block of the algorithm
     *
     * Algorithm: check the starting block and move from there to the next block(s), which are
     * then used as new center blocks, until the depthSearch list is empty or the max. threshold is reached.
     *
     * This algorithm has shown to achieve a performance between 1-5 milliseconds on regular oak trees.
     *
     */
    private void getBlockData(Block startBlock) {
        threshold++;
        List<Block> depthSearch = new ArrayList<>();
        for (int i = 0; i < BlockFace.values().length; i++) {
            Block found = startBlock.getRelative(BlockFace.values()[i]);
            if (found.getType() != Material.OAK_LEAVES) {
                continue;
            }
            depthSearch.add(found);
            found.setType(Material.AIR);

            for (Block depthBlock : depthSearch) {
                if (threshold <= 500) {
                    getBlockData(depthBlock);
                    depthBlock.setType(Material.AIR);
                    depthBlock.getWorld().playSound(depthBlock.getLocation(), Sound.BLOCK_GRASS_BREAK, 1f, 1f);
                }
            }

            depthSearch.clear();
            threshold = 0;
        }
    }

    public void executeTimber() {
        targetBlock.setType(Material.AIR);
        targetBlock.getDrops().clear();

        new BukkitRunnable() {
            double counter = 0;
            @Override
            public void run() {

                counter += 0.75;

                for (BlockFace face : BlockFace.values()) {
                    Block current = targetBlock.getLocation().add(0, counter, 0).getBlock().getRelative(face);
                    getBlockData(current);

                    if (isLog(current)) {
                        current.getDrops().clear();
                        current.setType(Material.AIR);
                        current.getWorld().dropItemNaturally(current.getLocation(), shouldDrop(10) ? elderWood(false) : elderWood(true));
                        new TimberEffect(Particle.VILLAGER_HAPPY).display(current.getLocation());
                        current.getWorld().playSound(current.getLocation(), Sound.BLOCK_WOOD_BREAK, 1f, 1f);
                    }

                    if (counter >= 25) {
                        cancel();
                        counter = 0;
                    }
                }
            }
        }.runTaskTimer(Core.getCore(), 0L, 2L);
    }

    /**
     * Method to build the (charged/dead) Elder Wood Item which is then dropped to the player.
     *
     * @param dead True if it should build a charged elder wood, false if it should build a dead elder wood
     * @return Elder Wood Itemstack
     */
    private ItemStack elderWood(boolean dead) {
        ItemStack item = new ItemStack(Material.OAK_WOOD, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(dead ? ChatColor.DARK_GRAY + "Dead Elder Wood" : ChatColor.GREEN + "Charged Elder Wood");
        item.setItemMeta(meta);
        if (!dead)
            addGlow(item);
        return item;
    }


    /**
     * Method to check the chance of getting a certain item, to be called in the method above
     *
     * @param dropChance the percentage of the chance
     * @return True if the chance is less or equal to the provided dropChance parameter
     */
    private boolean shouldDrop(int dropChance) {
        return r.nextInt(100) <= dropChance;
    }

    /**
     * Method to apply a glowing effect to an item along with a durability enchantment
     *
     * @param stack the item that's getting the glow effect applied to
     */
    private void addGlow(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        stack.setItemMeta(meta);
    }

}
