package lionking.world;

import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import java.util.Random;

public class LKWorldGenTallFlowers extends WorldGenerator {
    private int metadata;

    public LKWorldGenTallFlowers(boolean flag, int flowerType) {
        super(flag);
        metadata = flowerType;
    }

    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {
        int successfulPlacements = 0;
        int maxFlowers = 10;
        int maxAttempts = 64;
        int maxHeight = 256;

        for (int attempt = 0; attempt < maxAttempts && successfulPlacements < maxFlowers; attempt++) {
            int x1 = x + random.nextInt(8) - random.nextInt(8);
            int y1 = y + random.nextInt(4) - random.nextInt(4);
            int z1 = z + random.nextInt(8) - random.nextInt(8);

            if (y1 >= 1 && y1 + 1 < maxHeight &&
                world.isAirBlock(x1, y1, z1) &&
                world.isAirBlock(x1, y1 + 1, z1) &&
                Blocks.double_plant.canPlaceBlockAt(world, x1, y1, z1)) {
                world.setBlock(x1, y1, z1, Blocks.double_plant, metadata, 2); // Lower half
                world.setBlock(x1, y1 + 1, z1, Blocks.double_plant, metadata | 8, 2); // Upper half
                successfulPlacements++;
            }
        }

        return successfulPlacements > 0;
    }
}