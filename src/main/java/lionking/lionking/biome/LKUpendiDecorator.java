package lionking.biome;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenWaterlily;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.FMLLog;
import lionking.world.LKWorldGenMinable;
import lionking.world.LKWorldGenPassionTrees;
import lionking.world.LKWorldGenMangoTrees;
import lionking.world.LKWorldGenTallFlowers;
import lionking.world.LKWorldGenZazu;
import lionking.world.LKWorldGenMaize;
import lionking.world.LKWorldGenLiquids;
import lionking.world.LKWorldGenLily;
import java.util.Random;

public class LKUpendiDecorator {
    private static final int SEA_LEVEL = 63; 
    private World currentWorld;
    private Random randomGenerator;
    private int chunkX;
    private int chunkZ;
    private final LKBiomeGenUpendi biome;
    private final WorldGenerator dirtGen;
    private final WorldGenerator passionGen;
    private final WorldGenerator mangoGen;
    private final WorldGenerator whiteFlowerGen;
    private final WorldGenerator purpleFlowerGen;
    private final WorldGenerator redFlowerGen;
    private final WorldGenerator zazuGen;
    private final WorldGenerator maizeGen;
    public int treesPerChunk;
    public int mangoPerChunk;
    public int grassPerChunk;
    public int whiteFlowersPerChunk;
    public int purpleFlowersPerChunk;
    public int redFlowersPerChunk;
    public int zazuPerChunk;
    public int maizePerChunk;

    public LKUpendiDecorator(LKBiomeGenUpendi upendi) {
        biome = upendi;
        whiteFlowerGen = new WorldGenFlowers(Blocks.yellow_flower);
        dirtGen = new LKWorldGenMinable(Block.getIdFromBlock(Blocks.dirt), 32);
        passionGen = new LKWorldGenPassionTrees(false);
        mangoGen = new LKWorldGenMangoTrees(false);
        purpleFlowerGen = new WorldGenFlowers(Blocks.red_flower);
        redFlowerGen = new WorldGenFlowers(Blocks.red_flower);
        zazuGen = new LKWorldGenZazu();
        maizeGen = new LKWorldGenMaize();
        treesPerChunk = 0;
        mangoPerChunk = 0;
        grassPerChunk = 0;
        whiteFlowersPerChunk = 0;
        purpleFlowersPerChunk = 0;
        redFlowersPerChunk = 0;
        zazuPerChunk = 0;
        maizePerChunk = 0;
        FMLLog.info("Initialized LKUpendiDecorator for biome %s", upendi.biomeName);
    }

    public void decorate(World world, Random random, int chunkX, int chunkZ) {
        this.currentWorld = world;
        this.randomGenerator = random;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        generateDecorations();
        FMLLog.fine("Decorated chunk at x=%d, z=%d in Upendi", chunkX, chunkZ);
    }

    private void generateDecorations() {
        generateDirtPatches();
        generatePassionTreeClusters();
        generateTrees();
        generateMangoTrees();
        generateGrass();
        generateFlowers(whiteFlowersPerChunk, whiteFlowerGen);
        generateFlowers(purpleFlowersPerChunk, purpleFlowerGen);
        generateFlowers(redFlowersPerChunk, redFlowerGen);
        generateFlowers(zazuPerChunk, zazuGen);
        generateFlowers(maizePerChunk, maizeGen);
        generateLiquids();
        generateWaterlilies();
    }

    private void generateDirtPatches() {
        for (int i = 0; i < 10; i++) {
            int x = chunkX + randomGenerator.nextInt(16) + 8;
            int z = chunkZ + randomGenerator.nextInt(16) + 8;
            int y = currentWorld.getHeightValue(x, z);
            if (y > 0 && y < 128) {
                dirtGen.generate(currentWorld, randomGenerator, x, y, z);
            }
        }
    }

    private void generatePassionTreeClusters() {
        if (randomGenerator.nextInt(20) == 0) {
            int baseX = chunkX + randomGenerator.nextInt(16) + 8;
            int baseZ = chunkZ + randomGenerator.nextInt(16) + 8;
            int treeCount = 3 + randomGenerator.nextInt(4);
            for (int i = 0; i < treeCount; i++) {
                int x = baseX + randomGenerator.nextInt(13) - 6;
                int z = baseZ + randomGenerator.nextInt(13) - 6;
                int y = currentWorld.getHeightValue(x, z);
                if (y > 0 && y < 128) {
                    passionGen.generate(currentWorld, randomGenerator, x, y, z);
                }
            }
        }
    }

    private void generateTrees() {
        for (int i = 0; i < treesPerChunk; i++) {
            int x = chunkX + randomGenerator.nextInt(16) + 8;
            int z = chunkZ + randomGenerator.nextInt(16) + 8;
            int y = currentWorld.getHeightValue(x, z);
            if (y > 75 && y < 128) {
                WorldGenerator treeGen = biome.func_150567_a(randomGenerator);
                if (treeGen != null) {
                    treeGen.generate(currentWorld, randomGenerator, x, y, z);
                } else {
                    FMLLog.warning("Null tree generator in Upendi at x=%d, z=%d", x, z);
                }
            }
        }
    }

    private void generateMangoTrees() {
        for (int i = 0; i < mangoPerChunk; i++) {
            int x = chunkX + randomGenerator.nextInt(16) + 8;
            int z = chunkZ + randomGenerator.nextInt(16) + 8;
            int y = currentWorld.getHeightValue(x, z);
            if (y > 75 && y < 128) {
                mangoGen.generate(currentWorld, randomGenerator, x, y, z);
            }
        }
    }

    private void generateGrass() {
        for (int i = 0; i < grassPerChunk; i++) {
            int x = chunkX + randomGenerator.nextInt(16) + 8;
            int z = chunkZ + randomGenerator.nextInt(16) + 8;
            int y = currentWorld.getHeightValue(x, z);
            if (y > 0 && y < 128) {
                WorldGenerator grassGen = biome.getRandomWorldGenForGrass(randomGenerator);
                if (grassGen != null) {
                    grassGen.generate(currentWorld, randomGenerator, x, y, z);
                } else {
                    FMLLog.warning("Null grass generator in Upendi at x=%d, z=%d", x, z);
                }
            }
        }
    }

    private void generateFlowers(int count, WorldGenerator generator) {
        for (int i = 0; i < count; i++) {
            int x = chunkX + randomGenerator.nextInt(16) + 8;
            int z = chunkZ + randomGenerator.nextInt(16) + 8;
            int y = currentWorld.getHeightValue(x, z);
            if (y > 0 && y < 128 && generator != null) {
                generator.generate(currentWorld, randomGenerator, x, y, z);
            }
        }
    }

    private void generateLiquids() {
        for (int i = 0; i < 50; i++) {
            int x = chunkX + randomGenerator.nextInt(16) + 8;
            int z = chunkZ + randomGenerator.nextInt(16) + 8;
            int y = randomGenerator.nextInt(randomGenerator.nextInt(120) + 8);
            if (y > 0 && y < 128) {
                new LKWorldGenLiquids(Block.getIdFromBlock(Blocks.water)).generate(currentWorld, randomGenerator, x, y, z);
            }
        }
    }

    private void generateWaterlilies() {
        for (int i = 0; i < 3; i++) {
            int x = chunkX + randomGenerator.nextInt(16) + 8;
            int z = chunkZ + randomGenerator.nextInt(16) + 8;
            int y = SEA_LEVEL;
            if (currentWorld.getBlock(x, y - 1, z) == Blocks.water && currentWorld.getBlock(x, y, z) == Blocks.air) {
                WorldGenerator lilyGen = randomGenerator.nextInt(3) == 0 ? new LKWorldGenLily() : new WorldGenWaterlily();
                lilyGen.generate(currentWorld, randomGenerator, x, y, z);
            }
        }
    }
}