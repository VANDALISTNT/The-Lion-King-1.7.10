package lionking.world;

import net.minecraft.world.World;
import net.minecraft.util.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenerator;
import java.util.Random;
import cpw.mods.fml.common.FMLLog;
import lionking.mod_LionKing;
import lionking.entity.LKEntityRafiki;

public class LKWorldGenRafiki extends WorldGenerator {
    private static final Block RAFIKI_WOOD = mod_LionKing.rafikiWood;
    private static final Block RAFIKI_LEAVES = mod_LionKing.rafikiLeaves;
    private static final Block TORCH = Blocks.torch;

    public LKWorldGenRafiki() {
        super(false);
        FMLLog.fine("Initialized LKWorldGenRafiki");
    }

    public boolean generate(World world, Random random, int i, int j, int k) {
        FMLLog.fine("Attempting to generate Rafiki tree at (%d, %d, %d)", i, j, k);

        if (RAFIKI_WOOD == null || RAFIKI_LEAVES == null) {
            FMLLog.severe("Cannot generate Rafiki tree at (%d, %d, %d): rafikiWood or rafikiLeaves is null", i, j, k);
            return false;
        }

        if (j < 1 || j + 56 >= world.getHeight()) {
            FMLLog.fine("Cannot generate Rafiki tree at (%d, %d, %d): invalid height", i, j, k);
            return false;
        }

        for (int j1 = 0; j1 < 40; j1++) {
            int radius = 20 - MathHelper.floor_double(j1 / 4.0);
            int offsetX = MathHelper.floor_double(j1 / 6.0);
            for (int i1 = -1; i1 >= -2; i1--) {
                for (int k1 = -1; k1 >= -2; k1--) {
                    generateCircle(world, i + i1 + offsetX, j + j1, k + k1, radius, RAFIKI_WOOD, 0, true);
                    generateCircle(world, i + i1 + offsetX, j + j1 - 1, k + k1, radius, RAFIKI_WOOD, 0, true);
                    generateCircle(world, i + i1 + offsetX, j + j1 - 2, k + k1, radius, RAFIKI_WOOD, 0, true);
                }
            }
        }

        for (int i1 = -3; i1 < 15; i1++) {
            world.setBlock(i + i1, j + 39, k, Blocks.air, 0, 2);
            FMLLog.fine("Cleared air at (%d, %d, %d)", i + i1, j + 39, k);
            world.setBlock(i + i1, j + 38, k, RAFIKI_WOOD, 2, 2);
            FMLLog.fine("Placed rafikiWood at (%d, %d, %d), meta=2", i + i1, j + 38, k);
        }

        generateCircle(world, i + 6, j + 39, k - 1, 8, Blocks.air, 0, true);
        generateCircle(world, i + 6, j + 39, k, 8, Blocks.air, 0, true);
        generateCircle(world, i + 5, j + 39, k - 1, 8, Blocks.air, 0, true);
        generateCircle(world, i + 5, j + 39, k, 8, Blocks.air, 0, true);
        generateCircle(world, i + 6, j + 38, k, 10, RAFIKI_WOOD, 2, true);

        for (int i1 = -1; i1 >= -2; i1--) {
            for (int k1 = -1; k1 >= -2; k1--) {
                generateCircle(world, i + i1 + 6, j + 40, k + k1, 11, RAFIKI_WOOD, 1, false);
                generateCircle(world, i + i1 + 6, j + 40, k + k1, 10, Blocks.air, 0, false);
            }
        }

        placeUpperTorches(world, i, j, k);
        finishGeneration1(world, i, j, k);
        finishGeneration2(world, i, j, k);
        finishGeneration3(world, i, j, k);
        finishGeneration4(world, i, j, k);
        finishGeneration5(world, i, j, k);
        finishGeneration6(world, i, j, k);
        finishGeneration7(world, i, j, k);
        finishGeneration8(world, i, j, k);
        finishGeneration9(world, i, j, k);
        finishGeneration10(world, i, j, k);
        finishGeneration11(world, i, j, k);
        finishGeneration12(world, i, j, k);
        finishGeneration13(world, i, j, k);
        finishGeneration14(world, i, j, k);
        finishGeneration15(world, i, j, k);
        finishGeneration16(world, i, j, k);

        if (!world.isRemote) {
            LKEntityRafiki rafiki = new LKEntityRafiki(world);
            rafiki.setLocationAndAngles(i, j + 39, k, 0.0F, 0.0F);
            rafiki.isThisTheRealRafiki = true;
            if (world.spawnEntityInWorld(rafiki)) {
                FMLLog.fine("Successfully spawned Rafiki at (%d, %d, %d)", i, j + 39, k);
            } else {
                FMLLog.warning("Failed to spawn Rafiki at (%d, %d, %d)", i, j + 39, k);
            }
        }

        FMLLog.info("Successfully generated Rafiki tree at (%d, %d, %d)", i, j, k);
        return true;
    }

    private int generateCircle(World world, int i0, int j, int k0, int radius, Block block, int metadata, boolean replaceable) {
        int blocksPlaced = 0;
        FMLLog.fine("Generating circle at (%d, %d, %d) with radius=%d, block=%s, metadata=%d, replaceable=%b", 
            i0, j, k0, radius, block.getUnlocalizedName(), metadata, replaceable);

        int x = 0;
        int z = radius;
        int f = 1 - radius;
        int ddF_x = 1;
        int ddF_z = -2 * radius;

        if (!replaceable || isBlockReplaceable(world, i0, j, k0 + radius)) {
            world.setBlock(i0, j, k0 + radius, block, metadata, 2);
            FMLLog.fine("Placed %s at (%d, %d, %d), metadata=%d", block.getUnlocalizedName(), i0, j, k0 + radius, metadata);
            blocksPlaced++;
        }
        if (!replaceable || isBlockReplaceable(world, i0, j, k0 - radius)) {
            world.setBlock(i0, j, k0 - radius, block, metadata, 2);
            FMLLog.fine("Placed %s at (%d, %d, %d), metadata=%d", block.getUnlocalizedName(), i0, j, k0 - radius, metadata);
            blocksPlaced++;
        }
        if (!replaceable || isBlockReplaceable(world, i0 + radius, j, k0)) {
            world.setBlock(i0 + radius, j, k0, block, metadata, 2);
            FMLLog.fine("Placed %s at (%d, %d, %d), metadata=%d", block.getUnlocalizedName(), i0 + radius, j, k0, metadata);
            blocksPlaced++;
        }
        if (!replaceable || isBlockReplaceable(world, i0 - radius, j, k0)) {
            world.setBlock(i0 - radius, j, k0, block, metadata, 2);
            FMLLog.fine("Placed %s at (%d, %d, %d), metadata=%d", block.getUnlocalizedName(), i0 - radius, j, k0, metadata);
            blocksPlaced++;
        }

        while (x < z) {
            if (f >= 0) {
                z--;
                ddF_z += 2;
                f += ddF_z;
            }
            x++;
            ddF_x += 2;
            f += ddF_x;

        for (int xi = i0 - x; xi <= i0 + x; xi++) {
            if (!replaceable || isBlockReplaceable(world, xi, j, k0 + z)) {
                world.setBlock(xi, j, k0 + z, block, metadata, 2);
                FMLLog.fine("Placed %s at (%d, %d, %d), metadata=%d", block.getUnlocalizedName(), xi, j, k0 + z, metadata);
                blocksPlaced++;
            }
            if (!replaceable || isBlockReplaceable(world, xi, j, k0 - z)) {
                world.setBlock(xi, j, k0 - z, block, metadata, 2);
                FMLLog.fine("Placed %s at (%d, %d, %d), metadata=%d", block.getUnlocalizedName(), xi, j, k0 - z, metadata);
                blocksPlaced++;
            }
        }
        for (int xi = i0 - z; xi <= i0 + z; xi++) {
            if (!replaceable || isBlockReplaceable(world, xi, j, k0 + x)) {
                world.setBlock(xi, j, k0 + x, block, metadata, 2);
                FMLLog.fine("Placed %s at (%d, %d, %d), metadata=%d", block.getUnlocalizedName(), xi, j, k0 + x, metadata);
                blocksPlaced++;
            }
            if (!replaceable || isBlockReplaceable(world, xi, j, k0 - x)) {
                world.setBlock(xi, j, k0 - x, block, metadata, 2);
                FMLLog.fine("Placed %s at (%d, %d, %d), metadata=%d", block.getUnlocalizedName(), xi, j, k0 - x, metadata);
                blocksPlaced++;
               }
            }
        }

        FMLLog.fine("Finished generating circle at (%d, %d, %d), placed %d blocks", i0, j, k0, blocksPlaced);
        return blocksPlaced;
        }

    private boolean isBlockReplaceable(World world, int i, int j, int k) {
        Block block = world.getBlock(i, j, k);
        return block == Blocks.air || block.isLeaves(world, i, j, k) || block == Blocks.tallgrass || block == Blocks.deadbush;
    }

    private void placeUpperTorches(World world, int i, int j, int k) {
        world.setBlock(i + 6, j + 38, k - 2, TORCH, 0, 2);
        world.setBlock(i + 6, j + 38, k + 2, TORCH, 0, 2);
        world.setBlock(i + 4, j + 38, k, TORCH, 0, 2);
        world.setBlock(i + 8, j + 38, k, TORCH, 0, 2);
        FMLLog.fine("Placed torches at (%d, %d, %d)", i + 6, j + 38, k);
        
        int[][] torchPositions39 = {
            {-2, -5}, {-3, -3}, {-3, 2}, {-2, 4}, {1, 7}, {3, 8}, {8, 8}, {10, 7},
            {13, 4}, {14, 2}, {14, -3}, {13, -5}, {10, -8}, {8, -9}, {3, -9}, {1, -8}
        };
        int[][] torchPositions40 = {
            {-3, -7}, {-4, -5}, {-5, -3}, {-5, 2}, {-4, 4}, {-3, 6}, {-1, 8}, {1, 9},
            {3, 10}, {8, 10}, {10, 9}, {12, 8}, {14, 6}, {15, 4}, {16, 2}, {16, -3},
            {15, -5}, {14, -7}, {12, -9}, {10, -10}, {8, -11}, {3, -11}, {1, -10}, {-1, -9}
        };

        for (int[] pos : torchPositions39) {
            world.setBlock(i + pos[0], j + 39, k + pos[1], TORCH, 5, 2);
        }
        for (int[] pos : torchPositions40) {
            world.setBlock(i + pos[0], j + 40, k + pos[1], TORCH, 5, 2);
        }
    }

    private void finishGeneration1(World world, int i, int j, int k) {
        for (int j1 = 40; j1 < 56; j1++) {
        for (int i1 = -1; i1 >= -2; i1--) {
        for (int k1 = -1; k1 >= -2; k1--) {
            generateCircle(world, i + i1 + 6, j + j1, k + k1, 11, RAFIKI_LEAVES, 0, false);
            }
            }
        }
        int[][] branchPositions1 = {
            {-2, 36, 9, 0}, {-2, 36, 10, 0}, {-3, 36, 9, 0}, {-3, 36, 10, 0}, {-4, 36, 9, 0},
            {-4, 36, 10, 0}, {-3, 36, 11, 0}, {-4, 36, 11, 0}, {-5, 36, 10, 0}, {-5, 36, 11, 0},
            {-4, 36, 12, 0}, {-5, 36, 12, 0}, {-4, 35, 10, 0}, {-4, 35, 11, 0}, {-4, 34, 10, 0},
            {-4, 33, 10, 0}, {-5, 35, 10, 0}, {-5, 34, 10, 0}, {-5, 36, 9, 0}, {-4, 37, 10, 0}
        };
        for (int[] pos : branchPositions1) {
            world.setBlock(i + pos[0], j + pos[1], k + pos[2], RAFIKI_WOOD, pos[3], 2);
        }

        for (int x = 3; x <= 8; x++) {
            for (int y = 32; y <= 34; y++) {
                if (y == 32 && x > 5) continue;
                world.setBlock(i + x, j + y, k + 13, RAFIKI_WOOD, 0, 2);
            }
        }
        for (int x = 3; x <= 8; x++) {
            for (int z = 12; z <= 17; z++) {
                if (x == 4 && z == 17) world.setBlock(i + x, j + 38, k + z, Blocks.air, 0, 2);
                else if (x == 8 && z == 16) world.setBlock(i + x, j + 38, k + z, Blocks.air, 0, 2);
                else world.setBlock(i + x, j + 38, k + z, RAFIKI_WOOD, 0, 2);
                world.setBlock(i + x, j + 39, k + z, RAFIKI_WOOD, 0, 2);
                if (x >= 4 && z <= 16) world.setBlock(i + x, j + 40, k + z, RAFIKI_WOOD, 0, 2);
            }
        }
        for (int y = 41; y <= 44; y++) {
            for (int x = 3; x <= 8; x++) {
                for (int z = 14; z <= 18; z++) {
                    if (y == 41 && z < 17 && x > 5) continue;
                    if (y == 44 && z < 16) continue;
                    world.setBlock(i + x, j + y, k + z, RAFIKI_WOOD, 0, 2);
                }
            }
        }
        for (int y = 45; y <= 50; y++) {
            if (y <= 46) {
                for (int x = -2; x <= 9; x++) {
                    for (int z = 18; z <= 25; z++) {
                        if (y == 45 && x < 2 && z > 20) continue;
                        if (y == 46 && x < 1 && z > 23) continue;
                        world.setBlock(i + x, j + y, k + z, RAFIKI_WOOD, 0, 2);
                    }
                }
            } else {
                for (int x = -3; x <= 11; x++) {
                    for (int z = 20; z <= 25; z++) {
                        if (y == 47 && x < -1 && z > 23) continue;
                        world.setBlock(i + x, j + y, k + z, RAFIKI_WOOD, 0, 2);
                    }
                }
            }
        }

        for (int y = 51; y <= 53; y++) {
            world.setBlock(i - 10, j + y, k + 16, RAFIKI_WOOD, 0, 2);
            world.setBlock(i - 11, j + y, k + 16, RAFIKI_WOOD, 0, 2);
            if (y >= 52) world.setBlock(i - 10, j + y, k + 17, RAFIKI_WOOD, 0, 2);
        }
        world.setBlock(i - 10, j + 51, k + 17, RAFIKI_WOOD, 0, 2);
        world.setBlock(i - 11, j + 53, k + 17, RAFIKI_WOOD, 0, 2);
        for (int y = 39; y <= 56; y++) {
            if (y >= 39 && y <= 45) {
                for (int x = 5; x <= 9; x++) {
                    for (int z = 15; z <= 22; z++) {
                        if (y == 39 && (x > 7 || z > 17)) continue;
                        if (y == 40 && (x < 6 || z < 17 || z > 18)) continue;
                        if (y == 41 && (x < 5 || z < 17 || z > 18)) continue;
                        if (y == 42 && (x < 6 || z < 17 || z > 19)) continue;
                        if (y == 43 && (x < 6 || z < 19 || z > 20)) continue;
                        if (y == 44 && (x < 7 || z < 19 || z > 21)) continue;
                        if (y == 45 && (x < 6 || z < 20 || z > 22)) continue;
                        world.setBlock(i + x, j + y, k + z, RAFIKI_WOOD, 0, 2);
                    }
                }
            }
            if (y >= 46 && y <= 56) {
                for (int x = 6; x <= 13; x++) {
                    for (int z = 20; z <= 28; z++) {
                        if (y == 46 && (x > 9 || z > 24)) continue;
                        if (y == 47 && (x > 8 || z > 24)) continue;
                        if (y == 48 && (x > 10 || z > 24)) continue;
                        if (y == 50 && (x > 11 || z != 23)) continue;
                        if (y == 51 && (x > 11 || z < 23 || z > 26)) continue;
                        if (y == 52 && (x > 11 || z < 23 || z > 26)) continue;
                        if (y == 53 && (x > 12 || z < 22 || z > 27)) continue;
                        if (y == 54 && (x > 13 || z < 21 || z > 27)) continue;
                        if (y == 55 && (x > 12 || z < 23 || z > 26)) continue;
                        if (y == 56 && (x < 11 || x > 12 || z != 25 && z != 26)) continue;
                        world.setBlock(i + x, j + y, k + z, RAFIKI_WOOD, 0, 2);
                    }
                }
            }
        }

        for (int x = 3; x <= 8; x++) {
            for (int y = 33; y <= 37; y++) {
                for (int z = 14; z <= 16; z++) {
                    if (y == 33 && (x < 5 || z != 14)) continue;
                    if (y == 36 && (x < 5 || (z == 16 && x > 6))) continue;
                    if (y == 37 && (x != 5 && x != 6 || z != 16)) continue;
                    world.setBlock(i + x, j + y, k + z, RAFIKI_WOOD, 0, 2);
                }
            }
        }
        world.setBlock(i + 7, j + 33, k + 13, RAFIKI_WOOD, 0, 2);

        for (int y = 45; y <= 52; y++) {
            for (int x = 5; x <= 11; x++) {
                for (int z = 15; z <= 19; z++) {
                    if (y == 45 && (x > 9 || z < 17 || z > 18)) continue;
                    if (y == 46 && (x > 8 || z != 17 && z != 18)) continue;
                    if (y == 47 && (x < 6 || x > 9 || z < 17 || z > 19)) continue;
                    if (y == 48 && (x < 7 || x > 9 || z != 18 && z != 19)) continue;
                    if (y == 49 && (x < 7 || x > 9 || z != 18 && z != 19)) continue;
                    if (y == 50 && (x < 8 || x > 11 || z < 17 || z > 19)) continue;
                    if (y == 51 && (x < 9 || x > 11 || z < 16 || z > 19)) continue;
                    if (y == 52 && (x != 11 || z != 17)) continue;
                    world.setBlock(i + x, j + y, k + z, RAFIKI_WOOD, 0, 2);
                }
            }
        }

        for (int y = 49; y <= 52; y++) {
            for (int x = -11; x <= -9; x++) {
                for (int z = 15; z <= 17; z++) {
                    if (y == 49 && (x > -10 || z > 16)) continue;
                    if (y == 50 && (x == -11 || z != 16 && z != 17)) continue;
                    if (y == 51 || y == 52) continue;
                    world.setBlock(i + x, j + y, k + z, RAFIKI_WOOD, 0, 2);
                }
            }
        }
        world.setBlock(i - 11, j + 52, k + 17, RAFIKI_WOOD, 0, 2);

        for (int y = 39; y <= 42; y++) {
            for (int x = -5; x <= -1; x++) {
                for (int z = 13; z <= 18; z++) {
                    if (y == 39 && (x > -5 || z < 13 || z > 14)) continue;
                    if (y == 40 && (x == -5 && z < 14) || (x == -1 && z < 16) || z > 17) continue;
                    if (y == 41 && (x < -4 || x > -2 || z < 15 || z > 16)) continue;
                    if (y == 42 && (x != -3 || z != 16)) continue;
                    world.setBlock(i + x, j + y, k + z, RAFIKI_WOOD, 0, 2);
                }
            }
        }

        for (int y = 32; y <= 37; y++) {
            for (int x = -12; x <= -4; x++) {
                for (int z = -10; z <= 2; z++) {
                    if (y == 32 && (x != -7 || z != -7)) continue;
                    if (y == 33 && (x != -7 || z != -7)) continue;
                    if (y == 34 && (x < -8 || x > -6 || z < -9 || z > -6)) continue;
                    if (y == 35 && (x < -11 || x > -4 || z < -10 || z > 0)) continue;
                    if (y == 36 && (x < -8 || x > -4 || z < -9 || z > 2)) continue;
                    if (y == 37 && (x != -6 || z != -5 && z != -6)) continue;
                    world.setBlock(i + x, j + y, k + z, RAFIKI_WOOD, 0, 2);
                }
            }
        }

        for (int x = -13; x <= -5; x++) {
            for (int z = -7; z <= 7; z++) {
                if (x >= -12 && x <= -10 && z >= 0 && z <= 2) {
                    world.setBlock(i + x, j + 35, k + z, RAFIKI_WOOD, 0, 2);
                }
                if (x == -9 && z >= 1 && z <= 5) {
                    world.setBlock(i + x, j + 35, k + z, RAFIKI_WOOD, 0, 2);
                }
                if (x == -8 && z >= 4 && z <= 6) {
                    world.setBlock(i + x, j + 35, k + z, RAFIKI_WOOD, 0, 2);
                }
                if (x == -7 && z == 6) {
                    world.setBlock(i + x, j + 35, k + z, RAFIKI_WOOD, 0, 2);
                }
            }
        }
        for (int x = -13; x <= -5; x++) {
            for (int z = -7; z <= 7; z++) {
                if (x == -13 && z >= -3 && z <= 3) {
                    world.setBlock(i + x, j + 36, k + z, RAFIKI_WOOD, 0, 2);
                }
                if (x == -12 && z >= -4 && z <= 5) {
                    world.setBlock(i + x, j + 36, k + z, RAFIKI_WOOD, 0, 2);
                }
                if (x == -11 && z >= -5 && z <= 5) {
                    world.setBlock(i + x, j + 36, k + z, RAFIKI_WOOD, 0, 2);
                }
                if (x == -10 && z >= -6 && z <= 5) {
                    world.setBlock(i + x, j + 36, k + z, RAFIKI_WOOD, 0, 2);
                }
                if (x == -9 && z >= -6 && z <= 6) {
                    world.setBlock(i + x, j + 36, k + z, RAFIKI_WOOD, 0, 2);
                }
                if (x == -8 && z >= -7 && z <= 6) {
                    world.setBlock(i + x, j + 36, k + z, RAFIKI_WOOD, 0, 2);
                }
                if (x == -7 && z >= -7 && z <= 6) {
                    world.setBlock(i + x, j + 36, k + z, RAFIKI_WOOD, 0, 2);
                }
                if (x == -6 && z == 6) {
                    world.setBlock(i + x, j + 36, k + z, RAFIKI_WOOD, 0, 2);
                }
                if (x == -5 && z >= 4 && z <= 6) {
                    world.setBlock(i + x, j + 36, k + z, RAFIKI_WOOD, 0, 2);
                }
            }
        }
        for (int x = -13; x <= -5; x++) {
            for (int z = -7; z <= 7; z++) {
                if (x == -13 && z >= -3 && z <= 3) {
                    world.setBlock(i + x, j + 37, k + z, RAFIKI_WOOD, 0, 2);
                }
                if (x == -12 && z >= -4 && z <= 5) {
                    world.setBlock(i + x, j + 37, k + z, RAFIKI_WOOD, 0, 2);
                }
                if (x == -11 && z >= -5 && z <= 5) {
                    world.setBlock(i + x, j + 37, k + z, RAFIKI_WOOD, 0, 2);
                }
                if (x == -10 && z >= -6 && z <= 5) {
                    world.setBlock(i + x, j + 37, k + z, RAFIKI_WOOD, 0, 2);
                }
                if (x == -9 && z >= -6 && z <= 7) {
                    world.setBlock(i + x, j + 37, k + z, RAFIKI_WOOD, 0, 2);
                }
                if (x == -8 && z >= -6 && z <= 6) {
                    world.setBlock(i + x, j + 37, k + z, RAFIKI_WOOD, 0, 2);
                }
                if (x == -7 && z >= -6 && z <= 6) {
                    world.setBlock(i + x, j + 37, k + z, RAFIKI_WOOD, 0, 2);
                }
                if (x == -6 && z >= 4 && z <= 6) {
                    world.setBlock(i + x, j + 37, k + z, RAFIKI_WOOD, 0, 2);
                }
                if (x == -5 && z >= -7 && z <= 6) {
                    world.setBlock(i + x, j + 37, k + z, RAFIKI_WOOD, 0, 2);
                }
            }
        }
        for (int x = -10; x <= -8; x++) {
            for (int y = 30; y <= 34; y++) {
                for (int z = -6; z <= 2; z++) {
                    if (y == 30 && (x != -10 || z < -1 || z > 2)) continue;
                    if (y == 31 && (x != -10 || z < -2 || z > 0)) continue;
                    if (y == 32 && (x == -9 && z == -4) || (x == -8 && z == -5)) continue;
                    if (y == 33 && (x == -8 && z == -6) || (x == -9 && z < -5 || z > -2)) continue;
                    if (y == 34 && (x == -8 && z > -5) || (x == -9 && z < -5 || z == -1 || z > 2) || (x == -10 && z > -1)) continue;
                    world.setBlock(i + x, j + y, k + z, RAFIKI_WOOD, 0, 2);
                }
            }
        }

        for (int x = -14; x <= -6; x++) {
            for (int y = 46; y <= 48; y++) {
                for (int z = 10; z <= 20; z++) {
                    if (Math.abs(x + 10) + Math.abs(z - 15) < 7) {
                        world.setBlock(i + x, j + y, k + z, RAFIKI_LEAVES, 0, 2);
                    }
                }
            }
        }
        for (int x = -13; x <= -7; x++) {
            for (int y = 53; y <= 54; y++) {
                for (int z = 14; z <= 20; z++) {
                    if (y == 53 && (x > -8 && z == 20)) continue;
                    if (y == 54 && (x > -8 && (z == 19 || z == 20))) continue;
                    world.setBlock(i + x, j + y, k + z, RAFIKI_LEAVES, 0, 2);
                }
            }
        }
        for (int x = -3; x <= 5; x++) {
            for (int y = 43; y <= 47; y++) {
                for (int z = 18; z <= 25; z++) {
                    if (y == 43 && (x != -1 || z != 21)) continue;
                    if (y == 44 && (x > 1 || z < 19 || z > 22)) continue;
                    if (y == 45 && (x > 1 || z < 18 || z > 23)) continue;
                    if (y == 46 && (x > 2 || z < 18 || z > 25)) continue;
                    if (y == 47 && (x > 5 || z < 20 || z > 23)) continue;
                    world.setBlock(i + x, j + y, k + z, RAFIKI_LEAVES, 0, 2);
                }
            }
        }
        for (int x = 4; x <= 16; x++) {
            for (int y = 52; y <= 55; y++) {
                for (int z = 21; z <= 30; z++) {
                    if (y == 52 && (x > 12 || z < 23 || z > 26)) continue;
                    if (y == 53 && (x > 13 || z < 22 || z > 27)) continue;
                    if (y == 54 && (x > 16 || z < 21 || z > 30)) continue;
                    if (y == 55 && (x > 14 || z < 23 || z > 29)) continue;
                    world.setBlock(i + x, j + y, k + z, RAFIKI_LEAVES, 0, 2);
                }
            }
        }
        for (int y = 55; y <= 56; y++) {
            for (int x = 8; x <= 15; x++) {
                for (int z = 23; z <= 27; z++) {
                    if (y == 55 && (x > 14 || z < 24 || z > 25)) continue;
                    if (y == 56 && (x < 9 || x > 12 || z < 24 || z > 27)) continue;
                    world.setBlock(i + x, j + y, k + z, RAFIKI_LEAVES, 0, 2);
                }
            }
        }
        for (int x = 4; x <= 7; x++) {
            for (int z = 20; z <= 21; z++) {
                if (x == 7 && z == 20) continue;
                world.setBlock(i + x, j + 47, k + z, RAFIKI_LEAVES, 0, 2);
                if (x == 7 && z == 21) world.setBlock(i + x, j + 48, k + z, RAFIKI_LEAVES, 0, 2);
            }
        }
        for (int x = -1; x <= 3; x++) {
            for (int z = 19; z <= 25; z++) {
                if (x == -1 && z == 25) world.setBlock(i + x, j + 46, k + z, RAFIKI_LEAVES, 0, 2);
                if (x >= 0 && x <= 3 && z >= 20 && z <= 24) {
                    world.setBlock(i + x, j + 47, k + z, RAFIKI_LEAVES, 0, 2);
                }
                if (x >= 1 && x <= 3 && z == 19) world.setBlock(i + x, j + 46, k + z, RAFIKI_LEAVES, 0, 2);
                if (x == 2 && z == 25) world.setBlock(i + x, j + 46, k + z, RAFIKI_LEAVES, 0, 2);
                if (x == 3 && z == 24) world.setBlock(i + x, j + 46, k + z, RAFIKI_LEAVES, 0, 2);
            }
        }
        for (int x = -14; x <= 12; x++) {
            for (int z = 14; z <= 29; z++) {
                if (x >= -14 && x <= -10 && z >= 14 && z <= 20) {
                    world.setBlock(i + x, j + 53, k + z, RAFIKI_LEAVES, 0, 2);
                }
                if (x >= 6 && x <= 12 && z >= 25 && z <= 29) {
                    world.setBlock(i + x, j + 53, k + z, RAFIKI_LEAVES, 0, 2);
                }
            }
        }
        for (int y = 45; y <= 48; y++) {
            for (int x = -14; x <= -9; x++) {
                for (int z = 14; z <= 20; z++) {
                    if (y == 45 && (x > -10 || z != 15)) continue;
                    if (y == 46 && (x != -12 || z != 16 && z != 17)) continue;
                    if (y == 47 && (x < -14 || x > -9 || z < 15 || z > 20)) continue;
                    if (y == 48 && (x < -13 || x > -11 || z < 14 || z > 17)) continue;
                    world.setBlock(i + x, j + y, k + z, RAFIKI_LEAVES, 0, 2);
                }
            }
        }
        for (int y = 50; y <= 52; y++) {
            for (int x = 7; x <= 13; x++) {
                for (int z = 15; z <= 20; z++) {
                    if (y == 50 && (x < 8 || x > 11 || z < 16 || z > 17)) continue;
                    if (y == 51 && (x < 7 || x > 13 || z < 15 || z > 20)) continue;
                    if (y == 52 && (x < 8 || x > 11 || z < 16 || z > 19)) continue;
                    world.setBlock(i + x, j + y, k + z, RAFIKI_LEAVES, 0, 2);
                }
            }
        }
        for (int y = 38; y <= 42; y++) {
            for (int x = -5; x <= 0; x++) {
                for (int z = 14; z <= 18; z++) {
                    if (y == 38 && (x > -3 || z != 15 && z != 16 && z != 17)) continue;
                    if (y == 39 && (x < -4 || x > -1 || z < 14 || z > 18)) continue;
                    if (y == 40 && (x < -5 || x > 0 || z < 14 || z > 18)) continue;
                    if (y == 41 && (x < -4 || x > -1 || z < 14 || z > 17)) continue;
                    if (y == 42 && (x != -4 || z != 15)) continue;
                    world.setBlock(i + x, j + y, k + z, RAFIKI_LEAVES, 0, 2);
                }
            }
        }

        for (int x = -12; x <= -10; x++) {
            for (int z = 10; z <= 12; z++) {
                if (x == -10 && z == 12) {
                    world.setBlock(i + x, j + 47, k + z, mod_LionKing.rafikiLeaves, 0, 2);
                }
                if (x >= -12 && x <= -11 && z >= 10 && z <= 11) {
                    world.setBlock(i + x, j + 47, k + z, mod_LionKing.rafikiLeaves, 0, 2);
                }
                if (x == -11 && z == 11) {
                    world.setBlock(i + x, j + 48, k + z, mod_LionKing.rafikiLeaves, 0, 2);
                }
            }
        }

        j -= 40;
        finishGeneration2(world, i, j, k);
    }

    private void finishGeneration2(World world, int i, int j, int k) {
        int[][] woodPositions82 = {
             {-13, 82, -2, 0}, {-13, 82, -3, 0}, {-14, 82, -3, 0}, {-16, 82, -2, 0}, {-16, 82, -3, 0},
             {-15, 82, -5, 0}, {-11, 82, -7, 0}, {-12, 82, -7, 0}, {-12, 82, 5, 0}, {-8, 82, -8, 0},
             {-9, 82, -8, 0}, {-9, 82, 6, 0}, {-8, 82, 6, 0}
        };
        for (int[] pos : woodPositions82) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], mod_LionKing.rafikiWood, pos[3], 2);
        }

        for (int x = -17; x <= -6; x++) {
             for (int z = -9; z <= 6; z++) {
              if (x == -16 && (z >= -4 && z <= 3)) {
                   world.setBlock(i + x, j + 83, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -17 && (z == -1 || z == -2)) {
                   world.setBlock(i + x, j + 83, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -15 && (z == -4 || z == -5 || (z >= 0 && z <= 3))) {
                   world.setBlock(i + x, j + 83, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -14 && (z == -6 || (z >= -5 && z <= 2))) {
                   world.setBlock(i + x, j + 83, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -13 && (z == -6 || (z >= -5 && z <= 4))) {
                   world.setBlock(i + x, j + 83, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -12 && (z == -7 || (z >= -5 && z <= 4))) {
                   world.setBlock(i + x, j + 83, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -11 && ((z >= -7 && z <= 5) && z != 4)) {
                   world.setBlock(i + x, j + 83, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -10 && ((z >= -7 && z <= 5) && z != 4)) {
                   world.setBlock(i + x, j + 83, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -9 && ((z >= -8 && z <= 5) && z != 4)) {
                   world.setBlock(i + x, j + 83, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -8 && ((z >= -8 && z <= 5) && z != -7)) {
                   world.setBlock(i + x, j + 83, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -7 && (z == -8 || (z >= -6 && z <= 6))) {
                   world.setBlock(i + x, j + 83, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -6 && z == -8) {
                   world.setBlock(i + x, j + 83, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }

        for (int x = -17; x <= -6; x++) {
             for (int z = -10; z <= 7; z++) {
              if (x == -16 && (z >= -5 && z <= 1)) {
                   world.setBlock(i + x, j + 84, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -17 && (z >= -3 && z <= 0)) {
                   world.setBlock(i + x, j + 84, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -15 && (z >= -6 && z <= 2)) {
                   world.setBlock(i + x, j + 84, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -14 && (z >= -7 && z <= 3)) {
                   world.setBlock(i + x, j + 84, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -13 && (z >= -7 && z <= 3)) {
                   world.setBlock(i + x, j + 84, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -12 && (z >= -7 && z <= 4)) {
                   world.setBlock(i + x, j + 84, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -11 && (z >= -7 && z <= 4)) {
                   world.setBlock(i + x, j + 84, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -10 && (z >= -8 && z <= 4)) {
                   world.setBlock(i + x, j + 84, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -9 && (z >= -8 && z <= 5)) {
                   world.setBlock(i + x, j + 84, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -8 && (z >= -9 && z <= 4)) {
                   world.setBlock(i + x, j + 84, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -7 && (z >= -8 && z <= -6)) {
                   world.setBlock(i + x, j + 84, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -6 && z == 7) {
                   world.setBlock(i + x, j + 84, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -9 && z == -9) {
                   world.setBlock(i + x, j + 84, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -7 && (z == -10 || z == -9)) {
                   world.setBlock(i + x, j + 84, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -6 && z == -9) {
                   world.setBlock(i + x, j + 84, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }

        for (int x = -17; x <= -6; x++) {
             for (int z = -10; z <= 4; z++) {
              if (x == -16 && z == -6) {
                   world.setBlock(i + x, j + 85, k + z, Blocks.air, 0, 2);
              }
              if (x == -17 && (z >= -4 && z <= 0)) {
                   world.setBlock(i + x, j + 85, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -16 && (z >= -5 && z <= 1)) {
                   world.setBlock(i + x, j + 85, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -15 && (z >= -7 && z <= 2)) {
                   world.setBlock(i + x, j + 85, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -14 && (z >= -7 && z <= 3)) {
                   world.setBlock(i + x, j + 85, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -13 && (z >= -7 && z <= 3)) {
                   world.setBlock(i + x, j + 85, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -12 && (z >= -8 && z <= 3)) {
                   world.setBlock(i + x, j + 85, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -11 && (z >= -8 && z <= 4)) {
                   world.setBlock(i + x, j + 85, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -10 && (z >= -9 && z <= 4)) {
                   world.setBlock(i + x, j + 85, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -9 && (z >= -9 && z <= 4)) {
                   world.setBlock(i + x, j + 85, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -8 && (z >= -10 && z <= 4)) {
                   world.setBlock(i + x, j + 85, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -7 && (z >= -10 && z <= -7)) {
                   world.setBlock(i + x, j + 85, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -6 && z == -9) {
                   world.setBlock(i + x, j + 85, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }

        for (int x = -17; x <= -7; x++) {
             for (int z = -10; z <= 4; z++) {
              if (x == -17 && (z >= -4 && z <= -1)) {
                   world.setBlock(i + x, j + 86, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -16 && (z >= -4 && z <= 1)) {
                   world.setBlock(i + x, j + 86, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -15 && (z >= -5 && z <= 2)) {
                   world.setBlock(i + x, j + 86, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -14 && (z >= -8 && z <= 3)) {
                   world.setBlock(i + x, j + 86, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -13 && (z >= -7 && z <= 3)) {
                   world.setBlock(i + x, j + 86, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -12 && (z >= -7 && z <= 3)) {
                   world.setBlock(i + x, j + 86, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -11 && (z >= -7 && z <= 4)) {
                   world.setBlock(i + x, j + 86, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -10 && (z >= -8 && z <= 4)) {
                   world.setBlock(i + x, j + 86, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -9 && (z >= -9 && z <= 4)) {
                   world.setBlock(i + x, j + 86, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -8 && (z == -10 || z == -9 || (z >= -7 && z <= -6))) {
                   world.setBlock(i + x, j + 86, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -7 && z == -9) {
                   world.setBlock(i + x, j + 86, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }

        for (int x = -17; x <= -7; x++) {
             for (int z = -9; z <= 4; z++) {
              if (x == -17 && (z >= -5 && z <= -1)) {
                   world.setBlock(i + x, j + 87, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -16 && (z >= -6 && z <= 1)) {
                   world.setBlock(i + x, j + 87, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -15 && (z >= -7 && z <= 2)) {
                   world.setBlock(i + x, j + 87, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -14 && (z >= -8 && z <= 2)) {
                   world.setBlock(i + x, j + 87, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -13 && (z >= -8 && z <= 3)) {
                   world.setBlock(i + x, j + 87, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -12 && (z >= -8 && z <= 3)) {
                   world.setBlock(i + x, j + 87, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -11 && (z >= -9 && z <= 4)) {
                   world.setBlock(i + x, j + 87, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -10 && (z >= -8 && z <= 4)) {
                   world.setBlock(i + x, j + 87, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -9 && (z >= -8 && z <= 4)) {
                   world.setBlock(i + x, j + 87, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -8 && (z >= -9 && z <= -6)) {
                   world.setBlock(i + x, j + 87, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -7 && (z >= -9 && z <= -7)) {
                   world.setBlock(i + x, j + 87, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }

        for (int x = -17; x <= -7; x++) {
             for (int z = -8; z <= 4; z++) {
              if (x == -17 && (z >= -5 && z <= -3)) {
                   world.setBlock(i + x, j + 88, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -16 && (z >= -6 && z <= 0)) {
                   world.setBlock(i + x, j + 88, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -15 && (z >= -7 && z <= 1)) {
                   world.setBlock(i + x, j + 88, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -14 && (z >= -7 && z <= 2)) {
                   world.setBlock(i + x, j + 88, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -13 && (z >= -8 && z <= 3)) {
                   world.setBlock(i + x, j + 88, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -12 && (z >= -8 && z <= 3)) {
                   world.setBlock(i + x, j + 88, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -11 && (z >= -8 && z <= 4)) {
                   world.setBlock(i + x, j + 88, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -10 && (z >= -8 && z <= 4)) {
                   world.setBlock(i + x, j + 88, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -9 && (z >= -7 && z <= 4)) {
                   world.setBlock(i + x, j + 88, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -8 && (z >= -8 && z <= -6)) {
                   world.setBlock(i + x, j + 88, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if (x == -7 && z == -8) {
                   world.setBlock(i + x, j + 88, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }

        j += 40;
        finishGeneration3(world, i, j, k);
       }

    private void finishGeneration3(World world, int i, int j, int k) {
        world.setBlock(i + 9, j + 46, k + 21, mod_LionKing.rafikiWood, 0, 2);
        world.setBlock(i + 9, j + 45, k + 20, mod_LionKing.rafikiWood, 0, 2);
        world.setBlock(i + 9, j + 44, k + 19, mod_LionKing.rafikiWood, 0, 2);
        for (int y = 39; y <= 41; y++) {
             world.setBlock(i + 9, j + y, k + (54 - y), mod_LionKing.rafikiWood, 0, 2);
        }
        world.setBlock(i + 8, j + 41, k + 15, mod_LionKing.rafikiWood, 0, 2);
        world.setBlock(i + 9, j + 41, k + 14, mod_LionKing.rafikiWood, 0, 2);
        world.setBlock(i + 9, j + 41, k + 16, Blocks.air, 0, 2);
        world.setBlock(i + 9, j + 41, k + 15, Blocks.air, 0, 2);

        for (int x = -17; x <= -8; x++) {
             for (int z = -8; z <= 3; z++) {
              if ((x >= -15 && x <= -8 && z >= -8 && z <= -5) || 
                   (x >= -17 && x <= -11 && z >= -5 && z <= -1) || 
                   (x >= -16 && x <= -10 && z >= 0 && z <= 3)) {
                   world.setBlock(i + x, j + 49, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }

        for (int y = 50; y <= 56; y++) {
             if (y <= 54) {
              world.setBlock(i - 17, j + y, k + 2, mod_LionKing.rafikiWood, 0, 2);
             }
             if (y >= 51 && y <= 54) {
              world.setBlock(i - 18, j + y, k + 2, mod_LionKing.rafikiWood, 0, 2);
              world.setBlock(i - 18, j + y, k + 3, mod_LionKing.rafikiWood, 0, 2);
             }
             if (y >= 52 && y <= 55) {
              world.setBlock(i - 17, j + y, k + 4, mod_LionKing.rafikiWood, 0, 2);
             }
             if (y >= 54) {
              world.setBlock(i - 19, j + y, k + 3, mod_LionKing.rafikiWood, 0, 2);
              world.setBlock(i - 19, j + y, k + 4, mod_LionKing.rafikiWood, 0, 2);
             }
             if (y == 56) {
              world.setBlock(i - 20, j + y, k + 4, mod_LionKing.rafikiWood, 0, 2);
              world.setBlock(i - 20, j + y, k + 5, mod_LionKing.rafikiWood, 0, 2);
              world.setBlock(i - 21, j + y, k + 5, mod_LionKing.rafikiWood, 0, 2);
              world.setBlock(i - 20, j + y, k + 6, mod_LionKing.rafikiWood, 0, 2);
              world.setBlock(i - 21, j + y, k + 6, mod_LionKing.rafikiWood, 0, 2);
             }
        }

        for (int y = 49; y <= 53; y++) {
             if (y <= 52) {
              world.setBlock(i - 12, j + y, k + 4, mod_LionKing.rafikiWood, 0, 2);
             }
             if (y >= 50 && y <= 52) {
              world.setBlock(i - 13, j + y, k + 6, mod_LionKing.rafikiWood, 0, 2);
             }
             if (y >= 51) {
              world.setBlock(i - 14, j + y, k + 6, mod_LionKing.rafikiWood, 0, 2);
              world.setBlock(i - 13, j + y, k + 7, mod_LionKing.rafikiWood, 0, 2);
             }
        }

        for (int x = -24; x <= -17; x++) {
             for (int z = 1; z <= 8; z++) {
              if ((x >= -23 && x <= -17 && z >= 1 && z <= 7) || 
                   (x == -24 && z >= 2 && z <= 7)) {
                   world.setBlock(i + x, j + 56, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }
        for (int x = -23; x <= -18; x++) {
             for (int z = 2; z <= 6; z++) {
              if ((x >= -22 && x <= -18 && z >= 2 && z <= 5) || 
                   (x == -23 && (z == 4 || z == 5))) {
                   world.setBlock(i + x, j + 57, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }
        for (int x = -15; x <= -10; x++) {
             for (int z = 4; z <= 8; z++) {
              if ((x >= -14 && x <= -11 && z >= 5 && z <= 7) || 
                   (x == -15 && (z == 6 || z == 7))) {
                   world.setBlock(i + x, j + 53, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }

        for (int x = -15; x <= -12; x++) {
             world.setBlock(i + x, j + 51, k - 4, mod_LionKing.rafikiWood, 0, 2);
        }
        for (int y = 52; y <= 53; y++) {
             for (int x = -16; x <= -7; x++) {
              for (int z = -9; z <= -2; z++) {
                   if ((y == 52 && x >= -16 && x <= -8 && z >= -8 && z <= -2) || 
                      (y == 53 && x >= -15 && x <= -7 && z >= -8 && z <= -2)) {
                      world.setBlock(i + x, j + y, k + z, mod_LionKing.rafikiWood, 0, 2);
                   }
              }
             }
        }
        for (int x = -16; x <= -7; x++) {
             for (int z = -9; z <= -1; z++) {
              if ((x >= -15 && x <= -7 && z >= -8 && z <= -4) || 
                   (x >= -14 && x <= -12 && z == -3) || 
                   (x >= -14 && x <= -13 && z <= -1) || 
                   (x == -16 && z >= -5 && z <= -4) || 
                   (x == -10 && z == -3)) {
                   world.setBlock(i + x, j + 54, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }

        for (int y = 48; y <= 49; y++) {
             for (int x = -10; x <= -7; x++) {
              for (int z = 2; z <= 5; z++) {
                   if ((y == 48 && x >= -9 && x <= -7 && z >= 3 && z <= 5) || 
                      (y == 49 && x >= -9 && x <= -7 && z == 4)) {
                      world.setBlock(i + x, j + y, k + z, mod_LionKing.rafikiWood, 0, 2);
                   }
              }
             }
        }
        for (int x = -10; x <= -6; x++) {
             for (int z = 1; z <= 7; z++) {
              if ((x >= -10 && x <= -6 && z >= 2 && z <= 6) || 
                   (x == -8 && z == 7) || (x == -7 && z == 7)) {
                   world.setBlock(i + x, j + 49, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }
        for (int x = -10; x <= -7; x++) {
             for (int z = 2; z <= 5; z++) {
              if ((x >= -9 && x <= -7 && z >= 3 && z <= 5) || (x == -9 && z == 2)) {
                   world.setBlock(i + x, j + 50, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }

        for (int y = 35; y <= 45; y++) {
             for (int x = 9; x <= 16; x++) {
              for (int z = -15; z <= -10; z++) {
                   if ((y == 35 && x == 11 && z == -12) || 
                      (y == 36 && x >= 10 && x <= 13 && z >= -12 && z <= -10) || 
                      (y == 37 && x >= 10 && x <= 14 && z >= -13 && z <= -10) || 
                      (y == 38 && x >= 10 && x <= 14 && z >= -13 && z <= -10) || 
                      (y == 39 && x >= 10 && x <= 14 && z >= -13 && z <= -10) || 
                      (y == 40 && x >= 12 && x <= 15 && z >= -14 && z <= -11) || 
                      (y == 41 && x >= 12 && x <= 15 && z >= -14 && z <= -11) || 
                      (y == 42 && x >= 13 && x <= 15 && z >= -15 && z <= -12) || 
                      (y == 43 && x >= 14 && x <= 16 && z >= -15 && z <= -13) || 
                      (y == 44 && x >= 14 && x <= 16 && z >= -15 && z <= -13) || 
                      (y == 45 && x >= 15 && x <= 16 && z >= -15 && z <= -14)) {
                      world.setBlock(i + x, j + y, k + z, mod_LionKing.rafikiWood, 0, 2);
                   }
              }
             }
        }

        for (int y = 43; y <= 49; y++) {
             for (int x = 13; x <= 18; x++) {
              for (int z = -19; z <= -12; z++) {
                   if ((y >= 43 && y <= 48 && x >= 15 && x <= 18 && z >= -17 && z <= -13) || 
                      (y == 49 && x >= 15 && x <= 18 && z >= -16 && z <= -14)) {
                      world.setBlock(i + x, j + y, k + z, mod_LionKing.rafikiWood, 0, 2);
                   }
              }
             }
        }
        for (int x = 13; x <= 19; x++) {
             for (int z = -19; z <= -12; z++) {
              if ((x >= 13 && x <= 19 && z >= -18 && z <= -12) || 
                   (x >= 15 && x <= 17 && z == -19)) {
                   world.setBlock(i + x, j + 49, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }
        for (int x = 14; x <= 18; x++) {
             for (int z = -18; z <= -13; z++) {
              world.setBlock(i + x, j + 50, k + z, mod_LionKing.rafikiLeaves, 0, 2);
             }
        }
        for (int x = 15; x <= 18; x++) {
             for (int z = -17; z <= -13; z++) {
              world.setBlock(i + x, j + 48, k + z, mod_LionKing.rafikiLeaves, 0, 2);
             }
        }
        world.setBlock(i + 16, j + 47, k - 14, mod_LionKing.rafikiLeaves, 0, 2);
        world.setBlock(i + 16, j + 47, k - 16, mod_LionKing.rafikiLeaves, 0, 2);

        for (int y = 48; y <= 54; y++) {
             for (int x = -11; x <= -7; x++) {
              for (int z = -9; z <= -6; z++) {
                   if ((y >= 48 && y <= 50 && x >= -11 && x <= -9 && z == -9) || 
                      (y >= 51 && y <= 54 && x == -11 && z == -8) || 
                      (y >= 52 && y <= 54 && x == -10 && z >= -9 && z <= -8) || 
                      (y >= 53 && y <= 54 && x >= -9 && x <= -7 && z >= -8 && z <= -6)) {
                      world.setBlock(i + x, j + y, k + z, mod_LionKing.rafikiWood, 0, 2);
                   }
              }
             }
        }

        for (int x = -12; x <= -8; x++) {
             for (int z = -8; z <= -5; z++) {
              if ((x >= -11 && x <= -8 && z >= -7 && z <= -5) || 
                   (x == -12 && z == -8) || 
                   (x == -10 && z == -6)) {
                   world.setBlock(i + x, j + 54, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }

        for (int x = -16; x <= -7; x++) {
             for (int z = -9; z <= -1; z++) {
              if ((x >= -16 && x <= -7 && z >= -8 && z <= -2) || 
                   (x >= -15 && x <= -9 && z >= -9 && z <= -1)) {
                   world.setBlock(i + x, j + 55, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }

        for (int x = -16; x <= -7; x++) {
             for (int z = -9; z <= -1; z++) {
              if ((x >= -16 && x <= -7 && z >= -9 && z <= -4) || 
                   (x >= -15 && x <= -11 && z >= -3 && z <= -1)) {
                   world.setBlock(i + x, j + 56, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }

        for (int x = -16; x <= -6; x++) {
             for (int z = -9; z <= -3; z++) {
              if ((x >= -16 && x <= -7 && z >= -9 && z <= -3) || 
                   (x >= -12 && x <= -9 && z == -4)) {
                   world.setBlock(i + x, j + 57, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }

        for (int x = -16; x <= -6; x++) {
             for (int z = -9; z <= -3; z++) {
              if ((x >= -16 && x <= -7 && z >= -9 && z <= -4) || 
                   (x >= -12 && x <= -10 && z == -3)) {
                   world.setBlock(i + x, j + 58, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }

        for (int x = -16; x <= -5; x++) {
             for (int z = -10; z <= -3; z++) {
              if ((x >= -16 && x <= -7 && z >= -10 && z <= -4) || 
                   (x >= -12 && x <= -11 && z == -3)) {
                   world.setBlock(i + x, j + 59, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }

        for (int x = -11; x <= -4; x++) {
             for (int z = -10; z <= -4; z++) {
              if ((x >= -11 && x <= -4 && z >= -10 && z <= -4) || 
                   (x >= -10 && x <= -5 && z == -6)) {
                   world.setBlock(i + x, j + 60, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }
        for (int x = -7; x <= -4; x++) {
             for (int z = -7; z <= -6; z++) {
              if ((x >= -7 && x <= -5 && z == -7) || (x >= -7 && x <= -6 && z == -6)) {
                   world.setBlock(i + x, j + 60, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }

        for (int x = -10; x <= -3; x++) {
             for (int z = -10; z <= -4; z++) {
              if ((x >= -10 && x <= -3 && z >= -10 && z <= -4) || 
                   (x >= -9 && x <= -5 && z >= -9 && z <= -5)) {
                   world.setBlock(i + x, j + 61, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }

        for (int x = -9; x <= -2; x++) {
             for (int z = -10; z <= -3; z++) {
              if ((x >= -9 && x <= -2 && z >= -10 && z <= -3) || 
                   (x >= -8 && x <= -4 && z >= -9 && z <= -4)) {
                   world.setBlock(i + x, j + 62, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }

        for (int x = -11; x <= -2; x++) {
             for (int z = -10; z <= -1; z++) {
              if ((x >= -11 && x <= -4 && z >= -9 && z <= -2) || 
                   (x >= -9 && x <= -2 && z >= -10 && z <= -3)) {
                   world.setBlock(i + x, j + 63, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }

        for (int x = -10; x <= -1; x++) {
             for (int z = -10; z <= -2; z++) {
              if ((x >= -10 && x <= -1 && z >= -10 && z <= -2) || 
                   (x >= -9 && x <= -2 && z >= -9 && z <= -3)) {
                   world.setBlock(i + x, j + 64, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }

        for (int x = -11; x <= 0; x++) {
             for (int z = -11; z <= -2; z++) {
              if ((x >= -11 && x <= 0 && z >= -11 && z <= -2) || 
                   (x >= -10 && x <= -1 && z >= -10 && z <= -3)) {
                   world.setBlock(i + x, j + 65, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }
        for (int x = -3; x <= 2; x++) {
             for (int z = -11; z <= -3; z++) {
              if ((x >= -3 && x <= 2 && z >= -11 && z <= -3) || 
                   (x >= -2 && x <= 1 && z >= -10 && z <= -4)) {
                   world.setBlock(i + x, j + 65, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }

        for (int x = -8; x <= 1; x++) {
             for (int z = -11; z <= 1; z++) {
              if ((x >= -8 && x <= 1 && z >= -11 && z <= 1) || 
                   (x >= -7 && x <= 0 && z >= -10 && z <= 0)) {
                   world.setBlock(i + x, j + 66, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }
        for (int x = -2; x <= 4; x++) {
             for (int z = -14; z <= 3; z++) {
              if ((x >= -2 && x <= 4 && z >= -14 && z <= 3) || 
                   (x >= -1 && x <= 3 && z >= -13 && z <= 2)) {
                   world.setBlock(i + x, j + 66, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }

        for (int x = -7; x <= 2; x++) {
             for (int z = -12; z <= -5; z++) {
              if ((x >= -7 && x <= 2 && z >= -12 && z <= -5) || 
                   (x >= -6 && x <= 1 && z >= -11 && z <= -6)) {
                   world.setBlock(i + x, j + 67, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }
        for (int x = -1; x <= 3; x++) {
             for (int z = -13; z <= 1; z++) {
              if ((x >= -1 && x <= 3 && z >= -13 && z <= 1) || 
                   (x >= 0 && x <= 2 && z >= -12 && z <= 0)) {
                   world.setBlock(i + x, j + 67, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }

        for (int x = -7; x <= 3; x++) {
             for (int z = -12; z <= -5; z++) {
              if ((x >= -7 && x <= 3 && z >= -12 && z <= -5) || 
                   (x >= -6 && x <= 2 && z >= -11 && z <= -6)) {
                   world.setBlock(i + x, j + 68, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }

        for (int x = -6; x <= 3; x++) {
             for (int z = -13; z <= -4; z++) {
              if ((x >= -6 && x <= 3 && z >= -13 && z <= -4) || 
                   (x >= -5 && x <= 2 && z >= -12 && z <= -5)) {
                   world.setBlock(i + x, j + 69, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }
        for (int x = 1; x <= 6; x++) {
             for (int z = -7; z <= -3; z++) {
              if ((x >= 1 && x <= 6 && z >= -7 && z <= -3) || 
                   (x >= 2 && x <= 5 && z >= -6 && z <= -4)) {
                   world.setBlock(i + x, j + 69, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }

        for (int x = -15; x <= -9; x++) {
             for (int z = -11; z <= -1; z++) {
              if ((x >= -15 && x <= -9 && z >= -11 && z <= -1) || 
                   (x >= -14 && x <= -10 && z >= -10 && z <= -2)) {
                   world.setBlock(i + x, j + 70, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }
        for (int x = 1; x <= 5; x++) {
             for (int z = -6; z <= -4; z++) {
              if ((x >= 1 && x <= 5 && z >= -6 && z <= -4) || 
                   (x >= 2 && x <= 4 && z == -5)) {
                   world.setBlock(i + x, j + 70, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }

        world.setBlock(i - 10, j + 71, k - 6, mod_LionKing.rafikiWood, 0, 2);

        finishGeneration4(world, i, j, k);
     }

    private void finishGeneration4(World world, int i, int j, int k) {
        world.setBlock(i - 14, j + 43, k + 5, mod_LionKing.rafikiWood, 0, 2);
        for (int x = -14; x <= -13; x++) {
             for (int z = 4; z <= 7; z++) {
              if ((x == -14 && z >= 4 && z <= 7) || (x == -13 && z >= 5 && z <= 6)) {
                   world.setBlock(i + x, j + 44, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }
        for (int x = -14; x <= -13; x++) {
             for (int z = 6; z <= 9; z++) {
              world.setBlock(i + x, j + 45, k + z, mod_LionKing.rafikiWood, 0, 2);
             }
        }
        for (int x = -16; x <= -12; x++) {
             for (int z = 5; z <= 10; z++) {
              if ((x == -15 && z >= 6 && z <= 10) || (x == -14 && z == 5) || (x == -13 && z == 5) ||
                   (x == -12 && z >= 6 && z <= 9) || (x == -14 && z == 10) || (x == -13 && z == 10) ||
                   (x == -16 && z >= 7 && z <= 9)) {
                   world.setBlock(i + x, j + 45, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }
        for (int x = -14; x <= -13; x++) {
             for (int z = 6; z <= 9; z++) {
              if ((x == -14 && z >= 6 && z <= 9) || (x == -13 && z >= 6 && z <= 8)) {
                   world.setBlock(i + x, j + 46, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }
        world.setBlock(i - 15, j + 46, k + 8, mod_LionKing.rafikiLeaves, 0, 2);

        for (int y = 44; y <= 45; y++) {
             world.setBlock(i - 15, j + y, k + 3, mod_LionKing.rafikiWood, 0, 2);
             world.setBlock(i - 15, j + y, k + 4, mod_LionKing.rafikiWood, 0, 2);
             world.setBlock(i - 16, j + y, k + 3, mod_LionKing.rafikiWood, 0, 2);
        }
        world.setBlock(i - 16, j + 46, k + 4, mod_LionKing.rafikiWood, 0, 2);
        world.setBlock(i - 17, j + 46, k, mod_LionKing.rafikiWood, 0, 2);
        world.setBlock(i - 17, j + 47, k + 1, mod_LionKing.rafikiWood, 0, 2);
        world.setBlock(i - 16, j + 47, k + 2, mod_LionKing.rafikiWood, 0, 2);
        for (int x = -17; x <= -15; x++) {
             for (int z = 1; z <= 3; z++) {
              if ((x == -16 && z >= 1 && z <= 3) || (x == -17 && z == 2) || (x == -15 && z == 2)) {
                   world.setBlock(i + x, j + 48, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }

        world.setBlock(i - 8, j + 41, k + 7, mod_LionKing.rafikiWood, 0, 2);
        world.setBlock(i - 8, j + 43, k + 2, mod_LionKing.rafikiWood, 0, 2);
        for (int y = 43; y <= 44; y++) {
             world.setBlock(i - 8, j + y, k + 6, mod_LionKing.rafikiWood, 0, 2);
        }
        for (int y = 44; y <= 46; y++) {
             world.setBlock(i - 7, j + y, k + 4, mod_LionKing.rafikiWood, 0, 2);
        }
        world.setBlock(i - 8, j + 45, k + 3, mod_LionKing.rafikiWood, 0, 2);
        for (int x = -10; x <= -6; x++) {
             for (int z = 1; z <= 7; z++) {
              if ((x == -10 && z >= 1 && z <= 4) || (x == -9 && z == 3) || (x == -8 && z == 5) ||
                   (x == -7 && z >= 5 && z <= 6) || (x == -6 && z == 5)) {
                   world.setBlock(i + x, j + 45, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }
        for (int x = -10; x <= -7; x++) {
             for (int z = 3; z <= 5; z++) {
              if ((x >= -10 && x <= -8 && z == 3) || (x >= -10 && x <= -9 && z == 4) || (x == -7 && z == 5)) {
                   world.setBlock(i + x, j + 46, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }
        for (int y = 47; y <= 50; y++) {
             world.setBlock(i - 7, j + y, k + 5, mod_LionKing.rafikiWood, 0, 2);
             if (y <= 49) world.setBlock(i - 6, j + y, k + 5, mod_LionKing.rafikiWood, 0, 2);
        }
        world.setBlock(i - 8, j + 47, k + 4, mod_LionKing.rafikiWood, 0, 2);
        world.setBlock(i - 5, j + 50, k + 5, mod_LionKing.rafikiWood, 0, 2);
        world.setBlock(i - 4, j + 50, k + 5, mod_LionKing.rafikiWood, 0, 2);
        for (int y = 48; y <= 49; y++) {
             for (int x = -7; x <= -3; x++) {
              for (int z = 4; z <= 6; z++) {
                   if ((x == -7 && z == 5 && y == 48) || (x >= -6 && x <= -3 && z >= 4 && z <= 6 && y == 49)) {
                      world.setBlock(i + x, j + y, k + z, mod_LionKing.rafikiLeaves, 0, 2);
                   }
              }
             }
        }
        for (int x = -8; x <= -2; x++) {
             for (int z = 2; z <= 8; z++) {
              if ((x >= -8 && x <= -2 && z >= 2 && z <= 8) && !((x == -7 && z == 8) || (x == -4 && z == 8))) {
                   world.setBlock(i + x, j + 50, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }
        for (int x = -6; x <= -2; x++) {
             for (int z = 3; z <= 7; z++) {
              if ((x >= -6 && x <= -2 && z >= 3 && z <= 7) && !(x == -3 && z == 7)) {
                   world.setBlock(i + x, j + 51, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }

        for (int y = 44; y <= 47; y++) {
             world.setBlock(i - 7, j + y, k - 10, mod_LionKing.rafikiWood, 0, 2);
        }
        for (int x = -8; x <= -6; x++) {
             for (int z = -11; z <= -9; z++) {
              if ((x == -8 && z >= -11 && z <= -9) || (x == -7 && z >= -11 && z <= -10) || (x == -6 && z >= -10 && z <= -9)) {
                   world.setBlock(i + x, j + 46, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }
        for (int y = 47; y <= 53; y++) {
             world.setBlock(i - 8, j + y, k - 12, mod_LionKing.rafikiWood, 0, 2);
             if (y >= 48 && y <= 50) world.setBlock(i - 8, j + y, k - 10, mod_LionKing.rafikiWood, 0, 2);
             if (y >= 49 && y <= 53) world.setBlock(i - 8, j + y, k - 13, mod_LionKing.rafikiWood, 0, 2);
        }
        for (int x = -9; x <= -7; x++) {
             for (int z = -14; z <= -11; z++) {
              if ((x == -9 && z >= -13 && z <= -11) || (x == -8 && z >= -14 && z <= -11) || (x == -7 && z >= -13 && z <= -12)) {
                   world.setBlock(i + x, j + 49, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }
        for (int x = -9; x <= -7; x++) {
             for (int z = -13; z <= -11; z++) {
              if ((x == -9 && z == -11) || (x == -8 && z >= -13 && z <= -11) || (x == -7 && z == -12)) {
                   world.setBlock(i + x, j + 50, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }
        world.setBlock(i - 9, j + 51, k - 12, mod_LionKing.rafikiWood, 0, 2);
        for (int x = -11; x <= -5; x++) {
             for (int z = -17; z <= -11; z++) {
              if ((x >= -11 && x <= -5 && z >= -15 && z <= -11) || (x >= -10 && x <= -5 && z >= -16 && z <= -12) ||
                   (x >= -9 && x <= -6 && z == -17)) {
                   world.setBlock(i + x, j + 53, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }
        for (int x = -10; x <= -6; x++) {
             for (int z = -16; z <= -12; z++) {
              if ((x >= -10 && x <= -6 && z >= -15 && z <= -12) || (x >= -9 && x <= -7 && z == -16)) {
                   world.setBlock(i + x, j + 54, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }

        for (int x = -9; x <= -6; x++) {
             for (int z = -5; z <= -2; z++) {
              if ((x == -9 && z == -3) || (x == -8 && z >= -4 && z <= -2) || (x == -7 && z == -5) || (x == -6 && z == -3)) {
                   world.setBlock(i + x, j + 54, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }
        for (int x = -9; x <= -6; x++) {
             for (int z = -4; z <= -3; z++) {
              if ((x >= -9 && x <= -7 && z == -4) || (x == -6 && z == -3)) {
                   world.setBlock(i + x, j + 55, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }
        for (int x = -7; x <= -4; x++) {
             for (int z = -4; z <= -2; z++) {
              if ((x >= -7 && x <= -5 && z == -4) || (x == -6 && z == -3) || (x == -4 && z >= -3 && z <= -2)) {
                   world.setBlock(i + x, j + 56, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }
        for (int x = -6; x <= -1; x++) {
             for (int z = -5; z <= -1; z++) {
              if ((x == -6 && z >= -5 && z <= -4) || (x >= -5 && x <= -4 && z == -3) || (x >= -3 && x <= -2 && z == -2) ||
                   (x == -1 && z == -1)) {
                   world.setBlock(i + x, j + 57, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }
        for (int x = -4; x <= 2; x++) {
             for (int z = -4; z <= 2; z++) {
              if ((x >= -4 && x <= 2 && z >= -2 && z <= 2) && !(x == -3 && z == -2)) {
                   world.setBlock(i + x, j + 58, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }
        for (int x = -2; x <= 1; x++) {
             for (int z = -3; z <= 1; z++) {
              if ((x >= -2 && x <= 1 && z >= -2 && z <= 1) || (x >= -1 && x <= 0 && z == -3)) {
                   world.setBlock(i + x, j + 59, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }

        world.setBlock(i - 17, j + 53, k + 4, mod_LionKing.rafikiWood, 0, 2);
        for (int y = 54; y <= 55; y++) {
             world.setBlock(i - 17, j + y, k + 4, mod_LionKing.rafikiWood, 0, 2);
             world.setBlock(i - 18, j + y, k + 4, mod_LionKing.rafikiWood, 0, 2);
        }
        for (int x = -23; x <= -16; x++) {
             for (int z = 2; z <= 6; z++) {
              if ((x >= -23 && x <= -17 && z >= 2 && z <= 5) || (x >= -22 && x <= -16 && z == 6)) {
                   world.setBlock(i + x, j + 55, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }
        for (int x = -21; x <= -19; x++) {
             for (int z = 3; z <= 5; z++) {
              world.setBlock(i + x, j + 54, k + z, mod_LionKing.rafikiLeaves, 0, 2);
             }
        }
        for (int x = -16; x <= -15; x++) {
             for (int z = 2; z <= 5; z++) {
              world.setBlock(i + x, j + 56, k + z, mod_LionKing.rafikiLeaves, 0, 2);
             }
        }
        for (int x = -18; x <= -17; x++) {
             for (int z = 3; z <= 5; z++) {
              world.setBlock(i + x, j + 57, k + z, mod_LionKing.rafikiLeaves, 0, 2);
             }
        }

        for (int y = 35; y <= 36; y++) {
             world.setBlock(i + 9, j + y, k + 13, mod_LionKing.rafikiWood, 0, 2);
        }
        world.setBlock(i + 9, j + 36, k + 14, mod_LionKing.rafikiWood, 0, 2);
        world.setBlock(i + 9, j + 37, k + 14, mod_LionKing.rafikiWood, 0, 2);
        world.setBlock(i + 10, j + 38, k + 14, mod_LionKing.rafikiWood, 0, 2);
        for (int y = 39; y <= 40; y++) {
             world.setBlock(i + 9, j + y, k + 15, mod_LionKing.rafikiWood, 0, 2);
        }
        world.setBlock(i + 9, j + 39, k + 14, mod_LionKing.rafikiWood, 0, 2);

        world.setBlock(i + 4, j + 45, k + 17, mod_LionKing.rafikiWood, 0, 2);
        for (int x = 1; x <= 6; x++) {
             for (int z = 17; z <= 23; z++) {
              if ((x >= 1 && x <= 4 && z >= 17 && z <= 18) || (x >= 2 && x <= 4 && z == 19) ||
                   (x == 4 && z >= 20 && z <= 22) || (x >= 4 && x <= 6 && z == 23)) {
                   world.setBlock(i + x, j + 46, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }
        for (int x = 1; x <= 5; x++) {
             for (int z = 18; z <= 23; z++) {
              if ((x >= 1 && x <= 3 && z >= 18 && z <= 19) || (x >= 2 && x <= 4 && z >= 20 && z <= 22) ||
                   (x == 5 && z == 20) || (x >= 2 && x <= 4 && z == 23)) {
                   world.setBlock(i + x, j + 47, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }
        for (int x = 0; x <= 3; x++) {
             for (int z = 20; z <= 22; z++) {
              if ((x >= 0 && x <= 1 && z >= 21 && z <= 22) || (x >= 2 && x <= 3 && z == 20)) {
                   world.setBlock(i + x, j + 48, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }

        world.setBlock(i - 11, j + 61, k - 8, mod_LionKing.rafikiWood, 0, 2);
        for (int y = 62; y <= 70; y++) {
             if (y <= 64) world.setBlock(i - 10, j + y, k - 8, mod_LionKing.rafikiWood, 0, 2);
             if (y <= 67) world.setBlock(i - 10, j + y, k - 6, mod_LionKing.rafikiWood, 0, 2);
             if (y >= 63 && y <= 65) world.setBlock(i - 9, j + y, k - 7, mod_LionKing.rafikiWood, 0, 2);
             if (y == 63 || y == 64) world.setBlock(i - 9, j + y, k - 6, mod_LionKing.rafikiWood, 0, 2);
             if (y == 64 || y == 65) world.setBlock(i - 8, j + y, k - 7, mod_LionKing.rafikiWood, 0, 2);
             if (y >= 69) world.setBlock(i - 11, j + y, k - 6, mod_LionKing.rafikiWood, 0, 2);
             if (y >= 68) world.setBlock(i - 10, j + y, k - 7, mod_LionKing.rafikiWood, 0, 2);
             if (y >= 69) world.setBlock(i - 8, j + y, k - 6, mod_LionKing.rafikiWood, 0, 2);
        }
        world.setBlock(i - 9, j + 62, k - 5, mod_LionKing.rafikiWood, 0, 2);
        world.setBlock(i - 8, j + 63, k - 5, mod_LionKing.rafikiWood, 0, 2);
        world.setBlock(i - 9, j + 68, k - 5, mod_LionKing.rafikiWood, 0, 2);
        for (int x = -14; x <= -2; x++) {
             for (int z = -13; z <= 3; z++) {
              if ((x >= -14 && x <= -8 && z >= -3 && z <= 2) || (x >= -6 && x <= -2 && z >= 0 && z <= 3)) {
                   world.setBlock(i + x, j + 66, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }
        world.setBlock(i + 3, j + 66, k + 3, mod_LionKing.rafikiLeaves, 0, 2);
        for (int x = -13; x <= -2; x++) {
             for (int z = -2; z <= 3; z++) {
              if ((x >= -13 && x <= -9 && z >= -2 && z <= 1) || (x >= -6 && x <= -2 && z >= 0 && z <= 3)) {
                   world.setBlock(i + x, j + 67, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }
        for (int x = -10; x <= -3; x++) {
             for (int z = -13; z <= 2; z++) {
              if ((x >= -10 && x <= -4 && z >= -13 && z <= -10) || (x >= -5 && x <= -3 && z >= 0 && z <= 2)) {
                   world.setBlock(i + x, j + 68, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }
        for (int x = -14; x <= -3; x++) {
             for (int z = -14; z <= -3; z++) {
              if ((x >= -14 && x <= -7 && z >= -14 && z <= -8) || (x >= -13 && x <= -7 && z >= -7 && z <= -4) ||
                   (x >= -12 && x <= -8 && z == -3) || (x >= -7 && x <= -3 && z >= -13 && z <= -10)) {
                   world.setBlock(i + x, j + 69, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }
        for (int x = -16; x <= -6; x++) {
             for (int z = -13; z <= -3; z++) {
              if ((x >= -16 && x <= -6 && z >= -13 && z <= -3) && !(x >= -15 && x <= -11 && z >= -2 && z <= -1)) {
                   world.setBlock(i + x, j + 70, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }
        for (int x = -15; x <= -7; x++) {
             for (int z = -9; z <= -2; z++) {
              if ((x >= -15 && x <= -7 && z >= -9 && z <= -2) && !(x == -15 && z >= -7 && z <= -6)) {
                   world.setBlock(i + x, j + 71, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }
        for (int x = -13; x <= -10; x++) {
             for (int z = -8; z <= -4; z++) {
              if ((x >= -13 && x <= -10 && z >= -8 && z <= -4) && !(x == -13 && z == -5)) {
                   world.setBlock(i + x, j + 72, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }

        world.setBlock(i + 14, j + 32, k - 10, mod_LionKing.rafikiWood, 0, 2);
        world.setBlock(i + 10, j + 32, k - 13, mod_LionKing.rafikiWood, 0, 2);
        world.setBlock(i + 9, j + 30, k - 14, mod_LionKing.rafikiWood, 0, 2);
        for (int y = 31; y <= 34; y++) {
             world.setBlock(i + 9, j + y, k - 14, mod_LionKing.rafikiWood, 0, 2);
             if (y <= 33) world.setBlock(i + 9, j + y, k - 13, mod_LionKing.rafikiWood, 0, 2);
             if (y >= 32) world.setBlock(i + 10, j + y, k - 14, mod_LionKing.rafikiWood, 0, 2);
             if (y == 33) world.setBlock(i + 11, j + y, k - 13, mod_LionKing.rafikiWood, 0, 2);
        }
        for (int y = 35; y <= 38; y++) {
             world.setBlock(i + 15, j + y, k - 10, mod_LionKing.rafikiWood, 0, 2);
             if (y <= 36) world.setBlock(i + 11, j + y, k - 13, mod_LionKing.rafikiWood, 0, 2);
             if (y == 35) world.setBlock(i + 15, j + y, k - 9, mod_LionKing.rafikiWood, 0, 2);
             if (y == 36) world.setBlock(i + 15, j + y, k - 11, mod_LionKing.rafikiWood, 0, 2);
             if (y >= 36 && y <= 37) world.setBlock(i + 13, j + y, k - 10, mod_LionKing.rafikiWood, 0, 2);
             if (y == 36) world.setBlock(i + 11, j + y, k - 12, mod_LionKing.rafikiWood, 0, 2);
             if (y == 36 || y == 37) world.setBlock(i + 12, j + y, k - 11, mod_LionKing.rafikiWood, 0, 2);
             if (y == 37) world.setBlock(i + 12, j + y, k - 12, mod_LionKing.rafikiWood, 0, 2);
             if (y == 37 || y == 38) world.setBlock(i + 13, j + y, k - 11, mod_LionKing.rafikiWood, 0, 2);
             if (y == 38) {
              world.setBlock(i + 13, j + y, k - 13, mod_LionKing.rafikiWood, 0, 2);
              world.setBlock(i + 14, j + y, k - 12, mod_LionKing.rafikiWood, 0, 2);
              world.setBlock(i + 12, j + y, k - 12, mod_LionKing.rafikiWood, 0, 2);
             }
        }
        for (int y = 39; y <= 43; y++) {
             world.setBlock(i + 12, j + y, k - 11, mod_LionKing.rafikiWood, 0, 2);
             if (y <= 40) world.setBlock(i + 14, j + y, k - 13, mod_LionKing.rafikiWood, 0, 2);
             if (y <= 40) world.setBlock(i + 13, j + y, k - 12, mod_LionKing.rafikiWood, 0, 2);
             if (y == 40) world.setBlock(i + 15, j + y, k - 13, mod_LionKing.rafikiWood, 0, 2);
             if (y == 42 || y == 43) world.setBlock(i + 15, j + y, k - 15, mod_LionKing.rafikiWood, 0, 2);
             if (y == 42 || y == 43) world.setBlock(i + 16, j + y, k - 14, mod_LionKing.rafikiWood, 0, 2);
        }
        world.setBlock(i + 14, j + 44, k - 16, mod_LionKing.rafikiLeaves, 0, 2);
        world.setBlock(i + 14, j + 44, k - 15, mod_LionKing.rafikiLeaves, 0, 2);

        world.setBlock(i - 6, j + 36, k + 12, mod_LionKing.rafikiWood, 0, 2);
        for (int y = 37; y <= 38; y++) {
             world.setBlock(i - 6, j + y, k + 12, mod_LionKing.rafikiWood, 0, 2);
             if (y == 37) world.setBlock(i - 6, j + y, k + 13, mod_LionKing.rafikiWood, 0, 2);
             if (y == 38) world.setBlock(i - 5, j + y, k + 13, mod_LionKing.rafikiWood, 0, 2);
        }
        world.setBlock(i - 5, j + 37, k + 13, mod_LionKing.rafikiWood, 0, 2);
        world.setBlock(i - 8, j + 36, k + 9, mod_LionKing.rafikiLeaves, 0, 2);
        world.setBlock(i - 8, j + 36, k + 10, mod_LionKing.rafikiLeaves, 0, 2);
        world.setBlock(i - 1, j + 37, k + 11, mod_LionKing.rafikiLeaves, 0, 2);
        world.setBlock(i - 1, j + 37, k + 12, mod_LionKing.rafikiLeaves, 0, 2);
        world.setBlock(i - 2, j + 38, k + 11, mod_LionKing.rafikiLeaves, 0, 2);

        for (int y = 47; y <= 49; y++) {
             if (y <= 48) {
              world.setBlock(i - 14, j + y, k + 3, mod_LionKing.rafikiWood, 0, 2);
              world.setBlock(i - 15, j + y, k + 3, mod_LionKing.rafikiWood, 0, 2);
             }
             if (y == 49) {
              for (int x = -15; x <= -11; x++) {
                   for (int z = -1; z <= 2; z++) {
                      if ((x >= -15 && x <= -11 && z >= 0 && z <= 2) || (x == -14 && z == -1)) {
                           world.setBlock(i + x, j + y, k + z, mod_LionKing.rafikiWood, 0, 2);
                      }
                   }
              }
             }
        }
        for (int y = 50; y <= 52; y++) {
             for (int x = -13; x <= -9; x++) {
              for (int z = 0; z <= 3; z++) {
                   if ((x >= -13 && x <= -11 && z >= 0 && z <= 2 && y <= 51) ||
                      (x >= -11 && x <= -10 && z == 3 && y <= 51) || (x == -9 && z >= 1 && z <= 3 && y == 52)) {
                      world.setBlock(i + x, j + y, k + z, mod_LionKing.rafikiWood, 0, 2);
                   }
              }
             }
        }
        for (int x = -10; x <= -9; x++) {
             for (int z = 1; z <= 3; z++) {
              world.setBlock(i + x, j + 53, k + z, mod_LionKing.rafikiWood, 0, 2);
             }
        }

        for (int y = 54; y <= 58; y++) {
             for (int x = -10; x <= -7; x++) {
              for (int z = 1; z <= 4; z++) {
                   if ((x == -10 && z == 1 && y == 54) || (x >= -10 && x <= -8 && z >= 2 && z <= 3 && y == 54) ||
                      (x >= -9 && x <= -7 && z >= 2 && z <= 4 && y >= 55 && y <= 58)) {
                      world.setBlock(i + x, j + y, k + z, mod_LionKing.rafikiWood, 0, 2);
                   }
              }
             }
        }
        for (int x = -10; x <= -4; x++) {
             for (int z = 0; z <= 6; z++) {
              if ((x >= -10 && x <= -4 && z >= 1 && z <= 5) || (x >= -7 && x <= -6 && z == 6)) {
                   world.setBlock(i + x, j + 58, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }
        for (int x = -9; x <= -5; x++) {
             for (int z = 2; z <= 5; z++) {
              world.setBlock(i + x, j + 59, k + z, mod_LionKing.rafikiLeaves, 0, 2);
             }
        }

        for (int y = 48; y <= 50; y++) {
             for (int x = -20; x <= -17; x++) {
              for (int z = -8; z <= -5; z++) {
                   if ((x >= -20 && x <= -17 && z >= -7 && z <= -5 && y <= 49) || (x == -20 && z == -8 && y <= 50) ||
                      (x == -19 && z == -8 && y == 49)) {
                      world.setBlock(i + x, j + y, k + z, mod_LionKing.rafikiWood, 0, 2);
                   }
              }
             }
        }
        for (int y = 51; y <= 52; y++) {
             for (int x = -21; x <= -20; x++) {
              for (int z = -8; z <= -7; z++) {
                   world.setBlock(i + x, j + y, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }
        for (int x = -25; x <= -18; x++) {
             for (int z = -11; z <= -5; z++) {
              if ((x >= -25 && x <= -18 && z >= -10 && z <= -5) || (x >= -23 && x <= -19 && z == -11)) {
                   world.setBlock(i + x, j + 52, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }
        for (int x = -22; x <= -19; x++) {
             for (int z = -10; z <= -6; z++) {
              world.setBlock(i + x, j + 53, k + z, mod_LionKing.rafikiLeaves, 0, 2);
             }
        }

        world.setBlock(i - 10, j + 30, k - 2, mod_LionKing.rafikiWood, 0, 2);
        world.setBlock(i - 8, j + 30, k + 7, mod_LionKing.rafikiWood, 0, 2);
        world.setBlock(i - 3, j + 32, k + 11, mod_LionKing.rafikiWood, 0, 2);
        for (int y = 32; y <= 34; y++) {
             world.setBlock(i - 2, j + y, k + 11, mod_LionKing.rafikiWood, 0, 2);
        }
        world.setBlock(i + 6, j + 33, k + 14, mod_LionKing.rafikiWood, 0, 2);
        world.setBlock(i + 8, j + 36, k + 15, mod_LionKing.rafikiWood, 0, 2);
        world.setBlock(i + 7, j + 37, k + 16, mod_LionKing.rafikiWood, 0, 2);
        for (int y = 41; y <= 42; y++) {
             world.setBlock(i - 6, j + y, k - 10, mod_LionKing.rafikiWood, 0, 2);
             if (y == 41) world.setBlock(i - 5, j + y, k - 9, mod_LionKing.rafikiWood, 0, 2);
             if (y == 42) {
              world.setBlock(i - 6, j + y, k - 7, mod_LionKing.rafikiWood, 0, 2);
              world.setBlock(i - 5, j + y, k - 8, mod_LionKing.rafikiWood, 0, 2);
             }
        }
        world.setBlock(i - 1, j + 59, k - 5, mod_LionKing.rafikiWood, 0, 2);

        finishGeneration5(world, i, j, k);
     }

    private void finishGeneration5(World world, int i, int j, int k) {
        for (int y = 39; y <= 45; y++) {
             for (int x = 5; x <= 6; x++) {
              for (int z = -1; z <= 0; z++) {
                   world.setBlock(i + x, j + y, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
             if (y == 44) {
              world.setBlock(i + 4, j + y, k - 1, mod_LionKing.rafikiWood, 0, 2);
              world.setBlock(i + 5, j + y, k + 1, mod_LionKing.rafikiWood, 0, 2);
              world.setBlock(i + 7, j + y, k, mod_LionKing.rafikiWood, 0, 2);
              world.setBlock(i + 6, j + y, k - 2, mod_LionKing.rafikiWood, 0, 2);
             }
             if (y == 45) {
              int[][] extraWood = {{7, 0}, {5, 1}, {4, -1}, {6, -2}, {4, 0}, {5, -2}, {6, 1}, {7, -1}};
              for (int[] pos : extraWood) {
                   world.setBlock(i + pos[0], j + y, k + pos[1], mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }
        for (int x = 3; x <= 8; x++) {
             for (int z = -3; z <= 2; z++) {
              if ((x == 3 && z == 0) || (x == 4 && (z == -2 || z == 1)) || (x == 5 && z == -3) ||
                   (x == 6 && z == 2) || (x == 7 && z == -1) || (x == 8 && z == 0)) {
                   world.setBlock(i + x, j + 46, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }

        for (int x = 3; x <= 8; x++) {
             for (int z = -3; z <= 2; z++) {
              if ((x == 3 && z == 0) || (x == 4 && (z == -2 || z == -1 || z == 0 || z == 1)) ||
                   (x == 5 && (z == -3 || z == -2 || z == -1 || z == 1)) || (x == 6 && (z == -2 || z == 1 || z == 2)) ||
                   (x == 7 && (z == -2 || z == -1 || z == 0 || z == 1)) || (x == 8 && z == -1)) {
                   world.setBlock(i + x, j + 46, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
              if ((x == 3 && (z == -2 || z == 1)) || (x == 4 && (z == -3 || z == 2)) || (x == 5 && (z == -3 || z == 0)) ||
                   (x == 6 && z == -2) || (x == 7 && (z == -3 || z == 2)) || (x == 8 && z == 0)) {
                   world.setBlock(i + x, j + 47, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }

        for (int x = 2; x <= 9; x++) {
             for (int z = -4; z <= 3; z++) {
              if ((x == 2 && z == 0) || (x == 3 && (z == -3 || z == -1 || z == 1)) || (x == 4 && (z == -2 || z == 0 || z == 2)) ||
                   (x == 5 && (z == -4 || z == -1 || z == 2)) || (x == 6 && (z == -3 || z == 3)) || (x == 7 && (z == -2 || z == 1)) ||
                   (x == 8 && (z == -3 || z == 0)) || (x == 9 && z == -1)) {
                   world.setBlock(i + x, j + 48, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }
        for (int x = 2; x <= 9; x++) {
             for (int z = -4; z <= 3; z++) {
              if ((x == 2 && (z == -2 || z == 1)) || (x == 3 && (z == -4 || z == 0 || z == 2)) || (x == 4 && (z == -3 || z == 1)) ||
                   (x == 5 && (z == -2 || z == 0)) || (x == 6 && (z == -4 || z == 2)) || (x == 7 && (z == -3 || z == 3)) ||
                   (x == 8 && (z == -1 || z == 2)) || (x == 9 && z == 0)) {
                   world.setBlock(i + x, j + 49, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }
        for (int x = 0; x <= 11; x++) {
             for (int z = -6; z <= 3; z++) {
              if ((x == 0 && z == -3) || (x == 1 && z == -4) || (x == 2 && (z == -5 || z == -1)) || (x == 3 && z == -6) ||
                   (x == 4 && z == -5) || (x == 5 && (z == -6 || z == 3)) || (x == 6 && (z == -4 || z == 3)) ||
                   (x == 7 && z == -5) || (x == 8 && (z == -6 || z == 1)) || (x == 9 && (z == -2 || z == 0)) ||
                   (x == 10 && z == -3) || (x == 11 && z == 2)) {
                   world.setBlock(i + x, j + 50, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }

        for (int x = -15; x <= -12; x++) {
             for (int z = -6; z <= -4; z++) {
              if ((x == -15 && (z == -6 || z == -5)) || (x == -14 && z == -4) || (x == -13 && z == -5) || (x == -12 && z == -6)) {
                   world.setBlock(i + x, j + 56, k + z, mod_LionKing.rafikiWood, 0, 2);
              }
             }
        }
        for (int y = 57; y <= 59; y++) {
             for (int x = -17; x <= -13; x++) {
              for (int z = -5; z <= -1; z++) {
                   if ((y == 57 && x == -15 && z == -4) || (y == 58 && x == -14 && (z == -3 || z == -2)) ||
                      (y == 59 && ((x == -17 && z == -3) || (x == -16 && z == -2) || (x == -15 && z == -1) || (x == -13 && z == -4)))) {
                      world.setBlock(i + x, j + y, k + z, mod_LionKing.rafikiWood, 0, 2);
                   }
              }
             }
        }
        for (int x = -18; x <= -12; x++) {
             for (int z = -7; z <= 2; z++) {
              if ((x == -18 && (z == -5 || z == -3)) || (x == -17 && (z == -6 || z == -2)) || (x == -16 && (z == -7 || z == 1)) ||
                   (x == -15 && (z == -4 || z == 0)) || (x == -14 && (z == -5 || z == 2)) || (x == -13 && z == -3) || (x == -12 && z == -1)) {
                   world.setBlock(i + x, j + 59, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }
        for (int x = -17; x <= -13; x++) {
             for (int z = -6; z <= 0; z++) {
              if ((x == -17 && z == -4) || (x == -16 && (z == -6 || z == -2)) || (x == -15 && z == -5) ||
                   (x == -14 && (z == -3 || z == 0)) || (x == -13 && z == -1)) {
                   world.setBlock(i + x, j + 60, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }

        finishGeneration6(world, i, j, k);
     }

    private void finishGeneration6(World world, int i, int j, int k) {
        for (int x = 1; x <= 10; x++) {
             for (int z = -5; z <= 4; z++) {
              if ((x == 1 && (z == -2 || z == 0)) || (x == 2 && (z == -4 || z == 1)) || (x == 3 && (z == -5 || z == 2)) ||
                   (x == 4 && (z == -3 || z == 3)) || (x == 5 && (z == -4 || z == 4)) || (x == 6 && (z == -2 || z == 1)) ||
                   (x == 7 && (z == -5 || z == 0)) || (x == 8 && (z == -3 || z == 2)) || (x == 9 && z == -1) || (x == 10 && z == 1)) {
                   world.setBlock(i + x, j + 49, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }
        for (int x = 2; x <= 9; x++) {
             for (int z = -4; z <= 3; z++) {
              if ((x == 2 && z == -3) || (x == 3 && (z == -2 || z == 1)) || (x == 4 && z == -4) || (x == 5 && (z == -1 || z == 2)) ||
                   (x == 6 && z == -3) || (x == 7 && (z == 0 || z == 3)) || (x == 8 && z == -2) || (x == 9 && z == 1)) {
                   world.setBlock(i + x, j + 50, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }

        int[][] woodPositions = {
             {3, 38, -5}, {4, 38, -4}, {4, 38, 2}, {8, 38, -3}, {8, 38, 2},
             {4, 39, -4}, {4, 39, -2}, {4, 39, 0}, {4, 39, 1}, {5, 39, -2}, {6, 39, -2}, {7, 39, -3}, {7, 39, 0}, {8, 39, 1},
             {11, 35, 11}, {13, 35, 10}, {14, 35, 9}, {15, 36, 8}, {16, 36, 7}, {17, 36, 5}, {18, 37, 4}, {19, 39, 6},
             {15, 43, 11}, {16, 44, 10}, {17, 44, 9}, {18, 44, 11}, {19, 44, 10}, {20, 44, 9}
        };
        for (int[] pos : woodPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], mod_LionKing.rafikiWood, 0, 2);
        }

        finishGeneration7(world, i, j, k);
     }

    private void finishGeneration7(World world, int i, int j, int k) {
        int[][] woodPositions = {
             {16, 44, 11}, {18, 45, 10}, {20, 46, 9}, {21, 47, 8}, {22, 48, 7}, {23, 49, 6}, {24, 50, 5}, {25, 51, 4},
             {11, 50, 25}, {13, 51, 26}, {14, 53, 27}, {7, 50, 25}, {6, 53, 25}, {17, 44, -13}, {19, 48, -13},
             {-8, 50, -14}, {-8, 51, -15}, {-11, 66, -6}, {-13, 65, -2}, {-17, 57, -4}, {-19, 51, 2}, {-22, 55, 3}
        };
        for (int[] pos : woodPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], mod_LionKing.rafikiWood, 0, 2);
        }

        for (int x = 21; x <= 28; x++) {
             for (int z = 0; z <= 9; z++) {
              if ((x == 21 && z == 4) || (x == 22 && (z == 2 || z == 6)) || (x == 23 && (z == 1 || z == 8)) ||
                   (x == 24 && (z == 3 || z == 7)) || (x == 25 && (z == 0 || z == 5)) || (x == 26 && z == 2) ||
                   (x == 27 && z == 4) || (x == 28 && z == 6)) {
                   world.setBlock(i + x, j + 54, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }
        for (int x = 23; x <= 27; x++) {
             for (int z = 1; z <= 7; z++) {
              if ((x == 23 && z == 5) || (x == 24 && (z == 2 || z == 6)) || (x == 25 && z == 4) ||
                   (x == 26 && (z == 1 || z == 7)) || (x == 27 && z == 3)) {
                   world.setBlock(i + x, j + 55, k + z, mod_LionKing.rafikiLeaves, 0, 2);
              }
             }
        }

        finishGeneration8(world, i, j, k);
     }

    private void finishGeneration8(World world, int i, int j, int k) {
        int[][] woodPositions = {
             {14, 44, 14}, {15, 44, 14}, {13, 44, 12}, {14, 45, 14}, {16, 45, 14}, {13, 45, 13},
             {13, 45, 15}, {15, 45, 15}, {14, 46, 13}, {14, 46, 14}, {13, 46, 13}, {13, 46, 14},
             {13, 46, 15}, {14, 46, 15}, {15, 46, 13}, {15, 46, 14}, {15, 46, 15}, {13, 47, 13},
             {13, 47, 14}, {13, 47, 15}, {14, 47, 14}, {14, 47, 15}, {15, 47, 14}, {15, 47, 15},
             {14, 47, 16}, {13, 47, 16}, {15, 47, 16}, {12, 47, 14}, {12, 47, 15}, {12, 47, 16},
             {13, 47, 17}, {14, 47, 17}, {14, 46, 16}, {15, 46, 16}, {16, 46, 15}, {12, 48, 14},
             {12, 48, 15}, {12, 48, 16}, {13, 48, 14}, {13, 48, 15}, {13, 48, 16}, {14, 48, 14},
             {14, 48, 15}, {14, 48, 16}, {15, 48, 15}, {15, 48, 16}, {16, 48, 15}, {16, 48, 16},
             {13, 48, 17}, {14, 48, 17}, {15, 48, 17}, {12, 49, 16}, {12, 49, 17}, {13, 49, 17},
             {15, 49, 15}, {15, 49, 16}, {15, 49, 17}, {16, 49, 16}, {16, 49, 17}, {12, 50, 17},
             {15, 50, 17}, {16, 50, 16}, {16, 50, 17}, {13, 51, 16}, {16, 51, 17}, {16, 52, 17},
             {16, 53, 17}, {18, 45, 12}, {19, 45, 11}, {19, 45, 12}, {17, 45, 11}, {17, 45, 12},
             {18, 46, 12}, {19, 46, 11}, {19, 46, 12}, {19, 47, 12}, {19, 47, 13}, {19, 48, 13},
             {19, 49, 13}, {4, 43, 20}, {3, 42, 20}, {3, 43, 20}, {4, 44, 20}, {2, 42, 21},
             {2, 43, 21}, {3, 42, 21}, {3, 43, 21}, {4, 43, 21}, {1, 43, 21}, {1, 43, 22},
             {0, 44, 22}, {1, 44, 22}, {-12, 51, -8}, {-13, 51, -8}, {-12, 52, -8}, {-12, 52, -9},
             {-13, 52, -9}, {-12, 53, -10}, {-13, 53, -10}, {-12, 54, -10}, {-13, 54, -10},
             {-13, 54, -11}, {-7, 43, -10}, {-6, 42, -9}, {-6, 43, -9}, {7, 39, 0}, {8, 39, 1},
             {8, 38, 2}, {4, 39, 0}, {4, 39, 1}, {4, 38, 2}, {3, 38, 3}, {6, 39, -2}, {7, 39, -3},
             {8, 38, -3}, {5, 39, -2}, {4, 39, -2}, {4, 39, -3}, {4, 38, -4}, {3, 38, -5},
             {15, 35, 8}, {16, 35, 6}, {16, 35, 7}, {14, 35, 9}, {15, 34, 8}, {15, 33, 8},
             {16, 34, 6}, {16, 33, 6}, {16, 34, 7}, {17, 35, 6}, {17, 35, 4}, {18, 35, 4},
             {17, 36, 5}, {17, 35, 5}, {17, 34, 4}, {17, 34, 5}, {13, 36, 9}, {14, 36, 9},
             {15, 36, 9}, {15, 36, 7}, {15, 36, 8}, {16, 36, 8}, {16, 36, 6}, {16, 36, 7},
             {17, 36, 6}, {17, 36, 4}, {18, 36, 4}, {18, 36, 5}, {12, 36, 10}, {13, 36, 10},
             {13, 37, 9}, {14, 37, 9}, {15, 37, 9}, {15, 37, 7}, {15, 37, 8}, {16, 37, 6},
             {16, 37, 7}, {16, 37, 8}, {17, 37, 4}, {17, 37, 5}, {17, 37, 6}, {18, 37, 4},
             {18, 37, 5}, {13, 38, 9}, {14, 38, 9}, {15, 38, 8}, {15, 38, 9}, {15, 38, 7},
             {16, 38, 7}, {16, 38, 8}, {16, 38, 6}, {17, 38, 6}, {17, 38, 4}, {17, 38, 5},
             {18, 38, 4}, {18, 38, 5}, {18, 37, 3}, {18, 38, 3}, {17, 38, 7}, {12, 38, 10},
             {13, 38, 10}, {14, 38, 10}, {12, 37, 10}, {13, 37, 10}, {10, 38, 11}, {11, 38, 11},
             {12, 38, 11}, {13, 38, 11}, {10, 37, 11}, {11, 37, 11}, {12, 37, 11}, {10, 36, 11},
             {11, 36, 11}, {11, 35, 11}, {10, 39, 11}, {11, 39, 11}, {12, 39, 11}, {12, 39, 10},
             {13, 39, 9}, {13, 39, 10}, {13, 39, 11}, {14, 39, 9}, {14, 39, 10}, {15, 39, 7},
             {15, 39, 8}, {15, 39, 9}, {16, 39, 6}, {16, 39, 7}, {16, 39, 8}, {17, 39, 4},
             {17, 39, 5}, {17, 39, 6}, {17, 39, 7}, {18, 39, 4}, {18, 39, 5}, {18, 39, 6},
             {16, 39, 9}, {11, 39, 12}, {12, 39, 12}, {11, 40, 12}, {12, 40, 12}, {11, 40, 11},
             {12, 40, 11}, {12, 40, 10}, {13, 40, 10}, {13, 40, 11}, {13, 40, 9}, {14, 40, 9},
             {15, 40, 9}, {15, 40, 7}, {15, 40, 8}, {14, 40, 10}, {16, 40, 9}, {16, 40, 8},
             {16, 40, 7}, {17, 40, 7}, {16, 40, 6}, {17, 40, 6}, {17, 40, 4}, {17, 40, 5},
             {18, 40, 4}, {18, 40, 5}, {18, 40, 6}, {18, 40, 7}, {17, 40, 8}, {15, 40, 10},
             {11, 38, 12}, {13, 40, 12}, {14, 40, 11}, {12, 41, 12}, {13, 41, 12}, {14, 41, 12},
             {14, 41, 11}, {15, 41, 11}, {15, 41, 10}, {16, 41, 10}, {16, 41, 9}, {17, 41, 9},
             {17, 41, 10}
        };
        for (int[] pos : woodPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], mod_LionKing.rafikiWood, 0, 2);
        }

        int[][] leafPositions = {
             {16, 53, 18}, {17, 53, 17}, {17, 53, 18}, {16, 53, 19}, {17, 53, 19}, {18, 53, 19},
             {18, 53, 18}, {18, 53, 17}, {15, 53, 18}, {15, 53, 17}, {17, 53, 16}, {16, 53, 16},
             {15, 53, 16}, {15, 53, 19}, {18, 53, 16}, {19, 53, 17}, {19, 53, 18}, {16, 53, 20},
             {17, 53, 20}, {16, 52, 18}, {16, 52, 19}, {17, 52, 17}, {17, 52, 18}, {16, 52, 16},
             {17, 53, 15}, {16, 53, 15}, {15, 53, 15}, {14, 53, 17}, {14, 53, 18}, {14, 53, 16},
             {16, 54, 17}, {15, 54, 17}, {15, 54, 18}, {16, 54, 18}, {17, 54, 18}, {17, 54, 17},
             {18, 54, 18}, {18, 54, 17}, {16, 54, 16}, {17, 54, 16}, {18, 53, 15}, {19, 53, 16},
             {16, 54, 19}, {17, 54, 19}, {17, 52, 19}, {18, 52, 17}, {15, 52, 17}, {15, 52, 18},
             {14, 55, 27}, {15, 55, 26}, {16, 54, 26}, {16, 54, 27}, {13, 51, 16}, {12, 51, 15},
             {11, 52, 16}, {12, 52, 17}, {12, 52, 16}, {13, 51, 15}, {14, 51, 18}, {14, 51, 17},
             {14, 51, 16}, {13, 52, 17}, {13, 52, 16}, {12, 52, 18}, {13, 51, 19}, {12, 50, 18},
             {11, 51, 20}, {19, 49, 14}, {20, 49, 13}, {20, 49, 14}, {19, 49, 15}, {20, 49, 15},
             {21, 49, 14}, {21, 49, 13}, {21, 49, 12}, {20, 49, 12}, {20, 49, 11}, {19, 49, 12},
             {19, 49, 11}, {18, 49, 14}, {18, 49, 13}, {18, 49, 12}, {18, 49, 11}, {17, 49, 13},
             {17, 49, 12}, {19, 50, 13}, {19, 50, 12}, {18, 50, 12}, {18, 50, 13}, {18, 49, 15},
             {19, 50, 14}, {20, 50, 13}, {20, 50, 14}, {0, 44, 23}, {1, 44, 23}, {2, 45, 21},
             {2, 44, 22}, {1, 45, 23}, {2, 45, 22}, {2, 45, 23}, {0, 45, 24}, {1, 45, 24},
             {3, 45, 21}, {3, 45, 22}, {-12, 54, -11}, {-13, 54, -11}
        };
        for (int[] pos : leafPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], mod_LionKing.rafikiLeaves, 0, 2);
        }

        int[][] airPositions = {
             {12, 47, 15}, {12, 47, 16}, {12, 47, 17}, {13, 46, 16}, {13, 47, 17}, {14, 47, 17},
             {17, 52, 16}, {19, 46, 12}
        };
        for (int[] pos : airPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], Blocks.air, 0, 2);
        }

        finishGeneration9(world, i, j, k);
     }

    private void finishGeneration9(World world, int i, int j, int k) {
        int[][] woodPositions = {
             {16, 41, 11}, {17, 41, 8}, {18, 41, 8}, {18, 41, 9}, {18, 41, 7}, {19, 41, 8},
             {19, 41, 7}, {18, 41, 6}, {19, 41, 6}, {18, 41, 5}, {17, 41, 6}, {17, 41, 7},
             {16, 41, 8}, {16, 41, 7}, {15, 41, 9}, {15, 41, 8}, {14, 41, 10}, {14, 41, 9},
             {13, 41, 11}, {13, 41, 10}, {12, 41, 11}, {16, 40, 10}, {15, 39, 10}, {15, 38, 10},
             {14, 37, 10}, {13, 35, 10}, {16, 38, 9}, {16, 37, 9}, {16, 39, 10}, {15, 40, 11},
             {14, 39, 11}, {14, 38, 11}, {15, 39, 11}, {13, 37, 11}, {12, 36, 11}, {11, 34, 11},
             {12, 35, 11}, {11, 33, 11}, {11, 37, 12}, {12, 38, 12}, {13, 39, 12}, {17, 39, 8},
             {17, 40, 9}, {17, 38, 8}, {17, 37, 7}, {17, 36, 7}, {15, 32, 8}, {16, 32, 6},
             {16, 33, 7}, {14, 34, 9}, {15, 35, 9}, {18, 39, 7}, {18, 40, 8}, {17, 39, 9},
             {18, 38, 6}, {19, 40, 7}, {19, 39, 6}, {19, 40, 6}, {19, 38, 5}, {17, 41, 5},
             {13, 42, 12}, {14, 42, 12}, {15, 42, 12}, {16, 42, 11}, {16, 42, 12}, {14, 42, 13},
             {15, 42, 13}, {13, 42, 13}, {13, 41, 13}, {12, 40, 13}, {12, 39, 13}, {15, 41, 12},
             {14, 40, 12}, {16, 42, 10}, {17, 42, 10}, {17, 42, 11}, {17, 42, 9}, {17, 42, 8},
             {18, 42, 9}, {18, 42, 10}, {19, 42, 9}, {18, 42, 8}, {19, 42, 8}, {19, 42, 7},
             {18, 42, 7}, {17, 42, 7}, {16, 42, 9}, {16, 42, 8}, {15, 42, 8}, {15, 42, 11},
             {15, 42, 10}, {15, 43, 10}, {15, 42, 9}, {14, 42, 11}, {14, 42, 10}, {13, 42, 11},
             {14, 42, 9}, {13, 42, 10}, {13, 41, 9}, {14, 43, 11}, {14, 43, 12}, {14, 43, 13},
             {15, 43, 13}, {16, 43, 13}, {15, 43, 11}, {15, 43, 12}, {16, 43, 12}, {17, 43, 12},
             {16, 43, 11}, {17, 43, 11}, {18, 43, 11}, {19, 43, 11}, {17, 43, 10}, {18, 43, 10},
             {19, 43, 10}, {18, 43, 12}, {18, 42, 11}, {17, 41, 11}, {16, 40, 11}, {14, 36, 10},
             {19, 43, 9}, {19, 43, 8}, {20, 43, 9}, {20, 43, 8}, {20, 42, 7}, {18, 43, 9},
             {18, 43, 8}, {17, 43, 9}, {17, 43, 8}, {16, 43, 10}, {16, 43, 9}, {15, 43, 9},
             {16, 43, 8}, {15, 44, 13}, {16, 44, 13}, {17, 44, 13}, {17, 44, 12}, {18, 44, 12},
             {19, 44, 12}, {18, 44, 11}, {19, 44, 11}, {19, 44, 10}, {20, 44, 9}, {20, 44, 10},
             {19, 44, 9}, {18, 44, 9}, {19, 44, 8}, {18, 44, 8}, {18, 43, 7}, {18, 42, 6},
             {17, 44, 9}, {16, 44, 9}, {18, 44, 10}, {17, 44, 10}, {16, 44, 10}, {17, 44, 11},
             {16, 44, 11}, {15, 44, 11}, {15, 44, 10}, {14, 43, 10}, {16, 44, 12}, {15, 44, 12},
             {14, 44, 12}, {14, 44, 11}, {13, 43, 12}, {18, 45, 10}, {19, 45, 10}, {20, 45, 10},
             {19, 45, 9}, {20, 45, 9}, {20, 46, 9}, {19, 45, 8}, {20, 44, 8}, {20, 45, 8},
             {21, 45, 9}, {21, 45, 8}, {21, 43, 8}, {21, 44, 9}, {21, 44, 8}, {20, 42, 8},
             {20, 43, 7}, {21, 43, 7}, {21, 44, 7}, {21, 45, 7}, {20, 44, 7}, {20, 45, 7},
             {19, 43, 7}, {19, 44, 7}, {21, 46, 8}, {21, 46, 7}, {20, 46, 8}, {20, 46, 7},
             {20, 44, 6}, {20, 45, 6}, {20, 46, 6}, {21, 44, 6}, {21, 45, 6}, {21, 46, 6},
             {22, 46, 7}, {22, 45, 7}, {22, 44, 7}, {22, 45, 8}, {22, 46, 8}, {21, 47, 7},
             {21, 47, 8}, {22, 47, 7}, {22, 47, 8}, {21, 47, 6}, {22, 47, 6}, {20, 47, 8},
             {20, 47, 7}, {21, 46, 9}, {21, 47, 9}, {19, 43, 6}, {19, 42, 6}, {20, 43, 6},
             {20, 41, 6}, {20, 42, 6}, {19, 38, 4}, {19, 39, 4}, {19, 40, 5}, {22, 48, 7},
             {22, 48, 6}, {21, 48, 7}, {21, 48, 6}, {23, 47, 7}, {23, 47, 6}, {21, 48, 5},
             {22, 48, 5}, {23, 48, 7}, {23, 48, 6}, {23, 47, 5}, {23, 48, 5}, {21, 48, 5},
             {22, 48, 5}, {23, 46, 6}, {22, 46, 5}, {23, 46, 5}, {24, 48, 6}, {24, 48, 5},
             {24, 47, 5}, {22, 48, 4}, {23, 48, 4}, {23, 47, 4}, {23, 49, 6}, {23, 49, 5},
             {22, 49, 5}, {24, 49, 5}, {23, 49, 4}, {24, 48, 4}, {24, 49, 4}, {23, 50, 4},
             {24, 50, 4}, {24, 50, 5}, {24, 51, 4}, {25, 51, 4}, {24, 51, 3}, {25, 50, 4},
             {24, 49, 3}, {25, 49, 4}, {24, 52, 3}, {25, 52, 3}, {25, 52, 4}, {24, 53, 3},
             {25, 53, 3}, {25, 53, 4}, {24, 54, 3}, {25, 54, 3}, {25, 54, 4}, {11, 50, 25},
             {12, 50, 25}, {12, 51, 25}, {13, 51, 25}, {13, 51, 26}, {14, 53, 27}, {14, 52, 27},
             {7, 50, 25}, {7, 51, 25}, {6, 51, 25}, {6, 52, 25}, {6, 53, 25}, {17, 44, -13},
             {17, 45, -13}, {18, 45, -13}, {18, 46, -13}, {18, 47, -13}, {19, 47, -13},
             {19, 48, -13}, {20, 48, -12}, {20, 48, -11}, {21, 48, -11}, {-8, 50, -14},
             {-8, 51, -15}, {-11, 66, -6}, {-12, 66, -6}, {-12, 67, -6}, {-12, 68, -6},
             {-11, 64, -2}, {-12, 64, -2}, {-13, 65, -2}, {-17, 57, -4}, {-17, 57, -3},
             {-18, 58, -4}, {-18, 58, -3}, {-19, 51, 2}, {-20, 51, 2}, {-18, 51, 3},
             {-19, 51, 3}, {-20, 51, 3}, {-20, 52, 3}, {-21, 52, 3}, {-21, 53, 3},
             {-22, 53, 3}, {-22, 54, 3}, {-22, 55, 3}, {-17, 45, 0}, {-17, 47, -1},
             {-18, 47, -1}, {-17, 48, 0}, {-17, 47, 0}, {-15, 46, 3}, {2, 32, 13},
             {2, 33, 13}, {3, 33, 13}, {22, 46, 9}, {23, 46, 9}, {24, 46, 9}, {24, 47, 9},
             {24, 47, 10}, {24, 48, 10}, {24, 48, 11}, {24, 49, 11}, {24, 49, 12}
        };
        for (int[] pos : woodPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], mod_LionKing.rafikiWood, 0, 2);
        }

        int[][] leafPositions = {
             {26, 54, 4}, {25, 54, 5}, {26, 54, 5}, {27, 54, 5}, {27, 54, 4}, {26, 54, 3},
             {27, 54, 3}, {28, 54, 3}, {28, 54, 4}, {28, 54, 5}, {28, 54, 6}, {27, 54, 6},
             {26, 54, 6}, {25, 54, 6}, {24, 54, 6}, {24, 54, 5}, {23, 54, 5}, {23, 54, 4},
             {22, 54, 4}, {21, 54, 4}, {22, 54, 5}, {21, 54, 5}, {23, 54, 6}, {22, 54, 6},
             {23, 54, 7}, {24, 54, 7}, {25, 54, 7}, {26, 54, 7}, {27, 54, 7}, {26, 54, 8},
             {25, 54, 8}, {24, 54, 8}, {23, 54, 8}, {24, 54, 9}, {25, 54, 9}, {28, 54, 2},
             {27, 54, 2}, {26, 54, 2}, {25, 54, 2}, {24, 54, 2}, {23, 54, 3}, {23, 54, 2},
             {22, 54, 3}, {22, 54, 2}, {23, 54, 1}, {24, 54, 1}, {25, 54, 1}, {26, 54, 1},
             {27, 54, 1}, {26, 54, 0}, {25, 54, 0}, {24, 54, 0}, {25, 55, 4}, {25, 55, 3},
             {25, 55, 2}, {25, 55, 1}, {24, 55, 2}, {24, 55, 3}, {24, 55, 4}, {23, 55, 4},
             {23, 55, 5}, {25, 55, 5}, {24, 55, 6}, {24, 55, 7}, {25, 55, 7}, {25, 55, 6},
             {26, 55, 5}, {27, 55, 5}, {26, 55, 6}, {26, 55, 4}, {27, 55, 4}, {26, 55, 3},
             {27, 55, 3}, {26, 55, 2}, {14, 54, 28}, {15, 54, 27}, {15, 54, 28}, {6, 53, 24},
             {5, 53, 25}, {6, 53, 26}, {5, 53, 26}, {4, 54, 26}, {23, 53, 3}, {24, 53, 4},
             {23, 53, 4}, {25, 53, 5}, {24, 53, 5}, {26, 53, 4}, {25, 53, 2}, {26, 53, 3},
             {19, 49, -12}, {19, 48, -12}, {19, 48, -11}, {19, 48, -10}, {20, 48, -13},
             {20, 48, -12}, {20, 48, -10}, {21, 48, -10}, {21, 48, -12}, {21, 48, -13},
             {19, 48, -14}, {20, 48, -14}, {21, 48, -14}, {22, 48, -12}, {22, 48, -13},
             {22, 48, -11}, {20, 49, -11}, {21, 49, -12}, {20, 49, -13}, {21, 49, -13},
             {21, 49, -11}, {22, 48, -10}, {19, 47, -12}, {20, 47, -13}, {20, 47, -12},
             {20, 47, -11}, {21, 47, -12}, {21, 47, -11}, {19, 46, -13}, {19, 46, -12},
             {18, 46, -12}, {19, 47, -11}, {18, 48, -13}, {18, 47, -13}, {18, 47, -12},
             {18, 48, -12}, {18, 48, -11}, {19, 49, -11}, {19, 47, -14}, {19, 48, -15},
             {20, 48, -15}, {-6, 52, -15}, {-8, 52, -16}, {-7, 52, -16}, {-14, 66, -3},
             {-19, 59, -4}, {-19, 59, -5}, {-22, 55, 2}, {-23, 55, 3}
        };
        for (int[] pos : leafPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], mod_LionKing.rafikiLeaves, 0, 2);
        }

        int[][] airPositions = {
             {22, 44, 6}, {19, 39, 4}, {19, 38, 5}, {19, 40, 5}, {19, 39, 6}, {23, 50, 3}
        };
        for (int[] pos : airPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], Blocks.air, 0, 2);
        }

        finishGeneration10(world, i, j, k);
     }

    private void finishGeneration10(World world, int i, int j, int k) {
        int[][] woodPositions = {
             {22, 45, 9}, {24, 47, 11}, {23, 47, 10}, {24, 48, 12},
             {20, 43, 5}, {20, 43, 4}, {20, 44, 3}, {20, 45, 3},
             {15, 45, 11}, {15, 45, 12}, {14, 45, 12}, {14, 44, 13}, {14, 45, 13}, 
             {15, 45, 13}, {16, 45, 12}, {16, 45, 13}, {16, 45, 11}, {17, 45, 13}, 
             {15, 45, 14}, {14, 44, 14}, {15, 44, 14}, {16, 45, 14}, {13, 45, 13}, 
             {13, 44, 12}, {13, 46, 13}, {13, 46, 14}, {13, 46, 15}, {14, 46, 13}, 
             {14, 46, 14}, {14, 46, 15}, {15, 46, 13}, {15, 46, 14}, {15, 46, 15}, 
             {12, 46, 14}, {14, 46, 16}, {15, 46, 16}, {16, 46, 15}, {12, 47, 14}, 
             {12, 47, 15}, {13, 47, 13}, {13, 47, 14}, {13, 47, 15}, {13, 47, 16}, 
             {14, 47, 14}, {14, 47, 15}, {14, 47, 16}, {15, 47, 14}, {15, 47, 15}, 
             {15, 47, 16}, {12, 48, 14}, {12, 48, 15}, {12, 48, 16}, {13, 48, 14}, 
             {13, 48, 15}, {13, 48, 16}, {14, 48, 14}, {14, 48, 15}, {14, 48, 16}, 
             {15, 48, 15}, {15, 48, 16}, {16, 48, 15}, {16, 47, 15}, {13, 48, 17}, 
             {14, 48, 17}, {15, 48, 17}, {15, 49, 15}, {15, 49, 16}, {15, 49, 17}, 
             {16, 49, 16}, {16, 49, 17}, {15, 50, 17}, {16, 50, 16}, {16, 50, 17}, 
             {16, 51, 17}, {16, 52, 17}, {16, 53, 17},
             {18, 45, 12}, {19, 45, 12}, {19, 46, 12}, {19, 47, 12}, {19, 47, 13}, 
             {19, 48, 13}, {19, 49, 13}, {17, 45, 12}, {17, 45, 11}, {19, 45, 11}, 
             {18, 46, 12}, {19, 46, 11}, {18, 45, 11}
        };
        for (int[] pos : woodPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], mod_LionKing.rafikiWood, 0, 2);
        }

        int[][] leafPositions = {
             {24, 49, 13}, {24, 49, 14}, {25, 49, 12}, {25, 49, 13}, {24, 49, 11}, 
             {25, 49, 11}, {26, 49, 12}, {26, 49, 11}, {24, 49, 10}, {25, 49, 10}, 
             {23, 49, 11}, {23, 49, 13}, {23, 49, 12}, {24, 48, 12}, {23, 49, 10}, 
             {24, 49, 9}, {25, 49, 9}, {26, 49, 10}, {25, 50, 12}, {25, 50, 11}, 
             {24, 50, 11}, {24, 50, 12}, {25, 49, 14}, {26, 49, 13}, {24, 50, 13}, 
             {25, 50, 13},
             {21, 45, 3}, {20, 45, 2}, {21, 45, 2}, {19, 45, 3}, {19, 45, 2}, 
             {21, 45, 4}, {20, 45, 4}, {19, 45, 4}, {20, 46, 3}, {20, 45, 1}, 
             {21, 45, 1}, {22, 45, 3}, {22, 45, 2}, {21, 46, 3}, {21, 46, 2}, 
             {20, 46, 2}, {19, 45, 1}, {19, 45, 5}, {21, 45, 5}, {20, 45, 5}, 
             {19, 44, 3}, {19, 44, 4}, {20, 44, 4}, {21, 44, 4}, {22, 45, 4}, 
             {21, 44, 3}, {20, 44, 2}, {18, 45, 4}, {18, 45, 3}, {18, 45, 2}, 
             {19, 46, 3}, {19, 46, 4}, {20, 46, 4},
             {16, 53, 18}, {17, 53, 17}, {17, 53, 18}, {16, 53, 19}, {17, 53, 19}, 
             {18, 53, 19}, {18, 53, 18}, {18, 53, 17}, {15, 53, 18}, {15, 53, 17}, 
             {17, 53, 16}, {16, 53, 16}, {15, 53, 16}, {15, 53, 19}, {18, 53, 16}, 
             {19, 53, 17}, {19, 53, 18}, {16, 53, 20}, {17, 53, 20}, {16, 52, 18}, 
             {16, 52, 19}, {17, 52, 17}, {17, 52, 18}, {16, 52, 16}, {17, 53, 15}, 
             {16, 53, 15}, {15, 53, 15}, {14, 53, 17}, {14, 53, 18}, {14, 53, 16}, 
             {16, 54, 17}, {15, 54, 17}, {15, 54, 18}, {16, 54, 18}, {17, 54, 18}, 
             {17, 54, 17}, {18, 54, 18}, {18, 54, 17}, {16, 54, 16}, {17, 54, 16}, 
             {18, 53, 15}, {19, 53, 16}, {16, 54, 19}, {17, 54, 19}, {17, 52, 19}, 
             {18, 52, 17}, {15, 52, 17}, {15, 52, 18},
             {14, 55, 27}, {15, 55, 26}, {16, 54, 26}, {16, 54, 27},
             {13, 51, 16}, {12, 51, 15}, {11, 52, 16}, {12, 52, 17}, {12, 52, 16}, 
             {13, 51, 15}, {14, 51, 18}, {14, 51, 17}, {14, 51, 16}, {13, 52, 17}, 
             {13, 52, 16}, {12, 52, 18}, {13, 51, 19}, {12, 50, 18}, {11, 51, 20},
             {19, 49, 14}, {20, 49, 13}, {20, 49, 14}, {19, 49, 15}, {20, 49, 15}, 
             {21, 49, 14}, {21, 49, 13}, {21, 49, 12}, {20, 49, 12}, {20, 49, 11}, 
             {19, 49, 12}, {19, 49, 11}, {18, 49, 14}, {18, 49, 13}, {18, 49, 12}, 
             {18, 49, 11}, {17, 49, 13}, {17, 49, 12}, {19, 50, 13}, {19, 50, 12}, 
             {18, 50, 12}, {18, 50, 13}, {18, 49, 15}, {19, 50, 14}, {20, 50, 13}, 
             {20, 50, 14}
        };
        for (int[] pos : leafPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], mod_LionKing.rafikiLeaves, 0, 2);
        }

        int[][] airPositions = {
             {24, 50, 10}, {23, 50, 11}, {23, 46, 10}, {24, 47, 10}, {24, 48, 10}, 
             {24, 48, 12}, {21, 47, 9}, {20, 43, 3}, {20, 44, 3}, {18, 44, 3}, 
             {19, 46, 2}
        };
        for (int[] pos : airPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], Blocks.air, 0, 2);
        }

        finishGeneration11(world, i, j, k);
     }

    private void finishGeneration11(World world, int i, int j, int k) {
        int[][] woodPositions = {
             {21, 53, 0}, {21, 53, 1}, {22, 53, 2}, {22, 54, 3},
             {12, 48, -17}, {13, 48, -16}, {14, 49, -15},
             {-6, 53, -11}, {-7, 53, -11},
             {-7, 47, 17}
        };
        for (int[] pos : woodPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], mod_LionKing.rafikiWood, 0, 2);
        }

        int[][] leafPositions = {
             {21, 54, 6}, {22, 54, 7}, {21, 54, 3}, {21, 54, 2}, {22, 54, 1}, 
             {23, 54, 0}, {27, 54, 0}, {28, 54, 1}, {28, 54, 7}, {27, 54, 8}, 
             {26, 54, 9}, {26, 55, 7}, {26, 53, 5},
             {22, 45, 1}, {22, 45, 5}, {23, 45, 6}, {23, 45, 5}, {23, 45, 4}, 
             {23, 45, 3}, {23, 45, 2}, {22, 44, 6}, {22, 44, 5}, {22, 44, 4}, 
             {21, 44, 5}, {23, 45, 7}, {21, 46, 5}, {21, 46, 4}, {22, 46, 4}, 
             {22, 46, 3}, {20, 45, 0}, {21, 45, 0}, {22, 45, 0}, {19, 45, 0}, 
             {19, 46, 2}, {21, 46, 1}, {20, 46, 1},
             {18, 48, -10}, {18, 48, -9}, {19, 48, -9}, {20, 48, -9}, {21, 48, -9}, 
             {23, 48, -11}, {23, 48, -12}, {23, 48, -13}, {22, 48, -14}, {23, 48, -14}, 
             {21, 48, -15}, {22, 48, -15}, {21, 47, -13}, {20, 47, -14}, {17, 48, -13}, 
             {17, 48, -12}, {18, 49, -11}, {17, 49, -11}, {16, 49, -11}, {13, 49, -12}, 
             {13, 49, -13}, {12, 49, -13}, {12, 49, -14}, {12, 49, -15}, {12, 49, -16}, 
             {12, 49, -17}, {14, 50, -14},
             {-9, 53, -10}, {-8, 53, -10}, {-7, 53, -10}, {-6, 53, -10}, {-5, 53, -11}, 
             {-4, 53, -12}, {-4, 53, -13}, {-4, 53, -14}, {-4, 53, -15}, {-5, 53, -16}, 
             {-6, 53, -17},
             {-7, 47, 18}, {-6, 47, 17}, {-6, 47, 18}
        };
        for (int[] pos : leafPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], mod_LionKing.rafikiLeaves, 0, 2);
        }

        int[][] airPositions = {
             {-5, 53, -17}, {-3, 53, -13}
        };
        for (int[] pos : airPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], Blocks.air, 0, 2);
        }

        finishGeneration12(world, i, j, k);
     }

    private void finishGeneration12(World world, int i, int j, int k) {
        int[][] woodPositions = {
             {7, 39, -13}, {6, 39, -13}, {5, 39, -13}, {4, 39, -13}, {3, 39, -13},
             {7, 38, -13}, {6, 38, -13}, {5, 38, -13}, {4, 38, -13}, {3, 38, -13},
             {7, 37, -13}, {6, 37, -13}, {5, 37, -13}, {4, 37, -13}, {3, 37, -13},
             {6, 36, -13}, {5, 36, -13}, {4, 36, -13}, {3, 36, -13}, {7, 36, -13},
             {8, 38, -13}, {8, 39, -13}, {2, 38, -13}, {2, 39, -13}, {2, 40, -13},
             {3, 40, -13}, {4, 40, -13}, {5, 40, -13}, {6, 40, -13}, {7, 40, -13},
             {8, 40, -13}, {9, 40, -13}, {9, 41, -13}, {8, 41, -13}, {7, 41, -13},
             {6, 41, -13}, {5, 41, -13}, {4, 41, -13}, {3, 41, -13}, {2, 41, -13},
             {1, 41, -13}, {1, 42, -13}, {0, 42, -13}, {2, 42, -13}, {3, 42, -13},
             {4, 42, -13}, {5, 42, -13}, {6, 42, -13}, {7, 42, -13}, {8, 42, -13},
             {2, 43, -13}, {3, 43, -13}, {4, 43, -13}, {5, 43, -13}, {6, 43, -13},
             {7, 43, -13}, {8, 43, -13},
             {6, 39, -14}, {5, 39, -14}, {4, 39, -14}, {3, 39, -14}, {7, 38, -14},
             {6, 38, -14}, {5, 38, -14}, {4, 38, -14}, {3, 38, -14}, {7, 40, -14},
             {6, 40, -14}, {5, 40, -14}, {4, 40, -14}, {3, 40, -14}, {2, 40, -14},
             {8, 40, -14}, {6, 37, -14}, {5, 37, -14}, {4, 37, -14}, {3, 37, -14},
             {7, 39, -14}, {8, 41, -14}, {7, 41, -14}, {6, 41, -14}, {5, 41, -14},
             {4, 41, -14}, {3, 41, -14}, {2, 41, -14}, {2, 42, -14}, {3, 42, -14},
             {4, 42, -14}, {5, 42, -14}, {6, 42, -14}, {7, 42, -14}, {8, 42, -14},
             {2, 43, -14}, {3, 43, -14}, {4, 43, -14}, {5, 43, -14}, {6, 43, -14},
             {7, 43, -14}, {8, 43, -14},
             {7, 41, -15}, {6, 41, -15}, {5, 41, -15}, {4, 41, -15}, {3, 41, -15},
             {6, 40, -15}, {5, 40, -15}, {4, 40, -15}, {5, 39, -15}, {4, 39, -15},
             {7, 42, -15}, {6, 42, -15}, {5, 42, -15}, {4, 42, -15}, {3, 42, -15},
             {2, 42, -15}, {7, 43, -15}, {6, 43, -15}, {5, 43, -15}, {4, 43, -15},
             {3, 43, -15}, {2, 43, -15}, {1, 44, -15}, {2, 44, -15}, {3, 44, -15},
             {4, 44, -15}, {7, 44, -15}, {8, 44, -15}, {8, 45, -15}, {7, 45, -15},
             {6, 42, -16}, {5, 42, -16}, {4, 42, -16}, {3, 42, -16}, {2, 43, -16},
             {3, 43, -16}, {4, 43, -16}, {5, 43, -16}, {6, 43, -16}, {7, 43, -16},
             {2, 44, -16}, {3, 44, -16}, {4, 44, -16}, {1, 44, -16}, {1, 45, -16},
             {2, 45, -16}, {3, 45, -16}, {1, 46, -16}, {0, 46, -16}, {0, 47, -18},
             {1, 47, -17}, {2, 47, -18}, {1, 47, -18}, {1, 48, -18}, {0, 48, -18},
             {0, 49, -18}, {1, 49, -19}, {0, 49, -19}, {1, 47, -19}, {0, 50, -19},
             {0, 51, -19}, {0, 52, -19}, {2, 45, -17}, {3, 45, -17}, {1, 45, -17},
             {2, 46, -17}, {1, 46, -17}, {1, 46, -18}, {2, 46, -18},
             {3, 8, -20}, {2, 8, -20}, {1, 8, -20}, {-2, 8, -20}, {-3, 8, -20},
             {4, 11, -19}, {5, 11, -19}
        };
        for (int[] pos : woodPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], mod_LionKing.rafikiWood, 0, 2);
        }

        int[][] leafPositions = {
             {0, 51, -20}, {1, 51, -20}, {1, 51, -21}, {-1, 51, -20}, {-1, 51, -21},
             {0, 51, -21}, {2, 51, -21}, {2, 51, -20}, {1, 51, -19}, {2, 51, -19},
             {2, 51, -18}, {1, 51, -18}, {0, 51, -18}, {-1, 52, -20}, {-1, 51, -19},
             {-1, 51, -18}, {-2, 51, -18}, {-2, 51, -19}, {-2, 51, -20}, {-2, 51, -21},
             {0, 50, -20}, {1, 50, -19}, {1, 50, -20}, {1, 50, -18}, {0, 50, -18},
             {-1, 50, -19}, {-1, 50, -18}, {-2, 51, -17}, {-1, 51, -17}, {0, 51, -17},
             {1, 51, -17}, {2, 51, -17}, {3, 51, -18}, {3, 51, -19}, {3, 51, -20},
             {1, 51, -22}, {0, 51, -22}, {-1, 51, -22}, {-1, 52, -21}, {-2, 51, -22},
             {-3, 51, -21}, {-3, 51, -20}, {-3, 51, -19}, {-1, 52, -19}, {-1, 52, -18},
             {-1, 51, -16}, {0, 51, -16}, {1, 51, -16}, {-3, 51, -18}, {-2, 51, -16},
             {0, 52, -18}, {1, 52, -18}, {1, 52, -19}, {2, 52, -19}, {1, 52, -20},
             {0, 52, -20}, {0, 52, -21}, {2, 50, -19},
             {8, 48, -18}, {9, 48, -17}, {8, 47, -18}, {8, 48, -17}, {7, 48, -18},
             {7, 48, -17}, {9, 48, -16}, {7, 48, -16}, {8, 48, -16}, {7, 47, -17},
             {7, 47, -18}, {8, 47, -19}, {9, 47, -17}, {9, 47, -18}, {9, 47, -19},
             {7, 47, -19}, {6, 47, -18}, {6, 47, -17}, {7, 47, -16}, {6, 47, -16},
             {8, 47, -16}, {9, 47, -16}, {10, 47, -16}, {10, 47, -17}, {10, 47, -18},
             {6, 47, -15}, {7, 47, -15}, {8, 47, -15}, {9, 47, -15}, {8, 46, -16},
             {7, 46, -17}
        };
        for (int[] pos : leafPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], mod_LionKing.rafikiLeaves, 0, 2);
        }

        int[][] portalFramePositions = {
             {-11, 40, 0}, {-11, 40, -1}, {-11, 41, -2}, {-11, 42, -2}, {-11, 43, -2},
             {-11, 44, -1}, {-11, 44, 0}, {-11, 43, 1}, {-11, 42, 1}, {-11, 41, 1},
             {-12, 44, 0}, {-12, 44, -1}, {-12, 44, -2}, {-12, 43, -2}, {-12, 42, -2},
             {-12, 41, -2}, {-12, 40, -1}, {-12, 40, -2}
        };
        for (int[] pos : portalFramePositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], mod_LionKing.outlandsPortalFrame, 0, 2);
        }

        int[][] airPositions = {
             {5, 7, -20}, {5, 6, -20}, {5, 5, -20}, {5, 4, -20}, {5, 11, -19},
             {4, 11, -19}, {3, 8, -20}, {2, 8, -20}, {1, 8, -20}, {-2, 8, -20},
             {-3, 8, -20}, {0, 47, -17}, {0, 46, -16}, {7, 45, -15}, {8, 45, -18},
             {7, 48, -15}, {8, 48, -15}, {6, 47, -19}, {-1, 52, -17}, {0, 52, -19},
             {-10, 43, 0}, {-10, 42, 0}, {-10, 41, 0}, {-10, 41, -1}, {-10, 42, -1},
             {-10, 43, -1}, {-11, 43, 0}, {-11, 43, -1}, {-11, 42, -1}, {-11, 42, 0},
             {-11, 41, 0}, {-11, 41, -1}, {-11, 40, -1}, {-12, 42, 0}, {-12, 41, 0},
             {-12, 41, -1}, {-12, 42, -1}, {-12, 43, -1}, {-12, 43, 0}
        };
        for (int[] pos : airPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], Blocks.air, 0, 2);
        }

        finishGeneration13(world, i, j, k);
     }

    private void finishGeneration13(World world, int i, int j, int k) {
        int[][] portalFramePositions = {
             {-13, 40, 1}, {-13, 40, 0}, {-13, 40, -1}, {-13, 40, -2}, {-13, 41, -2}, 
             {-13, 42, -2}, {-13, 43, -2}, {-13, 44, -2}, {-13, 44, -1}, {-13, 44, 0}, 
             {-13, 44, 1}, {-13, 43, 1}, {-13, 42, 1}, {-13, 41, 1}
        };
        for (int[] pos : portalFramePositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], mod_LionKing.outlandsPortalFrame, 0, 2);
        }

        int[][] woodPositions = {
             {-10, 40, 0}, {-10, 40, -1}, {-9, 40, 0}, {-9, 40, -1}, {-8, 40, 0}, 
             {-8, 40, -1}, {-7, 40, 0}, {-7, 40, -1}, {-6, 40, 0}, {-6, 40, -1}, 
             {-5, 39, 0}, {-5, 39, -1}, {-4, 39, 0}, {-4, 39, -1}, {-4, 40, -1},
             {-14, 41, 1}, {-14, 42, 1}, {-14, 43, 1}, {-14, 44, 1}, {-14, 44, 0}, 
             {-14, 44, -1}, {-14, 44, -2}, {-14, 43, -2}, {-14, 42, -2}, {-14, 41, -2}, 
             {-14, 40, 0}, {-14, 40, -1}, {-15, 41, 0}, {-15, 41, -1}, {-15, 42, 0}, 
             {-15, 42, -1}, {-15, 43, 0}, {-15, 43, -1}, {-8, 41, 0}, {-8, 41, -1}, 
             {-8, 42, 0}, {-8, 42, -1}, {-8, 43, 0}, {-8, 43, -1}, {-9, 41, 0}, 
             {-9, 41, -1}, {-9, 42, 0}, {-9, 42, -1}, {-9, 43, 0}, {-9, 43, -1},
             {-12, 44, -3}, {-12, 41, -3}, {-12, 41, 2}, {-12, 44, 2}, {-11, 44, 2}, 
             {-10, 43, 2}, {-10, 42, 2}, {-10, 44, 0}, {-10, 43, -2}, {-10, 44, -2}, 
             {-10, 43, 1}, {-10, 44, 1}, {-11, 43, 1}, {-11, 43, -2}, {-11, 44, 0}, 
             {-11, 44, -1},
             {18, 38, 1}, {18, 38, 0}, {18, 38, -1}, {18, 38, -2}, {19, 38, 1}, 
             {19, 38, 0}, {19, 38, -1}, {19, 38, -2}, {20, 38, 1}, {20, 38, 0}, 
             {20, 38, -1}, {20, 38, -2}, {21, 38, 1}, {21, 38, 0}, {21, 38, -1}, 
             {21, 38, -2}, {18, 39, 2}, {19, 39, 2}, {20, 39, 2}, {21, 39, 2}, 
             {22, 39, 2}, {18, 39, -3}, {19, 39, -3}, {22, 39, -3}, {22, 39, -2}, 
             {22, 39, -1}, {22, 39, 0}, {22, 39, 1}, {22, 38, -2}, {22, 38, -3}, 
             {22, 38, -4}, {21, 37, -3}, {21, 37, -4}, {20, 37, -3}, {20, 37, -4}, 
             {19, 37, -3}, {19, 37, -4}, {18, 37, -2}, {18, 37, -3}, {18, 40, 2},
             {17, 36, -5}, {18, 36, -5}, {19, 36, -5}, {20, 36, -5}, {17, 36, -6}, 
             {18, 36, -6}, {19, 36, -6}, {20, 36, -6}, {21, 37, -5}, {21, 37, -6}, 
             {21, 37, -7}, {20, 36, -7}, {20, 36, -8}, {20, 36, -9}, {20, 35, -9}, 
             {20, 35, -10}, {16, 35, -7}, {17, 35, -7}, {18, 35, -7}, {19, 35, -7}, 
             {16, 35, -8}, {17, 35, -8}, {18, 35, -8}, {19, 35, -8}, {15, 34, -9}, 
             {16, 34, -9}, {17, 34, -9}, {18, 34, -9}, {19, 34, -9}, {16, 34, -10}, 
             {17, 34, -10}, {18, 34, -10}, {19, 34, -10}, {16, 33, -10}, {16, 33, -11}, 
             {15, 33, -11}, {17, 33, -11}, {18, 33, -11}, {16, 33, -12}, {17, 33, -12}, 
             {18, 33, -12}, {15, 33, -12}, {14, 33, -11}, {14, 33, -12}, {14, 32, -12}, 
             {14, 32, -13}, {14, 32, -14}, {13, 32, -13}, {15, 32, -13}, {16, 32, -13}, 
             {13, 32, -14}, {15, 32, -14}, {16, 32, -14}, {12, 32, -11}, {12, 32, -12}, 
             {12, 32, -13}, {12, 32, -14}, {13, 32, -11}, {13, 32, -15}, {12, 32, -15}
        };
        for (int[] pos : woodPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], mod_LionKing.rafikiWood, 0, 2);
        }

        int[][] torchPositions = {
             {-12, 43, 2, 1}, {-12, 43, -3, 1}, {-10, 43, -2, 2}, {-10, 43, 1, 2},
             {-10, 44, -1, 2}, {-10, 44, 0, 2}
        };
        for (int[] pos : torchPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], Blocks.torch, pos[3], 2);
        }

        int[][] airPositions = {
             {-9, 41, -2}, {-9, 42, -2}, {-9, 42, 1}, {-9, 41, 1}, {-14, 42, 0}, 
             {-14, 41, 0}, {-14, 41, -1}, {-14, 42, -1}, {-14, 43, -1}, {-14, 43, 0}, 
             {-15, 43, 0}, {-15, 42, -1}, {-15, 42, 0}, {-15, 43, -1}, {17, 40, 0}, 
             {17, 40, -1}, {17, 40, 1}, {17, 40, -2}, {19, 39, -3}, {18, 39, -3}, 
             {22, 38, -2}, {22, 39, -3}, {21, 37, -7}, {20, 35, -10}, {19, 34, -10}, 
             {19, 34, -9}, {15, 35, -10}, {15, 35, -9}, {18, 33, -11}, {18, 33, -12}, 
             {16, 33, -10}, {13, 32, -11}, {12, 32, -11}, {15, 32, -13}, {15, 32, -14}, 
             {14, 32, -12}, {14, 32, -14}, {13, 32, -14}, {13, 33, -11}
        };
        for (int[] pos : airPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], Blocks.air, 0, 2);
        }

        finishGeneration14(world, i, j, k);
     }

    private void finishGeneration14(World world, int i, int j, int k) {
        int[][] woodPositions = {
             {13, 32, -11, 0}, {12, 32, -11, 0}, {12, 33, -11, 0}, {13, 33, -11, 0}, {13, 34, -11, 0},
             {14, 33, -10, 0}, {14, 34, -10, 0}, {11, 32, -12, 0}, {12, 32, -12, 0}, {13, 32, -12, 0},
             {12, 33, -12, 0}, {13, 33, -12, 0}, {10, 33, -13, 0}, {11, 32, -13, 0}, {12, 32, -13, 0},
             {9, 32, -13, 0},
             {19, 35, -9, 1}, {19, 35, -10, 1}, {19, 35, -11, 1}, {18, 34, -11, 1}, {18, 34, -12, 1},
             {18, 34, -13, 1}, {18, 34, -14, 1}, {18, 34, -15, 1}, {17, 34, -15, 1}, {16, 34, -15, 1},
             {15, 34, -15, 1}, {14, 34, -15, 1}, {13, 32, -16, 1}, {12, 32, -16, 1}, {13, 33, -16, 1},
             {12, 33, -16, 1}, {14, 33, -16, 1}, {14, 33, -15, 1}, {14, 34, -16, 1}, {17, 33, -13, 1},
             {17, 33, -14, 1}, {17, 33, -15, 1}, {17, 33, -16, 1}, {17, 33, -17, 1}, {16, 33, -17, 1},
             {15, 33, -17, 1}, {14, 33, -17, 1}, {13, 33, -17, 1}, {12, 33, -17, 1}, {12, 32, -17, 1},
             {11, 32, -17, 1},
             {16, 32, -13, 2}, {15, 32, -13, 2}, {14, 32, -13, 2}, {13, 32, -13, 2}, {16, 32, -14, 2},
             {15, 32, -14, 2}, {14, 32, -14, 2}, {13, 32, -14, 2}, {15, 32, -15, 2}, {14, 32, -15, 2},
             {13, 32, -15, 2}, {16, 32, -16, 2}, {15, 32, -16, 2}, {14, 32, -16, 2}, {13, 32, -16, 2},
             {16, 31, -15, 2}, {15, 31, -15, 2}, {14, 31, -15, 2}, {13, 31, -15, 2}, {13, 31, -16, 2},
             {14, 31, -16, 2}, {15, 31, -16, 2}, {16, 31, -16, 2}, {16, 31, -17, 2}, {15, 31, -17, 2},
             {14, 31, -17, 2}, {13, 31, -17, 2},
             {17, 32, -15, 1}, {17, 32, -16, 1}, {17, 32, -17, 1}, {17, 32, -18, 1}, {16, 32, -18, 1},
             {15, 32, -18, 1}, {14, 32, -18, 1}, {13, 32, -18, 1}, {16, 31, -18, 1}, {15, 31, -18, 1},
             {14, 31, -18, 1}, {13, 31, -18, 1}, {17, 32, -19, 1}, {16, 32, -19, 1}, {15, 32, -19, 1},
             {14, 32, -19, 1}, {13, 32, -19, 1}, {13, 31, -19, 1}, {12, 31, -19, 1}, {11, 31, -19, 1},
             {10, 31, -19, 1}, {10, 30, -18, 1}, {9, 30, -18, 1}, {10, 31, -18, 1}, {9, 31, -18, 1},
             {11, 31, -17, 1},
             {9, 32, -18, 1}, {8, 31, -18, 1}, {8, 30, -19, 1}, {9, 30, -19, 1}, {8, 31, -19, 1}, 
             {7, 30, -19, 1}, {7, 29, -19, 1},
             {6, 28, -15, 0}, {5, 28, -15, 0}, {4, 28, -15, 0}, {3, 28, -15, 0}, {2, 28, -15, 0},
             {1, 28, -15, 0}, {6, 29, -15, 0}, {5, 29, -15, 0}, {4, 29, -15, 0}, {3, 29, -15, 0},
             {2, 29, -15, 0}, {6, 30, -15, 0}, {5, 30, -15, 0}, {4, 30, -15, 0}, {3, 30, -15, 0},
             {-1, 26, -15, 0}, {-1, 25, -15, 0}, {-5, 25, -13, 0}, {-5, 26, -13, 0}, {-7, 24, -12, 0},
             {-7, 25, -12, 0},
             {6, 28, -16, 2}, {5, 28, -16, 2}, {6, 28, -17, 2}, {6, 28, -18, 2}, {5, 28, -17, 2},
             {5, 28, -18, 2}, {6, 28, -19, 2}, {5, 28, -19, 2}, {4, 27, -16, 2}, {4, 27, -17, 2},
             {4, 27, -18, 2}, {4, 27, -19, 2}, {3, 27, -16, 2}, {3, 27, -17, 2}, {3, 27, -18, 2},
             {3, 27, -19, 2}, {2, 27, -16, 2}, {2, 27, -17, 2}, {2, 27, -18, 2}, {2, 27, -19, 2},
             {1, 27, -16, 2}, {1, 27, -17, 2}, {1, 27, -18, 2}, {1, 27, -19, 2}, {0, 26, -16, 2},
             {0, 26, -17, 2}, {0, 26, -18, 2}, {0, 26, -19, 2}, {-1, 26, -16, 2}, {-1, 26, -17, 2},
             {-1, 26, -19, 2}, {-2, 25, -15, 2}, {-2, 25, -16, 2}, {-2, 25, -17, 2}, {-2, 25, -18, 2},
             {-3, 25, -15, 2}, {-3, 25, -16, 2}, {-3, 25, -17, 2}, {-3, 25, -18, 2}, {-4, 24, -14, 2},
             {-4, 24, -15, 2}, {-4, 24, -16, 2}, {-4, 24, -17, 2}, {-5, 24, -14, 2}, {-5, 24, -15, 2},
             {-5, 24, -16, 2}, {-5, 24, -17, 2}, {-6, 23, -13, 2}, {-6, 23, -14, 2}, {-6, 23, -15, 2},
             {-6, 23, -16, 2}, {-7, 23, -13, 2}, {-7, 23, -14, 2}, {-7, 23, -15, 2}, {-7, 23, -16, 2},
             {-8, 22, -12, 2}, {-8, 22, -13, 2},
             {5, 28, -20, 1}, {5, 29, -20, 1}, {6, 29, -20, 1}, {4, 28, -20, 1}, {3, 28, -20, 1},
             {2, 28, -20, 1}, {1, 28, -20, 1}, {0, 28, -20, 1}, {0, 27, -20, 1}, {-1, 27, -20, 1},
             {-2, 27, -20, 1}, {-2, 26, -19, 1}, {-3, 26, -19, 1}, {-4, 26, -19, 1},
             {16, 40, -2, 1}, {16, 40, -1, 1}, {16, 40, 0, 1}, {16, 40, 1, 1}, {17, 41, -3, 0},
             {17, 41, 2, 0}, {17, 42, -3, 0}, {17, 42, -2, 0}, {17, 42, 2, 0}, {17, 43, -3, 0},
             {17, 43, -2, 0}, {17, 43, 1, 0}, {17, 43, 2, 0}, {17, 44, -1, 0}, {17, 44, 0, 0},
             {17, 44, 1, 0}
        };
        for (int[] pos : woodPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], mod_LionKing.rafikiWood, pos[3], 2);
        }

        int[][] torchPositions = {
             {22, 40, 2, 5}, {22, 40, -3, 5}, {22, 39, -5, 5}, {21, 38, -7, 5}, {20, 37, -9, 5},
             {19, 36, -11, 5}, {18, 35, -13, 5}, {17, 34, -17, 5}, {12, 34, -17, 5}, {9, 32, -18, 5},
             {5, 30, -20, 5}, {0, 29, -20, 5}, {-2, 28, -20, 5}, {-4, 27, -19, 5}, {18, 43, 1, 1},
             {18, 43, -2, 1}, {16, 43, -2, 2}, {16, 43, 1, 2}
        };
        for (int[] pos : torchPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], Blocks.torch, pos[3], 2);
        }

        int[][] airPositions = {
             {20, 35, -9}, {14, 33, -16}, {14, 34, -15}, {13, 33, -16}, {12, 33, -16}, 
             {13, 32, -15}, {13, 32, -14}, {13, 32, -13}, {12, 32, -13}, {12, 32, -14}, 
             {14, 33, -15}, {15, 34, -15}, {16, 34, -15}, {17, 34, -15}, {18, 34, -15}, 
             {17, 33, -13}, {16, 33, -13}, {15, 33, -13}, {14, 33, -13}, {14, 33, -14}, 
             {15, 33, -14}, {16, 33, -14}, {18, 34, -14}, {17, 33, -14}, {16, 32, -12}, 
             {16, 31, -14}, {13, 32, -18}, {14, 32, -18}, {15, 32, -18}, {16, 32, -18}, 
             {13, 32, -19}, {12, 32, -19}, {12, 31, -19}, {12, 30, -18}, {11, 31, -19}, 
             {10, 31, -19}, {14, 32, -19}, {15, 32, -19}, {16, 32, -19}, {17, 32, -18}, 
             {17, 32, -19}, {17, 32, -17}, {17, 32, -16}, {17, 33, -15}, {17, 32, -15}, 
             {15, 31, -18}, {15, 31, -17}, {16, 31, -17}, {16, 31, -16}, {16, 31, -18}, 
             {16, 31, -15}, {15, 31, -16}, {15, 31, -15}, {14, 31, -16}, {14, 31, -15}, 
             {13, 31, -16}, {13, 31, -15}, {-1, 26, -18}, {-5, 24, -13}, {-5, 24, -16}, 
             {-7, 22, -14}, {22, 40, -2}, {16, 40, 1}, {16, 40, 0}, {16, 40, -1}, {16, 40, -2}
        };
        for (int[] pos : airPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], Blocks.air, 0, 2);
        }

        finishGeneration15(world, i, j, k);
     }

    private void finishGeneration15(World world, int i, int j, int k) {
        for (int j1 = 40; j1 < 56; j1++) {
        for (int i1 = -1; i1 >= -2; i1--) {
        for (int k1 = -1; k1 >= -2; k1--) {
            generateCircle(world, i + i1 + 6, j + j1, k + k1, 10, Blocks.air, 0, false);
            }
            }
        }
        int[][] woodPositions = {
             {-8, 22, -14, 2}, {-8, 22, -15, 2}, {-9, 22, -12, 2}, {-9, 22, -13, 2}, {-9, 22, -14, 2},
             {-9, 22, -15, 2},
             {-10, 21, -11, 2}, {-10, 21, -12, 2}, {-10, 21, -13, 2}, {-10, 21, -14, 2},
             {-11, 21, -11, 2}, {-11, 21, -12, 2}, {-11, 21, -13, 2}, {-11, 21, -14, 2},
             {-4, 25, -18, 1}, {-5, 25, -18, 1}, {-6, 25, -18, 1}, {-6, 24, -17, 1}, {-7, 24, -17, 1},
             {-8, 24, -17, 1}, {-8, 23, -16, 1}, {-9, 23, -16, 1}, {-10, 23, -16, 1}, {-10, 22, -15, 1},
             {-11, 22, -15, 1}, {-12, 22, -15, 1},
             {-11, 20, -10, 0}, {-11, 21, -10, 0}, {-11, 22, -10, 0}, {-8, 24, -11, 0}, {-10, 24, -9, 0},
             {-9, 24, -10, 0}, {-9, 25, -9, 0}, {-9, 26, -9, 0}, {-10, 25, -8, 0}, {-10, 24, -8, 0},
             {-11, 24, -7, 0}, {-11, 25, -6, 0}, {-12, 20, -8, 0}, {-12, 20, -9, 0}, {-12, 21, -8, 0},
             {-12, 21, -9, 0}, {-12, 22, -8, 0}, {-12, 23, -8, 0},
             {-12, 20, -10, 2}, {-12, 20, -11, 2}, {-12, 20, -12, 2}, {-12, 20, -13, 2},
             {-13, 20, -10, 2}, {-13, 20, -11, 2}, {-13, 20, -12, 2}, {-13, 20, -13, 2},
             {-12, 21, -14, 1}, {-13, 21, -14, 1}, {-14, 21, -14, 1}, {-14, 20, -14, 1},
             {-14, 19, -10, 2}, {-14, 19, -11, 2}, {-14, 19, -12, 2}, {-14, 19, -13, 2},
             {-15, 19, -10, 2}, {-15, 19, -11, 2}, {-15, 19, -12, 2}, {-15, 19, -13, 2},
             {-16, 19, -10, 2}, {-16, 19, -11, 2}, {-16, 19, -12, 2}, {-16, 19, -13, 2},
             {-17, 19, -10, 2}, {-17, 19, -11, 2}, {-17, 19, -12, 2}, {-17, 19, -13, 2},
             {-15, 20, -14, 1}, {-16, 20, -14, 1}, {-17, 20, -14, 1}, {-18, 20, -14, 1},
             {-18, 20, -13, 1}, {-18, 20, -12, 1}, {-18, 20, -11, 1}, {-18, 20, -10, 1}, {-18, 20, -9, 1},
             {-14, 18, -9, 2}, {-14, 18, -8, 2}, {-15, 18, -9, 2}, {-15, 18, -8, 2},
             {-16, 18, -9, 2}, {-16, 18, -8, 2}, {-17, 18, -9, 2}, {-17, 18, -8, 2},
             {-14, 17, -7, 2}, {-14, 17, -6, 2}, {-15, 17, -7, 2}, {-15, 17, -6, 2},
             {-16, 17, -7, 2}, {-16, 17, -6, 2}, {-17, 17, -7, 2}, {-17, 17, -6, 2},
             {-18, 19, -9, 1}, {-18, 19, -8, 1}, {-18, 18, -8, 1}, {-18, 18, -7, 1}, {-18, 17, -6, 1},
             {-19, 17, -6, 1}, {-19, 18, -6, 1}, {-19, 18, -7, 1}
        };
        for (int[] pos : woodPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], mod_LionKing.rafikiWood, pos[3], 2);
        }

        int[][] leafPositions = {
             {-18, 21, -9}, {-18, 21, -10}, {-19, 20, -7}, {-19, 20, -6}, {-11, 26, -9}, 
             {-10, 26, -8}, {-11, 25, -7}
        };
        for (int[] pos : leafPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], mod_LionKing.rafikiLeaves, 0, 2);
        }

        int[][] torchPositions = {
             {-6, 26, -18, 5}, {-8, 25, -17, 5}, {-10, 24, -16, 5}, {-12, 23, -15, 5},
             {-14, 22, -14, 5}, {-18, 21, -14, 5}, {-18, 21, -10, 5}, {-19, 19, -6, 5}
        };
        for (int[] pos : torchPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], Blocks.torch, pos[3], 2);
        }

        int[][] airPositions = {
             {-18, 19, -7}, {-18, 18, -8}, {-18, 17, -6}, {-14, 17, -7}, {-14, 17, -6}, 
             {-16, 16, -7}, {-16, 16, -6}, {-16, 23, -15}, {-16, 23, -14}, {-16, 24, -14}, 
             {-16, 22, -15}, {-16, 21, -15}, {-16, 20, -15}
        };
        for (int[] pos : airPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], Blocks.air, 0, 2);
        }

        finishGeneration16(world, i, j, k);
     }

    private void finishGeneration16(World world, int i, int j, int k) {
        for (int i1 = -3; i1 < 15; i1++) {
        for (int k1 = -5; k1 < 5; k1++) {
        for (int j1 = 40; j1 < 56; j1++) {
            if (world.getBlock(i + i1, j + j1, k + k1) == RAFIKI_WOOD) {
                world.setBlock(i + i1, j + j1, k + k1, RAFIKI_LEAVES, 0, 2);
                FMLLog.fine("Replaced rafikiWood with rafikiLeaves at (%d, %d, %d)", i + i1, j + j1, k + k1);
                }
            }
            }
        }
        int[][] rootPositions = {
             {-10, 4, 21, 2}, {-11, 4, 21, 2}, {-8, 4, 21, 2}, {-8, 4, 22, 2}, {-9, 4, 22, 2},
             {-10, 4, 22, 2}, {-11, 4, 22, 2}, {-7, 3, 23, 2}, {-8, 3, 23, 2}, {-9, 3, 23, 2},
             {-10, 3, 23, 2}, {-7, 3, 24, 2}, {-8, 3, 24, 2}, {-9, 3, 24, 2}, {-10, 3, 24, 2},
             {-6, 2, 25, 2}, {-7, 2, 25, 2}, {-8, 2, 25, 2}, {-9, 2, 25, 2}, {-6, 2, 26, 2},
             {-7, 2, 26, 2}, {-8, 2, 26, 2}, {-9, 2, 26, 2}, {-5, 1, 27, 2}, {-6, 1, 27, 2},
             {-7, 1, 27, 2}, {-8, 1, 27, 2}, {-5, 1, 28, 2}, {-6, 1, 28, 2}, {-7, 1, 28, 2},
             {-8, 1, 28, 2}, {-4, 0, 29, 2}, {-5, 0, 29, 2}, {-6, 0, 29, 2}, {-7, 0, 29, 2},
             {-4, 0, 30, 2}, {-5, 0, 30, 2}, {-6, 0, 30, 2}, {-7, 0, 30, 2}, {-3, -1, 31, 2},
             {-4, -1, 31, 2}, {-5, -1, 31, 2}, {-6, -1, 31, 2}, {-3, -1, 32, 2}, {-4, -1, 32, 2},
             {-5, -1, 32, 2}, {-6, -1, 32, 2}, {-2, -2, 33, 2}, {-3, -2, 33, 2}, {-4, -2, 33, 2},
             {-5, -2, 33, 2}, {-2, -2, 34, 2}, {-3, -2, 34, 2}, {-4, -2, 34, 2}, {-5, -2, 34, 2}
        };
        for (int[] pos : rootPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], mod_LionKing.rafikiWood, pos[3], 2);
        }

        int[][] branchPositions1 = {
             {-21, 15, -2, 1}, {-21, 15, -1, 1}, {-21, 15, 0, 1}, {-21, 15, 1, 1}, {-21, 15, 2, 1},
             {-21, 15, 3, 1}, {-21, 14, 3, 1}, {-21, 14, 4, 1}, {-21, 13, 3, 1}, {-20, 13, 5, 1},
             {-20, 13, 6, 1}, {-20, 13, 7, 1}, {-19, 12, 7, 1}, {-19, 12, 8, 1}, {-19, 12, 9, 1},
             {-18, 11, 9, 1}, {-18, 11, 10, 1}, {-18, 11, 11, 1}, {-17, 10, 11, 1}, {-17, 10, 12, 1},
             {-17, 10, 13, 1}, {-16, 9, 13, 1}, {-16, 9, 14, 1}, {-16, 9, 15, 1}, {-15, 8, 15, 1},
             {-15, 8, 16, 1}, {-14, 8, 15, 1}, {-15, 8, 17, 1}, {-14, 7, 17, 1}, {-14, 7, 18, 1},
             {-14, 7, 19, 1}, {-13, 6, 19, 1}, {-13, 6, 20, 1}, {-13, 6, 21, 1}, {-12, 5, 21, 1},
             {-12, 5, 22, 1}, {-12, 5, 23, 1}, {-11, 4, 23, 1}, {-11, 4, 24, 1}, {-11, 4, 25, 1},
             {-10, 3, 25, 1}, {-10, 3, 26, 1}, {-10, 3, 27, 1}, {-9, 2, 27, 1}, {-9, 2, 28, 1},
             {-9, 2, 29, 1}, {-8, 1, 29, 1}, {-8, 1, 30, 1}, {-8, 1, 31, 1}, {-7, 0, 31, 1},
             {-7, 0, 32, 1}, {-6, -1, 33, 1}, {-6, -1, 34, 1}
        };
        for (int[] pos : branchPositions1) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], mod_LionKing.rafikiWood, pos[3], 2);
        }

        int[][] topPositions = {
             {11, 28, -14, 0}, {12, 30, -13, 0}, {12, 29, -13, 0}, {12, 28, -13, 0}, {12, 27, -13, 0},
             {13, 31, -13, 0}, {13, 31, -12, 0}, {13, 30, -12, 0}, {13, 29, -12, 0}, {13, 28, -12, 0},
             {13, 31, -14, 0}, {13, 30, -14, 0}, {13, 29, -13, 0}, {13, 28, -13, 0}, {14, 32, -12, 0},
             {14, 32, -11, 0}, {14, 31, -14, 0}, {14, 31, -13, 0}, {14, 31, -11, 0}, {14, 30, -14, 0},
             {14, 30, -13, 0}, {14, 30, -12, 0}, {14, 30, -11, 0}, {14, 29, -13, 0}, {14, 29, -12, 0},
             {14, 29, -11, 0}, {14, 28, -12, 0}, {14, 28, -11, 0}, {15, 31, -15, 0}, {15, 31, -14, 0},
             {15, 30, -13, 0}, {15, 30, -12, 0}, {15, 30, -11, 0}, {15, 30, -10, 0}, {15, 29, -11, 0},
             {15, 29, -10, 0}, {15, 28, -10, 0}
        };
        for (int[] pos : topPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], mod_LionKing.rafikiWood, pos[3], 2);
        }

        int[][] crownPositions = {
             {18, 38, 2, 0}, {18, 37, 2, 0}, {18, 37, 1, 0}, {18, 37, 0, 0}, {18, 37, -1, 0},
             {18, 37, -2, 0}, {18, 36, 2, 0}, {18, 36, 1, 0}, {18, 36, 0, 0}, {18, 36, -1, 0},
             {18, 36, -2, 0}, {18, 35, 1, 0}, {18, 35, 0, 0}, {18, 34, 0, 0}, {19, 38, 2, 0},
             {19, 37, 2, 0}, {19, 37, 1, 0}, {19, 37, 0, 0}, {19, 37, -1, 0}, {19, 37, -2, 0},
             {19, 36, 1, 0}, {19, 36, 0, 0}, {19, 35, 0, 0}, {20, 38, 2, 0}, {20, 37, 2, 0},
             {20, 37, 1, 0}, {20, 37, 0, 0}, {20, 37, -1, 0}, {20, 37, -2, 0}, {20, 36, 0, 0},
             {21, 37, 0, 0}, {22, 38, 0, 0}, {22, 38, -1, 0}
        };
        for (int[] pos : crownPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], mod_LionKing.rafikiWood, pos[3], 2);
        }

        int[][] leafPositions = {
             {15, 32, -15}, {15, 32, -14}, {14, 33, -12}, {14, 32, -13}, {13, 32, -14}, 
             {15, 31, -10}, {15, 31, -11}, {19, 39, 3}, {20, 39, 3}, {22, 40, 1}, 
             {22, 39, -2}, {18, 38, 3}, {19, 37, 3}
        };
        for (int[] pos : leafPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], mod_LionKing.rafikiLeaves, 0, 2);
        }

        int[][] torchPositions = {
             {-21, 17, -2, 5}, {-21, 16, 3, 5}, {-21, 15, 5, 5}, {-20, 14, 7, 5}, {-19, 13, 9, 5},
             {-18, 12, 11, 5}, {-17, 11, 13, 5}, {-16, 10, 15, 5}, {-15, 9, 17, 5}, {-14, 8, 19, 5},
             {-13, 7, 21, 5}, {-12, 6, 23, 5}, {-11, 5, 25, 5}, {-10, 4, 27, 5}, {-9, 3, 29, 5},
             {-8, 2, 31, 5}, {-7, 1, 32, 5}, {-6, 0, 34, 5}
        };
        for (int[] pos : torchPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], Blocks.torch, pos[3], 2);
        }

        int[][] airPositions = {
             {-9, 3, 28}, {-16, 17, -12}, {-15, 16, -11}, {-15, 17, -12}, {-16, 17, -11}, 
             {-16, 18, -12}, {14, 31, -12}, {15, 31, -13}, {12, 30, -14}, {15, 29, -12}, 
             {18, 33, 0}, {18, 33, -1}, {18, 34, -1}, {18, 35, 1}, {19, 36, -1}, {21, 37, -1}
        };
        for (int[] pos : airPositions) {
             world.setBlock(i + pos[0], j + pos[1], k + pos[2], Blocks.air, 0, 2);
        }
     }

}
