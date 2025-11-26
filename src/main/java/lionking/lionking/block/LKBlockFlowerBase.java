package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.mod_LionKing;

import java.util.Random;

public class LKBlockFlowerBase extends Block {
    @SideOnly(Side.CLIENT)
    private IIcon[] flowerIcons;

    private static final String[] FLOWER_NAMES = {"purpleFlower_base", "redFlower_base"};

    public LKBlockFlowerBase() {
        super(Material.plants);
        setTickRandomly(true);
        float width = 0.2F;
        setBlockBounds(0.5F - width, 0.0F, 0.5F - width, 0.5F + width, 1.0F, 0.5F + width);
        setCreativeTab(null);
        setHardness(0.0F);
        setStepSound(soundTypeGrass);
        setBlockName("flowerBase");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        return flowerIcons[Math.min(metadata, FLOWER_NAMES.length - 1)];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        flowerIcons = new IIcon[FLOWER_NAMES.length];
        for (int i = 0; i < FLOWER_NAMES.length; i++) {
            flowerIcons[i] = iconRegister.registerIcon("lionking:" + FLOWER_NAMES[i]);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        Block blockBelow = world.getBlock(x, y - 1, z);
        return super.canPlaceBlockAt(world, x, y, z) && canThisPlantGrowOnThisBlock(blockBelow);
    }

    private boolean canThisPlantGrowOnThisBlock(Block block) {
        return block == Blocks.grass || block == Blocks.dirt || block == Blocks.farmland;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlock) {
        if (!world.isRemote) {
            checkFlowerChange(world, x, y, z);
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!world.isRemote) {
            checkFlowerChange(world, x, y, z);
        }
    }

    private void checkFlowerChange(World world, int x, int y, int z) {
        Block blockAbove = world.getBlock(x, y + 1, z);
        if (blockAbove != mod_LionKing.flowerTop || !canBlockStay(world, x, y, z)) {
            world.setBlockToAir(x, y, z);
            world.setBlockToAir(x, y + 1, z);
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block oldBlock, int oldMetadata) {
        if (!world.isRemote) {
            ItemStack drop = new ItemStack(world.getBlockMetadata(x, y, z) == 0 ? mod_LionKing.purpleFlower : mod_LionKing.redFlower);
            dropBlockAsItem(world, x, y, z, drop);
        }
        super.breakBlock(world, x, y, z, oldBlock, oldMetadata);
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int metadata) {
        if (!world.isRemote) {
            checkFlowerChange(world, x, y, z);
        }
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        boolean hasEnoughLight = world.getBlockMetadata(x, y, z) == 1 || world.getFullBlockLightValue(x, y, z) >= 8;
        Block blockBelow = world.getBlock(x, y - 1, z);
        return (hasEnoughLight || world.canBlockSeeTheSky(x, y, z)) && canThisPlantGrowOnThisBlock(blockBelow);
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
        return 1;
    }

    @Override
    public Item getItemDropped(int metadata, Random random, int fortune) {
        return metadata == 1 ? mod_LionKing.redFlower : mod_LionKing.purpleFlower;
    }

    @Override
    public int quantityDropped(Random random) {
        return 1; 
    }
}
