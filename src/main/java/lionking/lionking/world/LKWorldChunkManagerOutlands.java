package lionking.world;

import net.minecraft.world.ChunkPosition;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.WorldType;
import cpw.mods.fml.common.FMLLog;

import lionking.biome.*;
import lionking.mod_LionKing;

import java.util.Random;
import java.util.List;

public class LKWorldChunkManagerOutlands extends WorldChunkManager {
    private static final int BIOME_CACHE_SIZE = 16;
    private static final float MAX_VALUE = 1.0F;

    private GenLayer genBiomes;
    private GenLayer biomeIndexLayer;
    private BiomeCache biomeCache;

    public LKWorldChunkManagerOutlands(long seed, WorldType worldType) {
        this.biomeCache = new BiomeCache(this);
        GenLayer[] layers = LKGenLayerWorld.createOutlands(seed, worldType);
        this.genBiomes = layers[0];
        this.biomeIndexLayer = layers[1];
    }

    @Override
    public BiomeGenBase getBiomeGenAt(int x, int z) {
        BiomeGenBase biome = biomeCache.getBiomeGenAt(x, z);
        if (biome == null) {
            FMLLog.warning("Null biome at x=%d, z=%d in Outlands, defaulting to plains", x, z);
            return BiomeGenBase.plains;
        }
        return biome;
    }

    @Override
    public float[] getRainfall(float[] rainfall, int x, int z, int width, int height) {
        IntCache.resetIntCache();
        rainfall = ensureArraySize(rainfall, width * height);

        int[] biomeIds = biomeIndexLayer.getInts(x, z, width, height);
        for (int i = 0; i < width * height; i++) {
            BiomeGenBase biome = BiomeGenBase.getBiome(biomeIds[i]);
            float value = biome != null ? biome.getFloatRainfall() : BiomeGenBase.plains.getFloatRainfall();
            rainfall[i] = Math.min(value, MAX_VALUE);
        }
        return rainfall;
    }

    public float[] getTemperature(float[] temperatures, int x, int z, int width, int height) {
        IntCache.resetIntCache();
        temperatures = ensureArraySize(temperatures, width * height);

        for (int i = 0; i < width * height; i++) {
            int ix = x + i % width;
            int iz = z + i / width;
            BiomeGenBase biome = getBiomeGenAt(ix, iz);
            float value = biome != null ? biome.temperature : BiomeGenBase.plains.temperature;
            temperatures[i] = Math.min(value, MAX_VALUE);
        }
        return temperatures;
    }

    @Override
    public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] biomes, int x, int z, int width, int height) {
        IntCache.resetIntCache();
        biomes = ensureBiomeArraySize(biomes, width * height);

        int[] biomeIds = genBiomes.getInts(x, z, width, height);
        for (int i = 0; i < width * height; i++) {
            BiomeGenBase biome = BiomeGenBase.getBiome(biomeIds[i]);
            biomes[i] = biome != null ? biome : BiomeGenBase.plains;
            if (biome == null) {
                FMLLog.warning("Null biome ID %d in Outlands generation, defaulting to plains", biomeIds[i]);
            }
        }
        return biomes;
    }

    @Override
    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] biomes, int x, int z, int width, int height) {
        return getBiomeGenAt(biomes, x, z, width, height, true);
    }

    @Override
    public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] biomes, int x, int z, int width, int height, boolean useCache) {
        IntCache.resetIntCache();
        biomes = ensureBiomeArraySize(biomes, width * height);

        if (useCache && width == BIOME_CACHE_SIZE && height == BIOME_CACHE_SIZE && (x & 15) == 0 && (z & 15) == 0) {
            BiomeGenBase[] cached = biomeCache.getCachedBiomes(x, z);
            System.arraycopy(cached, 0, biomes, 0, width * height);
        } else {
            int[] biomeIds = biomeIndexLayer.getInts(x, z, width, height);
            for (int i = 0; i < width * height; i++) {
                BiomeGenBase biome = BiomeGenBase.getBiome(biomeIds[i]);
                biomes[i] = biome != null ? biome : BiomeGenBase.plains;
                if (biome == null) {
                    FMLLog.warning("Null biome ID %d in Outlands at x=%d, z=%d, defaulting to plains", biomeIds[i], x + i % width, z + i / width);
                }
            }
        }
        return biomes;
    }

    @Override
    public boolean areBiomesViable(int x, int z, int radius, List allowedBiomes) {
        int minX = x - radius >> 2;
        int minZ = z - radius >> 2;
        int maxX = x + radius >> 2;
        int maxZ = z + radius >> 2;
        int width = maxX - minX + 1;
        int height = maxZ - minZ + 1;

        int[] biomeIds = genBiomes.getInts(minX, minZ, width, height);
        for (int i = 0; i < width * height; i++) {
            BiomeGenBase biome = BiomeGenBase.getBiome(biomeIds[i]);
            if (!allowedBiomes.contains(biome != null ? biome : BiomeGenBase.plains)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ChunkPosition findBiomePosition(int x, int z, int radius, List allowedBiomes, Random random) {
        int minX = x - radius >> 2;
        int minZ = z - radius >> 2;
        int maxX = x + radius >> 2;
        int maxZ = z + radius >> 2;
        int width = maxX - minX + 1;
        int height = maxZ - minZ + 1;

        int[] biomeIds = genBiomes.getInts(minX, minZ, width, height);
        ChunkPosition position = null;
        int count = 0;

        for (int i = 0; i < biomeIds.length; i++) {
            int biomeX = minX + i % width << 2;
            int biomeZ = minZ + i / width << 2;
            BiomeGenBase biome = BiomeGenBase.getBiome(biomeIds[i]);
            if (biome == null) {
                biome = BiomeGenBase.plains;
                FMLLog.warning("Null biome ID %d in Outlands position search, defaulting to plains", biomeIds[i]);
            }
            if (allowedBiomes.contains(biome) && (position == null || random.nextInt(count + 1) == 0)) {
                position = new ChunkPosition(biomeX, 0, biomeZ);
                count++;
            }
        }
        return position;
    }

    @Override
    public void cleanupCache() {
        biomeCache.cleanupCache();
    }

    private float[] ensureArraySize(float[] array, int size) {
        if (array == null || array.length < size) {
            return new float[size];
        }
        return array;
    }

    private BiomeGenBase[] ensureBiomeArraySize(BiomeGenBase[] array, int size) {
        if (array == null || array.length < size) {
            return new BiomeGenBase[size];
        }
        return array;
    }
}