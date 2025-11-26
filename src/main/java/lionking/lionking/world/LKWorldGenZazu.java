package lionking.world;

import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.world.gen.feature.WorldGenerator;
import java.util.Random;
import cpw.mods.fml.common.FMLLog;

import lionking.block.LKBlockLeaves;
import lionking.entity.LKEntityZazu;

public class LKWorldGenZazu extends WorldGenerator {
    @Override
    public boolean generate(World world, Random random, int i, int j, int k) {
        FMLLog.fine("Attempting to spawn Zazu at (%d, %d, %d)", i, j, k);
        if (j < 1 || j + 2 >= 256) { 
            FMLLog.warning("Invalid height for Zazu spawn: %d", j);
            return false;
        }

        Block block = world.getBlock(i, j, k);
        if (block instanceof LKBlockLeaves && 
            world.isAirBlock(i, j + 1, k) && 
            world.isAirBlock(i, j + 2, k)) {
            if (world.isRemote) { 
                FMLLog.fine("Zazu spawn cancelled: client-side execution");
                return false;
            }

            LKEntityZazu entityzazu = new LKEntityZazu(world);
            entityzazu.setLocationAndAngles(i + 0.5D, j + 1, k + 0.5D, random.nextFloat() * 360F, 0.0F);
            if (world.spawnEntityInWorld(entityzazu)) {
                FMLLog.fine("Zazu successfully spawned at (%d, %d, %d)", i, j + 1, k);
                return true;
            } else {
                FMLLog.warning("Failed to spawn Zazu at (%d, %d, %d)", i, j + 1, k);
            }
        } else {
            FMLLog.fine("Zazu spawn conditions not met: block=%s, air above=%b", 
                block.getUnlocalizedName(), 
                world.isAirBlock(i, j + 1, k) && world.isAirBlock(i, j + 2, k));
        }
        return false;
    }
}