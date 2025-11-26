package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.client.LKCreativeTabs;
import lionking.tileentity.LKTileEntityFurRug;

import java.util.List;

public class LKBlockRug extends BlockContainer {
    public static final String[] COLOUR_NAMES = {"Default", "White", "Blue", "Yellow", "Red", "Purple", "Light Blue", "Green", "Orange", "Light Grey", "Grey", "Black", "Outlander", "Violet", "Pink", "Light Green"};
    public static final String[] COLOUR_NAMES_US = {"Default", "White", "Blue", "Yellow", "Red", "Purple", "Light Blue", "Green", "Orange", "Light Gray", "Gray", "Black", "Outlander", "Violet", "Pink", "Light Green"};

    @SideOnly(Side.CLIENT)
    private IIcon[] rugIcons;

    private static final String[] RUG_TYPES = {"white", "blue", "yellow", "red", "purple", "lightBlue", "green", "orange", "lightGrey", "grey", "black", "outlander", "violet", "pink", "lightGreen"};

    public LKBlockRug() {
        super(Material.cloth);
        setBlockBounds(0F, 0F, 0F, 1F, 0.0625F, 1F);
        setCreativeTab(LKCreativeTabs.TAB_DECORATIONS);
        setHardness(0.8F);
        setStepSound(soundTypeCloth);
        setBlockName("furRug");
        this.blockResistance = 4.0F;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new LKTileEntityFurRug();
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        float thickness = 0.0625F;
        setBlockBounds(0F, 0F, 0F, 1F, thickness, 1F);
    }

    @Override
    public void setBlockBoundsForItemRender() {
        float thickness = 0.03125F;
        setBlockBounds(0F, 0.5F - thickness, 0F, 1F, 0.5F + thickness, 1F);
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        if (metadata == 0) {
            return super.getIcon(side, metadata);
        }
        return rugIcons[metadata - 1];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("lionking:rug_default");
        rugIcons = new IIcon[RUG_TYPES.length];
        for (int i = 0; i < RUG_TYPES.length; i++) {
            rugIcons[i] = iconRegister.registerIcon("lionking:rug_" + RUG_TYPES[i]);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < COLOUR_NAMES.length; i++) {
            list.add(new ItemStack(this, 1, i));
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
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side) {
        ForgeDirection dir = ForgeDirection.getOrientation(side).getOpposite();
        return world.isSideSolid(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, dir.getOpposite());
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlock) {
        if (!world.isRemote) {
            if (!canBlockStay(world, x, y, z)) {
                dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                world.setBlockToAir(x, y, z);
            }
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (!(te instanceof LKTileEntityFurRug)) return false;
        int direction = ((LKTileEntityFurRug) te).getDirection();
        ForgeDirection dir = ForgeDirection.getOrientation(direction);
        return world.isSideSolid(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, dir.getOpposite());
    }

    public int getMobilityFlag() { 
        return 1;
    }
}
