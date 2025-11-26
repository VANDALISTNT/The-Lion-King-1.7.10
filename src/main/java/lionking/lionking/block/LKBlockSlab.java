package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType; 
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
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

public class LKBlockSlab extends BlockSlab {
    public LKBlockSlab(boolean isDouble) {
        super(isDouble, Material.rock);
        setCreativeTab(LKCreativeTabs.tabBlock);
        setHardness(2.0F);
        setResistance(10.0F);
        setStepSound(soundTypeStone);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        int slabType = metadata & 7;
        switch (slabType) {
            case 1: return mod_LionKing.prideBrick.getIcon(side, 0);
            case 2: return mod_LionKing.pridePillar.getIcon(side, 0);
            case 3: return mod_LionKing.pridestone.getIcon(side, 1);
            case 4: return mod_LionKing.prideBrick.getIcon(side, 1);
            case 5: return mod_LionKing.pridePillar.getIcon(side, 4);
            case 0:
            default: return mod_LionKing.pridestone.getIcon(side, 0);
        }
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata & 7;
    }

    @Override
    protected ItemStack createStackedBlock(int metadata) {
        return new ItemStack(mod_LionKing.slabSingle, 2, metadata & 7);
    }

    @Override
    public String func_150002_b(int metadata) {
        return super.getUnlocalizedName() + "." + getSlabName(metadata & 7);
    }

    public static String getSlabName(int slabType) {
        switch (slabType) {
            case 0: return "Pridestone Slab";
            case 1: return "Pridestone Brick Slab";
            case 2: return "Pridestone Pillar Slab";
            case 3: return "Corrupt Pridestone Slab";
            case 4: return "Corrupt Pridestone Brick Slab";
            case 5: return "Corrupt Pridestone Pillar Slab";
            default: return "Unknown Slab";
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        if (this == mod_LionKing.slabDouble) {
            return super.shouldSideBeRendered(world, x, y, z, side);
        }
        if (side != 1 && side != 0 && !super.shouldSideBeRendered(world, x, y, z, side)) {
            return false;
        }
        int oppositeSide = Facing.oppositeSide[side];
        int adjX = x + Facing.offsetsXForSide[oppositeSide];
        int adjY = y + Facing.offsetsYForSide[oppositeSide];
        int adjZ = z + Facing.offsetsZForSide[oppositeSide];
        boolean isUpper = (world.getBlockMetadata(adjX, adjY, adjZ) & 8) != 0;
        return isUpper ?
            (side == 0 || (side == 1 && super.shouldSideBeRendered(world, x, y, z, side)) ||
                world.getBlock(x, y, z) != mod_LionKing.slabSingle || (world.getBlockMetadata(x, y, z) & 8) == 0)
            : (side == 1 || (side == 0 && super.shouldSideBeRendered(world, x, y, z, side)) ||
                world.getBlock(x, y, z) != mod_LionKing.slabSingle || (world.getBlockMetadata(x, y, z) & 8) != 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        if (this != mod_LionKing.slabDouble) {
            for (int i = 0; i < 6; i++) {
                list.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        int slabType = world.getBlockMetadata(x, y, z) & 7;
        switch (slabType) {
            case 2: return 1.2F;
            case 3: return 2.0F * 0.7F;
            case 4: return 2.0F * 0.7F;
            case 5: return 1.2F * 0.7F;
            default: return 2.0F;
        }
    }

    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
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
        return Item.getItemFromBlock(mod_LionKing.slabSingle);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
    }

    @Override
    public int quantityDropped(Random random) {
        return this == mod_LionKing.slabDouble ? 2 : 1; 
    }
}