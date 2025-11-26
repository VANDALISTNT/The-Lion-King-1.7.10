package lionking.item;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import lionking.mod_LionKing;
import lionking.client.LKCreativeTabs;

public class LKItemTallFlower extends LKItem {
    private final int flowerMetadata;

    public LKItemTallFlower(int metadata) {
        super();
        flowerMetadata = metadata;
        setCreativeTab(LKCreativeTabs.TAB_DECORATIONS);
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        Block block = world.getBlock(i, j, k);

        if (block == Blocks.snow_layer) {
            l = 1;
        } else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush &&
                   (block != null && !block.isReplaceable(world, i, j, k))) {
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

        Block flowerBase = mod_LionKing.flowerBase;
        Block flowerTop = mod_LionKing.flowerTop;
        if (j < 255 &&
            (world.getBlock(i, j - 1, k) == Blocks.grass ||
             world.getBlock(i, j - 1, k) == Blocks.dirt ||
             world.getBlock(i, j - 1, k) == Blocks.farmland) &&
            isReplaceableBlock(world, i, j, k) && isReplaceableBlock(world, i, j + 1, k)) {

            if (!world.isRemote) {
                world.setBlock(i, j, k, flowerBase, flowerMetadata, 3);
                world.setBlock(i, j + 1, k, flowerTop, flowerMetadata, 3);

                world.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, flowerBase.stepSound.getBreakSound(),
                    (flowerBase.stepSound.getVolume() + 1.0F) / 2.0F, flowerBase.stepSound.getPitch() * 0.8F);
                --itemstack.stackSize;
                return true;
            }
        }
        return false;
    }

    private boolean isReplaceableBlock(World world, int i, int j, int k) {
        Block block = world.getBlock(i, j, k);
        return block == null || block.isAir(world, i, j, k) || block.isReplaceable(world, i, j, k);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        return "item.tallFlower." + flowerMetadata;
    }
}