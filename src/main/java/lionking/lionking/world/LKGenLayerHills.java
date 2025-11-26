package lionking.world;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import cpw.mods.fml.common.FMLLog;
import lionking.biome.LKPrideLandsBiome;
import lionking.biome.LKOutlandsBiome;

public class LKGenLayerHills extends GenLayer {
    public LKGenLayerHills(long seed, GenLayer parentLayer) {
        super(seed);
        this.parent = parentLayer;
    }

    @Override
    public int[] getInts(int x, int z, int width, int height) {
        int[] parentData = parent.getInts(x - 1, z - 1, width + 2, height + 2);
        int[] result = IntCache.getIntCache(width * height);

        for (int dz = 0; dz < height; dz++) {
            for (int dx = 0; dx < width; dx++) {
                initChunkSeed(x + dx, z + dz);

                int idxCenter = dx + 1 + (dz + 1) * (width + 2);
                int idxNorth = dx + 1 + dz * (width + 2);
                int idxEast = dx + 2 + (dz + 1) * (width + 2);
                int idxWest = dx + (dz + 1) * (width + 2);
                int idxSouth = dx + 1 + (dz + 2) * (width + 2);

                int center = parentData[idxCenter];
                int north = parentData[idxNorth];
                int east = parentData[idxEast];
                int west = parentData[idxWest];
                int south = parentData[idxSouth];

                if (nextInt(3) == 0) {
                    int hillyBiome = getHillyBiome(center);

                    if (hillyBiome != center && north == center && east == center && west == center && south == center) {
                        result[dx + dz * width] = hillyBiome;
                        FMLLog.fine("Applied hilly biome ID %d at x=%d, z=%d", hillyBiome, x + dx, z + dz);
                    } else {
                        result[dx + dz * width] = center;
                    }
                } else {
                    result[dx + dz * width] = center;
                }
            }
        }

        return result;
    }

    private int getHillyBiome(int biomeID) {
        if (LKPrideLandsBiome.rainforest != null && biomeID == LKPrideLandsBiome.rainforest.biomeID) {
            if (LKPrideLandsBiome.rainforestHills != null) {
                return LKPrideLandsBiome.rainforestHills.biomeID;
            } else {
                FMLLog.warning("rainforestHills is null, returning original biome ID %d", biomeID);
                return biomeID;
            }
        }
        if (LKOutlandsBiome.outlands != null && biomeID == LKOutlandsBiome.outlands.biomeID) {
            if (LKOutlandsBiome.outlandsMountains != null) {
                return LKOutlandsBiome.outlandsMountains.biomeID;
            } else {
                FMLLog.warning("outlandsMountains is null, returning original biome ID %d", biomeID);
                return biomeID;
            }
        }
        return biomeID;
    }
}
