package lionking.world;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.entity.EnumCreatureType;
import java.util.Random;
import java.util.List;

import lionking.biome.*;
import lionking.world.LKMapGenCaves;
import lionking.world.LKWorldGenRafiki;
import lionking.world.LKWorldGenTimonAndPumbaa;
import lionking.world.LKWorldGenDungeons;
import lionking.mod_LionKing;

public class LKChunkProviderPrideLands implements IChunkProvider {

    private Random rand;
    private NoiseGeneratorOctaves noiseGen1, noiseGen2, noiseGen3, noiseGen4, noiseGen5, noiseGen6;
    private World worldObj;
    private double[] noiseField;
    private double[] stoneNoise = new double[256];
    private MapGenBase caveGenerator = new LKMapGenCaves();
    private BiomeGenBase[] biomesForGeneration;
    private double[] noise1, noise2, noise3, noise5, noise6;
    private float[] noiseFloatArray;

    public LKChunkProviderPrideLands(World world, long seed) {
        this.worldObj = world;
        this.rand = new Random(seed);
        noiseGen1 = new NoiseGeneratorOctaves(rand, 16);
        noiseGen2 = new NoiseGeneratorOctaves(rand, 16);
        noiseGen3 = new NoiseGeneratorOctaves(rand, 8);
        noiseGen4 = new NoiseGeneratorOctaves(rand, 4);
        noiseGen5 = new NoiseGeneratorOctaves(rand, 10);
        noiseGen6 = new NoiseGeneratorOctaves(rand, 16);
    }

    public void generateTerrain(int chunkX, int chunkZ, Block[] blockData) {
        byte seaLevel = 4;
        byte topLevel = 32;
        int heightShift = seaLevel + 1;
        byte noiseShift = 17;
        int size = 5;

        biomesForGeneration = worldObj.getWorldChunkManager().getBiomesForGeneration(biomesForGeneration, chunkX * 4 - 2, chunkZ * 4 - 2, 10, 10);
        noiseField = initializeNoiseField(noiseField, chunkX * 4, 0, chunkZ * 4, 5, 17, 5);

        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                for (int k = 0; k < 16; ++k) {
                    double d0 = 0.125D;
                    double d1 = noiseField[((i + 0) * size + (j + 0)) * noiseShift + (k + 0)];
                    double d2 = noiseField[((i + 0) * size + (j + 1)) * noiseShift + (k + 0)];
                    double d3 = noiseField[((i + 1) * size + (j + 0)) * noiseShift + (k + 0)];
                    double d4 = noiseField[((i + 1) * size + (j + 1)) * noiseShift + (k + 0)];
                    double d5 = (noiseField[((i + 0) * size + (j + 0)) * noiseShift + (k + 1)] - d1) * d0;
                    double d6 = (noiseField[((i + 0) * size + (j + 1)) * noiseShift + (k + 1)] - d2) * d0;
                    double d7 = (noiseField[((i + 1) * size + (j + 0)) * noiseShift + (k + 1)] - d3) * d0;
                    double d8 = (noiseField[((i + 1) * size + (j + 1)) * noiseShift + (k + 1)] - d4) * d0;

                    for (int l = 0; l < 8; ++l) {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;

                        for (int i1 = 0; i1 < 4; ++i1) {
                            int j1 = i1 + i * 4 << 11 | 0 + j * 4 << 7 | k * 8 + l;
                            short s = 128;
                            j1 -= s;
                            double d14 = 0.25D;
                            double d15 = (d11 - d10) * d14;
                            double d16 = d10 - d15;

                            for (int k1 = 0; k1 < 4; ++k1) {
                                if ((d16 += d15) > 0.0D) {
                                    blockData[j1 += s] = Blocks.stone;
                                } else if (k * 8 + l < topLevel) {
                                    blockData[j1 += s] = Blocks.water;
                                } else {
                                    blockData[j1 += s] = null;
                                }
                            }

                            d10 += d12;
                            d11 += d13;
                        }

                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }
    }

    public void replaceBlocksForBiome(int chunkX, int chunkZ, Block[] blockData, BiomeGenBase[] biomes) {
        byte seaLevel = 63;
        double d0 = 0.03125D;
        stoneNoise = noiseGen4.generateNoiseOctaves(stoneNoise, chunkX * 16, chunkZ * 16, 0, 16, 16, 1, d0 * 2.0D, d0 * 2.0D, d0 * 2.0D);

        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                BiomeGenBase biome = biomes[j + i * 16];
                float temperature = biome.getFloatTemperature(chunkX * 16 + i, 64, chunkZ * 16 + j);
                int k = (int) (stoneNoise[i + j * 16] / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
                int l = -1;
                Block block = biome.topBlock;
                Block block1 = biome.fillerBlock;

                for (int m = 127; m >= 0; --m) {
                    int n = (j * 16 + i) * 128 + m;

                    if (m <= 0 + rand.nextInt(5)) {
                        blockData[n] = Blocks.bedrock;
                    } else {
                        Block block2 = blockData[n];

                        if (block2 == null) {
                            l = -1;
                        } else if (block2 == Blocks.stone) {
                            blockData[n] = mod_LionKing.pridestone;
                            if (l == -1) {
                                if (k <= 0) {
                                    block = null;
                                    block1 = mod_LionKing.pridestone;
                                } else if (m >= seaLevel - 4 && m <= seaLevel + 1) {
                                    block = biome.topBlock;
                                    block1 = biome.fillerBlock;
                                }

                                if (m < seaLevel && block == null) {
                                    if (temperature < 0.15F && m >= seaLevel - 3) {
                                        block = Blocks.ice;
                                    } else {
                                        block = Blocks.water;
                                    }
                                }

                                l = k;

                                if (m >= seaLevel - 1) {
                                    blockData[n] = block;
                                } else {
                                    blockData[n] = block1;
                                }
                            } else if (l > 0) {
                                --l;
                                blockData[n] = block1;

                                if (l == 0 && block1 == Blocks.sand) {
                                    l = rand.nextInt(4);
                                    block1 = Blocks.sandstone;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private double[] initializeNoiseField(double[] noise, int x, int y, int z, int width, int height, int depth) {
        if (noise == null) {
            noise = new double[width * height * depth];
        }

        double d0 = 684.412D;
        double d1 = 684.412D;
        noise5 = noiseGen5.generateNoiseOctaves(noise5, x, z, width, depth, 1.121D, 1.121D, 0.5D);
        noise6 = noiseGen6.generateNoiseOctaves(noise6, x, z, width, depth, 200.0D, 200.0D, 0.5D);
        d0 *= 2.0D;
        noise1 = noiseGen3.generateNoiseOctaves(noise1, x, y, z, width, height, depth, d0 / 80.0D, d1 / 160.0D, d0 / 80.0D);
        noise2 = noiseGen1.generateNoiseOctaves(noise2, x, y, z, width, height, depth, d0, d1, d0);
        noise3 = noiseGen2.generateNoiseOctaves(noise3, x, y, z, width, height, depth, d0, d1, d0);
        int k1 = 0;
        int l1 = 0;
        int i2 = 0;

        for (int j2 = 0; j2 < width; ++j2) {
            for (int k2 = 0; k2 < depth; ++k2) {
                double d2 = (noise5[l1] + 256.0D) / 512.0D;

                if (d2 > 1.0D) {
                    d2 = 1.0D;
                }

                double d3 = noise6[l1] / 8000.0D;

                if (d3 < 0.0D) {
                    d3 = -d3 * 0.3D;
                }

                d3 = d3 * 3.0D - 2.0D;
                float f = (j2 + x) / 1.0F;
                float f1 = (k2 + z) / 1.0F;
                float f2 = 100.0F - MathHelper.sqrt_float(f * f + f1 * f1) * 8.0F;

                if (f2 > 80.0F) {
                    f2 = 80.0F;
                }

                if (f2 < -100.0F) {
                    f2 = -100.0F;
                }

                if (d3 > 1.0D) {
                    d3 = 1.0D;
                }

                d3 /= 8.0D;
                d3 = 0.0D;

                if (d2 < 0.0D) {
                    d2 = 0.0D;
                }

                d2 += 0.5D;
                d3 = d3 * height / 16.0D;
                ++l1;
                double d4 = height / 2.0D;

                for (int l2 = 0; l2 < height; ++l2) {
                    double d5 = 0.0D;
                    double d6 = (l2 - d4) * 8.0D / d2;

                    if (d6 < 0.0D) {
                        d6 *= -1.0D;
                    }

                    double d7 = noise2[i2] / 512.0D;
                    double d8 = noise3[i2] / 512.0D;
                    double d9 = (noise1[i2] / 10.0D + 1.0D) / 2.0D;

                    if (d9 < 0.0D) {
                        d5 = d7;
                    } else if (d9 > 1.0D) {
                        d5 = d8;
                    } else {
                        d5 = d7 + (d8 - d7) * d9;
                    }

                    d5 -= d6;

                    if (l2 > height - 4) {
                        double d10 = (l2 - (height - 4)) / 3.0F;
                        d5 = d5 * (1.0D - d10) + -10.0D * d10;
                    }

                    d5 += f2 / 512.0D;
                    noise[k1] = d5;
                    ++k1;
                    ++i2;
                }
            }
        }

        return noise;
    }

    @Override
    public Chunk provideChunk(int chunkX, int chunkZ) {
        rand.setSeed(chunkX * 341873128712L + chunkZ * 132897987541L);
        Block[] blockData = new Block[32768];
        generateTerrain(chunkX, chunkZ, blockData);
        biomesForGeneration = worldObj.getWorldChunkManager().loadBlockGeneratorData(biomesForGeneration, chunkX * 16, chunkZ * 16, 16, 16);
        replaceBlocksForBiome(chunkX, chunkZ, blockData, biomesForGeneration);
        caveGenerator.func_151539_a(this, worldObj, chunkX, chunkZ, blockData);
        Chunk chunk = new Chunk(worldObj, blockData, chunkX, chunkZ);
        byte[] abyte1 = chunk.getBiomeArray();

        for (int k = 0; k < abyte1.length; ++k) {
            abyte1[k] = (byte)biomesForGeneration[k].biomeID;
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public void populate(IChunkProvider provider, int chunkX, int chunkZ) {
        BlockSand.fallInstantly = true;
        int x = chunkX * 16;
        int z = chunkZ * 16;
        BiomeGenBase biome = worldObj.getBiomeGenForCoords(x + 8, z + 8);
        rand.setSeed(worldObj.getSeed());
        long a = rand.nextLong() / 2L * 2L + 1L;
        long b = rand.nextLong() / 2L * 2L + 1L;
        rand.setSeed(chunkX * a + chunkZ * b ^ worldObj.getSeed());

        if (chunkX == 0 && chunkZ == 0) {
            new LKWorldGenTicketBooth().generate(worldObj, rand, 0, 64, 0);
        }

        for (int i = 0; i < 20; i++) {
            int rx = x + rand.nextInt(16);
            int rz = z + rand.nextInt(16);
            int ry = rand.nextInt(108) + 10;
            (new LKWorldGenMinable(Block.getIdFromBlock(mod_LionKing.prideCoal), 16, Block.getIdFromBlock(mod_LionKing.pridestone))).generate(worldObj, rand, rx, ry, rz);
        }

        for (int i = 0; i < 8; i++) {
            int rx = x + rand.nextInt(16);
            int rz = z + rand.nextInt(16);
            int ry = rand.nextInt(32) + 10;
            (new LKWorldGenMinable(Block.getIdFromBlock(mod_LionKing.oreSilver), 8, Block.getIdFromBlock(mod_LionKing.pridestone))).generate(worldObj, rand, rx, ry, rz);
        }

        for (int i = 0; i < 1; i++) {
            int rx = x + rand.nextInt(16);
            int rz = z + rand.nextInt(16);
            int ry = rand.nextInt(18) + 10;
            (new LKWorldGenMinable(Block.getIdFromBlock(mod_LionKing.termite), 32, Block.getIdFromBlock(mod_LionKing.pridestone))).generate(worldObj, rand, rx, ry, rz);
        }

        if (rand.nextInt(4) == 0) {
            int lx = x + rand.nextInt(16) + 8;
            int lz = z + rand.nextInt(16) + 8;
            int ly = worldObj.getHeightValue(lx, lz) - 1;
            if (ly > 0) new WorldGenLakes(Blocks.water).generate(worldObj, rand, lx, ly, lz);
        }

        if (rand.nextInt(8) == 0) {
            int lx = x + rand.nextInt(16) + 8;
            int lz = z + rand.nextInt(16) + 8;
            int ly = worldObj.getHeightValue(lx, lz) - 1;
            if (ly > 0) new WorldGenLakes(Blocks.lava).generate(worldObj, rand, lx, ly, lz);
        }

        if (rand.nextInt(200) == 0) {
            int rx = x + rand.nextInt(16) + 8;
            int rz = z + rand.nextInt(16) + 8;
            int ry = worldObj.getHeightValue(rx, rz);
            new LKWorldGenRafiki().generate(worldObj, rand, rx, ry, rz);
        }

        for (int i = 0; i < 10; i++) {
            int tx = x + rand.nextInt(16) + 8;
            int tz = z + rand.nextInt(16) + 8;
            int ty = worldObj.getHeightValue(tx, tz);
            int id = worldObj.getBiomeGenForCoords(tx, tz).biomeID;

            if (ty > 60 && (id == mod_LionKing.idBiomeSavannah ||
                           id == mod_LionKing.idBiomeGrasslandSavannah ||
                           id == mod_LionKing.idBiomeWoodedSavannah)) {
                new LKWorldGenTimonAndPumbaa(true).generate(worldObj, rand, tx, ty - 1, tz);
            }
        }

        for (int i = 0; i < 10; i++) {
            int dx = x + rand.nextInt(16) + 8;
            int dz = z + rand.nextInt(16) + 8;
            int dy = rand.nextInt(128);
            new LKWorldGenDungeons().generate(worldObj, rand, dx, dy, dz);
        }

        if (biome instanceof LKPrideLandsBiome) {
            ((LKPrideLandsBiome)biome).decorate(worldObj, rand, x, z);
        }

        BlockSand.fallInstantly = false;
    }

    @Override public Chunk loadChunk(int cx, int cz) { return provideChunk(cx, cz); }
    @Override public boolean chunkExists(int cx, int cz) { return true; }
    @Override public boolean saveChunks(boolean b, IProgressUpdate p) { return true; }
    @Override public void saveExtraData() {}
    @Override public boolean unloadQueuedChunks() { return false; }
    @Override public boolean canSave() { return true; }
    @Override public String makeString() { return "RandomPrideLandsLevelSource"; }
    @Override public List getPossibleCreatures(EnumCreatureType t, int x, int y, int z) {
        BiomeGenBase b = worldObj.getBiomeGenForCoords(x, z);
        return b == null ? null : b.getSpawnableList(t);
    }
    @Override public ChunkPosition func_147416_a(World w, String s, int x, int y, int z) { return null; }
    @Override public int getLoadedChunkCount() { return 0; }
    @Override public void recreateStructures(int cx, int cz) {}
}
