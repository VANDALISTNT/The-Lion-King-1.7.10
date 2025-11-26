package lionking.biome;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenSand;
import net.minecraft.world.gen.feature.WorldGenWaterlily;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.FMLLog;
import lionking.entity.LKEntityCrocodile;
import lionking.world.LKWorldGenMangoTrees;
import java.util.Random;

public class LKBiomeGenRiver extends LKPrideLandsBiome {
    public LKBiomeGenRiver(int biomeId) {
        super(biomeId);
        setBiomeName("River");

        topBlock = Blocks.grass;
        fillerBlock = Blocks.dirt;
        setHeight(new Height(-0.5F, 0.0F));
        setTemperatureRainfall(0.8F, 0.8F);
        waterColorMultiplier = 0x3F76E4;

        spawnableCreatureList.clear();
        spawnableCaveCreatureList.clear();
        spawnableCreatureList.add(new SpawnListEntry(LKEntityCrocodile.class, 3, 4, 4));

        if (lkDecorator != null) {
            lkDecorator.treesPerChunk = 0;
            lkDecorator.mangoPerChunk = 0;
            lkDecorator.grassPerChunk = 3;
            lkDecorator.whiteFlowersPerChunk = 5;
            lkDecorator.purpleFlowersPerChunk = 0;
            lkDecorator.blueFlowersPerChunk = 0;
            lkDecorator.logsPerChunk = 0;
            lkDecorator.maizePerChunk = 10;
            lkDecorator.zazuPerChunk = 10;
        } else {
            FMLLog.warning("Decorator is null for LKBiomeGenRiver with ID %d", biomeId);
        }

        FMLLog.info("Initialized LKBiomeGenRiver with ID %d", biomeId);
    }

    @Override
    public void decorate(World world, Random random, int chunkX, int chunkZ) {
        if (lkDecorator == null) {
            FMLLog.warning("Decorator is null for LKBiomeGenRiver at chunk x=%d, z=%d", chunkX, chunkZ);
            return;
        }

        if (random.nextInt(10) == 0) {
            int extraTrees = 1 + random.nextInt(2);
            lkDecorator.treesPerChunk += extraTrees;
            FMLLog.fine("Increased treesPerChunk by %d to %d for chunk x=%d, z=%d", extraTrees, lkDecorator.treesPerChunk, chunkX, chunkZ);
        }

        if (random.nextInt(20) == 0) {
            lkDecorator.mangoPerChunk++;
            FMLLog.fine("Increased mangoPerChunk to %d for chunk x=%d, z=%d", lkDecorator.mangoPerChunk, chunkX, chunkZ);
        }

        super.decorate(world, random, chunkX, chunkZ);

        for (int i = 0; i < 3; i++) {
            int x = chunkX + random.nextInt(16) + 8;
            int z = chunkZ + random.nextInt(16) + 8;
            int y = world.getTopSolidOrLiquidBlock(x, z);
            WorldGenerator sandGen = new WorldGenSand(Blocks.sand, 7);
            sandGen.generate(world, random, x, y, z);
            FMLLog.fine("Generated sand patch at x=%d, y=%d, z=%d in LKBiomeGenRiver", x, y, z);
        }

        for (int i = 0; i < 2; i++) {
            int x = chunkX + random.nextInt(16) + 8;
            int z = chunkZ + random.nextInt(16) + 8;
            int y = world.getTopSolidOrLiquidBlock(x, z);
            WorldGenerator lilyGen = new WorldGenWaterlily();
            if (lilyGen != null) {
                lilyGen.generate(world, random, x, y, z);
                FMLLog.fine("Generated waterlily at x=%d, y=%d, z=%d in LKBiomeGenRiver", x, y, z);
            }
        }

        if (random.nextInt(10) == 0) {
            int x = chunkX + random.nextInt(16) + 8;
            int z = chunkZ + random.nextInt(16) + 8;
            int y = world.getHeightValue(x, z);
            WorldGenerator mangoGen = new LKWorldGenMangoTrees(false);
            if (mangoGen != null) {
                mangoGen.generate(world, random, x, y, z);
                FMLLog.fine("Generated mango tree at x=%d, y=%d, z=%d in LKBiomeGenRiver", x, y, z);
            } else {
                FMLLog.warning("Mango tree generator is null for LKBiomeGenRiver at x=%d, z=%d", x, z);
            }
        }

        lkDecorator.treesPerChunk = 0;
        lkDecorator.mangoPerChunk = 0;
        FMLLog.fine("Decorated chunk at x=%d, z=%d in LKBiomeGenRiver", chunkX, chunkZ);
    }

    @Override
    public WorldGenAbstractTree func_150567_a(Random random) {
        FMLLog.fine("Using vanilla tree generator for River biome (func_150567_a)");
        return new WorldGenTrees(false, 4, 0, 0, false);
    }

    @Override
    public WorldGenerator getRandomWorldGenForGrass(Random random) {
        WorldGenerator grassGen = new WorldGenTallGrass(Blocks.tallgrass, random.nextInt(5) == 0 ? 2 : 1);
        return grassGen;
    }
}