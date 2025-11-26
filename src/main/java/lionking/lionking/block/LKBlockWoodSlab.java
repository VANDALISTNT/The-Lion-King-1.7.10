package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.client.LKCreativeTabs;
import lionking.mod_LionKing;

import java.util.List;
import java.util.Random;

public class LKBlockWoodSlab extends BlockSlab {
    public LKBlockWoodSlab(boolean isDouble) {
        super(isDouble, Material.wood);
        setCreativeTab(LKCreativeTabs.tabBlock);
        setHardness(2.0F);
        setResistance(5.0F);
        setStepSound(soundTypeWood);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        int woodType = metadata & 7;
        return mod_LionKing.planks.getIcon(side, woodType);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
    }

    @Override
    public Item getItemDropped(int metadata, Random random, int fortune) {
        return Item.getItemFromBlock(mod_LionKing.woodSlabSingle);
    }

    @Override
    protected ItemStack createStackedBlock(int metadata) {
        return new ItemStack(mod_LionKing.woodSlabSingle, 2, metadata & 7);
    }

    @Override
    public String func_150002_b(int metadata) {
        return super.getUnlocalizedName() + "." + getSlabName(metadata & 7);
    }

    public static String getSlabName(int woodType) {
        switch (woodType) {
            case 0: return "Acacia Wood Slab";
            case 1: return "Rainforest Wood Slab";
            case 2: return "Mango Wood Slab";
            case 3: return "Passion Wood Slab";
            case 4: return "Banana Wood Slab";
            case 5: return "Deadwood Slab";
            default: return "Unknown Wood Slab";
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        if (this == mod_LionKing.woodSlabDouble) {
            return super.shouldSideBeRendered(world, x, y, z, side);
        }
        int metadata = world.getBlockMetadata(x, y, z);
        boolean isUpper = (metadata & 8) != 0;
        int oppositeSide = ForgeDirection.OPPOSITES[side]; 
        int adjX = x + ForgeDirection.getOrientation(oppositeSide).offsetX;
        int adjY = y + ForgeDirection.getOrientation(oppositeSide).offsetY;
        int adjZ = z + ForgeDirection.getOrientation(oppositeSide).offsetZ;

        if (side != 1 && side != 0 && !super.shouldSideBeRendered(world, x, y, z, side)) {
            return false;
        }
        return isUpper ?
            (side == 0 || (side == 1 && super.shouldSideBeRendered(world, x, y, z, side)) ||
                world.getBlock(x, y, z) != mod_LionKing.woodSlabSingle || (metadata & 8) == 0)
            : (side == 1 || (side == 0 && super.shouldSideBeRendered(world, x, y, z, side)) ||
                world.getBlock(x, y, z) != mod_LionKing.woodSlabSingle || (metadata & 8) != 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        if (this != mod_LionKing.woodSlabDouble) {
            for (int i = 0; i <= 5; i++) {
                list.add(new ItemStack(this, 1, i));
            }
        }
    }

    public boolean canCreatureSpawn(EnumCreatureType type, World world, int x, int y, int z) {
        int metadata = world.getBlockMetadata(x, y, z);
        return (metadata & 8) == 8;
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        int metadata = world.getBlockMetadata(x, y, z);
        return (metadata & 8) == 8 && side == ForgeDirection.UP;
    }

    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) {
        return Item.getItemFromBlock(mod_LionKing.woodSlabSingle);
    }

    @Override
    public int quantityDropped(Random random) {
        return this == mod_LionKing.woodSlabDouble ? 2 : 1; 
    }
}