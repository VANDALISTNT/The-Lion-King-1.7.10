package lionking.biome;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenWaterlily;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.entity.LKEntityCrocodile;
import lionking.world.LKWorldGenLily;
import lionking.world.LKWorldGenMangoTrees;
import java.util.Random;

public class LKBiomeGenRainforest extends LKPrideLandsBiome {
    public LKBiomeGenRainforest(int biomeId) {
        super(biomeId);
        setBiomeName("Rainforest");

        topBlock = Blocks.grass;
        fillerBlock = Blocks.dirt;
        setHeight(new Height(0.2F, 0.3F));
        setTemperatureRainfall(0.9F, 0.9F);

        if (lkDecorator != null) {
            lkDecorator.treesPerChunk = 15;
            lkDecorator.mangoPerChunk = 2;
            lkDecorator.grassPerChunk = 20;
            lkDecorator.whiteFlowersPerChunk = 10;
            lkDecorator.purpleFlowersPerChunk = 5;
            lkDecorator.blueFlowersPerChunk = 0;
            lkDecorator.logsPerChunk = 40;
            lkDecorator.maizePerChunk = 20;
            lkDecorator.zazuPerChunk = 40;
        } else {
            FMLLog.warning("Decorator is null for LKBiomeGenRainforest with ID %d", biomeId);
        }

        waterColorMultiplier = 0x9BFF8C;
        spawnableCreatureList.add(new SpawnListEntry(LKEntityCrocodile.class, 3, 4, 4));

        FMLLog.info("Initialized LKBiomeGenRainforest with ID %d", biomeId);
    }

    @Override
    public void decorate(World world, Random random, int chunkX, int chunkZ) {
        if (lkDecorator == null) {
            FMLLog.warning("Decorator is null for LKBiomeGenRainforest at chunk x=%d, z=%d", chunkX, chunkZ);
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

        if (random.nextInt(20) == 0) {
            lkDecorator.mangoPerChunk++;
            FMLLog.fine("Increased mangoPerChunk to %d for chunk x=%d, z=%d", lkDecorator.mangoPerChunk, chunkX, chunkZ);
        }

        super.decorate(world, random, chunkX, chunkZ);

        for (int i = 0; i < 3; i++) {
            int x = chunkX + random.nextInt(16) + 8;
            int z = chunkZ + random.nextInt(16) + 8;
            int y = world.getTopSolidOrLiquidBlock(x, z);
            WorldGenerator lilyGen = random.nextInt(3) == 0 ? new LKWorldGenLily() : new WorldGenWaterlily();
            if (lilyGen != null) {
                lilyGen.generate(world, random, x, y, z);
                FMLLog.fine("Generated %s at x=%d, y=%d, z=%d in LKBiomeGenRainforest",
                    lilyGen instanceof LKWorldGenLily ? "lily" : "waterlily", x, y, z);
            }
        }

        if (random.nextInt(10) == 0) {
            int x = chunkX + random.nextInt(16) + 8;
            int z = chunkZ + random.nextInt(16) + 8;
            int y = world.getHeightValue(x, z);
            WorldGenerator mangoGen = new LKWorldGenMangoTrees(false);
            if (mangoGen != null) {
                mangoGen.generate(world, random, x, y, z);
                FMLLog.fine("Generated mango tree at x=%d, y=%d, z=%d in LKBiomeGenRainforest", x, y, z);
            } else {
                FMLLog.warning("Mango tree generator is null for LKBiomeGenRainforest at x=%d, z=%d", x, z);
            }
        }

        lkDecorator.treesPerChunk = 15;
        lkDecorator.mangoPerChunk = 2;
        FMLLog.fine("Decorated chunk at x=%d, z=%d in LKBiomeGenRainforest", chunkX, chunkZ);
    }

    @Override
    public WorldGenAbstractTree func_150567_a(Random random) {
        FMLLog.fine("Using vanilla tree generator for Rainforest biome (func_150567_a)");
        return new WorldGenTrees(false, 5, 3, 3, true);
    }

    @Override
    public WorldGenerator getRandomWorldGenForGrass(Random random) {
        WorldGenerator grassGen = new WorldGenTallGrass(Blocks.tallgrass, random.nextInt(5) == 0 ? 2 : 1);
        return grassGen;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getModdedBiomeFoliageColor(int original) {
        return 0x097705;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getModdedBiomeGrassColor(int original) {
        return 0x18CE21;
    }
}