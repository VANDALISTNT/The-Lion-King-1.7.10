package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

import lionking.mod_LionKing;
import lionking.common.LKIngame;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Random;

public class LKBlockMaize extends Block {
    @SideOnly(Side.CLIENT)
    private IIcon cornIcon;

    public LKBlockMaize() { 
        super(Material.plants);
        float width = 0.375F;
        setBlockBounds(0.5F - width, 0.0F, 0.5F - width, 0.5F + width, 1.0F, 0.5F + width);
        setTickRandomly(true);
        setCreativeTab(null);
        setHardness(0.0F);
        setStepSound(soundTypeGrass);
        setBlockName("maize"); 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        return metadata == 1 ? cornIcon : blockIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) { 
        this.blockIcon = iconRegister.registerIcon("lionking:maize");
        cornIcon = iconRegister.registerIcon("lionking:maize_corn");
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        checkBlockCoordValid(world, x, y, z);

        if (!LKIngame.isLKWorld(world.provider.dimensionId)) return;

        int heightBelow;
        for (heightBelow = 1; world.getBlock(x, y - heightBelow, z) == this; heightBelow++) {}

        if (heightBelow >= 4) return;

        int growthRate = 8;
        for (int i = 1; i < 5; i++) {
            if (world.getBlock(x, y - i, z) == Blocks.farmland) { 
                growthRate = world.getBlockMetadata(x, y - i, z) > 0 ? 2 : 4;
                break;
            }
        }

        if (random.nextInt(growthRate) == 0) {
            if (world.isAirBlock(x, y + 1, z) && random.nextInt(22) == 0) {
                world.setBlock(x, y + 1, z, this, 1, 3);
            }
            if (world.getBlockMetadata(x, y, z) == 0 && world.getBlock(x, y - 1, z) == this && random.nextInt(25) == 0) {
                world.setBlockMetadataWithNotify(x, y, z, 1, 3);
            }
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        Block belowBlock = world.getBlock(x, y - 1, z);
        if (belowBlock == this || belowBlock == Blocks.farmland) return true; 
        if (belowBlock != Blocks.grass && belowBlock != Blocks.dirt) return false; 
        return world.getBlock(x - 1, y - 1, z).getMaterial() == Material.water || 
               world.getBlock(x + 1, y - 1, z).getMaterial() == Material.water ||
               world.getBlock(x, y - 1, z - 1).getMaterial() == Material.water ||
               world.getBlock(x, y - 1, z + 1).getMaterial() == Material.water;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) { 
        checkBlockCoordValid(world, x, y, z);
    }

    private void checkBlockCoordValid(World world, int x, int y, int z) {
        if (!canBlockStay(world, x, y, z)) {
            dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
        }
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        return canPlaceBlockAt(world, x, y, z);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Item getItemDropped(int metadata, Random random, int fortune) { 
        return mod_LionKing.cornStalk; 
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
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (world.isRemote || world.getBlockMetadata(x, y, z) != 1) return false;

        world.setBlockMetadataWithNotify(x, y, z, 0, 3);
        dropBlockAsItem(world, x, y, z, new ItemStack(mod_LionKing.corn));
        if (world.rand.nextInt(4) == 0) {
            dropBlockAsItem(world, x, y, z, new ItemStack(mod_LionKing.corn));
        }
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) { 
        if (!world.isRemote && metadata == 1) {
            dropBlockAsItem(world, x, y, z, new ItemStack(mod_LionKing.corn));
            if (world.rand.nextInt(4) == 0) {
                dropBlockAsItem(world, x, y, z, new ItemStack(mod_LionKing.corn));
            }
        }
        super.breakBlock(world, x, y, z, this, metadata);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) { 
        return mod_LionKing.cornStalk; 
    }
}