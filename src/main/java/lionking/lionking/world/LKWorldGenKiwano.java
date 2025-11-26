package lionking.world;

import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenerator;
import java.util.Random;
import cpw.mods.fml.common.FMLLog;
import lionking.mod_LionKing;

public class LKWorldGenKiwano extends WorldGenerator {
    private static final int ATTEMPTS = 32;
    private static final int RADIUS_XZ = 8;
    private static final int RADIUS_Y = 4;
    private static final int MIN_Y = 1;
    private static final int MAX_Y = 255;

    public LKWorldGenKiwano() {
        super(false);
        FMLLog.fine("Initialized LKWorldGenKiwano");
    }

    public boolean generate(World world, Random random, int x, int y, int z) {
        FMLLog.fine("Attempting to generate kiwano patch at (%d, %d, %d)", x, y, z);

        if (mod_LionKing.kiwanoBlock == null) {
            FMLLog.severe("Cannot generate kiwano patch at (%d, %d, %d): kiwanoBlock is null", x, y, z);
            return false;
        }

        int kiwanosPlaced = 0;
        for (int i = 0; i < ATTEMPTS; i++) {
            int posX = x + random.nextInt(RADIUS_XZ) - random.nextInt(RADIUS_XZ);
            int posY = y + random.nextInt(RADIUS_Y) - random.nextInt(RADIUS_Y);
            int posZ = z + random.nextInt(RADIUS_XZ) - random.nextInt(RADIUS_XZ);

            if (posY < MIN_Y || posY >= MAX_Y) {
                FMLLog.fine("Cannot place kiwano at (%d, %d, %d): invalid Y coordinate", posX, posY, posZ);
                continue;
            }
            if (!world.isAirBlock(posX, posY, posZ)) {
                FMLLog.fine("Cannot place kiwano at (%d, %d, %d): block is not air", posX, posY, posZ);
                continue;
            }
            if (world.getBlock(posX, posY - 1, posZ) != Blocks.sand) {
                FMLLog.fine("Cannot place kiwano at (%d, %d, %d): ground is not sand", posX, posY, posZ);
                continue;
            }
            if (!mod_LionKing.kiwanoBlock.canPlaceBlockAt(world, posX, posY, posZ)) {
                FMLLog.fine("Cannot place kiwano at (%d, %d, %d): block cannot be placed", posX, posY, posZ);
                continue;
            }

            world.setBlock(posX, posY, posZ, mod_LionKing.kiwanoBlock, 0, 2);
            FMLLog.fine("Placed kiwanoBlock at (%d, %d, %d)", posX, posY, posZ);
            kiwanosPlaced++;
        }

        if (kiwanosPlaced == 0) {
            FMLLog.warning("No kiwano blocks placed at (%d, %d, %d)", x, y, z);
            return false;
        }

        FMLLog.info("Successfully generated kiwano patch at (%d, %d, %d) with %d kiwanos", x, y, z, kiwanosPlaced);
        return true;
    }

    private boolean isBlockReplaceable(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        return block == Blocks.air || block.isLeaves(world, x, y, z) || block == Blocks.tallgrass || block == Blocks.deadbush;
    }
}