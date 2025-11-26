package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;

import lionking.mod_LionKing;
import lionking.tileentity.LKTileEntityHyenaHead;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Random;

public class LKBlockHyenaHead extends BlockContainer {
    public LKBlockHyenaHead() {
        super(Material.circuits);
        setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
        setBlockName("hyenaHead");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        return Blocks.soul_sand.getIcon(side, metadata);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
    }

    @Override
    public int getRenderType() {
        return -1;
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
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int metadata = world.getBlockMetadata(x, y, z) & 0x7;
        switch (metadata) {
            case 1: default:
                setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
                break;
            case 2:
                setBlockBounds(0.25F, 0.25F, 0.5F, 0.75F, 0.75F, 1.0F);
                break;
            case 3:
                setBlockBounds(0.25F, 0.25F, 0.0F, 0.75F, 0.75F, 0.5F);
                break;
            case 4:
                setBlockBounds(0.5F, 0.25F, 0.25F, 1.0F, 0.75F, 0.75F);
                break;
            case 5:
                setBlockBounds(0.0F, 0.25F, 0.25F, 0.5F, 0.75F, 0.75F);
                break;
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        setBlockBoundsBasedOnState(world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack stack) {
        int direction = MathHelper.floor_double((placer.rotationYaw * 4.0F / 360.0F) + 2.5D) & 0x3;
        world.setBlockMetadataWithNotify(x, y, z, direction, 2);
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof LKTileEntityHyenaHead) {
            ((LKTileEntityHyenaHead) te).setHyenaType(stack.getItemDamage());
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new LKTileEntityHyenaHead();
    }

    @Override
    public Item getItemDropped(int metadata, Random random, int fortune) {
        return mod_LionKing.hyenaHeadItem;
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata & 0x7;
    }

    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) {
        return mod_LionKing.hyenaHeadItem;
    }

    @Override
    public int getDamageValue(World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof LKTileEntityHyenaHead) {
            return ((LKTileEntityHyenaHead) tileEntity).getHyenaType();
        }
        return 0;
    }

    @Override
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int metadata, float dropChance, int fortune) {
    }

    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int metadata, EntityPlayer player) {
        if (player.capabilities.isCreativeMode) {
            world.setBlockMetadataWithNotify(x, y, z, metadata | 8, 4);
        }
        super.onBlockHarvested(world, x, y, z, metadata, player);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
        if (!world.isRemote && (metadata & 8) == 0) {
            ItemStack drop = new ItemStack(mod_LionKing.hyenaHeadItem, 1, getDamageValue(world, x, y, z));
            dropBlockAsItem(world, x, y, z, drop);
        }
        super.breakBlock(world, x, y, z, block, metadata);
    }

    @Override
    public int quantityDropped(Random random) {
        return 1; 
    }
}