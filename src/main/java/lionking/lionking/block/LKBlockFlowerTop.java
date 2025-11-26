package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon; 
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.mod_LionKing;

import java.util.Random;

public class LKBlockFlowerTop extends Block { 
    @SideOnly(Side.CLIENT)
    private IIcon[] flowerIcons; 

    private static final String[] FLOWER_NAMES = {"purpleFlower_top", "redFlower_top"};

    public LKBlockFlowerTop() { 
        super(Material.plants);
        setTickRandomly(true);
        float width = 0.2F;
        setBlockBounds(0.5F - width, 0.0F, 0.5F - width, 0.5F + width, 0.7F, 0.5F + width);
        setCreativeTab(null); 
        setHardness(0.0F); 
        setStepSound(soundTypeGrass); 
        setBlockName("flowerTop"); 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) { 
        return flowerIcons[Math.min(metadata, FLOWER_NAMES.length - 1)]; 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) { 
        flowerIcons = new IIcon[FLOWER_NAMES.length];
        for (int i = 0; i < FLOWER_NAMES.length; i++) {
            flowerIcons[i] = iconRegister.registerIcon("lionking:" + FLOWER_NAMES[i]);
        }
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int metadata) {
        if (!world.isRemote) { 
            checkFlowerChange(world, x, y, z);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlock) { 
        if (!world.isRemote) { 
            checkFlowerChange(world, x, y, z);
        }
    }

    private void checkFlowerChange(World world, int x, int y, int z) {
        Block belowBlock = world.getBlock(x, y - 1, z);
        if (belowBlock != mod_LionKing.flowerBase || belowBlock == Blocks.water) { 
            world.setBlockToAir(x, y, z);
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!world.isRemote) { 
            checkFlowerChange(world, x, y, z);
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null; 
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
        return 1; 
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z) == 1 ? 11 : 0; 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItemDropped(int metadata, Random random, int fortune) { 
        return metadata == 1 ? mod_LionKing.redFlower : mod_LionKing.purpleFlower; 
    }
}
