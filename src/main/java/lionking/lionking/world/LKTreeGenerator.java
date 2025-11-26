package lionking.world;

import net.minecraft.world.gen.feature.WorldGenerator;
import java.util.Random;
import cpw.mods.fml.common.FMLLog;
import lionking.world.*;

public class LKTreeGenerator {
    /**
     * Returns a tree generator for the Upendi biome.
     * - 1/3 chance: Standard tree (LKWorldGenTrees).
     * - 1/8 chance: Huge rainforest tree (LKWorldGenHugeRainforestTrees).
     * - 7/12 chance: Rainforest tree (LKWorldGenRainforestTrees).
     */
    public static WorldGenerator getUpendiTreeGenerator(Random random) {
        WorldGenerator treeGen;
        if (random.nextInt(3) == 0) {
            treeGen = new LKWorldGenTrees(false);
            FMLLog.fine("Selected LKWorldGenTrees for Upendi biome");
        } else if (random.nextInt(8) == 0) {
            treeGen = new LKWorldGenHugeRainforestTrees(false);
            FMLLog.fine("Selected LKWorldGenHugeRainforestTrees for Upendi biome");
        } else {
            treeGen = new LKWorldGenRainforestTrees(false);
            FMLLog.fine("Selected LKWorldGenRainforestTrees for Upendi biome");
        }
        return treeGen;
    }

    /**
     * Returns a tree generator for the Rainforest biome.
     * - 1/3 chance: Standard tree (LKWorldGenTrees).
     * - 1/10 chance: Huge rainforest tree (LKWorldGenHugeRainforestTrees).
     * - 13/20 chance: Rainforest tree (LKWorldGenRainforestTrees).
     */
    public static WorldGenerator getRainforestTreeGenerator(Random random) {
        WorldGenerator treeGen;
        if (random.nextInt(3) == 0) {
            treeGen = new LKWorldGenTrees(false);
            FMLLog.fine("Selected LKWorldGenTrees for Rainforest biome");
        } else if (random.nextInt(10) == 0) {
            treeGen = new LKWorldGenHugeRainforestTrees(false);
            FMLLog.fine("Selected LKWorldGenHugeRainforestTrees for Rainforest biome");
        } else {
            treeGen = new LKWorldGenRainforestTrees(false);
            FMLLog.fine("Selected LKWorldGenRainforestTrees for Rainforest biome");
        }
        return treeGen;
    }
}
