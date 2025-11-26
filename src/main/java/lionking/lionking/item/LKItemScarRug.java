package lionking.item;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.item.EnumRarity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;
import lionking.client.LKCreativeTabs;
import lionking.entity.LKEntityScarRug;

public class LKItemScarRug extends LKItem {
    private final int type;

    public LKItemScarRug(int j) {
        super();
        type = j;
        setCreativeTab(LKCreativeTabs.TAB_QUEST);
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        Block block = world.getBlock(i, j, k);

        if (block == Blocks.snow) {
            l = 1;
        } else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && !block.isReplaceable(world, i, j, k)) {
            switch (l) {
                case 0: --j; break;
                case 1: ++j; break;
                case 2: --k; break;
                case 3: ++k; break;
                case 4: --i; break;
                case 5: ++i; break;
            }
        }

        if (!entityplayer.canPlayerEdit(i, j, k, l, itemstack)) {
            return false;
        }

        Block blockBelow = world.getBlock(i, j - 1, k);
        if (world.doesBlockHaveSolidTopSurface(world, i, j - 1, k) || (blockBelow != null && blockBelow.isNormalCube())) {
            if (!world.isRemote) {
                LKEntityScarRug rug = new LKEntityScarRug(world, type);
                rug.setLocationAndAngles(i + f, j, k + f2, (entityplayer.rotationYaw % 360.0F) + 180.0F, 0.0F);

                if (world.checkNoEntityCollision(rug.boundingBox) && world.getCollidingBoundingBoxes(rug, rug.boundingBox).isEmpty()
                        && !world.isAnyLiquid(rug.boundingBox)) {
                    world.spawnEntityInWorld(rug);
                    world.playSoundAtEntity(rug, "lionking:lion", 1.0F, (itemRand.nextFloat() - itemRand.nextFloat()) * 0.2F + 1.0F);
                    --itemstack.stackSize;
                    return true;
                }
                rug.setDead();
            }
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.uncommon;
    }
}
