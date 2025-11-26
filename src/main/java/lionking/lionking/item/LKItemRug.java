package lionking.item;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import lionking.tileentity.LKTileEntityFurRug;
import lionking.client.LKCreativeTabs;
import lionking.block.LKBlockRug;
import lionking.mod_LionKing;

public class LKItemRug extends LKItemMetadata {
    public LKItemRug(Block block) {
        super(block);
		setCreativeTab(LKCreativeTabs.TAB_BLOCK);
    }

    @Override
    public boolean placeBlockAt(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float hitX, float hitY, float hitZ, int metadata) {
        boolean placed = super.placeBlockAt(itemstack, entityplayer, world, i, j, k, side, hitX, hitY, hitZ, metadata);

        if (placed && world.getBlock(i, j, k) == field_150939_a) {
            TileEntity tileentity = world.getTileEntity(i, j, k);
            if (tileentity instanceof LKTileEntityFurRug) {
                ForgeDirection dir = ForgeDirection.getOrientation(side).getOpposite();
                ((LKTileEntityFurRug) tileentity).setDirection(dir.ordinal());
            }
        }
        return placed;
    }
}
