package lionking.biome;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import lionking.world.LKWorldGenDeadTrees;

import java.util.Random;

public class LKBiomeGenOutlandsMountains extends LKOutlandsBiome {
    public LKBiomeGenOutlandsMountains(int biomeId) {
        super(biomeId);
        setBiomeName("Outlands Mountains");
        setHeight(new Height(0.8F, 0.4F)); 

        if (outlandsDecorator != null) {
            outlandsDecorator.treesPerChunk = 10;
            outlandsDecorator.deadBushPerChunk = 4;
        } else {
            FMLLog.warning("outlandsDecorator is null for Outlands Mountains biome ID %d", biomeId);
        }

        FMLLog.info("Initialized Outlands Mountains biome with ID %d", biomeId);
    }

    @Override
    public void decorate(World world, Random random, int chunkX, int chunkZ) {
        super.decorate(world, random, chunkX, chunkZ);
        FMLLog.fine("Decorated chunk at x=%d, z=%d in Outlands Mountains biome", chunkX, chunkZ);
    }

    @Override
    public WorldGenerator getOutlandsTreeGen(Random random) {
        WorldGenerator treeGen = new LKWorldGenDeadTrees();
        if (treeGen == null) {
            FMLLog.warning("Tree generator for Outlands Mountains biome returned null");
        }
        return treeGen;
    }
}