package lionking.biome;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import lionking.world.LKWorldGenDeadTrees;

import java.util.Random;

public class LKBiomeGenOutlandsRiver extends LKOutlandsBiome {
    public LKBiomeGenOutlandsRiver(int biomeId) {
        super(biomeId);
        setBiomeName("Outlands River");

        topBlock = Blocks.gravel;
        fillerBlock = Blocks.dirt;
        setHeight(new Height(-0.5F, 0.0F)); 

        if (outlandsDecorator != null) {
            outlandsDecorator.treesPerChunk = 0;
            outlandsDecorator.deadBushPerChunk = 3;
        } else {
            FMLLog.warning("outlandsDecorator is null for Outlands River biome ID %d", biomeId);
        }

        setTemperatureRainfall(0.8F, 0.4F); 
        setColor(0x4A90E2); 

        FMLLog.info("Initialized Outlands River biome with ID %d", biomeId);
    }

    @Override
    public void decorate(World world, Random random, int chunkX, int chunkZ) {
        super.decorate(world, random, chunkX, chunkZ);
        FMLLog.fine("Decorated chunk at x=%d, z=%d in Outlands River biome", chunkX, chunkZ);
    }

    @Override
    public WorldGenerator getOutlandsTreeGen(Random random) {
        WorldGenerator treeGen = new LKWorldGenDeadTrees();
        if (treeGen == null) {
            FMLLog.warning("Tree generator for Outlands River biome returned null");
        }
        return treeGen;
    }
}