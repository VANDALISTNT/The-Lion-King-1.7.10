package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon; 
import net.minecraft.item.Item;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import lionking.mod_LionKing;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Random;

public class LKBlockHangingBanana extends Block { 
    @SideOnly(Side.CLIENT)
    private IIcon[] bananaIcons; 

    private static final String[] BANANA_SIDES = {"top", "side", "bottom"};

    public LKBlockHangingBanana() { 
        super(Material.plants);
        setTickRandomly(true);
        setCreativeTab(null); 
        setBlockName("hangingBanana"); 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) { 
        if (side == 0) return bananaIcons[2]; 
        if (side == 1) return bananaIcons[0]; 
        return bananaIcons[1]; 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) { 
        bananaIcons = new IIcon[3]; 
        for (int i = 0; i < 3; i++) {
            bananaIcons[i] = iconRegister.registerIcon("lionking:banana_" + BANANA_SIDES[i]); 
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!canBlockStay(world, x, y, z)) {
            dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlock) { 
        if (!canBlockStay(world, x, y, z)) {
            dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
        }
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        int metadata = world.getBlockMetadata(x, y, z);
        ForgeDirection dir = ForgeDirection.getOrientation(metadata + 2);
        Block adjacentBlock = world.getBlock(x + dir.offsetX, y, z + dir.offsetZ); 
        int adjacentMeta = world.getBlockMetadata(x + dir.offsetX, y, z + dir.offsetZ);
        return adjacentBlock == mod_LionKing.prideWood2 && (adjacentMeta & 3) == 0; 
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        setBlockBoundsBasedOnState(world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        setBlockBoundsBasedOnState(world, x, y, z);
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int direction = world.getBlockMetadata(x, y, z);
        switch (direction) {
            case 0: 
                setBlockBounds(0.375F, 0.1875F, 0.0F, 0.625F, 0.9375F, 0.25F);
                break;
            case 1: 
                setBlockBounds(0.375F, 0.1875F, 0.75F, 0.625F, 0.9375F, 1.0F);
                break;
            case 2: 
                setBlockBounds(0.0F, 0.1875F, 0.375F, 0.25F, 0.9375F, 0.625F);
                break;
            case 3: 
                setBlockBounds(0.75F, 0.1875F, 0.375F, 1.0F, 0.9375F, 0.625F);
                break;
            default:
                setBlockBounds(0.375F, 0.1875F, 0.0F, 0.625F, 0.9375F, 0.25F); 
                break;
        }
    }

    @Override
    public Item getItemDropped(int metadata, Random random, int fortune) { 
        return mod_LionKing.banana; 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) { 
        return mod_LionKing.banana; 
    }
}