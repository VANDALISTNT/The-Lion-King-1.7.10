package lionking.world;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraft.world.biome.BiomeGenBase;
import lionking.biome.LKOutlandsBiome;
import cpw.mods.fml.common.FMLLog;

public class LKGenLayerOutlandsBiomes extends GenLayer {
    private static final BiomeGenBase[] BIOMES = {
        LKOutlandsBiome.outlands,
        LKOutlandsBiome.outlandsMountains,
        LKOutlandsBiome.outlandsRiver
    };

    public LKGenLayerOutlandsBiomes(long seed, GenLayer parentLayer) {
        super(seed);
        this.parent = parentLayer;
    }

    public LKGenLayerOutlandsBiomes(long seed) {
        super(seed);
    }

    @Override
    public int[] getInts(int x, int z, int width, int height) {
        int[] result = IntCache.getIntCache(width * height);

        for (int dz = 0; dz < height; dz++) {
            for (int dx = 0; dx < width; dx++) {
                initChunkSeed(x + dx, z + dz);
                int biomeIndex = nextInt(BIOMES.length);
                BiomeGenBase biome = BIOMES[biomeIndex];
                if (biome == null) {
                    FMLLog.severe("Null biome detected in LKGenLayerOutlandsBiomes at index %d! Using plains biome.", biomeIndex);
                    biome = BiomeGenBase.plains;
                }
                result[dx + dz * width] = biome.biomeID;
            }
        }

        return result;
    }
}