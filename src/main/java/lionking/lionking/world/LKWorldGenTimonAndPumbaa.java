package lionking.world;

import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenerator;
import java.util.Random;
import cpw.mods.fml.common.FMLLog;
import lionking.entity.LKEntityPumbaa;
import lionking.entity.LKEntityTimon;

public class LKWorldGenTimonAndPumbaa extends WorldGenerator {
    public LKWorldGenTimonAndPumbaa(boolean notify) {
        super(notify);
    }

    @Override
    public boolean generate(World world, Random random, int i, int j, int k) {
        Block block = world.getBlock(i, j - 1, k);
        if (j < 1 || j >= 256 || !isBlockReplaceable(world, i, j, k) || block == null || !block.isOpaqueCube()) {
            FMLLog.fine("Cannot spawn Timon and Pumbaa at (%d, %d, %d): invalid height or base block", i, j, k);
            return false;
        }

        if (world.isRemote) {
            FMLLog.fine("Timon and Pumbaa spawn cancelled: client-side execution at (%d, %d, %d)", i, j, k);
            return false;
        }

        LKEntityPumbaa pumbaa = new LKEntityPumbaa(world);
        pumbaa.setLocationAndAngles(i + 0.5D, j, k + 0.5D, random.nextFloat() * 360F, 0F);
        if (!world.spawnEntityInWorld(pumbaa)) {
            FMLLog.warning("Failed to spawn Pumbaa at (%d, %d, %d)", i, j, k);
            return false;
        }

        int offsetX = random.nextBoolean() ? 1 : -1;
        int offsetZ = random.nextBoolean() ? 1 : -1;

        Block timonBlock = world.getBlock(i + offsetX, j - 1, k + offsetZ);
        if (!isBlockReplaceable(world, i + offsetX, j, k + offsetZ) || timonBlock == null || !timonBlock.isOpaqueCube()) {
            FMLLog.fine("Cannot spawn Timon at (%d, %d, %d): invalid block conditions", i + offsetX, j, k + offsetZ);
            return false;
        }

        LKEntityTimon timon = new LKEntityTimon(world);
        timon.setLocationAndAngles(i + 0.5D + offsetX, j, k + 0.5D + offsetZ, random.nextFloat() * 360F, 0F);
        if (!world.spawnEntityInWorld(timon)) {
            FMLLog.warning("Failed to spawn Timon at (%d, %d, %d)", i + offsetX, j, k + offsetZ);
            return false;
        }

        FMLLog.info("Successfully spawned Timon and Pumbaa at (%d, %d, %d)", i, j, k);
        return true;
    }

    private boolean isBlockReplaceable(World world, int i, int j, int k) {
        Block block = world.getBlock(i, j, k);
        return block == Blocks.air || block.isLeaves(world, i, j, k) || block == Blocks.tallgrass || block == Blocks.deadbush;
    }
}