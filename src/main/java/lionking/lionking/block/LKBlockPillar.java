package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import lionking.client.LKCreativeTabs;
import lionking.mod_LionKing;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

public class LKBlockPillar extends Block {
    @SideOnly(Side.CLIENT)
    private IIcon[] pillarIcons;

    @SideOnly(Side.CLIENT)
    private IIcon[] corruptIcons;

    public LKBlockPillar() {
        super(Material.rock);
        setCreativeTab(LKCreativeTabs.tabBlock);
        setHardness(1.5F);
        setResistance(10.0F);
        setStepSound(soundTypeStone);
        setBlockName("pillar");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        boolean isCorrupt = metadata > 3 && metadata < 8;
        IIcon[] icons = isCorrupt ? corruptIcons : pillarIcons;
        return (side == 0 || side == 1) ? icons[1] : icons[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        pillarIcons = new IIcon[2];
        corruptIcons = new IIcon[2];
        pillarIcons[0] = iconRegister.registerIcon("lionking:pridestonePillar_side");
        pillarIcons[1] = iconRegister.registerIcon("lionking:pridestonePillar_top");
        corruptIcons[0] = iconRegister.registerIcon("lionking:pridestonePillar_corrupt_side");
        corruptIcons[1] = iconRegister.registerIcon("lionking:pridestonePillar_corrupt_top");
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int size = getSizeFromMetadata(world.getBlockMetadata(x, y, z));
        float offset = 0.125F * size;
        setBlockBounds(offset, 0.0F, offset, 1.0F - offset, 1.0F, 1.0F - offset);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        int size = getSizeFromMetadata(world.getBlockMetadata(x, y, z));
        double offset = 0.125D * size;
        return AxisAlignedBB.getBoundingBox(x + offset, y, z + offset, x + 1.0D - offset, y + 1.0D, z + 1.0D - offset);
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
        return mod_LionKing.proxy.getPillarRenderID();
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        int metadata = world.getBlockMetadata(x, y, z);
        return (metadata == 0 || metadata == 4) || side == ForgeDirection.UP;
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        int metadata = world.getBlockMetadata(x, y, z);
        return (metadata > 3 && metadata < 8) ? blockHardness * 0.7F : blockHardness;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < 8; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    private int getSizeFromMetadata(int metadata) {
        return (metadata > 3 && metadata < 8) ? metadata - 4 : metadata;
    }
}