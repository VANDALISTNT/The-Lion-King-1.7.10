package lionking.biome;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import lionking.mod_LionKing;
import lionking.world.LKWorldGenDeadTrees;

import java.util.Random;

public class LKBiomeGenOutlands extends LKOutlandsBiome {
    public LKBiomeGenOutlands(int biomeId) {
        super(biomeId);
        setBiomeName("Outlands");

        this.topBlock = mod_LionKing.outsand != null ? mod_LionKing.outsand : Blocks.sand;
        this.fillerBlock = mod_LionKing.pridestone != null ? mod_LionKing.pridestone : Blocks.stone;

        if (outlandsDecorator != null) {
            outlandsDecorator.treesPerChunk = 20;
            outlandsDecorator.deadBushPerChunk = 3;
        } else {
            FMLLog.warning("outlandsDecorator is null for Outlands biome ID %d", biomeId);
        }

        FMLLog.info("Initialized Outlands biome with ID %d", biomeId);
    }

    @Override
    public void decorate(World world, Random random, int chunkX, int chunkZ) {
        super.decorate(world, random, chunkX, chunkZ);
        FMLLog.fine("Decorated chunk at x=%d, z=%d in Outlands biome", chunkX, chunkZ);
    }

    @Override
    public WorldGenerator getOutlandsTreeGen(Random random) {
        WorldGenerator treeGen = new LKWorldGenDeadTrees();
        if (treeGen == null) {
            FMLLog.warning("Tree generator for Outlands biome returned null");
        }
        return treeGen;
    }
}