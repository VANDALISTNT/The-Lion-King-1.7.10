package lionking.biome;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.init.Blocks;
import java.util.Random;
import cpw.mods.fml.common.FMLLog;
import lionking.entity.*;
import lionking.world.*;
import lionking.mod_LionKing;

public class LKPrideLandsBiome extends BiomeGenBase {
    public static BiomeGenBase savannah;
    public static BiomeGenBase rainforest;
    public static BiomeGenBase mountains;
    public static BiomeGenBase rainforestHills;
    public static BiomeGenBase river;
    public static BiomeGenBase aridSavannah;
    public static BiomeGenBase desert;
    public static BiomeGenBase grasslandSavannah;
    public static BiomeGenBase woodedSavannah;
    public static BiomeGenBase bananaForest;

    protected LKBiomeDecorator lkDecorator;

    public LKPrideLandsBiome(final int id) {
        super(id);
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.spawnableCreatureList.add(new SpawnListEntry(LKEntityLion.class, 4, 4, 4));
        this.spawnableCreatureList.add(new SpawnListEntry(LKEntityLioness.class, 4, 4, 4));
        this.spawnableCreatureList.add(new SpawnListEntry(LKEntityZebra.class, 8, 4, 4));
        this.spawnableCreatureList.add(new SpawnListEntry(LKEntityRhino.class, 8, 4, 4));
        this.spawnableCreatureList.add(new SpawnListEntry(LKEntityGemsbok.class, 8, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(LKEntityHyena.class, 80, 4, 6));
        this.spawnableMonsterList.add(new SpawnListEntry(LKEntityCrocodile.class, 30, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(LKEntitySkeletalHyena.class, 1, 2, 4));
        this.lkDecorator = new LKBiomeDecorator(this);
        this.theBiomeDecorator.treesPerChunk = 10;
        this.theBiomeDecorator.grassPerChunk = 5;
        FMLLog.info("Initialized LKPrideLandsBiome with ID %d", id);
    }

    @Override
    public void decorate(World world, Random random, int chunkX, int chunkZ) {
        super.decorate(world, random, chunkX, chunkZ);
        if (lkDecorator != null) {
            this.lkDecorator.decorate(world, random, chunkX, chunkZ);
        } else {
            FMLLog.warning("Biome decorator for LKPrideLandsBiome is null for chunk x=%d, z=%d", chunkX, chunkZ);
        }
        
        FMLLog.fine("Decorating Pride Lands biome with trees at chunk (%d, %d)", chunkX, chunkZ);
        for (int i = 0; i < this.theBiomeDecorator.treesPerChunk; i++) {
            int treeX = chunkX + random.nextInt(16) + 8;
            int treeZ = chunkZ + random.nextInt(16) + 8;
            int treeY = world.getHeightValue(treeX, treeZ);
            WorldGenerator treeGen = LKTreeGenerator.getUpendiTreeGenerator(random); 
            if (treeGen.generate(world, random, treeX, treeY, treeZ)) {
                FMLLog.fine("Successfully generated tree at (%d, %d, %d)", treeX, treeY, treeZ);
            } else {
                FMLLog.warning("Failed to generate tree at (%d, %d, %d)", treeX, treeY, treeZ);
            }
        }
    }

    @Override
    public WorldGenAbstractTree func_150567_a(Random random) {
        FMLLog.fine("Using default tree generator for Pride Lands biome (func_150567_a)");
        return new WorldGenTrees(false, 4, 0, 0, false); 
    }

    @Override
    public WorldGenerator getRandomWorldGenForGrass(Random random) {
        WorldGenerator grassGen = new WorldGenTallGrass(Blocks.tallgrass, 1);
        if (grassGen == null) {
            FMLLog.warning("Grass generator for LKPrideLandsBiome returned null");
        }
        return grassGen;
    }
}