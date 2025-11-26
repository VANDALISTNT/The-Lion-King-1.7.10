package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon; 
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.World;
import net.minecraft.item.Item;

import lionking.client.LKCreativeTabs;
import lionking.mod_LionKing;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Random;

public class LKBlockKiwano extends Block { 
    @SideOnly(Side.CLIENT)
    private IIcon[] kiwanoIcons; 

    public LKBlockKiwano() { 
        super(Material.gourd); 
        setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.8125F, 0.875F);
        setCreativeTab(LKCreativeTabs.tabDecorations); 
        setBlockName("kiwano");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) { 
        return (metadata == -1) ? kiwanoIcons[1] : kiwanoIcons[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) { 
        kiwanoIcons = new IIcon[2]; 
        kiwanoIcons[0] = iconRegister.registerIcon("lionking:kiwano"); 
        kiwanoIcons[1] = iconRegister.registerIcon("lionking:kiwano_spikes");
    }

    @Override
    public Item getItemDropped(int metadata, Random random, int fortune) { 
        return mod_LionKing.kiwano; 
    }

    @Override
    public int quantityDropped(Random random) {
        return 2 + random.nextInt(5); 
    }

    @Override
    public int quantityDroppedWithBonus(int fortune, Random random) {
        int baseDrop = quantityDropped(random);
        int bonusDrop = baseDrop + random.nextInt(1 + fortune);
        return Math.min(bonusDrop, 9); 
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlock) { 
        if (!world.isRemote) {
            if (!world.doesBlockHaveSolidTopSurface(world, x, y - 1, z)) { 
                dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0); 
                world.setBlockToAir(x, y, z);
            }
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && super.canPlaceBlockAt(world, x, y, z); 
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return mod_LionKing.proxy.getKiwanoBlockRenderID(); 
    }
}