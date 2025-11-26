package lionking.block;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.ForgeDirection;

import lionking.client.LKCreativeTabs;
import lionking.mod_LionKing;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class LKBlockMaizeSlab extends BlockSlab {
    private final boolean isDouble;

    public LKBlockMaizeSlab(boolean isDouble) {
        super(isDouble, Material.grass);
        this.isDouble = isDouble;
        setCreativeTab(LKCreativeTabs.tabBlock);
        setHardness(0.5F);
        setStepSound(soundTypeGrass);
        setBlockName(isDouble ? "driedMaizeSlabDouble" : "driedMaizeSlabSingle");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        return mod_LionKing.driedMaizeBlock.getIcon(side, metadata & 7);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
    }

    @Override
    public Item getItemDropped(int metadata, Random random, int fortune) {
        return Item.getItemFromBlock(mod_LionKing.driedMaizeSlabSingle);
    }

    @Override
    protected ItemStack createStackedBlock(int metadata) {
        return new ItemStack(mod_LionKing.driedMaizeSlabSingle, 2, metadata & 7);
    }

    @Override
    public String func_150002_b(int metadata) {
        return getUnlocalizedName() + "." + (metadata & 7);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        if (this == mod_LionKing.driedMaizeSlabDouble) {
            return super.shouldSideBeRendered(world, x, y, z, side);
        }
        if (side != 1 && side != 0 && !super.shouldSideBeRendered(world, x, y, z, side)) {
            return false;
        }
        int oppositeX = x + Facing.offsetsXForSide[Facing.oppositeSide[side]];
        int oppositeY = y + Facing.offsetsYForSide[Facing.oppositeSide[side]];
        int oppositeZ = z + Facing.offsetsZForSide[Facing.oppositeSide[side]];
        boolean isUpper = (world.getBlockMetadata(oppositeX, oppositeY, oppositeZ) & 8) != 0;

        return isUpper
            ? (side == 0 || (side == 1 && super.shouldSideBeRendered(world, x, y, z, side))
                || (world.getBlock(x, y, z) != mod_LionKing.driedMaizeSlabSingle || (world.getBlockMetadata(x, y, z) & 8) == 0))
            : (side == 1 || (side == 0 && super.shouldSideBeRendered(world, x, y, z, side))
                || (world.getBlock(x, y, z) != mod_LionKing.driedMaizeSlabSingle || (world.getBlockMetadata(x, y, z) & 8) != 0));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        if (this != mod_LionKing.driedMaizeSlabDouble) {
            list.add(new ItemStack(this, 1, 0));
        }
    }

    public boolean canCreatureSpawn(EnumCreatureType type, World world, int x, int y, int z) {
        int metadata = world.getBlockMetadata(x, y, z);
        return ((metadata & 8) == 8) || isOpaqueCube();
    }

    public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side) {
        int metadata = world.getBlockMetadata(x, y, z);
        return ((metadata & 8) == 8 && side == ForgeDirection.UP) || isOpaqueCube();
    }

    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) {
        return Item.getItemFromBlock(mod_LionKing.driedMaizeSlabSingle);
    }

    @Override
    public int quantityDropped(Random random) {
        return this.isDouble ? 2 : 1; 
    }
}