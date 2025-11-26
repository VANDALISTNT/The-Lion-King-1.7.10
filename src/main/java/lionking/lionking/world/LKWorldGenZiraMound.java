package lionking.world;

import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenerator;
import java.util.Random;
import java.util.function.BiPredicate;
import cpw.mods.fml.common.FMLLog;

import lionking.mod_LionKing;
import lionking.entity.LKEntityZira;
import lionking.common.LKLevelData;

public class LKWorldGenZiraMound extends WorldGenerator {
    private static final int MOUND_RADIUS = 28;
    private static final int MAX_HEIGHT = 51;
    private static final Block FRAME = mod_LionKing.outlandsPortalFrame;
    private static final Block POOL = mod_LionKing.outlandsPool;
    private static final Block GLOWING = mod_LionKing.outshroomGlowing;
    private static final Block AIR = Blocks.air;

    private void setVerticalLine(World world, int x, int yStart, int yEnd, int z, Block block) {
        for (int y = yStart; y <= yEnd; y++) {
            world.setBlock(x, y, z, block, block == AIR ? 0 : 1, 2);
        }
    }

    private void setHorizontalLine(World world, int xStart, int xEnd, int y, int z, Block block) {
        for (int x = xStart; x <= xEnd; x++) {
            world.setBlock(x, y, z, block, block == AIR ? 0 : 1, 2);
        }
    }

    private void setHorizontalLineZ(World world, int x, int y, int zMin, int zMax, Block block) {
        for (int z = zMin; z <= zMax; z++) {
            world.setBlock(x, y, z, block, 0, 2);
        }
    }

    private void setRectangleXY(World world, int xMin, int xMax, int yMin, int yMax, int z, Block block) {
        for (int x = xMin; x <= xMax; x++) {
            for (int y = yMin; y <= yMax; y++) {
                world.setBlock(x, y, z, block, 0, 2);
            }
        }
    }

    private void fillRect(World world, int xMin, int xMax, int y, int zMin, int zMax, Block block, int meta) {
        for (int x = xMin; x <= xMax; x++) {
            for (int z = zMin; z <= zMax; z++) {
                world.setBlock(x, y, z, block, meta, 2);
            }
        }
    }

    private void fillRectConditional(World world, int xMin, int xMax, int y, int zMin, int zMax, Block block, int meta, BiPredicate<Integer, Integer> condition) {
        for (int x = xMin; x <= xMax; x++) {
            for (int z = zMin; z <= zMax; z++) {
                if (condition.test(x - (xMin + xMax) / 2, z - (zMin + zMax) / 2)) {
                    world.setBlock(x, y, z, block, meta, 2);
                }
            }
        }
    }

    private void setBlocksFromCoords(World world, int i, int j, int k, int[][] coords, Block block, int meta) {
        for (int[] coord : coords) {
            world.setBlock(i + coord[0], j + coord[1], k + coord[2], block, meta, 2);
        }
    }

    private void setBlocksHeight(World world, int i, int j, int k, int[][] coords, int yStart, int yEnd, Block block, int meta) {
        for (int[] coord : coords) {
            for (int y = yStart; y <= yEnd; y++) {
                world.setBlock(i + coord[0], j + y, k + coord[1], block, meta, 2);
            }
        }
    }

    private void fillHeight(World world, int x, int yStart, int yEnd, int z, Block block, int meta) {
        for (int y = yStart; y <= yEnd; y++) {
            world.setBlock(x, y, z, block, meta, 2);
        }
    }

    private void setBlock(World world, int x, int y, int z, Block block, int meta) {
        world.setBlock(x, y, z, block, meta, 2);
    }

    private void clearBlock(World world, int x, int y, int z) {
        world.setBlock(x, y, z, AIR, 0, 2);
    }

    @Override
    public boolean generate(World world, Random random, int i, int j, int k) {
        FMLLog.fine("Attempting to generate Zira's mound at (%d, %d, %d)", i, j, k);

        if (j + MAX_HEIGHT >= world.getHeight()) {
            FMLLog.severe("Mound height (%d) exceeds world limit (%d) at (%d, %d, %d)", j + MAX_HEIGHT, world.getHeight(), i, j, k);
            return false;
        }

        for (int layer = 0; layer <= 32; layer++) {
            FMLLog.fine("Generating layer %d at (%d, %d, %d)", layer, i, j, k);
            generateLayer(world, i, j, k, layer);
        }

        generateBaseStructure(world, i, j, k);
        generateOuterLayers(world, i, j, k);
        generatePoolCover(world, i, j, k);
        clearAboveMound(world, i, j, k);
        generatePortalBase(world, i, j, k);
        generatePortalWalls(world, i, j, k);
        generatePortalTop(world, i, j, k);
        spawnAltarAndZira(world, i, j, k);
        finalizeStructure(world, i, j, k, random);
        LKLevelData.markMoundLocation(i, j, k);
        FMLLog.fine("Zira's mound successfully generated at (%d, %d, %d)", i, j, k);
        return true;
    }

    private void generateLayer(World world, int i, int j, int k, int layer) {
        if (layer == 0) generate0(world, i, j, k);
        else if (layer == 1) generate1(world, i, j, k);
        else if (layer == 2) generate2(world, i, j, k);
        else if (layer == 3) generate3(world, i, j, k);
        else if (layer == 4) generate4(world, i, j, k);
        else if (layer == 5) generate5(world, i, j, k);
        else if (layer == 6) generate6(world, i, j, k);
        else if (layer == 7) generate7(world, i, j, k);
        else if (layer == 8) generate8(world, i, j, k);
        else if (layer == 9) generateLayer9(world, i, j, k);
        else if (layer == 10) generateLayer10(world, i, j, k);
        else if (layer == 11) generateLayer11(world, i, j, k);
        else if (layer == 12) generateLayer12(world, i, j, k);
        else if (layer == 13) generateLayer13(world, i, j, k);
        else if (layer == 14) generateLayer14(world, i, j, k);
        else if (layer == 15) generateLayer15(world, i, j, k);
        else if (layer == 16) generateLayer16(world, i, j, k);
        else if (layer == 17) generateLayer17(world, i, j, k);
        else if (layer == 18) generateLayer18(world, i, j, k);
        else if (layer == 19) generateLayer19(world, i, j, k);
        else if (layer == 20) generateLayer20(world, i, j, k);
        else if (layer == 21) generateLayer21(world, i, j, k);
        else if (layer == 22) generateLayer22(world, i, j, k);
        else if (layer == 23) generateLayer23(world, i, j, k);
        else if (layer == 24) generateLayer24(world, i, j, k);
        else if (layer == 25) generateLayer25(world, i, j, k);
        else if (layer == 26) generateLayer26(world, i, j, k);
        else if (layer == 27) generateLayer27(world, i, j, k);
        else if (layer == 28) generateLayer28(world, i, j, k);
        else if (layer == 29) generateLayer29(world, i, j, k);
        else if (layer == 30) generateLayer30(world, i, j, k);
        else if (layer == 31) generateLayer31(world, i, j, k);
        else if (layer == 32) generateLayer32(world, i, j, k);
        FMLLog.fine("Generating layer %d at (%d, %d, %d)", layer, i, j, k);
    }
    
    private void generatePortalBase(World world, int i, int j, int k) {
        FMLLog.fine("Generating portal base at (%d, %d, %d)", i, j, k);
        int[][] portalCoords = {
            {0, -12}, {1, -12}, {-1, -12}, {2, -12}, {-2, -12}, {0, -13}, {1, -13}, {-1, -13},
            {2, -13}, {-2, -13}, {-3, -12}, {-4, -11}, {-5, -11}, {-6, -10}, {-7, -9}, {-8, -8},
            {-9, -7}, {-10, -6}, {-11, -5}, {-11, -4}, {-12, -3}, {-12, -2}, {-12, -1}, {-12, 0},
            {-12, 1}, {-12, 2}
        };
        setBlocksHeight(world, i, j, k, portalCoords, 4, 6, FRAME, 1);

        int[][] verticalCoords = {{0, -14}, {1, -14}, {-1, -14}, {2, -14}, {-2, -14}, {-3, -13}, {-7, -11}};
        setBlocksHeight(world, i, j, k, verticalCoords, 7, 13, FRAME, 1);

        int[][] sideCoords = {
            {-13, -3}, {-13, -2}, {-13, -1}, {-13, 0}, {-13, 1}, {-13, 2}, {-12, -4}, {-12, -5},
            {-11, -6}, {-10, -7}, {-9, -8}, {-8, -9}, {-7, -10}
        };
        setBlocksHeight(world, i, j, k, sideCoords, 7, 9, FRAME, 1);

        for (int x = -14; x <= -7; x++) {
            int z = x + 4;
            world.setBlock(i + x, j + 10, k + z, FRAME, 1, 2);
            fillHeight(world, i + x, j + 11, j + 13, k + z, FRAME, 1);
        }
    }

    private void generatePortalWalls(World world, int i, int j, int k) {
        FMLLog.fine("Generating portal walls at (%d, %d, %d)", i, j, k);
        int[] wallX = {-6, -5, -4, -3, 3, 4, 5, 6, 7};
        for (int x : wallX) {
            int zOffset = (x + 6) / 2;
            fillHeight(world, i + x, j + 9, j + 13, k - 14 + zOffset, FRAME, 1);
        }

        for (int z = 3; z <= 6; z++) {
            fillHeight(world, i - 14 + (z - 3), j + 9, j + 13, k + z, FRAME, 1);
        }

        fillRectConditional(world, i - 11, i + 12, j + 4, k - 12, k + 13, FRAME, 1,
                (x, z) -> Math.abs(x) > 9 || Math.abs(z) > 9);
        fillRect(world, i - 11, i + 12, j + 5, k - 12, k + 13, FRAME, 1);
        fillRectConditional(world, i - 9, i + 12, j + 6, k - 12, k + 12, FRAME, 1,
                (x, z) -> Math.abs(x) >= 8 || Math.abs(z) >= 10);
    }

    private void generatePortalTop(World world, int i, int j, int k) {
        FMLLog.fine("Generating portal top at (%d, %d, %d)", i, j, k);
        fillRect(world, i - 14, i + 14, j + 13, k - 14, k - 14, FRAME, 1);
        fillRect(world, i - 14, i + 14, j + 13, k + 14, k + 14, FRAME, 1);
        fillRect(world, i - 14, i - 14, j + 13, k - 14, k + 14, FRAME, 1);
        fillRect(world, i + 14, i + 14, j + 13, k - 14, k + 14, FRAME, 1);
    }
   
    private void generatePoolCover(World world, int i, int j, int k) {
    FMLLog.fine("Generating pool cover at (%d, %d, %d)", i, j, k);
        int[][] poolCoverCoords = {
            {-4, 17, 12}, {-4, 17, 11}, {-3, 17, 12}, {-2, 17, 12}, {-1, 17, 12},
            {-3, 17, 11}, {-2, 17, 11}, {-1, 17, 11}, {-1, 17, 10}, {-1, 18, 10},
            {-2, 17, 10}, {-3, 17, 10}, {-4, 17, 10}, {-2, 17, 9}, {-3, 17, 9},
            {-4, 17, 9}, {-4, 17, 8}, {-5, 17, 8}, {-6, 17, 8}, {-5, 17, 9},
            {-6, 17, 9}, {-5, 17, 10}, {-6, 17, 10}, {-5, 17, 11}, {-1, 18, 11},
            {-1, 18, 12}, {-2, 18, 12}, {-2, 18, 11}, {-2, 18, 10}, {-3, 18, 10},
            {-3, 18, 9}, {-4, 18, 9}, {-5, 18, 9}, {-6, 18, 10}, {-5, 18, 10},
            {-4, 18, 10}, {-3, 18, 11}, {-4, 18, 11}, {-3, 18, 12}, {-5, 19, 10},
            {-4, 19, 10}, {-3, 19, 10}, {-3, 19, 11}, {-2, 19, 11}, {-2, 19, 12},
            {-1, 19, 11}, {-2, 19, 10}, {-2, 20, 11}, {-2, 21, 11}, {-3, 20, 10},
            {-3, 21, 10}, {-2, 20, 10}, {-4, 20, 10}, {-4, 19, 9}, {-4, 21, 10},
            {-3, 22, 10}, {-3, 23, 10}, {-4, 22, 10}, {-2, 22, 11}, {-3, 24, 10},
            {-3, 25, 10}, {-2, 21, 10}, {-3, 20, 11}, {-3, 21, 11}, {-5, 20, 10},
            {-4, 19, 11}, {-5, 18, 11}, {-4, 20, 11}, {-5, 21, 10}, {-4, 23, 10},
            {-3, 22, 11}, {-3, 23, 11}, {-3, 24, 11}, {-3, 26, 10}, {-3, 27, 10},
            {-3, 28, 10}, {-4, 20, 9}
        };
        setBlocksFromCoords(world, i, j, k, poolCoverCoords, FRAME, 1);
    }
    
    private void clearAboveMound(World world, int i, int j, int k) {
        FMLLog.fine("Clearing above mound at (%d, %d, %d)", i, j, k);
        for (int i1 = i - MOUND_RADIUS; i1 <= i + MOUND_RADIUS; i1++) {
            for (int k1 = k - MOUND_RADIUS; k1 <= k + MOUND_RADIUS; k1++) {
                int i2 = i1 - i;
                int k2 = k1 - k;
                if (i2 * i2 + k2 * k2 > MOUND_RADIUS * MOUND_RADIUS) continue;
                for (int j1 = 255; j1 >= j + 3; j1--) {
                    if (canReplace(world, i1, j1, k1)) world.setBlockToAir(i1, j1, k1);
                }
            }
        }
    }

    private void generateBaseStructure(World world, int i, int j, int k) {
        FMLLog.fine("Generating base structure at (%d, %d, %d)", i, j, k);
        for (int i1 = i - MOUND_RADIUS; i1 <= i + MOUND_RADIUS; i1++) {
            for (int k1 = k - MOUND_RADIUS; k1 <= k + MOUND_RADIUS; k1++) {
                int i2 = i1 - i;
                int k2 = k1 - k;
                if (i2 * i2 + k2 * k2 > MOUND_RADIUS * MOUND_RADIUS) continue;
                world.setBlock(i1, j + 2, k1, FRAME, 1, 2);
                for (int j1 = j + 3; j1 <= j + MAX_HEIGHT; j1++) {
                    if (world.isAirBlock(i1, j1, k1) && world.canBlockSeeTheSky(i1, j1, k1)) {
                        Object[] blockAndMetadata = getBlockForHeight(j1 - j);
                        world.setBlock(i1, j1, k1, (Block)blockAndMetadata[0], (Integer)blockAndMetadata[1], 2);
                    }
                }
            }
        }
    }

    private void generateOuterLayers(World world, int i, int j, int k) {
        FMLLog.fine("Generating outer layers at (%d, %d, %d)", i, j, k);
        for (int x = 1; x < 6; x++) {
            int radius = MOUND_RADIUS + x * 2;
            for (int i1 = i - radius; i1 <= i + radius; i1++) {
                for (int k1 = k - radius; k1 <= k + radius; k1++) {
                    int i2 = i1 - i;
                    int k2 = k1 - k;
                    if (i2 * i2 + k2 * k2 > radius * radius) continue;
                    for (int j1 = j + 49; j1 <= j + (MAX_HEIGHT - x); j1++) {
                        if (world.isAirBlock(i1, j1, k1) && world.canBlockSeeTheSky(i1, j1, k1)) {
                            world.setBlock(i1, j1, k1, Blocks.sand, 0, 2);
                        }
                    }
                }
            }
        }
    }

    private void spawnAltarAndZira(World world, int i, int j, int k) {
        FMLLog.fine("Spawning altar and Zira at (%d, %d, %d)", i, j, k);
        world.setBlock(i, j + 8, k, mod_LionKing.outlandsAltar, 0, 2);
        LKEntityZira zira = new LKEntityZira(world);
        zira.setLocationAndAngles(i, j + 17, k, 0.0F, 0.0F);
        if (!world.isRemote) {
            world.spawnEntityInWorld(zira);
            zira.spawnOutlandersInMound();
        }
    }

    private void finalizeStructure(World world, int i, int j, int k, Random random) {
        FMLLog.fine("Finalizing structure at (%d, %d, %d)", i, j, k);
        setBlock(world, i, j + MAX_HEIGHT, k, GLOWING, 0);
        setBlock(world, i + MOUND_RADIUS / 2, j + MAX_HEIGHT - 1, k, GLOWING, 0);
        setBlock(world, i - MOUND_RADIUS / 2, j + MAX_HEIGHT - 1, k, GLOWING, 0);
        setBlock(world, i, j + MAX_HEIGHT - 1, k + MOUND_RADIUS / 2, GLOWING, 0);
        setBlock(world, i, j + MAX_HEIGHT - 1, k - MOUND_RADIUS / 2, GLOWING, 0);

        for (int x = i - MOUND_RADIUS; x <= i + MOUND_RADIUS; x += 5) {
            for (int z = k - MOUND_RADIUS; z <= k + MOUND_RADIUS; z += 5) {
                if (random.nextFloat() < 0.2f && Math.abs(x - i) > 10 && Math.abs(z - k) > 10) {
                    int yTop = getTopBlock(world, x, z, j);
                    if (yTop > j && yTop <= j + MAX_HEIGHT) {
                        world.setBlock(x, yTop + 1, z, GLOWING, 0, 2);
                    }
                }
            }
        }
    }

    private Object[] getBlockForHeight(int height) {
        return height > 48 ? new Object[]{Blocks.sand, 0} :
               height > 46 ? new Object[]{Blocks.sandstone, 0} :
               height > 4 ? new Object[]{mod_LionKing.pridestone, 1} :
               new Object[]{Blocks.bedrock, 0};
    }

    private boolean canReplace(World world, int i, int j, int k) {
        Block block = world.getBlock(i, j, k);
        return block != null && block != FRAME && block != GLOWING && block != POOL;
    }

    private int getTopBlock(World world, int x, int z, int j) {
        for (int y = j + MAX_HEIGHT; y >= j; y--) {
            if (world.getBlock(x, y, z) != AIR) {
                return y;
            }
        }
        return j;
    }

    private void generate0(World world, int i, int j, int k) {
        fillRect(world, i - 2, i + 2, j + 3, k - 2, k + 2, FRAME, 1);
        fillRectConditional(world, i - 1, i + 1, j + 4, k - 1, k + 1, FRAME, 1, (x, z) -> x != 0 || z != 0);
        fillRectConditional(world, i - 1, i + 1, j + 5, k - 1, k + 1, FRAME, 1, (x, z) -> Math.abs(x) + Math.abs(z) == 1);
        fillRect(world, i - 1, i + 1, j + 6, k - 1, k + 1, FRAME, 1);
        fillRect(world, i - 1, i + 1, j + 7, k - 1, k + 1, FRAME, 1);
        setBlock(world, i + 2, j + 7, k - 2, FRAME, 1);
        setBlock(world, i - 2, j + 7, k, FRAME, 1);

        int[][] coords = {
            {-3, 3, -3}, {-4, 3, -4}, {-5, 3, -5}, {-6, 3, -6}, {-7, 3, -7},
            {3, 3, -3}, {4, 3, -4}, {5, 3, -5}, {6, 3, -6}, {7, 3, -7},
            {-3, 3, 3}, {-4, 3, 4}, {-5, 3, 5}, {-6, 3, 6}, {-7, 3, 7},
            {3, 3, 3}, {4, 3, 4}, {5, 3, 5}, {6, 3, 6}, {7, 3, 7},
            {-8, 3, -6}, {-6, 4, 0}, {-7, 4, 0}, {-8, 4, 0}, {-9, 4, -3},
            {-10, 4, -2}, {-10, 4, 0}
        };
        setBlocksFromCoords(world, i, j, k, coords, FRAME, 1);
    }

    private void generate1(World world, int i, int j, int k) {
        int[][] platformsJ3 = {
            {-11, -3, -3, 3}, {-10, -5, -5, 5}, {-9, -6, -6, 6}, {-8, -7, -7, 7},
            {-7, -8, -8, 8}, {-6, -9, -9, 9}, {-5, -10, -10, 10}, {-4, -10, -10, 10},
            {-3, -11, -11, 11}, {-2, -11, -11, 11}, {-1, -11, -11, 11}, {0, -11, -11, 11},
            {1, -11, -11, 11}, {2, -11, -11, 11}, {3, -11, -11, 11}, {4, -10, -10, 10},
            {5, -10, -10, 10}, {6, -9, -9, 9}, {7, -8, -8, 8}, {8, -7, -7, 7},
            {9, -6, -6, 6}, {10, -5, -5, 5}, {11, -3, -3, 3}, {-9, -9, 4, 5},
            {-8, -8, 5, 7}, {-7, -7, 6, 8}, {-6, -6, 7, 9}, {-5, -5, 9, 10}, {-4, -4, 9, 10}
        };
        for (int[] r : platformsJ3) fillRect(world, i + r[0], i + r[1], j + 3, k + r[2], k + r[3], FRAME, 1);

        int[][] platformsJ4 = {
            {-11, -3, -11, 11}, {0, 11, -11, 11}, {-10, -8, 4, 5}, {-9, -7, 6, 8},
            {-6, -4, 7, 10}, {4, 6, 8, 10}, {7, 8, 4, 8}, {9, 11, -6, 6}
        };
        for (int[] r : platformsJ4) fillRect(world, i + r[0], i + r[1], j + 4, k + r[2], k + r[3], FRAME, 1);

        fillRect(world, i - 7, i - 2, j + 4, k - 7, k + 5, POOL, 0);
        setBlock(world, i - 4, j + 3, k + 7, FRAME, 1);
        setBlocksFromCoords(world, i, j, k, new int[][]{{-2, 5, -10}, {-3, 5, 0}, {-4, 5, 0}, {-5, 5, 0}, {-6, 5, 0}}, FRAME, 1);
    }

    private void generate2(World world, int i, int j, int k) {
        fillRect(world, i - 3, i + 8, j + 4, k - 8, k + 8, POOL, 0);
        int[][] portalCoords = {
            {0, -12}, {1, -12}, {-1, -12}, {2, -12}, {-2, -12}, {0, -13}, {1, -13}, {-1, -13},
            {2, -13}, {-2, -13}, {-3, -12}, {-4, -11}, {-5, -11}, {-6, -10}, {-7, -9}, {-8, -8},
            {-9, -7}, {-10, -6}, {-11, -5}, {-11, -4}, {-12, -3}, {-12, -2}, {-12, -1}, {-12, 0},
            {-12, 1}, {-12, 2}
        };
        setBlocksHeight(world, i, j, k, portalCoords, 4, 6, FRAME, 1);

        int[][] verticalCoords = {{0, -14}, {1, -14}, {-1, -14}, {2, -14}, {-2, -14}, {-3, -13}, {-7, -11}};
        setBlocksHeight(world, i, j, k, verticalCoords, 7, 13, FRAME, 1);

        int[][] sideCoords = {
            {-13, -3}, {-13, -2}, {-13, -1}, {-13, 0}, {-13, 1}, {-13, 2}, {-12, -4}, {-12, -5},
            {-11, -6}, {-10, -7}, {-9, -8}, {-8, -9}, {-7, -10}
        };
        setBlocksHeight(world, i, j, k, sideCoords, 7, 9, FRAME, 1);

        for (int x = -14; x <= -7; x++) {
            int z = x + 4;
            setBlock(world, i + x, j + 10, k + z, FRAME, 1);
            fillHeight(world, i + x, j + 11, j + 13, k + z, FRAME, 1);
        }
    }

    private void generate3(World world, int i, int j, int k) {
        int[] wallX = {-6, -5, -4, -3, 3, 4, 5, 6, 7};
        for (int x : wallX) fillHeight(world, i + x, j + 9, j + 13, k - 14 + (x + 6) / 2, FRAME, 1);

        for (int z = 3; z <= 6; z++) fillHeight(world, i - 14 + (z - 3), j + 9, j + 13, k + z, FRAME, 1);

        fillRectConditional(world, i - 11, i + 12, j + 4, k - 12, k + 13, FRAME, 1, (x, z) -> Math.abs(x) > 9 || Math.abs(z) > 9);
        fillRect(world, i - 11, i + 12, j + 5, k - 12, k + 13, FRAME, 1);
        fillRectConditional(world, i - 9, i + 12, j + 6, k - 12, k + 12, FRAME, 1, (x, z) -> Math.abs(x) >= 8 || Math.abs(z) >= 10);

        fillRect(world, i - 14, i + 14, j + 13, k - 14, k - 14, FRAME, 1);
        fillRect(world, i - 14, i + 14, j + 13, k + 14, k + 14, FRAME, 1);
        fillRect(world, i - 14, i - 14, j + 13, k - 14, k + 14, FRAME, 1);
        fillRect(world, i + 14, i + 14, j + 13, k - 14, k + 14, FRAME, 1);
    }

    private void generate4(World world, int i, int j, int k) {
        int[][] coords14 = {
            {13, 4}, {13, 5}, {13, 3}, {13, 2}, {13, 1}, {13, 0}, {13, -1}, {13, -2}, {13, -3},
            {12, 5}, {12, 4}, {12, 3}, {12, -3}, {12, -4}, {12, -5}, {11, -5}, {11, -6},
            {10, -6}, {10, -7}, {9, -7}, {9, -8}, {8, -8}, {8, -9}, {7, -9}, {7, -10},
            {6, -10}, {6, -11}, {5, -11}, {5, -12}, {4, -12}, {3, -12}, {3, -13},
            {2, -13}, {1, -13}, {0, -13}, {-1, -13}, {-2, -13}, {-3, -13}, {-3, -12},
            {-4, -12}, {-5, -12}, {-5, -11}, {-6, -11}, {-6, -10}, {-7, -10}, {-7, -9}, {-8, -9}
        };
        setBlocksHeight(world, i, j, k, coords14, 14, 15, FRAME, 1);

        int[][] steppedCoords = {
            {11, 6, 6, 9}, {11, 7, 7, 11}, {12, 6, 6, 13}, {10, 7, 7, 13}, {9, 8, 8, 13},
            {8, 9, 9, 13}, {7, 10, 10, 13}, {6, 11, 11, 13}, {4, 12, 12, 13}, {5, 12, 13, 13},
            {3, 13, 13, 14}, {-4, 12, 12, 13}, {-5, 12, 13, 13}, {-6, 11, 11, 13},
            {-7, 10, 10, 13}, {-8, 9, 9, 13}, {-9, 8, 8, 13}, {-10, 7, 7, 13}, {-11, 7, 7, 13}
        };
        for (int[] c : steppedCoords) fillHeight(world, i + c[0], j + c[2], j + c[3], k + c[1], FRAME, 1);

        int[][] clearCoords = {
            {11, 6, 6}, {11, 8, 7}, {12, 9, 6}, {10, 6, 7}, {10, 7, 7}, {11, 9, 7}, {9, 6, 8},
            {10, 9, 8}, {8, 6, 9}, {9, 9, 9}, {8, 8, 10}, {8, 9, 10}, {6, 6, 11}, {6, 7, 12},
            {7, 9, 11}, {4, 6, 12}, {4, 10, 12}, {6, 9, 12}, {5, 9, 13}, {5, 11, 12}, {3, 6, 13},
            {3, 9, 14}, {1, 10, 13}, {-2, 11, 15}, {-3, 7, 12}, {-4, 6, 12}, {-4, 9, 13},
            {-6, 9, 12}, {-7, 7, 10}, {-7, 9, 11}, {-6, 11, 11}, {-6, 12, 11}, {-8, 5, 9},
            {-8, 6, 9}, {-9, 6, 8}, {-10, 6, 7}, {-11, 9, 7}, {-11, 10, 6}, {0, 13, 13},
            {3, 14, 12}, {5, 14, 11}, {12, 14, -3}, {12, 14, -5}, {7, 14, -9}, {8, 14, -8}
        };
        for (int[] c : clearCoords) clearBlock(world, i + c[0], j + c[1], k + c[2]);
    }

    private void generate5(World world, int i, int j, int k) {
        int[][] layer14Coords = {
            {-7, -9}, {-8, -8}, {-9, -7}, {-10, -6}, {-11, -5}, {-12, -4}, {-12, -3}, {-13, -3},
            {-13, -2}, {-13, -1}, {-13, 0}, {-13, 1}, {-13, 2}, {-13, 3}, {-12, 4}, {-12, 5},
            {-11, 6}, {-10, 7}, {-9, 8}, {-7, 9}, {-6, 10}, {-5, 11}, {-4, 12}, {3, 12}
        };
        setBlocksHeight(world, i, j, k, layer14Coords, 14, 15, FRAME, 1);

        fillRect(world, i - 12, i + 12, j + 16, k - 3, k + 3, FRAME, 1);
        fillRect(world, i - 11, i + 11, j + 16, k - 5, k + 5, FRAME, 1);
        fillRect(world, i - 9, i + 10, j + 16, k - 6, k + 6, FRAME, 1);
        fillRect(world, i - 7, i + 9, j + 16, k - 7, k + 7, FRAME, 1);
        fillRect(world, i - 6, i + 8, j + 16, k - 8, k + 8, FRAME, 1);
        fillRect(world, i - 5, i + 7, j + 16, k - 9, k + 9, FRAME, 1);
        fillRect(world, i - 4, i + 6, j + 16, k - 10, k + 10, FRAME, 1);
        fillRect(world, i - 3, i + 5, j + 16, k - 11, k + 11, FRAME, 1);
        fillRect(world, i - 2, i + 4, j + 16, k - 12, k + 12, FRAME, 1);

        int[][] layer16Extras = {
            {-5, 9}, {-4, 9}, {-3, 9}, {-2, 9}, {-1, 9}, {0, 9}, {1, 9}, {2, 9}, {3, 9}, {4, 9}, {5, 9},
            {-6, 7}, {-5, 7}, {-4, 7}, {-3, 7}, {-2, 7}, {-1, 7}, {0, 7}, {1, 7}, {2, 7}, {3, 7}, {4, 7}, {5, 7}, {6, 7}
        };
        setBlocksFromCoords(world, i, j, k, layer16Extras, FRAME, 1);

        int[][] additionalCoords = {
            {-3, 5, 11}, {-2, 5, 11}, {3, 5, 11}, {3, 7, 12}, {3, 8, 12}, {1, 7, 12}, {1, 10, 13},
            {3, 13, 13}, {3, 12, 13}, {5, 13, 12}, {5, 12, 12}, {3, 15, 12}, {-1, 13, 13}, {-1, 12, 13},
            {-1, 11, 13}, {0, 13, 13}, {0, 12, 13}, {-2, 13, 13}, {-3, 15, 12}, {-5, 13, 12},
            {-5, 12, 12}, {-5, 10, 12}, {-3, 7, 12}, {-3, 8, 12}, {-2, 7, 12}, {5, 5, 10}, {8, 4, 9},
            {8, 5, 9}, {8, 4, 8}, {9, 4, 8}, {9, 5, 8}, {9, 6, 8}, {8, 6, 9}, {8, 13, 9}, {8, 12, 9},
            {9, 13, 8}, {8, 15, 8}, {7, 15, 9}, {-6, 15, 10}, {-5, 15, 10}, {-5, 15, 11}, {-1, 15, 9},
            {-1, 14, 9}, {-1, 13, 9}, {-1, 12, 9}, {0, 15, 8}, {0, 14, 8}, {0, 13, 8}, {-1, 15, 8},
            {-1, 14, 8}, {-2, 15, 9}, {-2, 14, 9}, {-1, 15, 10}, {0, 15, 9}, {0, 14, 9}, {-1, 15, 11},
            {0, 15, 10}, {-1, 14, 10}, {-6, 5, 9}, {-5, 5, 10}, {-6, 7, 10}, {-7, 10, 10}, {-7, 12, 10},
            {-7, 11, 10}, {-6, 13, 11}, {-8, 13, 9}, {-8, 10, 9}, {-8, 7, 8}, {-8, 5, 7}
        };
        setBlocksFromCoords(world, i, j, k, additionalCoords, FRAME, 1);

        int[][] clearCoords = {
            {-7, 14, -9}, {-8, 14, -8}, {-9, 14, -7}, {-10, 14, -6}, {-11, 14, -5}, {-12, 14, -3},
            {-12, 15, 3}, {-11, 14, 5}, {-10, 14, 6}, {-9, 14, 7}, {-8, 14, 8}, {-7, 14, 9}, {-6, 14, 10},
            {-5, 14, 11}, {3, 15, 12}, {3, 16, 13}, {-2, 15, 12}, {3, 15, -12}, {12, 15, -3}, {13, 16, 1},
            {0, 15, 12}, {7, 5, 8}, {8, 5, 8}, {8, 4, 9}, {8, 6, 8}, {9, 4, 8}, {8, 6, 9},
            {8, 5, 9}, {9, 5, 8}, {-6, 11, 11}, {-1, 17, 5}
        };
        for (int[] coord : clearCoords) clearBlock(world, i + coord[0], j + coord[1], k + coord[2]);

        setBlock(world, i - 1, j + 17, k + 5, FRAME, 1);
    }

    private void generate6(World world, int i, int j, int k) {
        fillRect(world, i - 10, i + 11, j + 16, k - 5, k + 3, FRAME, 1);
        fillRect(world, i - 9, i + 10, j + 16, k - 7, k + 5, FRAME, 1);
        fillRect(world, i - 8, i + 9, j + 16, k - 8, k + 8, FRAME, 1);
        fillRect(world, i - 6, i + 5, j + 16, k - 10, k - 7, FRAME, 1);
        fillRect(world, i + 6, i + 11, j + 16, k - 1, k + 1, FRAME, 1);

        int[][] portalFrameCoords = {
            {8, 6, 8}, {10, 13, 7}, {11, 13, 6}, {12, 11, 5}, {12, 12, 5}, {12, 13, 5}, {13, 11, 3},
            {13, 12, 3}, {13, 13, 3}, {13, 13, 2}, {13, 13, -1}, {13, 12, -1}, {13, 11, -1}, {13, 13, 0},
            {13, 9, -1}, {12, 7, -3}, {12, 8, -3}, {11, 5, -3}, {11, 5, 3}, {11, 5, 2}, {12, 7, 3},
            {12, 8, 3}, {12, 7, -2}, {12, 15, -3}, {12, 15, -2}, {12, 15, 3}, {12, 15, 2}, {12, 15, 1},
            {12, 14, 3}, {12, 13, 4}, {11, 15, 5}, {11, 14, 5}, {10, 15, 6}, {12, 13, -5}, {12, 10, -5},
            {11, 7, -5}, {10, 5, -5}, {13, 13, -3}, {3, 13, -13}, {3, 12, -13}, {2, 13, -13}, {3, 11, -13},
            {5, 13, -12}, {5, 12, -12}, {3, 15, -12}, {3, 7, -12}, {3, 8, -12}, {3, 5, -11}, {-2, 5, -11},
            {0, 5, -11}, {1, 5, -11}, {-1, 5, -11}, {0, 6, -11}, {-1, 6, -11}, {0, 7, -12}, {-3, 7, -12},
            {-3, 10, -13}, {-3, 11, -13}, {-1, 10, -13}, {0, 10, -13}, {2, 10, -13}, {2, 11, -13},
            {1, 10, -13}, {3, 10, -13}, {8, 10, -9}, {8, 11, -9}, {7, 13, -10}, {7, 12, -10}, {7, 7, -9},
            {5, 5, -10}, {6, 5, -9}, {9, 7, -7}, {8, 5, 7}, {-1, 11, 9}, {-6, 5, -9}, {-6, 7, -10},
            {-6, 8, -10}, {-5, 5, -10}, {-6, 10, -11}, {-6, 13, -11}, {-5, 15, -11}, {-9, 13, -8},
            {-9, 15, -7}, {-6, 15, -10}, {-5, 15, -10}, {-10, 7, -6}, {-10, 5, -5}, {-10, 5, -4},
            {-10, 6, -5}, {-11, 5, -3}, {-12, 7, 1}, {-12, 7, 0}, {-12, 7, -1}, {-12, 8, 0}, {-12, 8, -1},
            {-12, 8, -2}, {-12, 7, -3}, {-12, 7, -2}, {-11, 5, 0}, {-11, 6, 0}, {-11, 5, -1}, {-11, 5, 1},
            {-11, 5, 2}, {-11, 7, -1}, {-11, 6, -1}, {-11, 5, -2}, {2, 15, -12}, {-3, 15, -12}, {-12, 13, -5},
            {-12, 12, -5}, {-12, 13, -4}, {-13, 13, -3}, {-13, 13, -2}, {-13, 12, -3}, {-13, 13, 2},
            {-13, 13, 3}, {-13, 12, 3}, {-13, 11, 3}, {-12, 13, 5}, {-12, 12, 5}, {-12, 11, 5}, {-12, 13, 4},
            {-12, 15, 3}, {-12, 15, 2}, {2, 7, -12}, {2, 8, -12}, {2, 9, -12}, {1, 9, -12}, {0, 8, -12},
            {8, 7, 8}, {9, 10, 8}, {5, 10, 12}, {6, 10, 11}, {6, 11, 11}, {7, 9, 10}, {9, 10, -8},
            {-9, 10, -8}, {-10, 10, -7}, {-10, 11, -7}, {-7, 7, -9}, {-8, 5, -7}, {-11, 13, 6}, {-12, 15, -3},
            {-11, 7, 5}, {-10, 10, 7}, {-13, 10, 0}, {-13, 10, -1}
        };
        setBlocksFromCoords(world, i, j, k, portalFrameCoords, FRAME, 1);

        int[][] shroomCoords = {
            {1, 5, 11}, {-1, 7, 12}, {-4, 10, 12}, {5, 7, 11}, {12, 10, 4}, {11, 8, -5}, {-6, 11, -11}, {8, 12, -9}
        };
        setBlocksFromCoords(world, i, j, k, shroomCoords, GLOWING, 0);

        int[][] clearCoords = {
            {9, 6, 8}, {13, 9, -1}, {6, 5, -9}, {-1, 12, 8}, {8, 17, -1}, {5, 16, 3}, {-5, 15, -9},
            {-4, 15, -10}, {0, 16, -6}, {3, 17, -5}, {7, 15, -8}, {7, 14, -8}, {-4, 14, -11}
        };
        for (int[] coord : clearCoords) clearBlock(world, i + coord[0], j + coord[1], k + coord[2]);
    }
	
    private void generate7(World world, int i, int j, int k) {
        fillRect(world, i - 13, i + 13, j + 16, k - 17, k - 13, FRAME, 1); 
        fillRect(world, i - 13, i + 13, j + 16, k + 13, k + 17, FRAME, 1); 
        fillRect(world, i - 9, i + 9, j + 16, k - 1, k + 3, FRAME, 1);     
        
        int[][] portalFrameCoords = {
            {-12, 15, 0}, {-11, 15, -3}, {-11, 15, -2}, {-11, 15, -1}, {-10, 15, -4}, {-10, 16, -1}, {-10, 16, 0},
            {-10, 16, 1}, {-10, 16, 3}, {-10, 16, 2}, {-11, 15, 5}, {-10, 15, 6}, {-9, 15, 7}, {-8, 15, 8},
            {-8, 15, 7}, {-9, 15, 6}, {-10, 15, 5}, {-11, 15, 4}, {-6, 15, 9}, {8, 15, 7}, {7, 15, 8}, {7, 15, 7},
            {9, 15, 7}, {9, 15, 6}, {10, 15, 5}, {8, 15, 6}, {6, 15, 7}, {6, 15, 8}, {7, 15, 6}, {6, 15, 9},
            {6, 15, 10}, {7, 14, 9}, {8, 14, 8}, {9, 14, 7}, {7, 14, 8}, {5, 15, 11}, {5, 15, 10}, {5, 15, 9},
            {4, 15, 11}, {0, 15, -4}, {-1, 15, -4}, {-2, 15, -4}, {-2, 15, -5}, {-3, 15, -6}, {-2, 15, -7},
            {-1, 15, -7}, {0, 15, -8}, {1, 15, -7}, {1, 15, -6}, {0, 15, -5}, {-1, 15, -5}, {-1, 14, -5},
            {0, 15, -6}, {0, 15, -7}, {-1, 15, -6}, {-2, 15, -6}, {-1, 14, -6}, {0, 14, -7}, {0, 14, -6},
            {0, 13, -6}, {0, 12, -6}, {-1, 13, -6}, {1, 14, -6}, {1, 15, -5}, {0, 15, -9}, {0, 13, -7},
            {0, 14, -8}, {0, 11, -6}, {6, 15, -3}, {6, 14, -3}, {6, 13, -3}, {6, 15, -2}, {6, 15, -4},
            {6, 14, -4}, {5, 15, -4}, {7, 15, -3}, {-9, 16, 3}, {-8, 16, 3}, {-7, 16, 3}, {-6, 16, 3},
            {5, 16, 2}, {4, 16, 2}, {3, 16, 2}, {2, 16, 2}, {1, 16, 2}, {0, 16, 2}, {-1, 16, 2}, {-2, 16, 2},
            {-3, 16, 2}, {-4, 16, 2}, {-5, 16, 2}, {-6, 16, 2}, {-7, 16, 2}, {-8, 16, 2}, {-9, 16, 2},
            {-9, 16, 1}, {-8, 16, 1}, {-7, 16, 1}, {-6, 16, 1}, {-5, 16, 1}, {-5, 15, 1}, {-4, 16, 1},
            {-3, 16, 1}, {-2, 16, 1}, {-1, 15, 2}, {0, 15, 2}, {-1, 16, 1}, {0, 16, 1}, {1, 16, 1}, {2, 16, 1},
            {4, 15, 1}, {3, 16, 1}, {3, 16, 0}, {3, 16, -1}, {2, 16, 0}, {1, 16, 0}, {-1, 15, 1}, {-2, 15, 1},
            {0, 16, 0}, {-1, 16, 0}, {-2, 16, 0}, {-3, 16, 0}, {-4, 16, 0}, {-7, 16, 0}, {-6, 16, 0}, {-6, 15, 0},
            {-5, 16, 0}, {-8, 16, 0}, {-8, 15, 4}, {-1, 15, 5}, {3, 15, -4}, {-6, 15, -5}, {-6, 14, -5},
            {-6, 15, -3}, {5, 16, 3}, {4, 16, 3}, {3, 16, 3}, {2, 16, 3}, {1, 16, 3}, {0, 16, 3}, {-1, 16, 3},
            {-2, 16, 3}, {-3, 16, 3}, {-4, 16, 3}, {-5, 16, 3}, {2, 16, -1}, {1, 16, -1}, {0, 16, -1},
            {-1, 16, -1}, {-2, 16, -1}, {-3, 16, -1}, {-4, 16, -1}, {-5, 16, -1}, {-6, 16, -1}, {-6, 15, -1},
            {-7, 16, -1}, {-9, 16, -1}, {-9, 15, -1}, {-8, 16, -1}, {-9, 16, 0}, {7, 15, 2}, {8, 15, 2},
            {7, 14, 2}, {13, 10, 1}, {-1, 14, 1}, {5, 15, 6}, {-11, 5, 3}, {-12, 8, 4}, {-12, 9, 4}, {-10, 7, 6},
            {-10, 5, 5}, {-10, 6, 5}, {-10, 7, 5}, {-9, 7, 7}, {-9, 8, 7}, {-9, 9, 7}, {-10, 8, 6}, {-9, 9, 6},
            {-8, 10, 8}, {-8, 10, 7}, {-7, 11, 9}, {-7, 11, 8}, {-10, 9, 6}, {-11, 8, 5}, {-10, 8, 5}, {-11, 7, 4},
            {-11, 6, 3}, {-9, 10, 8}, {-9, 10, 7}, {-8, 11, 9}, {-8, 11, 8}, {-9, 11, 8}, {-6, 12, 11}, {-6, 12, 10},
            {-8, 12, 9}, {-6, 11, 10}, {-7, 12, 9}, {-6, 12, 9}, {-8, 9, 8}, {-7, 10, 9}, {-9, 8, 6}, {-9, 7, 6},
            {-10, 5, 5}, {-5, 13, 11}, {-5, 13, 10}, {-5, 13, 9}, {-8, 14, 10}, {-7, 14, 11}, {-9, 14, 9},
            {-10, 14, 8}, {-11, 14, 7}, {-9, 15, 9}, {-8, 15, 10}, {-8, 16, 9}, {-9, 16, 8}, {-10, 15, 8},
            {-10, 7, 4}, {-10, 6, 3}, {-10, 5, 3}, {-10, 5, 2}, {-6, 16, 3}, {-4, 14, 11}, {-5, 12, 11},
            {-4, 13, 12}, {-4, 13, 11}, {-4, 13, 10}, {-4, 13, 9}, {-3, 14, 12}, {-3, 14, 11}, {-3, 14, 10},
            {-3, 14, 9}, {-2, 14, 10}, {-2, 14, 11}, {-1, 14, 12}, {-2, 14, 12}, {-1, 15, 11}, {-7, 15, 11},
            {-6, 15, 11}, {-6, 16, 11}, {-5, 16, 12}, {-4, 16, 12}, {-3, 16, 13}, {-2, 16, 13}, {-3, 16, 12},
            {-1, 16, 13}, {-5, 16, 8}, {-4, 16, 13}, {-4, 14, 13}, {-4, 15, 13}, {-7, 16, 10}, {-2, 15, 8},
            {-3, 15, 8}, {0, 15, 7}, {0, 16, 13}, {1, 16, 13}, {2, 16, 13}, {3, 16, 13}, {4, 16, 12}, {5, 16, 12},
            {6, 16, 11}, {7, 16, 10}, {8, 16, 9}, {9, 16, 8}, {10, 16, 7}, {11, 16, 6}, {12, 16, 5}, {12, 16, 4},
            {13, 16, 3}, {13, 16, 2}, {13, 16, 1}, {13, 16, 0}, {13, 16, -1}, {13, 16, -2}, {13, 16, -3},
            {12, 16, -4}, {12, 16, -5}, {11, 16, -6}, {10, 16, -7}, {9, 16, -8}, {8, 16, -9}, {7, 16, -10},
            {6, 16, -11}, {5, 16, -12}, {4, 16, -12}, {3, 16, -13}, {2, 16, -13}, {1, 16, -13}, {0, 16, -13},
            {-1, 16, -13}, {-2, 16, -13}, {-3, 16, -13}, {-4, 16, -12}, {-5, 16, -12}, {-6, 16, -11}, {-7, 16, -10},
            {-8, 16, -9}, {-9, 16, -8}, {-10, 16, -7}, {-11, 16, -6}, {-12, 16, -5}, {-12, 16, -4}, {-13, 16, -3},
            {-13, 16, -2}, {-13, 16, -1}, {-13, 16, 1}, {-13, 16, 0}, {-13, 16, 2}, {-13, 16, 3}, {-12, 16, 4},
            {-12, 16, 5}, {-11, 16, 6}, {-10, 16, 7}, {-10, 16, 8}, {-9, 16, 9}, {-8, 16, 10}, {-7, 16, 11},
            {3, 16, -14}, {3, 16, -15}, {3, 16, -16}, {3, 16, -17}, {2, 16, -14}, {2, 16, -15}, {2, 16, -16},
            {2, 16, -17}
        };
        setBlocksFromCoords(world, i, j, k, portalFrameCoords, FRAME, 1);
        
        int[][] shroomCoords = {
            {-10, 7, 3}, {-10, 9, 5}, {-8, 11, 7}, {-6, 13, 9}, {-5, 14, 11}
        };
        setBlocksFromCoords(world, i, j, k, shroomCoords, GLOWING, 0);
        
        int[][] clearCoords = {
            {-8, 17, 2}, {-7, 15, 2}, {-2, 15, 1}, {-1, 15, 2}, {-6, 15, 0}, {-6, 15, -1}, {-9, 15, -1},
            {-6, 16, 3}, {-12, 8, 4}, {-12, 9, 4}, {-10, 5, 5}, {-10, 6, 5}, {-8, 13, 9}, {-7, 14, 10},
            {-8, 14, 9}, {-9, 14, 8}, {-10, 14, 7}, {-6, 15, 9}, {-7, 15, 9}, {-8, 15, 8}, {-8, 15, 7},
            {-8, 15, 9}, {-9, 15, 8}, {-9, 15, 7}, {-9, 15, 6}, {-13, 10, 3}, {8, 12, -9}, {12, 10, 4},
            {1, 5, 11}, {-1, 7, 12}, {-4, 14, 11}, {-6, 15, 10}, {-5, 15, 10}, {-5, 15, 11}, {-4, 16, 10},
            {-3, 16, 10}, {-3, 16, 11}, {-4, 16, 11}, {-2, 16, 11}, {-3, 16, 12}, {-2, 16, 12}, {-2, 16, 10},
            {-5, 16, 10}, {-5, 16, 11}, {-6, 15, 11}, {-4, 16, 9}, {-3, 16, 9}, {-1, 15, 11}, {-1, 16, 11},
            {-1, 16, 10}, {-1, 16, 12}, {-2, 16, 9}, {-5, 16, 8}, {-5, 16, 9}, {-3, 15, 12}, {-6, 16, 9},
            {-6, 16, 10}, {-6, 16, 8}, {-4, 16, 8}, {6, 16, 12}, {-2, 17, -15}, {1, 17, -15}
        };
        for (int[] coord : clearCoords) {
            clearBlock(world, i + coord[0], j + coord[1], k + coord[2]);
     }
  }
	
    private void generate8(World world, int i, int j, int k) {
        fillRect(world, i - 20, i - 19, j + 15, k - 5, k + 6, FRAME, 1);
        fillRect(world, i - 18, i - 17, j + 15, k + 3, k + 9, FRAME, 1);
        fillRect(world, i - 16, i - 15, j + 15, k + 6, k + 11, FRAME, 1);
        fillRect(world, i - 14, i - 11, j + 15, k + 8, k + 15, FRAME, 1);
        fillRect(world, i - 10, i - 7, j + 15, k + 13, k + 17, FRAME, 1);
        fillRect(world, i - 6, i - 1, j + 15, k - 18, k - 18, FRAME, 1);
        fillRect(world, i - 13, i - 10, j + 15, k - 17, k - 12, FRAME, 1);
        fillRect(world, i + 10, i + 16, j + 15, k + 4, k + 12, FRAME, 1);
        fillRect(world, i + 0, i + 3, j + 15, k + 18, k + 18, FRAME, 1);
        
        int[][] additionalCoordsJ15 = {
            {-18, -7}, {-16, -9}, {-15, -11}, {-14, -12}, {-12, -15}, {-9, -17}, {-8, -17},
            {-7, -17}, {-5, -18}, {-4, -18}, {-3, -18}, {-2, -18}
        };
        for (int[] coord : additionalCoordsJ15) {
            setBlock(world, i + coord[0], j + 15, k + coord[1], FRAME, 1);
        }
        
        int[][] clearCoords = {
            {-14, 15, -13}, {-13, 15, -14}, {-12, 15, -16}, {-11, 15, 15}, {11, 15, -12}
        };
        for (int[] coord : clearCoords) {
            clearBlock(world, i + coord[0], j + coord[1], k + coord[2]);
        }
    }
	
    private void generateLayer9(World world, int i, int j, int k) {   
        fillRect(world, i - 20, i - 19, j + 16, k - 5, k + 6, FRAME, 1);
        fillRect(world, i - 18, i - 17, j + 16, k + 4, k + 9, FRAME, 1);
        fillRect(world, i - 16, i - 15, j + 16, k + 7, k + 11, FRAME, 1);
        fillRect(world, i - 14, i - 11, j + 16, k + 9, k + 15, FRAME, 1);
        fillRect(world, i - 10, i - 6, j + 16, k + 14, k + 17, FRAME, 1);
        fillRect(world, i - 5, i - 1, j + 16, k - 18, k - 18, FRAME, 1);
        fillRect(world, i - 13, i - 9, j + 16, k - 17, k - 12, FRAME, 1);
        fillRect(world, i + 11, i + 16, j + 16, k + 5, k + 11, FRAME, 1);
        fillRect(world, i + 0, i + 2, j + 16, k + 18, k + 18, FRAME, 1);
        
        int[][] additionalCoordsJ16 = {
            {-18, -7}, {-16, -9}, {-15, -11}, {-15, -10}, {-14, -13}, {-14, -12}, {-14, -11},
            {-12, -16}, {-12, -15}, {-10, -15}, {-9, -17}, {-8, -17}, {-7, -17}, {-6, -17},
            {-4, -18}, {-3, -18}, {-2, -18}, {-1, -18}
        };
        for (int[] coord : additionalCoordsJ16) {
            setBlock(world, i + coord[0], j + 16, k + coord[1], FRAME, 1);
        }
        
        fillRect(world, i - 20, i - 19, j + 17, k - 5, k + 6, FRAME, 1);
        fillRect(world, i - 18, i - 17, j + 17, k + 7, k + 9, FRAME, 1);
        fillRect(world, i - 16, i - 15, j + 17, k + 10, k + 11, FRAME, 1);
        fillRect(world, i - 14, i - 11, j + 17, k + 12, k + 17, FRAME, 1);
        fillRect(world, i - 10, i - 5, j + 17, k + 17, k + 17, FRAME, 1);
        fillRect(world, i - 4, i + 4, j + 17, k - 18, k - 18, FRAME, 1);
        fillRect(world, i + 5, i + 7, j + 17, k - 16, k - 15, FRAME, 1);
        fillRect(world, i + 8, i + 11, j + 17, k - 14, k - 12, FRAME, 1);
        fillRect(world, i + 12, i + 17, j + 17, k - 11, k - 5, FRAME, 1);
        fillRect(world, i + 9, i + 13, j + 17, k + 9, k + 15, FRAME, 1);
        fillRect(world, i + 14, i + 16, j + 17, k + 4, k + 8, FRAME, 1);
      
        int[][] additionalCoordsJ17 = {
            {-18, -6}, {-17, -8}, {-15, -12}, {-14, -13}, {-13, -14}, {-12, -15}, {-11, -15},
            {-10, -15}, {-9, -17}, {-8, -17}, {-7, -17}, {-6, -17}, {-5, -18}, {-4, -17},
            {-1, -1}, {4, 17}, {8, 14}, {13, -11}
        };
        for (int[] coord : additionalCoordsJ17) {
            setBlock(world, i + coord[0], j + 17, k + coord[1], FRAME, 1);
        }
        
        fillRect(world, i - 20, i - 19, j + 18, k - 5, k + 5, FRAME, 1);
        fillRect(world, i - 18, i - 15, j + 18, k + 7, k + 11, FRAME, 1);
        fillRect(world, i - 14, i - 11, j + 18, k + 13, k + 17, FRAME, 1);
        fillRect(world, i - 10, i - 1, j + 18, k - 18, k - 18, FRAME, 1);
        fillRect(world, i + 5, i + 6, j + 18, k - 16, k - 16, FRAME, 1);
        fillRect(world, i + 9, i + 11, j + 18, k - 14, k - 12, FRAME, 1);
        fillRect(world, i + 12, i + 17, j + 18, k - 11, k - 2, FRAME, 1);
        fillRect(world, i + 13, i + 16, j + 18, k + 8, k + 10, FRAME, 1);
        fillRect(world, i - 13, i - 12, j + 18, k + 14, k + 16, FRAME, 1);
       
        for (int y = j + 17; y <= j + 52; y++) {
            world.setBlock(i - 1, y, k - 1, FRAME, 1, 2);
        }
        
        int[][] additionalCoordsJ18Plus = {
            {-20, 19, 0}, {-20, 19, -2}, {-20, 20, -2}, {-19, 19, 9}, {-17, 19, 9}, {-16, 18, -9},
            {-15, 19, -10}, {-14, 19, -9}, {-13, 19, 14}, {-13, 20, 14}, {-13, 20, 15}, {-13, 21, 15},
            {-12, 19, 15}, {-12, 20, 16}, {-12, 21, 16}, {-11, 19, 17}, {-11, 20, 17}, {-11, 21, 17},
            {-10, 19, 17}, {-10, 18, 18}, {-5, 19, -18}, {-5, 19, 17}, {1, 19, 18}, {2, 19, 18},
            {2, 20, 18}, {3, 19, 17}, {4, 19, 17}, {4, 20, 17}, {5, 19, 16}, {11, 19, -13},
            {13, 19, 9}, {15, 19, 6}, {17, 19, 2}
        };
        setBlocksFromCoords(world, i, j, k, additionalCoordsJ18Plus, FRAME, 1);
        
        int[][] clearCoords = {
            {-18, 16, 7}, {-12, 17, 13}, {-12, 16, -16}, {-11, 16, 15}, {12, 17, -11}, {13, 18, 8},
            {16, 18, 0}, {-17, 18, -7}, {-14, 19, -10}, {-13, 18, -13}, {-13, 17, -13}, {3, 19, 17}
        };
        for (int[] coord : clearCoords) {
            clearBlock(world, i + coord[0], j + coord[1], k + coord[2]);
        }
    }

    private void generateLayer10(World world, int i, int j, int k) {
        fillRect(world, i - 19, i - 18, j + 17, k - 4, k + 5, FRAME, 1);
        fillRect(world, i - 17, i - 16, j + 17, k + 6, k + 8, FRAME, 1);
        fillRect(world, i - 15, i - 13, j + 17, k + 9, k + 12, FRAME, 1);
        fillRect(world, i - 12, i - 9, j + 17, k + 13, k + 16, FRAME, 1);
        fillRect(world, i - 8, i - 6, j + 17, k + 16, k + 17, FRAME, 1);
        fillRect(world, i - 5, i - 2, j + 17, k - 18, k - 17, FRAME, 1);
        fillRect(world, i - 12, i - 10, j + 17, k - 16, k - 14, FRAME, 1);
        fillRect(world, i + 6, i + 8, j + 17, k - 15, k - 13, FRAME, 1);
        fillRect(world, i + 9, i + 12, j + 17, k - 12, k - 10, FRAME, 1);
        fillRect(world, i + 13, i + 16, j + 17, k - 9, k - 6, FRAME, 1);
        fillRect(world, i + 10, i + 14, j + 17, k + 8, k + 11, FRAME, 1);
        fillRect(world, i + 0, i + 3, j + 17, k + 17, k + 18, FRAME, 1);
        
        int[][] additionalCoordsJ17 = {
            {-20, 2}, {-20, 1}, {-20, 0}, {-19, -5}, {-18, -6}, {-17, -7}, {-16, -8}, {-15, -9},
            {-14, -10}, {-13, -11}, {-11, -13}, {-9, -15}, {-7, -17}, {-6, -18}, {-4, -18},
            {-3, -18}, {-1, -18}, {4, -17}, {5, -16}, {9, 12}, {15, 7}
        };
        for (int[] coord : additionalCoordsJ17) {
            setBlock(world, i + coord[0], j + 17, k + coord[1], FRAME, 1);
        }
        
        fillRect(world, i - 19, i - 18, j + 18, k - 4, k + 5, FRAME, 1);
        fillRect(world, i - 17, i - 16, j + 18, k + 6, k + 8, FRAME, 1);
        fillRect(world, i - 15, i - 13, j + 18, k + 9, k + 12, FRAME, 1);
        fillRect(world, i - 12, i - 10, j + 17, j + 18, k + 16, FRAME, 1);
        fillRect(world, i - 9, i - 7, j + 18, k + 16, k + 17, FRAME, 1);
        fillRect(world, i - 6, i - 3, j + 18, k - 18, k - 17, FRAME, 1);
        fillRect(world, i - 11, i - 10, j + 18, k - 16, k - 15, FRAME, 1);
        fillRect(world, i + 6, i + 8, j + 18, k - 15, k - 13, FRAME, 1);
        fillRect(world, i + 9, i + 12, j + 18, k - 12, k - 10, FRAME, 1);
        fillRect(world, i + 13, i + 16, j + 18, k - 9, k - 6, FRAME, 1);
        fillRect(world, i + 10, i + 14, j + 18, k + 8, k + 11, FRAME, 1);
        fillRect(world, i + 0, i + 3, j + 18, k + 17, k + 18, FRAME, 1);
        
        int[][] additionalCoordsJ18Plus = {
            {-20, 18, 2}, {-20, 18, 1}, {-20, 18, 0}, {-19, 18, -5}, {-18, 18, -6}, {-17, 18, -7},
            {-16, 18, -8}, {-15, 18, -9}, {-14, 18, -10}, {-13, 18, -11}, {-12, 18, -12},
            {-11, 18, -13}, {-10, 18, -14}, {-9, 18, -15}, {-8, 18, -16}, {-7, 18, -17},
            {-6, 18, -18}, {-5, 18, -18}, {-4, 18, -18}, {-2, 18, -18}, {-1, 18, -18},
            {-1, 19, -18}, {-1, 20, -18}, {0, 19, -18}, {1, 19, -18}, {2, 19, -18}, {3, 19, -18},
            {4, 18, -17}, {4, 19, -17}, {5, 18, -16}, {5, 19, -16}, {6, 18, -15}, {7, 18, -14},
            {8, 18, -13}, {9, 18, 12}, {10, 18, 12}, {11, 18, 12}, {12, 18, 12}, {13, 18, 7},
            {14, 18, 7}, {15, 18, 7}
        };
        setBlocksFromCoords(world, i, j, k, additionalCoordsJ18Plus, FRAME, 1);
        
        int[][] clearCoords = {
            {-12, 18, -15}, {-10, 18, -16}, {-8, 18, -17}, {-6, 18, -18}, {4, 18, -18}
        };
        for (int[] coord : clearCoords) {
            clearBlock(world, i + coord[0], j + coord[1], k + coord[2]);
        }
    }
	
    private void generateLayer11(World world, int i, int j, int k) { 
        setVerticalLine(world, i - 1, j + 18, j + 20, k - 19, FRAME); 
        setVerticalLine(world, i - 2, j + 17, j + 19, k - 19, FRAME);
        setVerticalLine(world, i - 3, j + 18, j + 21, k - 19, FRAME); 
        setVerticalLine(world, i - 4, j + 18, j + 20, k - 19, FRAME); 
        setVerticalLine(world, i - 5, j + 19, j + 21, k - 19, FRAME); 
        setVerticalLine(world, i - 6, j + 19, j + 22, k - 18, FRAME); 
        setVerticalLine(world, i - 7, j + 18, j + 21, k - 18, FRAME); 
        setVerticalLine(world, i - 8, j + 18, j + 22, k - 17, FRAME); 
        setVerticalLine(world, i - 9, j + 19, j + 21, k - 16, FRAME); 
        setVerticalLine(world, i - 10, j + 17, j + 22, k - 16, FRAME); 
        setVerticalLine(world, i - 11, j + 19, j + 21, k - 16, FRAME); 
        
        setVerticalLine(world, i - 12, j + 18, j + 22, k - 15, FRAME); 
        setVerticalLine(world, i - 13, j + 19, j + 21, k - 14, FRAME); 
        setVerticalLine(world, i - 14, j + 19, j + 20, k - 13, FRAME); 
        setVerticalLine(world, i - 15, j + 18, j + 21, k - 13, FRAME); 
        setVerticalLine(world, i - 16, j + 18, j + 21, k - 12, FRAME); 
        setHorizontalLineZ(world, i - 16, j + 18, k - 11, k - 10, FRAME); 
        setVerticalLine(world, i - 16, j + 19, j + 20, k - 11, FRAME); 
        setVerticalLine(world, i - 16, j + 19, j + 20, k - 10, FRAME); 
        setVerticalLine(world, i - 17, j + 18, j + 22, k - 9, FRAME); 
        
        setVerticalLine(world, i - 18, j + 18, j + 20, k - 8, FRAME); 
        setVerticalLine(world, i - 19, j + 18, j + 20, k - 7, FRAME); 
        setVerticalLine(world, i - 19, j + 18, j + 21, k - 6, FRAME); 
        setRectangleXY(world, i - 20, i - 20, j + 17, j + 21, k - 5, FRAME); 
        setRectangleXY(world, i - 20, i - 20, j + 17, j + 20, k - 4, FRAME); 
        
        setVerticalLine(world, i - 20, j + 18, j + 21, k - 3, FRAME); 
        setVerticalLine(world, i - 21, j + 20, j + 21, k - 2, FRAME); 
        setVerticalLine(world, i - 21, j + 18, j + 21, k - 1, FRAME); 
        setRectangleXY(world, i - 21, i - 21, j + 19, j + 22, k + 0, FRAME); 
        setVerticalLine(world, i - 20, j + 20, j + 20, k + 0, FRAME); 
        
        setRectangleXY(world, i - 22, i - 21, j + 17, j + 21, k + 1, FRAME); 
        setRectangleXY(world, i - 21, i - 21, j + 18, j + 22, k + 2, FRAME); 
        setVerticalLine(world, i - 21, j + 17, j + 20, k + 3, FRAME); 
        setVerticalLine(world, i - 21, j + 18, j + 21, k + 4, FRAME); 
        setVerticalLine(world, i - 20, j + 18, j + 21, k + 5, FRAME); 
        
        setVerticalLine(world, i - 20, j + 19, j + 21, k + 6, FRAME); 
        setVerticalLine(world, i - 19, j + 18, j + 20, k + 7, FRAME); 
        setVerticalLine(world, i - 19, j + 18, j + 21, k + 8, FRAME); 
        setVerticalLine(world, i - 18, j + 19, j + 23, k + 9, FRAME); 
        
        setVerticalLine(world, i - 17, j + 18, j + 21, k + 10, FRAME); 
        setVerticalLine(world, i - 17, j + 19, j + 22, k + 11, FRAME); 
        setVerticalLine(world, i - 16, j + 18, j + 20, k + 11, FRAME); 
        setVerticalLine(world, i - 15, j + 18, j + 21, k + 12, FRAME); 
        setVerticalLine(world, i - 15, j + 18, j + 21, k + 13, FRAME); 
        setVerticalLine(world, i - 14, j + 19, j + 21, k + 14, FRAME); 
        
        setVerticalLine(world, i + 3, j + 19, j + 23, k + 18, FRAME); 
        setVerticalLine(world, i - 4, j + 21, j + 27, k + 20, FRAME); 
        setVerticalLine(world, i - 3, j + 20, j + 29, k + 20, FRAME); 
        setVerticalLine(world, i - 2, j + 20, j + 20, k + 20, FRAME); 
        
        setVerticalLine(world, i + 3, j + 18, j + 18, k - 18, FRAME); 
        setVerticalLine(world, i + 4, j + 19, j + 19, k - 18, FRAME); 
        setVerticalLine(world, i + 4, j + 18, j + 18, k - 17, FRAME); 
        
        world.setBlock(i - 1, j + 18, k - 18, AIR, 0, 2); 
        world.setBlock(i - 6, j + 20, k - 19, AIR, 0, 2); 
        world.setBlock(i - 6, j + 22, k - 19, AIR, 0, 2); 
        world.setBlock(i - 8, j + 20, k - 18, AIR, 0, 2); 
        world.setBlock(i - 16, j + 21, k - 13, AIR, 0, 2); 
        world.setBlock(i - 20, j + 21, k - 6, AIR, 0, 2); 
        world.setBlock(i - 21, j + 21, k - 3, AIR, 0, 2); 
        world.setBlock(i - 20, j + 20, k + 0, AIR, 0, 2); 
        world.setBlock(i - 21, j + 21, k + 0, AIR, 0, 2); 
        world.setBlock(i - 22, j + 22, k + 2, AIR, 0, 2); 
        world.setBlock(i - 21, j + 21, k + 6, AIR, 0, 2); 
        world.setBlock(i - 20, j + 21, k + 7, AIR, 0, 2); 
        world.setBlock(i - 19, j + 21, k + 9, AIR, 0, 2); 
        world.setBlock(i - 16, j + 21, k + 12, AIR, 0, 2); 
        world.setBlock(i - 16, j + 20, k + 12, AIR, 0, 2); 
        world.setBlock(i - 17, j + 19, k + 11, AIR, 0, 2); 
        world.setBlock(i - 22, j + 20, k + 1, AIR, 0, 2); 
        world.setBlock(i - 22, j + 19, k + 1, AIR, 0, 2); 
    }

    private void generateLayer12(World world, int i, int j, int k) {
        setVerticalLine(world, i - 2, j + 22, j + 27, k + 20, FRAME); 
        setVerticalLine(world, i - 1, j + 21, j + 28, k + 20, FRAME); 
        setVerticalLine(world, i + 0, j + 19, j + 27, k + 20, FRAME); 
        setVerticalLine(world, i + 1, j + 20, j + 29, k + 20, FRAME); 
        setVerticalLine(world, i + 2, j + 22, j + 28, k + 20, FRAME); 
        
        setVerticalLine(world, i + 3, j + 22, j + 28, k + 19, FRAME); 
        setVerticalLine(world, i + 4, j + 21, j + 27, k + 18, FRAME); 
        setVerticalLine(world, i + 5, j + 20, j + 26, k + 17, FRAME); 
        setVerticalLine(world, i + 6, j + 22, j + 28, k + 17, FRAME); 
        
        setVerticalLine(world, i + 7, j + 22, j + 27, k + 16, FRAME); 
        setVerticalLine(world, i + 8, j + 19, j + 24, k + 15, FRAME); 
        setVerticalLine(world, i + 9, j + 20, j + 27, k + 15, FRAME); 
        setVerticalLine(world, i + 10, j + 21, j + 26, k + 13, FRAME); 
        
        setVerticalLine(world, i + 11, j + 21, j + 26, k + 12, FRAME); 
        setVerticalLine(world, i + 12, j + 21, j + 25, k + 11, FRAME); 
        setVerticalLine(world, i + 13, j + 22, j + 26, k + 10, FRAME); 
        setVerticalLine(world, i + 14, j + 22, j + 26, k + 9, FRAME); 
        setVerticalLine(world, i + 15, j + 22, j + 26, k + 8, FRAME); 
        setVerticalLine(world, i + 16, j + 21, j + 26, k + 7, FRAME); 
        
        setVerticalLine(world, i + 17, j + 21, j + 25, k + 6, FRAME); 
        setVerticalLine(world, i + 18, j + 22, j + 24, k + 5, FRAME); 
        setVerticalLine(world, i + 18, j + 20, j + 26, k + 4, FRAME); 
        setVerticalLine(world, i + 19, j + 21, j + 25, k + 3, FRAME); 
        setVerticalLine(world, i + 19, j + 22, j + 27, k + 2, FRAME); 
        setVerticalLine(world, i + 19, j + 21, j + 26, k + 1, FRAME); 
        
        setVerticalLine(world, i + 19, j + 20, j + 25, k + 0, FRAME); 
        setVerticalLine(world, i + 19, j + 20, j + 24, k - 1, FRAME); 
        setVerticalLine(world, i + 19, j + 22, j + 26, k - 2, FRAME); 
        setVerticalLine(world, i + 19, j + 20, j + 25, k - 3, FRAME); 
        setVerticalLine(world, i + 19, j + 19, j + 24, k - 4, FRAME); 
        setVerticalLine(world, i + 19, j + 21, j + 26, k - 5, FRAME); 
        
        setVerticalLine(world, i + 18, j + 21, j + 27, k - 6, FRAME); 
        setVerticalLine(world, i + 17, j + 21, j + 26, k - 7, FRAME); 
        setVerticalLine(world, i + 17, j + 22, j + 26, k - 8, FRAME); 
        setVerticalLine(world, i + 16, j + 22, j + 28, k - 9, FRAME); 
        
        setVerticalLine(world, i + 15, j + 21, j + 27, k - 10, FRAME); 
        setVerticalLine(world, i + 15, j + 23, j + 26, k - 11, FRAME); 
        setVerticalLine(world, i + 14, j + 21, j + 26, k - 12, FRAME); 
        setVerticalLine(world, i + 13, j + 20, j + 25, k - 13, FRAME); 
        setVerticalLine(world, i + 12, j + 22, j + 25, k - 13, FRAME); 
        
        setVerticalLine(world, i + 11, j + 22, j + 26, k - 14, FRAME); 
        setVerticalLine(world, i + 10, j + 20, j + 25, k - 15, FRAME); 
        setVerticalLine(world, i + 9, j + 21, j + 27, k - 16, FRAME); 
        setVerticalLine(world, i + 8, j + 21, j + 26, k - 16, FRAME); 
        setVerticalLine(world, i + 7, j + 20, j + 27, k - 17, FRAME); 
        
        setVerticalLine(world, i + 6, j + 21, j + 27, k - 18, FRAME); 
        setVerticalLine(world, i + 5, j + 20, j + 28, k - 19, FRAME); 
        setRectangleXY(world, i + 4, i + 2, j + 20, j + 27, k - 20, FRAME); 
        setVerticalLine(world, i + 1, j + 22, j + 29, k - 20, FRAME); 
        setVerticalLine(world, i + 0, j + 21, j + 29, k - 20, FRAME); 
        setVerticalLine(world, i - 1, j + 20, j + 28, k - 20, FRAME); 
        setVerticalLine(world, i - 2, j + 19, j + 27, k - 20, FRAME); 
        setVerticalLine(world, i - 3, j + 21, j + 29, k - 20, FRAME); 
        setVerticalLine(world, i - 4, j + 20, j + 28, k - 20, FRAME); 
        setVerticalLine(world, i - 5, j + 21, j + 24, k - 20, FRAME); 
        
        setVerticalLine(world, i + 4, j + 18, j + 18, k + 16, FRAME); 
        setVerticalLine(world, i + 6, j + 18, j + 18, k + 15, FRAME); 
        setVerticalLine(world, i + 12, j + 18, j + 18, k + 10, FRAME); 
        setVerticalLine(world, i + 13, j + 18, j + 18, k + 8, FRAME); 
        setVerticalLine(world, i + 14, j + 19, j + 20, k + 8, FRAME); 
        setVerticalLine(world, i + 13, j + 20, j + 20, k + 9, FRAME); 
        setVerticalLine(world, i + 14, j + 18, j + 18, k + 7, FRAME); 
        setVerticalLine(world, i + 4, j + 18, j + 18, k - 16, FRAME);
        
        world.setBlock(i - 2, j + 21, k + 19, AIR, 0, 2); 
        world.setBlock(i + 17, j + 21, k + 5, AIR, 0, 2); 
        world.setBlock(i + 18, j + 22, k + 1, AIR, 0, 2); 
        world.setBlock(i + 18, j + 22, k - 7, AIR, 0, 2); 
        world.setBlock(i + 2, j + 27, k - 21, AIR, 0, 2); 
        world.setBlock(i + 0, j + 25, k - 20, AIR, 0, 2); 
        world.setBlock(i + 0, j + 25, k - 21, AIR, 0, 2); 
    }
	
    private void generateLayer13(World world, int i, int j, int k) {
        setVerticalLine(world, i - 5, j + 25, j + 28, k - 20, FRAME); 
        setVerticalLine(world, i - 6, j + 22, j + 27, k - 19, FRAME); 
        setVerticalLine(world, i - 7, j + 21, j + 29, k - 19, FRAME); 
        setVerticalLine(world, i - 8, j + 21, j + 27, k - 18, FRAME); 
        setVerticalLine(world, i - 9, j + 21, j + 27, k - 17, FRAME); 
        
        setVerticalLine(world, i - 10, j + 23, j + 27, k - 16, FRAME); 
        setVerticalLine(world, i - 11, j + 22, j + 27, k - 16, FRAME); 
        setVerticalLine(world, i - 12, j + 23, j + 26, k - 15, FRAME); 
        setVerticalLine(world, i - 13, j + 22, j + 28, k - 15, FRAME); 
        setVerticalLine(world, i - 14, j + 20, j + 23, k - 14, FRAME); 
        setVerticalLine(world, i - 14, j + 25, j + 28, k - 14, FRAME); 
        setVerticalLine(world, i - 15, j + 22, j + 24, k - 13, FRAME); 
        setVerticalLine(world, i - 16, j + 22, j + 27, k - 13, FRAME); 
        
        setVerticalLine(world, i - 17, j + 21, j + 28, k - 12, FRAME); 
        setVerticalLine(world, i - 17, j + 21, j + 26, k - 11, FRAME); 
        setVerticalLine(world, i - 17, j + 20, j + 28, k - 10, FRAME); 
        setVerticalLine(world, i - 18, j + 21, j + 27, k - 9, FRAME); 
        
        setVerticalLine(world, i - 19, j + 20, j + 24, k - 8, FRAME); 
        setVerticalLine(world, i - 18, j + 21, j + 22, k - 8, FRAME); 
        setVerticalLine(world, i - 20, j + 20, j + 26, k - 7, FRAME); 
        setVerticalLine(world, i - 20, j + 20, j + 28, k - 6, FRAME); 
        setVerticalLine(world, i - 21, j + 20, j + 26, k - 5, FRAME); 
        
        setVerticalLine(world, i - 21, j + 20, j + 29, k - 4, FRAME); 
        setVerticalLine(world, i - 21, j + 20, j + 27, k - 3, FRAME); 
        setVerticalLine(world, i - 22, j + 20, j + 28, k - 2, FRAME); 
        setVerticalLine(world, i - 22, j + 21, j + 29, k - 1, FRAME); 
        setVerticalLine(world, i - 22, j + 21, j + 28, k + 0, FRAME); 
        
        setVerticalLine(world, i - 1, j + 17, j + 49, k - 2, FRAME); 
        setVerticalLine(world, i + 0, j + 17, j + 48, k - 1, FRAME); 
        setVerticalLine(world, i - 1, j + 17, j + 47, k + 0, FRAME); 
        setVerticalLine(world, i - 2, j + 17, j + 49, k - 1, FRAME); 
        setVerticalLine(world, i - 2, j + 17, j + 47, k - 2, FRAME); 
        setVerticalLine(world, i + 0, j + 17, j + 43, k - 2, FRAME); 
        setVerticalLine(world, i + 0, j + 17, j + 25, k + 0, FRAME); 
        
        setVerticalLine(world, i - 3, j + 22, j + 22, k - 19, FRAME); 
        setVerticalLine(world, i + 1, j + 23, j + 24, k - 19, FRAME); 
        setVerticalLine(world, i + 0, j + 22, j + 22, k - 19, FRAME); 
        setVerticalLine(world, i - 16, j + 22, j + 22, k - 12, FRAME); 
        
        world.setBlock(i - 15, j + 26, k - 14, AIR, 0, 2); 
        world.setBlock(i - 14, j + 24, k - 14, AIR, 0, 2); 
        world.setBlock(i - 15, j + 23, k - 14, AIR, 0, 2); 
        world.setBlock(i - 18, j + 26, k - 11, AIR, 0, 2); 
        world.setBlock(i - 21, j + 23, k - 4, AIR, 0, 2); 
        world.setBlock(i + 1, j + 44, k - 1, AIR, 0, 2); 
        world.setBlock(i + 0, j + 42, k + 0, AIR, 0, 2); 
        world.setBlock(i - 3, j + 34, k - 1, AIR, 0, 2); 
        world.setBlock(i - 3, j + 34, k - 2, AIR, 0, 2); 
        world.setBlock(i - 3, j + 35, k - 2, AIR, 0, 2); 
        world.setBlock(i + 0, j + 18, k + 0, AIR, 0, 2); 
    }

    private void generateLayer14(World world, int i, int j, int k) {
        setVerticalLine(world, i + 0, j + 26, j + 45, k + 0, FRAME); 
        setVerticalLine(world, i - 2, j + 17, j + 46, k + 0, FRAME); 
        setVerticalLine(world, i - 3, j + 17, j + 36, k + 0, FRAME); 
        setVerticalLine(world, i + 1, j + 17, j + 39, k + 0, FRAME); 
       
        setVerticalLine(world, i - 3, j + 17, j + 44, k - 1, FRAME); 
        world.setBlock(i - 3, j + 37, k - 1, FRAME, 1, 2); 
        setVerticalLine(world, i + 1, j + 17, j + 41, k - 1, FRAME); 
        setVerticalLine(world, i - 4, j + 17, j + 32, k - 1, FRAME); 
        
        setVerticalLine(world, i - 3, j + 17, j + 37, k - 2, FRAME); 
        setVerticalLine(world, i + 1, j + 17, j + 37, k - 2, FRAME); 
        
        setVerticalLine(world, i - 1, j + 17, j + 41, k - 3, FRAME); 
        setVerticalLine(world, i - 2, j + 17, j + 38, k - 3, FRAME); 
        setVerticalLine(world, i + 0, j + 17, j + 38, k - 3, FRAME); 
        
        setVerticalLine(world, i - 1, j + 17, j + 44, k + 1, FRAME); 
        setVerticalLine(world, i - 2, j + 17, j + 41, k + 1, FRAME); 
        setVerticalLine(world, i + 0, j + 17, j + 39, k + 1, FRAME); 
        
        setVerticalLine(world, i - 1, j + 17, j + 35, k - 4, FRAME); 
        
        setVerticalLine(world, i - 1, j + 17, j + 36, k + 2, FRAME); 
        world.setBlock(i - 1, j + 34, k + 3, FRAME, 1, 2); 
      
        world.setBlock(i + 0, j + 46, k + 0, AIR, 0, 2); 
        world.setBlock(i - 4, j + 40, k - 1, AIR, 0, 2); 
        world.setBlock(i - 4, j + 36, k - 1, AIR, 0, 2); 
        world.setBlock(i - 4, j + 34, k - 1, AIR, 0, 2); 
        world.setBlock(i - 4, j + 33, k - 1, AIR, 0, 2); 
        world.setBlock(i - 2, j + 41, k + 1, AIR, 0, 2); 
    }
	
    private void generateLayer15(World world, int i, int j, int k) {
        setVerticalLine(world, i - 1, j + 17, j + 26, k - 5, FRAME); 
        setVerticalLine(world, i - 2, j + 17, j + 22, k - 5, FRAME); 
        setVerticalLine(world, i + 0, j + 17, j + 24, k - 5, FRAME); 
        
        setVerticalLine(world, i + 0, j + 17, j + 32, k - 4, FRAME); 
        setVerticalLine(world, i + 1, j + 17, j + 28, k - 4, FRAME); 
        setVerticalLine(world, i - 2, j + 17, j + 29, k - 4, FRAME); 
        setVerticalLine(world, i - 3, j + 17, j + 25, k - 4, FRAME); 
       
        setVerticalLine(world, i + 1, j + 17, j + 34, k - 3, FRAME); 
        setVerticalLine(world, i + 2, j + 17, j + 29, k - 3, FRAME); 
        setVerticalLine(world, i - 3, j + 17, j + 30, k - 3, FRAME); 
        setVerticalLine(world, i - 4, j + 17, j + 24, k - 3, FRAME); 
        
        setVerticalLine(world, i + 2, j + 17, j + 33, k - 2, FRAME); 
        setVerticalLine(world, i + 3, j + 17, j + 26, k - 2, FRAME); 
        setVerticalLine(world, i - 4, j + 17, j + 29, k - 2, FRAME); 
        setVerticalLine(world, i - 5, j + 17, j + 25, k - 2, FRAME); 
        
        setVerticalLine(world, i + 2, j + 17, j + 35, k - 1, FRAME); 
        world.setBlock(i + 2, j + 22, k - 1, FRAME, 1, 2); 
        
        setVerticalLine(world, i + 3, j + 17, j + 31, k - 1, FRAME); 
        setVerticalLine(world, i - 5, j + 17, j + 25, k - 1, FRAME); 
        
        setVerticalLine(world, i + 2, j + 17, j + 33, k + 0, FRAME); 
        setVerticalLine(world, i + 3, j + 17, j + 26, k + 0, FRAME); 
        setVerticalLine(world, i - 4, j + 17, j + 25, k + 0, FRAME); 
        setVerticalLine(world, i - 5, j + 17, j + 23, k + 0, FRAME); 
        
        setVerticalLine(world, i + 1, j + 17, j + 31, k + 1, FRAME); 
        setVerticalLine(world, i + 2, j + 17, j + 29, k + 1, FRAME); 
        
        setVerticalLine(world, i - 2, j + 17, j + 32, k + 2, FRAME); 
        setVerticalLine(world, i + 0, j + 17, j + 31, k + 2, FRAME); 
        setVerticalLine(world, i + 1, j + 17, j + 26, k + 2, FRAME); 
        
        setVerticalLine(world, i - 1, j + 17, j + 29, k + 3, FRAME); 
        setVerticalLine(world, i + 0, j + 17, j + 24, k + 3, FRAME); 
        
        world.setBlock(i - 1, j + 35, k + 2, FRAME, 1, 2); 
        world.setBlock(i + 3, j + 17, k + 0, FRAME, 1, 2); 
        world.setBlock(i + 2, j + 17, k + 2, FRAME, 1, 2); 
        
        world.setBlock(i - 1, j + 34, k + 3, AIR, 0, 2); 
        world.setBlock(i + 2, j + 23, k - 2, AIR, 0, 2); 
        world.setBlock(i - 3, j + 28, k - 4, AIR, 0, 2); 
        world.setBlock(i - 3, j + 26, k - 4, AIR, 0, 2); 
        world.setBlock(i - 4, j + 25, k - 3, AIR, 0, 2); 
        world.setBlock(i - 1, j + 27, k - 5, AIR, 0, 2); 
        world.setBlock(i - 1, j + 28, k - 5, AIR, 0, 2); 
        world.setBlock(i - 1, j + 29, k - 5, AIR, 0, 2); 
        world.setBlock(i - 2, j + 32, k + 3, AIR, 0, 2); 
        world.setBlock(i - 4, j + 24, k + 0, AIR, 0, 2); 
        world.setBlock(i - 4, j + 23, k + 1, AIR, 0, 2); 
        world.setBlock(i - 5, j + 24, k + 0, AIR, 0, 2); 
        world.setBlock(i - 5, j + 26, k + 0, AIR, 0, 2); 
        world.setBlock(i - 5, j + 26, k - 1, AIR, 0, 2); 
    }
	
    private void generateLayer16(World world, int i, int j, int k) {
        world.setBlock(i - 2, j + 17, k - 17, FRAME, 1, 2); 
        setVerticalLine(world, i - 2, j + 17, j + 18, k - 16, FRAME); 
        world.setBlock(i - 2, j + 17, k - 15, GLOWING, 0, 2); 
        world.setBlock(i - 2, j + 17, k - 12, FRAME, 1, 2); 
        setVerticalLine(world, i - 8, j + 17, j + 19, k - 11, FRAME); 
        world.setBlock(i - 8, j + 17, k - 12, FRAME, 1, 2); 
        world.setBlock(i - 9, j + 17, k - 9, GLOWING, 0, 2); 
        setVerticalLine(world, i - 13, j + 17, j + 19, k - 10, FRAME); 
        world.setBlock(i - 13, j + 17, k - 9, FRAME, 1, 2); 
        
        world.setBlock(i + 10, j + 17, k - 7, GLOWING, 0, 2); 
        setVerticalLine(world, i - 1, j + 17, j + 20, k - 6, FRAME); 
        world.setBlock(i + 0, j + 17, k - 6, FRAME, 1, 2); 
        world.setBlock(i + 0, j + 19, k - 6, GLOWING, 0, 2); 
        world.setBlock(i - 2, j + 17, k - 6, FRAME, 1, 2); 
        setVerticalLine(world, i - 7, j + 17, j + 19, k - 5, FRAME); 
        world.setBlock(i - 8, j + 17, k - 5, FRAME, 1, 2); 
        setVerticalLine(world, i - 14, j + 17, j + 19, k - 5, FRAME); 
        world.setBlock(i - 15, j + 17, k - 5, FRAME, 1, 2); 
        world.setBlock(i - 14, j + 20, k - 5, GLOWING, 0, 2); 
        setVerticalLine(world, i + 9, j + 17, j + 20, k - 5, FRAME); 
        setVerticalLine(world, i + 10, j + 17, j + 20, k - 5, FRAME); 
        setVerticalLine(world, i + 11, j + 17, j + 18, k - 5, FRAME); 
        world.setBlock(i + 11, j + 18, k - 5, GLOWING, 0, 2); 
        setVerticalLine(world, i + 2, j + 17, j + 19, k - 5, FRAME); 
        setVerticalLine(world, i - 4, j + 17, j + 18, k - 5, FRAME); 
        setVerticalLine(world, i - 3, j + 17, j + 20, k - 5, FRAME); 
        world.setBlock(i + 1, j + 17, k - 5, FRAME, 1, 2); 
        world.setBlock(i + 1, j + 21, k - 5, FRAME, 1, 2); 
        setVerticalLine(world, i + 10, j + 17, j + 18, k - 4, FRAME); 
        setVerticalLine(world, i + 3, j + 17, j + 18, k - 4, FRAME); 
        setVerticalLine(world, i + 2, j + 17, j + 23, k - 4, FRAME); 
        setVerticalLine(world, i - 5, j + 17, j + 19, k - 4, FRAME); 
        setVerticalLine(world, i - 4, j + 17, j + 23, k - 4, FRAME); 

        setVerticalLine(world, i + 3, j + 17, j + 21, k - 3, FRAME);
        world.setBlock(i + 4, j + 17, k - 2, FRAME, 1, 2);
        setVerticalLine(world, i - 5, j + 17, j + 21, k - 3, FRAME);
        world.setBlock(i - 4, j + 25, k - 3, GLOWING, 0, 2);
        setVerticalLine(world, i + 4, j + 17, j + 19, k - 1, FRAME);
        world.setBlock(i + 4, j + 21, k - 1, GLOWING, 0, 2);
        world.setBlock(i + 13, j + 17, k - 1, FRAME, 1, 2);
        setVerticalLine(world, i - 6, j + 17, j + 21, k - 1, FRAME);
        world.setBlock(i - 5, j + 26, k - 1, FRAME, 1, 2);
        setVerticalLine(world, i - 6, j + 17, j + 19, k + 0, FRAME);
        world.setBlock(i - 5, j + 24, k + 0, FRAME, 1, 2);
        setVerticalLine(world, i + 4, j + 17, j + 18, k + 0, FRAME);
        world.setBlock(i + 13, j + 17, k + 0, FRAME, 1, 2);

        setVerticalLine(world, i - 5, j + 17, j + 22, k + 1, FRAME);
        setVerticalLine(world, i - 4, j + 17, j + 24, k + 1, FRAME);
        setVerticalLine(world, i + 3, j + 17, j + 21, k + 1, FRAME);
        world.setBlock(i + 4, j + 17, k + 1, FRAME, 1, 2);
        setVerticalLine(world, i - 14, j + 17, j + 23, k + 1, FRAME);
        setVerticalLine(world, i - 13, j + 17, j + 20, k + 1, FRAME);
        setVerticalLine(world, i - 15, j + 17, j + 18, k + 1, FRAME);
        world.setBlock(i - 12, j + 17, k + 1, FRAME, 1, 2);
        setVerticalLine(world, i - 3, j + 17, j + 23, k + 2, FRAME);
        setVerticalLine(world, i - 4, j + 17, j + 20, k + 2, FRAME);
        world.setBlock(i - 5, j + 17, k + 2, FRAME, 1, 2);
        setVerticalLine(world, i + 2, j + 17, j + 23, k + 2, FRAME);
        setVerticalLine(world, i + 3, j + 17, j + 19, k + 2, FRAME);
        setVerticalLine(world, i - 14, j + 17, j + 21, k + 2, FRAME);
        setVerticalLine(world, i - 13, j + 17, j + 19, k + 2, FRAME);
        setVerticalLine(world, i - 12, j + 17, j + 19, k + 2, FRAME);
        setVerticalLine(world, i - 15, j + 17, j + 19, k + 2, FRAME);
        setVerticalLine(world, i - 2, j + 17, j + 26, k + 3, FRAME);
        setVerticalLine(world, i - 3, j + 17, j + 22, k + 3, FRAME);
        setVerticalLine(world, i - 4, j + 17, j + 18, k + 3, FRAME);
        setVerticalLine(world, i + 1, j + 17, j + 23, k + 3, FRAME);
        world.setBlock(i + 2, j + 17, k + 3, FRAME, 1, 2);
        setVerticalLine(world, i - 14, j + 17, j + 18, k + 3, FRAME);
        world.setBlock(i - 13, j + 17, k + 3, FRAME, 1, 2);
        setVerticalLine(world, i - 1, j + 17, j + 22, k + 4, FRAME);
        setVerticalLine(world, i - 2, j + 17, j + 19, k + 4, FRAME);
        world.setBlock(i - 3, j + 17, k + 4, FRAME, 1, 2);

        world.setBlock(i + 0, j + 17, k + 5, GLOWING, 0, 2);
        world.setBlock(i - 2, j + 17, k + 5, FRAME, 1, 2);
        setVerticalLine(world, i + 5, j + 17, j + 21, k + 5, FRAME);
        setVerticalLine(world, i + 6, j + 17, j + 19, k + 5, FRAME);
        setVerticalLine(world, i + 4, j + 17, j + 18, k + 5, FRAME);
        setVerticalLine(world, i + 5, j + 17, j + 19, k + 4, FRAME);
        world.setBlock(i + 6, j + 17, k + 4, FRAME, 1, 2);
        setVerticalLine(world, i + 5, j + 17, j + 19, k + 6, FRAME);
        world.setBlock(i + 4, j + 17, k + 6, FRAME, 1, 2);
        world.setBlock(i + 6, j + 17, k + 6, FRAME, 1, 2);
        world.setBlock(i + 3, j + 17, k + 7, FRAME, 1, 2);
        world.setBlock(i + 9, j + 17, k + 7, FRAME, 1, 2);
        world.setBlock(i + 8, j + 16, k + 9, FRAME, 1, 2);
        setVerticalLine(world, i - 10, j + 17, j + 21, k + 10, FRAME);
        setVerticalLine(world, i - 11, j + 17, j + 19, k + 10, FRAME);
        world.setBlock(i - 9, j + 17, k + 10, FRAME, 1, 2);
        setVerticalLine(world, i - 10, j + 17, j + 19, k + 9, FRAME);
        setVerticalLine(world, i - 11, j + 17, j + 18, k + 9, FRAME);
        world.setBlock(i - 9, j + 17, k + 9, FRAME, 1, 2);
        world.setBlock(i - 10, j + 17, k + 11, FRAME, 1, 2);
        world.setBlock(i - 10, j + 18, k + 11, FRAME, 1, 2);
        world.setBlock(i + 5, j + 17, k + 12, FRAME, 1, 2);
        setVerticalLine(world, i + 4, j + 17, j + 19, k + 13, FRAME);
        world.setBlock(i + 3, j + 17, k + 13, FRAME, 1, 2);
        world.setBlock(i - 3, j + 17, k + 14, FRAME, 1, 2);
        setVerticalLine(world, i - 3, j + 17, j + 19, k + 15, FRAME);
        setVerticalLine(world, i - 4, j + 17, j + 18, k + 15, FRAME);
        setVerticalLine(world, i - 3, j + 17, j + 21, k + 16, FRAME);
        setVerticalLine(world, i - 2, j + 17, j + 20, k + 16, FRAME);
        world.setBlock(i - 5, j + 17, k + 16, FRAME, 1, 2);
        world.setBlock(i - 4, j + 17, k + 16, FRAME, 1, 2);
        world.setBlock(i - 1, j + 17, k + 16, FRAME, 1, 2);
        world.setBlock(i - 5, j + 19, k + 17, GLOWING, 0, 2);
        world.setBlock(i - 2, j + 17, k + 17, FRAME, 1, 2);
        world.setBlock(i - 1, j + 17, k + 17, FRAME, 1, 2);
        setVerticalLine(world, i - 9, j + 29, j + 33, k + 18, FRAME);
        setVerticalLine(world, i - 10, j + 27, j + 31, k + 18, FRAME);

        setVerticalLine(world, i - 11, j + 29, j + 32, k + 17, FRAME);
        setVerticalLine(world, i - 12, j + 28, j + 31, k + 16, FRAME);
        world.setBlock(i - 12, j + 28, k + 17, FRAME, 1, 2);
        setVerticalLine(world, i - 13, j + 25, j + 30, k + 15, FRAME);
        setVerticalLine(world, i - 14, j + 26, j + 29, k + 14, FRAME);
        setVerticalLine(world, i - 15, j + 28, j + 30, k + 13, FRAME);
        setVerticalLine(world, i - 15, j + 28, j + 29, k + 12, FRAME);

        world.setBlock(i + 1, j + 27, k + 2, GLOWING, 0, 2);
        world.setBlock(i - 4, j + 19, k + 3, GLOWING, 0, 2);
        world.setBlock(i - 7, j + 17, k + 3, FRAME, 1, 2);
        world.setBlock(i - 15, j + 17, k + 4, GLOWING, 0, 2);
        world.setBlock(i + 8, j + 17, k - 6, GLOWING, 0, 2);
        world.setBlock(i - 17, j + 19, k - 8, GLOWING, 0, 2);
        world.setBlock(i + 4, j + 19, k - 17, GLOWING, 0, 2);
        world.setBlock(i - 13, j + 18, k + 12, GLOWING, 0, 2);

        world.setBlock(i - 5, j + 25, k + 0, AIR, 0, 2);
        world.setBlock(i + 3, j + 18, k - 4, AIR, 0, 2);
        setVerticalLine(world, i + 1, j + 22, j + 23, k - 5, AIR);
        world.setBlock(i - 3, j + 21, k - 5, AIR, 0, 2);
        world.setBlock(i - 6, j + 18, k + 1, AIR, 0, 2);
        world.setBlock(i + 0, j + 19, k + 4, AIR, 0, 2);
        setVerticalLine(world, i + 4, j + 19, j + 20, k - 2, AIR);
        world.setBlock(i + 5, j + 19, k - 1, AIR, 0, 2);
        world.setBlock(i + 4, j + 17, k - 3, AIR, 0, 2);
        world.setBlock(i - 7, j + 17, k - 11, AIR, 0, 2);
        world.setBlock(i - 7, j + 17, k - 1, AIR, 0, 2);
        world.setBlock(i - 5, j + 20, k - 4, AIR, 0, 2);
        world.setBlock(i + 12, j + 18, k - 5, AIR, 0, 2);
        world.setBlock(i - 12, j + 20, k + 1, AIR, 0, 2);
        world.setBlock(i - 14, j + 24, k + 14, AIR, 0, 2);
        world.setBlock(i - 15, j + 18, k + 3, AIR, 0, 2);
        world.setBlock(i + 5, j + 17, k + 6, AIR, 0, 2);
        world.setBlock(i + 4, j + 18, k + 7, AIR, 0, 2);
        world.setBlock(i + 4, j + 19, k + 5, AIR, 0, 2);
        world.setBlock(i + 5, j + 19, k + 4, AIR, 0, 2);
        world.setBlock(i - 3, j + 20, k + 15, AIR, 0, 2);
        world.setBlock(i + 8, j + 17, k + 8, AIR, 0, 2);
    }

    private void generateLayer17(World world, int i, int j, int k) {    
        setVerticalLine(world, i - 2, j + 28, j + 32, k - 20, FRAME);
        world.setBlock(i - 2, j + 30, k - 19, FRAME, 1, 2);
        setVerticalLine(world, i - 3, j + 30, j + 32, k - 19, FRAME);
        setVerticalLine(world, i - 4, j + 28, j + 32, k - 19, FRAME);
        setVerticalLine(world, i - 5, j + 28, j + 33, k - 19, FRAME);
        world.setBlock(i - 6, j + 28, k - 19, FRAME, 1, 2);
        setVerticalLine(world, i - 6, j + 29, j + 31, k - 19, FRAME);
        setVerticalLine(world, i - 7, j + 29, j + 32, k - 18, FRAME);
        setVerticalLine(world, i - 8, j + 28, j + 31, k - 17, FRAME);
        setVerticalLine(world, i - 9, j + 27, j + 30, k - 16, FRAME);
        setVerticalLine(world, i - 10, j + 28, j + 30, k - 15, FRAME);
        setVerticalLine(world, i - 11, j + 27, j + 31, k - 15, FRAME);
        setVerticalLine(world, i - 12, j + 27, j + 30, k - 14, FRAME);

        world.setBlock(i - 13, j + 27, k - 14, FRAME, 1, 2);
        setVerticalLine(world, i - 13, j + 28, j + 30, k - 14, FRAME);
        world.setBlock(i - 14, j + 29, k - 14, FRAME, 1, 2);
        setVerticalLine(world, i - 15, j + 27, j + 29, k - 13, FRAME);
        setVerticalLine(world, i - 16, j + 26, j + 30, k - 12, FRAME);
        setVerticalLine(world, i - 16, j + 27, j + 29, k - 11, FRAME);
        setVerticalLine(world, i - 17, j + 27, j + 30, k - 9, FRAME);

        setVerticalLine(world, i - 18, j + 25, j + 28, k - 8, FRAME);
        setVerticalLine(world, i - 19, j + 26, j + 31, k - 7, FRAME);
        setVerticalLine(world, i - 19, j + 28, j + 32, k - 6, FRAME);
        setVerticalLine(world, i - 20, j + 26, j + 31, k - 5, FRAME);
        setVerticalLine(world, i - 20, j + 28, j + 32, k - 4, FRAME);
        setVerticalLine(world, i - 20, j + 27, j + 32, k - 3, FRAME);

        setVerticalLine(world, i - 21, j + 27, j + 33, k - 2, FRAME);
        setVerticalLine(world, i - 21, j + 28, j + 31, k - 1, FRAME);
        setVerticalLine(world, i - 21, j + 26, j + 30, k + 0, FRAME);
        setVerticalLine(world, i - 21, j + 26, j + 31, k + 1, FRAME);

        setVerticalLine(world, i - 21, j + 28, j + 32, k + 2, FRAME);
        setVerticalLine(world, i - 21, j + 27, j + 31, k + 3, FRAME);
        setVerticalLine(world, i - 21, j + 28, j + 32, k + 4, FRAME);
        setVerticalLine(world, i - 20, j + 28, j + 33, k + 5, FRAME);
        setVerticalLine(world, i - 20, j + 29, j + 32, k + 6, FRAME);
        setVerticalLine(world, i - 20, j + 27, j + 30, k + 7, FRAME);

        setVerticalLine(world, i - 19, j + 27, j + 30, k + 8, FRAME);
        setVerticalLine(world, i - 18, j + 27, j + 30, k + 9, FRAME);
        setVerticalLine(world, i - 17, j + 26, j + 29, k + 10, FRAME);
        setVerticalLine(world, i - 16, j + 28, j + 31, k + 11, FRAME);
        world.setBlock(i - 15, j + 30, k + 12, FRAME, 1, 2);
        setVerticalLine(world, i - 15, j + 26, j + 27, k + 13, FRAME);

        setVerticalLine(world, i - 5, j + 25, j + 27, k - 19, FRAME);
        setVerticalLine(world, i - 2, j + 29, j + 34, k - 19, FRAME);
        setVerticalLine(world, i - 1, j + 28, j + 32, k - 19, FRAME);
        setVerticalLine(world, i + 0, j + 29, j + 33, k - 19, FRAME);
        setVerticalLine(world, i + 1, j + 28, j + 32, k - 19, FRAME);
        setVerticalLine(world, i + 2, j + 27, j + 33, k - 19, FRAME);
        setVerticalLine(world, i + 3, j + 27, j + 35, k - 19, FRAME);
        setVerticalLine(world, i + 4, j + 25, j + 34, k - 19, FRAME);
        setVerticalLine(world, i - 19, j + 32, j + 35, k - 3, FRAME);
        setVerticalLine(world, i - 18, j + 34, j + 38, k - 3, FRAME);

        setVerticalLine(world, i - 17, j + 38, j + 40, k - 3, FRAME);
        setVerticalLine(world, i - 16, j + 40, j + 41, k - 3, FRAME);
        setVerticalLine(world, i - 15, j + 41, j + 43, k - 3, FRAME);
        setVerticalLine(world, i - 13, j + 45, j + 47, k - 3, FRAME);
        setVerticalLine(world, i - 12, j + 47, j + 50, k - 3, FRAME);
        setVerticalLine(world, i - 11, j + 50, j + 53, k - 3, FRAME);
        setVerticalLine(world, i - 10, j + 53, j + 58, k - 3, FRAME);
        world.setBlock(i - 9, j + 55, k - 3, FRAME, 1, 2);
        setVerticalLine(world, i - 7, j + 49, j + 50, k - 3, FRAME);
        world.setBlock(i - 6, j + 50, k - 3, FRAME, 1, 2);
        setVerticalLine(world, i - 5, j + 50, j + 51, k - 3, FRAME);

        setVerticalLine(world, i - 5, j + 51, j + 51, k - 5, FRAME);
        setVerticalLine(world, i - 4, j + 51, j + 51, k - 5, FRAME);
        setVerticalLine(world, i - 3, j + 51, j + 51, k - 5, FRAME);
        setVerticalLine(world, i - 2, j + 51, j + 51, k - 5, FRAME);
        setVerticalLine(world, i - 1, j + 51, j + 51, k - 5, FRAME);
        setVerticalLine(world, i - 5, j + 51, j + 51, k - 4, FRAME);
        setVerticalLine(world, i - 4, j + 51, j + 51, k - 4, FRAME);
        setVerticalLine(world, i - 3, j + 51, j + 51, k - 4, FRAME);
        setVerticalLine(world, i - 2, j + 51, j + 51, k - 4, FRAME);
        setVerticalLine(world, i - 1, j + 51, j + 51, k - 4, FRAME);
        setVerticalLine(world, i - 5, j + 51, j + 51, k - 3, FRAME);
        setVerticalLine(world, i - 4, j + 51, j + 51, k - 3, FRAME);
        setVerticalLine(world, i - 3, j + 51, j + 51, k - 3, FRAME);
        setVerticalLine(world, i - 2, j + 51, j + 51, k - 3, FRAME);
        setVerticalLine(world, i - 1, j + 51, j + 51, k - 3, FRAME);
        setVerticalLine(world, i + 0, j + 51, j + 51, k - 3, FRAME);
        setVerticalLine(world, i + 1, j + 51, j + 51, k - 3, FRAME);
        setVerticalLine(world, i - 5, j + 51, j + 51, k - 2, FRAME);
        setVerticalLine(world, i - 4, j + 51, j + 51, k - 2, FRAME);
        setVerticalLine(world, i - 3, j + 51, j + 51, k - 2, FRAME);
        setVerticalLine(world, i - 2, j + 51, j + 51, k - 2, FRAME);
        setVerticalLine(world, i - 1, j + 50, j + 51, k - 2, FRAME);
        setVerticalLine(world, i + 0, j + 51, j + 51, k - 2, FRAME);
        setVerticalLine(world, i + 1, j + 51, j + 51, k - 2, FRAME);
        setVerticalLine(world, i + 2, j + 51, j + 51, k - 2, FRAME);
        setVerticalLine(world, i + 3, j + 51, j + 51, k - 2, FRAME);
        setVerticalLine(world, i - 5, j + 51, j + 51, k - 1, FRAME);
        setVerticalLine(world, i - 4, j + 51, j + 51, k - 1, FRAME);
        setVerticalLine(world, i - 3, j + 51, j + 51, k - 1, FRAME);
        setVerticalLine(world, i - 2, j + 51, j + 51, k - 1, FRAME);
        setVerticalLine(world, i + 0, j + 51, j + 51, k - 1, FRAME);
        setVerticalLine(world, i + 1, j + 51, j + 51, k - 1, FRAME);
        setVerticalLine(world, i + 2, j + 51, j + 51, k - 1, FRAME);
        setVerticalLine(world, i + 3, j + 51, j + 51, k - 1, FRAME);
        setVerticalLine(world, i - 1, j + 51, j + 51, k + 0, FRAME);
        setVerticalLine(world, i + 0, j + 51, j + 51, k + 0, FRAME);
        setVerticalLine(world, i + 1, j + 51, j + 51, k + 0, FRAME);
        setVerticalLine(world, i + 2, j + 51, j + 51, k + 0, FRAME);
        setVerticalLine(world, i + 3, j + 51, j + 51, k + 0, FRAME);
        setVerticalLine(world, i - 1, j + 51, j + 51, k + 1, FRAME);
        setVerticalLine(world, i + 0, j + 51, j + 51, k + 1, FRAME);
        setVerticalLine(world, i + 1, j + 51, j + 51, k + 1, FRAME);
        setVerticalLine(world, i + 2, j + 51, j + 51, k + 1, FRAME);
        setVerticalLine(world, i + 3, j + 51, j + 51, k + 1, FRAME);
        setVerticalLine(world, i - 1, j + 51, j + 51, k + 2, FRAME);
        setVerticalLine(world, i + 0, j + 51, j + 51, k + 2, FRAME);
        setVerticalLine(world, i + 1, j + 51, j + 51, k + 2, FRAME);
        setVerticalLine(world, i + 2, j + 51, j + 51, k + 2, FRAME);
        setVerticalLine(world, i + 3, j + 51, j + 51, k + 2, FRAME);
        setVerticalLine(world, i - 1, j + 51, j + 51, k + 3, FRAME);
        setVerticalLine(world, i + 0, j + 51, j + 51, k + 3, FRAME);
        setVerticalLine(world, i + 1, j + 51, j + 51, k + 3, FRAME);
        setVerticalLine(world, i + 2, j + 51, j + 51, k + 3, FRAME);
        setVerticalLine(world, i + 3, j + 51, j + 51, k + 3, FRAME);

        world.setBlock(i - 19, j + 29, k + 6, AIR, 0, 2);
        world.setBlock(i - 19, j + 30, k + 6, AIR, 0, 2);
        world.setBlock(i - 20, j + 30, k + 0, AIR, 0, 2);
        world.setBlock(i - 18, j + 24, k - 8, AIR, 0, 2);
        world.setBlock(i - 16, j + 28, k - 9, AIR, 0, 2);
        world.setBlock(i - 16, j + 27, k - 10, AIR, 0, 2);
        world.setBlock(i - 15, j + 24, k - 13, AIR, 0, 2);
        world.setBlock(i - 15, j + 26, k - 13, AIR, 0, 2);
        world.setBlock(i - 10, j + 27, k - 15, AIR, 0, 2);
        setVerticalLine(world, i - 9, j + 25, j + 26, k - 16, AIR);
        world.setBlock(i - 9, j + 28, k - 17, AIR, 0, 2);
        setVerticalLine(world, i - 7, j + 27, j + 28, k - 18, AIR);
        world.setBlock(i - 6, j + 30, k - 18, AIR, 0, 2);
        setVerticalLine(world, i - 3, j + 28, j + 29, k - 19, AIR);
        world.setBlock(i - 2, j + 28, k - 19, AIR, 0, 2);
        world.setBlock(i + 1, j + 24, k - 19, AIR, 0, 2);
        world.setBlock(i - 20, j + 33, k - 3, AIR, 0, 2);
        world.setBlock(i - 15, j + 42, k - 2, AIR, 0, 2);
        world.setBlock(i - 14, j + 42, k - 3, AIR, 0, 2);
        setVerticalLine(world, i - 13, j + 43, j + 45, k - 3, AIR);
        setVerticalLine(world, i - 12, j + 44, j + 46, k - 3, AIR);
        setVerticalLine(world, i - 11, j + 46, j + 47, k - 3, AIR);
        world.setBlock(i - 10, j + 47, k - 3, AIR, 0, 2);
        setVerticalLine(world, i - 7, j + 49, j + 50, k - 3, AIR);
        setVerticalLine(world, i - 5, j + 50, j + 51, k - 3, AIR);
        world.setBlock(i - 12, j + 48, k - 4, AIR, 0, 2);
        world.setBlock(i - 2, j + 52, k - 4, AIR, 0, 2);
        world.setBlock(i - 1, j + 52, k - 1, AIR, 0, 2);
    }
     
    private void generateLayer18(World world, int i, int j, int k) {
        setVerticalLine(world, i + 2, j + 51, j + 51, k - 7, FRAME);
        setVerticalLine(world, i + 1, j + 51, j + 51, k - 7, FRAME);
        setVerticalLine(world, i + 0, j + 51, j + 51, k - 7, FRAME);
        setVerticalLine(world, i - 1, j + 51, j + 51, k - 7, FRAME);
        setVerticalLine(world, i - 2, j + 51, j + 51, k - 7, FRAME);
        setVerticalLine(world, i - 3, j + 51, j + 51, k - 7, FRAME);
        setVerticalLine(world, i + 2, j + 51, j + 51, k - 6, FRAME);
        setVerticalLine(world, i + 1, j + 51, j + 51, k - 6, FRAME);
        setVerticalLine(world, i + 0, j + 51, j + 51, k - 6, FRAME);
        setVerticalLine(world, i - 1, j + 51, j + 51, k - 6, FRAME);
        setVerticalLine(world, i - 2, j + 51, j + 51, k - 6, FRAME);
        setVerticalLine(world, i - 3, j + 51, j + 51, k - 6, FRAME);
        setVerticalLine(world, i - 4, j + 51, j + 51, k - 6, FRAME);
        setVerticalLine(world, i + 0, j + 51, j + 51, k - 5, FRAME);
        setVerticalLine(world, i + 1, j + 51, j + 51, k - 5, FRAME);
        setVerticalLine(world, i + 2, j + 51, j + 51, k - 5, FRAME);
        setVerticalLine(world, i + 3, j + 51, j + 51, k - 5, FRAME);
        setVerticalLine(world, i + 0, j + 51, j + 51, k - 4, FRAME);
        setVerticalLine(world, i + 1, j + 51, j + 51, k - 4, FRAME);
        setVerticalLine(world, i + 2, j + 51, j + 51, k - 4, FRAME);
        setVerticalLine(world, i + 3, j + 51, j + 51, k - 4, FRAME);
        setVerticalLine(world, i + 4, j + 51, j + 51, k - 4, FRAME);
        setVerticalLine(world, i + 2, j + 51, j + 51, k - 3, FRAME);
        setVerticalLine(world, i + 3, j + 51, j + 51, k - 3, FRAME);
        setVerticalLine(world, i + 4, j + 51, j + 51, k - 3, FRAME);
        setVerticalLine(world, i - 6, j + 51, j + 51, k - 3, FRAME);
        setVerticalLine(world, i - 7, j + 51, j + 51, k - 3, FRAME);
        setVerticalLine(world, i + 4, j + 51, j + 51, k - 2, FRAME);
        setVerticalLine(world, i - 6, j + 51, j + 51, k - 2, FRAME);
        setVerticalLine(world, i - 7, j + 51, j + 51, k - 2, FRAME);
        setVerticalLine(world, i + 4, j + 51, j + 51, k - 1, FRAME);
        setVerticalLine(world, i - 6, j + 51, j + 51, k - 1, FRAME);
        setVerticalLine(world, i - 7, j + 51, j + 51, k - 1, FRAME);
        setVerticalLine(world, i - 2, j + 51, j + 51, k + 0, FRAME);
        setVerticalLine(world, i - 3, j + 51, j + 51, k + 0, FRAME);
        setVerticalLine(world, i - 4, j + 51, j + 51, k + 0, FRAME);
        setVerticalLine(world, i - 5, j + 51, j + 51, k + 0, FRAME);
        setVerticalLine(world, i + 4, j + 51, j + 51, k + 0, FRAME);
        setVerticalLine(world, i - 6, j + 51, j + 51, k + 0, FRAME);
        setVerticalLine(world, i - 7, j + 51, j + 51, k + 0, FRAME);
        setVerticalLine(world, i - 2, j + 51, j + 51, k + 1, FRAME);
        setVerticalLine(world, i - 3, j + 51, j + 51, k + 1, FRAME);
        setVerticalLine(world, i - 4, j + 51, j + 51, k + 1, FRAME);
        setVerticalLine(world, i - 5, j + 51, j + 51, k + 1, FRAME);
        setVerticalLine(world, i - 6, j + 51, j + 51, k + 1, FRAME);
        setVerticalLine(world, i - 7, j + 51, j + 51, k + 1, FRAME);
        setVerticalLine(world, i + 4, j + 51, j + 51, k + 1, FRAME);
        setVerticalLine(world, i - 2, j + 51, j + 51, k + 2, FRAME);
        setVerticalLine(world, i - 3, j + 51, j + 51, k + 2, FRAME);
        setVerticalLine(world, i - 4, j + 51, j + 51, k + 2, FRAME);
        setVerticalLine(world, i - 5, j + 51, j + 51, k + 2, FRAME);
        setVerticalLine(world, i - 6, j + 51, j + 51, k + 2, FRAME);
        setVerticalLine(world, i - 7, j + 51, j + 51, k + 2, FRAME);
        setVerticalLine(world, i + 4, j + 51, j + 51, k + 2, FRAME);
        setVerticalLine(world, i + 5, j + 51, j + 51, k + 2, FRAME);
        setVerticalLine(world, i - 2, j + 51, j + 51, k + 3, FRAME);
        setVerticalLine(world, i - 3, j + 51, j + 51, k + 3, FRAME);
        setVerticalLine(world, i - 4, j + 51, j + 51, k + 3, FRAME);
        setVerticalLine(world, i - 5, j + 51, j + 51, k + 3, FRAME);
        setVerticalLine(world, i - 7, j + 51, j + 51, k + 3, FRAME);
        setVerticalLine(world, i + 5, j + 51, j + 51, k + 3, FRAME);
        setVerticalLine(world, i - 1, j + 51, j + 51, k + 4, FRAME);
        setVerticalLine(world, i + 0, j + 51, j + 51, k + 4, FRAME);
        setVerticalLine(world, i + 1, j + 51, j + 51, k + 4, FRAME);
        setVerticalLine(world, i + 2, j + 51, j + 51, k + 4, FRAME);
        setVerticalLine(world, i + 3, j + 51, j + 51, k + 4, FRAME);
        setVerticalLine(world, i - 4, j + 51, j + 51, k + 4, FRAME);
        setVerticalLine(world, i - 5, j + 51, j + 51, k + 4, FRAME);
        setVerticalLine(world, i - 1, j + 51, j + 51, k + 5, FRAME);
        setVerticalLine(world, i + 0, j + 51, j + 51, k + 5, FRAME);
        setVerticalLine(world, i + 1, j + 51, j + 51, k + 5, FRAME);
        setVerticalLine(world, i + 2, j + 51, j + 51, k + 5, FRAME);
        setVerticalLine(world, i - 3, j + 51, j + 51, k + 5, FRAME);
        setVerticalLine(world, i - 4, j + 51, j + 51, k + 5, FRAME);
        setVerticalLine(world, i + 3, j + 51, j + 51, k - 6, FRAME);
        setVerticalLine(world, i + 4, j + 51, j + 51, k - 5, FRAME);
        setVerticalLine(world, i + 5, j + 51, j + 51, k - 4, FRAME);
        setVerticalLine(world, i + 5, j + 51, j + 51, k - 3, FRAME);
        setVerticalLine(world, i + 5, j + 51, j + 51, k - 2, FRAME);
        setVerticalLine(world, i + 5, j + 51, j + 51, k - 1, FRAME);
        setVerticalLine(world, i + 5, j + 51, j + 51, k + 0, FRAME);
        setVerticalLine(world, i + 5, j + 51, j + 51, k + 1, FRAME);

        setVerticalLine(world, i - 2, j + 48, j + 50, k - 2, FRAME);
        setVerticalLine(world, i - 3, j + 47, j + 50, k - 2, FRAME);
        setVerticalLine(world, i - 1, j + 47, j + 50, k - 3, FRAME);
        setVerticalLine(world, i - 2, j + 47, j + 50, k - 3, FRAME);
        setVerticalLine(world, i - 3, j + 50, j + 50, k - 3, FRAME);
        setVerticalLine(world, i - 4, j + 47, j + 50, k - 2, FRAME);
        setVerticalLine(world, i + 0, j + 46, j + 50, k - 2, FRAME);
        setVerticalLine(world, i + 0, j + 47, j + 50, k - 3, FRAME);
        setVerticalLine(world, i - 1, j + 50, j + 50, k - 4, FRAME);
        setVerticalLine(world, i - 2, j + 50, j + 50, k - 4, FRAME);
        setVerticalLine(world, i - 1, j + 49, j + 49, k - 4, FRAME);
        setVerticalLine(world, i + 0, j + 47, j + 50, k + 0, FRAME);
        setVerticalLine(world, i - 1, j + 48, j + 50, k + 0, FRAME);
        setVerticalLine(world, i - 2, j + 48, j + 50, k + 0, FRAME);
        setVerticalLine(world, i - 3, j + 47, j + 50, k + 0, FRAME);
        setVerticalLine(world, i - 4, j + 50, j + 50, k + 0, FRAME);
        setVerticalLine(world, i + 1, j + 44, j + 50, k + 0, FRAME);
        setVerticalLine(world, i + 1, j + 46, j + 50, k - 1, FRAME);
        setVerticalLine(world, i + 0, j + 49, j + 50, k - 1, FRAME);
        setVerticalLine(world, i + 1, j + 49, j + 50, k - 2, FRAME);
        setVerticalLine(world, i - 2, j + 50, j + 50, k - 1, FRAME);
        setVerticalLine(world, i - 3, j + 47, j + 50, k - 1, FRAME);
        setVerticalLine(world, i - 4, j + 48, j + 50, k - 1, FRAME);
        setVerticalLine(world, i - 5, j + 50, j + 50, k - 1, FRAME);
        setVerticalLine(world, i - 4, j + 50, j + 50, k - 3, FRAME);
        setVerticalLine(world, i - 5, j + 50, j + 50, k - 2, FRAME);
        setVerticalLine(world, i + 0, j + 45, j + 50, k + 1, FRAME);
        setVerticalLine(world, i - 1, j + 47, j + 50, k + 1, FRAME);
        setVerticalLine(world, i - 2, j + 49, j + 50, k + 1, FRAME);
        setVerticalLine(world, i + 1, j + 48, j + 50, k + 1, FRAME);
        setVerticalLine(world, i + 0, j + 49, j + 50, k + 2, FRAME);
        setVerticalLine(world, i + 2, j + 48, j + 50, k + 0, FRAME);
        setVerticalLine(world, i + 2, j + 50, j + 50, k - 1, FRAME);
        setVerticalLine(world, i - 1, j + 44, j + 45, k - 3, FRAME);
        setVerticalLine(world, i - 3, j + 45, j + 46, k - 1, FRAME);
        setVerticalLine(world, i + 0, j + 44, j + 45, k - 2, FRAME);

        setVerticalLine(world, i - 7, j + 51, j + 54, k - 4, FRAME);
        setVerticalLine(world, i - 7, j + 51, j + 53, k + 3, FRAME);
        setVerticalLine(world, i - 7, j + 54, j + 57, k - 3, FRAME);
        setVerticalLine(world, i - 7, j + 55, j + 57, k - 2, FRAME);
        setVerticalLine(world, i - 7, j + 57, j + 57, k - 1, FRAME);
        setVerticalLine(world, i - 7, j + 57, j + 57, k + 0, FRAME);
        setVerticalLine(world, i - 7, j + 54, j + 57, k + 2, FRAME);
        setVerticalLine(world, i - 7, j + 56, j + 57, k + 1, FRAME);
        setVerticalLine(world, i - 8, j + 51, j + 52, k - 3, FRAME);
        setVerticalLine(world, i - 8, j + 51, j + 52, k + 2, FRAME);
        setVerticalLine(world, i - 8, j + 51, j + 51, k - 2, FRAME);
        setVerticalLine(world, i - 8, j + 51, j + 51, k - 1, FRAME);
        setVerticalLine(world, i - 8, j + 51, j + 51, k + 0, FRAME);
        setVerticalLine(world, i - 8, j + 51, j + 51, k + 1, FRAME);

        setVerticalLine(world, i - 6, j + 52, j + 53, k - 3, FRAME);
        world.setBlock(i - 6, j + 52, k - 2, FRAME, 2, 2);
        world.setBlock(i - 6, j + 52, k - 1, FRAME, 2, 2);
        world.setBlock(i - 6, j + 52, k + 0, FRAME, 2, 2);
        world.setBlock(i - 6, j + 52, k + 1, FRAME, 2, 2);
        world.setBlock(i - 6, j + 53, k - 2, FRAME, 2, 2);
        world.setBlock(i - 6, j + 53, k - 1, FRAME, 2, 2);
        world.setBlock(i - 6, j + 53, k + 0, FRAME, 2, 2);
        world.setBlock(i - 6, j + 53, k + 1, FRAME, 2, 2);
        setVerticalLine(world, i - 6, j + 54, j + 55, k - 2, FRAME);
        setVerticalLine(world, i - 6, j + 54, j + 55, k - 1, FRAME);
        setVerticalLine(world, i - 6, j + 54, j + 55, k + 0, FRAME);
        setVerticalLine(world, i - 6, j + 54, j + 55, k + 1, FRAME);
        world.setBlock(i - 6, j + 55, k - 3, FRAME, 2, 2);
        world.setBlock(i - 6, j + 56, k - 1, FRAME, 2, 2);
        world.setBlock(i - 6, j + 56, k + 0, FRAME, 2, 2);

        setVerticalLine(world, i + 5, j + 28, j + 32, k - 18, FRAME);
        setVerticalLine(world, i + 6, j + 27, j + 31, k - 17, FRAME);
        setVerticalLine(world, i + 7, j + 25, j + 31, k - 16, FRAME);
        setVerticalLine(world, i + 8, j + 26, j + 30, k - 15, FRAME);
        setVerticalLine(world, i + 9, j + 24, j + 31, k - 15, FRAME);
        setVerticalLine(world, i + 10, j + 25, j + 28, k - 14, FRAME);
        setVerticalLine(world, i + 11, j + 24, j + 30, k - 13, FRAME);
        setVerticalLine(world, i + 12, j + 26, j + 30, k - 12, FRAME);
        setVerticalLine(world, i + 13, j + 25, j + 30, k - 12, FRAME);
        setVerticalLine(world, i + 14, j + 29, j + 31, k - 10, FRAME);
        setVerticalLine(world, i + 14, j + 27, j + 28, k - 11, FRAME);
        setVerticalLine(world, i + 15, j + 28, j + 30, k - 10, FRAME);
        setVerticalLine(world, i + 15, j + 28, j + 30, k - 9, FRAME);
        setVerticalLine(world, i + 16, j + 26, j + 31, k - 8, FRAME);
        setVerticalLine(world, i + 16, j + 28, j + 30, k - 7, FRAME);
        setVerticalLine(world, i + 17, j + 27, j + 31, k - 6, FRAME);

        setVerticalLine(world, i - 4, j + 52, j + 52, k + 4, AIR);
        setVerticalLine(world, i - 4, j + 52, j + 52, k - 5, AIR);
        setVerticalLine(world, i - 1, j + 47, j + 47, k - 4, AIR);
        setVerticalLine(world, i - 2, j + 48, j + 48, k - 4, AIR);
        setVerticalLine(world, i + 0, j + 49, j + 49, k + 1, AIR);
        setVerticalLine(world, i + 0, j + 44, j + 45, k + 0, AIR);
        setVerticalLine(world, i - 1, j + 43, j + 44, k + 1, AIR);
        setVerticalLine(world, i - 2, j + 46, j + 46, k + 0, AIR);
        setVerticalLine(world, i + 1, j + 49, j + 49, k - 2, AIR);
        setVerticalLine(world, i + 0, j + 48, j + 48, k + 2, AIR);
        setVerticalLine(world, i - 10, j + 53, j + 58, k - 3, AIR);
        setVerticalLine(world, i - 11, j + 53, j + 53, k - 3, AIR);
        setVerticalLine(world, i - 9, j + 55, j + 55, k - 3, AIR);
        setVerticalLine(world, i - 6, j + 52, j + 52, k - 4, AIR);
        setVerticalLine(world, i - 6, j + 52, j + 52, k - 3, AIR);
        setVerticalLine(world, i - 6, j + 54, j + 54, k - 3, AIR);
        setVerticalLine(world, i - 6, j + 52, j + 52, k + 2, AIR);
        setVerticalLine(world, i - 7, j + 52, j + 53, k - 3, AIR);
        setVerticalLine(world, i - 7, j + 54, j + 57, k - 3, AIR);
        setVerticalLine(world, i - 7, j + 55, j + 57, k - 2, AIR);
        setVerticalLine(world, i - 7, j + 52, j + 52, k - 2, AIR);
        setVerticalLine(world, i - 7, j + 55, j + 55, k + 2, AIR);
        setVerticalLine(world, i + 5, j + 29, j + 30, k - 18, AIR);
        setVerticalLine(world, i + 14, j + 29, j + 29, k - 9, AIR);
        setVerticalLine(world, i + 17, j + 29, j + 29, k - 5, AIR);

        world.setBlock(i + 3, j + 52, k - 4, GLOWING, 0, 2);
        world.setBlock(i - 6, j + 53, k + 2, GLOWING, 0, 2);
        world.setBlock(i + 0, j + 51, k + 6, GLOWING, 0, 2);
        world.setBlock(i - 3, j + 50, k - 5, GLOWING, 0, 2);
        world.setBlock(i + 5, j + 51, k - 6, GLOWING, 0, 2);
        world.setBlock(i - 7, j + 56, k + 0, GLOWING, 0, 2);
        world.setBlock(i + 0, j + 48, k + 3, GLOWING, 0, 2);
        world.setBlock(i + 4, j + 50, k - 2, GLOWING, 0, 2);
    }

    private void generateLayer19(World world, int i, int j, int k) {
        setVerticalLine(world, i + 4, j + 51, j + 51, k + 3, FRAME);
        setVerticalLine(world, i + 5, j + 51, j + 51, k + 3, AIR);

        setVerticalLine(world, i + 18, j + 25, j + 32, k - 5, FRAME);
        setVerticalLine(world, i + 18, j + 26, j + 30, k - 4, FRAME);
        setVerticalLine(world, i + 19, j + 25, j + 25, k - 4, FRAME);
        setVerticalLine(world, i + 18, j + 26, j + 29, k - 3, FRAME);
        setVerticalLine(world, i + 19, j + 26, j + 26, k - 3, FRAME);
        setVerticalLine(world, i + 18, j + 26, j + 30, k - 2, FRAME);
        setVerticalLine(world, i + 18, j + 26, j + 31, k - 1, FRAME);
        setVerticalLine(world, i + 19, j + 25, j + 25, k - 1, FRAME);
        setVerticalLine(world, i + 18, j + 25, j + 29, k + 0, FRAME);
        setVerticalLine(world, i + 18, j + 26, j + 32, k + 1, FRAME);
        setVerticalLine(world, i + 18, j + 27, j + 31, k + 2, FRAME);
        setVerticalLine(world, i + 18, j + 25, j + 29, k + 3, FRAME);

        setVerticalLine(world, i + 17, j + 24, j + 29, k + 5, FRAME);
        setVerticalLine(world, i + 16, j + 25, j + 28, k + 6, FRAME);
        setVerticalLine(world, i + 15, j + 26, j + 30, k + 7, FRAME);
        setVerticalLine(world, i + 14, j + 25, j + 29, k + 8, FRAME);
        setVerticalLine(world, i + 13, j + 24, j + 29, k + 9, FRAME);
        setVerticalLine(world, i + 12, j + 26, j + 30, k + 10, FRAME);
        setVerticalLine(world, i + 11, j + 25, j + 29, k + 11, FRAME);
        setVerticalLine(world, i + 10, j + 26, j + 29, k + 12, FRAME);
        setVerticalLine(world, i + 9, j + 27, j + 29, k + 13, FRAME);
        setVerticalLine(world, i + 8, j + 28, j + 30, k + 14, FRAME);
        setVerticalLine(world, i + 8, j + 27, j + 29, k + 15, FRAME);
        setVerticalLine(world, i + 8, j + 25, j + 26, k + 16, FRAME);

        setVerticalLine(world, i - 6, j + 32, j + 33, k - 18, FRAME);
        setVerticalLine(world, i - 7, j + 31, j + 32, k - 17, FRAME);
        setVerticalLine(world, i - 8, j + 30, j + 33, k - 16, FRAME);
        setVerticalLine(world, i - 9, j + 30, j + 32, k - 15, FRAME);
        setVerticalLine(world, i - 10, j + 31, j + 32, k - 14, FRAME);
        setVerticalLine(world, i - 11, j + 31, j + 34, k - 14, FRAME);
        setVerticalLine(world, i - 12, j + 31, j + 32, k - 14, FRAME);
        setVerticalLine(world, i - 12, j + 30, j + 31, k - 13, FRAME);
        setVerticalLine(world, i - 13, j + 29, j + 31, k - 13, FRAME);
        setVerticalLine(world, i - 14, j + 28, j + 32, k - 13, FRAME);
        setVerticalLine(world, i - 14, j + 30, j + 33, k - 12, FRAME);
        setVerticalLine(world, i - 15, j + 30, j + 32, k - 11, FRAME);
        setVerticalLine(world, i - 16, j + 30, j + 30, k - 11, FRAME);
        setVerticalLine(world, i - 15, j + 30, j + 31, k - 10, FRAME);
        setVerticalLine(world, i - 16, j + 30, j + 33, k - 9, FRAME);
        setVerticalLine(world, i - 17, j + 29, j + 34, k - 8, FRAME);
        setVerticalLine(world, i - 18, j + 30, j + 35, k - 7, FRAME);
        setVerticalLine(world, i - 18, j + 32, j + 34, k - 6, FRAME);

        setVerticalLine(world, i - 19, j + 31, j + 34, k - 5, FRAME);
        setVerticalLine(world, i - 19, j + 32, j + 34, k - 4, FRAME);
        setVerticalLine(world, i - 19, j + 30, j + 31, k - 3, FRAME);
        setVerticalLine(world, i - 20, j + 33, j + 36, k - 2, FRAME);
        setVerticalLine(world, i - 19, j + 35, j + 35, k - 2, FRAME);
        setVerticalLine(world, i - 20, j + 31, j + 34, k - 1, FRAME);
        setVerticalLine(world, i - 20, j + 31, j + 36, k + 0, FRAME);
        setVerticalLine(world, i - 20, j + 30, j + 34, k + 1, FRAME);
        setVerticalLine(world, i - 20, j + 32, j + 34, k + 2, FRAME);
        setVerticalLine(world, i - 20, j + 31, j + 34, k + 3, FRAME);
        setVerticalLine(world, i - 20, j + 31, j + 34, k + 4, FRAME);
        setVerticalLine(world, i - 19, j + 33, j + 34, k + 5, FRAME);
        setVerticalLine(world, i - 19, j + 31, j + 33, k + 6, FRAME);
        setVerticalLine(world, i - 19, j + 32, j + 33, k + 7, FRAME);
        setVerticalLine(world, i - 18, j + 30, j + 33, k + 8, FRAME);
        setVerticalLine(world, i - 17, j + 30, j + 32, k + 9, FRAME);
        setVerticalLine(world, i - 16, j + 29, j + 32, k + 10, FRAME);
        setVerticalLine(world, i - 15, j + 30, j + 34, k + 11, FRAME);
        setVerticalLine(world, i - 14, j + 30, j + 31, k + 12, FRAME);
        setVerticalLine(world, i - 14, j + 30, j + 33, k + 13, FRAME);
        setVerticalLine(world, i - 13, j + 30, j + 32, k + 14, FRAME);
        setVerticalLine(world, i - 12, j + 31, j + 32, k + 15, FRAME);
        setVerticalLine(world, i - 11, j + 31, j + 34, k + 16, FRAME);
        setVerticalLine(world, i - 10, j + 31, j + 34, k + 17, FRAME);
        setVerticalLine(world, i - 9, j + 33, j + 35, k + 17, FRAME);
        setVerticalLine(world, i - 8, j + 30, j + 34, k + 17, FRAME);
        setVerticalLine(world, i - 7, j + 29, j + 32, k + 17, FRAME);

        setVerticalLine(world, i + 7, j + 28, j + 29, k + 16, FRAME);
        setVerticalLine(world, i + 6, j + 28, j + 31, k + 16, FRAME);
        setVerticalLine(world, i + 5, j + 27, j + 31, k + 16, FRAME);
        setVerticalLine(world, i + 4, j + 27, j + 31, k + 17, FRAME);
        setVerticalLine(world, i + 3, j + 27, j + 31, k + 18, FRAME);
        setVerticalLine(world, i + 2, j + 28, j + 31, k + 19, FRAME);
        setVerticalLine(world, i + 1, j + 29, j + 31, k + 19, FRAME);
        setVerticalLine(world, i + 0, j + 28, j + 32, k + 19, FRAME);
        setVerticalLine(world, i - 1, j + 28, j + 32, k + 19, FRAME);
        setVerticalLine(world, i - 2, j + 28, j + 30, k + 19, FRAME);
        setVerticalLine(world, i - 3, j + 28, j + 32, k + 19, FRAME);
        setVerticalLine(world, i - 4, j + 27, j + 30, k + 19, FRAME);
        setVerticalLine(world, i - 5, j + 28, j + 29, k + 19, FRAME);
        setVerticalLine(world, i - 6, j + 27, j + 30, k + 18, FRAME);
        setVerticalLine(world, i - 7, j + 25, j + 29, k + 18, FRAME);
        setVerticalLine(world, i - 8, j + 28, j + 30, k + 18, FRAME);

        setVerticalLine(world, i + 18, j + 24, j + 25, k - 4, AIR);
        setVerticalLine(world, i + 18, j + 24, j + 25, k - 1, AIR);
        setVerticalLine(world, i + 17, j + 26, j + 26, k + 4, AIR);
        setVerticalLine(world, i + 12, j + 25, j + 25, k + 9, AIR);
        setVerticalLine(world, i + 8, j + 24, j + 24, k + 15, AIR);
        setVerticalLine(world, i + 7, j + 29, j + 29, k + 14, AIR);
        setVerticalLine(world, i - 8, j + 29, j + 29, k + 19, AIR);
        setVerticalLine(world, i + 1, j + 30, j + 30, k + 18, AIR);
        setVerticalLine(world, i + 7, j + 27, j + 27, k + 15, AIR);
        setVerticalLine(world, i + 7, j + 30, j + 31, k + 15, AIR);
        setVerticalLine(world, i + 5, j + 26, j + 27, k + 16, AIR);
        setVerticalLine(world, i + 6, j + 27, j + 27, k + 16, AIR);
        setVerticalLine(world, i + 4, j + 25, j + 26, k + 17, AIR);
        setVerticalLine(world, i - 19, j + 30, j + 30, k - 5, AIR);
        setVerticalLine(world, i - 20, j + 33, j + 33, k - 5, AIR);
        setVerticalLine(world, i - 20, j + 34, j + 34, k + 2, AIR);
        setVerticalLine(world, i - 17, j + 31, j + 31, k + 8, AIR);
        setVerticalLine(world, i - 16, j + 31, j + 31, k + 9, AIR);
        setVerticalLine(world, i - 15, j + 29, j + 29, k - 10, AIR);
        setVerticalLine(world, i - 14, j + 29, j + 29, k - 10, AIR);
        setVerticalLine(world, i - 14, j + 32, j + 32, k - 11, AIR);
        setVerticalLine(world, i - 12, j + 30, j + 30, k - 13, AIR);

        world.setBlock(i + 4, j + 52, k + 3, GLOWING, 0, 2);
        world.setBlock(i + 18, j + 33, k - 5, GLOWING, 0, 2);
        world.setBlock(i + 17, j + 30, k + 5, GLOWING, 0, 2);
        world.setBlock(i - 18, j + 36, k - 7, GLOWING, 0, 2);
        world.setBlock(i - 20, j + 37, k + 0, GLOWING, 0, 2);
        world.setBlock(i + 0, j + 33, k + 19, GLOWING, 0, 2);
     }

    private void generateLayer20(World world, int i, int j, int k) { 
        setVerticalLine(world, i - 6, j + 34, j + 34, k - 18, FRAME);
        setVerticalLine(world, i - 5, j + 33, j + 33, k - 18, FRAME);
        setVerticalLine(world, i - 4, j + 32, j + 34, k - 18, FRAME);
        setVerticalLine(world, i - 3, j + 31, j + 35, k - 18, FRAME);
        setVerticalLine(world, i - 2, j + 33, j + 36, k - 18, FRAME);
        setVerticalLine(world, i - 1, j + 32, j + 35, k - 18, FRAME);
        setVerticalLine(world, i + 0, j + 33, j + 35, k - 18, FRAME);
        setVerticalLine(world, i + 1, j + 32, j + 36, k - 18, FRAME);
        setVerticalLine(world, i + 2, j + 33, j + 37, k - 18, FRAME);
        setVerticalLine(world, i + 3, j + 34, j + 36, k - 18, FRAME);
        setVerticalLine(world, i + 4, j + 33, j + 35, k - 18, FRAME);

        setVerticalLine(world, i + 5, j + 32, j + 35, k - 17, FRAME);
        setVerticalLine(world, i + 6, j + 31, j + 34, k - 16, FRAME);
        setVerticalLine(world, i + 7, j + 30, j + 33, k - 15, FRAME);
        setVerticalLine(world, i + 8, j + 30, j + 33, k - 14, FRAME);
        setVerticalLine(world, i + 9, j + 28, j + 31, k - 14, FRAME);
        setVerticalLine(world, i + 9, j + 31, j + 34, k - 13, FRAME);
        setVerticalLine(world, i + 10, j + 28, j + 31, k - 13, FRAME);
        setVerticalLine(world, i + 10, j + 29, j + 29, k - 14, FRAME);
        setVerticalLine(world, i + 11, j + 29, j + 33, k - 12, FRAME);
        setVerticalLine(world, i + 10, j + 31, j + 32, k - 12, FRAME);
        setVerticalLine(world, i + 13, j + 29, j + 33, k - 11, FRAME);
        setVerticalLine(world, i + 12, j + 30, j + 33, k - 11, FRAME);
        setVerticalLine(world, i + 13, j + 31, j + 33, k - 10, FRAME);
        setVerticalLine(world, i - 14, j + 32, j + 34, k - 10, FRAME);
        setVerticalLine(world, i - 15, j + 33, j + 35, k - 9, FRAME);

        setVerticalLine(world, i + 15, j + 29, j + 34, k - 8, FRAME);
        setVerticalLine(world, i + 16, j + 31, j + 32, k - 7, FRAME);
        setVerticalLine(world, i + 15, j + 32, j + 33, k - 7, FRAME);
        setVerticalLine(world, i + 16, j + 30, j + 34, k - 6, FRAME);
        setVerticalLine(world, i + 17, j + 31, j + 33, k - 5, FRAME);
        setVerticalLine(world, i + 18, j + 33, j + 33, k - 5, FRAME);
        setVerticalLine(world, i + 18, j + 31, j + 33, k - 4, FRAME);
        setVerticalLine(world, i + 17, j + 32, j + 33, k - 4, FRAME);
        setVerticalLine(world, i + 17, j + 29, j + 33, k - 3, FRAME);
        setVerticalLine(world, i + 18, j + 30, j + 31, k - 3, FRAME);
        setVerticalLine(world, i + 17, j + 30, j + 33, k - 2, FRAME);
        setVerticalLine(world, i + 17, j + 30, j + 34, k - 1, FRAME);

        setVerticalLine(world, i + 17, j + 29, j + 33, k + 0, FRAME);
        setVerticalLine(world, i + 18, j + 30, j + 30, k + 0, FRAME);
        setVerticalLine(world, i + 17, j + 30, j + 33, k + 1, FRAME);
        setVerticalLine(world, i + 17, j + 29, j + 32, k + 2, FRAME);
        setVerticalLine(world, i + 17, j + 28, j + 30, k + 3, FRAME);
        setVerticalLine(world, i + 17, j + 27, j + 31, k + 4, FRAME);
        setVerticalLine(world, i + 16, j + 29, j + 31, k + 5, FRAME);
        setVerticalLine(world, i + 15, j + 27, j + 30, k + 6, FRAME);
        setVerticalLine(world, i + 14, j + 28, j + 31, k + 7, FRAME);
        setVerticalLine(world, i + 15, j + 31, j + 31, k + 7, FRAME);
        setVerticalLine(world, i + 13, j + 29, j + 31, k + 8, FRAME);
        setVerticalLine(world, i + 12, j + 28, j + 32, k + 9, FRAME);

        setVerticalLine(world, i + 11, j + 29, j + 31, k + 10, FRAME);
        setVerticalLine(world, i + 10, j + 29, j + 32, k + 11, FRAME);
        setVerticalLine(world, i + 9, j + 29, j + 31, k + 12, FRAME);
        setVerticalLine(world, i + 9, j + 28, j + 28, k + 12, FRAME);
        setVerticalLine(world, i + 8, j + 29, j + 32, k + 13, FRAME);
        setVerticalLine(world, i + 7, j + 29, j + 32, k + 14, FRAME);
        setVerticalLine(world, i + 6, j + 29, j + 34, k + 15, FRAME);
        setVerticalLine(world, i + 5, j + 31, j + 34, k + 15, FRAME);
        setVerticalLine(world, i + 4, j + 30, j + 32, k + 16, FRAME);
        setVerticalLine(world, i + 3, j + 29, j + 30, k + 17, FRAME);
        setVerticalLine(world, i + 2, j + 30, j + 33, k + 18, FRAME);
        setVerticalLine(world, i + 1, j + 31, j + 32, k + 18, FRAME);
        setVerticalLine(world, i + 0, j + 31, j + 32, k + 18, FRAME);
        setVerticalLine(world, i - 1, j + 30, j + 32, k + 18, FRAME);
        setVerticalLine(world, i - 2, j + 29, j + 31, k + 18, FRAME);
        setVerticalLine(world, i - 3, j + 31, j + 33, k + 18, FRAME);
        setVerticalLine(world, i - 4, j + 29, j + 33, k + 18, FRAME);

        setVerticalLine(world, i - 5, j + 30, j + 32, k + 17, FRAME);
        setVerticalLine(world, i - 6, j + 31, j + 33, k + 17, FRAME);
        setVerticalLine(world, i - 9, j + 34, j + 36, k + 16, FRAME);
        setVerticalLine(world, i - 10, j + 34, j + 36, k + 16, FRAME);
        setVerticalLine(world, i - 11, j + 34, j + 36, k + 15, FRAME);
        setVerticalLine(world, i - 12, j + 33, j + 35, k + 14, FRAME);
        setVerticalLine(world, i - 13, j + 33, j + 35, k + 13, FRAME);
        setVerticalLine(world, i - 15, j + 31, j + 34, k + 12, FRAME);
        setVerticalLine(world, i - 14, j + 34, j + 36, k + 12, FRAME);
        setVerticalLine(world, i - 14, j + 34, j + 35, k + 11, FRAME);
        setVerticalLine(world, i - 15, j + 33, j + 36, k + 10, FRAME);
        setVerticalLine(world, i - 16, j + 32, j + 34, k + 9, FRAME);
        setVerticalLine(world, i - 17, j + 32, j + 34, k + 8, FRAME);

        setVerticalLine(world, i - 18, j + 33, j + 35, k + 7, FRAME);
        setVerticalLine(world, i - 18, j + 34, j + 36, k + 6, FRAME);
        setVerticalLine(world, i - 18, j + 35, j + 37, k + 5, FRAME);
        setVerticalLine(world, i - 19, j + 34, j + 36, k + 4, FRAME);
        setVerticalLine(world, i - 19, j + 34, j + 36, k + 3, FRAME);
        setVerticalLine(world, i - 19, j + 35, j + 37, k + 2, FRAME);
        setVerticalLine(world, i - 19, j + 34, j + 36, k + 1, FRAME);
        setVerticalLine(world, i - 19, j + 35, j + 37, k + 0, FRAME);
        setVerticalLine(world, i - 19, j + 34, j + 36, k - 1, FRAME);
        setVerticalLine(world, i - 19, j + 36, j + 37, k - 2, FRAME);
        setVerticalLine(world, i - 18, j + 35, j + 37, k - 4, FRAME);
        setVerticalLine(world, i - 18, j + 34, j + 36, k - 5, FRAME);
        setVerticalLine(world, i - 17, j + 35, j + 37, k - 6, FRAME);
        setVerticalLine(world, i - 17, j + 33, j + 35, k - 7, FRAME);
        setVerticalLine(world, i - 16, j + 34, j + 36, k - 8, FRAME);
        setVerticalLine(world, i - 14, j + 33, j + 34, k - 11, FRAME);

        setVerticalLine(world, i - 6, j + 35, j + 35, k - 18, AIR);
        setVerticalLine(world, i - 3, j + 33, j + 33, k - 19, AIR);
        setVerticalLine(world, i + 1, j + 35, j + 35, k - 17, AIR);
        setVerticalLine(world, i + 3, j + 37, j + 38, k - 18, AIR);
        setVerticalLine(world, i + 4, j + 36, j + 36, k - 18, AIR);
        setVerticalLine(world, i + 6, j + 35, j + 35, k - 16, AIR);
        setVerticalLine(world, i + 9, j + 29, j + 29, k - 13, AIR);
        setVerticalLine(world, i + 9, j + 32, j + 32, k - 15, AIR);
        setVerticalLine(world, i + 10, j + 30, j + 30, k - 12, AIR);
        setVerticalLine(world, i + 13, j + 31, j + 31, k + 7, AIR);
        setVerticalLine(world, i + 9, j + 32, j + 32, k + 11, AIR);
        setVerticalLine(world, i + 8, j + 23, j + 23, k + 15, AIR);
        setVerticalLine(world, i + 4, j + 31, j + 31, k + 15, AIR);
        setVerticalLine(world, i + 3, j + 31, j + 31, k + 16, AIR);
        setVerticalLine(world, i - 1, j + 32, j + 32, k + 17, AIR);
        setVerticalLine(world, i - 3, j + 32, j + 32, k + 17, AIR);
        setVerticalLine(world, i - 5, j + 29, j + 29, k + 17, AIR);
        setVerticalLine(world, i - 6, j + 32, j + 32, k + 16, AIR);
        setVerticalLine(world, i - 9, j + 36, j + 36, k + 17, AIR);
        setVerticalLine(world, i - 10, j + 35, j + 35, k + 17, AIR);
        setVerticalLine(world, i - 14, j + 32, j + 32, k + 12, AIR);
        setVerticalLine(world, i - 15, j + 35, j + 35, k + 12, AIR);
        setVerticalLine(world, i - 16, j + 33, j + 33, k + 10, AIR);
        setVerticalLine(world, i - 18, j + 34, j + 34, k + 5, AIR);
        setVerticalLine(world, i - 19, j + 35, j + 35, k + 0, AIR);
        setVerticalLine(world, i - 20, j + 35, j + 35, k + 3, AIR);
        setVerticalLine(world, i - 18, j + 35, j + 35, k - 2, AIR);
        setVerticalLine(world, i - 19, j + 36, j + 36, k - 3, AIR);
        setVerticalLine(world, i - 19, j + 36, j + 36, k - 4, AIR);
        setVerticalLine(world, i - 17, j + 34, j + 34, k - 6, AIR);
        setVerticalLine(world, i + 17, j + 28, j + 28, k - 1, AIR);
        setVerticalLine(world, i + 16, j + 32, j + 32, k - 5, AIR);
        setVerticalLine(world, i + 17, j + 31, j + 31, k - 4, AIR);

        world.setBlock(i - 6, j + 35, k - 18, GLOWING, 0, 2);
        world.setBlock(i + 5, j + 36, k - 17, GLOWING, 0, 2);
        world.setBlock(i + 15, j + 35, k - 8, GLOWING, 0, 2);
        world.setBlock(i + 17, j + 34, k + 0, GLOWING, 0, 2);
        world.setBlock(i + 2, j + 34, k + 18, GLOWING, 0, 2);
        world.setBlock(i - 18, j + 37, k + 6, GLOWING, 0, 2);
    }

    private void generateLayer21(World world, int i, int j, int k) {  
        setVerticalLine(world, i - 5, j + 34, j + 34, k - 18, FRAME);
        setVerticalLine(world, i - 1, j + 36, j + 36, k - 18, AIR);
        setVerticalLine(world, i + 4, j + 36, j + 36, k - 18, AIR);
        setVerticalLine(world, i - 6, j + 33, j + 35, k - 17, FRAME);
        setVerticalLine(world, i - 5, j + 34, j + 36, k - 17, FRAME);
        setVerticalLine(world, i - 4, j + 34, j + 36, k - 17, FRAME);
        setVerticalLine(world, i - 3, j + 35, j + 37, k - 17, FRAME);
        setVerticalLine(world, i - 2, j + 36, j + 38, k - 17, FRAME);
        setVerticalLine(world, i - 1, j + 35, j + 37, k - 17, FRAME);
        setVerticalLine(world, i + 0, j + 35, j + 37, k - 17, FRAME);
        setVerticalLine(world, i + 1, j + 36, j + 38, k - 17, FRAME);
        setVerticalLine(world, i + 1, j + 37, j + 37, k - 18, AIR);
        setVerticalLine(world, i + 2, j + 36, j + 38, k - 17, FRAME);
        setVerticalLine(world, i + 3, j + 36, j + 38, k - 17, FRAME);
        setVerticalLine(world, i + 4, j + 35, j + 37, k - 17, FRAME);
        setVerticalLine(world, i - 7, j + 33, j + 35, k - 16, FRAME);
        setVerticalLine(world, i + 5, j + 34, j + 36, k - 16, FRAME);
        setVerticalLine(world, i + 5, j + 35, j + 36, k - 17, AIR);
        setVerticalLine(world, i - 6, j + 33, j + 35, k - 17, FRAME);
        setVerticalLine(world, i - 5, j + 34, j + 36, k - 17, FRAME);
        setVerticalLine(world, i + 5, j + 35, j + 35, k - 15, AIR);
        setVerticalLine(world, i - 8, j + 32, j + 35, k - 15, FRAME);
        setVerticalLine(world, i - 10, j + 31, j + 31, k - 14, AIR);
        setVerticalLine(world, i - 10, j + 33, j + 35, k - 14, FRAME);
        setVerticalLine(world, i - 9, j + 33, j + 34, k - 14, FRAME);
        setVerticalLine(world, i - 7, j + 33, j + 33, k - 15, AIR);
        setVerticalLine(world, i + 6, j + 33, j + 35, k - 15, FRAME);
        setVerticalLine(world, i - 7, j + 33, j + 35, k - 16, FRAME);
        setVerticalLine(world, i + 7, j + 33, j + 35, k - 14, FRAME);
        setVerticalLine(world, i - 11, j + 33, j + 35, k - 13, FRAME);
        setVerticalLine(world, i + 8, j + 34, j + 36, k - 13, FRAME);
        setVerticalLine(world, i - 12, j + 32, j + 34, k - 12, FRAME);
        setVerticalLine(world, i - 13, j + 31, j + 33, k - 12, FRAME);
        setVerticalLine(world, i + 9, j + 35, j + 36, k - 12, FRAME);
        setVerticalLine(world, i + 10, j + 33, j + 36, k - 12, FRAME);
        setVerticalLine(world, i - 14, j + 35, j + 35, k - 11, FRAME);
        setVerticalLine(world, i + 11, j + 32, j + 35, k - 11, FRAME);

        setVerticalLine(world, i + 12, j + 33, j + 35, k - 10, FRAME);
        setVerticalLine(world, i + 11, j + 34, j + 35, k - 10, FRAME);
        setVerticalLine(world, i + 13, j + 33, j + 35, k - 9, FRAME);
        setVerticalLine(world, i + 12, j + 35, j + 36, k - 9, FRAME);
        setVerticalLine(world, i + 14, j + 34, j + 36, k - 8, FRAME);
        setVerticalLine(world, i + 13, j + 35, j + 36, k - 8, FRAME);
        setVerticalLine(world, i + 15, j + 34, j + 35, k - 7, FRAME);
        setVerticalLine(world, i + 14, j + 35, j + 36, k - 7, FRAME);
        setVerticalLine(world, i + 15, j + 34, j + 36, k - 6, FRAME);
        setVerticalLine(world, i + 16, j + 33, j + 35, k - 5, FRAME);
        setVerticalLine(world, i + 15, j + 35, j + 36, k - 5, FRAME);
        setVerticalLine(world, i + 16, j + 33, j + 35, k - 4, FRAME);
        setVerticalLine(world, i + 17, j + 34, j + 34, k - 4, AIR);
        setVerticalLine(world, i + 15, j + 35, j + 36, k - 4, FRAME);
        setVerticalLine(world, i + 16, j + 33, j + 35, k - 3, FRAME);
        setVerticalLine(world, i + 15, j + 34, j + 35, k - 3, FRAME);
        setVerticalLine(world, i + 16, j + 32, j + 34, k - 2, FRAME);
        setVerticalLine(world, i + 15, j + 34, j + 35, k - 2, FRAME);
        setVerticalLine(world, i + 16, j + 33, j + 35, k - 1, FRAME);
        setVerticalLine(world, i + 15, j + 34, j + 35, k - 1, FRAME);

        setVerticalLine(world, i + 16, j + 33, j + 34, k + 0, FRAME);
        setVerticalLine(world, i + 15, j + 34, j + 35, k + 0, FRAME);
        setVerticalLine(world, i + 16, j + 31, j + 33, k + 1, FRAME);
        setVerticalLine(world, i + 15, j + 33, j + 34, k + 1, FRAME);
        setVerticalLine(world, i + 16, j + 32, j + 34, k + 2, FRAME);
        setVerticalLine(world, i + 15, j + 34, j + 35, k + 2, FRAME);
        setVerticalLine(world, i + 16, j + 30, j + 32, k + 3, FRAME);
        setVerticalLine(world, i + 15, j + 33, j + 34, k + 3, FRAME);
        setVerticalLine(world, i + 16, j + 31, j + 33, k + 4, FRAME);
        setVerticalLine(world, i + 15, j + 33, j + 34, k + 4, FRAME);
        setVerticalLine(world, i + 16, j + 32, j + 32, k + 5, AIR);
        setVerticalLine(world, i + 15, j + 31, j + 33, k + 5, FRAME);
        setVerticalLine(world, i + 14, j + 31, j + 34, k + 6, FRAME);
        setVerticalLine(world, i + 13, j + 30, j + 34, k + 7, FRAME);
        setVerticalLine(world, i + 12, j + 30, j + 33, k + 8, FRAME);
        setVerticalLine(world, i + 11, j + 31, j + 33, k + 9, FRAME);

        setVerticalLine(world, i + 10, j + 33, j + 34, k + 10, FRAME);
        setVerticalLine(world, i + 9, j + 33, j + 34, k + 10, FRAME);
        setVerticalLine(world, i + 8, j + 33, j + 34, k + 11, FRAME);
        setVerticalLine(world, i + 7, j + 34, j + 35, k + 12, FRAME);
        setVerticalLine(world, i + 8, j + 35, j + 35, k + 12, AIR);
        setVerticalLine(world, i + 6, j + 35, j + 36, k + 13, FRAME);
        setVerticalLine(world, i + 5, j + 36, j + 37, k + 13, FRAME);
        setVerticalLine(world, i + 6, j + 33, j + 35, k + 14, FRAME);
        setVerticalLine(world, i + 5, j + 34, j + 36, k + 14, FRAME);
        setVerticalLine(world, i + 4, j + 32, j + 34, k + 15, FRAME);
        setVerticalLine(world, i + 3, j + 34, j + 35, k + 15, FRAME);
        setVerticalLine(world, i - 5, j + 34, j + 35, k + 15, FRAME);
        setVerticalLine(world, i + 3, j + 32, j + 34, k + 16, FRAME);
        setVerticalLine(world, i + 2, j + 34, j + 35, k + 16, FRAME);
        setVerticalLine(world, i + 1, j + 33, j + 34, k + 16, FRAME);
        setVerticalLine(world, i + 0, j + 34, j + 35, k + 16, FRAME);
        setVerticalLine(world, i - 1, j + 35, j + 36, k + 16, FRAME);
        setVerticalLine(world, i - 2, j + 34, j + 35, k + 16, FRAME);
        setVerticalLine(world, i - 3, j + 35, j + 36, k + 16, FRAME);
        setVerticalLine(world, i - 4, j + 34, j + 35, k + 16, FRAME);
        setVerticalLine(world, i - 5, j + 35, j + 35, k + 16, AIR);
        setVerticalLine(world, i - 6, j + 35, j + 36, k + 15, FRAME);
        setVerticalLine(world, i - 7, j + 35, j + 36, k + 15, FRAME);
        setVerticalLine(world, i - 8, j + 35, j + 36, k + 15, FRAME);
        setVerticalLine(world, i - 9, j + 36, j + 37, k + 15, FRAME);
        setVerticalLine(world, i - 10, j + 36, j + 37, k + 15, FRAME);
        setVerticalLine(world, i - 11, j + 36, j + 37, k + 14, FRAME);
        setVerticalLine(world, i - 12, j + 35, j + 36, k + 13, FRAME);
        setVerticalLine(world, i + 2, j + 32, j + 34, k + 17, FRAME);
        setVerticalLine(world, i + 1, j + 31, j + 33, k + 17, FRAME);
        setVerticalLine(world, i + 1, j + 33, j + 33, k + 18, AIR);
        setVerticalLine(world, i + 0, j + 33, j + 34, k + 17, FRAME);
        setVerticalLine(world, i - 1, j + 33, j + 35, k + 17, FRAME);
        setVerticalLine(world, i - 2, j + 32, j + 34, k + 17, FRAME);
        setVerticalLine(world, i - 3, j + 33, j + 35, k + 17, FRAME);
        setVerticalLine(world, i - 4, j + 32, j + 34, k + 17, FRAME);
        setVerticalLine(world, i - 5, j + 32, j + 34, k + 16, FRAME);
        setVerticalLine(world, i - 6, j + 33, j + 35, k + 16, FRAME);
        setVerticalLine(world, i - 7, j + 32, j + 35, k + 16, FRAME);
        setVerticalLine(world, i - 8, j + 34, j + 36, k + 16, FRAME);
        setVerticalLine(world, i + 0, j + 33, j + 33, k + 18, FRAME);
        setVerticalLine(world, i - 2, j + 32, j + 32, k + 18, FRAME);

        setVerticalLine(world, i - 18, j + 37, j + 38, k - 2, FRAME);
        setVerticalLine(world, i - 17, j + 38, j + 39, k - 2, FRAME);
        setVerticalLine(world, i - 18, j + 37, j + 38, k - 1, FRAME);
        setVerticalLine(world, i - 17, j + 39, j + 40, k - 1, FRAME);
        setVerticalLine(world, i - 16, j + 40, j + 40, k - 1, FRAME);
        setVerticalLine(world, i - 16, j + 40, j + 40, k - 2, FRAME);
        setVerticalLine(world, i - 18, j + 37, j + 39, k + 0, FRAME);
        setVerticalLine(world, i - 17, j + 38, j + 39, k + 0, FRAME);
        setVerticalLine(world, i - 16, j + 40, j + 40, k + 0, FRAME);
        setVerticalLine(world, i - 18, j + 36, j + 39, k + 1, FRAME);
        setVerticalLine(world, i - 17, j + 38, j + 38, k + 1, FRAME);
        setVerticalLine(world, i - 18, j + 37, j + 38, k + 2, FRAME);
        setVerticalLine(world, i - 17, j + 38, j + 38, k + 2, FRAME);
        setVerticalLine(world, i - 18, j + 36, j + 37, k + 3, FRAME);
        setVerticalLine(world, i - 18, j + 36, j + 37, k + 4, FRAME);
        setVerticalLine(world, i - 17, j + 37, j + 38, k + 5, FRAME);
        setVerticalLine(world, i - 17, j + 35, j + 36, k + 6, FRAME);
        setVerticalLine(world, i - 17, j + 35, j + 36, k + 7, FRAME);
        setVerticalLine(world, i - 16, j + 34, j + 35, k + 8, FRAME);
        setVerticalLine(world, i - 15, j + 34, j + 35, k + 9, FRAME);
        setVerticalLine(world, i - 14, j + 36, j + 36, k + 10, FRAME);
        setVerticalLine(world, i - 13, j + 35, j + 36, k + 11, FRAME);
        setVerticalLine(world, i - 13, j + 35, j + 36, k + 12, FRAME);

        setVerticalLine(world, i - 19, j + 33, j + 33, k + 1, FRAME);
        setVerticalLine(world, i - 14, j + 36, j + 36, k + 9, AIR);
        setVerticalLine(world, i - 13, j + 35, j + 35, k + 11, AIR);
        setVerticalLine(world, i - 10, j + 37, j + 37, k + 16, AIR);
        setVerticalLine(world, i - 6, j + 54, j + 54, k - 3, FRAME);
        world.setBlock(i - 6, j + 54, k - 3, FRAME, 2, 2);
        setVerticalLine(world, i - 7, j + 52, j + 52, k - 2, AIR);
        setVerticalLine(world, i - 7, j + 52, j + 52, k + 1, AIR);
        setVerticalLine(world, i - 7, j + 54, j + 54, k - 3, AIR);
        setVerticalLine(world, i - 7, j + 54, j + 54, k + 2, AIR);

        world.setBlock(i - 2, j + 39, k - 17, GLOWING, 0, 2);
        world.setBlock(i + 12, j + 37, k - 9, GLOWING, 0, 2);
        world.setBlock(i + 13, j + 35, k + 7, GLOWING, 0, 2);
        world.setBlock(i + 5, j + 38, k + 13, GLOWING, 0, 2);
        world.setBlock(i - 17, j + 41, k - 1, GLOWING, 0, 2);
     }

    private void generateLayer22(World world, int i, int j, int k) {   
        setVerticalLine(world, i - 4, j + 36, j + 36, k - 16, FRAME);
        setVerticalLine(world, i - 4, j + 37, j + 37, k - 17, FRAME);
        setVerticalLine(world, i - 4, j + 37, j + 37, k - 16, FRAME);
        setVerticalLine(world, i - 4, j + 37, j + 37, k - 17, AIR);
        setVerticalLine(world, i - 5, j + 36, j + 37, k - 16, FRAME);
        setVerticalLine(world, i - 6, j + 35, j + 36, k - 16, FRAME);
        setVerticalLine(world, i - 7, j + 35, j + 36, k - 15, FRAME);
        setVerticalLine(world, i - 8, j + 34, j + 35, k - 14, FRAME);
        setVerticalLine(world, i - 10, j + 34, j + 35, k - 13, FRAME);
        setVerticalLine(world, i - 9, j + 34, j + 35, k - 13, FRAME);
        setVerticalLine(world, i - 11, j + 34, j + 35, k - 12, FRAME);
        setVerticalLine(world, i - 12, j + 34, j + 35, k - 11, FRAME);
        setVerticalLine(world, i - 13, j + 33, j + 34, k - 11, FRAME);
        setVerticalLine(world, i - 13, j + 34, j + 35, k - 10, FRAME);

        setVerticalLine(world, i - 14, j + 34, j + 35, k - 9, FRAME);
        setVerticalLine(world, i - 15, j + 35, j + 36, k - 8, FRAME);
        setVerticalLine(world, i - 16, j + 35, j + 36, k - 7, FRAME);
        setVerticalLine(world, i - 16, j + 37, j + 38, k - 6, FRAME);
        setVerticalLine(world, i - 15, j + 37, j + 37, k - 6, AIR);
        setVerticalLine(world, i - 17, j + 36, j + 37, k - 5, FRAME);
        setVerticalLine(world, i - 17, j + 37, j + 38, k - 4, FRAME);
        setVerticalLine(world, i - 18, j + 38, j + 38, k - 4, AIR);

        setVerticalLine(world, i - 15, j + 40, j + 41, k - 2, FRAME);
        setVerticalLine(world, i - 15, j + 40, j + 42, k - 1, FRAME);
        setVerticalLine(world, i - 15, j + 41, j + 41, k + 0, FRAME);
        setVerticalLine(world, i - 16, j + 38, j + 41, k + 1, FRAME);
        setVerticalLine(world, i - 16, j + 39, j + 40, k + 2, FRAME);
        setVerticalLine(world, i - 17, j + 38, j + 39, k + 3, FRAME);
        setVerticalLine(world, i - 17, j + 38, j + 40, k + 4, FRAME);
        setVerticalLine(world, i - 16, j + 38, j + 39, k + 5, FRAME);
        setVerticalLine(world, i - 16, j + 36, j + 38, k + 6, FRAME);
        setVerticalLine(world, i - 16, j + 37, j + 38, k + 7, FRAME);
        setVerticalLine(world, i - 15, j + 36, j + 37, k + 8, FRAME);
        setVerticalLine(world, i - 14, j + 36, j + 37, k + 9, FRAME);

        setVerticalLine(world, i - 13, j + 36, j + 37, k + 10, FRAME);
        setVerticalLine(world, i - 12, j + 36, j + 37, k + 11, FRAME);
        setVerticalLine(world, i - 12, j + 36, j + 37, k + 12, FRAME);
        setVerticalLine(world, i - 13, j + 37, j + 37, k + 12, AIR);
        setVerticalLine(world, i - 11, j + 36, j + 37, k + 13, FRAME);
        setVerticalLine(world, i - 10, j + 37, j + 38, k + 14, FRAME);
        setVerticalLine(world, i - 9, j + 37, j + 38, k + 14, FRAME);
        setVerticalLine(world, i - 8, j + 36, j + 37, k + 14, FRAME);
        setVerticalLine(world, i - 7, j + 36, j + 37, k + 14, FRAME);
        setVerticalLine(world, i - 6, j + 36, j + 37, k + 14, FRAME);
        setVerticalLine(world, i - 5, j + 35, j + 36, k + 14, FRAME);
        setVerticalLine(world, i - 4, j + 35, j + 36, k + 15, FRAME);
        setVerticalLine(world, i - 3, j + 36, j + 37, k + 15, FRAME);
        setVerticalLine(world, i - 2, j + 35, j + 36, k + 15, FRAME);
        setVerticalLine(world, i - 1, j + 36, j + 37, k + 15, FRAME);
        setVerticalLine(world, i + 0, j + 35, j + 36, k + 15, FRAME);
        setVerticalLine(world, i + 1, j + 34, j + 35, k + 15, FRAME);
        setVerticalLine(world, i + 2, j + 35, j + 36, k + 15, FRAME);

        setVerticalLine(world, i + 7, j + 36, j + 37, k - 13, FRAME);
        setVerticalLine(world, i + 6, j + 36, j + 37, k - 13, FRAME);
        setVerticalLine(world, i + 5, j + 36, j + 37, k - 14, FRAME);
        setVerticalLine(world, i + 4, j + 36, j + 37, k - 15, FRAME);
        setVerticalLine(world, i + 4, j + 36, j + 36, k - 15, AIR);
        setVerticalLine(world, i + 4, j + 36, j + 36, k - 14, AIR);
        setVerticalLine(world, i + 3, j + 38, j + 39, k - 15, FRAME);
        setVerticalLine(world, i + 3, j + 39, j + 39, k - 16, AIR);
        setVerticalLine(world, i + 2, j + 38, j + 39, k - 15, FRAME);
        setVerticalLine(world, i + 0, j + 37, j + 38, k - 15, FRAME);
        setVerticalLine(world, i - 1, j + 38, j + 39, k - 15, FRAME);
        setVerticalLine(world, i - 2, j + 38, j + 39, k - 15, FRAME);
        setVerticalLine(world, i - 3, j + 37, j + 38, k - 15, FRAME);
        setVerticalLine(world, i - 4, j + 37, j + 38, k - 15, FRAME);
        setVerticalLine(world, i - 5, j + 37, j + 38, k - 15, FRAME);
        setVerticalLine(world, i - 6, j + 36, j + 37, k - 15, FRAME);
        setVerticalLine(world, i - 7, j + 36, j + 37, k - 14, FRAME);
        setVerticalLine(world, i - 8, j + 35, j + 36, k - 13, FRAME);
        setVerticalLine(world, i - 9, j + 35, j + 36, k - 12, FRAME);
        setVerticalLine(world, i - 10, j + 35, j + 36, k - 12, FRAME);
        setVerticalLine(world, i - 10, j + 36, j + 36, k - 13, AIR);
        setVerticalLine(world, i - 11, j + 35, j + 37, k - 11, FRAME);
        setVerticalLine(world, i - 11, j + 35, j + 35, k - 11, AIR);
        setVerticalLine(world, i - 12, j + 35, j + 36, k - 10, FRAME);
        setVerticalLine(world, i - 12, j + 35, j + 35, k - 10, AIR);
        setVerticalLine(world, i - 13, j + 35, j + 36, k - 9, FRAME);
        setVerticalLine(world, i - 14, j + 36, j + 37, k - 8, FRAME);
        setVerticalLine(world, i - 15, j + 36, j + 37, k - 7, FRAME);

        setVerticalLine(world, i - 16, j + 37, j + 38, k - 5, FRAME);
        setVerticalLine(world, i + 3, j + 35, j + 37, k + 14, FRAME);
        setVerticalLine(world, i + 4, j + 36, j + 36, k + 14, AIR);
        setVerticalLine(world, i + 4, j + 36, j + 37, k + 13, FRAME);
        setVerticalLine(world, i + 5, j + 35, j + 35, k + 13, AIR);
        setVerticalLine(world, i + 6, j + 35, j + 36, k + 12, FRAME);
        setVerticalLine(world, i + 6, j + 35, j + 36, k + 11, FRAME);
        setVerticalLine(world, i + 7, j + 34, j + 36, k + 11, FRAME);
        setVerticalLine(world, i + 7, j + 36, j + 37, k + 10, FRAME);
        setVerticalLine(world, i + 8, j + 34, j + 36, k + 10, FRAME);
        setVerticalLine(world, i + 8, j + 35, j + 36, k + 9, FRAME);
        setVerticalLine(world, i + 9, j + 34, j + 36, k + 9, FRAME);
        setVerticalLine(world, i + 9, j + 35, j + 36, k + 8, FRAME);
        setVerticalLine(world, i + 10, j + 34, j + 35, k + 8, FRAME);
        setVerticalLine(world, i + 10, j + 34, j + 35, k + 7, FRAME);
        setVerticalLine(world, i + 11, j + 33, j + 34, k + 7, FRAME);
        setVerticalLine(world, i + 11, j + 34, j + 36, k + 6, FRAME);
        setVerticalLine(world, i + 12, j + 34, j + 38, k + 6, FRAME);
        setVerticalLine(world, i + 12, j + 36, j + 38, k + 5, FRAME);
        setVerticalLine(world, i + 12, j + 34, j + 34, k + 5, AIR);
        setVerticalLine(world, i + 13, j + 34, j + 35, k + 5, FRAME);
        setVerticalLine(world, i + 13, j + 35, j + 37, k + 4, FRAME);
        setVerticalLine(world, i + 13, j + 36, j + 38, k + 3, FRAME);
        setVerticalLine(world, i + 14, j + 34, j + 36, k + 4, FRAME);
        setVerticalLine(world, i + 14, j + 34, j + 35, k + 3, FRAME);
        setVerticalLine(world, i + 14, j + 36, j + 36, k + 3, FRAME);
        setVerticalLine(world, i + 13, j + 36, j + 38, k + 2, FRAME);
        setVerticalLine(world, i + 14, j + 36, j + 36, k + 2, FRAME);
        setVerticalLine(world, i + 13, j + 35, j + 38, k + 1, FRAME);
        setVerticalLine(world, i + 13, j + 35, j + 35, k + 1, AIR);
        setVerticalLine(world, i + 14, j + 34, j + 36, k + 1, FRAME);
        setVerticalLine(world, i + 14, j + 35, j + 37, k + 0, FRAME);

        setVerticalLine(world, i + 12, j + 36, j + 37, k - 8, FRAME);
        setVerticalLine(world, i + 13, j + 36, j + 37, k - 7, FRAME);
        setVerticalLine(world, i + 14, j + 36, j + 37, k - 6, FRAME);
        setVerticalLine(world, i + 14, j + 36, j + 37, k - 5, FRAME);
        setVerticalLine(world, i + 14, j + 36, j + 37, k - 4, FRAME);
        setVerticalLine(world, i + 14, j + 35, j + 36, k - 3, FRAME);
        setVerticalLine(world, i + 14, j + 35, j + 36, k - 2, FRAME);
        setVerticalLine(world, i + 13, j + 36, j + 38, k - 2, FRAME);
        setVerticalLine(world, i + 13, j + 36, j + 38, k - 1, FRAME);
        setVerticalLine(world, i + 14, j + 35, j + 37, k - 1, FRAME);

        setVerticalLine(world, i - 15, j + 37, j + 40, k + 6, FRAME);
        setVerticalLine(world, i - 14, j + 37, j + 39, k + 7, FRAME);
        setVerticalLine(world, i - 14, j + 37, j + 38, k + 8, FRAME);
        setVerticalLine(world, i - 13, j + 37, j + 38, k + 9, FRAME);
        setVerticalLine(world, i - 12, j + 37, j + 38, k + 10, FRAME);
        setVerticalLine(world, i - 11, j + 37, j + 38, k + 12, FRAME);
        setVerticalLine(world, i - 10, j + 38, j + 39, k + 13, FRAME);
        setVerticalLine(world, i - 9, j + 38, j + 39, k + 13, FRAME);
        setVerticalLine(world, i - 8, j + 37, j + 38, k + 13, FRAME);
        setVerticalLine(world, i - 7, j + 37, j + 38, k + 13, FRAME);
        setVerticalLine(world, i - 6, j + 37, j + 38, k + 13, FRAME);
        setVerticalLine(world, i - 5, j + 37, j + 38, k + 13, FRAME);

        setVerticalLine(world, i + 0, j + 40, j + 40, k + 0, FRAME);
        setVerticalLine(world, i - 1, j + 43, j + 44, k + 1, AIR);
        setVerticalLine(world, i + 1, j + 47, j + 47, k + 1, FRAME);
        setVerticalLine(world, i + 0, j + 48, j + 48, k + 2, FRAME);
        setVerticalLine(world, i + 2, j + 47, j + 47, k + 0, FRAME);
        setVerticalLine(world, i + 2, j + 49, j + 49, k - 1, FRAME);
        setVerticalLine(world, i + 2, j + 49, j + 49, k + 1, FRAME);
        setVerticalLine(world, i - 3, j + 49, j + 49, k - 3, FRAME);
        setVerticalLine(world, i - 1, j + 50, j + 50, k - 6, FRAME);
        setVerticalLine(world, i - 1, j + 50, j + 50, k - 5, FRAME);
        setVerticalLine(world, i + 0, j + 50, j + 50, k - 4, FRAME);
        setVerticalLine(world, i - 6, j + 50, j + 50, k - 2, FRAME);
        setVerticalLine(world, i - 5, j + 50, j + 50, k - 3, FRAME);
        setVerticalLine(world, i - 4, j + 50, j + 50, k + 1, FRAME);
        setVerticalLine(world, i - 3, j + 50, j + 50, k + 1, FRAME);
        setVerticalLine(world, i - 3, j + 50, j + 50, k + 2, FRAME);
        setVerticalLine(world, i - 2, j + 50, j + 50, k + 2, FRAME);
        setVerticalLine(world, i + 1, j + 50, j + 50, k + 2, FRAME);
        setVerticalLine(world, i + 0, j + 50, j + 50, k + 3, FRAME);
        setVerticalLine(world, i + 0, j + 50, j + 50, k + 4, FRAME);
        setVerticalLine(world, i - 1, j + 50, j + 50, k + 3, FRAME);
        setVerticalLine(world, i + 2, j + 50, j + 50, k + 1, FRAME);
        setVerticalLine(world, i + 3, j + 50, j + 50, k + 0, FRAME);
        setVerticalLine(world, i + 3, j + 50, j + 50, k + 1, AIR);
        setVerticalLine(world, i + 4, j + 50, j + 50, k + 0, FRAME);
        setVerticalLine(world, i - 8, j + 51, j + 51, k - 5, FRAME);
        setVerticalLine(world, i - 8, j + 51, j + 51, k - 4, FRAME);
        setVerticalLine(world, i - 7, j + 51, j + 51, k - 5, FRAME);
        setVerticalLine(world, i - 6, j + 51, j + 51, k - 5, FRAME);
        setVerticalLine(world, i - 8, j + 51, j + 51, k + 4, FRAME);
        setVerticalLine(world, i - 7, j + 51, j + 51, k + 4, FRAME);
        setVerticalLine(world, i - 6, j + 51, j + 51, k + 4, FRAME);

        setVerticalLine(world, i - 15, j + 42, j + 43, k - 3, AIR);
        setVerticalLine(world, i - 14, j + 43, j + 43, k - 3, AIR);
        setVerticalLine(world, i - 13, j + 44, j + 47, k - 3, AIR);
        setVerticalLine(world, i - 12, j + 47, j + 50, k - 3, AIR);
        setVerticalLine(world, i - 11, j + 48, j + 52, k - 3, AIR);
        setVerticalLine(world, i - 10, j + 48, j + 49, k - 3, AIR);
        setVerticalLine(world, i - 9, j + 49, j + 49, k - 3, AIR);
        setVerticalLine(world, i - 8, j + 49, j + 49, k - 3, AIR);
        setVerticalLine(world, i - 3, j + 41, j + 43, k - 1, AIR);
        setVerticalLine(world, i + 1, j + 39, j + 41, k - 1, AIR);
        setVerticalLine(world, i + 0, j + 40, j + 40, k + 0, AIR);
        setVerticalLine(world, i - 15, j + 42, j + 42, k + 0, AIR);
        setVerticalLine(world, i - 14, j + 40, j + 40, k + 6, AIR);
        setVerticalLine(world, i - 11, j + 37, j + 37, k + 11, AIR);
        setVerticalLine(world, i - 9, j + 38, j + 38, k + 13, AIR);
        setVerticalLine(world, i - 7, j + 37, j + 37, k + 13, AIR);
        setVerticalLine(world, i - 11, j + 38, j + 38, k + 14, AIR);

        world.setBlock(i - 5, j + 38, k - 16, GLOWING, 0, 2);
        world.setBlock(i - 17, j + 39, k - 4, GLOWING, 0, 2);
        world.setBlock(i - 15, j + 43, k - 1, GLOWING, 0, 2);
        world.setBlock(i - 3, j + 38, k + 15, GLOWING, 0, 2);
        world.setBlock(i + 3, j + 40, k - 15, GLOWING, 0, 2);
        world.setBlock(i + 12, j + 39, k + 6, GLOWING, 0, 2);
     }

    private void generateLayer23(World world, int i, int j, int k) {   
        setVerticalLine(world, i + 13, j + 36, j + 38, k - 3, FRAME);
        setVerticalLine(world, i + 12, j + 37, j + 37, k - 3, AIR);
        setVerticalLine(world, i + 13, j + 37, j + 38, k - 4, FRAME);
        setVerticalLine(world, i + 13, j + 37, j + 38, k - 5, FRAME);
        setVerticalLine(world, i + 13, j + 37, j + 38, k - 6, FRAME);
        setVerticalLine(world, i + 12, j + 37, j + 38, k - 7, FRAME);
        setVerticalLine(world, i + 11, j + 36, j + 37, k - 9, FRAME);
        setVerticalLine(world, i + 10, j + 36, j + 36, k - 10, FRAME);
        setVerticalLine(world, i + 9, j + 36, j + 36, k - 10, AIR);
        setVerticalLine(world, i + 10, j + 37, j + 37, k - 11, FRAME);
        setVerticalLine(world, i + 9, j + 37, j + 38, k - 11, FRAME);

        setVerticalLine(world, i + 7, j + 37, j + 38, k - 12, FRAME);
        setVerticalLine(world, i + 7, j + 38, j + 38, k - 13, AIR);
        setVerticalLine(world, i + 8, j + 38, j + 38, k - 11, FRAME);
        setVerticalLine(world, i + 6, j + 37, j + 38, k - 12, FRAME);
        setVerticalLine(world, i + 5, j + 37, j + 38, k - 13, FRAME);
        setVerticalLine(world, i + 5, j + 37, j + 37, k - 13, AIR);
        setVerticalLine(world, i + 4, j + 38, j + 39, k - 14, FRAME);

        setVerticalLine(world, i + 1, j + 39, j + 40, k - 15, FRAME);
        setVerticalLine(world, i + 0, j + 39, j + 39, k - 15, FRAME);
        setVerticalLine(world, i - 6, j + 37, j + 38, k - 14, FRAME);
        setVerticalLine(world, i - 5, j + 38, j + 38, k - 14, FRAME);
        setVerticalLine(world, i - 5, j + 39, j + 39, k - 15, AIR);
        setVerticalLine(world, i - 5, j + 39, j + 39, k - 14, FRAME);
        setVerticalLine(world, i - 7, j + 37, j + 38, k - 13, FRAME);
        setVerticalLine(world, i - 8, j + 37, j + 39, k - 12, FRAME);
        setVerticalLine(world, i - 9, j + 36, j + 38, k - 11, FRAME);
        setVerticalLine(world, i - 10, j + 37, j + 38, k - 11, FRAME);

        setVerticalLine(world, i - 11, j + 37, j + 38, k - 10, FRAME);
        setVerticalLine(world, i - 12, j + 36, j + 37, k - 9, FRAME);
        setVerticalLine(world, i - 12, j + 37, j + 37, k - 8, AIR);
        setVerticalLine(world, i - 13, j + 37, j + 38, k - 8, FRAME);
        setVerticalLine(world, i - 14, j + 37, j + 38, k - 7, FRAME);
        setVerticalLine(world, i - 15, j + 38, j + 40, k - 6, FRAME);
        setVerticalLine(world, i - 14, j + 38, j + 38, k - 6, AIR);

        setVerticalLine(world, i - 16, j + 39, j + 39, k - 5, FRAME);
        setVerticalLine(world, i - 16, j + 38, j + 40, k - 4, FRAME);
        setVerticalLine(world, i - 17, j + 39, j + 39, k - 4, FRAME);
        setVerticalLine(world, i - 15, j + 39, j + 40, k - 4, FRAME);
        setVerticalLine(world, i - 15, j + 39, j + 39, k - 4, AIR);
        setVerticalLine(world, i - 15, j + 40, j + 40, k - 5, FRAME);
        setVerticalLine(world, i - 15, j + 42, j + 42, k - 1, FRAME);
        setVerticalLine(world, i - 14, j + 42, j + 43, k - 1, FRAME);

        setVerticalLine(world, i - 14, j + 41, j + 43, k - 2, FRAME);
        setVerticalLine(world, i - 15, j + 42, j + 42, k - 3, FRAME);
        setVerticalLine(world, i - 14, j + 41, j + 42, k - 3, FRAME);

        setVerticalLine(world, i - 14, j + 40, j + 41, k - 5, FRAME);
        setVerticalLine(world, i - 15, j + 40, j + 41, k - 4, FRAME);
        setVerticalLine(world, i - 14, j + 39, j + 41, k - 6, FRAME);
        setVerticalLine(world, i - 13, j + 39, j + 40, k - 7, FRAME);

        setVerticalLine(world, i - 12, j + 38, j + 40, k - 8, FRAME);
        setVerticalLine(world, i - 11, j + 38, j + 40, k - 9, FRAME);
        setVerticalLine(world, i - 10, j + 38, j + 41, k - 10, FRAME);
        setVerticalLine(world, i - 9, j + 39, j + 39, k - 11, FRAME);
        setVerticalLine(world, i - 8, j + 39, j + 40, k - 11, FRAME);
        setVerticalLine(world, i - 7, j + 39, j + 41, k - 12, FRAME);
        setVerticalLine(world, i - 6, j + 38, j + 39, k - 13, FRAME);
        setVerticalLine(world, i - 5, j + 39, j + 40, k - 13, FRAME);
        setVerticalLine(world, i - 4, j + 38, j + 40, k - 14, FRAME);

        setVerticalLine(world, i - 2, j + 39, j + 41, k - 14, FRAME);
        setVerticalLine(world, i - 2, j + 40, j + 40, k - 15, FRAME);
        setVerticalLine(world, i - 1, j + 40, j + 42, k - 14, FRAME);
        setVerticalLine(world, i + 0, j + 40, j + 43, k - 14, FRAME);
        setVerticalLine(world, i + 0, j + 42, j + 42, k - 13, AIR);
        setVerticalLine(world, i + 1, j + 40, j + 41, k - 15, FRAME);
        setVerticalLine(world, i + 2, j + 39, j + 41, k - 14, FRAME);
        setVerticalLine(world, i + 3, j + 39, j + 42, k - 14, FRAME);
        setVerticalLine(world, i + 4, j + 39, j + 41, k - 13, FRAME);
        setVerticalLine(world, i + 5, j + 38, j + 41, k - 12, FRAME);
        setVerticalLine(world, i + 5, j + 38, j + 38, k - 12, AIR);
        setVerticalLine(world, i + 7, j + 38, j + 39, k - 11, FRAME);
        setVerticalLine(world, i + 6, j + 38, j + 39, k - 11, FRAME);
        setVerticalLine(world, i + 6, j + 38, j + 38, k - 11, AIR);

        setVerticalLine(world, i + 9, j + 38, j + 39, k - 10, FRAME);
        setVerticalLine(world, i + 8, j + 39, j + 40, k - 10, FRAME);
        setVerticalLine(world, i + 10, j + 37, j + 38, k - 9, FRAME);
        setVerticalLine(world, i + 10, j + 37, j + 37, k - 9, AIR);
        setVerticalLine(world, i + 11, j + 37, j + 39, k - 8, FRAME);
        setVerticalLine(world, i + 11, j + 37, j + 37, k - 8, AIR);
        setVerticalLine(world, i + 12, j + 38, j + 40, k - 5, FRAME);
        setVerticalLine(world, i + 12, j + 38, j + 38, k - 5, AIR);
        setVerticalLine(world, i + 12, j + 38, j + 39, k - 6, FRAME);

        setVerticalLine(world, i + 12, j + 39, j + 40, k - 4, FRAME);
        setVerticalLine(world, i + 12, j + 38, j + 39, k - 3, FRAME);
        setVerticalLine(world, i + 12, j + 38, j + 39, k - 2, FRAME);
        setVerticalLine(world, i + 12, j + 37, j + 39, k - 1, FRAME);
        setVerticalLine(world, i + 12, j + 38, j + 40, k + 0, FRAME);
        setVerticalLine(world, i + 13, j + 39, j + 39, k + 0, FRAME);
        setVerticalLine(world, i + 12, j + 39, j + 40, k + 1, FRAME);
        setVerticalLine(world, i + 12, j + 40, j + 41, k + 2, FRAME);
        setVerticalLine(world, i + 11, j + 40, j + 40, k + 2, AIR);
        setVerticalLine(world, i + 13, j + 39, j + 39, k + 2, FRAME);
        setVerticalLine(world, i + 12, j + 38, j + 40, k + 3, FRAME);
        setVerticalLine(world, i + 12, j + 38, j + 41, k + 4, FRAME);
        setVerticalLine(world, i + 11, j + 38, j + 39, k + 5, FRAME);
        setVerticalLine(world, i + 11, j + 37, j + 39, k + 6, FRAME);
        setVerticalLine(world, i + 10, j + 37, j + 40,  k + 6, FRAME);

        setVerticalLine(world, i + 10, j + 36, j + 36, k + 7, FRAME);
        setVerticalLine(world, i + 9, j + 36, j + 39, k + 7, FRAME);
        setVerticalLine(world, i + 8, j + 36, j + 38, k + 8, FRAME);
        setVerticalLine(world, i + 7, j + 37, j + 39, k + 9, FRAME);
        setVerticalLine(world, i + 6, j + 37, j + 38, k + 10, FRAME);
        setVerticalLine(world, i + 5, j + 37, j + 39, k + 11, FRAME);
        setVerticalLine(world, i + 5, j + 37, j + 39, k + 12, FRAME);
        setVerticalLine(world, i + 4, j + 38, j + 40, k + 12, FRAME);
        setVerticalLine(world, i + 3, j + 38, j + 39, k + 13, FRAME);
        setVerticalLine(world, i + 2, j + 37, j + 40, k + 13, FRAME);

        setVerticalLine(world, i + 1, j + 38, j + 39, k + 13, FRAME);
        setVerticalLine(world, i + 0, j + 37, j + 39, k + 13, FRAME);
        setVerticalLine(world, i - 1, j + 38, j + 40, k + 13, FRAME);
        setVerticalLine(world, i - 2, j + 38, j + 40, k + 13, FRAME);
        setVerticalLine(world, i - 3, j + 39, j + 40, k + 13, FRAME);
        setVerticalLine(world, i - 3, j + 39, j + 39, k + 14, FRAME);
        setVerticalLine(world, i - 4, j + 38, j + 39, k + 13, FRAME);
        setVerticalLine(world, i - 5, j + 38, j + 40, k + 12, FRAME);
        setVerticalLine(world, i - 6, j + 38, j + 40, k + 12, FRAME);
        setVerticalLine(world, i - 7, j + 39, j + 40, k + 12, FRAME);
        setVerticalLine(world, i - 8, j + 38, j + 39, k + 12, FRAME);
        setVerticalLine(world, i - 9, j + 39, j + 40, k + 12, FRAME);
        setVerticalLine(world, i - 10, j + 38, j + 40, k + 12, FRAME);

        setVerticalLine(world, i - 10, j + 39, j + 40, k + 11, FRAME);
        setVerticalLine(world, i - 9, j + 40, j + 40, k + 11, AIR);
        setVerticalLine(world, i - 11, j + 39, j + 41, k + 10, FRAME);
        setVerticalLine(world, i - 12, j + 39, j + 40, k + 9, FRAME);
        setVerticalLine(world, i - 13, j + 38, j + 40, k + 8, FRAME);
        setVerticalLine(world, i - 13, j + 39, j + 40, k + 7, FRAME);

        setVerticalLine(world, i - 14, j + 39, j + 41, k + 6, FRAME);
        setVerticalLine(world, i - 13, j + 41, j + 41, k + 6, AIR);
        setVerticalLine(world, i - 15, j + 39, j + 40, k + 5, FRAME);
        setVerticalLine(world, i - 14, j + 40, j + 40, k + 5, AIR);
        setVerticalLine(world, i - 15, j + 41, j + 41, k + 4, FRAME);
        setVerticalLine(world, i - 16, j + 41, j + 41, k + 4, FRAME);
        setVerticalLine(world, i - 15, j + 41, j + 42, k + 3, FRAME);
        setVerticalLine(world, i - 15, j + 41, j + 41, k + 3, AIR);
        setVerticalLine(world, i - 15, j + 41, j + 42, k + 2, FRAME);
        setVerticalLine(world, i - 16, j + 41, j + 41, k + 2, FRAME);
        setVerticalLine(world, i - 15, j + 41, j + 43, k + 1, FRAME);
        setVerticalLine(world, i - 14, j + 42, j + 42, k + 1, AIR);

        setVerticalLine(world, i - 15, j + 42, j + 43, k + 0, FRAME);
        setVerticalLine(world, i - 18, j + 27, j + 29, k + 10, FRAME);
        setVerticalLine(world, i - 17, j + 27, j + 30, k + 10, FRAME);
        setVerticalLine(world, i - 17, j + 27, j + 27, k + 10, GLOWING);
        setVerticalLine(world, i - 17, j + 29, j + 29, k + 11, FRAME);
        setVerticalLine(world, i - 17, j + 28, j + 28, k + 11, FRAME);

        setVerticalLine(world, i - 4, j + 41, j + 41, k - 14, FRAME);
        setVerticalLine(world, i - 4, j + 41, j + 42, k - 13, FRAME);
        setVerticalLine(world, i - 3, j + 40, j + 41, k - 13, FRAME);
        setVerticalLine(world, i - 2, j + 41, j + 42, k - 13, FRAME);
        setVerticalLine(world, i - 1, j + 42, j + 43, k - 13, FRAME);
        setVerticalLine(world, i + 0, j + 43, j + 44, k - 13, FRAME);
        setVerticalLine(world, i + 1, j + 41, j + 43, k - 14, FRAME);
        setVerticalLine(world, i + 1, j + 42, j + 42, k - 15, FRAME);
        setVerticalLine(world, i + 2, j + 41, j + 43, k - 13, FRAME);
        setVerticalLine(world, i + 3, j + 42, j + 43, k - 13, FRAME);
        setVerticalLine(world, i + 4, j + 41, j + 43, k - 12, FRAME);
        setVerticalLine(world, i + 5, j + 41, j + 42, k - 11, FRAME);
        setVerticalLine(world, i + 6, j + 40, j + 41, k - 10, FRAME);
        setVerticalLine(world, i + 7, j + 40, j + 41, k - 10, FRAME);
        setVerticalLine(world, i + 8, j + 40, j + 40, k - 9, FRAME);

        setVerticalLine(world, i - 5, j + 51, j + 51, k + 5, FRAME);
        setVerticalLine(world, i - 6, j + 51, j + 51, k + 5, FRAME);
        setVerticalLine(world, i - 5, j + 51, j + 54, k - 6, FRAME);
        setVerticalLine(world, i - 6, j + 51, j + 54, k - 5, FRAME);
        setVerticalLine(world, i - 4, j + 51, j + 54, k - 7, FRAME);
        setVerticalLine(world, i - 3, j + 51, j + 51, k - 8, FRAME);
        setVerticalLine(world, i - 2, j + 51, j + 51, k - 8, FRAME);
        setVerticalLine(world, i - 1, j + 51, j + 51, k - 8, FRAME);
        setVerticalLine(world, i + 0, j + 51, j + 51, k - 8, FRAME);
        setVerticalLine(world, i - 3, j + 51, j + 51, k - 8, AIR);

        world.setBlock(i + 18, j + 23, k - 2, GLOWING, 0, 2);
        world.setBlock(i + 3, j + 24, k + 18, GLOWING, 0, 2);

        world.setBlock(i + 9, j + 39, k - 11, GLOWING, 0, 2);
        world.setBlock(i + 4, j + 40, k - 14, GLOWING, 0, 2);
        world.setBlock(i - 15, j + 41, k - 6, GLOWING, 0, 2);
        world.setBlock(i - 14, j + 44, k - 1, GLOWING, 0, 2);
        world.setBlock(i + 0, j + 45, k - 13, GLOWING, 0, 2);
        world.setBlock(i + 10, j + 41, k + 6, GLOWING, 0, 2);
     }

    private void generateLayer24(World world, int i, int j, int k) {  
        setVerticalLine(world, i + 8, j + 41, j + 41, k - 10, FRAME);
        setVerticalLine(world, i + 8, j + 41, j + 41, k - 9, FRAME);
        setVerticalLine(world, i + 9, j + 39, j + 40, k - 9, FRAME);
        setVerticalLine(world, i + 10, j + 39, j + 40, k - 8, FRAME);
        setVerticalLine(world, i + 11, j + 39, j + 40, k - 7, FRAME);
        setVerticalLine(world, i + 11, j + 40, j + 41, k - 6, FRAME);
        setVerticalLine(world, i + 12, j + 40, j + 40, k - 6, FRAME);
        setVerticalLine(world, i + 11, j + 40, j + 41, k - 5, FRAME);
        setVerticalLine(world, i + 11, j + 39, j + 41, k - 4, FRAME);
        setVerticalLine(world, i + 11, j + 40, j + 41, k - 3, FRAME);
        setVerticalLine(world, i + 12, j + 40, j + 40, k - 3, FRAME);
        setVerticalLine(world, i + 11, j + 38, j + 38, k - 3, AIR);

        setVerticalLine(world, i - 14, j + 41, j + 42, k - 4, FRAME);
        setVerticalLine(world, i - 13, j + 41, j + 43, k - 6, FRAME);
        setVerticalLine(world, i - 13, j + 42, j + 42, k - 5, FRAME);
        setVerticalLine(world, i - 12, j + 40, j + 41, k - 7, FRAME);
        setVerticalLine(world, i - 11, j + 40, j + 41, k - 8, FRAME);
        setVerticalLine(world, i - 10, j + 41, j + 42, k - 9, FRAME);
        setVerticalLine(world, i - 9, j + 40, j + 41, k - 10, FRAME);
        setVerticalLine(world, i - 9, j + 40, j + 40, k - 11, FRAME);
        setVerticalLine(world, i - 8, j + 41, j + 41, k - 11, FRAME);
        setVerticalLine(world, i - 7, j + 41, j + 42, k - 11, FRAME);
        setVerticalLine(world, i - 6, j + 40, j + 41, k - 12, FRAME);
        setVerticalLine(world, i - 5, j + 40, j + 41, k - 12, FRAME);
        setVerticalLine(world, i - 5, j + 41, j + 41, k - 13, FRAME);

        setVerticalLine(world, i + 9, j + 39, j + 43, k - 8, FRAME);
        setVerticalLine(world, i + 9, j + 39, j + 39, k - 8, AIR);
        setVerticalLine(world, i + 10, j + 40, j + 42, k - 7, FRAME);
        setVerticalLine(world, i + 10, j + 41, j + 42, k - 6, FRAME);
        setVerticalLine(world, i + 10, j + 41, j + 42, k - 5, FRAME);
        setVerticalLine(world, i + 10, j + 41, j + 43, k - 4, FRAME);
        setVerticalLine(world, i + 10, j + 41, j + 42, k - 3, FRAME);
        setVerticalLine(world, i + 11, j + 39, j + 41, k - 2, FRAME);
        setVerticalLine(world, i + 11, j + 39, j + 41, k - 1, FRAME);
        setVerticalLine(world, i + 11, j + 39, j + 39, k - 1, AIR);
        setVerticalLine(world, i + 12, j + 40, j + 40, k - 1, AIR);
        setVerticalLine(world, i + 11, j + 40, j + 41, k + 0, FRAME);

        setVerticalLine(world, i + 11, j + 41, j + 42, k + 1, FRAME);
        setVerticalLine(world, i + 12, j + 41, j + 41, k + 1, FRAME);
        setVerticalLine(world, i + 11, j + 41, j + 42, k + 2, FRAME);
        setVerticalLine(world, i + 11, j + 41, j + 41, k + 2, AIR);
        setVerticalLine(world, i + 11, j + 40, j + 41, k + 3, FRAME);
        setVerticalLine(world, i + 10, j + 40, j + 42, k + 4, FRAME);
        setVerticalLine(world, i + 10, j + 40, j + 40, k + 4, AIR);
        setVerticalLine(world, i + 9, j + 41, j + 41, k + 4, AIR);
        setVerticalLine(world, i + 9, j + 40, j + 41, k + 5, FRAME);
        setVerticalLine(world, i + 9, j + 40, j + 41, k + 6, FRAME);
        setVerticalLine(world, i + 8, j + 39, j + 41, k + 7, FRAME);
        setVerticalLine(world, i + 7, j + 39, j + 40, k + 8, FRAME);
        setVerticalLine(world, i + 7, j + 39, j + 39, k + 8, AIR);

        setVerticalLine(world, i + 6, j + 39, j + 40, k + 9, FRAME);
        setVerticalLine(world, i + 5, j + 39, j + 41, k + 10, FRAME);
        setVerticalLine(world, i + 5, j + 39, j + 39, k + 11, FRAME);
        setVerticalLine(world, i + 4, j + 40, j + 41, k + 11, FRAME);
        setVerticalLine(world, i + 3, j + 40, j + 41, k + 12, FRAME);
        setVerticalLine(world, i + 2, j + 40, j + 41, k + 12, FRAME);
        setVerticalLine(world, i + 1, j + 40, j + 41, k + 12, FRAME);
        setVerticalLine(world, i + 1, j + 40, j + 40, k + 13, FRAME);
        setVerticalLine(world, i + 0, j + 39, j + 41, k + 12, FRAME);
        setVerticalLine(world, i - 1, j + 40, j + 41, k + 12, FRAME);
        setVerticalLine(world, i - 2, j + 40, j + 41, k + 12, FRAME);
        setVerticalLine(world, i - 3, j + 40, j + 41, k + 12, FRAME);
        setVerticalLine(world, i - 3, j + 41, j + 41, k + 13, FRAME);
        setVerticalLine(world, i - 4, j + 40, j + 41, k + 12, FRAME);
        setVerticalLine(world, i - 5, j + 41, j + 41, k + 12, FRAME);
        setVerticalLine(world, i - 6, j + 40, j + 41, k + 11, FRAME);
        setVerticalLine(world, i - 7, j + 40, j + 40, k + 11, FRAME);
        setVerticalLine(world, i - 8, j + 39, j + 41, k + 11, FRAME);
        setVerticalLine(world, i - 8, j + 40, j + 40, k + 12, FRAME);

        setVerticalLine(world, i - 13, j + 42, j + 45, k - 4, FRAME);
        setVerticalLine(world, i - 12, j + 42, j + 44, k - 5, FRAME);
        setVerticalLine(world, i - 12, j + 42, j + 43, k - 6, FRAME);
        setVerticalLine(world, i - 11, j + 42, j + 42, k - 8, FRAME);
        setVerticalLine(world, i - 11, j + 41, j + 42, k - 7, FRAME);
        setVerticalLine(world, i - 10, j + 42, j + 43, k - 8, FRAME);
        setVerticalLine(world, i - 9, j + 41, j + 42, k - 9, FRAME);
        setVerticalLine(world, i - 8, j + 41, j + 42, k - 10, FRAME);
        setVerticalLine(world, i - 7, j + 42, j + 43, k - 10, FRAME);
        setVerticalLine(world, i - 6, j + 42, j + 43, k - 11, FRAME);
        setVerticalLine(world, i - 6, j + 42, j + 42, k - 12, FRAME);
        setVerticalLine(world, i - 5, j + 42, j + 44, k - 11, FRAME);
        setVerticalLine(world, i - 5, j + 42, j + 43, k - 12, FRAME);
        setVerticalLine(world, i - 4, j + 42, j + 43, k - 12, FRAME);
        setVerticalLine(world, i - 3, j + 42, j + 42, k - 13, FRAME);
        setVerticalLine(world, i - 3, j + 42, j + 44, k - 12, FRAME);
        setVerticalLine(world, i - 3, j + 42, j + 42, k - 12, AIR);

        setVerticalLine(world, i + 1, j + 42, j + 44, k - 13, FRAME);
        setVerticalLine(world, i + 1, j + 44, j + 44, k - 12, FRAME);
        setVerticalLine(world, i + 2, j + 43, j + 44, k - 12, FRAME);
        setVerticalLine(world, i + 3, j + 44, j + 44, k - 12, FRAME);
        setVerticalLine(world, i + 4, j + 43, j + 43, k - 11, FRAME);
        setVerticalLine(world, i + 5, j + 42, j + 43, k - 10, FRAME);
        setVerticalLine(world, i + 5, j + 43, j + 43, k - 11, AIR);
        setVerticalLine(world, i + 6, j + 41, j + 42, k - 9, FRAME);
        setVerticalLine(world, i + 7, j + 42, j + 43, k - 9, FRAME);
        setVerticalLine(world, i + 8, j + 42, j + 42, k - 8, FRAME);

        setVerticalLine(world, i + 9, j + 41, j + 43, k - 7, FRAME);
        setVerticalLine(world, i + 9, j + 42, j + 44, k - 6, FRAME);
        setVerticalLine(world, i + 9, j + 42, j + 43, k - 5, FRAME);
        setVerticalLine(world, i + 9, j + 42, j + 44, k - 4, FRAME);
        setVerticalLine(world, i + 9, j + 43, j + 44, k - 3, FRAME);
        setVerticalLine(world, i + 10, j + 42, j + 43, k - 5, FRAME);
        setVerticalLine(world, i + 10, j + 43, j + 43, k - 4, AIR);
        setVerticalLine(world, i + 10, j + 41, j + 42, k - 3, FRAME);

        setVerticalLine(world, i + 10, j + 41, j + 42, k - 2, FRAME);
        setVerticalLine(world, i + 10, j + 41, j + 41, k - 2, AIR);
        setVerticalLine(world, i + 11, j + 41, j + 41, k - 2, FRAME);
        setVerticalLine(world, i + 10, j + 42, j + 42, k - 1, FRAME);
        setVerticalLine(world, i + 10, j + 41, j + 42, k + 0, FRAME);
        setVerticalLine(world, i + 10, j + 42, j + 43, k + 1, FRAME);
        setVerticalLine(world, i + 9, j + 43, j + 43, k + 1, AIR);
        setVerticalLine(world, i + 10, j + 42, j + 43, k + 2, FRAME);
        setVerticalLine(world, i + 10, j + 42, j + 43, k + 3, FRAME);
        setVerticalLine(world, i + 11, j + 42, j + 42, k + 3, FRAME);
        setVerticalLine(world, i + 9, j + 42, j + 43, k + 4, FRAME);

        setVerticalLine(world, i + 8, j + 41, j + 43, k + 6, FRAME);
        setVerticalLine(world, i + 7, j + 42, j + 43, k + 7, FRAME);
        setVerticalLine(world, i + 6, j + 40, j + 42, k + 8, FRAME);
        setVerticalLine(world, i + 5, j + 41, j + 43, k + 9, FRAME);
        setVerticalLine(world, i + 4, j + 41, j + 42, k + 10, FRAME);
        setVerticalLine(world, i + 3, j + 41, j + 43, k + 11, FRAME);

        setVerticalLine(world, i + 2, j + 42, j + 44, k + 11, FRAME);
        setVerticalLine(world, i + 1, j + 41, j + 43, k + 11, FRAME);
        setVerticalLine(world, i + 0, j + 41, j + 43, k + 11, FRAME);
        setVerticalLine(world, i + 0, j + 41, j + 41, k + 11, AIR);
        setVerticalLine(world, i - 1, j + 43, j + 43, k + 11, FRAME);
        setVerticalLine(world, i - 2, j + 41, j + 43, k + 11, FRAME);
        setVerticalLine(world, i - 2, j + 41, j + 41, k + 11, AIR);
        setVerticalLine(world, i - 3, j + 41, j + 43, k + 11, FRAME);
        setVerticalLine(world, i - 3, j + 42, j + 42, k + 12, FRAME);
        setVerticalLine(world, i - 4, j + 42, j + 44, k + 11, FRAME);
        setVerticalLine(world, i - 4, j + 43, j + 43, k + 12, AIR);

        setVerticalLine(world, i - 13, j + 41, j + 43, k + 6, FRAME);
        setVerticalLine(world, i - 13, j + 41, j + 41, k + 7, FRAME);
        setVerticalLine(world, i - 12, j + 41, j + 43, k + 7, FRAME);
        setVerticalLine(world, i - 12, j + 40, j + 42, k + 8, FRAME);
        setVerticalLine(world, i - 11, j + 41, j + 42, k + 9, FRAME);
        setVerticalLine(world, i - 10, j + 41, j + 43, k + 10, FRAME);
        setVerticalLine(world, i - 9, j + 41, j + 42, k + 11, FRAME);

        setVerticalLine(world, i - 11, j + 44, j + 46, k - 5, FRAME);
        setVerticalLine(world, i - 10, j + 44, j + 45, k - 6, FRAME);
        setVerticalLine(world, i - 10, j + 45, j + 45, k - 7, FRAME);
        setVerticalLine(world, i - 9, j + 44, j + 46, k - 7, FRAME);
        setVerticalLine(world, i - 8, j + 44, j + 45, k - 8, FRAME);
        setVerticalLine(world, i - 7, j + 43, j + 45, k - 9, FRAME);
        setVerticalLine(world, i - 6, j + 43, j + 45, k - 10, FRAME);
        setVerticalLine(world, i - 5, j + 45, j + 46, k - 10, FRAME);
        setVerticalLine(world, i - 4, j + 44, j + 46, k - 11, FRAME);
        setVerticalLine(world, i - 3, j + 45, j + 46, k - 11, FRAME);
        setVerticalLine(world, i - 2, j + 44, j + 46, k - 11, FRAME);
        setVerticalLine(world, i - 2, j + 44, j + 45, k - 12, FRAME);
        setVerticalLine(world, i + 0, j + 44, j + 45, k - 12, FRAME);

        setVerticalLine(world, i - 14, j + 43, j + 43, k + 2, FRAME);
        setVerticalLine(world, i - 14, j + 43, j + 43, k + 3, FRAME);
        setVerticalLine(world, i - 14, j + 42, j + 43, k + 4, FRAME);
        setVerticalLine(world, i - 14, j + 42, j + 42, k + 5, FRAME);
        setVerticalLine(world, i - 13, j + 43, j + 44, k + 4, FRAME);
        setVerticalLine(world, i - 13, j + 44, j + 44, k + 5, AIR);
        setVerticalLine(world, i - 12, j + 44, j + 44, k + 4, AIR);
        setVerticalLine(world, i - 12, j + 43, j + 44, k + 6, FRAME);
        setVerticalLine(world, i - 11, j + 43, j + 44, k + 8, FRAME);
        setVerticalLine(world, i - 11, j + 43, j + 43, k + 7, AIR);
        setVerticalLine(world, i - 10, j + 43, j + 44, k + 9, FRAME);
        setVerticalLine(world, i - 9, j + 43, j + 44, k + 10, FRAME);

        setVerticalLine(world, i - 13, j + 42, j + 44, k - 3, FRAME);
        setVerticalLine(world, i - 12, j + 43, j + 43, k - 3, AIR);
        setVerticalLine(world, i - 12, j + 42, j + 44, k - 5, FRAME);
        setVerticalLine(world, i - 11, j + 43, j + 43, k - 6, FRAME);
        setVerticalLine(world, i - 10, j + 42, j + 44, k - 7, FRAME);
        setVerticalLine(world, i - 9, j + 43, j + 44, k - 8, FRAME);
        setVerticalLine(world, i - 8, j + 42, j + 43, k - 9, FRAME);
        setVerticalLine(world, i - 7, j + 43, j + 43, k - 10, FRAME);
        setVerticalLine(world, i - 2, j + 43, j + 43, k - 13, FRAME);
        setVerticalLine(world, i - 2, j + 42, j + 43, k - 12, FRAME);
        setVerticalLine(world, i - 1, j + 43, j + 44, k - 12, FRAME);

        setVerticalLine(world, i - 13, j + 43, j + 45, k - 1, FRAME);
        setVerticalLine(world, i - 13, j + 43, j + 45, k - 2, FRAME);
        setVerticalLine(world, i - 13, j + 43, j + 44, k + 0, FRAME);
        setVerticalLine(world, i - 14, j + 43, j + 45, k + 1, FRAME);
        setVerticalLine(world, i - 13, j + 44, j + 45, k + 1, FRAME);
        setVerticalLine(world, i - 12, j + 45, j + 46, k - 2, FRAME);
        setVerticalLine(world, i - 12, j + 45, j + 46, k - 3, FRAME);
        setVerticalLine(world, i - 12, j + 45, j + 46, k - 4, FRAME);

        setVerticalLine(world, i + 3, j + 42, j + 44, k + 10, FRAME);
        setVerticalLine(world, i + 1, j + 42, j + 43, k + 10, FRAME);
        setVerticalLine(world, i - 2, j + 43, j + 43, k + 10, FRAME);
        setVerticalLine(world, i + 0, j + 43, j + 43, k + 10, FRAME);
        setVerticalLine(world, i - 4, j + 44, j + 44, k + 10, FRAME);
        setVerticalLine(world, i - 6, j + 41, j + 43, k + 10, FRAME);
        setVerticalLine(world, i - 7, j + 41, j + 42, k + 10, FRAME);
        setVerticalLine(world, i - 8, j + 42, j + 43, k + 10, FRAME);
        setVerticalLine(world, i - 5, j + 42, j + 43, k + 10, FRAME);
        setVerticalLine(world, i - 5, j + 42, j + 42, k + 11, FRAME);

        world.setBlock(i + 11, j + 42, k - 3, GLOWING, 0, 2);
        world.setBlock(i - 13, j + 44, k - 6, GLOWING, 0, 2);
        world.setBlock(i + 10, j + 44, k - 4, GLOWING, 0, 2);
        world.setBlock(i + 8, j + 42, k + 7, GLOWING, 0, 2);
        world.setBlock(i + 2, j + 42, k + 12, GLOWING, 0, 2);
        world.setBlock(i - 11, j + 47, k - 5, GLOWING, 0, 2);
        world.setBlock(i - 12, j + 47, k - 2, GLOWING, 0, 2);
     }

    private void generateLayer25(World world, int i, int j, int k) {
        setVerticalLine(world, i + 0, j + 45, j + 45, k - 11, FRAME);
        setVerticalLine(world, i - 1, j + 45, j + 45, k - 11, FRAME);
        setVerticalLine(world, i + 1, j + 45, j + 45, k - 12, FRAME);
        setVerticalLine(world, i + 1, j + 45, j + 47, k - 10, FRAME);
        setVerticalLine(world, i + 2, j + 45, j + 48, k - 10, FRAME);
        setVerticalLine(world, i + 3, j + 45, j + 47, k - 10, FRAME);
        setVerticalLine(world, i + 4, j + 44, j + 46, k - 10, FRAME);
        setVerticalLine(world, i + 4, j + 44, j + 45, k - 9, FRAME);
        setVerticalLine(world, i + 5, j + 43, j + 47, k - 8, FRAME);
        setVerticalLine(world, i + 6, j + 42, j + 45, k - 7, FRAME);

        setVerticalLine(world, i - 1, j + 45, j + 48, k - 10, FRAME);
        setVerticalLine(world, i + 0, j + 46, j + 47, k - 10, FRAME);
        setVerticalLine(world, i + 0, j + 47, j + 49, k - 9, FRAME);
        setVerticalLine(world, i + 1, j + 45, j + 49, k - 9, FRAME);
        setVerticalLine(world, i + 2, j + 47, j + 50, k - 9, FRAME);
        setVerticalLine(world, i + 3, j + 47, j + 47, k - 9, FRAME);
        setVerticalLine(world, i - 2, j + 45, j + 47, k - 10, FRAME);
        setVerticalLine(world, i - 3, j + 46, j + 48, k - 10, FRAME);
        setVerticalLine(world, i - 4, j + 46, j + 48, k - 10, FRAME);
        setVerticalLine(world, i - 5, j + 47, j + 47, k - 9, FRAME);
        setVerticalLine(world, i - 6, j + 45, j + 47, k - 9, FRAME);
        setVerticalLine(world, i - 7, j + 46, j + 48, k - 8, FRAME);
        setVerticalLine(world, i - 8, j + 46, j + 46, k - 8, FRAME);
        setVerticalLine(world, i - 9, j + 47, j + 47, k - 7, FRAME);
        setVerticalLine(world, i - 10, j + 46, j + 47, k - 6, FRAME);

        setVerticalLine(world, i + 5, j + 43, j + 44, k - 9, FRAME);
        setVerticalLine(world, i + 6, j + 42, j + 44, k - 8, FRAME);
        setVerticalLine(world, i + 7, j + 43, j + 47, k - 7, FRAME);
        setVerticalLine(world, i + 8, j + 42, j + 44, k - 7, FRAME);
        setVerticalLine(world, i + 8, j + 43, j + 46, k - 6, FRAME);
        setVerticalLine(world, i + 8, j + 43, j + 47, k - 5, FRAME);
        setVerticalLine(world, i + 8, j + 44, j + 45, k - 4, FRAME);
        setVerticalLine(world, i + 8, j + 44, j + 45, k - 3, FRAME);
        setVerticalLine(world, i + 8, j + 43, j + 45, k - 2, FRAME);

        setVerticalLine(world, i + 9, j + 42, j + 44, k - 2, FRAME);
        setVerticalLine(world, i + 9, j + 43, j + 45, k - 1, FRAME);
        setVerticalLine(world, i + 9, j + 43, j + 44, k + 0, FRAME);
        setVerticalLine(world, i + 9, j + 44, j + 45, k + 1, FRAME);
        setVerticalLine(world, i + 9, j + 43, j + 44, k + 2, FRAME);
        setVerticalLine(world, i + 9, j + 43, j + 44, k + 3, FRAME);
        setVerticalLine(world, i + 8, j + 43, j + 45, k + 4, FRAME);
        setVerticalLine(world, i + 8, j + 43, j + 45, k + 5, FRAME);
        setVerticalLine(world, i + 7, j + 42, j + 46, k + 5, FRAME);

        setVerticalLine(world, i + 7, j + 42, j + 44, k + 6, FRAME);
        setVerticalLine(world, i + 6, j + 44, j + 46, k + 6, FRAME);
        setVerticalLine(world, i + 5, j + 45, j + 47, k + 7, FRAME);
        setVerticalLine(world, i + 4, j + 42, j + 47, k + 8, FRAME);
        setVerticalLine(world, i + 3, j + 43, j + 47, k + 9, FRAME);
        setVerticalLine(world, i + 2, j + 44, j + 47, k + 9, FRAME);
        setVerticalLine(world, i + 1, j + 43, j + 45, k + 9, FRAME);
        setVerticalLine(world, i + 0, j + 44, j + 46, k + 10, FRAME);

        setVerticalLine(world, i - 13, j + 45, j + 45, k - 3, FRAME);
        setVerticalLine(world, i - 12, j + 45, j + 46, k - 1, FRAME);
        setVerticalLine(world, i - 12, j + 45, j + 46, k + 0, FRAME);
        setVerticalLine(world, i - 12, j + 45, j + 46, k + 2, FRAME);
        setVerticalLine(world, i - 12, j + 45, j + 47, k + 4, FRAME);
        setVerticalLine(world, i - 11, j + 45, j + 48, k + 5, FRAME);
        setVerticalLine(world, i - 11, j + 45, j + 47, k + 6, FRAME);
        setVerticalLine(world, i - 10, j + 45, j + 47, k + 7, FRAME);
        setVerticalLine(world, i - 9, j + 45, j + 46, k + 7, FRAME);
        setVerticalLine(world, i - 8, j + 45, j + 46, k + 8, FRAME);
        setVerticalLine(world, i - 7, j + 45, j + 46, k + 8, FRAME);
        setVerticalLine(world, i - 6, j + 45, j + 46, k + 8, FRAME);
        setVerticalLine(world, i - 5, j + 45, j + 46, k + 8, FRAME);

        setVerticalLine(world, i - 11, j + 46, j + 48, k - 4, FRAME);
        setVerticalLine(world, i - 10, j + 46, j + 47, k - 5, FRAME);
        setVerticalLine(world, i - 9, j + 47, j + 47, k - 6, FRAME);
        setVerticalLine(world, i - 8, j + 46, j + 47, k - 7, FRAME);
        setVerticalLine(world, i - 7, j + 46, j + 48, k - 8, FRAME);
        setVerticalLine(world, i - 6, j + 47, j + 47, k - 9, FRAME);

        setVerticalLine(world, i - 4, j + 47, j + 48, k - 10, FRAME);
        setVerticalLine(world, i - 3, j + 48, j + 49, k - 10, FRAME);
        setVerticalLine(world, i - 2, j + 48, j + 49, k - 9, FRAME);
        setVerticalLine(world, i - 1, j + 47, j + 49, k - 9, FRAME);
        setVerticalLine(world, i + 0, j + 48, j + 50, k - 8, FRAME);
        setVerticalLine(world, i + 1, j + 50, j + 50, k - 8, FRAME);
        setVerticalLine(world, i + 2, j + 49, j + 50, k - 8, FRAME);
        setVerticalLine(world, i - 11, j + 47, j + 48, k - 2, FRAME);
        setVerticalLine(world, i - 10, j + 48, j + 50, k - 2, FRAME);
        setVerticalLine(world, i - 9, j + 49, j + 50, k - 1, FRAME);

        setVerticalLine(world, i - 8, j + 48, j + 50, k - 6, FRAME);
        setVerticalLine(world, i - 7, j + 48, j + 50, k - 7, FRAME);
        setVerticalLine(world, i - 6, j + 48, j + 50, k - 8, FRAME);
        setVerticalLine(world, i - 5, j + 48, j + 50, k - 8, FRAME);
        setVerticalLine(world, i - 4, j + 49, j + 50, k - 8, FRAME);
        setVerticalLine(world, i - 11, j + 48, j + 49, k + 1, FRAME);
        setVerticalLine(world, i - 10, j + 49, j + 50, k + 0, FRAME);

        setVerticalLine(world, i + 0, j + 46, j + 46, k - 11, AIR);
        setVerticalLine(world, i + 6, j + 43, j + 43, k - 7, AIR);
        setVerticalLine(world, i + 8, j + 42, j + 42, k - 5, AIR);
        setVerticalLine(world, i + 8, j + 43, j + 43, k - 4, AIR);
        setVerticalLine(world, i + 10, j + 43, j + 43, k - 2, AIR);
        setVerticalLine(world, i + 5, j + 43, j + 43, k + 7, AIR);
        setVerticalLine(world, i - 11, j + 45, j + 45, k + 5, AIR);
        setVerticalLine(world, i - 12, j + 45, j + 45, k + 4, AIR);
        setVerticalLine(world, i - 13, j + 46, j + 46, k + 1, AIR);
        setVerticalLine(world, i - 12, j + 45, j + 45, k - 3, AIR);
        setVerticalLine(world, i - 12, j + 45, j + 45, k - 2, AIR);
        setVerticalLine(world, i - 10, j + 47, j + 47, k - 1, AIR);
        setVerticalLine(world, i - 11, j + 48, j + 48, k + 0, AIR);
        setVerticalLine(world, i - 10, j + 46, j + 46, k - 2, AIR);
        setVerticalLine(world, i - 7, j + 47, j + 47, k - 7, AIR);
        setVerticalLine(world, i - 1, j + 46, j + 46, k - 11, AIR);
        setVerticalLine(world, i + 7, j + 44, j + 44, k - 6, AIR);
        setVerticalLine(world, i + 9, j + 45, j + 45, k + 2, AIR);
        setVerticalLine(world, i + 5, j + 44, j + 44, k + 6, AIR);
        setVerticalLine(world, i + 0, j + 46, j + 46, k + 8, AIR);
        setVerticalLine(world, i - 5, j + 44, j + 44, k + 8, AIR);
        setVerticalLine(world, i - 8, j + 45, j + 45, k + 8, AIR);
        setVerticalLine(world, i - 9, j + 45, j + 45, k + 7, AIR);
        setVerticalLine(world, i - 10, j + 44, j + 44, k + 8, AIR);
        setVerticalLine(world, i - 10, j + 48, j + 48, k + 1, AIR);
        setVerticalLine(world, i - 10, j + 48, j + 48, k - 1, AIR);
        setVerticalLine(world, i - 9, j + 49, j + 49, k - 3, AIR);
        setVerticalLine(world, i - 8, j + 50, j + 50, k - 5, AIR);
        setVerticalLine(world, i - 2, j + 47, j + 47, k - 9, AIR);
        setVerticalLine(world, i - 3, j + 50, j + 50, k - 5, AIR);
        setVerticalLine(world, i + 0, j + 47, j + 47, k - 9, AIR);

        world.setBlock(i + 2, j + 49, k - 10, GLOWING, 0, 2);
        world.setBlock(i + 2, j + 51, k - 9, GLOWING, 0, 2);
        world.setBlock(i + 7, j + 48, k - 7, GLOWING, 0, 2);
        world.setBlock(i + 7, j + 47, k + 5, GLOWING, 0, 2);
        world.setBlock(i + 4, j + 48, k + 8, GLOWING, 0, 2);
        world.setBlock(i - 11, j + 49, k + 5, GLOWING, 0, 2);
        world.setBlock(i + 0, j + 51, k - 8, GLOWING, 0, 2);
        world.setBlock(i - 7, j + 51, k - 7, GLOWING, 0, 2);
        }

    private void generateLayer26(World world, int i, int j, int k) {   
        setVerticalLine(world, i + 3, j + 48, j + 48, k - 9, FRAME);
        setVerticalLine(world, i + 3, j + 48, j + 50, k - 8, FRAME);
        setVerticalLine(world, i + 3, j + 50, j + 51, k - 7, FRAME);
        setVerticalLine(world, i + 4, j + 46, j + 48, k - 8, FRAME);
        setVerticalLine(world, i + 4, j + 48, j + 50, k - 7, FRAME);
        setVerticalLine(world, i + 4, j + 49, j + 50, k - 6, FRAME);
        setVerticalLine(world, i + 5, j + 46, j + 47, k - 7, FRAME);
        setVerticalLine(world, i + 5, j + 47, j + 47, k - 6, FRAME);
        setVerticalLine(world, i + 5, j + 48, j + 50, k - 5, FRAME);
        setVerticalLine(world, i + 6, j + 45, j + 46, k - 6, FRAME);
        setVerticalLine(world, i + 6, j + 46, j + 48, k - 5, FRAME);
        setVerticalLine(world, i + 6, j + 46, j + 46, k - 7, FRAME);

        setVerticalLine(world, i + 5, j + 48, j + 50, k - 4, FRAME);
        setVerticalLine(world, i + 5, j + 48, j + 50, k - 1, FRAME);
        setVerticalLine(world, i + 6, j + 47, j + 50, k - 4, FRAME);
        setVerticalLine(world, i + 6, j + 47, j + 50, k - 2, FRAME);
        setVerticalLine(world, i + 6, j + 48, j + 49, k - 3, FRAME);
        setVerticalLine(world, i + 7, j + 47, j + 47, k - 4, FRAME);
        setVerticalLine(world, i + 7, j + 47, j + 48, k - 1, FRAME);

        setVerticalLine(world, i + 5, j + 50, j + 50, k + 0, FRAME);
        setVerticalLine(world, i + 5, j + 49, j + 50, k + 1, FRAME);
        setVerticalLine(world, i + 5, j + 50, j + 50, k + 2, FRAME);
        setVerticalLine(world, i + 5, j + 50, j + 50, k + 3, FRAME);
        setVerticalLine(world, i + 5, j + 47, j + 49, k + 4, FRAME);
        setVerticalLine(world, i + 6, j + 48, j + 50, k + 0, FRAME);
        setVerticalLine(world, i + 6, j + 49, j + 50, k + 1, FRAME);
        setVerticalLine(world, i + 6, j + 49, j + 50, k + 3, FRAME);
        setVerticalLine(world, i + 6, j + 47, j + 49, k + 4, FRAME);
        setVerticalLine(world, i + 7, j + 47, j + 48, k + 0, FRAME);
        setVerticalLine(world, i + 7, j + 47, j + 48, k + 1, FRAME);
        setVerticalLine(world, i + 7, j + 47, j + 48, k + 2, FRAME);
        setVerticalLine(world, i + 7, j + 47, j + 48, k + 3, FRAME);
        setVerticalLine(world, i + 8, j + 47, j + 47, k + 0, FRAME);
        setVerticalLine(world, i + 8, j + 47, j + 47, k + 2, FRAME);

        setVerticalLine(world, i + 0, j + 47, j + 50, k + 8, FRAME);
        setVerticalLine(world, i + 1, j + 46, j + 49, k + 7, FRAME);
        setVerticalLine(world, i + 1, j + 50, j + 51, k + 6, FRAME);
        setVerticalLine(world, i + 2, j + 48, j + 50, k + 7, FRAME);
        setVerticalLine(world, i + 2, j + 50, j + 51, k + 6, FRAME);
        setVerticalLine(world, i + 3, j + 47, j + 51, k + 6, FRAME);
        setVerticalLine(world, i + 3, j + 47, j + 48, k + 7, FRAME);
        setVerticalLine(world, i + 4, j + 47, j + 50, k + 5, FRAME);
        setVerticalLine(world, i + 5, j + 47, j + 48, k + 5, FRAME);
        setVerticalLine(world, i - 1, j + 46, j + 48, k + 8, FRAME);
        setVerticalLine(world, i - 1, j + 46, j + 46, k + 9, FRAME);
        setVerticalLine(world, i - 2, j + 47, j + 50, k + 8, FRAME);
        setVerticalLine(world, i - 3, j + 46, j + 50, k + 8, FRAME);
        setVerticalLine(world, i - 4, j + 46, j + 50, k + 8, FRAME);
        setVerticalLine(world, i - 5, j + 47, j + 50, k + 7, FRAME);

        setVerticalLine(world, i - 7, j + 51, j + 51, k - 6, FRAME);
        setVerticalLine(world, i - 6, j + 51, j + 51, k - 7, FRAME);
        setVerticalLine(world, i - 5, j + 51, j + 51, k - 7, FRAME);
        setVerticalLine(world, i - 4, j + 51, j + 51, k - 8, FRAME);
        setVerticalLine(world, i - 3, j + 51, j + 51, k - 9, FRAME);
        setVerticalLine(world, i - 2, j + 51, j + 51, k - 9, FRAME);
        setVerticalLine(world, i - 1, j + 51, j + 51, k - 9, FRAME);
        setVerticalLine(world, i + 0, j + 51, j + 54, k - 8, FRAME);
        setVerticalLine(world, i + 1, j + 51, j + 51, k - 9, FRAME);
        setVerticalLine(world, i + 2, j + 51, j + 54, k - 7, FRAME);
        setVerticalLine(world, i + 3, j + 51, j + 55, k - 7, FRAME);
        setVerticalLine(world, i + 4, j + 51, j + 54, k - 6, FRAME);
        setVerticalLine(world, i + 5, j + 51, j + 51, k - 8, FRAME);
        setVerticalLine(world, i + 6, j + 51, j + 51, k - 7, FRAME);

        setVerticalLine(world, i + 4, j + 51, j + 55, k - 5, FRAME);
        setVerticalLine(world, i + 5, j + 51, j + 54, k - 4, FRAME);
        setVerticalLine(world, i + 5, j + 51, j + 54, k - 3, FRAME);
        setVerticalLine(world, i + 5, j + 51, j + 54, k - 2, FRAME);
        setVerticalLine(world, i + 5, j + 51, j + 53, k - 1, FRAME);
        setVerticalLine(world, i + 5, j + 52, j + 55, k + 0, FRAME);
        setVerticalLine(world, i + 6, j + 51, j + 51, k - 5, FRAME);

        setVerticalLine(world, i + 5, j + 51, j + 53, k + 1, FRAME);
        setVerticalLine(world, i + 5, j + 51, j + 54, k + 2, FRAME);
        setVerticalLine(world, i + 5, j + 51, j + 53, k + 3, FRAME);
        setVerticalLine(world, i + 4, j + 51, j + 53, k + 4, FRAME);
        setVerticalLine(world, i + 5, j + 51, j + 51, k + 5, FRAME);
        setVerticalLine(world, i + 0, j + 51, j + 53, k + 6, FRAME);
        setVerticalLine(world, i + 1, j + 51, j + 54, k + 6, FRAME);
        setVerticalLine(world, i + 2, j + 51, j + 53, k + 6, FRAME);
        setVerticalLine(world, i + 3, j + 51, j + 54, k + 5, FRAME);
        setVerticalLine(world, i - 1, j + 51, j + 54, k + 7, FRAME);

        setVerticalLine(world, i - 8, j + 51, j + 51, k - 8, FRAME);
        setVerticalLine(world, i - 7, j + 51, j + 51, k - 9, FRAME);
        setVerticalLine(world, i - 6, j + 51, j + 51, k - 9, FRAME);
        setVerticalLine(world, i - 5, j + 51, j + 51, k - 9, FRAME);
        setVerticalLine(world, i - 5, j + 55, j + 55, k - 6, FRAME);

        setVerticalLine(world, i - 11, j + 47, j + 48, k + 2, FRAME);
        setVerticalLine(world, i - 10, j + 47, j + 50, k + 4, FRAME);
        setVerticalLine(world, i - 9, j + 46, j + 50, k + 6, FRAME);
        setVerticalLine(world, i - 8, j + 46, j + 50, k + 7, FRAME);
        setVerticalLine(world, i - 7, j + 46, j + 50, k + 7, FRAME);

        setVerticalLine(world, i + 4, j + 47, j + 47, k - 6, AIR);
        setVerticalLine(world, i + 6, j + 47, j + 47, k - 4, AIR);
        setVerticalLine(world, i + 6, j + 47, j + 47, k + 4, AIR);
        setVerticalLine(world, i + 4, j + 47, j + 47, k + 6, AIR);
        setVerticalLine(world, i + 3, j + 50, j + 50, k + 7, AIR);
        setVerticalLine(world, i + 2, j + 49, j + 50, k + 6, AIR);
        setVerticalLine(world, i - 4, j + 49, j + 49, k + 6, AIR);
        setVerticalLine(world, i - 5, j + 49, j + 49, k + 5, AIR);
        setVerticalLine(world, i - 7, j + 46, j + 46, k + 7, AIR);
        setVerticalLine(world, i - 8, j + 46, j + 46, k + 6, AIR);
        setVerticalLine(world, i - 8, j + 46, j + 46, k + 7, AIR);
        setVerticalLine(world, i - 9, j + 46, j + 46, k + 6, AIR);
        setVerticalLine(world, i + 3, j + 50, j + 50, k - 1, AIR);
        setVerticalLine(world, i + 1, j + 49, j + 49, k - 2, AIR);
        setVerticalLine(world, i - 7, j + 50, j + 50, k + 4, AIR);
        setVerticalLine(world, i - 8, j + 52, j + 52, k + 1, AIR);
        setVerticalLine(world, i + 1, j + 52, j + 53, k + 5, AIR);
        setVerticalLine(world, i + 6, j + 52, j + 52, k - 3, AIR);
        setVerticalLine(world, i + 5, j + 52, j + 52, k - 7, AIR);

        world.setBlock(i + 3, j + 52, k - 7, GLOWING, 0, 2);
        world.setBlock(i + 6, j + 51, k - 2, GLOWING, 0, 2);
        world.setBlock(i + 6, j + 51, k + 3, GLOWING, 0, 2);
        world.setBlock(i + 3, j + 52, k + 6, GLOWING, 0, 2);
        world.setBlock(i + 3, j + 56, k - 7, GLOWING, 0, 2);
        world.setBlock(i + 5, j + 55, k - 2, GLOWING, 0, 2);
        world.setBlock(i + 1, j + 55, k + 6, GLOWING, 0, 2);
        world.setBlock(i - 5, j + 56, k - 6, GLOWING, 0, 2);
        world.setBlock(i - 8, j + 51, k + 7, GLOWING, 0, 2);
}

    private void generateLayer27(World world, int i, int j, int k) {         
        setVerticalLine(world, i - 8, j + 50, j + 50, k - 9, FRAME);
        setVerticalLine(world, i - 7, j + 50, j + 50, k - 9, FRAME);
        setVerticalLine(world, i - 6, j + 50, j + 50, k - 9, FRAME);
        setVerticalLine(world, i - 5, j + 50, j + 50, k - 9, FRAME);
        setVerticalLine(world, i - 4, j + 50, j + 50, k - 9, FRAME);
        setVerticalLine(world, i - 3, j + 50, j + 50, k - 9, FRAME);
        setVerticalLine(world, i - 2, j + 50, j + 50, k - 9, FRAME);
        setVerticalLine(world, i - 1, j + 49, j + 50, k - 9, FRAME);
        setVerticalLine(world, i + 0, j + 50, j + 50, k - 9, FRAME);
        setVerticalLine(world, i + 1, j + 50, j + 50, k - 9, FRAME);
        setVerticalLine(world, i + 2, j + 50, j + 50, k - 9, FRAME);
        setVerticalLine(world, i + 3, j + 50, j + 50, k - 9, FRAME);
        setVerticalLine(world, i + 4, j + 50, j + 50, k - 9, FRAME);
        setVerticalLine(world, i - 7, j + 50, j + 50, k - 8, FRAME);
        setVerticalLine(world, i - 8, j + 50, j + 50, k - 8, FRAME);
        setVerticalLine(world, i - 9, j + 50, j + 50, k - 8, FRAME);
        setVerticalLine(world, i + 4, j + 50, j + 50, k - 8, FRAME);
        setVerticalLine(world, i + 5, j + 50, j + 50, k - 8, FRAME);
        setVerticalLine(world, i - 7, j + 50, j + 50, k - 7, FRAME);
        setVerticalLine(world, i - 8, j + 50, j + 50, k - 7, FRAME);
        setVerticalLine(world, i - 9, j + 50, j + 50, k - 7, FRAME);
        setVerticalLine(world, i - 10, j + 50, j + 50, k - 7, FRAME);
        setVerticalLine(world, i + 5, j + 50, j + 50, k - 7, FRAME);
        setVerticalLine(world, i + 6, j + 50, j + 50, k - 7, FRAME);
        setVerticalLine(world, i - 9, j + 50, j + 50, k - 6, FRAME);
        setVerticalLine(world, i - 10, j + 50, j + 50, k - 6, FRAME);
        setVerticalLine(world, i + 5, j + 50, j + 50, k - 6, FRAME);
        setVerticalLine(world, i + 6, j + 50, j + 50, k - 6, FRAME);
        setVerticalLine(world, i - 10, j + 50, j + 50, k - 5, FRAME);
        setVerticalLine(world, i + 6, j + 50, j + 50, k - 5, FRAME);
        setVerticalLine(world, i - 8, j + 50, j + 50, k - 4, FRAME);

        setVerticalLine(world, i - 3, j + 54, j + 54, k - 7, FRAME);
        setVerticalLine(world, i - 2, j + 54, j + 54, k - 7, FRAME);
        setVerticalLine(world, i - 1, j + 54, j + 58, k - 7, FRAME);
        setVerticalLine(world, i + 0, j + 54, j + 56, k - 7, FRAME);
        setVerticalLine(world, i + 1, j + 54, j + 55, k - 7, FRAME);
        setVerticalLine(world, i - 4, j + 55, j + 57, k - 6, FRAME);
        setVerticalLine(world, i - 3, j + 54, j + 56, k - 6, FRAME);
        setVerticalLine(world, i - 2, j + 55, j + 57, k - 6, FRAME);
        setVerticalLine(world, i - 1, j + 56, j + 58, k - 6, FRAME);
        setVerticalLine(world, i + 0, j + 56, j + 59, k - 6, FRAME);
        setVerticalLine(world, i + 1, j + 55, j + 56, k - 6, FRAME);
        setVerticalLine(world, i + 2, j + 54, j + 55, k - 6, FRAME);
        setVerticalLine(world, i + 3, j + 54, j + 55, k - 6, FRAME);
        setVerticalLine(world, i - 5, j + 54, j + 56, k - 5, FRAME);
        setVerticalLine(world, i - 4, j + 56, j + 58, k - 5, FRAME);
        setVerticalLine(world, i - 3, j + 57, j + 59, k - 5, FRAME);
        setVerticalLine(world, i - 2, j + 58, j + 60, k - 5, FRAME);
        setVerticalLine(world, i - 1, j + 57, j + 60, k - 5, FRAME);
        setVerticalLine(world, i + 1, j + 56, j + 59, k - 5, FRAME);
        setVerticalLine(world, i + 2, j + 56, j + 58, k - 5, FRAME);
        setVerticalLine(world, i + 3, j + 56, j + 56, k - 5, FRAME);
        setVerticalLine(world, i - 6, j + 55, j + 58, k - 4, FRAME);
        setVerticalLine(world, i - 5, j + 57, j + 59, k - 4, FRAME);
        setVerticalLine(world, i + 3, j + 54, j + 55, k - 4, FRAME);
        setVerticalLine(world, i + 4, j + 54, j + 56, k - 4, FRAME);
        setVerticalLine(world, i - 6, j + 56, j + 57, k - 3, FRAME);
        setVerticalLine(world, i + 4, j + 55, j + 57, k - 3, FRAME);
        setVerticalLine(world, i - 6, j + 57, j + 58, k - 2, FRAME);
        setVerticalLine(world, i + 4, j + 55, j + 56, k - 2, FRAME);
        setVerticalLine(world, i - 6, j + 57, j + 59, k - 1, FRAME);
        setVerticalLine(world, i + 4, j + 54, j + 56, k - 1, FRAME);

        setVerticalLine(world, i - 6, j + 57, j + 58, k + 0, FRAME);
        setVerticalLine(world, i - 5, j + 58, j + 61, k + 0, FRAME);
        setVerticalLine(world, i + 3, j + 55, j + 60, k + 0, FRAME);
        setVerticalLine(world, i - 6, j + 57, j + 59, k + 1, FRAME);
        setVerticalLine(world, i - 5, j + 59, j + 62, k + 1, FRAME);
        setVerticalLine(world, i + 4, j + 54, j + 56, k + 1, FRAME);
        setVerticalLine(world, i + 5, j + 54, j + 54, k + 1, FRAME);
        setVerticalLine(world, i + 4, j + 55, j + 57, k + 2, FRAME);
        setVerticalLine(world, i + 3, j + 57, j + 59, k + 2, FRAME);
        setVerticalLine(world, i - 6, j + 56, j + 57, k + 2, FRAME);
        setVerticalLine(world, i - 5, j + 58, j + 59, k + 2, FRAME);
        setVerticalLine(world, i + 4, j + 53, j + 54, k + 3, FRAME);
        setVerticalLine(world, i - 6, j + 55, j + 57, k + 3, FRAME);
        setVerticalLine(world, i - 5, j + 57, j + 58, k + 3, FRAME);
        setVerticalLine(world, i - 3, j + 56, j + 59, k + 3, FRAME);
        setVerticalLine(world, i - 2, j + 57, j + 59, k + 3, FRAME);
        setVerticalLine(world, i - 1, j + 56, j + 58, k + 3, FRAME);
        setVerticalLine(world, i + 2, j + 56, j + 58, k + 3, FRAME);
        setVerticalLine(world, i + 3, j + 56, j + 56, k + 3, FRAME);
        setVerticalLine(world, i - 3, j + 54, j + 57, k + 4, FRAME);
        setVerticalLine(world, i - 2, j + 55, j + 56, k + 4, FRAME);
        setVerticalLine(world, i - 1, j + 54, j + 55, k + 4, FRAME);
        setVerticalLine(world, i + 0, j + 56, j + 58, k + 4, FRAME);
        setVerticalLine(world, i + 1, j + 57, j + 59, k + 4, FRAME);
        setVerticalLine(world, i + 2, j + 56, j + 58, k + 4, FRAME);
        setVerticalLine(world, i + 3, j + 54, j + 56, k + 4, FRAME);
        setVerticalLine(world, i + 4, j + 54, j + 54, k + 4, FRAME);
        setVerticalLine(world, i + 0, j + 53, j + 57, k + 5, FRAME);
        setVerticalLine(world, i + 1, j + 55, j + 56, k + 5, FRAME);
        setVerticalLine(world, i + 2, j + 54, j + 55, k + 5, FRAME);
        setVerticalLine(world, i + 1, j + 55, j + 55, k + 6, FRAME);
        setVerticalLine(world, i + 3, j + 50, j + 50, k + 7, FRAME);
        setVerticalLine(world, i + 1, j + 50, j + 50, k + 7, FRAME);
        setVerticalLine(world, i - 5, j + 50, j + 50, k + 6, FRAME);
        setVerticalLine(world, i - 7, j + 50, j + 50, k + 6, FRAME);
        setVerticalLine(world, i - 8, j + 50, j + 50, k + 6, FRAME);
        setVerticalLine(world, i - 9, j + 50, j + 50, k + 6, FRAME);
        setVerticalLine(world, i + 4, j + 50, j + 50, k + 6, FRAME);
        setVerticalLine(world, i + 5, j + 50, j + 50, k + 6, FRAME);
        setVerticalLine(world, i - 5, j + 50, j + 50, k + 7, FRAME);
        setVerticalLine(world, i - 7, j + 50, j + 50, k + 7, FRAME);
        setVerticalLine(world, i - 8, j + 50, j + 50, k + 7, FRAME);
        setVerticalLine(world, i + 4, j + 50, j + 50, k + 7, FRAME);
        setVerticalLine(world, i + 6, j + 50, j + 50, k + 5, FRAME);
        setVerticalLine(world, i + 5, j + 50, j + 50, k + 5, FRAME);

        setVerticalLine(world, i - 11, j + 50, j + 50, k - 5, FRAME);
        setVerticalLine(world, i - 11, j + 50, j + 50, k - 4, FRAME);
        setVerticalLine(world, i - 11, j + 50, j + 50, k - 3, FRAME);
        setVerticalLine(world, i - 11, j + 50, j + 50, k - 2, FRAME);
        setVerticalLine(world, i - 11, j + 50, j + 50, k - 1, FRAME);
        setVerticalLine(world, i - 11, j + 50, j + 50, k + 0, FRAME);
        setVerticalLine(world, i - 11, j + 50, j + 50, k + 1, FRAME);
        setVerticalLine(world, i - 11, j + 50, j + 50, k + 2, FRAME);
        setVerticalLine(world, i - 11, j + 50, j + 50, k + 3, FRAME);
        setVerticalLine(world, i - 11, j + 50, j + 50, k + 4, FRAME);
        setVerticalLine(world, i - 10, j + 51, j + 51, k - 7, FRAME);

        setVerticalLine(world, i - 10, j + 51, j + 51, k - 7, AIR);
        setVerticalLine(world, i - 9, j + 51, j + 51, k - 8, AIR);
        setVerticalLine(world, i - 8, j + 51, j + 51, k - 9, AIR);
        setVerticalLine(world, i - 7, j + 51, j + 51, k - 9, AIR);
        setVerticalLine(world, i - 6, j + 51, j + 51, k - 9, AIR);
        setVerticalLine(world, i - 5, j + 51, j + 51, k - 9, AIR);
        setVerticalLine(world, i - 4, j + 51, j + 51, k - 9, AIR);
        setVerticalLine(world, i - 3, j + 51, j + 51, k - 9, AIR);
        setVerticalLine(world, i - 2, j + 51, j + 51, k - 9, AIR);
        setVerticalLine(world, i - 1, j + 51, j + 51, k - 9, AIR);
        setVerticalLine(world, i + 0, j + 51, j + 51, k - 9, AIR);
        setVerticalLine(world, i + 1, j + 51, j + 51, k - 9, AIR);
        setVerticalLine(world, i + 2, j + 51, j + 51, k - 9, AIR);
        setVerticalLine(world, i + 3, j + 51, j + 51, k - 9, AIR);
        setVerticalLine(world, i + 4, j + 51, j + 51, k - 9, AIR);
        setVerticalLine(world, i - 8, j + 51, j + 51, k - 8, AIR);
        setVerticalLine(world, i - 7, j + 51, j + 51, k - 8, AIR);
        setVerticalLine(world, i - 5, j + 51, j + 51, k - 8, AIR);
        setVerticalLine(world, i - 4, j + 51, j + 51, k - 8, AIR);
        setVerticalLine(world, i - 3, j + 51, j + 51, k - 8, AIR);
        setVerticalLine(world, i - 2, j + 51, j + 51, k - 8, AIR);
        setVerticalLine(world, i + 1, j + 51, j + 51, k - 8, AIR);
        setVerticalLine(world, i + 3, j + 51, j + 51, k - 8, AIR);
        setVerticalLine(world, i + 4, j + 51, j + 51, k - 8, AIR);
        setVerticalLine(world, i + 5, j + 51, j + 51, k - 8, AIR);
        setVerticalLine(world, i - 9, j + 51, j + 51, k - 7, AIR);
        setVerticalLine(world, i - 8, j + 51, j + 51, k - 7, AIR);
        setVerticalLine(world, i - 7, j + 51, j + 51, k - 7, AIR);
        setVerticalLine(world, i - 6, j + 51, j + 51, k - 7, AIR);
        setVerticalLine(world, i - 5, j + 51, j + 51, k - 7, AIR);
        setVerticalLine(world, i + 4, j + 51, j + 51, k - 7, AIR);
        setVerticalLine(world, i + 5, j + 51, j + 51, k - 7, AIR);
        setVerticalLine(world, i + 6, j + 51, j + 51, k - 7, AIR);
        setVerticalLine(world, i - 10, j + 51, j + 51, k - 6, AIR);
        setVerticalLine(world, i - 9, j + 51, j + 51, k - 6, AIR);
        setVerticalLine(world, i - 8, j + 51, j + 51, k - 6, AIR);
        setVerticalLine(world, i - 7, j + 51, j + 51, k - 6, AIR);
        setVerticalLine(world, i - 6, j + 51, j + 51, k - 6, AIR);
        setVerticalLine(world, i + 4, j + 51, j + 51, k - 6, AIR);
        setVerticalLine(world, i + 5, j + 51, j + 51, k - 6, AIR);
        setVerticalLine(world, i + 6, j + 51, j + 51, k - 6, AIR);
        setVerticalLine(world, i - 10, j + 51, j + 51, k - 5, AIR);
        setVerticalLine(world, i - 9, j + 51, j + 51, k - 5, AIR);
        setVerticalLine(world, i - 8, j + 51, j + 51, k - 5, AIR);
        setVerticalLine(world, i - 7, j + 51, j + 51, k - 5, AIR);
        setVerticalLine(world, i - 6, j + 51, j + 51, k - 5, AIR);
        setVerticalLine(world, i + 5, j + 51, j + 51, k - 5, AIR);
        setVerticalLine(world, i + 6, j + 51, j + 51, k - 5, AIR);
        setVerticalLine(world, i - 10, j + 51, j + 51, k - 4, AIR);
        setVerticalLine(world, i - 9, j + 51, j + 51, k - 4, AIR);
        setVerticalLine(world, i - 8, j + 51, j + 51, k - 4, AIR);
        setVerticalLine(world, i + 6, j + 51, j + 51, k - 4, AIR);
        setVerticalLine(world, i - 6, j + 51, j + 51, k - 8, AIR);
        setVerticalLine(world, i - 5, j + 51, j + 51, k - 8, AIR);
        setVerticalLine(world, i + 6, j + 51, j + 51, k - 3, AIR);
        setVerticalLine(world, i + 6, j + 51, j + 51, k - 2, AIR);
        setVerticalLine(world, i + 6, j + 51, j + 51, k - 1, AIR);
        setVerticalLine(world, i + 3, j + 55, j + 55, k - 1, AIR);
        setVerticalLine(world, i + 6, j + 51, j + 51, k + 0, AIR);
        setVerticalLine(world, i + 6, j + 51, j + 51, k + 1, AIR);
        setVerticalLine(world, i + 6, j + 51, j + 51, k + 2, AIR);
        setVerticalLine(world, i + 6, j + 51, j + 51, k + 3, AIR);
        setVerticalLine(world, i + 6, j + 51, j + 51, k + 4, AIR);
        setVerticalLine(world, i + 5, j + 51, j + 51, k + 4, AIR);
        setVerticalLine(world, i + 6, j + 51, j + 51, k + 5, AIR);
        setVerticalLine(world, i + 5, j + 51, j + 51, k + 5, AIR);
        setVerticalLine(world, i + 4, j + 51, j + 51, k + 5, AIR);
        setVerticalLine(world, i + 5, j + 51, j + 51, k + 6, AIR);
        setVerticalLine(world, i + 4, j + 51, j + 51, k + 6, AIR);
        setVerticalLine(world, i + 3, j + 51, j + 51, k + 6, AIR);
        setVerticalLine(world, i + 4, j + 51, j + 51, k + 7, AIR);
        setVerticalLine(world, i + 3, j + 51, j + 51, k + 7, AIR);
        setVerticalLine(world, i + 2, j + 51, j + 51, k + 7, AIR);
        setVerticalLine(world, i + 1, j + 51, j + 51, k + 7, AIR);
        setVerticalLine(world, i + 0, j + 51, j + 51, k + 7, AIR);
        setVerticalLine(world, i - 1, j + 51, j + 51, k + 7, AIR);
        setVerticalLine(world, i - 2, j + 51, j + 51, k + 7, AIR);
        setVerticalLine(world, i - 3, j + 51, j + 51, k + 7, AIR);
        setVerticalLine(world, i - 4, j + 51, j + 51, k + 7, AIR);
        setVerticalLine(world, i - 5, j + 51, j + 51, k + 7, AIR);
        setVerticalLine(world, i - 6, j + 51, j + 51, k + 7, AIR);
        setVerticalLine(world, i - 7, j + 51, j + 51, k + 7, AIR);
        setVerticalLine(world, i - 8, j + 51, j + 51, k + 7, AIR);
        setVerticalLine(world, i - 9, j + 51, j + 51, k + 6, AIR);
        setVerticalLine(world, i - 8, j + 51, j + 51, k + 6, AIR);
        setVerticalLine(world, i - 7, j + 51, j + 51, k + 6, AIR);
        setVerticalLine(world, i - 6, j + 51, j + 51, k + 6, AIR);
        setVerticalLine(world, i - 5, j + 51, j + 51, k + 6, AIR);
        setVerticalLine(world, i - 4, j + 51, j + 51, k + 6, AIR);
        setVerticalLine(world, i - 3, j + 51, j + 51, k + 6, AIR);
        setVerticalLine(world, i - 2, j + 51, j + 51, k + 6, AIR);
        setVerticalLine(world, i - 1, j + 51, j + 51, k + 6, AIR);
        setVerticalLine(world, i - 10, j + 51, j + 51, k + 5, AIR);
        setVerticalLine(world, i - 9, j + 51, j + 51, k + 5, AIR);
        setVerticalLine(world, i - 8, j + 51, j + 51, k + 5, AIR);
        setVerticalLine(world, i - 7, j + 51, j + 51, k + 5, AIR);
        setVerticalLine(world, i - 6, j + 51, j + 51, k + 5, AIR);
        setVerticalLine(world, i - 7, j + 51, j + 51, k + 4, AIR);
        setVerticalLine(world, i - 9, j + 51, j + 51, k + 4, AIR);
        setVerticalLine(world, i - 8, j + 51, j + 51, k + 4, AIR);
        setVerticalLine(world, i - 10, j + 51, j + 51, k + 4, AIR);
        setVerticalLine(world, i + 1, j + 54, j + 54, k - 6, AIR);
        setVerticalLine(world, i + 2, j + 54, j + 54, k - 6, AIR);
        setVerticalLine(world, i + 3, j + 54, j + 54, k - 5, AIR);
        setVerticalLine(world, i - 6, j + 55, j + 55, k - 5, AIR);
        setVerticalLine(world, i - 1, j + 53, j + 53, k - 7, AIR);
        setVerticalLine(world, i + 4, j + 53, j + 53, k - 1, AIR);
        setVerticalLine(world, i - 5, j + 58, j + 58, k + 1, AIR);
        setVerticalLine(world, i - 5, j + 58, j + 58, k + 2, AIR);
        setVerticalLine(world, i - 4, j + 57, j + 57, k + 4, AIR);

        world.setBlock(i - 8, j + 51, k - 4, GLOWING, 0, 2);
        world.setBlock(i - 1, j + 61, k - 5, GLOWING, 0, 2);
        world.setBlock(i - 5, j + 63, k + 1, GLOWING, 0, 2);
        world.setBlock(i - 11, j + 51, k - 5, GLOWING, 0, 2);
  }
  
    private void generateLayer28(World world, int i, int j, int k) {    
        setVerticalLine(world, i - 5, j + 60, j + 62, k - 1, FRAME);
        setVerticalLine(world, i - 5, j + 58, j + 60, k - 2, FRAME);
        setVerticalLine(world, i - 5, j + 58, j + 60, k - 3, FRAME);
        setVerticalLine(world, i - 4, j + 58, j + 61, k - 4, FRAME);
        setVerticalLine(world, i - 3, j + 59, j + 61, k - 4, FRAME);
        setVerticalLine(world, i - 2, j + 60, j + 62, k - 4, FRAME);
        setVerticalLine(world, i - 2, j + 61, j + 61, k - 5, FRAME);
        setVerticalLine(world, i - 1, j + 61, j + 62, k - 4, FRAME);
        setVerticalLine(world, i + 0, j + 59, j + 61, k - 5, FRAME);
        setVerticalLine(world, i + 0, j + 61, j + 68, k - 3, FRAME);
        setVerticalLine(world, i + 1, j + 59, j + 61, k - 4, FRAME);
        setVerticalLine(world, i + 1, j + 60, j + 67, k - 1, FRAME);
        setVerticalLine(world, i + 1, j + 62, j + 65, k - 3, FRAME);
        setVerticalLine(world, i + 1, j + 60, j + 60, k - 5, FRAME);
        setVerticalLine(world, i + 2, j + 58, j + 61, k - 4, FRAME);
        setVerticalLine(world, i + 2, j + 59, j + 62, k - 2, FRAME);
        setVerticalLine(world, i + 2, j + 60, j + 63, k - 3, FRAME);
        setVerticalLine(world, i + 2, j + 58, j + 61, k - 1, FRAME);
        setVerticalLine(world, i + 2, j + 60, j + 62, k + 0, FRAME);

        setVerticalLine(world, i - 5, j + 62, j + 63, k + 0, FRAME);
        setVerticalLine(world, i - 4, j + 62, j + 65, k - 1, FRAME);
        setVerticalLine(world, i - 4, j + 60, j + 64, k - 2, FRAME);
        setVerticalLine(world, i - 4, j + 61, j + 65, k - 3, FRAME);
        setVerticalLine(world, i - 4, j + 59, j + 61, k + 2, FRAME);
        setVerticalLine(world, i - 3, j + 60, j + 62, k + 2, FRAME);
        setVerticalLine(world, i - 3, j + 63, j + 69, k + 1, FRAME);
        setVerticalLine(world, i - 2, j + 59, j + 63, k + 2, FRAME);
        setVerticalLine(world, i - 1, j + 59, j + 62, k + 2, FRAME);
        setVerticalLine(world, i - 1, j + 60, j + 61, k + 3, FRAME);
        setVerticalLine(world, i + 0, j + 60, j + 65, k + 2, FRAME);
        setVerticalLine(world, i + 0, j + 59, j + 62, k + 3, FRAME);
        setVerticalLine(world, i + 1, j + 58, j + 61, k + 3, FRAME);
        setVerticalLine(world, i + 1, j + 62, j + 67, k + 0, FRAME);
        setVerticalLine(world, i + 1, j + 61, j + 64, k + 1, FRAME);
        setVerticalLine(world, i + 1, j + 62, j + 66, k + 2, FRAME);
        setVerticalLine(world, i + 2, j + 58, j + 62, k + 1, FRAME);
        setVerticalLine(world, i + 2, j + 59, j + 62, k + 2, FRAME);
        setVerticalLine(world, i + 2, j + 59, j + 59, k + 3, FRAME);
        setVerticalLine(world, i + 3, j + 58, j + 58, k + 1, FRAME);

        setVerticalLine(world, i - 4, j + 66, j + 66, k - 1, FRAME);
        setVerticalLine(world, i - 3, j + 64, j + 72, k - 2, FRAME);
        setVerticalLine(world, i - 3, j + 66, j + 71, k - 1, FRAME);
        setVerticalLine(world, i - 3, j + 65, j + 70, k + 0, FRAME);
        setVerticalLine(world, i - 2, j + 64, j + 73, k - 3, FRAME);
        setVerticalLine(world, i - 2, j + 67, j + 72, k - 2, FRAME);
        setVerticalLine(world, i - 1, j + 62, j + 66, k - 3, FRAME);
        setVerticalLine(world, i - 1, j + 63, j + 64, k - 4, FRAME);
        setVerticalLine(world, i - 1, j + 66, j + 71, k - 2, FRAME);
        setVerticalLine(world, i + 0, j + 64, j + 74, k - 2, FRAME);
        setVerticalLine(world, i + 0, j + 62, j + 72, k - 4, FRAME);
        setVerticalLine(world, i + 0, j + 66, j + 72, k - 1, FRAME);

        setVerticalLine(world, i - 4, j + 61, j + 66, k + 1, FRAME);
        setVerticalLine(world, i - 3, j + 63, j + 67, k + 1, FRAME);
        setVerticalLine(world, i - 2, j + 64, j + 72, k + 1, FRAME);
        setVerticalLine(world, i - 2, j + 64, j + 66, k + 2, FRAME);
        setVerticalLine(world, i - 1, j + 64, j + 85, k + 0, FRAME);
        setVerticalLine(world, i - 1, j + 64, j + 72, k + 1, FRAME);
        setVerticalLine(world, i - 1, j + 65, j + 65, k + 2, FRAME);
        setVerticalLine(world, i + 0, j + 65, j + 73, k + 1, FRAME);
        setVerticalLine(world, i + 0, j + 68, j + 74, k + 0, FRAME);

        setVerticalLine(world, i - 6, j + 52, j + 53, k + 0, FRAME);
        setVerticalLine(world, i + 4, j + 52, j + 52, k - 2, FRAME);
        setVerticalLine(world, i + 5, j + 52, j + 52, k - 3, FRAME);
        setVerticalLine(world, i + 5, j + 52, j + 52, k - 1, FRAME);
        setVerticalLine(world, i + 5, j + 52, j + 52, k + 0, FRAME);
        setVerticalLine(world, i + 6, j + 50, j + 51, k - 2, FRAME);
        setVerticalLine(world, i + 6, j + 51, j + 51, k - 3, FRAME);
        setVerticalLine(world, i + 6, j + 51, j + 51, k - 1, FRAME);
        setVerticalLine(world, i + 6, j + 51, j + 51, k + 0, FRAME);

        setVerticalLine(world, i - 6, j + 52, j + 53, k + 0, AIR);
        setVerticalLine(world, i - 5, j + 58, j + 58, k - 1, AIR);
        setVerticalLine(world, i - 4, j + 59, j + 59, k - 2, AIR);
        setVerticalLine(world, i - 2, j + 61, j + 61, k - 5, AIR);
        setVerticalLine(world, i - 2, j + 59, j + 59, k + 2, AIR);
        setVerticalLine(world, i - 2, j + 61, j + 61, k + 1, AIR);
        setVerticalLine(world, i - 1, j + 59, j + 60, k + 2, AIR);
        setVerticalLine(world, i - 1, j + 64, j + 64, k - 2, AIR);
        setVerticalLine(world, i + 0, j + 61, j + 61, k - 5, AIR);
        setVerticalLine(world, i + 1, j + 60, j + 60, k + 1, AIR);
        setVerticalLine(world, i + 2, j + 59, j + 59, k - 3, AIR);
        setVerticalLine(world, i + 2, j + 62, j + 62, k - 1, AIR);
        setVerticalLine(world, i + 2, j + 63, j + 63, k + 1, AIR);
        setVerticalLine(world, i - 3, j + 60, j + 60, k + 1, AIR);
        setVerticalLine(world, i - 2, j + 72, j + 72, k + 1, AIR);
        setVerticalLine(world, i - 2, j + 66, j + 66, k - 1, AIR);
        setVerticalLine(world, i - 2, j + 67, j + 67, k + 1, AIR);
        setVerticalLine(world, i - 2, j + 62, j + 62, k - 3, AIR);
        setVerticalLine(world, i - 1, j + 52, j + 52, k - 1, AIR);
        setVerticalLine(world, i + 0, j + 74, j + 74, k - 1, AIR);
        setVerticalLine(world, i + 3, j + 51, j + 51, k - 3, AIR);
        setVerticalLine(world, i + 4, j + 51, j + 52, k - 3, AIR);
        setVerticalLine(world, i + 4, j + 51, j + 52, k - 2, AIR);
        setVerticalLine(world, i + 4, j + 51, j + 51, k - 1, AIR);
        setVerticalLine(world, i + 4, j + 51, j + 51, k + 0, AIR);
        setVerticalLine(world, i + 5, j + 50, j + 51, k - 2, AIR);
        setVerticalLine(world, i + 5, j + 51, j + 52, k - 1, AIR);
        setVerticalLine(world, i + 5, j + 51, j + 52, k + 0, AIR);
        setVerticalLine(world, i + 5, j + 51, j + 52, k - 3, AIR);
        setVerticalLine(world, i + 6, j + 50, j + 50, k - 2, AIR);

        world.setBlock(i - 5, j + 52, k - 4, GLOWING, 0, 2);
        world.setBlock(i + 0, j + 52, k - 7, GLOWING, 0, 2);
        world.setBlock(i - 4, j + 52, k + 4, GLOWING, 0, 2);
        world.setBlock(i + 3, j + 52, k + 4, GLOWING, 0, 2);
        world.setBlock(i + 0, j + 69, k - 3, GLOWING, 0, 2);
        world.setBlock(i - 3, j + 70, k + 1, GLOWING, 0, 2);
        world.setBlock(i - 2, j + 74, k - 3, GLOWING, 0, 2);
        world.setBlock(i - 1, j + 86, k + 0, GLOWING, 0, 2);
     }

    private void generateLayer29(World world, int i, int j, int k) {
        setVerticalLine(world, i + 2, j + 52, j + 53, k - 6, FRAME);
        setVerticalLine(world, i + 2, j + 52, j + 52, k - 5, FRAME);
        setVerticalLine(world, i + 3, j + 52, j + 54, k - 5, FRAME);
        setVerticalLine(world, i + 1, j + 50, j + 50, k - 4, FRAME);
        setVerticalLine(world, i + 2, j + 50, j + 50, k - 4, FRAME);
        setVerticalLine(world, i + 3, j + 50, j + 50, k - 4, FRAME);
        setVerticalLine(world, i + 4, j + 50, j + 50, k - 4, FRAME);
        setVerticalLine(world, i + 2, j + 49, j + 49, k - 3, FRAME);
        setVerticalLine(world, i + 3, j + 49, j + 49, k - 3, FRAME);
        setVerticalLine(world, i + 4, j + 49, j + 49, k - 3, FRAME);
        setVerticalLine(world, i + 5, j + 49, j + 50, k - 3, FRAME);
        setVerticalLine(world, i + 1, j + 49, j + 49, k - 2, FRAME);
        setVerticalLine(world, i + 2, j + 48, j + 48, k - 2, FRAME);
        setVerticalLine(world, i + 3, j + 48, j + 49, k - 2, FRAME);
        setVerticalLine(world, i + 4, j + 48, j + 49, k - 2, FRAME);
        setVerticalLine(world, i + 5, j + 47, j + 49, k - 2, FRAME);
        setVerticalLine(world, i + 2, j + 48, j + 48, k - 1, FRAME);
        setVerticalLine(world, i + 3, j + 48, j + 48, k - 1, FRAME);
        setVerticalLine(world, i + 4, j + 48, j + 48, k - 1, FRAME);
        setVerticalLine(world, i + 5, j + 48, j + 49, k - 1, FRAME);
        setVerticalLine(world, i + 7, j + 47, j + 47, k - 1, FRAME);

        setVerticalLine(world, i + 3, j + 48, j + 48, k + 0, FRAME);
        setVerticalLine(world, i + 4, j + 48, j + 48, k + 0, FRAME);
        setVerticalLine(world, i + 5, j + 48, j + 49, k + 0, FRAME);
        setVerticalLine(world, i + 2, j + 47, j + 48, k + 1, FRAME);
        setVerticalLine(world, i + 3, j + 47, j + 47, k + 1, FRAME);
        setVerticalLine(world, i + 4, j + 47, j + 47, k + 1, FRAME);
        setVerticalLine(world, i + 4, j + 52, j + 53, k + 1, FRAME);
        setVerticalLine(world, i + 5, j + 47, j + 47, k + 1, FRAME);
        setVerticalLine(world, i + 6, j + 47, j + 47, k + 1, FRAME);
        setVerticalLine(world, i + 2, j + 47, j + 47, k + 2, FRAME);
        setVerticalLine(world, i + 3, j + 47, j + 47, k + 2, FRAME);
        setVerticalLine(world, i + 4, j + 47, j + 47, k + 2, FRAME);
        setVerticalLine(world, i + 4, j + 54, j + 54, k + 2, FRAME);
        setVerticalLine(world, i + 5, j + 47, j + 47, k + 2, FRAME);
        setVerticalLine(world, i + 6, j + 47, j + 47, k + 2, FRAME);
        setVerticalLine(world, i + 2, j + 46, j + 47, k + 3, FRAME);
        setVerticalLine(world, i + 3, j + 46, j + 47, k + 3, FRAME);
        setVerticalLine(world, i + 4, j + 46, j + 46, k + 3, FRAME);
        setVerticalLine(world, i + 5, j + 46, j + 47, k + 3, FRAME);
        setVerticalLine(world, i + 6, j + 46, j + 47, k + 3, FRAME);
        setVerticalLine(world, i + 7, j + 46, j + 47, k + 3, FRAME);
        setVerticalLine(world, i + 2, j + 46, j + 46, k + 4, FRAME);
        setVerticalLine(world, i + 3, j + 45, j + 46, k + 4, FRAME);
        setVerticalLine(world, i + 4, j + 46, j + 46, k + 4, FRAME);
        setVerticalLine(world, i + 5, j + 46, j + 46, k + 4, FRAME);
        setVerticalLine(world, i + 6, j + 46, j + 46, k + 4, FRAME);
        setVerticalLine(world, i + 2, j + 45, j + 45, k + 5, FRAME);
        setVerticalLine(world, i + 3, j + 45, j + 45, k + 5, FRAME);
        setVerticalLine(world, i + 4, j + 45, j + 45, k + 5, FRAME);
        setVerticalLine(world, i + 5, j + 45, j + 45, k + 5, FRAME);
        setVerticalLine(world, i + 6, j + 45, j + 45, k + 5, FRAME);
        setVerticalLine(world, i + 1, j + 44, j + 44, k + 5, FRAME);
        setVerticalLine(world, i + 0, j + 44, j + 44, k + 5, FRAME);
        setVerticalLine(world, i + 1, j + 44, j + 44, k + 6, FRAME);
        setVerticalLine(world, i + 2, j + 44, j + 44, k + 6, FRAME);
        setVerticalLine(world, i + 0, j + 43, j + 44, k + 7, FRAME);
        setVerticalLine(world, i + 1, j + 44, j + 44, k + 7, FRAME);
        setVerticalLine(world, i + 2, j + 44, j + 44, k + 7, FRAME);
        setVerticalLine(world, i + 3, j + 44, j + 44, k + 7, FRAME);
        setVerticalLine(world, i + 0, j + 43, j + 44, k + 8, FRAME);
        setVerticalLine(world, i + 1, j + 44, j + 44, k + 8, FRAME);
        setVerticalLine(world, i + 2, j + 44, j + 44, k + 8, FRAME);
        setVerticalLine(world, i + 4, j + 43, j + 43, k + 8, FRAME);
        setVerticalLine(world, i + 0, j + 43, j + 44, k + 9, FRAME);

        setVerticalLine(world, i - 11, j + 32, j + 32, k - 13, FRAME);
        setVerticalLine(world, i - 10, j + 32, j + 32, k - 13, FRAME);
        setVerticalLine(world, i - 11, j + 32, j + 32, k - 12, FRAME);
        setVerticalLine(world, i - 10, j + 32, j + 32, k - 12, FRAME);
        setVerticalLine(world, i - 11, j + 32, j + 32, k - 11, FRAME);
        setVerticalLine(world, i - 10, j + 32, j + 32, k - 11, FRAME);
        setVerticalLine(world, i - 11, j + 32, j + 32, k - 10, FRAME);
        setVerticalLine(world, i - 10, j + 32, j + 32, k - 10, FRAME);
        setVerticalLine(world, i - 11, j + 32, j + 32, k - 9, FRAME);
        setVerticalLine(world, i - 10, j + 32, j + 32, k - 9, FRAME);
        setVerticalLine(world, i - 11, j + 32, j + 32, k - 8, FRAME);
        setVerticalLine(world, i - 10, j + 32, j + 32, k - 8, FRAME);
        setVerticalLine(world, i - 11, j + 32, j + 32, k - 7, FRAME);
        setVerticalLine(world, i - 10, j + 32, j + 32, k - 7, FRAME);
        setVerticalLine(world, i - 9, j + 31, j + 31, k - 14, FRAME);
        setVerticalLine(world, i - 8, j + 31, j + 31, k - 15, FRAME);
        setVerticalLine(world, i - 8, j + 31, j + 31, k - 14, FRAME);
        setVerticalLine(world, i - 8, j + 31, j + 31, k - 13, FRAME);
        setVerticalLine(world, i - 8, j + 31, j + 31, k - 12, FRAME);
        setVerticalLine(world, i - 8, j + 31, j + 31, k - 11, FRAME);
        setVerticalLine(world, i - 12, j + 33, j + 33, k - 11, FRAME);
        setVerticalLine(world, i - 12, j + 33, j + 33, k - 10, FRAME);
        setVerticalLine(world, i - 13, j + 33, j + 33, k - 10, FRAME);
        setVerticalLine(world, i - 12, j + 33, j + 33, k - 9, FRAME);
        setVerticalLine(world, i - 13, j + 33, j + 33, k - 9, FRAME);
        setVerticalLine(world, i - 14, j + 33, j + 33, k - 9, FRAME);
        setVerticalLine(world, i - 12, j + 33, j + 33, k - 8, FRAME);
        setVerticalLine(world, i - 13, j + 33, j + 33, k - 8, FRAME);
        setVerticalLine(world, i - 14, j + 33, j + 33, k - 8, FRAME);
        setVerticalLine(world, i - 15, j + 33, j + 33, k - 8, FRAME);
        setVerticalLine(world, i - 16, j + 33, j + 33, k - 8, FRAME);
        setVerticalLine(world, i - 12, j + 33, j + 33, k - 7, FRAME);
        setVerticalLine(world, i - 13, j + 33, j + 33, k - 7, FRAME);
        setVerticalLine(world, i - 14, j + 33, j + 33, k - 7, FRAME);
        setVerticalLine(world, i - 15, j + 33, j + 33, k - 7, FRAME);
        setVerticalLine(world, i - 16, j + 33, j + 33, k - 7, FRAME);
        setVerticalLine(world, i - 12, j + 33, j + 33, k - 6, FRAME);
        setVerticalLine(world, i - 13, j + 33, j + 33, k - 6, FRAME);
        setVerticalLine(world, i - 14, j + 33, j + 33, k - 6, FRAME);
        setVerticalLine(world, i - 15, j + 33, j + 33, k - 6, FRAME);
        setVerticalLine(world, i - 16, j + 33, j + 33, k - 6, FRAME);
        setVerticalLine(world, i - 17, j + 33, j + 33, k - 6, FRAME);
        setVerticalLine(world, i - 13, j + 34, j + 34, k - 6, FRAME);
        setVerticalLine(world, i - 12, j + 34, j + 34, k - 5, FRAME);
        setVerticalLine(world, i - 13, j + 34, j + 34, k - 5, FRAME);
        setVerticalLine(world, i - 14, j + 34, j + 34, k - 5, FRAME);
        setVerticalLine(world, i - 15, j + 34, j + 34, k - 5, FRAME);
        setVerticalLine(world, i - 16, j + 34, j + 34, k - 5, FRAME);
        setVerticalLine(world, i - 17, j + 34, j + 34, k - 5, FRAME);
        setVerticalLine(world, i - 12, j + 34, j + 34, k - 4, FRAME);
        setVerticalLine(world, i - 13, j + 34, j + 34, k - 4, FRAME);
        setVerticalLine(world, i - 14, j + 34, j + 34, k - 4, FRAME);
        setVerticalLine(world, i - 15, j + 34, j + 34, k - 4, FRAME);
        setVerticalLine(world, i - 16, j + 34, j + 34, k - 4, FRAME);
        setVerticalLine(world, i - 17, j + 34, j + 34, k - 4, FRAME);
        setVerticalLine(world, i - 18, j + 34, j + 34, k - 4, FRAME);
        setVerticalLine(world, i - 13, j + 35, j + 35, k - 3, FRAME);
        setVerticalLine(world, i - 14, j + 35, j + 35, k - 3, FRAME);
        setVerticalLine(world, i - 15, j + 35, j + 35, k - 3, FRAME);
        setVerticalLine(world, i - 16, j + 35, j + 35, k - 3, FRAME);
        setVerticalLine(world, i - 17, j + 35, j + 35, k - 3, FRAME);
        setVerticalLine(world, i - 13, j + 35, j + 35, k - 2, FRAME);
        setVerticalLine(world, i - 14, j + 35, j + 35, k - 2, FRAME);
        setVerticalLine(world, i - 15, j + 35, j + 35, k - 2, FRAME);
        setVerticalLine(world, i - 16, j + 35, j + 35, k - 2, FRAME);
        setVerticalLine(world, i - 17, j + 35, j + 35, k - 2, FRAME);
        setVerticalLine(world, i - 18, j + 35, j + 35, k - 2, FRAME);
        setVerticalLine(world, i - 13, j + 36, j + 36, k - 1, FRAME);
        setVerticalLine(world, i - 14, j + 36, j + 36, k - 1, FRAME);
        setVerticalLine(world, i - 15, j + 36, j + 36, k - 1, FRAME);
        setVerticalLine(world, i - 16, j + 36, j + 36, k - 1, FRAME);
        setVerticalLine(world, i - 17, j + 36, j + 36, k - 1, FRAME);
        setVerticalLine(world, i - 18, j + 36, j + 36, k - 1, FRAME);
        setVerticalLine(world, i - 13, j + 36, j + 36, k + 0, FRAME);
        setVerticalLine(world, i - 14, j + 36, j + 36, k + 0, FRAME);
        setVerticalLine(world, i - 15, j + 36, j + 36, k + 0, FRAME);
        setVerticalLine(world, i - 16, j + 36, j + 36, k + 0, FRAME);
        setVerticalLine(world, i - 17, j + 36, j + 36, k + 0, FRAME);
        setVerticalLine(world, i - 18, j + 36, j + 36, k + 0, FRAME);
        
        setVerticalLine(world, i - 13, j + 37, j + 37, k + 1, FRAME);    
        setVerticalLine(world, i - 14, j + 37, j + 37, k + 1, FRAME);   
        setVerticalLine(world, i - 15, j + 37, j + 37, k + 1, FRAME);    
        setVerticalLine(world, i - 16, j + 37, j + 37, k + 1, FRAME);   
        setVerticalLine(world, i - 17, j + 37, j + 37, k + 1, FRAME);    
        setVerticalLine(world, i - 13, j + 37, j + 38, k + 2, FRAME);    
        setVerticalLine(world, i - 14, j + 37, j + 37, k + 2, FRAME);    
        setVerticalLine(world, i - 15, j + 37, j + 37, k + 2, FRAME);    
        setVerticalLine(world, i - 16, j + 37, j + 37, k + 2, FRAME);    
        setVerticalLine(world, i - 17, j + 37, j + 37, k + 2, FRAME);    
        setVerticalLine(world, i - 13, j + 37, j + 37, k + 3, FRAME);    
        setVerticalLine(world, i - 14, j + 37, j + 37, k + 3, FRAME);    
        setVerticalLine(world, i - 15, j + 37, j + 37, k + 3, FRAME);
        setVerticalLine(world, i - 16, j + 37, j + 37, k + 3, FRAME);
        setVerticalLine(world, i - 17, j + 37, j + 37, k + 3, FRAME);
        setVerticalLine(world, i - 11, j + 38, j + 38, k + 3, FRAME);
        setVerticalLine(world, i - 12, j + 38, j + 38, k + 3, FRAME);
        setVerticalLine(world, i - 9, j + 39, j + 39, k + 4, FRAME);
        setVerticalLine(world, i - 10, j + 39, j + 39, k + 4, FRAME);
        setVerticalLine(world, i - 11, j + 38, j + 38, k + 4, FRAME);
        setVerticalLine(world, i - 12, j + 38, j + 38, k + 4, FRAME);
        setVerticalLine(world, i - 13, j + 37, j + 37, k + 4, FRAME);
        setVerticalLine(world, i - 14, j + 37, j + 38, k + 5, FRAME);
        setVerticalLine(world, i - 15, j + 37, j + 37, k + 5, FRAME);
        setVerticalLine(world, i - 16, j + 37, j + 37, k + 5, FRAME);
        setVerticalLine(world, i - 7, j + 40, j + 40, k + 5, FRAME);
        setVerticalLine(world, i - 8, j + 40, j + 40, k + 5, FRAME);
        setVerticalLine(world, i - 9, j + 39, j + 39, k + 5, FRAME);
        setVerticalLine(world, i - 10, j + 39, j + 39, k + 5, FRAME);
        setVerticalLine(world, i - 11, j + 38, j + 38, k + 5, FRAME);
        setVerticalLine(world, i - 12, j + 38, j + 38, k + 5, FRAME);
        setVerticalLine(world, i - 13, j + 37, j + 37, k + 5, FRAME);
        setVerticalLine(world, i - 6, j + 41, j + 41, k + 6, FRAME);
        setVerticalLine(world, i - 7, j + 40, j + 40, k + 6, FRAME);
        setVerticalLine(world, i - 8, j + 40, j + 40, k + 6, FRAME);
        setVerticalLine(world, i - 9, j + 39, j + 39, k + 6, FRAME);
        setVerticalLine(world, i - 10, j + 39, j + 39, k + 6, FRAME);
        setVerticalLine(world, i - 11, j + 38, j + 38, k + 6, FRAME);
        setVerticalLine(world, i - 12, j + 38, j + 38, k + 6, FRAME);
        setVerticalLine(world, i - 13, j + 37, j + 37, k + 6, FRAME);
        setVerticalLine(world, i - 14, j + 37, j + 37, k + 6, FRAME);
        setVerticalLine(world, i - 15, j + 37, j + 37, k + 6, FRAME);
        setVerticalLine(world, i - 5, j + 41, j + 41, k + 7, FRAME);
        setVerticalLine(world, i - 6, j + 41, j + 41, k + 7, FRAME);
        setVerticalLine(world, i - 7, j + 40, j + 40, k + 7, FRAME);
        setVerticalLine(world, i - 8, j + 40, j + 40, k + 7, FRAME);
        setVerticalLine(world, i - 9, j + 39, j + 39, k + 7, FRAME);
        setVerticalLine(world, i - 10, j + 39, j + 39, k + 7, FRAME);
        setVerticalLine(world, i - 11, j + 38, j + 38, k + 7, FRAME);
        setVerticalLine(world, i - 12, j + 38, j + 38, k + 7, FRAME);
        setVerticalLine(world, i - 13, j + 37, j + 37, k + 7, FRAME);
        setVerticalLine(world, i - 3, j + 42, j + 42, k + 8, FRAME);
        setVerticalLine(world, i - 4, j + 42, j + 42, k + 8, FRAME);
        setVerticalLine(world, i - 5, j + 41, j + 41, k + 8, FRAME);
        setVerticalLine(world, i - 6, j + 41, j + 41, k + 8, FRAME);
        setVerticalLine(world, i - 7, j + 40, j + 40, k + 8, FRAME);
        setVerticalLine(world, i - 8, j + 40, j + 40, k + 8, FRAME);
        setVerticalLine(world, i - 9, j + 39, j + 39, k + 8, FRAME);
        setVerticalLine(world, i - 10, j + 39, j + 39, k + 8, FRAME);
        setVerticalLine(world, i - 11, j + 38, j + 38, k + 8, FRAME);
        setVerticalLine(world, i - 12, j + 38, j + 38, k + 8, FRAME);
        setVerticalLine(world, i - 13, j + 37, j + 37, k + 8, FRAME);
        setVerticalLine(world, i - 2, j + 42, j + 43, k + 9, FRAME);
        setVerticalLine(world, i - 3, j + 42, j + 42, k + 9, FRAME);
        setVerticalLine(world, i - 4, j + 41, j + 42, k + 9, FRAME);
        setVerticalLine(world, i - 5, j + 41, j + 41, k + 9, FRAME);
        setVerticalLine(world, i - 6, j + 41, j + 41, k + 9, FRAME);
        setVerticalLine(world, i - 7, j + 40, j + 41, k + 9, FRAME);
        setVerticalLine(world, i - 8, j + 40, j + 40, k + 9, FRAME);
        setVerticalLine(world, i - 9, j + 39, j + 40, k + 9, FRAME);
        setVerticalLine(world, i - 10, j + 39, j + 39, k + 9, FRAME);
        setVerticalLine(world, i - 2, j + 42, j + 42, k + 10, FRAME);
        setVerticalLine(world, i - 3, j + 42, j + 42, k + 10, FRAME);
        setVerticalLine(world, i - 4, j + 41, j + 43, k + 10, FRAME);
        setVerticalLine(world, i - 5, j + 40, j + 41, k + 10, FRAME);
        setVerticalLine(world, i - 6, j + 40, j + 40, k + 10, FRAME);
        setVerticalLine(world, i - 7, j + 40, j + 40, k + 10, FRAME);
        setVerticalLine(world, i - 8, j + 40, j + 41, k + 10, FRAME);
        setVerticalLine(world, i - 9, j + 40, j + 40, k + 10, FRAME);
        setVerticalLine(world, i - 5, j + 40, j + 40, k + 11, FRAME);
        setVerticalLine(world, i - 6, j + 39, j + 39, k + 11, FRAME);
        setVerticalLine(world, i - 8, j + 42, j + 42, k + 11, FRAME);
        setVerticalLine(world, i - 9, j + 40, j + 40, k + 11, FRAME);
        setVerticalLine(world, i - 1, j + 37, j + 37, k + 14, FRAME);

        setVerticalLine(world, i - 12, j + 35, j + 35, k - 5, AIR);
        setVerticalLine(world, i - 13, j + 34, j + 34, k - 6, AIR);
        setVerticalLine(world, i - 12, j + 34, j + 35, k - 4, AIR);
        setVerticalLine(world, i - 16, j + 34, j + 34, k - 4, AIR);
        setVerticalLine(world, i - 3, j + 40, j + 40, k + 11, AIR);
        setVerticalLine(world, i - 7, j + 41, j + 41, k + 6, AIR);
        setVerticalLine(world, i - 8, j + 42, j + 42, k + 10, AIR);
        setVerticalLine(world, i - 9, j + 40, j + 40, k + 8, AIR);
        setVerticalLine(world, i - 11, j + 39, j + 39, k + 8, AIR);
        setVerticalLine(world, i - 14, j + 38, j + 38, k + 5, AIR);
        setVerticalLine(world, i - 1, j + 37, j + 37, k + 14, AIR);
        setVerticalLine(world, i - 2, j + 44, j + 44, k + 8, AIR);
        setVerticalLine(world, i + 0, j + 42, j + 42, k + 8, AIR);
        setVerticalLine(world, i + 0, j + 43, j + 43, k + 9, AIR);
        setVerticalLine(world, i + 1, j + 45, j + 48, k + 6, AIR);
        setVerticalLine(world, i + 1, j + 47, j + 47, k + 7, AIR);
        setVerticalLine(world, i + 1, j + 51, j + 51, k - 4, AIR);
        setVerticalLine(world, i + 1, j + 51, j + 51, k - 3, AIR);
        setVerticalLine(world, i + 1, j + 51, j + 51, k - 2, AIR);
        setVerticalLine(world, i + 1, j + 51, j + 51, k - 1, AIR);
        setVerticalLine(world, i + 2, j + 49, j + 51, k - 4, AIR);
        setVerticalLine(world, i + 2, j + 51, j + 51, k - 3, AIR);
        setVerticalLine(world, i + 2, j + 49, j + 51, k - 2, AIR);
        setVerticalLine(world, i + 2, j + 49, j + 51, k - 1, AIR);
        setVerticalLine(world, i + 2, j + 47, j + 47, k + 3, AIR);
        setVerticalLine(world, i + 3, j + 51, j + 51, k - 4, AIR);
        setVerticalLine(world, i + 3, j + 50, j + 51, k - 1, AIR);
        setVerticalLine(world, i + 3, j + 50, j + 50, k + 0, AIR);
        setVerticalLine(world, i + 3, j + 47, j + 47, k + 7, AIR);
        setVerticalLine(world, i + 4, j + 51, j + 51, k - 4, AIR);
        setVerticalLine(world, i + 4, j + 50, j + 50, k + 0, AIR);
        setVerticalLine(world, i + 4, j + 47, j + 47, k + 2, AIR);
        setVerticalLine(world, i + 5, j + 50, j + 50, k - 2, AIR);
        setVerticalLine(world, i + 5, j + 48, j + 50, k - 1, AIR);
        setVerticalLine(world, i + 5, j + 50, j + 50, k + 1, AIR);
        setVerticalLine(world, i + 5, j + 50, j + 50, k + 2, AIR);
        setVerticalLine(world, i + 6, j + 47, j + 47, k + 3, AIR);
        setVerticalLine(world, i + 6, j + 47, j + 47, k + 4, AIR);
        setVerticalLine(world, i + 7, j + 47, j + 48, k - 1, AIR);
        setVerticalLine(world, i + 7, j + 47, j + 47, k + 4, AIR);

        world.setBlock(i - 3, j + 41, k - 1, GLOWING, 0, 2);
        world.setBlock(i - 1, j + 44, k + 8, GLOWING, 0, 2);
        world.setBlock(i - 12, j + 39, k + 7, GLOWING, 0, 2);
        world.setBlock(i - 16, j + 35, k - 5, GLOWING, 0, 2);
        world.setBlock(i + 5, j + 50, k - 3, GLOWING, 0, 2);
        world.setBlock(i + 6, j + 47, k + 4, GLOWING, 0, 2);
        world.setBlock(i + 3, j + 55, k - 5, GLOWING, 0, 2);
        world.setBlock(i + 4, j + 55, k + 2, GLOWING, 0, 2);
        world.setBlock(i - 13, j + 37, k - 1, GLOWING, 0, 2);
        world.setBlock(i - 4, j + 44, k + 10, GLOWING, 0, 2);
        }

    private void generateLayer30(World world, int i, int j, int k) { 
        setHorizontalLine(world, i - 8, i - 6, j + 31, k - 10, FRAME);
        setHorizontalLine(world, i - 8, i - 6, j + 31, k - 9, FRAME);  
        world.setBlock(i - 8, j + 31, k - 8, FRAME, 1, 2);

        setHorizontalLine(world, i - 7, i - 6, j + 30, k - 18, FRAME);
        setHorizontalLine(world, i - 7, i - 6, j + 30, k - 17, FRAME);
        setHorizontalLine(world, i - 7, i - 6, j + 30, k - 16, FRAME);
        setHorizontalLine(world, i - 7, i - 6, j + 30, k - 15, FRAME);
        setHorizontalLine(world, i - 7, i - 6, j + 30, k - 14, FRAME);
        setHorizontalLine(world, i - 7, i - 6, j + 30, k - 13, FRAME);
        setHorizontalLine(world, i - 7, i - 6, j + 30, k - 12, FRAME);
        setHorizontalLine(world, i - 7, i - 6, j + 30, k - 11, FRAME);
        setHorizontalLine(world, i - 7, i - 6, j + 30, k - 10, FRAME);
        setHorizontalLine(world, i - 7, i - 6, j + 30, k - 9, FRAME);

        setHorizontalLine(world, i - 5, i - 4, j + 29, k - 18, FRAME);
        setHorizontalLine(world, i - 5, i - 4, j + 29, k - 17, FRAME);
        setHorizontalLine(world, i - 5, i - 4, j + 29, k - 16, FRAME);
        setHorizontalLine(world, i - 5, i - 4, j + 29, k - 15, FRAME);
        setHorizontalLine(world, i - 5, i - 4, j + 29, k - 14, FRAME);
        setHorizontalLine(world, i - 5, i - 4, j + 29, k - 13, FRAME);
        setHorizontalLine(world, i - 5, i - 4, j + 29, k - 12, FRAME);
        setHorizontalLine(world, i - 5, i - 4, j + 29, k - 11, FRAME);

        setVerticalLine(world, i - 3, j + 28, j + 29, k - 19, FRAME);
        setHorizontalLine(world, i - 3, i - 2, j + 28, k - 18, FRAME);
        setHorizontalLine(world, i - 3, i - 2, j + 28, k - 17, FRAME);
        setHorizontalLine(world, i - 3, i - 2, j + 28, k - 16, FRAME);
        setHorizontalLine(world, i - 3, i - 2, j + 28, k - 15, FRAME);
        setHorizontalLine(world, i - 3, i - 2, j + 28, k - 14, FRAME);
        setHorizontalLine(world, i - 3, i - 2, j + 28, k - 13, FRAME);
        world.setBlock(i - 3, j + 29, k - 18, GLOWING, 0, 2);

        setHorizontalLine(world, i - 1, i + 0, j + 27, k - 19, FRAME);
        setHorizontalLine(world, i - 1, i + 0, j + 27, k - 18, FRAME);
        setHorizontalLine(world, i - 1, i + 0, j + 27, k - 17, FRAME);
        setHorizontalLine(world, i - 1, i + 0, j + 27, k - 16, FRAME);
        setHorizontalLine(world, i - 1, i + 0, j + 27, k - 15, FRAME);
        setHorizontalLine(world, i - 1, i + 0, j + 27, k - 14, FRAME);
        world.setBlock(i - 1, j + 28, k - 19, FRAME, 1, 2);
        world.setBlock(i + 0, j + 28, k - 19, FRAME, 1, 2);

        setVerticalLine(world, i + 1, j + 26, j + 27, k - 19, FRAME);
        setVerticalLine(world, i + 1, j + 26, j + 27, k - 18, FRAME);
        setHorizontalLine(world, i + 1, i + 2, j + 26, k - 17, FRAME);
        setHorizontalLine(world, i + 1, i + 2, j + 26, k - 16, FRAME);
        setHorizontalLine(world, i + 1, i + 2, j + 26, k - 15, FRAME);
        setHorizontalLine(world, i + 1, i + 2, j + 26, k - 14, FRAME);

        setVerticalLine(world, i + 3, j + 25, j + 26, k - 19, FRAME);
        setHorizontalLine(world, i + 3, i + 4, j + 25, k - 18, FRAME);
        setHorizontalLine(world, i + 3, i + 4, j + 25, k - 17, FRAME);
        setHorizontalLine(world, i + 3, i + 4, j + 25, k - 16, FRAME);
        setHorizontalLine(world, i + 3, i + 4, j + 25, k - 15, FRAME);
        setHorizontalLine(world, i + 3, i + 4, j + 25, k - 14, FRAME);
        setHorizontalLine(world, i + 5, i + 6, j + 24, k - 17, FRAME);
        setHorizontalLine(world, i + 5, i + 7, j + 24, k - 16, FRAME);
        setHorizontalLine(world, i + 5, i + 7, j + 24, k - 15, FRAME);
        setHorizontalLine(world, i + 5, i + 7, j + 24, k - 14, FRAME);
        setHorizontalLine(world, i + 5, i + 7, j + 24, k - 13, FRAME);
        world.setBlock(i + 5, j + 24, k - 18, FRAME, 1, 2);
        setHorizontalLine(world, i + 7, i + 8, j + 25, k - 17, FRAME);
        setVerticalLine(world, i + 7, j + 25, j + 26, k - 18, FRAME);
        setVerticalLine(world, i + 8, j + 26, j + 27, k - 17, FRAME);
        world.setBlock(i + 8, j + 28, k - 16, FRAME, 1, 2);
        world.setBlock(i + 8, j + 25, k - 16, GLOWING, 0, 2);

        setHorizontalLine(world, i + 7, i + 13, j + 23, k - 12, FRAME);
        setHorizontalLine(world, i + 7, i + 13, j + 23, k - 11, FRAME);
        setVerticalLine(world, i + 13, j + 22, j + 23, k - 12, FRAME);
        setHorizontalLine(world, i + 9, i + 14, j + 22, k - 10, FRAME);
        setHorizontalLine(world, i + 9, i + 14, j + 22, k - 9, FRAME);
        setHorizontalLine(world, i + 11, i + 14, j + 21, k - 8, FRAME);
        setHorizontalLine(world, i + 11, i + 14, j + 21, k - 7, FRAME);
        setHorizontalLine(world, i + 12, i + 16, j + 20, k - 6, FRAME);
        setHorizontalLine(world, i + 12, i + 17, j + 20, k - 5, FRAME);
        setVerticalLine(world, i + 12, j + 17, j + 19, k - 5, FRAME);
        setHorizontalLine(world, i + 11, i + 12, j + 17, k - 6, FRAME);
        setVerticalLine(world, i + 12, j + 17, j + 18, k - 6, FRAME);
        world.setBlock(i + 12, j + 17, k - 7, FRAME, 1, 2);
        setHorizontalLine(world, i + 11, i + 13, j + 17, k - 4, FRAME);
        setVerticalLine(world, i + 12, j + 17, j + 18, k - 4, FRAME);
        setHorizontalLine(world, i + 13, i + 17, j + 19, k - 4, FRAME);
        setHorizontalLine(world, i + 14, i + 17, j + 19, k - 3, FRAME);

        setHorizontalLine(world, i - 7, i - 5, j + 42, k + 9, FRAME);
        setVerticalLine(world, i - 5, j + 42, j + 46, k + 9, FRAME);
        setHorizontalLine(world, i - 7, i - 1, j + 46, k + 10, FRAME);
        setHorizontalLine(world, i - 7, i - 1, j + 47, k + 11, FRAME);
        setHorizontalLine(world, i - 7, i - 2, j + 47, k + 12, FRAME);
        setHorizontalLine(world, i - 7, i - 2, j + 47, k + 13, FRAME);
        setHorizontalLine(world, i - 7, i - 2, j + 47, k + 14, FRAME);
        setVerticalLine(world, i - 7, j + 42, j + 45, k + 11, FRAME);
        setVerticalLine(world, i - 7, j + 43, j + 45, k + 12, FRAME);
        setVerticalLine(world, i - 7, j + 43, j + 45, k + 13, FRAME);
        setVerticalLine(world, i - 7, j + 43, j + 45, k + 14, FRAME);
        setHorizontalLine(world, i - 7, i - 1, j + 45, k + 15, FRAME);
        setHorizontalLine(world, i - 6, i - 1, j + 44, k + 15, FRAME);
        setHorizontalLine(world, i - 6, i - 1, j + 43, k + 15, FRAME);
        setHorizontalLine(world, i - 6, i - 2, j + 42, k + 14, FRAME);
        setHorizontalLine(world, i - 6, i - 1, j + 43, k + 13, FRAME);
        setHorizontalLine(world, i - 6, i - 2, j + 42, k + 12, FRAME);
        world.setBlock(i - 6, j + 44, k + 14, GLOWING, 0, 2);

        world.setBlock(i + 6, j + 4, k + 1, POOL, 0, 2);
        world.setBlock(i - 9, j + 32, k - 9, GLOWING, 0, 2);
        setVerticalLine(world, i - 9, j + 36, j + 37, k - 13, FRAME);
        world.setBlock(i - 9, j + 37, k - 12, FRAME, 1, 2);
        world.setBlock(i - 9, j + 33, k - 15, FRAME, 1, 2);

        world.setBlock(i - 7, j + 32, k - 9, GLOWING, 0, 2);
        world.setBlock(i + 12, j + 20, k - 5, GLOWING, 0, 2);
        world.setBlock(i - 7, j + 48, k + 11, GLOWING, 0, 2);
        }

    private void generateLayer31(World world, int i, int j, int k) {     
        setHorizontalLine(world, i + 13, i + 17, j + 18, k - 3, FRAME);
        setHorizontalLine(world, i + 13, i + 17, j + 18, k - 2, FRAME);
        setHorizontalLine(world, i + 12, i + 17, j + 18, k - 1, FRAME);
        setHorizontalLine(world, i + 12, i + 15, j + 17, k - 3, FRAME);
        setHorizontalLine(world, i + 12, i + 15, j + 17, k - 2, FRAME);
        setHorizontalLine(world, i + 12, i + 15, j + 17, k - 1, FRAME);
        setHorizontalLine(world, i + 12, i + 15, j + 17, k + 0, FRAME);
        setHorizontalLine(world, i + 12, i + 15, j + 17, k + 1, FRAME);
        setHorizontalLine(world, i + 13, i + 14, j + 17, k + 2, FRAME);
        setHorizontalLine(world, i + 13, i + 14, j + 17, k + 3, FRAME);
        world.setBlock(i + 13, j + 19, k - 3, FRAME, 1, 2);
        world.setBlock(i + 17, j + 18, k - 4, FRAME, 1, 2);
        world.setBlock(i + 16, j + 18, k + 3, GLOWING, 0, 2);
        world.setBlock(i + 6, j + 18, k + 6, GLOWING, 0, 2);

        setHorizontalLine(world, i + 12, i + 14, j + 20, k - 9, FRAME);
        world.setBlock(i + 14, j + 20, k - 8, FRAME, 1, 2);
        setHorizontalLine(world, i + 12, i + 14, j + 21, k - 9, FRAME);
        world.setBlock(i + 13, j + 21, k - 10, FRAME, 1, 2);
        setHorizontalLine(world, i + 11, i + 13, j + 22, k - 11, FRAME);
        world.setBlock(i + 12, j + 22, k - 12, FRAME, 1, 2);
        setHorizontalLine(world, i + 8, i + 11, j + 23, k - 13, FRAME);
        setHorizontalLine(world, i + 9, i + 10, j + 23, k - 14, FRAME);
        setHorizontalLine(world, i + 8, i + 9, j + 23, k - 15, FRAME);
        setHorizontalLine(world, i + 6, i + 7, j + 23, k - 16, FRAME);
        world.setBlock(i + 7, j + 22, k - 16, FRAME, 1, 2);
        world.setBlock(i + 6, j + 23, k - 17, FRAME, 1, 2);
        world.setBlock(i + 5, j + 23, k - 18, FRAME, 1, 2);
        setVerticalLine(world, i + 4, j + 23, j + 24, k - 19, FRAME);
        setHorizontalLine(world, i + 3, i + 4, j + 24, k - 15, FRAME);
        world.setBlock(i + 14, j + 24, k - 11, GLOWING, 0, 2);

        setHorizontalLine(world, i + 1, i + 2, j + 25, k - 19, FRAME);
        world.setBlock(i + 2, j + 25, k - 18, FRAME, 1, 2);
        world.setBlock(i + 2, j + 25, k - 15, FRAME, 1, 2);
        setHorizontalLine(world, i + 0, i + 1, j + 26, k - 19, FRAME);
        setHorizontalLine(world, i + 0, i + 5, j + 26, k - 18, FRAME);
        setHorizontalLine(world, i + 0, i + 6, j + 26, k - 17, FRAME);
        setHorizontalLine(world, i - 6, i + 7, j + 27, k - 16, FRAME);
        setHorizontalLine(world, i - 7, i + 7, j + 28, k - 15, FRAME);
        setHorizontalLine(world, i - 8, i + 7, j + 29, k - 14, FRAME);
        setHorizontalLine(world, i - 8, i - 10, j + 30, k - 13, FRAME);
        setHorizontalLine(world, i - 10, i - 12, j + 31, k - 12, FRAME);
        setHorizontalLine(world, i - 12, i - 14, j + 32, k - 11, FRAME);
        world.setBlock(i - 12, j + 32, k - 9, FRAME, 1, 2);
        world.setBlock(i - 12, j + 32, k - 8, FRAME, 1, 2);
        world.setBlock(i - 17, j + 32, k - 7, FRAME, 1, 2);

        setHorizontalLine(world, i - 18, i - 15, j + 33, k - 5, FRAME);
        setHorizontalLine(world, i - 17, i - 12, j + 34, k - 3, FRAME);
        setHorizontalLine(world, i - 19, i - 14, j + 35, k - 1, FRAME);
        setHorizontalLine(world, i - 18, i - 14, j + 36, k + 1, FRAME);
        setHorizontalLine(world, i - 11, i - 12, j + 37, k + 4, FRAME);
        setHorizontalLine(world, i - 10, i - 8, j + 38, k + 10, FRAME);
        setHorizontalLine(world, i - 8, i - 6, j + 39, k + 7, FRAME);
        setHorizontalLine(world, i - 6, i - 4, j + 40, k + 8, FRAME);
        setHorizontalLine(world, i - 4, i + 1, j + 41, k + 8, FRAME);
        setHorizontalLine(world, i - 1, i + 4, j + 42, k + 9, FRAME);
        setHorizontalLine(world, i + 2, i + 5, j + 43, k + 7, FRAME);
        setHorizontalLine(world, i + 2, i + 5, j + 44, k + 6, FRAME);
        setHorizontalLine(world, i + 2, i + 5, j + 45, k + 4, FRAME);
        setHorizontalLine(world, i + 3, i + 5, j + 46, k + 2, FRAME);
        world.setBlock(i - 1, j + 42, k + 11, FRAME, 1, 2);

        setVerticalLine(world, i - 15, j + 33, j + 34, k - 5, AIR);
        setVerticalLine(world, i - 12, j + 34, j + 35, k - 4, AIR);
        world.setBlock(i - 5, j + 40, k + 8, AIR, 0, 2);
        world.setBlock(i - 1, j + 41, k + 11, AIR, 0, 2);
        world.setBlock(i + 2, j + 43, k + 4, AIR, 0, 2);
        world.setBlock(i + 3, j + 45, k + 2, AIR, 0, 2);
        world.setBlock(i + 5, j + 42, k + 7, AIR, 0, 2);
        setHorizontalLine(world, i - 1, i + 2, j + 27, k - 19, AIR);
        setHorizontalLine(world, i - 1, i + 2, j + 28, k - 19, AIR);
        setHorizontalLine(world, i - 1, i + 2, j + 29, k - 19, AIR);
        setHorizontalLine(world, i - 1, i + 2, j + 30, k - 19, AIR);
        setHorizontalLine(world, i + 0, i + 2, j + 27, k - 20, AIR);
        setHorizontalLine(world, i + 0, i + 2, j + 28, k - 20, AIR);
        setHorizontalLine(world, i + 0, i + 1, j + 29, k - 20, AIR);
        world.setBlock(i + 3, j + 27, k - 23, AIR, 0, 2);
        world.setBlock(i - 5, j + 28, k - 21, AIR, 0, 2);
        world.setBlock(i + 5, j + 27, k - 24, AIR, 0, 2);

        world.setBlock(i + 13, j + 20, k - 3, GLOWING, 0, 2);
        world.setBlock(i + 4, j + 25, k - 19, GLOWING, 0, 2);
        world.setBlock(i - 14, j + 33, k - 11, GLOWING, 0, 2);
        world.setBlock(i + 5, j + 47, k + 2, GLOWING, 0, 2);
        }

    private void generateLayer32(World world, int i, int j, int k) {
        world.setBlock(i - 2, j + 15, k + 9, FRAME, 1, 2);
        setHorizontalLine(world, i - 2, i + 1, j + 17, k - 2, FRAME);
        setHorizontalLine(world, i - 2, i + 1, j + 18, k - 2, FRAME);
        setHorizontalLine(world, i - 1, i + 1, j + 17, k + 1, FRAME);
        setHorizontalLine(world, i - 2, i + 2, j + 18, k + 1, FRAME);
        setHorizontalLine(world, i - 1, i + 0, j + 17, k + 2, FRAME);
        setHorizontalLine(world, i - 2, i - 1, j + 18, k + 3, FRAME);
        world.setBlock(i - 1, j + 17, k + 3, FRAME, 1, 2);
        world.setBlock(i - 1, j + 17, k + 2, GLOWING, 0, 2);
        world.setBlock(i - 2, j + 18, k - 4, GLOWING, 0, 2);
        setHorizontalLine(world, i + 5, i + 6, j + 25, k - 23, FRAME);
        setHorizontalLine(world, i + 5, i + 6, j + 25, k - 22, FRAME);
        world.setBlock(i + 6, j + 25, k - 24, FRAME, 1, 2);
        world.setBlock(i + 5, j + 25, k - 21, FRAME, 1, 2);
        setHorizontalLine(world, i + 5, i + 7, j + 26, k - 25, FRAME);
        setHorizontalLine(world, i + 6, i + 7, j + 26, k - 24, FRAME);
        setHorizontalLine(world, i + 6, i + 7, j + 26, k - 23, FRAME);
        world.setBlock(i + 7, j + 26, k - 22, FRAME, 1, 2);
        world.setBlock(i + 6, j + 27, k - 24, FRAME, 1, 2);
        world.setBlock(i + 5, j + 27, k - 20, GLOWING, 0, 2);

        world.setBlock(i - 6, j + 28, k - 21, GLOWING, 0, 2);
        setHorizontalLine(world, i + 2, i + 6, j + 29, k - 27, FRAME);
        setHorizontalLine(world, i + 3, i + 5, j + 29, k - 28, FRAME);
        setHorizontalLine(world, i + 7, i + 7, j + 29, k - 26, FRAME);
        setHorizontalLine(world, i + 7, i + 7, j + 29, k - 25, FRAME);
        setHorizontalLine(world, i + 7, i + 7, j + 29, k - 24, FRAME);
        setHorizontalLine(world, i + 7, i + 7, j + 29, k - 23, FRAME);
        world.setBlock(i + 7, j + 29, k - 21, FRAME, 1, 2);
        world.setBlock(i + 6, j + 29, k - 20, FRAME, 1, 2);
        world.setBlock(i + 5, j + 29, k - 19, FRAME, 1, 2);
        setHorizontalLine(world, i - 7, i + 6, j + 30, k - 24, FRAME);
        setHorizontalLine(world, i - 7, i + 7, j + 30, k - 23, FRAME);
        setHorizontalLine(world, i - 7, i + 7, j + 30, k - 22, FRAME);
        setHorizontalLine(world, i - 7, i + 7, j + 30, k - 21, FRAME);
        setHorizontalLine(world, i - 7, i + 6, j + 30, k - 20, FRAME);
        setHorizontalLine(world, i - 7, i + 5, j + 30, k - 19, FRAME);
        setHorizontalLine(world, i - 6, i + 6, j + 31, k - 25, FRAME);
        setHorizontalLine(world, i - 5, i + 6, j + 31, k - 24, FRAME);
        setHorizontalLine(world, i - 4, i + 6, j + 31, k - 23, FRAME);
        setHorizontalLine(world, i - 6, i + 6, j + 31, k - 22, FRAME);
        setHorizontalLine(world, i - 6, i + 6, j + 31, k - 21, FRAME);
        setHorizontalLine(world, i - 6, i + 5, j + 31, k - 20, FRAME);
        world.setBlock(i - 2, j + 32, k - 20, GLOWING, 0, 2);
        setHorizontalLine(world, i - 6, i + 6, j + 32, k - 26, FRAME);
        setHorizontalLine(world, i - 5, i + 6, j + 32, k - 25, FRAME);
        setHorizontalLine(world, i - 4, i + 6, j + 32, k - 24, FRAME);
        setHorizontalLine(world, i - 6, i + 6, j + 32, k - 23, FRAME);
        setHorizontalLine(world, i - 5, i + 5, j + 32, k - 22, FRAME);
        setHorizontalLine(world, i - 5, i + 5, j + 32, k - 21, FRAME);
        setHorizontalLine(world, i - 5, i + 5, j + 32, k - 20, FRAME);
        setHorizontalLine(world, i - 5, i + 5, j + 33, k - 25, FRAME);
        setHorizontalLine(world, i - 4, i + 5, j + 33, k - 24, FRAME);
        setHorizontalLine(world, i - 5, i + 5, j + 33, k - 23, FRAME);
        setHorizontalLine(world, i - 5, i + 5, j + 33, k - 22, FRAME);
        setHorizontalLine(world, i - 5, i + 4, j + 33, k - 21, FRAME);
        setHorizontalLine(world, i - 5, i + 4, j + 33, k - 20, FRAME);
        setHorizontalLine(world, i - 4, i + 4, j + 34, k - 24, FRAME);
        setHorizontalLine(world, i - 4, i + 3, j + 34, k - 23, FRAME);
        setHorizontalLine(world, i - 4, i + 3, j + 34, k - 22, FRAME);
        setHorizontalLine(world, i - 4, i + 3, j + 34, k - 21, FRAME);
        setHorizontalLine(world, i - 1, i + 4, j + 34, k - 20, FRAME);
        setHorizontalLine(world, i - 3, i + 2, j + 35, k - 24, FRAME);
        setHorizontalLine(world, i - 3, i + 2, j + 35, k - 23, FRAME);
        setHorizontalLine(world, i - 3, i + 2, j + 35, k - 22, FRAME);
        setHorizontalLine(world, i - 1, i + 2, j + 35, k - 21, FRAME);
        setHorizontalLine(world, i + 0, i + 1, j + 36, k - 23, FRAME);
        setHorizontalLine(world, i + 0, i + 1, j + 36, k - 22, FRAME);

        setVerticalLine(world, i - 5, j + 19, j + 20, k - 1, FRAME);
        setVerticalLine(world, i - 4, j + 19, j + 20, k - 2, FRAME);
        setVerticalLine(world, i - 4, j + 19, j + 20, k + 1, FRAME);
        setVerticalLine(world, i - 3, j + 19, j + 20, k - 2, FRAME);
        setVerticalLine(world, i - 1, j + 19, j + 20, k - 2, FRAME);
        setVerticalLine(world, i + 1, j + 19, j + 20, k - 2, FRAME);
        setVerticalLine(world, i + 1, j + 19, j + 20, k + 1, FRAME);
        world.setBlock(i - 1, j + 19, k - 4, FRAME, 1, 2);
        world.setBlock(i - 4, j + 21, k + 1, FRAME, 1, 2);
        world.setBlock(i + 2, j + 19, k + 1, FRAME, 1, 2);

        setHorizontalLine(world, i - 6, i + 1, j + 17, k - 1, AIR);
        setHorizontalLine(world, i - 6, i + 1, j + 18, k - 1, AIR);
        setHorizontalLine(world, i - 6, i + 1, j + 17, k + 0, AIR);
        setHorizontalLine(world, i - 6, i + 1, j + 18, k + 0, AIR);
        setHorizontalLine(world, i - 4, i + 1, j + 19, k - 2, AIR);
        setHorizontalLine(world, i - 4, i + 1, j + 19, k - 1, AIR);
        setHorizontalLine(world, i - 4, i + 1, j + 19, k + 0, AIR);
        setHorizontalLine(world, i - 4, i + 1, j + 19, k + 1, AIR);
        setHorizontalLine(world, i - 4, i + 1, j + 20, k - 2, AIR);
        setHorizontalLine(world, i - 4, i + 1, j + 20, k - 1, AIR);
        setHorizontalLine(world, i - 3, i + 1, j + 20, k + 0, AIR);
        setHorizontalLine(world, i - 2, i + 0, j + 20, k + 1, AIR);
        setHorizontalLine(world, i - 3, i - 1, j + 21, k - 1, AIR);
        setHorizontalLine(world, i - 2, i + 0, j + 21, k + 0, AIR);
        setHorizontalLine(world, i - 4, i - 1, j + 17, k - 3, AIR);
        setHorizontalLine(world, i - 3, i - 1, j + 18, k - 3, AIR);
        setHorizontalLine(world, i - 2, i - 1, j + 18, k - 4, AIR);
        setHorizontalLine(world, i - 4, i - 1, j + 19, k - 3, AIR);
        setHorizontalLine(world, i - 3, i - 2, j + 17, k + 2, AIR);
        setHorizontalLine(world, i - 3, i + 0, j + 18, k + 2, AIR);
        setHorizontalLine(world, i - 2, i - 1, j + 17, k + 3, AIR);
        setHorizontalLine(world, i - 1, i + 0, j + 19, k + 2, AIR);
        setHorizontalLine(world, i + 5, i + 6, j + 26, k - 23, AIR);
        setHorizontalLine(world, i + 5, i + 6, j + 27, k - 25, AIR);
        world.setBlock(i + 5, j + 30, k - 25, AIR, 0, 2);
        world.setBlock(i - 5, j + 31, k - 21, AIR, 0, 2);
        world.setBlock(i + 2, j + 33, k - 21, AIR, 0, 2);
        world.setBlock(i - 1, j + 33, k - 21, AIR, 0, 2);
        world.setBlock(i + 1, j + 34, k - 26, AIR, 0, 2);
        world.setBlock(i + 2, j + 36, k - 21, AIR, 0, 2);
        world.setBlock(i + 0, j + 22, k + 0, AIR, 0, 2);
        world.setBlock(i - 2, j + 20, k - 3, AIR, 0, 2);
        world.setBlock(i - 3, j + 19, k + 2, AIR, 0, 2);
        world.setBlock(i - 1, j + 19, k + 3, AIR, 0, 2);
        world.setBlock(i - 5, j + 19, k - 1, AIR, 0, 2);

        world.setBlock(i - 2, j + 19, k + 1, GLOWING, 0, 2);
        world.setBlock(i + 7, j + 28, k - 25, GLOWING, 0, 2);
        world.setBlock(i + 0, j + 37, k - 22, GLOWING, 0, 2);
        world.setBlock(i - 5, j + 22, k - 1, GLOWING, 0, 2);
    }
    
}
