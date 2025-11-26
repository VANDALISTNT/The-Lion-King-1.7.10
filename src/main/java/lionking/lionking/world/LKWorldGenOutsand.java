package lionking.world;

import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenerator;
import java.util.Random;
import cpw.mods.fml.common.FMLLog;
import lionking.mod_LionKing;

public class LKWorldGenOutsand extends WorldGenerator {
    private static final int MIN_RADIUS = 2;
    private static final int RADIUS_VARIATION = 4;
    private static final int HEIGHT_RANGE = 3;
    private static final int FIRE_CHANCE = 8;

    public LKWorldGenOutsand() {
        super(false);
        FMLLog.fine("Initialized LKWorldGenOutsand");
    }

    public boolean generate(World world, Random random, int x, int y, int z) {
        FMLLog.fine("Attempting to generate outsand patch at (%d, %d, %d)", x, y, z);

        if (mod_LionKing.outsand == null) {
            FMLLog.severe("Cannot generate outsand patch at (%d, %d, %d): outsand block is null", x, y, z);
            return false;
        }

        int radius = MIN_RADIUS + random.nextInt(RADIUS_VARIATION);
        FMLLog.fine("Selected radius %d for outsand patch at (%d, %d, %d)", radius, x, y, z);

        int replacedBlocks = 0;
        for (int dx = x - radius; dx <= x + radius; dx++) {
            for (int dz = z - radius; dz <= z + radius; dz++) {
                int distX = dx - x;
                int distZ = dz - z;
                if (distX * distX + distZ * distZ > radius * radius) {
                    continue;
                }

                for (int dy = y - HEIGHT_RANGE; dy <= y + HEIGHT_RANGE; dy++) {
                    if (world.getBlock(dx, dy, dz) == Blocks.sand) {
                        world.setBlock(dx, dy, dz, mod_LionKing.outsand, 0, 3);
                        FMLLog.fine("Placed outsand at (%d, %d, %d)", dx, dy, dz);
                        replacedBlocks++;
                        if (random.nextInt(FIRE_CHANCE) == 0 && world.isAirBlock(dx, dy + 1, dz)) {
                            world.setBlock(dx, dy + 1, dz, Blocks.fire, 0, 3);
                            FMLLog.fine("Placed fire at (%d, %d, %d)", dx, dy + 1, dz);
                        }
                    }
                }
            }
        }

        if (replacedBlocks == 0) {
            FMLLog.warning("No sand blocks replaced for outsand patch at (%d, %d, %d)", x, y, z);
            return false;
        }

        FMLLog.info("Successfully generated outsand patch at (%d, %d, %d) with radius %d", x, y, z, radius);
        return true;
    }

    public boolean isRadiusClearOfOutsand(World world, int x, int y, int z, int radius) {
        for (int dx = x - radius; dx <= x + radius; dx++) {
            for (int dy = y - radius; dy <= y + radius; dy++) {
                for (int dz = z - radius; dz <= z + radius; dz++) {
                    if (dy >= 0 && dy < 256 && world.getBlock(dx, dy, dz) == mod_LionKing.outsand) {
                        FMLLog.fine("Outsand found at (%d, %d, %d), radius not clear", dx, dy, dz);
                        return false;
                    }
                }
            }
        }
        FMLLog.fine("Radius clear of outsand at (%d, %d, %d)", x, y, z);
        return true;
    }

    private boolean isBlockReplaceable(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        return block == Blocks.air || block.isLeaves(world, x, y, z) || block == Blocks.tallgrass || block == Blocks.deadbush;
    }
}