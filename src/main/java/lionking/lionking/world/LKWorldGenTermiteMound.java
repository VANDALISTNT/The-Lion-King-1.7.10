package lionking.world;

import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenerator;
import java.util.Random;
import cpw.mods.fml.common.FMLLog;
import lionking.mod_LionKing;

public class LKWorldGenTermiteMound extends WorldGenerator {
    @Override
    public boolean generate(World world, Random random, int i, int j, int k) {
        FMLLog.fine("Attempting to generate termite mound at (%d, %d, %d)", i, j, k);

        if (j < 1 || j + 30 > 256) {
            FMLLog.fine("Cannot generate termite mound at (%d, %d, %d): invalid height", i, j, k);
            return false;
        }

        boolean isLarge = random.nextInt(3) == 0;
        FMLLog.fine("Selected %s termite mound at (%d, %d, %d)", isLarge ? "large" : "small", i, j, k);
        boolean result = isLarge ? generateLargeTermiteMound(world, random, i, j, k)
                                : generateTermiteMound(world, random, i, j, k);

        if (result) {
            FMLLog.info("Successfully generated %s termite mound at (%d, %d, %d)", isLarge ? "large" : "small", i, j, k);
        } else {
            FMLLog.warning("Failed to generate %s termite mound at (%d, %d, %d)", isLarge ? "large" : "small", i, j, k);
        }
        return result;
    }

    private boolean generateTermiteMound(World world, Random random, int i, int j, int k) {
        if (!checkBase(world, i, j, k, 4)) {
            FMLLog.fine("Cannot generate small termite mound at (%d, %d, %d): invalid base", i, j, k);
            return false;
        }

        int[][] heights = new int[4][4];
        for (int i1 = 0; i1 < 4; i1++) {
            for (int k1 = 0; k1 < 4; k1++) {
                heights[i1][k1] = 3 + random.nextInt(2);
                if (i1 > 0 && i1 < 3 && k1 > 0 && k1 < 3) {
                    heights[i1][k1] = 5 + random.nextInt(3);
                }
            }
        }

        placeMound(world, i, j, k, heights, 4);
        return true;
    }

    private boolean generateLargeTermiteMound(World world, Random random, int i, int j, int k) {
        if (!checkBase(world, i, j, k, 8)) {
            FMLLog.fine("Cannot generate large termite mound at (%d, %d, %d): invalid base", i, j, k);
            return false;
        }

        int[][] heights = new int[8][8];
        for (int i1 = 0; i1 < 8; i1++) {
            for (int k1 = 0; k1 < 8; k1++) {
                heights[i1][k1] = 2 + random.nextInt(4);
                if (i1 > 0 && i1 < 7 && k1 > 0 && k1 < 7) {
                    heights[i1][k1] = 7 + random.nextInt(5);
                }
                if (i1 > 1 && i1 < 6 && k1 > 1 && k1 < 6) {
                    heights[i1][k1] = 13 + random.nextInt(7);
                }
                if (i1 > 2 && i1 < 5 && k1 > 2 && k1 < 5) {
                    heights[i1][k1] = 18 + random.nextInt(13);
                }
            }
        }

        placeMound(world, i, j, k, heights, 8);
        return true;
    }

    private boolean checkBase(World world, int i, int j, int k, int size) {
        for (int i1 = 0; i1 < size; i1++) {
            for (int k1 = 0; k1 < size; k1++) {
                Block block = world.getBlock(i + i1, j - 1, k + k1);
                if (block == null || !block.isOpaqueCube() || !world.isAirBlock(i + i1, j, k + k1)) {
                    FMLLog.fine("Base check failed at (%d, %d, %d): block=%s, air above=%b", 
                        i + i1, j - 1, k + k1, block != null ? block.getUnlocalizedName() : "null", 
                        world.isAirBlock(i + i1, j, k + k1));
                    return false;
                }
            }
        }
        return true;
    }

    private void placeMound(World world, int i, int j, int k, int[][] heights, int size) {
        for (int i1 = 0; i1 < size; i1++) {
            for (int k1 = 0; k1 < size; k1++) {
                int height = heights[i1][k1];
                for (int j1 = 0; j1 < height && j + j1 < 256; j1++) {
                    if (world.isAirBlock(i + i1, j + j1, k + k1)) {
                        world.setBlock(i + i1, j + j1, k + k1, mod_LionKing.termite, 0, 2);
                        FMLLog.fine("Placed termite block at (%d, %d, %d)", i + i1, j + j1, k + k1);
                    }
                }
            }
        }
    }

    private boolean isBlockReplaceable(World world, int i, int j, int k) {
        Block block = world.getBlock(i, j, k);
        return block == Blocks.air || block.isLeaves(world, i, j, k) || block == Blocks.tallgrass || block == Blocks.deadbush;
    }
}