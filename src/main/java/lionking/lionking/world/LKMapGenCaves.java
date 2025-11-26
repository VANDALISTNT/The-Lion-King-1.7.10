package lionking.world;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.util.MathHelper;
import net.minecraft.init.Blocks;

import lionking.mod_LionKing;

import java.util.Random;

public class LKMapGenCaves extends MapGenBase {
    private static final Block WATER_MOVING = Blocks.flowing_water;
    private static final Block WATER_STILL = Blocks.water;
    private static final Block GRASS = Blocks.grass;
    private static final Block DIRT = Blocks.dirt;
    private static final Block SAND = Blocks.sand;
    private static final Block LAVA_MOVING = Blocks.flowing_lava;
    private static final Block PRIDESTONE = mod_LionKing.pridestone;

    protected void generateLargeCaveNode(long seed, int chunkX, int chunkZ, Block[] blockData, double x, double y, double z) {
        generateCaveNode(seed, chunkX, chunkZ, blockData, x, y, z, 1.0F + rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
    }

    protected void generateCaveNode(long seed, int chunkX, int chunkZ, Block[] blockData, double x, double y, double z,
                                    float radius, float yaw, float pitch, int startStep, int totalSteps, double heightScale) {
        double chunkCenterX = chunkX * 16 + 8;
        double chunkCenterZ = chunkZ * 16 + 8;
        float yawChange = 0.0F;
        float pitchChange = 0.0F;
        Random random = new Random(seed);

        if (totalSteps <= 0) {
            int rangeBlocks = range * 16 - 16;
            totalSteps = rangeBlocks - random.nextInt(rangeBlocks / 4);
        }

        boolean isMainCave = startStep == -1;
        if (isMainCave) {
            startStep = totalSteps / 2;
        }

        int branchPoint = random.nextInt(totalSteps / 2) + totalSteps / 4;
        boolean reducePitch = random.nextInt(6) == 0;

        for (; startStep < totalSteps; startStep++) {
            double width = 1.5D + MathHelper.sin((float) startStep * (float) Math.PI / totalSteps) * radius;
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

            if (!isMainCave && startStep == branchPoint && radius > 1.0F && totalSteps > 0) {
                generateCaveNode(random.nextLong(), chunkX, chunkZ, blockData, x, y, z,
                        random.nextFloat() * 0.5F + 0.5F, yaw - ((float) Math.PI / 2F), pitch / 3.0F, startStep, totalSteps, 1.0D);
                generateCaveNode(random.nextLong(), chunkX, chunkZ, blockData, x, y, z,
                        random.nextFloat() * 0.5F + 0.5F, yaw + ((float) Math.PI / 2F), pitch / 3.0F, startStep, totalSteps, 1.0D);
                return;
            }

            if (isMainCave || random.nextInt(4) != 0) {
                double dx = x - chunkCenterX;
                double dz = z - chunkCenterZ;
                double stepsLeft = totalSteps - startStep;
                double maxDistance = radius + 18.0F;

                if (dx * dx + dz * dz - stepsLeft * stepsLeft > maxDistance * maxDistance) {
                    return;
                }

                if (x >= chunkCenterX - 16.0D - width * 2.0D && z >= chunkCenterZ - 16.0D - width * 2.0D &&
                    x <= chunkCenterX + 16.0D + width * 2.0D && z <= chunkCenterZ + 16.0D + width * 2.0D) {
                    int minX = Math.max(MathHelper.floor_double(x - width) - chunkX * 16 - 1, 0);
                    int maxX = Math.min(MathHelper.floor_double(x + width) - chunkX * 16 + 1, 16);
                    int minY = Math.max(MathHelper.floor_double(y - height) - 1, 1);
                    int maxY = Math.min(MathHelper.floor_double(y + height) + 1, 120);
                    int minZ = Math.max(MathHelper.floor_double(z - width) - chunkZ * 16 - 1, 0);
                    int maxZ = Math.min(MathHelper.floor_double(z + width) - chunkZ * 16 + 1, 16);

                    if (!hasWater(blockData, minX, maxX, minY, maxY, minZ, maxZ)) {
                        carveCave(blockData, chunkX, chunkZ, x, y, z, minX, maxX, minY, maxY, minZ, maxZ, width, height);
                        if (isMainCave) {
                            break;
                        }
                    }
                }
            }
        }
    }

    private boolean hasWater(Block[] blockData, int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
        for (int x = minX; x < maxX; x++) {
            for (int z = minZ; z < maxZ; z++) {
                for (int y = maxY; y >= minY - 1; y--) {
                    int index = (x * 16 + z) * 128 + y;
                    if (y >= 0 && y < 128) {
                        Block block = blockData[index];
                        if (block == WATER_MOVING || block == WATER_STILL) {
                            return true;
                        }
                        if (x != minX && x != maxX - 1 && z != minZ && z != maxZ - 1 && y == minY - 1) {
                            y = minY;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void carveCave(Block[] blockData, int chunkX, int chunkZ, double x, double y, double z,
                           int minX, int maxX, int minY, int maxY, int minZ, int maxZ, double width, double height) {
        for (int localX = minX; localX < maxX; localX++) {
            double dx = (localX + chunkX * 16 + 0.5D - x) / width;
            for (int localZ = minZ; localZ < maxZ; localZ++) {
                double dz = (localZ + chunkZ * 16 + 0.5D - z) / width;
                if (dx * dx + dz * dz >= 1.0D) {
                    continue;
                }

                int index = (localX * 16 + localZ) * 128 + maxY;
                boolean grassFound = false;

                for (int localY = maxY - 1; localY >= minY; localY--) {
                    double dy = (localY + 0.5D - y) / height;
                    if (dy <= -0.7D || dx * dx + dy * dy + dz * dz >= 1.0D) {
                        index--;
                        continue;
                    }

                    Block block = blockData[index];
                    if (block == GRASS) {
                        grassFound = true;
                    }

                    if (block == PRIDESTONE || block == DIRT || block == GRASS || block == SAND) {
                        blockData[index] = localY < 10 ? LAVA_MOVING : Blocks.air;
                        if (grassFound && blockData[index - 1] == DIRT) {
                            Block topBlock = worldObj.getBiomeGenForCoords(localX + chunkX * 16, localZ + chunkZ * 16).topBlock;
                            blockData[index - 1] = (topBlock != null) ? topBlock : Blocks.grass;
                        }
                    }
                    index--;
                }
            }
        }
    }

    @Override
    public void func_151539_a(IChunkProvider chunkProvider, World world, int chunkX, int chunkZ, Block[] blockData) {
        int caveCount = rand.nextInt(rand.nextInt(rand.nextInt(40) + 1) + 1);
        if (rand.nextInt(15) != 0) {
            caveCount = 0;
        }

        for (int i = 0; i < caveCount; i++) {
            double x = chunkX * 16 + rand.nextInt(16);
            double y = rand.nextInt(rand.nextInt(120) + 8);
            double z = chunkZ * 16 + rand.nextInt(16);
            int caveNodes = 1;

            if (rand.nextInt(4) == 0) {
                generateLargeCaveNode(rand.nextLong(), chunkX, chunkZ, blockData, x, y, z);
                caveNodes += rand.nextInt(4);
            }

            for (int j = 0; j < caveNodes; j++) {
                float yaw = rand.nextFloat() * ((float) Math.PI * 2.0F);
                float pitch = (rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float radius = rand.nextFloat() * 2.0F + rand.nextFloat();
                if (rand.nextInt(10) == 0) {
                    radius *= rand.nextFloat() * rand.nextFloat() * 3.0F + 1.0F;
                }
                generateCaveNode(rand.nextLong(), chunkX, chunkZ, blockData, x, y, z, radius, yaw, pitch, 0, 0, 1.0D);
            }
        }
    }
}