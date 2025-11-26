package lionking.world;

import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenerator;
import java.util.Random;
import cpw.mods.fml.common.FMLLog;
import lionking.mod_LionKing;

public class LKWorldGenLily extends WorldGenerator {
    private static final int ATTEMPTS = 10;
    private static final int RADIUS_XZ = 8;
    private static final int RADIUS_Y = 4;
    private static final int MIN_Y = 1;
    private static final int MAX_Y = 255;
    private static final int MAX_METADATA = 3;

    public LKWorldGenLily() {
        super(false);
        FMLLog.fine("Initialized LKWorldGenLily");
    }

    public boolean generate(World world, Random random, int x, int y, int z) {
        FMLLog.fine("Attempting to generate lily patch at (%d, %d, %d)", x, y, z);

        if (mod_LionKing.lily == null) {
            FMLLog.severe("Cannot generate lily patch at (%d, %d, %d): lily block is null", x, y, z);
            return false;
        }

        int liliesPlaced = 0;
        for (int i = 0; i < ATTEMPTS; i++) {
            int posX = x + random.nextInt(RADIUS_XZ) - random.nextInt(RADIUS_XZ);
            int posY = y + random.nextInt(RADIUS_Y) - random.nextInt(RADIUS_Y);
            int posZ = z + random.nextInt(RADIUS_XZ) - random.nextInt(RADIUS_XZ);

            if (posY < MIN_Y || posY >= MAX_Y) {
                FMLLog.fine("Cannot place lily at (%d, %d, %d): invalid Y coordinate", posX, posY, posZ);
                continue;
            }
            if (!world.isAirBlock(posX, posY, posZ)) {
                FMLLog.fine("Cannot place lily at (%d, %d, %d): block is not air", posX, posY, posZ);
                continue;
            }
            if (!mod_LionKing.lily.canPlaceBlockAt(world, posX, posY, posZ)) {
                FMLLog.fine("Cannot place lily at (%d, %d, %d): block cannot be placed", posX, posY, posZ);
                continue;
            }

            int metadata = random.nextInt(MAX_METADATA);
            world.setBlock(posX, posY, posZ, mod_LionKing.lily, metadata, 2);
            FMLLog.fine("Placed lily at (%d, %d, %d), meta=%d", posX, posY, posZ, metadata);
            liliesPlaced++;
        }

        if (liliesPlaced == 0) {
            FMLLog.warning("No lilies placed at (%d, %d, %d)", x, y, z);
            return false;
        }

        FMLLog.info("Successfully generated lily patch at (%d, %d, %d) with %d lilies", x, y, z, liliesPlaced);
        return true;
    }

    private boolean isBlockReplaceable(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        return block == Blocks.air || block.isLeaves(world, x, y, z) || block == Blocks.tallgrass || block == Blocks.deadbush;
    }
}