package lionking.biome;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.init.Blocks;
import java.util.Random;
import cpw.mods.fml.common.FMLLog;

public class LKBiomeGenGrasslandSavannah extends LKPrideLandsBiome {
    public LKBiomeGenGrasslandSavannah(int biomeId) {
        super(biomeId);
        setBiomeName("Grassland Savannah");

        this.topBlock = Blocks.grass;
        this.fillerBlock = Blocks.dirt;

        if (lkDecorator != null) {
            lkDecorator.treesPerChunk = 2;
            lkDecorator.grassPerChunk = 20;
            lkDecorator.whiteFlowersPerChunk = 4;
            lkDecorator.purpleFlowersPerChunk = 2;
            lkDecorator.blueFlowersPerChunk = 2;
            lkDecorator.logsPerChunk = 1;
            lkDecorator.maizePerChunk = 3;
            lkDecorator.zazuPerChunk = 10;
        } else {
            FMLLog.warning("Decorator is null for LKBiomeGenGrasslandSavannah with ID %d", biomeId);
        }

        FMLLog.info("Initialized LKBiomeGenGrasslandSavannah with ID %d", biomeId);
    }

    @Override
    public void decorate(World world, Random random, int chunkX, int chunkZ) {
        if (lkDecorator == null) {
            FMLLog.warning("Decorator is null for LKBiomeGenGrasslandSavannah at chunk x=%d, z=%d", chunkX, chunkZ);
            return;
        }

        super.decorate(world, random, chunkX, chunkZ);
        FMLLog.fine("Decorated chunk at x=%d, z=%d in LKBiomeGenGrasslandSavannah", chunkX, chunkZ);
    }

    @Override
    public WorldGenAbstractTree func_150567_a(Random random) {
        FMLLog.fine("Using default tree generator for Grassland Savannah biome (func_150567_a)");
        return new WorldGenTrees(false, 4, 0, 0, false);
    }

    @Override
    public WorldGenerator getRandomWorldGenForGrass(Random random) {
        WorldGenerator grassGen = new WorldGenTallGrass(Blocks.tallgrass, 1);
        if (grassGen == null) {
            FMLLog.warning("Grass generator for LKBiomeGenGrasslandSavannah returned null");
        }
        return grassGen;
    }
}
