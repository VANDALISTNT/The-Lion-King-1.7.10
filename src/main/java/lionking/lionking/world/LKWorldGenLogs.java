package lionking.world;

import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenerator;
import java.util.Random;
import cpw.mods.fml.common.FMLLog;
import lionking.mod_LionKing;

public class LKWorldGenLogs extends WorldGenerator {
    private static final int LOG_LENGTH = 7;
    private static final int GRASS_RADIUS_X = 5;
    private static final int GRASS_RADIUS_Y = 3;
    private static final int GRASS_RADIUS_Z = 10;

    public LKWorldGenLogs() {
        super(false);
        FMLLog.fine("Initialized LKWorldGenLogs");
    }

    public boolean generate(World world, Random random, int x, int y, int z) {
        FMLLog.fine("Attempting to generate log line at (%d, %d, %d)", x, y, z);

        if (mod_LionKing.log == null) {
            FMLLog.severe("Cannot generate log line at (%d, %d, %d): log block is null", x, y, z);
            return false;
        }

        for (int offsetZ = 0; offsetZ < LOG_LENGTH; offsetZ++) {
            Block groundBlock = world.getBlock(x, y - 1, z + offsetZ);
            if (groundBlock != Blocks.grass && groundBlock != Blocks.sand) {
                FMLLog.fine("Cannot generate log at (%d, %d, %d): invalid ground block %s", x, y - 1, z + offsetZ, groundBlock != null ? groundBlock.getUnlocalizedName() : "null");
                return false;
            }
            if (!isBlockReplaceable(world, world.getBlock(x, y, z + offsetZ), x, y, z + offsetZ)) {
                FMLLog.fine("Cannot generate log at (%d, %d, %d): non-replaceable block %s", x, y, z + offsetZ, world.getBlock(x, y, z + offsetZ) != null ? world.getBlock(x, y, z + offsetZ).getUnlocalizedName() : "null");
                return false;
            }
        }

        int logsPlaced = 0;
        for (int offsetZ = 0; offsetZ < LOG_LENGTH; offsetZ++) {
            world.setBlock(x, y, z + offsetZ, mod_LionKing.log, 0, 2);
            FMLLog.fine("Placed log at (%d, %d, %d)", x, y, z + offsetZ);
            logsPlaced++;
            if (world.getBlock(x, y - 1, z + offsetZ) == Blocks.grass) {
                world.setBlock(x, y - 1, z + offsetZ, Blocks.dirt, 0, 2);
                FMLLog.fine("Converted grass to dirt at (%d, %d, %d)", x, y - 1, z + offsetZ);
            }
        }

        int grassPlaced = 0;
        for (int dx = -GRASS_RADIUS_X; dx <= GRASS_RADIUS_X; dx++) {
            for (int dy = -GRASS_RADIUS_Y; dy < GRASS_RADIUS_Y; dy++) {
                for (int dz = -GRASS_RADIUS_Z; dz < GRASS_RADIUS_Z; dz++) {
                    int posX = x + dx;
                    int posY = y + dy;
                    int posZ = z + dz;
                    if (world.getBlock(posX, posY, posZ) == Blocks.grass &&
                        world.isAirBlock(posX, posY + 1, posZ) &&
                        random.nextInt(4) != 0) {
                        world.setBlock(posX, posY + 1, posZ, Blocks.tallgrass, 1, 2);
                        FMLLog.fine("Placed tallgrass at (%d, %d, %d)", posX, posY + 1, posZ);
                        grassPlaced++;
                    }
                }
            }
        }

        if (logsPlaced == 0) {
            FMLLog.warning("No logs placed at (%d, %d, %d)", x, y, z);
            return false;
        }

        FMLLog.info("Successfully generated log line at (%d, %d, %d) with %d logs and %d tallgrass", x, y, z, logsPlaced, grassPlaced);
        return true;
    }

    private boolean isBlockReplaceable(World world, Block block, int x, int y, int z) {
        return block == Blocks.tallgrass || block == mod_LionKing.aridGrass || block == Blocks.air;
    }
}