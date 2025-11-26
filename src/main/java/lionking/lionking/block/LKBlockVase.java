package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.client.LKCreativeTabs;
import lionking.mod_LionKing;

import java.util.Random;

public class LKBlockVase extends LKBlock {
    @SideOnly(Side.CLIENT)
    private IIcon[] vaseIcons;

    @SideOnly(Side.CLIENT)
    private static IIcon purpleFlowerIcon;

    public LKBlockVase() {
        super(Material.circuits);
        setCreativeTab(LKCreativeTabs.tabDecorations); 
        float offset = 0.1875F;
        setBlockBounds(offset, 0.0F, offset, 1.0F - offset, 0.75F, 1.0F - offset);
        setHardness(0.5F);
        setResistance(2.0F);
        setStepSound(soundTypeStone);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        if (side == 1) return vaseIcons[1];
        if (side == 0) return mod_LionKing.pridestone.getIcon(2, 0);
        return vaseIcons[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) { 
        vaseIcons = new IIcon[2];
        vaseIcons[0] = iconRegister.registerIcon("lionking:vase_side");
        vaseIcons[1] = iconRegister.registerIcon("lionking:vase_top");
        purpleFlowerIcon = iconRegister.registerIcon("lionking:vase_purpleFlower");
    }

    @SideOnly(Side.CLIENT)
    public static IIcon getPlantTextureFromMetadata(int metadata) {
        switch (metadata) {
            case 0: return mod_LionKing.whiteFlower.getIcon(2, 0);
            case 1: return mod_LionKing.blueFlower.getIcon(2, 0);
            case 2: return purpleFlowerIcon;
            case 3: return mod_LionKing.flowerTop.getIcon(2, 1);
            case 4: return mod_LionKing.sapling.getIcon(2, 0);
            case 5: return mod_LionKing.forestSapling.getIcon(2, 0);
            case 6: return mod_LionKing.mangoSapling.getIcon(2, 0);
            case 7: return mod_LionKing.outshroom.getIcon(2, 0);
            case 8: return mod_LionKing.outshroomGlowing.getIcon(2, 0);
            case 9: return mod_LionKing.passionSapling.getIcon(2, 0);
            case 10: return mod_LionKing.bananaSapling.getIcon(2, 0);
            default: return mod_LionKing.pumbaaBox.getIcon(1, 0);
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
    public int getRenderType() {
        return mod_LionKing.proxy.getVaseRenderID();
    }

    @Override
    public Item getItemDropped(int metadata, Random random, int fortune) { 
        return mod_LionKing.vase; 
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.doesBlockHaveSolidTopSurface(world, x, y - 1, z) &&
               super.canPlaceBlockAt(world, x, y, z) &&
               world.isAirBlock(x, y + 1, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlock) { 
        if (!world.isRemote) {
            super.onNeighborBlockChange(world, x, y, z, neighborBlock);
            checkVaseChange(world, x, y, z);
        }
    }

    protected final void checkVaseChange(World world, int x, int y, int z) {
        if (!canBlockStay(world, x, y, z)) {
            dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
        }
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        return world.doesBlockHaveSolidTopSurface(world, x, y - 1, z) &&
               world.isAirBlock(x, y + 1, z);
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        int metadata = world.getBlockMetadata(x, y, z);
        if (metadata == 3 || metadata == 9) return 11;
        if (metadata == 8) return 13;
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) { 
        return mod_LionKing.vase; 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        if (world.getBlockMetadata(x, y, z) == 9) {
            for (int i = 0; i < 4; i++) {
                double posX = x + random.nextFloat();
                double posY = y + 0.75F + random.nextFloat();
                double posZ = z + random.nextFloat();
                double motionX = (-0.5F + random.nextFloat()) * 0.01F;
                double motionY = random.nextFloat() * 0.01F;
                double motionZ = (-0.5F + random.nextFloat()) * 0.01F;
                mod_LionKing.proxy.spawnParticle("passion", posX, posY, posZ, motionX, motionY, motionZ);
            }
        }
    }
}