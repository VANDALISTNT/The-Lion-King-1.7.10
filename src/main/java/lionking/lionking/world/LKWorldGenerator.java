package lionking.world;

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import java.util.Random;
import lionking.mod_LionKing;
import lionking.biome.LKPrideLandsBiome;
import lionking.biome.LKOutlandsBiome;

public class LKWorldGenerator implements IWorldGenerator {

    private static final int BOOTH_CHANCE = 5;        
    private static final int BOOTH_LIMIT = 512;       
    private static final boolean RANDOM_BOOTHS = true; 

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world,
                         IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {

        int x = chunkX * 16;
        int z = chunkZ * 16;

        switch (world.provider.dimensionId) {
            case 0:  // Overworld
                generateOverworld(world, random, x, z);
                break;
            case 2:  // Pride Lands
                generatePrideLands(world, random, x, z);
                break;
            case 3:  // Outlands 
            case 7:
                generateOutlands(world, random, x, z);
                break;
            case 4:  // Upendi (idUpendi = 4)
                generateUpendi(world, random, x, z);
                break;
        }
    }

    private void generateOverworld(World world, Random random, int x, int z) {
        generateTicketBooth(world, random, x, z);
    //в Overworld
        generateOre(world, random, x, z, mod_LionKing.oreSilver,   4,  8, 10,  0, 50);
        generateOre(world, random, x, z, mod_LionKing.orePeacock, 2,  4,  5,  0, 30);

    }

    // ========================================
    // Pride Lands
    // ========================================
    private void generatePrideLands(World world, Random random, int x, int z) {
        if (random.nextInt(100) < 2) {
            int posX = x + random.nextInt(16);
            int posZ = z + random.nextInt(16);
            int posY = world.getHeightValue(posX, posZ);
            if (world.getBlock(posX, posY - 1, posZ).isNormalCube()) {
                world.setBlock(posX, posY, posZ, mod_LionKing.starAltar, 0, 2);
            }
        }

        generateOre(world, random, x, z, mod_LionKing.pridestone, 10, 20, 20, 0, 128);
        generateOre(world, random, x, z, mod_LionKing.prideCoal,   8, 16, 15, 0, 64);

        generateSurface(world, random, x, z, mod_LionKing.sapling,      10, 60, 90);
        generateSurface(world, random, x, z, mod_LionKing.whiteFlower,  20, 60, 100);
    }

    // ========================================
    // Outlands
    // ========================================
    private void generateOutlands(World world, Random random, int x, int z) {
        if (random.nextInt(100) < 3) {
            int posX = x + random.nextInt(16);
            int posZ = z + random.nextInt(16);
            int posY = world.getHeightValue(posX, posZ);
            if (world.getBlock(posX, posY - 1, posZ).isNormalCube()) {
                world.setBlock(posX, posY, posZ, mod_LionKing.outlandsAltar, 0, 2);
            }
        }

        generateOre(world, random, x, z, mod_LionKing.outsand, 15, 30, 30, 0, 256);
        generateSurface(world, random, x, z, mod_LionKing.outshroom,        10, 50, 80);
        generateSurface(world, random, x, z, mod_LionKing.outshroomGlowing,  5, 50, 80);
    }

    // ========================================
    // Upendi
    // ========================================
    private void generateUpendi(World world, Random random, int x, int z) {
        generateSurface(world, random, x, z, mod_LionKing.mangoSapling, 15, 60, 90);
        generateSurface(world, random, x, z, mod_LionKing.blueFlower, 25, 60, 100);
    }

    private void generateOre(World world, Random random, int x, int z, Block block,
                             int minVein, int maxVein, int chances, int minY, int maxY) {
        for (int i = 0; i < chances; i++) {
            int posX = x + random.nextInt(16);
            int posY = minY + random.nextInt(maxY - minY);
            int posZ = z + random.nextInt(16);
            new WorldGenMinable(block, minVein + random.nextInt(maxVein - minVein + 1))
                .generate(world, random, posX, posY, posZ);
        }
    }

    private void generateSurface(World world, Random random, int x, int z, Block block,
                                 int chances, int minY, int maxY) {
        for (int i = 0; i < chances; i++) {
            int posX = x + random.nextInt(16);
            int posZ = z + random.nextInt(16);
            int posY = world.getHeightValue(posX, posZ);

            if (posY >= minY && posY <= maxY &&
                world.getBlock(posX, posY - 1, posZ).isNormalCube()) {
                world.setBlock(posX, posY, posZ, block, 0, 2);
            }
        }
    }

    private void generateTicketBooth(World world, Random random, int x, int z) {
        if (!RANDOM_BOOTHS || random.nextInt(100) >= BOOTH_CHANCE) {
            return;
        }

        int posX = x + random.nextInt(16);
        int posZ = z + random.nextInt(16);
        int posY = world.getHeightValue(posX, posZ);

        if (posY <= 0 || !world.getBlock(posX, posY - 1, posZ).isNormalCube()) {
            return;
        }
        if (!checkBoothDistance(world, posX, posY, posZ)) {
            return;
        }

        new LKWorldGenTicketBooth().generate(world, random, posX, posY, posZ);
    }

    private boolean checkBoothDistance(World world, int x, int y, int z) {
        int limit = BOOTH_LIMIT;
        for (int dx = -limit; dx <= limit; dx += 64) {
            for (int dz = -limit; dz <= limit; dz += 64) {
                if (world.getBlock(x + dx, y, z + dz) == Blocks.wall_sign ||
                    world.getBlock(x + dx, y + 1, z + dz) == Blocks.wall_sign) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void registerBiomeFix() {
        MinecraftForge.EVENT_BUS.register(new Object() {
            @SubscribeEvent
            public void removeTLKBiomesFromOverworld(WorldEvent.Load event) {
                if (event.world.provider.dimensionId == 0) {
                    event.world.getWorldChunkManager().allowedBiomes.removeIf(biome ->
                        biome instanceof LKPrideLandsBiome ||
                        biome instanceof LKOutlandsBiome ||
                        "Upendi".equals(biome.biomeName)
                    );
                }
            }
        });
    }
    
    public static class BiomeFixer {
        @SubscribeEvent
        public void removeTLKBiomesFromOverworld(WorldEvent.Load event) {
            if (event.world.provider.dimensionId == 0) {
                event.world.getWorldChunkManager().allowedBiomes.removeIf(biome ->
                    biome instanceof LKPrideLandsBiome ||
                    biome instanceof LKOutlandsBiome ||
                    "Upendi".equals(biome.biomeName)
                );
            }
        }
    }
}
