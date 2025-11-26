package lionking.world;

import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.world.gen.feature.WorldGenerator;
import java.util.Random;
import cpw.mods.fml.common.FMLLog;
import lionking.mod_LionKing;

public class LKWorldGenLiquids extends WorldGenerator {
    private final Block liquidBlock;

    public LKWorldGenLiquids(int liquidBlockId) {
        this.liquidBlock = Block.getBlockById(liquidBlockId);
        FMLLog.fine("Initialized LKWorldGenLiquids with blockId=%d", liquidBlockId);
    }

    public boolean generate(World world, Random random, int x, int y, int z) {
        FMLLog.fine("Attempting to generate liquid at (%d, %d, %d)", x, y, z);

        if (liquidBlock == null || mod_LionKing.pridestone == null) {
            FMLLog.severe("Cannot generate liquid at (%d, %d, %d): liquidBlock or pridestone is null", x, y, z);
            return false;
        }

        if (world.getBlock(x, y + 1, z) != mod_LionKing.pridestone || 
            world.getBlock(x, y - 1, z) != mod_LionKing.pridestone) {
            FMLLog.fine("Cannot generate liquid at (%d, %d, %d): missing pridestone above or below", x, y, z);
            return false;
        }

        Block currentBlock = world.getBlock(x, y, z);
        if (!world.isAirBlock(x, y, z) && currentBlock != mod_LionKing.pridestone) {
            FMLLog.fine("Cannot generate liquid at (%d, %d, %d): block is not air or pridestone (%s)", x, y, z, currentBlock != null ? currentBlock.getUnlocalizedName() : "null");
            return false;
        }

        int stoneCount = 0;
        int airCount = 0;
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if (dir != ForgeDirection.UP && dir != ForgeDirection.DOWN) {
                int dx = x + dir.offsetX;
                int dz = z + dir.offsetZ;
                Block neighbor = world.getBlock(dx, y, dz);

                if (neighbor == mod_LionKing.pridestone) {
                    stoneCount++;
                } else if (world.isAirBlock(dx, y, dz)) {
                    airCount++;
                }
            }
        }

        if (stoneCount == 3 && airCount == 1) {
            world.setBlock(x, y, z, liquidBlock, 0, 3);
            world.scheduleBlockUpdate(x, y, z, liquidBlock, liquidBlock.tickRate(world));
            FMLLog.fine("Placed %s at (%d, %d, %d)", liquidBlock.getUnlocalizedName(), x, y, z);
            FMLLog.info("Successfully generated %s at (%d, %d, %d)", liquidBlock.getUnlocalizedName(), x, y, z);
            return true;
        } else {
            FMLLog.fine("Cannot generate liquid at (%d, %d, %d): stoneCount=%d, airCount=%d", x, y, z, stoneCount, airCount);
            return false;
        }
    }

    private boolean isBlockReplaceable(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        return block == Blocks.air || block.isLeaves(world, x, y, z) || block == Blocks.tallgrass || block == Blocks.deadbush;
    }
}