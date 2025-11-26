package lionking.world;

import net.minecraft.world.World;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.world.gen.feature.WorldGenerator;
import java.util.Random;
import cpw.mods.fml.common.FMLLog;
import lionking.mod_LionKing;

public class LKWorldGenMaize extends WorldGenerator {
    private static final int ATTEMPTS = 20;
    private static final int RADIUS = 4;
    private static final int MIN_HEIGHT = 2;
    private static final int MAX_HEIGHT_VARIATION = 3;

    public LKWorldGenMaize() {
        super(false);
        FMLLog.fine("Initialized LKWorldGenMaize");
    }

    public boolean generate(World world, Random random, int x, int y, int z) {
        FMLLog.fine("Attempting to generate maize patch at (%d, %d, %d)", x, y, z);

        if (mod_LionKing.maize == null) {
            FMLLog.severe("Cannot generate maize patch at (%d, %d, %d): maize block is null", x, y, z);
            return false;
        }

        Block groundBlock = world.getBlock(x, y - 1, z);
        if (groundBlock != Blocks.grass && groundBlock != Blocks.dirt) {
            FMLLog.fine("Cannot generate maize patch at (%d, %d, %d): invalid ground block %s", x, y, z, groundBlock != null ? groundBlock.getUnlocalizedName() : "null");
            return false;
        }

        int blocksPlaced = 0;
        for (int attempt = 0; attempt < ATTEMPTS; attempt++) {
            int posX = x + random.nextInt(RADIUS) - random.nextInt(RADIUS);
            int posY = y;
            int posZ = z + random.nextInt(RADIUS) - random.nextInt(RADIUS);

            if (!world.isAirBlock(posX, posY, posZ)) {
                FMLLog.fine("Cannot place maize at (%d, %d, %d): block is not air", posX, posY, posZ);
                continue;
            }
            if (!isWaterNearby(world, posX, posY - 1, posZ)) {
                FMLLog.fine("Cannot place maize at (%d, %d, %d): no water nearby", posX, posY, posZ);
                continue;
            }

            int height = MIN_HEIGHT + random.nextInt(random.nextInt(MAX_HEIGHT_VARIATION) + 1);
            for (int dy = 0; dy < height; dy++) {
                int metadata = (dy == 0) ? 0 : 1;
                if (mod_LionKing.maize.canBlockStay(world, posX, posY + dy, posZ)) {
                    world.setBlock(posX, posY + dy, posZ, mod_LionKing.maize, metadata, 2);
                    FMLLog.fine("Placed maize at (%d, %d, %d), meta=%d", posX, posY + dy, posZ, metadata);
                    blocksPlaced++;
                } else {
                    FMLLog.fine("Cannot place maize at (%d, %d, %d): block cannot stay", posX, posY + dy, posZ);
                    break;
                }
            }
        }

        if (blocksPlaced == 0) {
            FMLLog.warning("No maize blocks placed at (%d, %d, %d)", x, y, z);
            return false;
        }

        FMLLog.info("Successfully generated maize patch at (%d, %d, %d) with %d blocks", x, y, z, blocksPlaced);
        return true;
    }

    private boolean isWaterNearby(World world, int x, int y, int z) {
        boolean hasWater = world.getBlock(x - 1, y, z).getMaterial() == Material.water ||
                          world.getBlock(x + 1, y, z).getMaterial() == Material.water ||
                          world.getBlock(x, y, z - 1).getMaterial() == Material.water ||
                          world.getBlock(x, y, z + 1).getMaterial() == Material.water;
        if (hasWater) {
            FMLLog.fine("Water found near (%d, %d, %d)", x, y, z);
        }
        return hasWater;
    }

    private boolean isBlockReplaceable(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        return block == Blocks.air || block.isLeaves(world, x, y, z) || block == Blocks.tallgrass || block == Blocks.deadbush;
    }
}