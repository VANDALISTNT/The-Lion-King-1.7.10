package lionking.item;

import net.minecraft.world.World;
import net.minecraft.util.MathHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.BlockBed;

import lionking.client.LKCreativeTabs;
import lionking.mod_LionKing;

public class LKItemBed extends LKItem {
    public LKItemBed() {
        super();
        setCreativeTab(LKCreativeTabs.TAB_DECORATIONS);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (side != 1) {
            return false;
        }

        y++;
        BlockBed bedBlock = (BlockBed) mod_LionKing.blockBed;

        int direction = MathHelper.floor_double((player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int offsetX = getOffsetX(direction);
        int offsetZ = getOffsetZ(direction);

        if (!canPlaceBed(player, world, x, y, z, offsetX, offsetZ, direction, itemStack)) {
            return false;
        }

        world.setBlock(x, y, z, bedBlock, direction, 3);
        if (world.getBlock(x, y, z) == bedBlock) {
            world.setBlock(x + offsetX, y, z + offsetZ, bedBlock, direction + 8, 3);
        }

        itemStack.stackSize--;
        return true;
    }

    private int getOffsetX(int direction) {
        return direction == 1 ? -1 : direction == 3 ? 1 : 0;
    }

    private int getOffsetZ(int direction) {
        return direction == 0 ? 1 : direction == 2 ? -1 : 0;
    }

    private boolean canPlaceBed(EntityPlayer player, World world, int x, int y, int z, int offsetX, int offsetZ, int direction, ItemStack itemStack) {
        return player.canPlayerEdit(x, y, z, direction, itemStack) &&
               player.canPlayerEdit(x + offsetX, y, z + offsetZ, direction, itemStack) &&
               world.isAirBlock(x, y, z) &&
               world.isAirBlock(x + offsetX, y, z + offsetZ) &&
               world.doesBlockHaveSolidTopSurface(world, x, y - 1, z) &&
               world.doesBlockHaveSolidTopSurface(world, x + offsetX, y - 1, z + offsetZ);
    }
}