package lionking.world;

import net.minecraft.world.ChunkPosition;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.biome.BiomeGenBase;

import lionking.biome.*;
import lionking.mod_LionKing;

import java.util.Random;
import java.util.List;
import java.util.Arrays;

import cpw.mods.fml.common.FMLLog;

public class LKWorldChunkManagerUpendi extends WorldChunkManager {
    private static final BiomeGenBase UPENDI_BIOME = LKBiomeGenUpendi.upendi;

    private final BiomeGenBase theBiome;
    private final float temperature;
    private final float rainfall;

    public LKWorldChunkManagerUpendi() {
        FMLLog.info("Initializing LKWorldChunkManagerUpendi");
        if (UPENDI_BIOME == null) {
            FMLLog.severe("UPENDI_BIOME is null! Using default plains biome.");
            this.theBiome = BiomeGenBase.plains;
            this.temperature = BiomeGenBase.plains.temperature;
            this.rainfall = BiomeGenBase.plains.getFloatRainfall();
        } else {
            this.theBiome = UPENDI_BIOME;
            this.temperature = UPENDI_BIOME.temperature; 
            this.rainfall = UPENDI_BIOME.getFloatRainfall();
        }
    }

    @Override
    public BiomeGenBase getBiomeGenAt(int x, int z) {
        return theBiome;
    }

    @Override
    public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] biomes, int x, int z, int width, int height) {
        biomes = ensureBiomeArraySize(biomes, width * height);
        Arrays.fill(biomes, 0, width * height, theBiome);
        return biomes;
    }

    public float[] getTemperature(float[] temperatures, int x, int z, int width, int height) {
        temperatures = ensureArraySize(temperatures, width * height);
        Arrays.fill(temperatures, 0, width * height, this.temperature); 
        return temperatures;
    }

    @Override
    public float[] getRainfall(float[] rainfall, int x, int z, int width, int height) {
        rainfall = ensureArraySize(rainfall, width * height);
        Arrays.fill(rainfall, 0, width * height, this.rainfall);
        return rainfall;
    }

    @Override
    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] biomes, int x, int z, int width, int height) {
        biomes = ensureBiomeArraySize(biomes, width * height);
        Arrays.fill(biomes, 0, width * height, theBiome);
        return biomes;
    }

    @Override
    public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] biomes, int x, int z, int width, int height, boolean useCache) {
        return loadBlockGeneratorData(biomes, x, z, width, height);
    }

    @Override
    public ChunkPosition findBiomePosition(int x, int z, int radius, List allowedBiomes, Random random) {
        return allowedBiomes.contains(theBiome)
            ? new ChunkPosition(x - radius + random.nextInt(radius * 2 + 1), 0, z - radius + random.nextInt(radius * 2 + 1))
            : null;
    }

    @Override
    public boolean areBiomesViable(int x, int z, int radius, List allowedBiomes) {
        return allowedBiomes.contains(theBiome);
    }

    private float[] ensureArraySize(float[] array, int size) {
        return array == null || array.length < size ? new float[size] : array;
    }

    private BiomeGenBase[] ensureBiomeArraySize(BiomeGenBase[] array, int size) {
        return array == null || array.length < size ? new BiomeGenBase[size] : array;
    }
}
