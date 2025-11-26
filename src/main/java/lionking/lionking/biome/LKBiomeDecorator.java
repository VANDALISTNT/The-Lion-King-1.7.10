package lionking.biome;

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import java.util.Random;
import cpw.mods.fml.common.FMLLog;
import lionking.mod_LionKing;
import lionking.world.*;

public class LKBiomeDecorator {
    private World world;
    private Random random;
    private int chunkX;
    private int chunkZ;
    private final LKPrideLandsBiome biome;

    private final WorldGenerator dirtGen = new LKWorldGenMinable(Block.getIdFromBlock(Blocks.dirt), 32);
    private final WorldGenerator coalGen = new LKWorldGenMinable(Block.getIdFromBlock(mod_LionKing.prideCoal), 16);
    private final WorldGenerator silverGen = new LKWorldGenMinable(Block.getIdFromBlock(mod_LionKing.oreSilver), 6);
    private final WorldGenerator peacockGen = new LKWorldGenMinable(Block.getIdFromBlock(mod_LionKing.orePeacock), 6);
    private final WorldGenerator mangoGen = new LKWorldGenMangoTrees(false);
    private final WorldGenerator whiteFlowerGen = new WorldGenFlowers(mod_LionKing.whiteFlower); 
    private final WorldGenerator purpleFlowerGen = new LKWorldGenTallFlowers(false, 0);
    private final WorldGenerator blueFlowerGen = new WorldGenFlowers(mod_LionKing.blueFlower); 
    private final WorldGenerator logGen = new LKWorldGenLogs();
    private final WorldGenerator zazuGen = new LKWorldGenZazu();
    private final WorldGenerator maizeGen = new LKWorldGenMaize();

    public int treesPerChunk;
    public int mangoPerChunk;
    public int grassPerChunk;
    public int whiteFlowersPerChunk;
    public int purpleFlowersPerChunk;
    public int blueFlowersPerChunk;
    public int logsPerChunk;
    public int maizePerChunk;
    public int zazuPerChunk;

    public LKBiomeDecorator(LKPrideLandsBiome LKPrideLandsBiome) {
        this.biome = LKPrideLandsBiome;
    }

    public void decorate(World world, Random random, int chunkX, int chunkZ) {
        this.world = world;
        this.random = random;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        decorateChunk();
    }

    private void decorateChunk() {
        generateOres();

        generateFeature(treesPerChunk, () -> {
            int x = chunkX + random.nextInt(16) + 8;
            int z = chunkZ + random.nextInt(16) + 8;
            int y = world.getHeightValue(x, z);
            WorldGenerator treeGen = LKTreeGenerator.getUpendiTreeGenerator(random); 
            if (treeGen.generate(world, random, x, y, z)) {
                FMLLog.fine("Successfully generated tree at (%d, %d, %d)", x, y, z);
            } else {
                FMLLog.warning("Failed to generate tree at (%d, %d, %d)", x, y, z);
            }
        });

        generateFeature(mangoPerChunk, () -> {
            int x = chunkX + random.nextInt(16) + 8;
            int z = chunkZ + random.nextInt(16) + 8;
            int y = world.getHeightValue(x, z);
            if (mangoGen.generate(world, random, x, y, z)) {
                FMLLog.fine("Successfully generated mango tree at (%d, %d, %d)", x, y, z);
            } else {
                FMLLog.warning("Failed to generate mango tree at (%d, %d, %d)", x, y, z);
            }
        });

        generateFeature(grassPerChunk, () -> {
            int x = chunkX + random.nextInt(16) + 8;
            int y = random.nextInt(128);
            int z = chunkZ + random.nextInt(16) + 8;
            if (biome.getRandomWorldGenForGrass(random).generate(world, random, x, y, z)) {
                FMLLog.fine("Successfully generated grass at (%d, %d, %d)", x, y, z);
            } else {
                FMLLog.warning("Failed to generate grass at (%d, %d, %d)", x, y, z);
            }
        });

        generateFeature(whiteFlowersPerChunk, () -> generateAtRandomHeight(whiteFlowerGen));
        generateFeature(purpleFlowersPerChunk, () -> generateAtRandomHeight(purpleFlowerGen));
        generateFeature(blueFlowersPerChunk, () -> generateAtRandomHeight(blueFlowerGen));
        generateFeature(logsPerChunk, () -> generateAtRandomHeight(logGen));
        generateFeature(maizePerChunk, () -> generateAtRandomHeight(maizeGen));
        generateFeature(zazuPerChunk, () -> {
            int x = chunkX + random.nextInt(16) + 8;
            int y = random.nextInt(70) + 64;
            int z = chunkZ + random.nextInt(16) + 8;
            if (zazuGen.generate(world, random, x, y, z)) {
                FMLLog.fine("Successfully generated Zazu at (%d, %d, %d)", x, y, z);
            } else {
                FMLLog.warning("Failed to generate Zazu at (%d, %d, %d)", x, y, z);
            }
        });

        generateFeature(50, () -> {
            int x = chunkX + random.nextInt(16) + 8;
            int y = random.nextInt(random.nextInt(120) + 8);
            int z = chunkZ + random.nextInt(16) + 8;
            if (new LKWorldGenLiquids(Block.getIdFromBlock(Blocks.flowing_water)).generate(world, random, x, y, z)) {
                FMLLog.fine("Successfully generated water at (%d, %d, %d)", x, y, z);
            } else {
                FMLLog.warning("Failed to generate water at (%d, %d, %d)", x, y, z);
            }
        });

        generateFeature(18, () -> {
            int x = chunkX + random.nextInt(16) + 8;
            int y = random.nextInt(64) + 16;
            int z = chunkZ + random.nextInt(16) + 8;
            if (new LKWorldGenLiquids(Block.getIdFromBlock(Blocks.flowing_lava)).generate(world, random, x, y, z)) {
                FMLLog.fine("Successfully generated lava at (%d, %d, %d)", x, y, z);
            } else {
                FMLLog.warning("Failed to generate lava at (%d, %d, %d)", x, y, z);
            }
        });
    }

    private void generateFeature(int count, Runnable generator) {
        for (int i = 0; i < count; i++) {
            generator.run();
        }
    }

    private void generateAtRandomHeight(WorldGenerator generator) {
        int x = chunkX + random.nextInt(16) + 8;
        int y = random.nextInt(128);
        int z = chunkZ + random.nextInt(16) + 8;
        if (generator.generate(world, random, x, y, z)) {
            FMLLog.fine("Successfully generated feature at (%d, %d, %d)", x, y, z);
        } else {
            FMLLog.warning("Failed to generate feature at (%d, %d, %d)", x, y, z);
        }
    }

    private void generateOres() {
        generateOre(20, dirtGen, 0, 128);
        generateOre(20, coalGen, 0, 128);
        generateOre(8, silverGen, 0, 36);
        generateOre(1, peacockGen, 0, 18);
    }

    private void generateOre(int frequency, WorldGenerator generator, int minHeight, int maxHeight) {
        for (int i = 0; i < frequency; i++) {
            int x = chunkX + random.nextInt(16);
            int y = random.nextInt(maxHeight - minHeight) + minHeight;
            int z = chunkZ + random.nextInt(16);
            if (generator.generate(world, random, x, y, z)) {
                FMLLog.fine("Successfully generated ore at (%d, %d, %d)", x, y, z);
            } else {
                FMLLog.warning("Failed to generate ore at (%d, %d, %d)", x, y, z);
            }
        }
    }
}