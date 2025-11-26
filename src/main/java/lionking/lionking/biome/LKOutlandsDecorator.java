package lionking.biome;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenDeadBush;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.init.Blocks;

import lionking.mod_LionKing;
import lionking.world.LKWorldGenMinable;
import lionking.world.LKWorldGenTermiteMound;
import lionking.world.LKWorldGenLiquids;

import java.util.Random;

public class LKOutlandsDecorator {
    private static final int SEA_LEVEL = 63; 
    private World currentWorld;
    private Random randomGenerator;
    private int chunkX;
    private int chunkZ;
    private final LKOutlandsBiome biome;
    private final WorldGenerator nukaGen;
    private final WorldGenerator kivuliteGen;
    private final WorldGenerator termiteAsOreGen;
    public int treesPerChunk;
    public int deadBushPerChunk;

    public LKOutlandsDecorator(LKOutlandsBiome biome) {
        this.biome = biome;
        nukaGen = new LKWorldGenMinable(Block.getIdFromBlock(mod_LionKing.prideCoal), 9, Block.getIdFromBlock(mod_LionKing.pridestone));
        kivuliteGen = new LKWorldGenMinable(Block.getIdFromBlock(mod_LionKing.oreSilver), 4, Block.getIdFromBlock(mod_LionKing.pridestone));
        termiteAsOreGen = new LKWorldGenMinable(Block.getIdFromBlock(mod_LionKing.termite), 20, Block.getIdFromBlock(mod_LionKing.pridestone));
        treesPerChunk = 0;
        deadBushPerChunk = 0;
    }

    public void decorate(World world, Random random, int chunkX, int chunkZ) {
        this.currentWorld = world;
        this.randomGenerator = random;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        generateDecorations();
    }

    private void generateDecorations() {
        generateOres();
        generateTrees();
        generateDeadBushes();
        generateOutshrooms();
        generateTermiteMounds();
        generateLiquids();
    }

    private void generateOres() {
        generateOre(nukaGen, 18, 0, 128);
        generateOre(kivuliteGen, 8, 0, 36);
        generateOre(termiteAsOreGen, 1, 0, 18);
    }

    private void generateOre(WorldGenerator generator, int count, int minY, int maxY) {
        for (int i = 0; i < count; i++) {
            int x = chunkX + randomGenerator.nextInt(16);
            int y = randomGenerator.nextInt(maxY - minY) + minY;
            int z = chunkZ + randomGenerator.nextInt(16);
            generator.generate(currentWorld, randomGenerator, x, y, z);
        }
    }

    private void generateTrees() {
        for (int i = 0; i < treesPerChunk; i++) {
            int x = chunkX + randomGenerator.nextInt(16) + 8;
            int z = chunkZ + randomGenerator.nextInt(16) + 8;
            int y = currentWorld.getHeightValue(x, z);
            if (y > 0 && y < 128) {
                biome.getOutlandsTreeGen(randomGenerator).generate(currentWorld, randomGenerator, x, y, z);
            }
        }
    }

    private void generateDeadBushes() {
        if (!(biome instanceof LKBiomeGenOutlandsRiver)) {
            for (int i = 0; i < deadBushPerChunk; i++) {
                int x = chunkX + randomGenerator.nextInt(16) + 8;
                int z = chunkZ + randomGenerator.nextInt(16) + 8;
                int y = currentWorld.getHeightValue(x, z);
                if (y > 0 && y < 128) {
                    new WorldGenDeadBush(Blocks.deadbush).generate(currentWorld, randomGenerator, x, y, z);
                }
            }
        }
    }

    private void generateOutshrooms() {
        if (mod_LionKing.outshroom == null) {
            return;
        }
        for (int i = 0; i < 2; i++) {
            int x = chunkX + randomGenerator.nextInt(16) + 8;
            int z = chunkZ + randomGenerator.nextInt(16) + 8;
            int y = currentWorld.getHeightValue(x, z);
            if (y > 0 && y < 128) {
                new WorldGenFlowers(mod_LionKing.outshroom).generate(currentWorld, randomGenerator, x, y, z);
            }
        }
    }

    private void generateTermiteMounds() {
        if (!(biome instanceof LKBiomeGenOutlandsRiver)) {
            for (int i = 0; i < 10; i++) {
                int x = chunkX + randomGenerator.nextInt(16) + 8;
                int z = chunkZ + randomGenerator.nextInt(16) + 8;
                int y = currentWorld.getHeightValue(x, z);
                if (y > 0 && y < 128) {
                    new LKWorldGenTermiteMound().generate(currentWorld, randomGenerator, x, y, z);
                }
            }
        }
    }

    private void generateLiquids() {
        boolean isRiverBiome = biome instanceof LKBiomeGenOutlandsRiver;
        int count = isRiverBiome ? 50 : 20;
        Block liquid = isRiverBiome ? Blocks.flowing_water : Blocks.flowing_lava;
        for (int i = 0; i < count; i++) {
            int x = chunkX + randomGenerator.nextInt(16) + 8;
            int z = chunkZ + randomGenerator.nextInt(16) + 8;
            int y = randomGenerator.nextInt(randomGenerator.nextInt(120) + 8);
            if (y > 0 && y < 128) {
                new LKWorldGenLiquids(Block.getIdFromBlock(liquid)).generate(currentWorld, randomGenerator, x, y, z);
            }
        }
    }
}