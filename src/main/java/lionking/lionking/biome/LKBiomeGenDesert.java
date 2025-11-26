package lionking.biome;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.feature.WorldGenCactus;
import net.minecraft.world.gen.feature.WorldGenDeadBush;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.init.Blocks;
import java.util.Random;
import cpw.mods.fml.common.FMLLog;

public class LKBiomeGenDesert extends LKPrideLandsBiome {
    public LKBiomeGenDesert(int biomeId) {
        super(biomeId);
        setBiomeName("Desert");

        spawnableCreatureList.clear();
        spawnableCaveCreatureList.clear();

        topBlock = Blocks.sand;
        fillerBlock = Blocks.sand;

        if (lkDecorator != null) {
            lkDecorator.treesPerChunk = 0;
            lkDecorator.mangoPerChunk = 0;
            lkDecorator.grassPerChunk = 0;
            lkDecorator.whiteFlowersPerChunk = 0;
            lkDecorator.purpleFlowersPerChunk = 0;
            lkDecorator.blueFlowersPerChunk = 0;
            lkDecorator.logsPerChunk = 0;
            lkDecorator.maizePerChunk = 0;
            lkDecorator.zazuPerChunk = 0;
        } else {
            FMLLog.warning("Decorator is null for LKBiomeGenDesert with ID %d", biomeId);
        }

        FMLLog.info("Initialized LKBiomeGenDesert with ID %d", biomeId);
    }

    @Override
    public void decorate(World world, Random random, int chunkX, int chunkZ) {
        if (lkDecorator == null) {
            FMLLog.warning("Decorator is null for LKBiomeGenDesert at chunk x=%d, z=%d", chunkX, chunkZ);
            return;
        }

        super.decorate(world, random, chunkX, chunkZ);

        if (random.nextInt(4) == 0) {
            int x = chunkX + random.nextInt(16) + 8;
            int z = chunkZ + random.nextInt(16) + 8;
            int y = world.getHeightValue(x, z);
            WorldGenerator cactusGen = new WorldGenCactus();
            if (cactusGen.generate(world, random, x, y, z)) {
                FMLLog.fine("Generated cactus at x=%d, y=%d, z=%d in LKBiomeGenDesert", x, y, z);
            } else {
                FMLLog.warning("Failed to generate cactus at x=%d, y=%d, z=%d in LKBiomeGenDesert", x, y, z);
            }
        }

        for (int i = 0; i < 2; i++) {
            int x = chunkX + random.nextInt(16) + 8;
            int z = chunkZ + random.nextInt(16) + 8;
            int y = world.getHeightValue(x, z);
            WorldGenerator deadBushGen = new WorldGenDeadBush(Blocks.deadbush);
            if (deadBushGen.generate(world, random, x, y, z)) {
                FMLLog.fine("Generated dead bush at x=%d, y=%d, z=%d in LKBiomeGenDesert", x, y, z);
            } else {
                FMLLog.warning("Failed to generate dead bush at x=%d, y=%d, z=%d in LKBiomeGenDesert", x, y, z);
            }
        }

        FMLLog.fine("Decorated chunk at x=%d, z=%d in LKBiomeGenDesert", chunkX, chunkZ);
    }

    @Override
    public WorldGenAbstractTree func_150567_a(Random random) {
        FMLLog.fine("Using default tree generator for Desert biome (func_150567_a)");
        return new WorldGenTrees(false, 4, 0, 0, false);
    }

    @Override
    public WorldGenerator getRandomWorldGenForGrass(Random random) {
        FMLLog.fine("Grass generator requested for LKBiomeGenDesert, returning null");
        return null;
    }
}
