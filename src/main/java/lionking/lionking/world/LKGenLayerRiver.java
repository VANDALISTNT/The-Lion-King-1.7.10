package lionking.world;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraft.world.biome.BiomeGenBase;
import cpw.mods.fml.common.FMLLog;
import lionking.biome.LKPrideLandsBiome;
import lionking.biome.LKOutlandsBiome;

public class LKGenLayerRiver extends GenLayer {
    private final BiomeGenBase riverBiome;

    public LKGenLayerRiver(long seed, GenLayer parentLayer, int riverType) {
        super(seed);
        this.parent = parentLayer;
        if (riverType == 0) {
            riverBiome = LKPrideLandsBiome.river != null ? LKPrideLandsBiome.river : BiomeGenBase.plains;
            if (LKPrideLandsBiome.river == null) {
                FMLLog.warning("Pride Lands river biome is null, defaulting to plains");
            }
        } else {
            riverBiome = LKOutlandsBiome.outlandsRiver != null ? LKOutlandsBiome.outlandsRiver : BiomeGenBase.plains;
            if (LKOutlandsBiome.outlandsRiver == null) {
                FMLLog.warning("Outlands river biome is null, defaulting to plains");
            }
        }
        FMLLog.info("Initialized LKGenLayerRiver with riverType %d and biome ID %d", riverType, riverBiome.biomeID);
    }

    @Override
    public int[] getInts(int x, int z, int width, int height) {
        int xStart = x - 1;
        int zStart = z - 1;
        int expandedWidth = width + 2;
        int expandedHeight = height + 2;

        int[] parentData = parent.getInts(xStart, zStart, expandedWidth, expandedHeight);
        int[] result = IntCache.getIntCache(width * height);

        for (int dz = 0; dz < height; dz++) {
            for (int dx = 0; dx < width; dx++) {
                int idxWest = dx + (dz + 1) * expandedWidth;
                int idxEast = dx + 2 + (dz + 1) * expandedWidth;
                int idxNorth = dx + 1 + dz * expandedWidth;
                int idxSouth = dx + 1 + (dz + 2) * expandedWidth;
                int idxCenter = dx + 1 + (dz + 1) * expandedWidth;

                int west = parentData[idxWest];
                int east = parentData[idxEast];
                int north = parentData[idxNorth];
                int south = parentData[idxSouth];
                int center = parentData[idxCenter];

                if (center != 0 && west != 0 && east != 0 && north != 0 && south != 0) {
                    if (center == west && center == east && center == north && center == south) {
                        result[dx + dz * width] = -1;
                    } else {
                        result[dx + dz * width] = riverBiome.biomeID;
                        FMLLog.fine("Placed river biome ID %d at x=%d, z=%d", riverBiome.biomeID, x + dx, z + dz);
                    }
                } else {
                    result[dx + dz * width] = riverBiome.biomeID;
                    FMLLog.fine("Placed river biome ID %d at x=%d, z=%d (border case)", riverBiome.biomeID, x + dx, z + dz);
                }
            }
        }

        return result;
    }
}
