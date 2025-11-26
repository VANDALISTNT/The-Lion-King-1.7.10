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
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import lionking.entity.LKEntityLion;
import lionking.entity.LKEntityLioness;
import lionking.entity.LKEntityFlamingo;
import lionking.entity.LKEntityDikdik;
import lionking.world.LKWorldGenMangoTrees;
import java.util.Random;

public class LKBiomeGenUpendi extends LKPrideLandsBiome {
    public static BiomeGenBase upendi;

    public LKBiomeGenUpendi(int biomeId) {
        super(biomeId);
        setBiomeName("Upendi");

        topBlock = Blocks.grass;
        fillerBlock = Blocks.dirt;
        setHeight(new Height(0.1F, 0.3F));
        setTemperatureRainfall(1.0F, 1.0F);
        waterColorMultiplier = 0xFF1144;

        spawnableMonsterList.clear();
        spawnableWaterCreatureList.clear();
        spawnableCreatureList.clear();
        spawnableCaveCreatureList.clear();

        spawnableCreatureList.add(new SpawnListEntry(LKEntityLion.class, 4, 4, 4));
        spawnableCreatureList.add(new SpawnListEntry(LKEntityLioness.class, 4, 4, 4));
        spawnableCreatureList.add(new SpawnListEntry(LKEntityFlamingo.class, 3, 4, 4));
        spawnableCreatureList.add(new SpawnListEntry(LKEntityDikdik.class, 10, 4, 4));

        if (lkDecorator != null) {
            lkDecorator.treesPerChunk = 13;
            lkDecorator.mangoPerChunk = 3;
            lkDecorator.grassPerChunk = 20;
            lkDecorator.whiteFlowersPerChunk = 10;
            lkDecorator.purpleFlowersPerChunk = 8;
            lkDecorator.zazuPerChunk = 40;
            lkDecorator.maizePerChunk = 35;
        } else {
            FMLLog.warning("Decorator is null for LKBiomeGenUpendi with ID %d", biomeId);
        }

        FMLLog.info("Initialized LKBiomeGenUpendi with ID %d", biomeId);
    }

    public static void registerBiome(int biomeId) {
        if (upendi == null) {
            upendi = new LKBiomeGenUpendi(biomeId);
            BiomeDictionary.registerBiomeType(upendi, BiomeDictionary.Type.JUNGLE, BiomeDictionary.Type.LUSH);
            BiomeManager.addBiome(BiomeManager.BiomeType.WARM, new BiomeManager.BiomeEntry(upendi, 10));
            FMLLog.info("Registered Upendi biome with ID %d", biomeId);
        }
    }

    @Override
    public void decorate(World world, Random random, int chunkX, int chunkZ) {
        if (lkDecorator == null) {
            FMLLog.warning("Decorator is null for biome %s at chunk x=%d, z=%d", biomeName, chunkX, chunkZ);
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

        if (random.nextInt(10) == 0) {
            int x = chunkX + random.nextInt(16) + 8;
            int z = chunkZ + random.nextInt(16) + 8;
            int y = world.getHeightValue(x, z);
            WorldGenerator mangoGen = new LKWorldGenMangoTrees(false);
            if (mangoGen != null) {
                mangoGen.generate(world, random, x, y, z);
                FMLLog.fine("Generated mango tree at x=%d, y=%d, z=%d in LKBiomeGenUpendi", x, y, z);
            } else {
                FMLLog.warning("Mango tree generator is null for LKBiomeGenUpendi at x=%d, z=%d", x, z);
            }
        }

        for (int i = 0; i < 2; i++) {
            int x = chunkX + random.nextInt(16) + 8;
            int z = chunkZ + random.nextInt(16) + 8;
            int y = world.getTopSolidOrLiquidBlock(x, z);
            WorldGenerator lilyGen = new WorldGenWaterlily();
            if (lilyGen != null) {
                lilyGen.generate(world, random, x, y, z);
                FMLLog.fine("Generated waterlily at x=%d, y=%d, z=%d in LKBiomeGenUpendi", x, y, z);
            }
        }

        lkDecorator.treesPerChunk = 13;
        lkDecorator.mangoPerChunk = 3;
        FMLLog.fine("Decorated chunk at x=%d, z=%d in LKBiomeGenUpendi", chunkX, chunkZ);
    }

    @Override
    public WorldGenAbstractTree func_150567_a(Random random) {
        FMLLog.fine("Using vanilla tree generator for Upendi biome (func_150567_a)");
        return new WorldGenTrees(false, 5, 3, 3, true);
    }

    @Override
    public WorldGenerator getRandomWorldGenForGrass(Random random) {
        WorldGenerator grassGen = new WorldGenTallGrass(Blocks.tallgrass, random.nextInt(4) == 0 ? 2 : 1);
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

    @Override
    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float temperature) {
        return 0x8E84FF;
    }
}