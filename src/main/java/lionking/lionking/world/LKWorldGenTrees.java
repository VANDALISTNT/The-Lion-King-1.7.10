package lionking.world;

import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.biome.BiomeGenBase;
import java.util.Random;
import cpw.mods.fml.common.FMLLog;

import lionking.mod_LionKing;
import lionking.biome.LKBiomeGenMountains;
import lionking.biome.LKBiomeGenWoodedSavannah;
import lionking.biome.LKBiomeGenAridSavannah;

public class LKWorldGenTrees extends WorldGenerator {
    private boolean doBlockNotify;
    private boolean isLarge;

    public LKWorldGenTrees(boolean flag) {
        super();
        doBlockNotify = flag;
        FMLLog.fine("Initialized LKWorldGenTrees with doBlockNotify=%b", flag);
    }

    public LKWorldGenTrees setLarge() {
        isLarge = true;
        FMLLog.fine("Set LKWorldGenTrees to large mode");
        return this;
    }

    @Override
    public boolean generate(World world, Random random, int i, int j, int k) {
        if (mod_LionKing.prideWood == null || mod_LionKing.leaves == null) {
            FMLLog.severe("prideWood or leaves is null, cannot generate tree at (%d, %d, %d)", i, j, k);
            return false;
        }

        int height = random.nextInt(6);
        BiomeGenBase biome = world.getBiomeGenForCoords(i, k);
        int trunkWidth = 1;

        if (biome instanceof LKBiomeGenMountains && random.nextInt(3) != 0) {
            height += random.nextInt(6) + 5;
        }
        if (isLarge || (!doBlockNotify && biome instanceof LKBiomeGenWoodedSavannah && random.nextInt(3) == 0)) {
            height += random.nextInt(3) + 3;
            trunkWidth = 2;
        }

        if (j < 1 || j + height + 5 > 256) {
            FMLLog.fine("Invalid height for tree at (%d, %d, %d), height=%d", i, j, k, height);
            return false;
        }

        Block groundBlock = world.getBlock(i, j - 1, k);
        if (biome instanceof LKBiomeGenAridSavannah) {
            if (groundBlock != Blocks.sand && groundBlock != Blocks.dirt && groundBlock != Blocks.grass) {
                FMLLog.fine("Invalid ground block %s for tree in AridSavannah at (%d, %d, %d)", groundBlock.getUnlocalizedName(), i, j, k);
                return false;
            }
        } else if (groundBlock != Blocks.grass && groundBlock != Blocks.dirt) {
            FMLLog.fine("Invalid ground block %s for tree in biome %s at (%d, %d, %d)", groundBlock.getUnlocalizedName(), biome.biomeName, i, j, k);
            return false;
        }

        if (!isBlockReplaceable(world, i, j, k)) {
            FMLLog.fine("Non-replaceable block at tree start position (%d, %d, %d)", i, j, k);
            return false;
        }

        if (!checkSpace(world, i, j, k, height, trunkWidth)) {
            FMLLog.fine("Insufficient space for tree at (%d, %d, %d)", i, j, k);
            return false;
        }

        placeTree(world, i, j, k, height, trunkWidth);
        world.setBlock(i, j - 1, k, Blocks.dirt, 0, doBlockNotify ? 3 : 2);

        if (height > 4) tryToBranchAt(world, random, i, j, k, height);
        if (height > 7) tryToBranchAt(world, random, i, j, k, height - 1);
        if (random.nextInt(4) != 0) {
            int tries = random.nextInt(3) == 0 ? 2 : (random.nextInt(5) == 0 ? 3 : 1);
            for (int t = 0; t < tries; t++) {
                tryCanopyShift(world, random, i, j + height + 3, k, random.nextInt(3) == 0 ? 1 : 2, random.nextInt(4));
            }
        }

        FMLLog.fine("Generated tree at (%d, %d, %d) with height=%d in biome %s", i, j, k, height, biome.biomeName);
        return true;
    }

    private boolean checkSpace(World world, int i, int j, int k, int height, int trunkWidth) {
        for (int j2 = 0; j2 <= height; j2++) {
            for (int i1 = i; i1 < i + trunkWidth; i1++) {
                for (int k1 = k; k1 < k + trunkWidth; k1++) {
                    if (!isBlockReplaceable(world, i1, j + j2, k1)) {
                        return false;
                    }
                }
            }
        }

        if (!checkFoliageLayer(world, i, j + height + 3, k, 3) ||
            !checkFoliageLayer(world, i, j + height + 4, k, 2) ||
            !checkBranches(world, i, j + height + 1, k)) {
            return false;
        }
        return true;
    }

    private boolean checkFoliageLayer(World world, int i, int j, int k, int radius) {
        for (int i2 = -radius; i2 <= radius; i2++) {
            for (int k2 = -radius; k2 <= radius; k2++) {
                if (!isBlockReplaceable(world, i + i2, j, k + k2)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkBranches(World world, int i, int j, int k) {
        int[][] offsets = {{0, 0}, {-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-2, 0}, {2, 0}, {0, -2}, {0, 2}};
        for (int[] offset : offsets) {
            if (!isBlockReplaceable(world, i + offset[0], j, k + offset[1]) ||
                !isBlockReplaceable(world, i + offset[0], j + 1, k + offset[1]) ||
                !isBlockReplaceable(world, i + offset[0], j + 2, k + offset[1])) {
                return false;
            }
        }
        return true;
    }

    private void placeTree(World world, int i, int j, int k, int height, int trunkWidth) {
        placeFoliageLayer(world, i, j + height + 3, k, 3);
        clearCorners(world, i, j + height + 3, k, 3);
        placeFoliageLayer(world, i, j + height + 4, k, 2);
        clearCorners(world, i, j + height + 4, k, 2);

        for (int j2 = 0; j2 <= height; j2++) {
            for (int i1 = i; i1 < i + trunkWidth; i1++) {
                for (int k1 = k; k1 < k + trunkWidth; k1++) {
                    world.setBlock(i1, j + j2, k1, mod_LionKing.prideWood, 0, doBlockNotify ? 3 : 2);
                }
            }
        }

        int[][] branches = {{0, 0}, {-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-2, 0}, {2, 0}, {0, -2}, {0, 2}};
        for (int h = 1; h <= 3; h++) {
            for (int[] branch : branches) {
                world.setBlock(i + branch[0], j + height + h, k + branch[1], mod_LionKing.prideWood, 0, doBlockNotify ? 3 : 2);
            }
        }
    }

    private void placeFoliageLayer(World world, int i, int j, int k, int radius) {
        for (int i2 = -radius; i2 <= radius; i2++) {
            for (int k2 = -radius; k2 <= radius; k2++) {
                world.setBlock(i + i2, j, k + k2, mod_LionKing.leaves, 0, doBlockNotify ? 3 : 2);
            }
        }
    }

    private void clearCorners(World world, int i, int j, int k, int radius) {
        world.setBlock(i - radius, j, k - radius, Blocks.air, 0, doBlockNotify ? 3 : 2);
        world.setBlock(i + radius, j, k - radius, Blocks.air, 0, doBlockNotify ? 3 : 2);
        world.setBlock(i - radius, j, k + radius, Blocks.air, 0, doBlockNotify ? 3 : 2);
        world.setBlock(i + radius, j, k + radius, Blocks.air, 0, doBlockNotify ? 3 : 2);
    }

    private void tryCanopyShift(World world, Random random, int i, int j, int k, int length, int direction) {
        int[][] offsets = {{-4, 0}, {4, 0}, {0, -4}, {0, 4}};
        int[] offset = offsets[direction];

        int dx = offset[0], dz = offset[1];
        if (checkCanopySpace(world, i + dx, j, k + dz)) {
            placeCanopy(world, i, j, k, dx, dz);
            if (length > 1) tryCanopyShift(world, random, i + dx / 4, j, k + dz / 4, length - 1, direction);
        }
    }

    private boolean checkCanopySpace(World world, int i, int j, int k) {
        for (int i1 = -3; i1 <= 3; i1++) {
            for (int k1 = -3; k1 <= 1; k1++) {
                if (!isBlockReplaceable(world, i + i1, j, k + k1) || !isBlockReplaceable(world, i + i1, j + 1, k + k1)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void placeCanopy(World world, int i, int j, int k, int dx, int dz) {
        int woodX = i + dx / 2, woodZ = k + dz / 2;
        world.setBlock(woodX, j, woodZ, mod_LionKing.prideWood, 0, doBlockNotify ? 3 : 2);
        for (int i1 = -2; i1 <= 2; i1++) {
            for (int k1 = -2; k1 <= 2; k1++) {
                if (Math.abs(i1) == 2 && Math.abs(k1) == 2) continue;
                world.setBlock(i + dx + i1, j, k + dz + k1, mod_LionKing.leaves, 0, doBlockNotify ? 3 : 2);
            }
        }
    }

    private void tryToBranchAt(World world, Random random, int i, int j, int k, int height) {
        int direction = random.nextInt(4);
        int[][] branchOffsets = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int[] offset = branchOffsets[direction];

        int dx = offset[0], dz = offset[1];
        if (checkBranchSpace(world, i + dx, j + height - 1, k + dz)) {
            placeBranch(world, i, j, k, height, dx, dz);
        }
    }

    private boolean checkBranchSpace(World world, int i, int j, int k) {
        for (int i1 = -3; i1 <= 3; i1++) {
            for (int k1 = -1; k1 <= 1; k1++) {
                if (!isBlockReplaceable(world, i + i1, j, k + k1) || !isBlockReplaceable(world, i + i1, j + 1, k + k1)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void placeBranch(World world, int i, int j, int k, int height, int dx, int dz) {
        world.setBlock(i + dx, j + height - 1, k + dz, mod_LionKing.prideWood, 0, doBlockNotify ? 3 : 2);
        world.setBlock(i + dx * 2, j + height, k + dz * 2, mod_LionKing.prideWood, 0, doBlockNotify ? 3 : 2);
        world.setBlock(i + dx * 3, j + height, k + dz * 3, mod_LionKing.prideWood, 0, doBlockNotify ? 3 : 2);
        for (int i1 = 1; i1 <= 4; i1++) {
            for (int k1 = -1; k1 <= 1; k1++) {
                world.setBlock(i + dx * i1, j + height, k + dz * i1 + k1, mod_LionKing.leaves, 0, doBlockNotify ? 3 : 2);
            }
        }
    }

    private boolean isBlockReplaceable(World world, int i, int j, int k) {
        Block block = world.getBlock(i, j, k);
        return block == Blocks.air || block == mod_LionKing.leaves || block == mod_LionKing.sapling || 
               block.isLeaves(world, i, j, k) || block == Blocks.tallgrass || block == Blocks.deadbush;
    }
}