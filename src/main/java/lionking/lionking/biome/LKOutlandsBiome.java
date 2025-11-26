package lionking.biome;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import java.util.Random;

import lionking.entity.LKEntityOutlander;
import lionking.entity.LKEntityOutlandess;
import lionking.entity.LKEntityVulture;
import lionking.entity.LKEntitySkeletalHyena;
import lionking.world.LKWorldGenDeadTrees;
import lionking.mod_LionKing;

public class LKOutlandsBiome extends BiomeGenBase {
    public static BiomeGenBase outlands;
    public static BiomeGenBase outlandsMountains;
    public static BiomeGenBase outlandsRiver;

    protected final LKOutlandsDecorator outlandsDecorator;

    public LKOutlandsBiome(int biomeId) {
        super(biomeId);

        setTemperatureRainfall(1.0F, 0.0F); 
        setHeight(new Height(0.1F, 0.2F)); 
        setColor(0xD2B48C); 
        setDisableRain(); 

        topBlock = Blocks.sand;
        fillerBlock = Blocks.sand;

        spawnableMonsterList.clear();
        spawnableCreatureList.clear();
        spawnableWaterCreatureList.clear();

        spawnableMonsterList.add(new SpawnListEntry(LKEntityOutlander.class, 40, 4, 8));
        spawnableMonsterList.add(new SpawnListEntry(LKEntityOutlandess.class, 40, 4, 8));
        spawnableMonsterList.add(new SpawnListEntry(LKEntityVulture.class, 30, 4, 4));
        spawnableMonsterList.add(new SpawnListEntry(LKEntitySkeletalHyena.class, 10, 4, 6));

        outlandsDecorator = new LKOutlandsDecorator(this);
    }

    public static void registerBiomes() {
        outlands = new LKBiomeGenOutlands(136);
        outlandsMountains = new LKBiomeGenOutlandsMountains(137);
        outlandsRiver = new LKBiomeGenOutlandsRiver(138);
    }

    @Override
    public void decorate(World world, Random random, int chunkX, int chunkZ) {
        if (outlandsDecorator != null) {
            outlandsDecorator.decorate(world, random, chunkX, chunkZ);
        }
    }

    public WorldGenerator getOutlandsTreeGen(Random random) {
        return new LKWorldGenDeadTrees();
    }
}
