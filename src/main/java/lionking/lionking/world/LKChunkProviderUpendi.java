package lionking.world;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.entity.EnumCreatureType;
import java.util.Random;
import java.util.List;
import java.util.Arrays;

import lionking.biome.LKBiomeGenUpendi;

public class LKChunkProviderUpendi implements IChunkProvider {

    private static final int CHUNK_SIZE_X = 16;
    private static final int CHUNK_SIZE_Z = 16;
    private static final int CHUNK_HEIGHT = 128;
    private static final int SEA_LEVEL = 63;

    private Random rand;
    private NoiseGeneratorOctaves noiseGen1, noiseGen2, noiseGen3, noiseGen4, noiseGen5, noiseGen6;
    private World world;
    private double[] noiseField;
    private double[] stoneNoise = new double[256];
    private BiomeGenBase[] biomesForGeneration;
    private double[] noise1, noise2, noise3, noise5, noise6;
    private float[] noiseFloatArray;

    public LKChunkProviderUpendi(World world, long seed) {
        this.world = world;
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
        int height = 17;
        int xSize = b0 + 1;
        int zSize = b0 + 1;
        biomesForGeneration = world.getWorldChunkManager().getBiomesForGeneration(biomesForGeneration, chunkX * 4 - 2, chunkZ * 4 - 2, xSize + 5, zSize + 5);
        noiseField = initializeNoiseField(noiseField, chunkX * b0, 0, chunkZ * b0, xSize, height, zSize);

        for (int x = 0; x < b0; x++) for (int z = 0; z < b0; z++) for (int y = 0; y < 16; y++) {
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
                    int index = (x1 + x * 4) * 16 * 128 + (z * 4) * 128 + (y * 8 + h);
                    double d8 = 0.25D;
                    double n6 = n4;
                    double d9 = (n5 - n4) * d8;
                    for (int z1 = 0; z1 < 4; z1++) {
                        if ((n6 += d9) > 0.0D) blocks[index] = Blocks.stone;
                        else if (y * 8 + h < SEA_LEVEL) blocks[index] = Blocks.water;
                        else blocks[index] = Blocks.air;
                        index += 128;
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
        stoneNoise = noiseGen4.generateNoiseOctaves(stoneNoise, chunkX * 16, chunkZ * 16, 0, 16, 16, 1, 0.0625D, 0.0625D, 0.0625D);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                BiomeGenBase biome = biomes[z + x * 16];
                int depth = (int) (stoneNoise[x + z * 16] / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
                int currentDepth = -1;

                Block top = biome.topBlock != null ? biome.topBlock : Blocks.grass;
                Block filler = biome.fillerBlock != null ? biome.fillerBlock : Blocks.dirt;

                for (int y = 127; y >= 0; y--) {
                    int index = (z * 16 + x) * 128 + y;

                    if (y <= rand.nextInt(5)) {
                        blocks[index] = Blocks.bedrock;
                        continue;
                    }

                    Block block = blocks[index];

                    if (block == null || block == Blocks.air) {
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
                            blocks[index] = (y >= 62) ? top : filler;
                        } else if (currentDepth > 0) {
                            currentDepth--;
                            blocks[index] = filler;
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
        Arrays.fill(blocks, Blocks.air);
        generateTerrain(chunkX, chunkZ, blocks);
        biomesForGeneration = world.getWorldChunkManager().loadBlockGeneratorData(biomesForGeneration, chunkX * 16, chunkZ * 16, 16, 16);
        replaceBlocksForBiome(chunkX, chunkZ, blocks, biomesForGeneration);

        Chunk chunk = new Chunk(world, blocks, chunkX, chunkZ);
        byte[] biomeArray = chunk.getBiomeArray();
        for (int i = 0; i < biomeArray.length; i++) biomeArray[i] = (byte)biomesForGeneration[i].biomeID;
        chunk.generateSkylightMap();
        return chunk;
    }

    private double[] initializeNoiseField(double[] noise, int x, int y, int z, int xSize, int ySize, int zSize) {
        if (noise == null) noise = new double[xSize * ySize * zSize];
        if (noiseFloatArray == null) {
            noiseFloatArray = new float[25];
            for (int i = -2; i <= 2; i++) for (int j = -2; j <= 2; j++) {
                float val = 10.0F / MathHelper.sqrt_float(i * i + j * j + 0.2F);
                noiseFloatArray[i + 2 + (j + 2) * 5] = val;
            }
        }

        double scale1 = 684.412D;
        noise5 = noiseGen5.generateNoiseOctaves(noise5, x, z, xSize, zSize, 1.121D, 1.121D, 0.5D);
        noise6 = noiseGen6.generateNoiseOctaves(noise6, x, z, xSize, zSize, 200.0D, 200.0D, 0.5D);
        noise3 = noiseGen3.generateNoiseOctaves(noise3, x, y, z, xSize, ySize, zSize, scale1 / 80.0D, scale1 / 160.0D, scale1 / 80.0D);
        noise1 = noiseGen1.generateNoiseOctaves(noise1, x, y, z, xSize, ySize, zSize, scale1, scale1, scale1);
        noise2 = noiseGen2.generateNoiseOctaves(noise2, x, y, z, xSize, ySize, zSize, scale1, scale1, scale1);

        int index = 0;
        for (int i = 0; i < xSize; i++) for (int k = 0; k < zSize; k++) {
            float heightSum = 0.0F, baseSum = 0.0F, weightSum = 0.0F;
            BiomeGenBase baseBiome = biomesForGeneration[i + 2 + (k + 2) * (xSize + 5)];

            for (int di = -2; di <= 2; di++) for (int dk = -2; dk <= 2; dk++) {
                BiomeGenBase b = biomesForGeneration[i + di + 2 + (k + dk + 2) * (xSize + 5)];
                float h = b.rootHeight + b.heightVariation;
                float weight = noiseFloatArray[di + 2 + (dk + 2) * 5] / (b.rootHeight + 2.0F);
                if (b.rootHeight > baseBiome.rootHeight) weight /= 2;
                heightSum += h * weight;
                baseSum += b.rootHeight * weight;
                weightSum += weight;
            }

            heightSum /= weightSum;
            baseSum /= weightSum;
            heightSum = heightSum * 0.9F + 0.1F;
            baseSum = (baseSum * 4.0F - 1.0F) / 8.0F;

            double noise6Val = noise6[index] / 8000.0D;
            if (noise6Val < 0) noise6Val = -noise6Val * 0.3D;
            noise6Val = noise6Val * 3.0D - 2.0D;
            if (noise6Val < 0) { noise6Val /= 2; if (noise6Val < -1) noise6Val = -1; noise6Val /= 1.4D; noise6Val /= 2; }
            else { if (noise6Val > 1) noise6Val = 1; noise6Val /= 8; }

            index++;

            for (int j = 0; j < ySize; j++) {
                double val = baseSum + noise6Val * 0.2D;
                val = val * ySize / 16.0D;
                double adjusted = ySize / 2.0D + val * 4.0D;
                double heightDiff = ((double)j - adjusted) * 12.0D * 128.0D / 128.0D / heightSum;
                if (heightDiff < 0) heightDiff *= 4;

                double n1 = noise1[index - 1] / 512.0D;
                double n2 = noise2[index - 1] / 512.0D;
                double n3 = (noise3[index - 1] / 10.0D + 1.0D) / 2.0D;
                double finalVal = n3 < 0 ? n1 : n3 > 1 ? n2 : n1 + (n2 - n1) * n3;
                finalVal -= heightDiff;

                if (j > ySize - 4) {
                    double fade = (j - (ySize - 4)) / 3.0F;
                    finalVal = finalVal * (1.0D - fade) + -10.0D * fade;
                }
                noise[index - 1] = finalVal;
            }
        }
        return noise;
    }

    @Override
    public void populate(IChunkProvider provider, int chunkX, int chunkZ) {
        BlockSand.fallInstantly = true;
        int x = chunkX * 16;
        int z = chunkZ * 16;

        rand.setSeed(world.getSeed());
        long a = rand.nextLong() / 2L * 2L + 1L;
        long b = rand.nextLong() / 2L * 2L + 1L;
        rand.setSeed(chunkX * a + chunkZ * b ^ world.getSeed());

        if (rand.nextInt(10) == 0) {
            int lx = x + rand.nextInt(16) + 8;
            int lz = z + rand.nextInt(16) + 8;
            int ly = rand.nextInt(128);
            new WorldGenLakes(Blocks.water).generate(world, rand, lx, ly, lz);
        }

        BiomeGenBase biome = world.getBiomeGenForCoords(x + 8, z + 8);
        biome.decorate(world, rand, x, z);

        BlockSand.fallInstantly = false;
    }

    @Override public Chunk loadChunk(int cx, int cz) { return provideChunk(cx, cz); }
    @Override public boolean chunkExists(int cx, int cz) { return true; }
    @Override public boolean saveChunks(boolean b, IProgressUpdate p) { return true; }
    @Override public void saveExtraData() {}
    @Override public boolean unloadQueuedChunks() { return false; }
    @Override public boolean canSave() { return true; }
    @Override public String makeString() { return "RandomUpendiLevelSource"; }
    @Override public List getPossibleCreatures(EnumCreatureType t, int x, int y, int z) {
        BiomeGenBase b = world.getBiomeGenForCoords(x, z);
        return b == null ? null : b.getSpawnableList(t);
    }
    @Override public ChunkPosition func_147416_a(World w, String s, int x, int y, int z) { return null; }
    @Override public int getLoadedChunkCount() { return 0; }
    @Override public void recreateStructures(int cx, int cz) {}
}
