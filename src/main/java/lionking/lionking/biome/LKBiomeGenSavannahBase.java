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
import lionking.entity.LKEntityGiraffe;
import lionking.entity.LKEntityDikdik;
import lionking.world.LKWorldGenYams;

public class LKBiomeGenSavannahBase extends LKPrideLandsBiome {
    public LKBiomeGenSavannahBase(int biomeId) {
        super(biomeId);
        setBiomeName("Savannah Base");

        if (lkDecorator != null) {
            lkDecorator.treesPerChunk = 0;
            lkDecorator.mangoPerChunk = 0;
            lkDecorator.grassPerChunk = 13;
            lkDecorator.whiteFlowersPerChunk = 4;
            lkDecorator.purpleFlowersPerChunk = 0;
            lkDecorator.blueFlowersPerChunk = 0;
            lkDecorator.logsPerChunk = 3;
            lkDecorator.maizePerChunk = 15;
            lkDecorator.zazuPerChunk = 150;
        } else {
            FMLLog.warning("Decorator is null for LKBiomeGenSavannahBase with ID %d", biomeId);
        }

        spawnableCreatureList.add(new SpawnListEntry(LKEntityGiraffe.class, 2, 2, 4));
        spawnableCaveCreatureList.add(new SpawnListEntry(LKEntityDikdik.class, 10, 4, 4));

        FMLLog.info("Initialized LKBiomeGenSavannahBase with ID %d", biomeId);
    }

    @Override
    public void decorate(World world, Random random, int chunkX, int chunkZ) {
        if (lkDecorator == null) {
            FMLLog.warning("Decorator is null for LKBiomeGenSavannahBase at chunk x=%d, z=%d", chunkX, chunkZ);
            return;
        }

        if (random.nextInt(5) == 0) {
            lkDecorator.purpleFlowersPerChunk++;
            FMLLog.fine("Increased purpleFlowersPerChunk to %d for chunk x=%d, z=%d", lkDecorator.purpleFlowersPerChunk, chunkX, chunkZ);
        }

        super.decorate(world, random, chunkX, chunkZ);
        lkDecorator.purpleFlowersPerChunk = 0;

        if (random.nextInt(6) == 0) {
            int x = chunkX + random.nextInt(16) + 8;
            int y = random.nextInt(128);
            int z = chunkZ + random.nextInt(16) + 8;
            WorldGenerator yamGen = new LKWorldGenYams();
            if (yamGen.generate(world, random, x, y, z)) {
                FMLLog.fine("Generated yams at x=%d, y=%d, z=%d in LKBiomeGenSavannahBase", x, y, z);
            } else {
                FMLLog.warning("Failed to generate yams at x=%d, y=%d, z=%d in LKBiomeGenSavannahBase", x, y, z);
            }
        }

        FMLLog.fine("Decorated chunk at x=%d, z=%d in LKBiomeGenSavannahBase", chunkX, chunkZ);
    }

    @Override
    public WorldGenAbstractTree func_150567_a(Random random) {
        FMLLog.fine("Using default tree generator for Savannah Base biome (func_150567_a)");
        return new WorldGenTrees(false, 4, 0, 0, false);
    }

    @Override
    public WorldGenerator getRandomWorldGenForGrass(Random random) {
        WorldGenerator grassGen = new WorldGenTallGrass(Blocks.tallgrass, 1);
        if (grassGen == null) {
            FMLLog.warning("Grass generator for LKBiomeGenSavannahBase returned null");
        }
        return grassGen;
    }
}
