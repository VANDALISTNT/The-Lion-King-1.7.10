package lionking.biome;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.FMLLog;
import lionking.entity.LKEntityDikdik;
import lionking.world.LKWorldGenMangoTrees;
import java.util.Random;

public class LKBiomeGenWoodedSavannah extends LKPrideLandsBiome {
    public LKBiomeGenWoodedSavannah(int biomeId) {
        super(biomeId);
        setBiomeName("Wooded Savannah");

        topBlock = Blocks.grass;
        fillerBlock = Blocks.dirt;

        if (lkDecorator != null) {
            lkDecorator.treesPerChunk = 2;
            lkDecorator.grassPerChunk = 10;
            lkDecorator.mangoPerChunk = 0;
            lkDecorator.whiteFlowersPerChunk = 2;
            lkDecorator.purpleFlowersPerChunk = 1;
            lkDecorator.blueFlowersPerChunk = 1;
            lkDecorator.logsPerChunk = 8;
            lkDecorator.maizePerChunk = 0;
            lkDecorator.zazuPerChunk = 50;
        } else {
            FMLLog.warning("Decorator is null for LKBiomeGenWoodedSavannah with ID %d", biomeId);
        }

        spawnableCreatureList.add(new SpawnListEntry(LKEntityDikdik.class, 8, 2, 4));

        FMLLog.info("Initialized LKBiomeGenWoodedSavannah with ID %d", biomeId);
    }

    @Override
    public void decorate(World world, Random random, int chunkX, int chunkZ) {
        if (lkDecorator == null) {
            FMLLog.warning("Decorator is null for LKBiomeGenWoodedSavannah at chunk x=%d, z=%d", chunkX, chunkZ);
            return;
        }

        if (random.nextInt(4) == 0) {
            lkDecorator.treesPerChunk++;
            if (random.nextInt(4) == 0) {
                int extraTrees = random.nextInt(2) + 1;
                lkDecorator.treesPerChunk += extraTrees;
                FMLLog.fine("Increased treesPerChunk by %d to %d for chunk x=%d, z=%d", extraTrees, lkDecorator.treesPerChunk, chunkX, chunkZ);
            }
        }

        if (random.nextInt(40) == 0) {
            lkDecorator.mangoPerChunk++;
            FMLLog.fine("Increased mangoPerChunk to %d for chunk x=%d, z=%d", lkDecorator.mangoPerChunk, chunkX, chunkZ);
        }

        super.decorate(world, random, chunkX, chunkZ);

        if (random.nextInt(10) == 0) {
            int x = chunkX + random.nextInt(16) + 8;
            int z = chunkZ + random.nextInt(16) + 8;
            int y = world.getHeightValue(x, z);
            WorldGenerator mangoGen = new LKWorldGenMangoTrees(false);
            if (mangoGen != null) {
                mangoGen.generate(world, random, x, y, z);
                FMLLog.fine("Generated mango tree at x=%d, y=%d, z=%d in LKBiomeGenWoodedSavannah", x, y, z);
            } else {
                FMLLog.warning("Mango tree generator is null for LKBiomeGenWoodedSavannah at x=%d, z=%d", x, z);
            }
        }

        lkDecorator.treesPerChunk = 2;
        lkDecorator.mangoPerChunk = 0;
        FMLLog.fine("Decorated chunk at x=%d, z=%d in LKBiomeGenWoodedSavannah", chunkX, chunkZ);
    }

    @Override
    public WorldGenAbstractTree func_150567_a(Random random) {
        FMLLog.fine("Using vanilla tree generator for Wooded Savannah biome (func_150567_a)");
        return new WorldGenTrees(false, 4, 0, 0, false);
    }

    @Override
    public WorldGenerator getRandomWorldGenForGrass(Random random) {
        WorldGenerator grassGen = new WorldGenTallGrass(Blocks.tallgrass, random.nextInt(5) == 0 ? 2 : 1);
        return grassGen;
    }
}