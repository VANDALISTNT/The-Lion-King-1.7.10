package lionking.biome;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenWaterlily;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.FMLLog;
import lionking.entity.LKEntityGiraffe;
import lionking.world.LKWorldGenMangoTrees;
import java.util.Random;

public class LKBiomeGenMountains extends LKPrideLandsBiome {
    public LKBiomeGenMountains(int biomeId) {
        super(biomeId);
        setBiomeName("Mountains");

        topBlock = Blocks.stone;
        fillerBlock = Blocks.stone;
        setHeight(new Height(1.0F, 0.5F));
        setTemperatureRainfall(0.7F, 0.3F);

        if (lkDecorator != null) {
            lkDecorator.treesPerChunk = 0;
            lkDecorator.grassPerChunk = 1;
            lkDecorator.mangoPerChunk = 0;
            lkDecorator.whiteFlowersPerChunk = 0;
            lkDecorator.purpleFlowersPerChunk = 0;
            lkDecorator.blueFlowersPerChunk = 1;
            lkDecorator.logsPerChunk = 6;
            lkDecorator.maizePerChunk = 4;
            lkDecorator.zazuPerChunk = 20;
        } else {
            FMLLog.warning("Decorator is null for LKBiomeGenMountains with ID %d", biomeId);
        }

        spawnableCreatureList.add(new SpawnListEntry(LKEntityGiraffe.class, 3, 2, 4));

        FMLLog.info("Initialized LKBiomeGenMountains with ID %d", biomeId);
    }

    @Override
    public void decorate(World world, Random random, int chunkX, int chunkZ) {
        if (lkDecorator == null) {
            FMLLog.warning("Decorator is null for LKBiomeGenMountains at chunk x=%d, z=%d", chunkX, chunkZ);
            return;
        }

        if (random.nextInt(6) == 0) {
            lkDecorator.treesPerChunk++;
            if (random.nextInt(5) == 0) {
                int extraTrees = random.nextInt(2) + 1;
                lkDecorator.treesPerChunk += extraTrees;
                FMLLog.fine("Increased treesPerChunk by %d to %d for chunk x=%d, z=%d", extraTrees, lkDecorator.treesPerChunk, chunkX, chunkZ);
            }
        }

        super.decorate(world, random, chunkX, chunkZ);

        if (random.nextInt(20) == 0) {
            int x = chunkX + random.nextInt(16) + 8;
            int z = chunkZ + random.nextInt(16) + 8;
            int y = world.getHeightValue(x, z);
            WorldGenerator mangoGen = new LKWorldGenMangoTrees(false);
            if (mangoGen != null) {
                mangoGen.generate(world, random, x, y, z);
                FMLLog.fine("Generated mango tree at x=%d, y=%d, z=%d in LKBiomeGenMountains", x, y, z);
            } else {
                FMLLog.warning("Mango tree generator is null for LKBiomeGenMountains at x=%d, z=%d", x, z);
            }
        }

        for (int i = 0; i < 1; i++) {
            int x = chunkX + random.nextInt(16) + 8;
            int z = chunkZ + random.nextInt(16) + 8;
            int y = world.getTopSolidOrLiquidBlock(x, z);
            WorldGenerator lilyGen = new WorldGenWaterlily();
            if (lilyGen != null) {
                lilyGen.generate(world, random, x, y, z);
                FMLLog.fine("Generated waterlily at x=%d, y=%d, z=%d in LKBiomeGenMountains", x, y, z);
            }
        }

        lkDecorator.treesPerChunk = 0;
        lkDecorator.mangoPerChunk = 0;
        FMLLog.fine("Decorated chunk at x=%d, z=%d in LKBiomeGenMountains", chunkX, chunkZ);
    }

    @Override
    public WorldGenAbstractTree func_150567_a(Random random) {
        FMLLog.fine("Using vanilla tree generator for Mountains biome (func_150567_a)");
        return new WorldGenTrees(false, 4, 0, 0, false); 
    }

    @Override
    public WorldGenerator getRandomWorldGenForGrass(Random random) {
        WorldGenerator grassGen = new WorldGenTallGrass(Blocks.tallgrass, random.nextInt(5) == 0 ? 2 : 1);
        return grassGen;
    }
}