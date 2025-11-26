package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;
import lionking.client.LKCreativeTabs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LKBlockGlass extends BlockBreakable {
    @SideOnly(Side.CLIENT)
    private IIcon blockIcon;

    public LKBlockGlass(Material material, boolean breakable) { 
        super("lionking:outglass", material, breakable); 
        setCreativeTab(LKCreativeTabs.tabBlock);
        setHardness(0.3F); 
        setStepSound(Block.soundTypeGlass); 
        setBlockName("outglass"); 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1; 
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
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        return blockIcon; 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) { 
        blockIcon = iconRegister.registerIcon("lionking:outglass"); 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        Block adjacentBlock = world.getBlock(x, y, z); 
        return adjacentBlock != this && super.shouldSideBeRendered(world, x, y, z, side); 
    }

    @Override
    protected boolean canSilkHarvest() {
        return true; 
    }
}