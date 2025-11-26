package lionking.world;

import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.world.gen.feature.WorldGenerator;
import java.util.Random;
import cpw.mods.fml.common.FMLLog;
import lionking.mod_LionKing;

public class LKWorldGenPassionTrees extends WorldGenerator {
    private static final int MIN_HEIGHT = 6;
    private static final int HEIGHT_VARIATION = 4;
    private static final int MAX_WORLD_HEIGHT = 256;

    public LKWorldGenPassionTrees(boolean notify) {
        super(notify);
        FMLLog.fine("Initialized LKWorldGenPassionTrees with notify=%b", notify);
    }

    public boolean generate(World world, Random random, int x, int y, int z) {
        FMLLog.fine("Attempting to generate passion tree at (%d, %d, %d)", x, y, z);

        if (mod_LionKing.prideWood == null || mod_LionKing.passionLeaves == null) {
            FMLLog.severe("Cannot generate passion tree at (%d, %d, %d): prideWood or passionLeaves is null", x, y, z);
            return false;
        }

        int height = MIN_HEIGHT + random.nextInt(HEIGHT_VARIATION);
        if (y < 1 || y + height + 1 >= MAX_WORLD_HEIGHT) {
            FMLLog.fine("Cannot generate passion tree at (%d, %d, %d): invalid height (height=%d)", x, y, z, height);
            return false;
        }

        if (!checkSpace(world, x, y, z, height)) {
            FMLLog.fine("Cannot generate passion tree at (%d, %d, %d): insufficient space", x, y, z);
            return false;
        }

        Block groundBlock = world.getBlock(x, y - 1, z);
        if (groundBlock != Blocks.grass && groundBlock != Blocks.dirt || y >= MAX_WORLD_HEIGHT - height - 1) {
            FMLLog.fine("Cannot generate passion tree at (%d, %d, %d): invalid ground block %s", x, y, z, groundBlock != null ? groundBlock.getUnlocalizedName() : "null");
            return false;
        }

        world.setBlock(x, y - 1, z, Blocks.dirt, 0, 2);
        FMLLog.fine("Set dirt base at (%d, %d, %d)", x, y - 1, z);

        generateLeaves(world, random, x, y, z, height);
        generateTrunk(world, x, y, z, height);

        FMLLog.info("Successfully generated passion tree at (%d, %d, %d) with height=%d", x, y, z, height);
        return true;
    }

    private boolean checkSpace(World world, int x, int y, int z, int height) {
        for (int dy = y; dy <= y + 1 + height; dy++) {
            int radius = (dy == y) ? 0 : (dy >= y + height - 2) ? 2 : 1;
            for (int dx = x - radius; dx <= x + radius; dx++) {
                for (int dz = z - radius; dz <= z + radius; dz++) {
                    if (dy >= 0 && dy < MAX_WORLD_HEIGHT) {
                        Block block = world.getBlock(dx, dy, dz);
                        if (!isBlockReplaceable(world, block, dx, dy, dz)) {
                            FMLLog.fine("Non-replaceable block at (%d, %d, %d): %s", dx, dy, dz, block != null ? block.getUnlocalizedName() : "null");
                            return false;
                        }
                    } else {
                        FMLLog.fine("Out of world bounds at (%d, %d, %d)", dx, dy, dz);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void generateLeaves(World world, Random random, int x, int y, int z, int height) {
        for (int dy = y - 3 + height; dy <= y + height; dy++) {
            int offsetY = dy - (y + height);
            int radius = 1 - offsetY / 2;
            for (int dx = x - radius; dx <= x + radius; dx++) {
                int distX = dx - x;
                for (int dz = z - radius; dz <= z + radius; dz++) {
                    int distZ = dz - z;
                    if ((Math.abs(distX) != radius || Math.abs(distZ) != radius || 
                         random.nextInt(2) != 0 && offsetY != 0) && 
                        !world.getBlock(dx, dy, dz).isNormalCube()) {
                        world.setBlock(dx, dy, dz, mod_LionKing.passionLeaves, 0, 2);
                        FMLLog.fine("Placed passionLeaves at (%d, %d, %d)", dx, dy, dz);
                    }
                }
            }
        }
    }

    private void generateTrunk(World world, int x, int y, int z, int height) {
        for (int dy = 0; dy < height; dy++) {
            Block block = world.getBlock(x, y + dy, z);
            if (isBlockReplaceable(world, block, x, y + dy, z)) {
                world.setBlock(x, y + dy, z, mod_LionKing.prideWood, 3, 2);
                FMLLog.fine("Placed prideWood at (%d, %d, %d)", x, y + dy, z);
            }
        }
    }

    private boolean isBlockReplaceable(World world, Block block, int x, int y, int z) {
        return block == null || block.getMaterial() == Material.air || block.isLeaves(world, x, y, z) || block == mod_LionKing.passionLeaves;
    }
}