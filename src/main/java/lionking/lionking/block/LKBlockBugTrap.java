package lionking.block; 

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.texture.IIconRegister; 
import net.minecraft.world.IBlockAccess; 
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.client.LKCreativeTabs;
import lionking.mod_LionKing;
import lionking.tileentity.LKTileEntityBugTrap;

import java.util.Random;

public class LKBlockBugTrap extends BlockContainer {
    private final Random rand = new Random();

    public LKBlockBugTrap() { 
        super(Material.wood); 
        setCreativeTab(LKCreativeTabs.tabDecorations); 
        setHardness(0.5F); 
        setStepSound(soundTypeWood); 
        setBlockName("bugTrap"); 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) { 
        return mod_LionKing.prideWood.getIcon(2, 0); 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) { 
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) { 
        return new LKTileEntityBugTrap();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(mod_LionKing.instance, mod_LionKing.proxy.GUI_ID_TRAP, world, x, y, z);
        }
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block oldBlock, int oldMetadata) { 
        if (!world.isRemote) { 
            LKTileEntityBugTrap tileEntity = (LKTileEntityBugTrap) world.getTileEntity(x, y, z); 
            if (tileEntity != null) {
                for (int slot = 0; slot < tileEntity.getSizeInventory(); slot++) {
                    ItemStack stack = tileEntity.getStackInSlot(slot);
                    if (stack == null) continue;

                    float offsetX = rand.nextFloat() * 0.8F + 0.1F;
                    float offsetY = rand.nextFloat() * 0.8F + 0.1F;
                    float offsetZ = rand.nextFloat() * 0.8F + 0.1F;

                    while (stack.stackSize > 0) {
                        int dropAmount = rand.nextInt(21) + 10;
                        if (dropAmount > stack.stackSize) dropAmount = stack.stackSize;
                        stack.stackSize -= dropAmount;

                        EntityItem entityItem = new EntityItem(world, x + offsetX, y + offsetY, z + offsetZ,
                            new ItemStack(stack.getItem(), dropAmount, stack.getItemDamage())); 
                        float motionScale = 0.05F;
                        entityItem.motionX = rand.nextGaussian() * motionScale;
                        entityItem.motionY = rand.nextGaussian() * motionScale + 0.2F;
                        entityItem.motionZ = rand.nextGaussian() * motionScale;

                        if (stack.hasTagCompound()) {
                            entityItem.getEntityItem().setTagCompound((NBTTagCompound) stack.getTagCompound().copy());
                        }
                        world.spawnEntityInWorld(entityItem);
                    }
                }
            }
        }
        super.breakBlock(world, x, y, z, oldBlock, oldMetadata); 
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return super.canPlaceBlockAt(world, x, y, z) && world.doesBlockHaveSolidTopSurface(world, x, y - 1, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlock) { 
        if (!world.isRemote) { 
            if (!world.doesBlockHaveSolidTopSurface(world, x, y - 1, z)) {
                world.setBlockToAir(x, y, z);
                dropBlockAsItem(world, x, y, z, new ItemStack(this, 1, 0)); 
            }
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
        return mod_LionKing.proxy.getBugTrapRenderID(); 
    }

    @Override
    public Item getItemDropped(int metadata, Random random, int fortune) { 
        return Item.getItemFromBlock(this); 
    }
}