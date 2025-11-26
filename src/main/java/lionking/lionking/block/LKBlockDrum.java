package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon; 
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.client.LKCreativeTabs;
import lionking.mod_LionKing;
import lionking.tileentity.LKTileEntityDrum;

import java.util.List;
import java.util.Random;

public class LKBlockDrum extends BlockContainer {
    @SideOnly(Side.CLIENT)
    private IIcon drumTopIcon; 

    @SideOnly(Side.CLIENT)
    private IIcon[] drumSideIcons; 

    private static final String[] DRUM_TYPES = {"acacia", "rainforest", "mango", "passion", "banana", "deadwood"};

    public LKBlockDrum() { 
        super(Material.wood);
        float offset = 0.0625F;
        setBlockBounds(offset, 0.0F, offset, 1.0F - offset, 0.75F, 1.0F - offset);
        setCreativeTab(LKCreativeTabs.tabDecorations); 
        setHardness(2.0F); 
        setStepSound(soundTypeWood); 
        setBlockName("drum"); 
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) { 
        return new LKTileEntityDrum();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) { 
        if (side == 0) {
            Block woodBlock = metadata < 4 ? mod_LionKing.prideWood : mod_LionKing.prideWood2;
            return woodBlock.getIcon(2, metadata < 4 ? metadata : metadata - 4); 
        } else if (side == 1) {
            return drumTopIcon; 
        }
        return drumSideIcons[Math.min(metadata, DRUM_TYPES.length - 1)]; 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) { 
        drumTopIcon = iconRegister.registerIcon("lionking:drum_top");
        drumSideIcons = new IIcon[DRUM_TYPES.length];
        for (int i = 0; i < DRUM_TYPES.length; i++) {
            drumSideIcons[i] = iconRegister.registerIcon("lionking:drum_side_" + DRUM_TYPES[i]);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) { 
        for (int meta = 0; meta <= 5; meta++) {
            list.add(new ItemStack(this, 1, meta)); 
        }
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
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        LKTileEntityDrum drum = (LKTileEntityDrum) world.getTileEntity(x, y, z); 
        if (drum == null) return false;

        ItemStack heldItem = player.inventory.getCurrentItem();
        if (heldItem != null && heldItem.getItem() == mod_LionKing.staff) { 
            if (!world.isRemote) {
                player.openGui(mod_LionKing.instance, mod_LionKing.proxy.GUI_ID_DRUM, world, x, y, z);
            }
            return true;
        }

        if (!world.isRemote) { 
            drum.changePitch();
            playNote(world, x, y, z, drum.note);
        }
        return true;
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
        LKTileEntityDrum drum = (LKTileEntityDrum) world.getTileEntity(x, y, z); 
        if (drum != null && !world.isRemote) { 
            playNote(world, x, y, z, drum.note);
        }
    }

    private void playNote(World world, int x, int y, int z, int note) {
        float pitch = (float) Math.pow(2.0D, (double) (note - 12) / 12.0D);
        world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "lionking:bongo", 3.0F, pitch);
        world.spawnParticle("note", x + 0.5D, y + 1.2D, z + 0.5D, (double) note / 24.0D, 0.0D, 0.0D);
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block oldBlock, int oldMetadata) { 
        if (!world.isRemote) { 
            LKTileEntityDrum drum = (LKTileEntityDrum) world.getTileEntity(x, y, z); 
            if (drum != null) {
                Random rand = world.rand;
                for (int slot = 0; slot < drum.getSizeInventory(); slot++) {
                    ItemStack stack = drum.getStackInSlot(slot);
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
}