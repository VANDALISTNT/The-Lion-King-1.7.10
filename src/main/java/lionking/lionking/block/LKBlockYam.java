package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.mod_LionKing;
import lionking.common.LKIngame;
import lionking.client.LKCreativeTabs;

import java.util.ArrayList;
import java.util.Random;

public class LKBlockYam extends BlockCrops {
    @SideOnly(Side.CLIENT)
    private IIcon[] yamIcons;

    public LKBlockYam() {
        super();
        setTickRandomly(true);
        setBlockName("lionking:yam");
        setBlockTextureName("lionking:yam");
        setCreativeTab(LKCreativeTabs.TAB_FOOD);
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        int metadata = world.getBlockMetadata(x, y, z);
        if (metadata == 8) {
            Block blockBelow = world.getBlock(x, y - 1, z);
            return (blockBelow == Block.getBlockById(2) || blockBelow == Block.getBlockById(3)) &&
                   (world.getFullBlockLightValue(x, y, z) >= 8 || world.canBlockSeeTheSky(x, y, z));
        }
        return super.canBlockStay(world, x, y, z);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!world.isRemote && LKIngame.isLKWorld(world.provider.dimensionId)) {
            super.updateTick(world, x, y, z, random);
        }
    }

    public void fertilize(World world, int x, int y, int z) {
        if (!world.isRemote && LKIngame.isLKWorld(world.provider.dimensionId)) {
            int metadata = world.getBlockMetadata(x, y, z);
            if (metadata < 8) {
                world.setBlockMetadataWithNotify(x, y, z, Math.min(metadata + (world.rand.nextInt(2) + 1), 8), 2);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        if (metadata < 0 || metadata > 8) metadata = 0;
        if (metadata < 7) {
            if (metadata == 6) {
                metadata = 5;
            }
            return yamIcons[metadata >> 1];
        }
        return yamIcons[3];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        yamIcons = new IIcon[4];
        for (int i = 0; i < 4; i++) {
            yamIcons[i] = iconRegister.registerIcon(getTextureName() + "_" + i);
        }
    }

    @Override
    protected Item func_149866_i() {
        return mod_LionKing.yam;
    }

    @Override
    protected Item func_149865_P() {
        return mod_LionKing.yam;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>();
        if (metadata == 8) {
            if (world.rand.nextInt(3) > 0) {
                drops.add(new ItemStack(mod_LionKing.yam));
            }
        } else {
            drops.add(new ItemStack(mod_LionKing.yam, 1, 0));
        }
        return drops;
    }
}