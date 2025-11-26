package lionking.block;

import net.minecraft.block.BlockMushroom;
import net.minecraft.world.World;
import net.minecraft.block.Block;

import lionking.client.LKCreativeTabs;

import java.util.Random;

public class LKBlockOutshroom extends BlockMushroom {
    private final boolean isGlowing;

    public LKBlockOutshroom(boolean isGlowing) {
        super();
        this.isGlowing = isGlowing;
        setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.625F, 0.75F);
        if (isGlowing) {
            setLightLevel(0.875F);
        }
        setCreativeTab(LKCreativeTabs.tabDecorations);
        setBlockName("outshroom");
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!isGlowing && random.nextInt(50) == 0) {
            int newX = x + random.nextInt(3) - 1;
            int newY = y + random.nextInt(2) - random.nextInt(2);
            int newZ = z + random.nextInt(3) - 1;

            if (world.isAirBlock(newX, newY, newZ) && canBlockStay(world, newX, newY, newZ)) {
                x += random.nextInt(3) - 1;
                z += random.nextInt(3) - 1;

                if (world.isAirBlock(newX, newY, newZ) && canBlockStay(world, newX, newY, newZ)) {
                    world.setBlock(newX, newY, newZ, this);
                }
            }
        }
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        if (isGlowing) {
            Block blockBelow = world.getBlock(x, y - 1, z);
            return blockBelow != null && blockBelow.isOpaqueCube();
        } else {
            return super.canBlockStay(world, x, y, z);
        }
    }
}