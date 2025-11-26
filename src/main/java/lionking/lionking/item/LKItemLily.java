package lionking.item;

import net.minecraft.world.World;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;

import lionking.client.LKCreativeTabs;
import lionking.mod_LionKing;

public class LKItemLily extends LKItemMetadata {
    public LKItemLily(Block block) {
        super(block); 
		setCreativeTab(LKCreativeTabs.TAB_DECORATIONS);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        MovingObjectPosition m = getMovingObjectPositionFromPlayer(world, entityplayer, true);

        if (m == null) {
            return itemstack;
        }

        if (m.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            int x = m.blockX;
            int y = m.blockY;
            int z = m.blockZ;

            if (!world.canMineBlock(entityplayer, x, y, z) || !entityplayer.canPlayerEdit(x, y, z, m.sideHit, itemstack)) {
                return itemstack;
            }

            if (world.getBlock(x, y, z).getMaterial() == Material.water
                    && world.getBlockMetadata(x, y, z) == 0
                    && world.isAirBlock(x, y + 1, z)) {
                world.setBlock(x, y + 1, z, mod_LionKing.lily, getMetadata(itemstack.getItemDamage()), 3);

                if (!entityplayer.capabilities.isCreativeMode) {
                    itemstack.stackSize--;
                }
            }
        }

        return itemstack;
    }
}
