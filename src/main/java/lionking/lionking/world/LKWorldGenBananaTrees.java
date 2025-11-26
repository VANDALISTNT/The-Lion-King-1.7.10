package lionking.world;

import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;

import lionking.mod_LionKing;

import java.util.Random;

public class LKWorldGenBananaTrees extends WorldGenAbstractTree {
    private static final int MIN_HEIGHT = 4;
    private static final int MAX_HEIGHT_VARIATION = 3;
    private static final int LEAVES_BASE = 1;
    private static final int LEAVES_VARIATION = 3;
    private static final int MAX_WORLD_HEIGHT = 256;
    private static final ForgeDirection[] DIRECTIONS = {
        ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST
    };

    public LKWorldGenBananaTrees(boolean notify) {
        super(notify);
    }

    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {
        int height = MIN_HEIGHT + random.nextInt(MAX_HEIGHT_VARIATION);
        int[] leaves = new int[4];
        for (int i = 0; i < 4; i++) {
            leaves[i] = LEAVES_BASE + random.nextInt(LEAVES_VARIATION);
        }

        if (y < 1 || y + height + 5 >= MAX_WORLD_HEIGHT) {
            return false;
        }

        Block groundBlock = world.getBlock(x, y - 1, z);
        if (groundBlock != Blocks.grass && groundBlock != Blocks.dirt) {
            return false;
        }

        Block startBlock = world.getBlock(x, y, z);
        if (!isBlockReplaceable(startBlock) && startBlock != mod_LionKing.bananaSapling) {
            return false;
        }

        for (int dy = 0; dy < height + 2; dy++) {
            if (!isBlockReplaceable(world.getBlock(x, y + dy, z))) {
                return false;
            }
        }

        for (int dirIndex = 0; dirIndex < 4; dirIndex++) {
            ForgeDirection dir = DIRECTIONS[dirIndex];
            int dx = dir.offsetX;
            int dz = dir.offsetZ;

            for (int ly = -1; ly < leaves[dirIndex]; ly++) {
                if (!isBlockReplaceable(world.getBlock(x + dx, y + height + ly, z + dz))) {
                    return false;
                }
            }
            
            for (int ly = -1; ly < 1; ly++) {
                if (!isBlockReplaceable(world.getBlock(x + dx * 2, y + height + leaves[dirIndex] + ly, z + dz * 2))) {
                    return false;
                }
            }
        }

        for (int dy = 0; dy < height + 2; dy++) {
            world.setBlock(x, y + dy, z, mod_LionKing.prideWood2, 0, 2);
        }

        for (int dirIndex = 0; dirIndex < 4; dirIndex++) {
            ForgeDirection dir = DIRECTIONS[dirIndex];
            int dx = dir.offsetX;
            int dz = dir.offsetZ;

            for (int ly = 0; ly < leaves[dirIndex]; ly++) {
                world.setBlock(x + dx, y + height + ly, z + dz, mod_LionKing.bananaLeaves, 0, 2);
            }
            ForgeDirection opposite = dir.getOpposite();
            world.setBlock(x + opposite.offsetX, y + height - 1, z + opposite.offsetZ, mod_LionKing.hangingBanana, dirIndex, 2);
            for (int ly = -1; ly < 1; ly++) {
                world.setBlock(x + dx * 2, y + height + leaves[dirIndex] + ly, z + dz * 2, mod_LionKing.bananaLeaves, 0, 2);
            }
        }

        world.setBlock(x, y - 1, z, Blocks.dirt, 0, 2);
        return true;
    }

    private boolean isBlockReplaceable(Block block) {
        return block == Blocks.air || block == mod_LionKing.bananaLeaves;
    }
}
