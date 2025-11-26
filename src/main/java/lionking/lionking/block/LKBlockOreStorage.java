package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import lionking.mod_LionKing;
import lionking.client.LKCreativeTabs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

public class LKBlockOreStorage extends Block {
    @SideOnly(Side.CLIENT)
    private IIcon kivuliteIcon;

    public LKBlockOreStorage() {
        super(Material.iron);
        setCreativeTab(LKCreativeTabs.tabDecorations);
        setHardness(5.0F);
        setResistance(10.0F);
        setStepSound(soundTypeMetal);
        setBlockName("oreStorage");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        if (this == mod_LionKing.blockSilver && metadata == 1) {
            return kivuliteIcon;
        }
        return blockIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("lionking:blockSilver");
        if (this == mod_LionKing.blockSilver) {
            kivuliteIcon = iconRegister.registerIcon("lionking:blockKivulite");
        }
    }

    @Override
    public int damageDropped(int metadata) {
        return (this == mod_LionKing.blockSilver) ? metadata : 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        if (this == mod_LionKing.blockSilver) {
            for (int meta = 0; meta < 2; meta++) {
                list.add(new ItemStack(this, 1, meta));
            }
        } else {
            list.add(new ItemStack(this, 1, 0));
        }
    }

    @Override
    public boolean isBeaconBase(IBlockAccess world, int x, int y, int z, int beaconX, int beaconY, int beaconZ) {
        return true;
    }
}
