package lionking.world;

import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenerator;
import java.util.Random;
import cpw.mods.fml.common.FMLLog;
import lionking.mod_LionKing;

public class LKWorldGenRainforestTrees extends WorldGenerator {
    private boolean naturallyGenerated;

    public LKWorldGenRainforestTrees(boolean flag) {
        super(flag);
        naturallyGenerated = !flag;
        FMLLog.fine("Initialized LKWorldGenRainforestTrees with naturallyGenerated=%b", naturallyGenerated);
    }

    @Override
    public boolean generate(World world, Random random, int i, int j, int k) {
        FMLLog.fine("Attempting to generate rainforest tree at (%d, %d, %d)", i, j, k);

        if (mod_LionKing.prideWood == null || mod_LionKing.forestLeaves == null) {
            FMLLog.severe("Cannot generate rainforest tree at (%d, %d, %d): prideWood or forestLeaves is null", i, j, k);
            return false;
        }

        int l = random.nextInt(5) + 9;
        if (j < 1 || j + l + 2 > 256) {
            FMLLog.fine("Cannot generate rainforest tree at (%d, %d, %d): invalid height (height=%d)", i, j, k, l);
            return false;
        }

        if (!isBlockReplaceable(world, i, j, k)) {
            FMLLog.fine("Cannot generate rainforest tree at (%d, %d, %d): non-replaceable block at trunk base", i, j, k);
            return false;
        }

        Block groundBlock = world.getBlock(i, j - 1, k);
        if (groundBlock != Blocks.grass && groundBlock != Blocks.dirt) {
            FMLLog.fine("Cannot generate rainforest tree at (%d, %d, %d): invalid ground block %s", i, j, k, groundBlock != null ? groundBlock.getUnlocalizedName() : "null");
            return false;
        }

        boolean isLarge = (random.nextInt(5) == 0 && naturallyGenerated) || (random.nextInt(8) == 0 && !naturallyGenerated);
        boolean result = isLarge ? generateLargeTree(world, random, i, j, k, l + random.nextInt(7) + 3)
                                : generateNormalTree(world, random, i, j, k, l);

        if (result) {
            FMLLog.info("Successfully generated %s rainforest tree at (%d, %d, %d) with height=%d", isLarge ? "large" : "normal", i, j, k, l);
        } else {
            FMLLog.warning("Failed to generate %s rainforest tree at (%d, %d, %d)", isLarge ? "large" : "normal", i, j, k);
        }
        return result;
    }

    private boolean generateNormalTree(World world, Random random, int i, int j, int k, int l) {
        int l1 = l - 4;
        if (l > 11) l1--;
        if (l > 14) l1--;

        for (int j1 = 1; j1 <= l; j1++) {
            if (!isBlockReplaceable(world, i, j + j1, k)) {
                FMLLog.fine("Cannot generate normal rainforest tree at (%d, %d, %d): non-replaceable block at trunk position", i, j + j1, k);
                return false;
            }
        }

        for (int i1 = -1; i1 <= 1; i1++) {
            for (int k1 = -1; k1 <= 1; k1++) {
                if (!isBlockReplaceable(world, i + i1, j + l1, k + k1) ||
                    !isBlockReplaceable(world, i + i1, j + l, k + k1) ||
                    !isBlockReplaceable(world, i + i1, j + l + 2, k + k1)) {
                    FMLLog.fine("Cannot generate normal rainforest tree at (%d, %d, %d): non-replaceable block in foliage layer", i + i1, j + l, k + k1);
                    return false;
                }
            }
        }
        for (int i1 = -2; i1 <= 2; i1++) {
            for (int k1 = -2; k1 <= 2; k1++) {
                if (!isBlockReplaceable(world, i + i1, j + l + 1, k + k1)) {
                    FMLLog.fine("Cannot generate normal rainforest tree at (%d, %d, %d): non-replaceable block in foliage layer", i + i1, j + l + 1, k + k1);
                    return false;
                }
            }
        }

        generateFoliageLayer(world, random, i, j, k, l1, 1);
        generateFoliageLayer(world, random, i, j, k, l, 1);
        generateFoliageLayer(world, random, i, j, k, l + 1, 2);
        generateFoliageLayer(world, random, i, j, k, l + 2, 1);

        for (int j1 = 0; j1 <= l + 1; j1++) {
            world.setBlock(i, j + j1, k, mod_LionKing.prideWood, 1, 2);
            FMLLog.fine("Placed prideWood at (%d, %d, %d)", i, j + j1, k);
        }
        world.setBlock(i, j - 1, k, Blocks.dirt, 0, 2);
        FMLLog.fine("Set dirt base at (%d, %d, %d)", i, j - 1, k);
        return true;
    }

    private boolean generateLargeTree(World world, Random random, int i, int j, int k, int l) {
        int l1 = l - 4;
        if (l > 11) l1--;
        if (l > 14) l1--;
        if (l > 18) l1--;

        for (int j1 = 1; j1 <= l; j1++) {
            if (!isBlockReplaceable(world, i, j + j1, k)) {
                FMLLog.fine("Cannot generate large rainforest tree at (%d, %d, %d): non-replaceable block at trunk position", i, j + j1, k);
                return false;
            }
        }

        for (int i1 = -2; i1 <= 2; i1++) {
            for (int k1 = -2; k1 <= 2; k1++) {
                if (!(i1 == -2 && k1 == -2) && !(i1 == 2 && k1 == -2) &&
                    !(i1 == -2 && k1 == 2) && !(i1 == 2 && k1 == 2)) {
                    if (!isBlockReplaceable(world, i + i1, j + l1, k + k1)) {
                        FMLLog.fine("Cannot generate large rainforest tree at (%d, %d, %d): non-replaceable block in foliage layer", i + i1, j + l1, k + k1);
                        return false;
                    }
                }
            }
        }
        for (int i1 = -1; i1 <= 1; i1++) {
            for (int k1 = -1; k1 <= 1; k1++) {
                if (!isBlockReplaceable(world, i + i1, j + l, k + k1)) {
                    FMLLog.fine("Cannot generate large rainforest tree at (%d, %d, %d): non-replaceable block in foliage layer", i + i1, j + l, k + k1);
                    return false;
                }
            }
        }
        for (int i1 = -3; i1 <= 3; i1++) {
            for (int k1 = -3; k1 <= 3; k1++) {
                if (!isBlockReplaceable(world, i + i1, j + l + 1, k + k1)) {
                    FMLLog.fine("Cannot generate large rainforest tree at (%d, %d, %d): non-replaceable block in foliage layer", i + i1, j + l + 1, k + k1);
                    return false;
                }
            }
        }
        for (int i1 = -2; i1 <= 2; i1++) {
            for (int k1 = -2; k1 <= 2; k1++) {
                if (!isBlockReplaceable(world, i + i1, j + l + 2, k + k1)) {
                    FMLLog.fine("Cannot generate large rainforest tree at (%d, %d, %d): non-replaceable block in foliage layer", i + i1, j + l + 2, k + k1);
                    return false;
                }
            }
        }

        for (int j1 = 0; j1 < 3; j1++) {
            int i1 = j1 == 2 ? 2 : 1;
            int j2 = j1 == 0 ? l1 - 1 : (j1 == 1 ? l - 1 : l);
            if (!isBlockReplaceable(world, i - i1, j + j2, k) ||
                !isBlockReplaceable(world, i + i1, j + j2, k) ||
                !isBlockReplaceable(world, i, j + j2, k - i1) ||
                !isBlockReplaceable(world, i, j + j2, k + i1)) {
                FMLLog.fine("Cannot generate large rainforest tree at (%d, %d, %d): non-replaceable block at branch position", i, j + j2, k);
                return false;
            }
        }

        generateFoliageLayer(world, random, i, j, k, l1, 2, true);
        generateFoliageLayer(world, random, i, j, k, l, 1);
        generateFoliageLayer(world, random, i, j, k, l + 1, 3);
        generateFoliageLayer(world, random, i, j, k, l + 2, 2);

        for (int j1 = 0; j1 < 3; j1++) {
            int i1 = j1 == 2 ? 2 : 1;
            int j2 = j1 == 0 ? l1 - 1 : (j1 == 1 ? l - 1 : l);
            world.setBlock(i - i1, j + j2, k, mod_LionKing.prideWood, 1, 2);
            world.setBlock(i + i1, j + j2, k, mod_LionKing.prideWood, 1, 2);
            world.setBlock(i, j + j2, k - i1, mod_LionKing.prideWood, 1, 2);
            world.setBlock(i, j + j2, k + i1, mod_LionKing.prideWood, 1, 2);
            FMLLog.fine("Placed branch at (%d, %d, %d)", i - i1, j + j2, k);
        }

        for (int j1 = 0; j1 <= l + 1; j1++) {
            world.setBlock(i, j + j1, k, mod_LionKing.prideWood, 1, 2);
            FMLLog.fine("Placed prideWood at (%d, %d, %d)", i, j + j1, k);
        }
        world.setBlock(i, j - 1, k, Blocks.dirt, 0, 2);
        FMLLog.fine("Set dirt base at (%d, %d, %d)", i, j - 1, k);
        return true;
    }

    private void generateFoliageLayer(World world, Random random, int i, int j, int k, int height, int radius) {
        generateFoliageLayer(world, random, i, j, k, height, radius, false);
    }

    private void generateFoliageLayer(World world, Random random, int i, int j, int k, int height, int radius, boolean skipCorners) {
        for (int i1 = -radius; i1 <= radius; i1++) {
            for (int k1 = -radius; k1 <= radius; k1++) {
                if (skipCorners && (Math.abs(i1) == radius && Math.abs(k1) == radius)) continue;
                world.setBlock(i + i1, j + height, k + k1, mod_LionKing.forestLeaves, 0, 2);
                FMLLog.fine("Placed forestLeaves at (%d, %d, %d)", i + i1, j + height, k + k1);
                generateVines(world, random, i + i1, j + height, k + k1);
            }
        }
    }

    private void generateVines(World world, Random random, int i, int j, int k) {
        if (random.nextInt(9) == 0 && world.isAirBlock(i - 1, j, k)) {
            placeVines(world, random, i - 1, j, k, 8);
            FMLLog.fine("Placed vines at (%d, %d, %d), meta=8", i - 1, j, k);
        }
        if (random.nextInt(9) == 0 && world.isAirBlock(i + 1, j, k)) {
            placeVines(world, random, i + 1, j, k, 2);
            FMLLog.fine("Placed vines at (%d, %d, %d), meta=2", i + 1, j, k);
        }
        if (random.nextInt(9) == 0 && world.isAirBlock(i, j, k - 1)) {
            placeVines(world, random, i, j, k - 1, 1);
            FMLLog.fine("Placed vines at (%d, %d, %d), meta=1", i, j, k - 1);
        }
        if (random.nextInt(9) == 0 && world.isAirBlock(i, j, k + 1)) {
            placeVines(world, random, i, j, k + 1, 4);
            FMLLog.fine("Placed vines at (%d, %d, %d), meta=4", i, j, k + 1);
        }
    }

    private void placeVines(World world, Random random, int i, int j, int k, int meta) {
        world.setBlock(i, j, k, Blocks.vine, meta, 3);
        int l = random.nextInt(2) + 6;
        while (l > 0) {
            j--;
            if (!world.isAirBlock(i, j, k)) {
                return;
            }
            world.setBlock(i, j, k, Blocks.vine, meta, 3);
            FMLLog.fine("Extended vine to (%d, %d, %d), meta=%d", i, j, k, meta);
            l--;
        }
    }

    private boolean isBlockReplaceable(World world, int i, int j, int k) {
        Block block = world.getBlock(i, j, k);
        return block == Blocks.air || block == Blocks.vine || block == mod_LionKing.forestLeaves ||
               block == mod_LionKing.forestSapling || block.isLeaves(world, i, j, k) ||
               block == Blocks.tallgrass || block == Blocks.deadbush;
    }
}