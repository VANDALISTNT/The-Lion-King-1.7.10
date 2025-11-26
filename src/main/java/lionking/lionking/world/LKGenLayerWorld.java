package lionking.world;

import net.minecraft.world.WorldType;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerFuzzyZoom;
import net.minecraft.world.gen.layer.GenLayerRiverInit;
import net.minecraft.world.gen.layer.GenLayerRiverMix;
import net.minecraft.world.gen.layer.GenLayerSmooth;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;
import cpw.mods.fml.common.FMLLog;

import lionking.world.LKGenLayerPrideLandsBiomes;
import lionking.world.LKGenLayerOutlandsBiomes;
import lionking.world.LKGenLayerHills;
import lionking.world.LKGenLayerRiver;

public class LKGenLayerWorld {
    private static final int PRIDE_LANDS_RIVER = 0;
    private static final int OUTLANDS_RIVER = 1;

    public static GenLayer[] createPrideLands(long seed, WorldType worldType) {
        FMLLog.info("Creating Pride Lands biome layers with seed %d", seed);
        return createWorld(seed, worldType, new LKGenLayerPrideLandsBiomes(200L), PRIDE_LANDS_RIVER);
    }

    public static GenLayer[] createOutlands(long seed, WorldType worldType) {
        FMLLog.info("Creating Outlands biome layers with seed %d", seed);
        return createWorld(seed, worldType, new LKGenLayerOutlandsBiomes(200L), OUTLANDS_RIVER);
    }

    private static GenLayer[] createWorld(long seed, WorldType worldType, GenLayer biomeLayer, int riverType) {
        byte scale = (byte) (worldType == WorldType.LARGE_BIOMES ? 6 : 4);

        GenLayer layer = biomeLayer;
        layer = new GenLayerFuzzyZoom(2000L, layer);
        layer = applyZoom(2001L, layer, 3);

        GenLayer rivers = GenLayerZoom.magnify(1000L, layer, 0);
        rivers = new GenLayerRiverInit(100L, rivers);
        rivers = GenLayerZoom.magnify(1000L, rivers, scale + 2);
        rivers = new LKGenLayerRiver(1L, rivers, riverType);
        rivers = new GenLayerSmooth(1000L, rivers);

        GenLayer biomes = GenLayerZoom.magnify(1000L, layer, 0);
        biomes = GenLayerZoom.magnify(1000L, biomes, 2);
        layer = new LKGenLayerHills(1000L, biomes);
        layer = applyZoom(1000L, layer, scale);
        layer = new GenLayerSmooth(1000L, layer);
        layer = new GenLayerRiverMix(100L, layer, rivers);

        GenLayer voronoiLayer = new GenLayerVoronoiZoom(10L, layer);

        long adjustedSeed = seed + 1994L;
        layer.initWorldGenSeed(adjustedSeed);
        voronoiLayer.initWorldGenSeed(adjustedSeed);

        return new GenLayer[]{layer, voronoiLayer};
    }

    private static GenLayer applyZoom(long baseSeed, GenLayer layer, int times) {
        GenLayer result = layer;
        for (int i = 0; i < times; i++) {
            result = new GenLayerZoom(baseSeed + i, result);
        }
        return result;
    }
}