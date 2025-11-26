package lionking.world;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.util.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import java.util.Random;

import lionking.mod_LionKing;
import lionking.biome.LKBiomeGenOutlandsRiver;
import cpw.mods.fml.common.FMLLog;

public class LKMapGenOutlandsCaves extends MapGenBase {
    private static final int CHUNK_SIZE_X = 16;
    private static final int CHUNK_SIZE_Z = 16;
    private static final int MAX_HEIGHT = 128;
    private static final int MIN_CAVE_HEIGHT = 8;
    private static final int MAX_CAVE_HEIGHT = 120;

    public LKMapGenOutlandsCaves() {
        FMLLog.info("Initialized LKMapGenOutlandsCaves");
    }

    protected void generateLargeCaveNode(long seed, int chunkX, int chunkZ, Block[] blocks, double x, double y, double z) {
        generateCaveNode(seed, chunkX, chunkZ, blocks, x, y, z, 1.0F + rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
    }

    protected void generateCaveNode(long seed, int chunkX, int chunkZ, Block[] blocks, double x, double y, double z, float radius, float yaw, float pitch, int start, int length, double heightScale) {
        if (mod_LionKing.pridestone == null) {
            FMLLog.severe("pridestone is null in LKMapGenOutlandsCaves, skipping cave generation at chunk x=%d, z=%d", chunkX, chunkZ);
            return;
        }

        double centerX = chunkX * CHUNK_SIZE_X + 8;
        double centerZ = chunkZ * CHUNK_SIZE_Z + 8;
        float yawChange = 0.0F;
        float pitchChange = 0.0F;
        Random random = new Random(seed);

        boolean isRiverBiome = worldObj.getBiomeGenForCoords((int) centerX, (int) centerZ) instanceof LKBiomeGenOutlandsRiver;

        if (length <= 0) {
            int rangeBlocks = range * CHUNK_SIZE_X - CHUNK_SIZE_X;
            length = rangeBlocks - random.nextInt(rangeBlocks / 4);
        }

        boolean isLargeCave = start == -1;
        if (isLargeCave) {
            start = length / 2;
        }

        int branchPoint = random.nextInt(length / 2) + length / 4;
        boolean reducePitch = random.nextInt(6) == 0;

        for (; start < length; start++) {
            double width = 1.5D + MathHelper.sin((float) start * (float) Math.PI / length) * radius;
            double height = width * heightScale;
            float cosPitch = MathHelper.cos(pitch);
            float sinPitch = MathHelper.sin(pitch);

            x += MathHelper.cos(yaw) * cosPitch;
            y += sinPitch;
            z += MathHelper.sin(yaw) * cosPitch;

            pitch *= reducePitch ? 0.92F : 0.7F;
            pitch += pitchChange * 0.1F;
            yaw += yawChange * 0.1F;

            pitchChange *= 0.9F;
            yawChange *= 0.75F;
            pitchChange += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
            yawChange += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;

            if (!isLargeCave && start == branchPoint && radius > 1.0F && length > 0) {
                generateCaveNode(random.nextLong(), chunkX, chunkZ, blocks, x, y, z, random.nextFloat() * 0.5F + 0.5F, yaw - (float) Math.PI / 2F, pitch / 3.0F, start, length, 1.0D);
                generateCaveNode(random.nextLong(), chunkX, chunkZ, blocks, x, y, z, random.nextFloat() * 0.5F + 0.5F, yaw + (float) Math.PI / 2F, pitch / 3.0F, start, length, 1.0D);
                return;
            }

            if (isLargeCave || random.nextInt(4) != 0) {
                double dx = x - centerX;
                double dz = z - centerZ;
                double remaining = length - start;
                double maxDist = radius + 18.0F;

                if (dx * dx + dz * dz - remaining * remaining > maxDist * maxDist) {
                    return;
                }

                if (x >= centerX - 16.0D - width * 2.0D && z >= centerZ - 16.0D - width * 2.0D &&
                    x <= centerX + 16.0D + width * 2.0D && z <= centerZ + 16.0D + width * 2.0D) {
                    int minX = Math.max(MathHelper.floor_double(x - width) - chunkX * CHUNK_SIZE_X - 1, 0);
                    int maxX = Math.min(MathHelper.floor_double(x + width) - chunkX * CHUNK_SIZE_X + 1, CHUNK_SIZE_X);
                    int minY = Math.max(MathHelper.floor_double(y - height) - 1, 1);
                    int maxY = Math.min(MathHelper.floor_double(y + height) + 1, MAX_HEIGHT - 8);
                    int minZ = Math.max(MathHelper.floor_double(z - width) - chunkZ * CHUNK_SIZE_Z - 1, 0);
                    int maxZ = Math.min(MathHelper.floor_double(z + width) - chunkZ * CHUNK_SIZE_Z + 1, CHUNK_SIZE_Z);

                    boolean hasWater = false;
                    for (int ix = minX; !hasWater && ix < maxX; ix++) {
                        for (int iz = minZ; !hasWater && iz < maxZ; iz++) {
                            for (int iy = maxY + 1; !hasWater && iy >= minY - 1; iy--) {
                                if (iy >= 0 && iy < MAX_HEIGHT) {
                                    int index = (ix * CHUNK_SIZE_Z + iz) * MAX_HEIGHT + iy;
                                    Block block = blocks[index];
                                    if (block == Blocks.water || block == Blocks.flowing_water) {
                                        hasWater = true;
                                        FMLLog.fine("Water detected in cave at x=%d, y=%d, z=%d, skipping generation", ix + chunkX * CHUNK_SIZE_X, iy, iz + chunkZ * CHUNK_SIZE_Z);
                                    }
                                    if (iy != minY - 1 && ix != minX && ix != maxX - 1 && iz != minZ && iz != maxZ - 1) {
                                        iy = minY;
                                    }
                                }
                            }
                        }
                    }

                    if (!hasWater) {
                        for (int ix = minX; ix < maxX; ix++) {
                            double dxNorm = ((ix + chunkX * CHUNK_SIZE_X) + 0.5D - x) / width;
                            for (int iz = minZ; iz < maxZ; iz++) {
                                double dzNorm = ((iz + chunkZ * CHUNK_SIZE_Z) + 0.5D - z) / width;
                                int topIndex = (ix * CHUNK_SIZE_Z + iz) * MAX_HEIGHT + maxY;
                                boolean foundSand = false;

                                if (dxNorm * dxNorm + dzNorm * dzNorm < 1.0D) {
                                    for (int iy = maxY - 1; iy >= minY; iy--) {
                                        double dyNorm = (iy + 0.5D - y) / height;
                                        if (dyNorm > -0.7D && dxNorm * dxNorm + dyNorm * dyNorm + dzNorm * dzNorm < 1.0D) {
                                            Block block = blocks[topIndex];
                                            if (block == Blocks.sand) {
                                                foundSand = true;
                                            }
                                            if (block == mod_LionKing.pridestone || block == Blocks.sand || block == Blocks.sandstone) {
                                                blocks[topIndex] = isRiverBiome && iy < 63 ? Blocks.flowing_water : (iy < 10 ? Blocks.flowing_lava : Blocks.air);
                                                if (foundSand && iy > 0 && blocks[topIndex - 1] == Blocks.sand) {
                                                    Block topBlock = worldObj.getBiomeGenForCoords(ix + chunkX * CHUNK_SIZE_X, iz + chunkZ * CHUNK_SIZE_Z).topBlock;
                                                    blocks[topIndex - 1] = (topBlock != null) ? topBlock : Blocks.sand;
                                                }
                                            }
                                        }
                                        topIndex--;
                                    }
                                }
                            }
                        }
                        if (isLargeCave) {
                            break;
                        }
                    }
                }
            }
        }
        FMLLog.fine("Generated cave at chunk x=%d, z=%d", chunkX, chunkZ);
    }

    @Override
    public void func_151539_a(IChunkProvider chunkProvider, World world, int chunkX, int chunkZ, Block[] blocks) {
        this.worldObj = world;
        this.rand = new Random(world.getSeed());
        long seedX = (rand.nextLong() / 2L) * 2L + 1L;
        long seedZ = (rand.nextLong() / 2L) * 2L + 1L;
        rand.setSeed((long) chunkX * seedX + (long) chunkZ * seedZ ^ world.getSeed());

        int caveCount = rand.nextInt(rand.nextInt(rand.nextInt(40) + 1) + 1);
        if (rand.nextInt(15) != 0) {
            caveCount = 0;
        }

        for (int i = 0; i < caveCount; i++) {
            double x = chunkX * CHUNK_SIZE_X + rand.nextInt(CHUNK_SIZE_X);
            double y = rand.nextInt(rand.nextInt(MAX_CAVE_HEIGHT - MIN_CAVE_HEIGHT) + MIN_CAVE_HEIGHT);
            double z = chunkZ * CHUNK_SIZE_Z + rand.nextInt(CHUNK_SIZE_Z);
            int branches = 1;

            if (rand.nextInt(4) == 0) {
                generateLargeCaveNode(rand.nextLong(), chunkX, chunkZ, blocks, x, y, z);
                branches += rand.nextInt(4);
            }

            for (int j = 0; j < branches; j++) {
                float yaw = rand.nextFloat() * (float) Math.PI * 2.0F;
                float pitch = (rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float radius = rand.nextFloat() * 2.0F + rand.nextFloat();
                if (rand.nextInt(10) == 0) {
                    radius *= rand.nextFloat() * rand.nextFloat() * 3.0F + 1.0F;
                }
                generateCaveNode(rand.nextLong(), chunkX, chunkZ, blocks, x, y, z, radius, yaw, pitch, 0, 0, 1.0D);
            }
        }
    }
}