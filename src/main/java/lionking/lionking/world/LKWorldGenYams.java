package lionking.world;

import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.biome.BiomeGenBase;
import java.util.Random;
import cpw.mods.fml.common.FMLLog;

import lionking.mod_LionKing;

public class LKWorldGenYams extends WorldGenerator {
    @Override
    public boolean generate(World world, Random random, int i, int j, int k) {
        FMLLog.fine("Attempting to generate yams at (%d, %d, %d)", i, j, k);

        BiomeGenBase biome = world.getBiomeGenForCoords(i, k);
        if (biome != BiomeGenBase.savanna && biome != BiomeGenBase.savannaPlateau) {
            FMLLog.fine("Yam generation cancelled: unsuitable biome %s", biome.biomeName);
            return false;
        }

        int successfulPlacements = 0;
        int maxPlacements = 10;
        for (int l = 0; l < 64 && successfulPlacements < maxPlacements; l++) {
            int i1 = i + random.nextInt(8) - random.nextInt(8);
            int j1 = j + random.nextInt(4) - random.nextInt(4);
            int k1 = k + random.nextInt(8) - random.nextInt(8);
            if (j1 < 0 || j1 + 1 >= 256) {
                FMLLog.fine("Skipping placement at (%d, %d, %d): invalid height", i1, j1, k1);
                continue;
            }

            Block block = world.getBlock(i1, j1, k1);
            if (block == Blocks.grass && 
                world.isAirBlock(i1, j1 + 1, k1) && 
                (world.getFullBlockLightValue(i1, j1 + 1, k1) >= 8 || world.canBlockSeeTheSky(i1, j1 + 1, k1))) {
                world.setBlock(i1, j1 + 1, k1, mod_LionKing.yamCrops, 8, 2);
                successfulPlacements++;
                FMLLog.fine("Yam placed at (%d, %d, %d)", i1, j1 + 1, k1);
            } else {
                FMLLog.fine("Placement failed at (%d, %d, %d): block=%s, air above=%b, light=%d", 
                    i1, j1, k1, block.getUnlocalizedName(), 
                    world.isAirBlock(i1, j1 + 1, k1), 
                    world.getFullBlockLightValue(i1, j1 + 1, k1));
            }
        }

        if (successfulPlacements > 0) {
            FMLLog.fine("Successfully placed %d yams around (%d, %d, %d)", successfulPlacements, i, j, k);
            return true;
        } else {
            FMLLog.warning("No yams placed around (%d, %d, %d)", i, j, k);
            return false;
        }
    }
}