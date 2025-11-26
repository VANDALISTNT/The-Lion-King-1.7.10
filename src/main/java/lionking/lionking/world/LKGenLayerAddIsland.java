package lionking.world;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class LKGenLayerAddIsland extends GenLayer {
    public LKGenLayerAddIsland(long seed, GenLayer parentLayer) {
        super(seed);
        this.parent = parentLayer;
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
                
                int idxWest = dx + (dz + 0) * expandedWidth;
                int idxEast = dx + 2 + (dz + 0) * expandedWidth;
                int idxNorth = dx + (dz + 2) * expandedWidth;
                int idxSouth = dx + 2 + (dz + 2) * expandedWidth;
                int idxCenter = dx + 1 + (dz + 1) * expandedWidth;

                
                int west = parentData[idxWest];
                int east = parentData[idxEast];
                int north = parentData[idxNorth];
                int south = parentData[idxSouth];
                int center = parentData[idxCenter];

                
                initChunkSeed(x + dx, z + dz);

                
                if (center == 0 && (west != 0 || east != 0 || north != 0 || south != 0)) {
                    int neighborCount = 1;
                    int selectedBiome = 1;

                    
                    if (west != 0 && nextInt(neighborCount++) == 0) {
                        selectedBiome = west;
                    }
                    if (east != 0 && nextInt(neighborCount++) == 0) {
                        selectedBiome = east;
                    }
                    if (north != 0 && nextInt(neighborCount++) == 0) {
                        selectedBiome = north;
                    }
                    if (south != 0 && nextInt(neighborCount++) == 0) {
                        selectedBiome = south;
                    }

                    
                    result[dx + dz * width] = nextInt(3) == 0 ? selectedBiome : 0;
                } else {
                    
                    result[dx + dz * width] = center;
                }
            }
        }

        return result;
    }
}
