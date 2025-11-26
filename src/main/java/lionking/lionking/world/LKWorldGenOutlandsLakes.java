package lionking.world;

import net.minecraft.world.World;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenerator;
import java.util.Random;
import cpw.mods.fml.common.FMLLog;
import lionking.mod_LionKing;

public class LKWorldGenOutlandsLakes extends WorldGenerator {
    private final Block liquidBlock;

    public LKWorldGenOutlandsLakes(boolean isWater) {
        super(false);
        this.liquidBlock = isWater ? Blocks.water : Blocks.lava;
        FMLLog.fine("Initialized LKWorldGenOutlandsLakes with liquid: %s", isWater ? "water" : "lava");
    }

    public boolean generate(World world, Random random, int x, int y, int z) {
        x -= 8;
        z -= 8;
        FMLLog.fine("Attempting to generate %s lake at (%d, %d, %d)", liquidBlock.getMaterial() == Material.lava ? "lava" : "water", x, y, z);

        while (y > 0 && world.isAirBlock(x, y, z)) {
            y--;
        }
        y -= 4;

        boolean[] shape = new boolean[2048];
        int iterations = random.nextInt(4) + 4;
        FMLLog.fine("Generating lake shape with %d iterations at (%d, %d, %d)", iterations, x, y, z);
        generateShape(shape, random);

        if (!checkPlacement(world, shape, x, y, z)) {
            FMLLog.warning("Cannot generate %s lake at (%d, %d, %d): invalid placement", liquidBlock.getMaterial() == Material.lava ? "lava" : "water", x, y, z);
            return false;
        }

        int blocksPlaced = placeLakeBlocks(world, shape, x, y, z);
        if (blocksPlaced == 0) {
            FMLLog.warning("No blocks placed for %s lake at (%d, %d, %d)", liquidBlock.getMaterial() == Material.lava ? "lava" : "water", x, y, z);
            return false;
        }

        placeSandCover(world, shape, x, y, z, random);

        if (liquidBlock.getMaterial() == Material.lava) {
            surroundLavaWithStone(world, shape, x, y, z, random);
        }

        FMLLog.info("Successfully generated %s lake at (%d, %d, %d)", liquidBlock.getMaterial() == Material.lava ? "lava" : "water", x, y, z);
        return true;
    }

    private void generateShape(boolean[] shape, Random random) {
        int iterations = random.nextInt(4) + 4;
        for (int i = 0; i < iterations; i++) {
            double xSize = random.nextDouble() * 6 + 3;
            double ySize = random.nextDouble() * 4 + 2;
            double zSize = random.nextDouble() * 6 + 3;
            double xCenter = random.nextDouble() * (16 - xSize - 2) + 1 + xSize / 2;
            double yCenter = random.nextDouble() * (8 - ySize - 4) + 2 + ySize / 2;
            double zCenter = random.nextDouble() * (16 - zSize - 2) + 1 + zSize / 2;

            for (int dx = 1; dx < 15; dx++) {
                for (int dz = 1; dz < 15; dz++) {
                    for (int dy = 1; dy < 7; dy++) {
                        double xDist = (dx - xCenter) / (xSize / 2);
                        double yDist = (dy - yCenter) / (ySize / 2);
                        double zDist = (dz - zCenter) / (zSize / 2);
                        if (xDist * xDist + yDist * yDist + zDist * zDist < 1.0) {
                            shape[(dx * 16 + dz) * 8 + dy] = true;
                        }
                    }
                }
            }
        }
        FMLLog.fine("Generated lake shape with %d iterations", iterations);
    }

    private boolean checkPlacement(World world, boolean[] shape, int x, int y, int z) {
        for (int dx = 0; dx < 16; dx++) {
            for (int dz = 0; dz < 16; dz++) {
                for (int dy = 0; dy < 8; dy++) {
                    boolean isEdge = !shape[(dx * 16 + dz) * 8 + dy] &&
                            ((dx < 15 && shape[((dx + 1) * 16 + dz) * 8 + dy]) ||
                             (dx > 0 && shape[((dx - 1) * 16 + dz) * 8 + dy]) ||
                             (dz < 15 && shape[(dx * 16 + (dz + 1)) * 8 + dy]) ||
                             (dz > 0 && shape[(dx * 16 + (dz - 1)) * 8 + dy]) ||
                             (dy < 7 && shape[(dx * 16 + dz) * 8 + (dy + 1)]) ||
                             (dy > 0 && shape[(dx * 16 + dz) * 8 + (dy - 1)]));
                    if (!isEdge) continue;

                    Block block = world.getBlock(x + dx, y + dy, z + dz);
                    if (block == null) {
                        FMLLog.fine("Null block at (%d, %d, %d) during placement check", x + dx, y + dy, z + dz);
                        return false;
                    }
                    Material material = block.getMaterial();
                    if (dy >= 4 && material.isLiquid()) {
                        FMLLog.fine("Liquid block at (%d, %d, %d) prevents lake placement", x + dx, y + dy, z + dz);
                        return false;
                    }
                    if (dy < 4 && !material.isSolid() && block != liquidBlock) {
                        FMLLog.fine("Non-solid block at (%d, %d, %d) prevents lake placement", x + dx, y + dy, z + dz);
                        return false;
                    }
                }
            }
        }
        FMLLog.fine("Placement check passed at (%d, %d, %d)", x, y, z);
        return true;
    }

    private int placeLakeBlocks(World world, boolean[] shape, int x, int y, int z) {
        int blocksPlaced = 0;
        for (int dx = 0; dx < 16; dx++) {
            for (int dz = 0; dz < 16; dz++) {
                for (int dy = 0; dy < 8; dy++) {
                    if (shape[(dx * 16 + dz) * 8 + dy]) {
                        Block block = dy < 4 ? liquidBlock : Blocks.air;
                        world.setBlock(x + dx, y + dy, z + dz, block, 0, 2);
                        FMLLog.fine("Placed %s at (%d, %d, %d)", block.getUnlocalizedName(), x + dx, y + dy, z + dz);
                        blocksPlaced++;
                    }
                }
            }
        }
        return blocksPlaced;
    }

    private void placeSandCover(World world, boolean[] shape, int x, int y, int z, Random random) {
        for (int dx = 0; dx < 16; dx++) {
            for (int dz = 0; dz < 16; dz++) {
                for (int dy = 4; dy < 8; dy++) {
                    if (!shape[(dx * 16 + dz) * 8 + dy] &&
                            world.getBlock(x + dx, y + dy - 1, z + dz) == Blocks.sand &&
                            world.getSavedLightValue(EnumSkyBlock.Sky, x + dx, y + dy, z + dz) > 0) {
                        world.setBlock(x + dx, y + dy - 1, z + dz, Blocks.sand, 0, 2);
                        FMLLog.fine("Placed sand cover at (%d, %d, %d)", x + dx, y + dy - 1, z + dz);
                    }
                }
            }
        }
    }

    private void surroundLavaWithStone(World world, boolean[] shape, int x, int y, int z, Random random) {
        if (mod_LionKing.pridestone == null) {
            FMLLog.warning("Cannot surround lava lake at (%d, %d, %d): pridestone is null", x, y, z);
            return;
        }
        for (int dx = 0; dx < 16; dx++) {
            for (int dz = 0; dz < 16; dz++) {
                for (int dy = 0; dy < 8; dy++) {
                    boolean isEdge = !shape[(dx * 16 + dz) * 8 + dy] &&
                            ((dx < 15 && shape[((dx + 1) * 16 + dz) * 8 + dy]) ||
                             (dx > 0 && shape[((dx - 1) * 16 + dz) * 8 + dy]) ||
                             (dz < 15 && shape[(dx * 16 + (dz + 1)) * 8 + dy]) ||
                             (dz > 0 && shape[(dx * 16 + (dz - 1)) * 8 + dy]) ||
                             (dy < 7 && shape[(dx * 16 + dz) * 8 + (dy + 1)]) ||
                             (dy > 0 && shape[(dx * 16 + dz) * 8 + (dy - 1)]));
                    if (isEdge && (dy < 4 || random.nextInt(2) != 0) &&
                            world.getBlock(x + dx, y + dy, z + dz).getMaterial().isSolid()) {
                        world.setBlock(x + dx, y + dy, z + dz, mod_LionKing.pridestone, 1, 2);
                        FMLLog.fine("Placed pridestone at (%d, %d, %d)", x + dx, y + dy, z + dz);
                    }
                }
            }
        }
    }

    private boolean isBlockReplaceable(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        return block == Blocks.air || block.isLeaves(world, x, y, z) || block == Blocks.tallgrass || block == Blocks.deadbush;
    }
}