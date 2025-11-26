package lionking.item;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

import lionking.client.LKCreativeTabs;
import lionking.mod_LionKing;

public class LKItemBanana extends LKItemFood {
    public LKItemBanana(int hunger, float saturation, boolean isWolfFood) {
        super(hunger, saturation, isWolfFood);
	   setCreativeTab(LKCreativeTabs.TAB_FOOD);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        Block block = world.getBlock(x, y, z);
        int blockMeta = world.getBlockMetadata(x, y, z);
        if (block != mod_LionKing.prideWood2 || (blockMeta & 3) != 0) {
            return false;
        }

        if (side == 0 || side == 1) {
            return false;
        }

        int[] coords = adjustCoordinates(x, y, z, side);
        x = coords[0];
        y = coords[1];
        z = coords[2];

        if (world.isAirBlock(x, y, z)) {
            int bananaMetadata = ForgeDirection.getOrientation(side - 2).getOpposite().ordinal();
            world.setBlock(x, y, z, mod_LionKing.hangingBanana, bananaMetadata, 3);

            if (!player.capabilities.isCreativeMode) {
                itemStack.stackSize--;
            }
        }
        return true;
    }

    private int[] adjustCoordinates(int x, int y, int z, int side) {
        int[] coords = {x, y, z};
        switch (side) {
            case 2: coords[2]--; break; 
            case 3: coords[2]++; break; 
            case 4: coords[0]--; break; 
            case 5: coords[0]++; break; 
        }
        return coords;
    }
}
