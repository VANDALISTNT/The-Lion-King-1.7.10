package lionking.biome;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.init.Blocks;
import java.util.Random;
import cpw.mods.fml.common.FMLLog;

public class LKBiomeGenBananaForest extends LKPrideLandsBiome {
    public LKBiomeGenBananaForest(int biomeId) {
        super(biomeId);
        setBiomeName("Banana Forest");

        if (lkDecorator != null) {
            lkDecorator.treesPerChunk = 7;
            lkDecorator.mangoPerChunk = 0;
            lkDecorator.grassPerChunk = 5;
            lkDecorator.whiteFlowersPerChunk = 6;
            lkDecorator.purpleFlowersPerChunk = 6;
            lkDecorator.blueFlowersPerChunk = 0;
            lkDecorator.logsPerChunk = 10;
            lkDecorator.maizePerChunk = 20;
            lkDecorator.zazuPerChunk = 20;
        } else {
            FMLLog.warning("Decorator is null for LKBiomeGenBananaForest with ID %d", biomeId);
        }

        FMLLog.info("Initialized LKBiomeGenBananaForest with ID %d", biomeId);
    }

    @Override
    public void decorate(World world, Random random, int chunkX, int chunkZ) {
        if (lkDecorator == null) {
            FMLLog.warning("Decorator is null for LKBiomeGenBananaForest at chunk x=%d, z=%d", chunkX, chunkZ);
            return;
        }

        super.decorate(world, random, chunkX, chunkZ);
        FMLLog.fine("Decorated chunk at x=%d, z=%d in LKBiomeGenBananaForest", chunkX, chunkZ);
    }

    @Override
    public WorldGenAbstractTree func_150567_a(Random random) {
        FMLLog.fine("Using default tree generator for Banana Forest biome (func_150567_a)");
        return new WorldGenTrees(false, 4, 0, 0, false);
    }

    @Override
    public WorldGenerator getRandomWorldGenForGrass(Random random) {
        WorldGenerator grassGen = new WorldGenTallGrass(Blocks.tallgrass, 1);
        if (grassGen == null) {
            FMLLog.warning("Grass generator for LKBiomeGenBananaForest returned null");
        }
        return grassGen;
    }
}