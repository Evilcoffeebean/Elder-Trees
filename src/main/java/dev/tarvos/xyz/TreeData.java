package dev.tarvos.xyz;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
public class TreeData {

    private final Block targetBlock;
    private final TreeType treeType;

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
        this(block, TreeType.TREE);
    }

    public TreeData(Block block, TreeType type) {
        this.targetBlock = block;
        this.treeType = type != null ? type : TreeType.TREE;
    }

    public boolean isValid() {
        for (TreeType type : TYPES) {
            if (isLog(targetBlock) && treeType == type)
                return true;
        }
        return false;
    }

    public boolean hasAxe(Player player) {
        for (Material axe : AXES) {
            if (player.getInventory().getItemInMainHand().getType() == axe)
                return true;
        }
        return false;
    }

    private boolean isLog(Block given) {
        for (Material log : LOGS) {
            if (given.getType() == log)
                return true;
        }
        return false;
    }

    public void executeTimber() {
        targetBlock.breakNaturally();

        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                counter++;
                Block up = targetBlock.getLocation().add(0, counter, 0).getBlock();

                if (isLog(up)) {
                    up.breakNaturally();
                    new TimberEffect(Particle.VILLAGER_HAPPY).display(up.getLocation());
                    up.getWorld().playSound(up.getLocation(), Sound.BLOCK_WOOD_BREAK, 1f, 1f);
                } else {
                    cancel();
                    counter = 0;
                }
            }
        }.runTaskTimer(Core.getCore(), 0L, 2L);
    }

    public void regenTree() {
        targetBlock.getWorld().generateTree(targetBlock.getLocation(), treeType);
    }
}
