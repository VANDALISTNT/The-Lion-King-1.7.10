package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.World;
import net.minecraft.world.IBlockAccess; 
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lionking.mod_LionKing;

import java.util.Random;

public class LKBlockTilledSand extends LKBlock {
    @SideOnly(Side.CLIENT)
    private IIcon[] tilledSandIcons;

    public LKBlockTilledSand() {
        super(Material.sand);
        setTickRandomly(true);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
        setLightOpacity(255);
        setCreativeTab(null);
        setHardness(0.5F);
        setStepSound(Block.soundTypeSand);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1);
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
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        return side == 1 ? (metadata > 0 ? tilledSandIcons[1] : tilledSandIcons[0]) : Blocks.sand.getIcon(side, 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        tilledSandIcons = new IIcon[2];
        tilledSandIcons[0] = iconRegister.registerIcon("lionking:tilledSand");
        tilledSandIcons[1] = iconRegister.registerIcon("lionking:tilledSand_hydrated");
    }

    @Override
    public Item getItemDropped(int metadata, Random random, int fortune) {
        return Item.getItemFromBlock(Blocks.sand);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!world.isRemote) {
            tryToFall(world, x, y, z);
            if (!isWaterNearby(world, x, y, z) && !world.canLightningStrikeAt(x, y + 1, z)) {
                int moisture = world.getBlockMetadata(x, y, z);
                if (moisture > 0) {
                    world.setBlockMetadataWithNotify(x, y, z, moisture - 1, 3);
                } else if (!isCropsNearby(world, x, y, z)) {
                    world.setBlock(x, y, z, Blocks.sand, 0, 3);
                }
            } else if (world.getBlockMetadata(x, y, z) < 7) {
                world.setBlockMetadataWithNotify(x, y, z, 7, 3);
            }
        }
    }

    @Override
    public void onFallenUpon(World world, int x, int y, int z, Entity entity, float fallDistance) {
        if (!world.isRemote && world.rand.nextFloat() < fallDistance - 0.5F) {
            world.setBlock(x, y, z, Blocks.sand, 0, 3);
        }
    }

    private boolean isCropsNearby(World world, int x, int y, int z) {
        Block blockAbove = world.getBlock(x, y + 1, z);
        return blockAbove == mod_LionKing.kiwanoStem;
    }

    private boolean isWaterNearby(World world, int x, int y, int z) {
        for (int dx = x - 4; dx <= x + 4; dx++) {
            for (int dy = y; dy <= y + 1; dy++) {
                for (int dz = z - 4; dz <= z + 4; dz++) {
                    if (world.getBlock(dx, dy, dz).getMaterial() == Material.water) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plant) {
        Block plantBlock = plant.getPlant(world, x, y + 1, z);
        return plantBlock == mod_LionKing.kiwanoStem;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlock) {
        if (!world.isRemote) {
            tryToFall(world, x, y, z);
            if (world.getBlock(x, y + 1, z).getMaterial().isSolid()) {
                world.setBlock(x, y, z, Blocks.sand, 0, 3);
            }
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        if (!world.isRemote) {
            tryToFall(world, x, y, z);
        }
    }

    private void tryToFall(World world, int x, int y, int z) {
        if (BlockFalling.func_149831_e(world, x, y - 1, z) && y >= 0) {
            int range = 32;
            if (!world.isRemote && world.checkChunksExist(x - range, y - range, z - range, x + range, y + range, z + range)) {
                world.setBlock(x, y, z, Blocks.sand, 0, 3);
                Blocks.sand.onBlockAdded(world, x, y, z);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) {
        return Item.getItemFromBlock(Blocks.sand);
    }
}