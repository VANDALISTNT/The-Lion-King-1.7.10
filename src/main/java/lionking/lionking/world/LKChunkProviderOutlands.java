package lionking.world;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.block.BlockSand;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import lionking.biome.LKOutlandsBiome;
import lionking.world.LKWorldGenOutlandsLakes;
import lionking.world.LKWorldGenTreasureMound;
import lionking.world.LKMapGenOutlandsCaves;
import java.util.Random;
import java.util.List;

public class LKChunkProviderOutlands implements IChunkProvider {

    private static final int CHUNK_HEIGHT = 256;
    private Random rand;
    private NoiseGeneratorOctaves noiseGen1, noiseGen2, noiseGen3, noiseGen4, noiseGen5, noiseGen6;
    private World worldObj;
    private double[] noiseField;
    private double[] stoneNoise = new double[256];
    private MapGenBase caveGenerator = new LKMapGenOutlandsCaves();
    private BiomeGenBase[] biomesForGeneration;

    public LKChunkProviderOutlands(World world, long seed) {
        this.worldObj = world;
        this.rand = new Random(seed);
        this.noiseGen1 = new NoiseGeneratorOctaves(rand, 16);
        this.noiseGen2 = new NoiseGeneratorOctaves(rand, 16);
        this.noiseGen3 = new NoiseGeneratorOctaves(rand, 8);
        this.noiseGen4 = new NoiseGeneratorOctaves(rand, 4);
        this.noiseGen5 = new NoiseGeneratorOctaves(rand, 10);
        this.noiseGen6 = new NoiseGeneratorOctaves(rand, 16);
    }

    public void generateTerrain(int chunkX, int chunkZ, Block[] blocks) {
        byte b0 = 4;
        int height = 33;
        int xSize = b0 + 1;
        int zSize = b0 + 1;
        biomesForGeneration = worldObj.getWorldChunkManager().getBiomesForGeneration(biomesForGeneration, chunkX * 4 - 2, chunkZ * 4 - 2, xSize + 5, zSize + 5);
        noiseField = initializeNoiseField(noiseField, chunkX * b0, 0, chunkZ * b0, xSize, height, zSize);

        for (int x = 0; x < b0; x++) for (int z = 0; z < b0; z++) for (int y = 0; y < 32; y++) {
            double d0 = 0.125D;
            double n0 = noiseField[((x + 0) * zSize + z + 0) * height + y + 0];
            double n1 = noiseField[((x + 0) * zSize + z + 1) * height + y + 0];
            double n2 = noiseField[((x + 1) * zSize + z + 0) * height + y + 0];
            double n3 = noiseField[((x + 1) * zSize + z + 1) * height + y + 0];
            double d1 = (noiseField[((x + 0) * zSize + z + 0) * height + y + 1] - n0) * d0;
            double d2 = (noiseField[((x + 0) * zSize + z + 1) * height + y + 1] - n1) * d0;
            double d3 = (noiseField[((x + 1) * zSize + z + 0) * height + y + 1] - n2) * d0;
            double d4 = (noiseField[((x + 1) * zSize + z + 1) * height + y + 1] - n3) * d0;

            for (int h = 0; h < 8; h++) {
                double d5 = 0.25D;
                double n4 = n0;
                double n5 = n1;
                double d6 = (n2 - n0) * d5;
                double d7 = (n3 - n1) * d5;
                for (int x1 = 0; x1 < 4; x1++) {
                    int index = (x1 + x * 4) * 16 * 256 + (z * 4) * 256 + (y * 8 + h);
                    double d8 = 0.25D;
                    double n6 = n4;
                    double d9 = (n5 - n4) * d8;
                    for (int z1 = 0; z1 < 4; z1++) {
                        if ((n6 += d9) > 0.0D) blocks[index] = Blocks.stone;
                        else blocks[index] = Blocks.air;
                        index += 256;
                    }
                    n4 += d6;
                    n5 += d7;
                }
                n0 += d1;
                n1 += d2;
                n2 += d3;
                n3 += d4;
            }
        }
    }

    public void replaceBlocksForBiome(int chunkX, int chunkZ, Block[] blocks, BiomeGenBase[] biomes) {
        double[] noise = noiseGen4.generateNoiseOctaves(stoneNoise, chunkX * 16, chunkZ * 16, 0, 16, 16, 1, 0.0625D, 0.0625D, 0.0625D);
    
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                BiomeGenBase biome = biomes[z + x * 16];
                int depth = (int) (noise[x + z * 16] / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
                int currentDepth = -1;
                Block top = biome.topBlock != null ? biome.topBlock : Blocks.grass;
                Block filler = biome.fillerBlock != null ? biome.fillerBlock : Blocks.dirt;

                for (int y = 127; y >= 0; --y) {
                    int index = (z * 16 + x) * 128 + y;
                
                    if (y <= rand.nextInt(5)) {
                        blocks[index] = Blocks.bedrock;
                    } else {
                        Block block = blocks[index];
                    
                        if (block == Blocks.air) {
                            currentDepth = -1;
                        } else if (block == Blocks.stone) {
                            if (currentDepth == -1) {
                                if (depth <= 0) {
                                    top = Blocks.air;
                                    filler = Blocks.stone;
                                } else if (y >= 59 && y <= 65) {
                                    top = biome.topBlock != null ? biome.topBlock : Blocks.grass;
                                    filler = biome.fillerBlock != null ? biome.fillerBlock : Blocks.dirt;
                                }
                            
                                if (y < 63 && top == Blocks.air) {
                                    top = Blocks.water;
                                }
                            
                                currentDepth = depth;
                                blocks[index] = y >= 62 ? top : filler;
                            } else if (currentDepth > 0) {
                                --currentDepth;
                                blocks[index] = filler;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public Chunk provideChunk(int chunkX, int chunkZ) {
        rand.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
        Block[] blocks = new Block[65536];
        generateTerrain(chunkX, chunkZ, blocks);
        biomesForGeneration = worldObj.getWorldChunkManager().loadBlockGeneratorData(biomesForGeneration, chunkX * 16, chunkZ * 16, 16, 16);
        replaceBlocksForBiome(chunkX, chunkZ, blocks, biomesForGeneration);
        caveGenerator.func_151539_a(this, worldObj, chunkX, chunkZ, blocks);

        Chunk chunk = new Chunk(worldObj, blocks, chunkX, chunkZ);
        byte[] biomeArray = chunk.getBiomeArray();
        for (int i = 0; i < biomeArray.length; i++) biomeArray[i] = (byte)biomesForGeneration[i].biomeID;
        chunk.generateSkylightMap();
        return chunk;
    }

    private double[] initializeNoiseField(double[] noise, int x, int y, int z, int xSize, int ySize, int zSize) {
        if (noise == null) noise = new double[xSize * ySize * zSize];
        double scale = 684.412D;
        double[] noise5 = this.noiseGen5.generateNoiseOctaves(null, x, z, xSize, zSize, 1.121D, 1.121D, 0.5D);
        double[] noise6 = this.noiseGen6.generateNoiseOctaves(null, x, z, xSize, zSize, 200.0D, 200.0D, 0.5D);
        double[] noise3 = this.noiseGen3.generateNoiseOctaves(null, x, y, z, xSize, ySize, zSize, scale / 80.0D, scale / 160.0D, scale / 80.0D);
        double[] noise1 = this.noiseGen1.generateNoiseOctaves(null, x, y, z, xSize, ySize, zSize, scale, scale, scale);
        double[] noise2 = this.noiseGen2.generateNoiseOctaves(null, x, y, z, xSize, ySize, zSize, scale, scale, scale);

        int index = 0;
        for (int i = 0; i < xSize; i++) for (int k = 0; k < zSize; k++) {
            double height = 0.5D;
            double val = (noise5[index] / 10.0D + 1.0D) / 2.0D;
            double finalVal = val < 0 ? noise1[index] / 512.0D : val > 1 ? noise2[index] / 512.0D : noise1[index] / 512.0D + (noise2[index] / 512.0D - noise1[index] / 512.0D) * val;
            finalVal = finalVal - 8.0D + noise6[index] / 8000.0D * 3.0D;
            for (int j = 0; j < ySize; j++) {
                double density = (j - 64) / 64.0D * 8.0D;
                density = density > 0 ? density * 4 : density;
                density += finalVal;
                noise[index++] = density;
            }
        }
        return noise;
    }

    @Override
    public void populate(IChunkProvider provider, int chunkX, int chunkZ) {
        BlockSand.fallInstantly = true;
        int x = chunkX * 16;
        int z = chunkZ * 16;

        rand.setSeed(worldObj.getSeed());
        long a = rand.nextLong() / 2L * 2L + 1L;
        long b = rand.nextLong() / 2L * 2L + 1L;
        rand.setSeed(chunkX * a + chunkZ * b ^ worldObj.getSeed());

        BiomeGenBase biome = worldObj.getBiomeGenForCoords(x + 8, z + 8);

        if (rand.nextInt(4) == 0) {
            int lx = x + rand.nextInt(16) + 8;
            int lz = z + rand.nextInt(16) + 8;
            int ly = worldObj.getHeightValue(lx, lz) - 1;
            if (ly > 0) new LKWorldGenOutlandsLakes(true).generate(worldObj, rand, lx, ly, lz);
        }

        if (biome == LKOutlandsBiome.outlands || biome == LKOutlandsBiome.outlandsMountains) {
            if (rand.nextInt(100) == 0) {
                int mx = x + rand.nextInt(16) + 8;
                int mz = z + rand.nextInt(16) + 8;
                int my = worldObj.getHeightValue(mx, mz) + 1;
                new LKWorldGenTreasureMound().generate(worldObj, rand, mx, my, mz);
            }
        }

        biome.decorate(worldObj, rand, x, z);
        BlockSand.fallInstantly = false;
    }

    @Override public Chunk loadChunk(int cx, int cz) { return provideChunk(cx, cz); }
    @Override public boolean chunkExists(int cx, int cz) { return true; }
    @Override public boolean saveChunks(boolean b, IProgressUpdate p) { return true; }
    @Override public void saveExtraData() {}
    @Override public boolean unloadQueuedChunks() { return false; }
    @Override public boolean canSave() { return true; }
    @Override public String makeString() { return "RandomOutlandsLevelSource"; }
    @Override public List getPossibleCreatures(EnumCreatureType t, int x, int y, int z) {
        BiomeGenBase b = worldObj.getBiomeGenForCoords(x, z);
        return b == null ? null : b.getSpawnableList(t);
    }
    @Override public ChunkPosition func_147416_a(World w, String s, int x, int y, int z) { return null; }
    @Override public int getLoadedChunkCount() { return 0; }
    @Override public void recreateStructures(int cx, int cz) {}
}
