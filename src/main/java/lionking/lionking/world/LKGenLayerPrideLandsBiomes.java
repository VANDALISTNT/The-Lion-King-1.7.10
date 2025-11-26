package lionking.world;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraft.world.biome.BiomeGenBase;
import lionking.biome.LKPrideLandsBiome;
import lionking.biome.LKOutlandsBiome;
import lionking.biome.LKBiomeGenUpendi;
import cpw.mods.fml.common.FMLLog;

public class LKGenLayerPrideLandsBiomes extends GenLayer {
    private static final BiomeGenBase[] BIOMES = {
        LKPrideLandsBiome.savannah,
        LKPrideLandsBiome.rainforest,
        LKPrideLandsBiome.mountains,
        LKPrideLandsBiome.rainforestHills,
        LKPrideLandsBiome.river,
        LKPrideLandsBiome.aridSavannah,
        LKBiomeGenUpendi.upendi
    };

    public LKGenLayerPrideLandsBiomes(long seed, GenLayer parentLayer) {
        super(seed);
        this.parent = parentLayer;
    }

    public LKGenLayerPrideLandsBiomes(long seed) {
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
                    FMLLog.severe("Null biome detected in LKGenLayerPrideLandsBiomes at index %d for coordinates (%d, %d)", biomeIndex, x + dx, z + dz);
                    biome = BiomeGenBase.plains;
                }
                result[dx + dz * width] = biome.biomeID;
            }
        }

        return result;
    }
}
