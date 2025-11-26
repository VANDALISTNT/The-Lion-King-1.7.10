package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon; 
import net.minecraft.item.Item;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.World;

import lionking.mod_LionKing;
import lionking.tileentity.LKTileEntityGrindingBowl;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Random;

public class LKBlockGrindingBowl extends BlockContainer {
    @SideOnly(Side.CLIENT)
    private IIcon[] bowlIcons; 

    public LKBlockGrindingBowl() { 
        super(Material.wood);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
        setBlockName("grindingBowl"); 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) { 
        return side == 1 ? bowlIcons[1] : bowlIcons[0]; 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) { 
        bowlIcons = new IIcon[2]; 
        bowlIcons[0] = iconRegister.registerIcon("lionking:grindingBowl_side"); 
        bowlIcons[1] = iconRegister.registerIcon("lionking:grindingBowl_top");
    }

    @Override
    public Item getItemDropped(int metadata, Random random, int fortune) { 
        return mod_LionKing.itemGrindingBowl; 
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
        return mod_LionKing.proxy.getGrindingBowlRenderID(); 
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(mod_LionKing.instance, mod_LionKing.proxy.GUI_ID_BOWL, world, x, y, z);
        }
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) { 
        return new LKTileEntityGrindingBowl();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block oldBlock, int oldMetadata) { 
        LKTileEntityGrindingBowl tileEntity = (LKTileEntityGrindingBowl) world.getTileEntity(x, y, z); 
        if (tileEntity != null) {
            Random rand = world.rand;
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
            tileEntity.invalidate(); 
        }
        super.breakBlock(world, x, y, z, oldBlock, oldMetadata);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) { 
        return mod_LionKing.itemGrindingBowl; 
    }
}
