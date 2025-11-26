package lionking.world;

import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.gen.feature.WorldGenerator;
import java.util.Random;
import cpw.mods.fml.common.FMLLog;
import lionking.mod_LionKing;

public class LKWorldGenMinable extends WorldGenerator {
    private final Block minableBlock;
    private final int numberOfBlocks;
    private final int minableMetadata;

    public LKWorldGenMinable(int blockId, int count) {
        this(blockId, count, 0);
    }

    public LKWorldGenMinable(int blockId, int count, int metadata) {
        this.minableBlock = Block.getBlockById(blockId);
        this.numberOfBlocks = count;
        this.minableMetadata = metadata;
        FMLLog.fine("Initialized LKWorldGenMinable with blockId=%d, count=%d, metadata=%d", blockId, count, metadata);
    }

    public boolean generate(World world, Random random, int x, int y, int z) {
        FMLLog.fine("Attempting to generate ore vein at (%d, %d, %d) with %d blocks", x, y, z, numberOfBlocks);

        if (minableBlock == null || mod_LionKing.pridestone == null) {
            FMLLog.severe("Cannot generate ore vein at (%d, %d, %d): minableBlock or pridestone is null", x, y, z);
            return false;
        }

        float angle = random.nextFloat() * (float) Math.PI;
        double xStart = x + 8 + MathHelper.sin(angle) * numberOfBlocks / 8.0F;
        double xEnd = x + 8 - MathHelper.sin(angle) * numberOfBlocks / 8.0F;
        double zStart = z + 8 + MathHelper.cos(angle) * numberOfBlocks / 8.0F;
        double zEnd = z + 8 - MathHelper.cos(angle) * numberOfBlocks / 8.0F;
        double yStart = y + random.nextInt(3) - 2;
        double yEnd = y + random.nextInt(3) - 2;

        int blocksPlaced = 0;
        for (int step = 0; step <= numberOfBlocks; step++) {
            double lerpX = xStart + (xEnd - xStart) * step / numberOfBlocks;
            double lerpY = yStart + (yEnd - yStart) * step / numberOfBlocks;
            double lerpZ = zStart + (zEnd - zStart) * step / numberOfBlocks;
            double size = random.nextDouble() * numberOfBlocks / 16.0;
            double xSize = (MathHelper.sin(step * (float) Math.PI / numberOfBlocks) + 1.0F) * size + 1.0;
            double ySize = (MathHelper.sin(step * (float) Math.PI / numberOfBlocks) + 1.0F) * size + 1.0;

            int minX = MathHelper.floor_double(lerpX - xSize / 2.0);
            int minY = MathHelper.floor_double(lerpY - ySize / 2.0);
            int minZ = MathHelper.floor_double(lerpZ - xSize / 2.0);
            int maxX = MathHelper.floor_double(lerpX + xSize / 2.0);
            int maxY = MathHelper.floor_double(lerpY + ySize / 2.0);
            int maxZ = MathHelper.floor_double(lerpZ + xSize / 2.0);

            for (int dx = minX; dx <= maxX; dx++) {
                double xDist = (dx + 0.5 - lerpX) / (xSize / 2.0);
                if (xDist * xDist >= 1.0) continue;

                for (int dy = minY; dy <= maxY; dy++) {
                    double yDist = (dy + 0.5 - lerpY) / (ySize / 2.0);
                    if (xDist * xDist + yDist * yDist >= 1.0) continue;

                    for (int dz = minZ; dz <= maxZ; dz++) {
                        double zDist = (dz + 0.5 - lerpZ) / (xSize / 2.0);
                        if (xDist * xDist + yDist * yDist + zDist * zDist < 1.0 &&
                            world.getBlock(dx, dy, dz) == mod_LionKing.pridestone) {
                            if (dy >= 0 && dy < 256) {
                                world.setBlock(dx, dy, dz, minableBlock, minableMetadata, 2);
                                FMLLog.fine("Placed %s at (%d, %d, %d), meta=%d", minableBlock.getUnlocalizedName(), dx, dy, dz, minableMetadata);
                                blocksPlaced++;
                            }
                        }
                    }
                }
            }
        }

        if (blocksPlaced == 0) {
            FMLLog.warning("No blocks placed for ore vein at (%d, %d, %d)", x, y, z);
            return false;
        }

        FMLLog.info("Successfully generated ore vein at (%d, %d, %d) with %d blocks placed", x, y, z, blocksPlaced);
        return true;
    }

    private boolean isBlockReplaceable(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        return block == Blocks.air || block.isLeaves(world, x, y, z) || block == Blocks.tallgrass || block == Blocks.deadbush;
    }
}