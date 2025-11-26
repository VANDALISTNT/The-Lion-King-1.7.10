package lionking.world;

import net.minecraft.world.World;
import net.minecraft.util.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenerator;
import java.util.Random;
import cpw.mods.fml.common.FMLLog;
import lionking.mod_LionKing;

public class LKWorldGenHugeRainforestTrees extends WorldGenerator {
    private static final int MIN_HEIGHT = 25;
    private static final int HEIGHT_VARIATION = 25;
    private static final int MAX_WORLD_HEIGHT = 256;
    private static final int WOOD_METADATA = 1;

    public LKWorldGenHugeRainforestTrees(boolean notify) {
        super(notify);
        FMLLog.fine("Initialized LKWorldGenHugeRainforestTrees with notify=%b", notify);
    }

    public boolean generate(World world, Random random, int x, int y, int z) {
        FMLLog.fine("Attempting to generate huge rainforest tree at (%d, %d, %d)", x, y, z);

        if (mod_LionKing.prideWood == null || mod_LionKing.forestLeaves == null) {
            FMLLog.severe("Cannot generate huge rainforest tree at (%d, %d, %d): prideWood or forestLeaves is null", x, y, z);
            return false;
        }

        int height = MIN_HEIGHT + random.nextInt(HEIGHT_VARIATION);
        if (y < 1 || y + height + 5 >= MAX_WORLD_HEIGHT) {
            FMLLog.fine("Cannot generate huge rainforest tree at (%d, %d, %d): invalid height (height=%d)", x, y, z, height);
            return false;
        }

        if (!checkTrunkSpace(world, x, y, z, height)) {
            FMLLog.fine("Cannot generate huge rainforest tree at (%d, %d, %d): insufficient trunk space", x, y, z);
            return false;
        }

        if (!checkCanopySpace(world, x, y, z, height)) {
            FMLLog.fine("Cannot generate huge rainforest tree at (%d, %d, %d): insufficient canopy space", x, y, z);
            return false;
        }

        int blocksPlaced = 0;
        blocksPlaced += generateTrunk(world, random, x, y, z, height);
        blocksPlaced += generateCanopy(world, random, x, y, z, height);
        int midHeight = MathHelper.floor_double(height / 2.0);
        blocksPlaced += generateMidBranches(world, random, x, y, z, midHeight);

        if (blocksPlaced == 0) {
            FMLLog.warning("No blocks placed for huge rainforest tree at (%d, %d, %d)", x, y, z);
            return false;
        }

        FMLLog.info("Successfully generated huge rainforest tree at (%d, %d, %d) with height=%d and %d blocks", x, y, z, height, blocksPlaced);
        return true;
    }

    private int generateTrunk(World world, Random random, int x, int y, int z, int height) {
        int blocksPlaced = 0;
        for (int dx = x; dx < x + 2; dx++) {
            for (int dz = z; dz < z + 2; dz++) {
                for (int dy = y; dy < y + height; dy++) {
                    world.setBlock(dx, dy, dz, mod_LionKing.prideWood, WOOD_METADATA, 2);
                    FMLLog.fine("Placed prideWood at (%d, %d, %d), meta=%d", dx, dy, dz, WOOD_METADATA);
                    blocksPlaced++;
                    if (random.nextInt(5) != 0) blocksPlaced += generateVines(world, random, dx, dy, dz);
                    if (dy == y) {
                        world.setBlock(dx, dy - 1, dz, Blocks.dirt, 0, 2);
                        FMLLog.fine("Converted to dirt at (%d, %d, %d)", dx, dy - 1, dz);
                        blocksPlaced++;
                    }
                }
            }
        }
        return blocksPlaced;
    }

    private int generateCanopy(World world, Random random, int x, int y, int z, int height) {
        int blocksPlaced = 0;
        for (int dx = x - 1; dx < x + 3; dx++) {
            for (int dz = z - 1; dz < z + 3; dz++) {
                if (!isCorner(dx, dz, x - 1, z - 1, x + 2, z + 2)) {
                    world.setBlock(dx, y + height, dz, mod_LionKing.prideWood, WOOD_METADATA, 2);
                    FMLLog.fine("Placed prideWood at (%d, %d, %d), meta=%d", dx, y + height, dz, WOOD_METADATA);
                    blocksPlaced++;
                }
            }
        }

        for (int dx = x - 2; dx < x + 4; dx++) {
            for (int dz = z - 2; dz < z + 4; dz++) {
                if (!isCorner(dx, dz, x - 2, z - 2, x + 3, z + 3)) {
                    world.setBlock(dx, y + height + 1, dz, mod_LionKing.forestLeaves, 0, 2);
                    FMLLog.fine("Placed forestLeaves at (%d, %d, %d)", dx, y + height + 1, dz);
                    blocksPlaced++;
                }
            }
        }

        blocksPlaced += placeEdgeWood(world, x, y + height + 1, z, -2, 2, -3, 4);
        for (int dx = x - 5; dx < x + 7; dx++) {
            for (int dz = z - 5; dz < z + 7; dz++) {
                world.setBlock(dx, y + height + 2, dz, mod_LionKing.forestLeaves, 0, 2);
                FMLLog.fine("Placed forestLeaves at (%d, %d, %d)", dx, y + height + 2, dz);
                blocksPlaced++;
            }
        }

        blocksPlaced += placeExtendedWood(world, x, y + height + 2, z, -3, 3, -6, 7);
        for (int dx = x - 3; dx < x + 5; dx++) {
            for (int dz = z - 3; dz < z + 5; dz++) {
                world.setBlock(dx, y + height + 3, dz, mod_LionKing.forestLeaves, 0, 2);
                FMLLog.fine("Placed forestLeaves at (%d, %d, %d)", dx, y + height + 3, dz);
                blocksPlaced++;
            }
        }

        blocksPlaced += placeExtendedLeaves(world, x, y + height + 3, z, -4, 5, -2, 3);
        for (int dy = -1; dy < 2; dy++) {
            world.setBlock(x - 2 - dy, y + height + dy, z - 2 - dy, mod_LionKing.prideWood, WOOD_METADATA, 2);
            FMLLog.fine("Placed prideWood at (%d, %d, %d), meta=%d", x - 2 - dy, y + height + dy, z - 2 - dy, WOOD_METADATA);
            world.setBlock(x - 2 - dy, y + height + dy, z + 3 + dy, mod_LionKing.prideWood, WOOD_METADATA, 2);
            FMLLog.fine("Placed prideWood at (%d, %d, %d), meta=%d", x - 2 - dy, y + height + dy, z + 3 + dy, WOOD_METADATA);
            world.setBlock(x + 3 + dy, y + height + dy, z - 2 - dy, mod_LionKing.prideWood, WOOD_METADATA, 2);
            FMLLog.fine("Placed prideWood at (%d, %d, %d), meta=%d", x + 3 + dy, y + height + dy, z - 2 - dy, WOOD_METADATA);
            world.setBlock(x + 3 + dy, y + height + dy, z + 3 + dy, mod_LionKing.prideWood, WOOD_METADATA, 2);
            FMLLog.fine("Placed prideWood at (%d, %d, %d), meta=%d", x + 3 + dy, y + height + dy, z + 3 + dy, WOOD_METADATA);
            blocksPlaced += 4;
        }

        blocksPlaced += addCanopyVines(world, random, x, y, z, height);
        return blocksPlaced;
    }

    private int generateMidBranches(World world, Random random, int x, int y, int z, int midHeight) {
        int blocksPlaced = 0;
        for (int dx = x - 1; dx < x + 3; dx++) {
            for (int dz = z - 1; dz < z + 3; dz++) {
                if (!isCorner(dx, dz, x - 1, z - 1, x + 2, z + 2)) {
                    world.setBlock(dx, y + midHeight, dz, mod_LionKing.prideWood, WOOD_METADATA, 2);
                    FMLLog.fine("Placed prideWood at (%d, %d, %d), meta=%d", dx, y + midHeight, dz, WOOD_METADATA);
                    blocksPlaced++;
                    for (int dy = y; dy < y + midHeight; dy++) {
                        if (random.nextInt(40) == 0 && isBlockReplaceable(world, dx, dy, dz)) {
                            world.setBlock(dx, dy, dz, mod_LionKing.prideWood, WOOD_METADATA, 2);
                            FMLLog.fine("Placed sparse mid-branch at (%d, %d, %d), meta=%d", dx, dy, dz, WOOD_METADATA);
                            blocksPlaced++;
                        }
                    }
                    if (isBlockReplaceable(world, dx, y + midHeight + 1, dz)) {
                        world.setBlock(dx, y + midHeight + 1, dz, mod_LionKing.forestLeaves, 0, 2);
                        FMLLog.fine("Placed forestLeaves at (%d, %d, %d)", dx, y + midHeight + 1, dz);
                        blocksPlaced++;
                        blocksPlaced += generateVines(world, random, dx, y + midHeight + 1, dz);
                    }
                }
            }
        }
        return blocksPlaced;
    }

    private int generateVines(World world, Random random, int x, int y, int z) {
        int vinesPlaced = 0;
        if (random.nextInt(7) == 0 && world.isAirBlock(x - 1, y, z)) {
            vinesPlaced += placeVines(world, random, x - 1, y, z, 8);
        }
        if (random.nextInt(7) == 0 && world.isAirBlock(x + 1, y, z)) {
            vinesPlaced += placeVines(world, random, x + 1, y, z, 2);
        }
        if (random.nextInt(7) == 0 && world.isAirBlock(x, y, z - 1)) {
            vinesPlaced += placeVines(world, random, x, y, z - 1, 1);
        }
        if (random.nextInt(7) == 0 && world.isAirBlock(x, y, z + 1)) {
            vinesPlaced += placeVines(world, random, x, y, z + 1, 4);
        }
        return vinesPlaced;
    }

    private int placeVines(World world, Random random, int x, int y, int z, int meta) {
        world.setBlock(x, y, z, Blocks.vine, meta, 2);
        FMLLog.fine("Placed vine at (%d, %d, %d), meta=%d", x, y, z, meta);
        int length = random.nextInt(8) + 7;
        int vinesPlaced = 1;
        for (int dy = 1; dy <= length && world.isAirBlock(x, y - dy, z); dy++) {
            world.setBlock(x, y - dy, z, Blocks.vine, meta, 2);
            FMLLog.fine("Placed vine extension at (%d, %d, %d), meta=%d", x, y - dy, z, meta);
            vinesPlaced++;
        }
        return vinesPlaced;
    }

    private int addCanopyVines(World world, Random random, int x, int y, int z, int height) {
        int vinesPlaced = 0;
        for (int dx = x - 5; dx < x + 7; dx++) {
            for (int dz = z - 5; dz < z + 7; dz++) {
                for (int dy = y + height + 1; dy <= y + height + 3; dy++) {
                    if (world.getBlock(dx, dy, dz) == mod_LionKing.forestLeaves && world.isAirBlock(dx, dy - 1, dz)) {
                        vinesPlaced += generateVines(world, random, dx, dy, dz);
                    }
                }
            }
        }
        return vinesPlaced;
    }

    private boolean checkTrunkSpace(World world, int x, int y, int z, int height) {
        for (int dx = x; dx < x + 2; dx++) {
            for (int dz = z; dz < z + 2; dz++) {
                for (int dy = y; dy < y + height; dy++) {
                    if (!isBlockReplaceable(world, dx, dy, dz)) {
                        FMLLog.fine("Non-replaceable block at (%d, %d, %d) for trunk", dx, dy, dz);
                        return false;
                    }
                    if (dy == y) {
                        Block ground = world.getBlock(dx, dy - 1, dz);
                        if (ground != Blocks.grass && ground != Blocks.dirt) {
                            FMLLog.fine("Invalid ground block %s at (%d, %d, %d)", ground != null ? ground.getUnlocalizedName() : "null", dx, dy - 1, dz);
                            return false;
                        }
                        if (!isBlockReplaceable(world, dx, dy, dz) && world.getBlock(dx, dy, dz) != mod_LionKing.forestSapling) {
                            FMLLog.fine("Invalid start block at (%d, %d, %d)", dx, dy, dz);
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean checkCanopySpace(World world, int x, int y, int z, int height) {
        for (int dx = x - 1; dx < x + 3; dx++) {
            for (int dz = z - 1; dz < z + 3; dz++) {
                if (isCorner(dx, dz, x - 1, z - 1, x + 2, z + 2)) continue;
                if (!isBlockReplaceable(world, dx, y + height, dz)) {
                    FMLLog.fine("Non-replaceable block at (%d, %d, %d) for canopy", dx, y + height, dz);
                    return false;
                }
            }
        }

        for (int dx = x - 2; dx < x + 4; dx++) {
            for (int dz = z - 2; dz < z + 4; dz++) {
                if (isCorner(dx, dz, x - 2, z - 2, x + 3, z + 3)) continue;
                if (!isBlockReplaceable(world, dx, y + height + 1, dz)) {
                    FMLLog.fine("Non-replaceable block at (%d, %d, %d) for canopy", dx, y + height + 1, dz);
                    return false;
                }
            }
        }

        if (!checkEdgeBlocks(world, x, y + height + 1, z, -3, 4, -3, 4)) {
            FMLLog.fine("Non-replaceable edge block at y=%d for canopy", y + height + 1);
            return false;
        }

        for (int dx = x - 5; dx < x + 7; dx++) {
            for (int dz = z - 5; dz < z + 7; dz++) {
                if (!isBlockReplaceable(world, dx, y + height + 2, dz)) {
                    FMLLog.fine("Non-replaceable block at (%d, %d, %d) for canopy", dx, y + height + 2, dz);
                    return false;
                }
            }
        }

        if (!checkExtendedEdges(world, x, y + height + 2, z, -5, 6, -2, 3)) {
            FMLLog.fine("Non-replaceable extended edge block at y=%d for canopy", y + height + 2);
            return false;
        }

        for (int dx = x - 3; dx < x + 5; dx++) {
            for (int dz = z - 3; dz < z + 5; dz++) {
                if (!isBlockReplaceable(world, dx, y + height + 3, dz)) {
                    FMLLog.fine("Non-replaceable block at (%d, %d, %d) for canopy", dx, y + height + 3, dz);
                    return false;
                }
            }
        }

        if (!checkExtendedEdges(world, x, y + height + 3, z, -3, 4, -2, 3)) {
            FMLLog.fine("Non-replaceable extended edge block at y=%d for canopy", y + height + 3);
            return false;
        }

        for (int dy = -1; dy < 2; dy++) {
            if (!isBlockReplaceable(world, x - 2 - dy, y + height + dy, z - 2 - dy) ||
                !isBlockReplaceable(world, x - 2 - dy, y + height + dy, z + 3 + dy) ||
                !isBlockReplaceable(world, x + 3 + dy, y + height + dy, z - 2 - dy) ||
                !isBlockReplaceable(world, x + 3 + dy, y + height + dy, z + 3 + dy)) {
                FMLLog.fine("Non-replaceable block at y=%d for canopy corners", y + height + dy);
                return false;
            }
        }

        return true;
    }

    private boolean isBlockReplaceable(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        return block == Blocks.air || block == Blocks.vine || block == mod_LionKing.forestLeaves || 
               block == mod_LionKing.forestSapling || block.isLeaves(world, x, y, z) || 
               block == Blocks.tallgrass || block == Blocks.deadbush;
    }

    private boolean isCorner(int x, int z, int minX, int minZ, int maxX, int maxZ) {
        return (x == minX && z == minZ) || (x == maxX && z == minZ) ||
               (x == minX && z == maxZ) || (x == maxX && z == maxZ);
    }

    private boolean checkEdgeBlocks(World world, int x, int y, int z, int xOff1, int xOff2, int zOff1, int zOff2) {
        return isBlockReplaceable(world, x, y, z + zOff1) &&
               isBlockReplaceable(world, x + 1, y, z + zOff1) &&
               isBlockReplaceable(world, x, y, z + zOff2) &&
               isBlockReplaceable(world, x + 1, y, z + zOff2) &&
               isBlockReplaceable(world, x + xOff1, y, z) &&
               isBlockReplaceable(world, x + xOff1, y, z + 1) &&
               isBlockReplaceable(world, x + xOff2, y, z) &&
               isBlockReplaceable(world, x + xOff2, y, z + 1);
    }

    private boolean checkExtendedEdges(World world, int x, int y, int z, int xOff, int xOff2, int zOff, int zOff2) {
        for (int dx = x - 2; dx <= x + 3; dx++) {
            if (!isBlockReplaceable(world, dx, y, z + zOff) || !isBlockReplaceable(world, dx, y, z + zOff2)) {
                return false;
            }
        }
        for (int dz = z - 2; dz <= z + 3; dz++) {
            if (!isBlockReplaceable(world, x + xOff, y, dz) || !isBlockReplaceable(world, x + xOff2, y, dz)) {
                return false;
            }
        }
        return true;
    }

    private int placeEdgeWood(World world, int x, int y, int z, int xOff1, int xOff2, int zOff1, int zOff2) {
        int blocksPlaced = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                world.setBlock(x + i + xOff1, y, z + j, mod_LionKing.prideWood, WOOD_METADATA, 2);
                FMLLog.fine("Placed prideWood at (%d, %d, %d), meta=%d", x + i + xOff1, y, z + j, WOOD_METADATA);
                world.setBlock(x + i + xOff2, y, z + j, mod_LionKing.prideWood, WOOD_METADATA, 2);
                FMLLog.fine("Placed prideWood at (%d, %d, %d), meta=%d", x + i + xOff2, y, z + j, WOOD_METADATA);
                world.setBlock(x + i, y, z + j + zOff1, mod_LionKing.prideWood, WOOD_METADATA, 2);
                FMLLog.fine("Placed prideWood at (%d, %d, %d), meta=%d", x + i, y, z + j + zOff1, WOOD_METADATA);
                world.setBlock(x + i, y, z + j + zOff2, mod_LionKing.prideWood, WOOD_METADATA, 2);
                FMLLog.fine("Placed prideWood at (%d, %d, %d), meta=%d", x + i, y, z + j + zOff2, WOOD_METADATA);
                world.setBlock(x + i, y, z + j, mod_LionKing.prideWood, WOOD_METADATA, 2);
                FMLLog.fine("Placed prideWood at (%d, %d, %d), meta=%d", x + i, y, z + j, WOOD_METADATA);
                blocksPlaced += 5;
            }
        }
        return blocksPlaced;
    }

    private int placeExtendedWood(World world, int x, int y, int z, int xOff1, int xOff2, int zOff1, int zOff2) {
        int blocksPlaced = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                world.setBlock(x + i + xOff1, y, z + j, mod_LionKing.prideWood, WOOD_METADATA, 2);
                FMLLog.fine("Placed prideWood at (%d, %d, %d), meta=%d", x + i + xOff1, y, z + j, WOOD_METADATA);
                world.setBlock(x + i + xOff2, y, z + j, mod_LionKing.prideWood, WOOD_METADATA, 2);
                FMLLog.fine("Placed prideWood at (%d, %d, %d), meta=%d", x + i + xOff2, y, z + j, WOOD_METADATA);
                world.setBlock(x + i, y, z + j + zOff1, mod_LionKing.prideWood, WOOD_METADATA, 2);
                FMLLog.fine("Placed prideWood at (%d, %d, %d), meta=%d", x + i, y, z + j + zOff1, WOOD_METADATA);
                world.setBlock(x + i, y, z + j + zOff2, mod_LionKing.prideWood, WOOD_METADATA, 2);
                FMLLog.fine("Placed prideWood at (%d, %d, %d), meta=%d", x + i, y, z + j + zOff2, WOOD_METADATA);
                world.setBlock(x + i, y, z + j, mod_LionKing.prideWood, WOOD_METADATA, 2);
                FMLLog.fine("Placed prideWood at (%d, %d, %d), meta=%d", x + i, y, z + j, WOOD_METADATA);
                blocksPlaced += 5;
            }
        }
        return blocksPlaced;
    }

    private int placeExtendedLeaves(World world, int x, int y, int z, int xOff, int xOff2, int zOff, int zOff2) {
        int blocksPlaced = 0;
        for (int dx = x - 2; dx <= x + 3; dx++) {
            world.setBlock(dx, y, z + xOff, mod_LionKing.forestLeaves, 0, 2);
            FMLLog.fine("Placed forestLeaves at (%d, %d, %d)", dx, y, z + xOff);
            world.setBlock(dx, y, z + xOff2, mod_LionKing.forestLeaves, 0, 2);
            FMLLog.fine("Placed forestLeaves at (%d, %d, %d)", dx, y, z + xOff2);
            blocksPlaced += 2;
        }
        for (int dz = z - 2; dz <= z + 3; dz++) {
            world.setBlock(x + zOff, y, dz, mod_LionKing.forestLeaves, 0, 2);
            FMLLog.fine("Placed forestLeaves at (%d, %d, %d)", x + zOff, y, dz);
            world.setBlock(x + zOff2, y, dz, mod_LionKing.forestLeaves, 0, 2);
            FMLLog.fine("Placed forestLeaves at (%d, %d, %d)", x + zOff2, y, dz);
            blocksPlaced += 2;
        }
        return blocksPlaced;
    }
}