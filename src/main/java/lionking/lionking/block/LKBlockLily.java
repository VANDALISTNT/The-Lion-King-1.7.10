package lionking.block;

import net.minecraft.block.BlockLilyPad;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;
import net.minecraft.item.Item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.mod_LionKing;
import lionking.client.LKCreativeTabs;

import java.util.List;

public class LKBlockLily extends BlockLilyPad {
    @SideOnly(Side.CLIENT)
    private IIcon[] lilyIcons;

    private static final String[] LILY_TYPES = {"white", "violet", "red"};

    public LKBlockLily() {
        super();
        setCreativeTab(LKCreativeTabs.tabDecorations);
        setBlockName("lily");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int meta = 0; meta < LILY_TYPES.length; meta++) {
            list.add(new ItemStack(this, 1, meta));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        int index = metadata < 0 || metadata >= LILY_TYPES.length ? 0 : metadata;
        return lilyIcons[index];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        lilyIcons = new IIcon[LILY_TYPES.length];
        for (int i = 0; i < LILY_TYPES.length; i++) {
            lilyIcons[i] = iconRegister.registerIcon("lionking:lily_" + LILY_TYPES[i]);
        }
        blockIcon = iconRegister.registerIcon("lionking:lily_white");
    }

    @Override
    public int getRenderType() {
        return mod_LionKing.proxy.getLilyRenderID();
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBlockColor() {
        return 0xFFFFFF;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int metadata) {
        return 0xFFFFFF;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        return 0xFFFFFF;
    }
}