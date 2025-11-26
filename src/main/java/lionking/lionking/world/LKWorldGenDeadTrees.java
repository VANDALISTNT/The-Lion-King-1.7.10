package lionking.world;

import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenerator;
import java.util.Random;
import cpw.mods.fml.common.FMLLog;
import lionking.mod_LionKing;

public class LKWorldGenDeadTrees extends WorldGenerator {
    private static final int MIN_HEIGHT = 6;
    private static final int HEIGHT_VARIATION = 3;
    private static final int MAX_WORLD_HEIGHT = 256;
    private static final int WOOD_METADATA = 1;

    public LKWorldGenDeadTrees() {
        super(false);
        FMLLog.fine("Initialized LKWorldGenDeadTrees");
    }

    public boolean generate(World world, Random random, int x, int y, int z) {
        FMLLog.fine("Attempting to generate dead tree at (%d, %d, %d)", x, y, z);

        if (mod_LionKing.prideWood2 == null) {
            FMLLog.severe("Cannot generate dead tree at (%d, %d, %d): prideWood2 is null", x, y, z);
            return false;
        }

        int height = MIN_HEIGHT + random.nextInt(HEIGHT_VARIATION);
        if (y < 1 || y + height >= MAX_WORLD_HEIGHT) {
            FMLLog.fine("Cannot generate dead tree at (%d, %d, %d): invalid height (height=%d)", x, y, z, height);
            return false;
        }

        Block groundBlock = world.getBlock(x, y - 1, z);
        Block startBlock = world.getBlock(x, y, z);
        if (groundBlock != mod_LionKing.outsand && groundBlock != Blocks.sand) {
            FMLLog.fine("Cannot generate dead tree at (%d, %d, %d): invalid ground block %s", x, y, z, groundBlock != null ? groundBlock.getUnlocalizedName() : "null");
            return false;
        }
        if (!isBlockReplaceable(world, startBlock, x, y, z)) {
            FMLLog.fine("Cannot generate dead tree at (%d, %d, %d): non-replaceable start block %s", x, y, z, startBlock != null ? startBlock.getUnlocalizedName() : "null");
            return false;
        }

        int blocksPlaced = 0;
        for (int dy = 0; dy < height + 1; dy++) {
            if (!isBlockReplaceable(world, world.getBlock(x, y + dy, z), x, y + dy, z)) {
                FMLLog.fine("Cannot generate dead tree at (%d, %d, %d): non-replaceable block at (%d, %d, %d)", x, y, z, x, y + dy, z);
                return false;
            }
        }

        int branchHeightWest = height - 1 - random.nextInt(2);
        int branchHeightEast = height - 1 - random.nextInt(2);
        int branchHeightNorth = height - 1 - random.nextInt(2);
        int branchHeightSouth = height - 1 - random.nextInt(2);
        if (!isBlockReplaceable(world, world.getBlock(x - 1, y + branchHeightWest, z), x - 1, y + branchHeightWest, z) ||
            !isBlockReplaceable(world, world.getBlock(x + 1, y + branchHeightEast, z), x + 1, y + branchHeightEast, z) ||
            !isBlockReplaceable(world, world.getBlock(x, y + branchHeightNorth, z - 1), x, y + branchHeightNorth, z - 1) ||
            !isBlockReplaceable(world, world.getBlock(x, y + branchHeightSouth, z + 1), x, y + branchHeightSouth, z + 1)) {
            FMLLog.fine("Cannot generate dead tree at (%d, %d, %d): non-replaceable block at branch positions", x, y, z);
            return false;
        }

        for (int dy = 0; dy < height + 1; dy++) {
            world.setBlock(x, y + dy, z, mod_LionKing.prideWood2, WOOD_METADATA, 2);
            FMLLog.fine("Placed prideWood2 at (%d, %d, %d), meta=%d", x, y + dy, z, WOOD_METADATA);
            blocksPlaced++;
        }

        world.setBlock(x - 1, y + branchHeightWest, z, mod_LionKing.prideWood2, WOOD_METADATA, 2);
        FMLLog.fine("Placed branch (west) at (%d, %d, %d), meta=%d", x - 1, y + branchHeightWest, z, WOOD_METADATA);
        world.setBlock(x + 1, y + branchHeightEast, z, mod_LionKing.prideWood2, WOOD_METADATA, 2);
        FMLLog.fine("Placed branch (east) at (%d, %d, %d), meta=%d", x + 1, y + branchHeightEast, z, WOOD_METADATA);
        world.setBlock(x, y + branchHeightNorth, z - 1, mod_LionKing.prideWood2, WOOD_METADATA, 2);
        FMLLog.fine("Placed branch (north) at (%d, %d, %d), meta=%d", x, y + branchHeightNorth, z - 1, WOOD_METADATA);
        world.setBlock(x, y + branchHeightSouth, z + 1, mod_LionKing.prideWood2, WOOD_METADATA, 2);
        FMLLog.fine("Placed branch (south) at (%d, %d, %d), meta=%d", x, y + branchHeightSouth, z + 1, WOOD_METADATA);
        blocksPlaced += 4;

        if (blocksPlaced == 0) {
            FMLLog.warning("No blocks placed for dead tree at (%d, %d, %d)", x, y, z);
            return false;
        }

        FMLLog.info("Successfully generated dead tree at (%d, %d, %d) with height=%d and %d blocks", x, y, z, height, blocksPlaced);
        return true;
    }

    private boolean isBlockReplaceable(World world, Block block, int x, int y, int z) {
        return block == Blocks.air || block.isLeaves(world, x, y, z) || block == Blocks.tallgrass || block == Blocks.deadbush;
    }
}