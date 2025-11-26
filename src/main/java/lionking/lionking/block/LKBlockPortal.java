package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.mod_LionKing;

import java.util.Random;

public class LKBlockPortal extends BlockBreakable {
    public LKBlockPortal() { 
        super("lionking:portal", Material.portal, false); 
        setCreativeTab(null);
        setHardness(-1.0F);
        setStepSound(soundTypeGlass);
        setLightLevel(0.75F);
        setBlockName("lionPortal"); 
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        if (world.getBlock(x - 1, y, z) == this || world.getBlock(x + 1, y, z) == this) {
            setBlockBounds(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
        } else {
            setBlockBounds(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
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

    public static boolean tryToCreatePortal(World world, int x, int y, int z) {
        int axisX = 0;
        int axisZ = 0;
        if (world.getBlock(x - 1, y, z) == mod_LionKing.lionPortalFrame || world.getBlock(x + 1, y, z) == mod_LionKing.lionPortalFrame) {
            axisX = 1;
        }
        if (world.getBlock(x, y, z - 1) == mod_LionKing.lionPortalFrame || world.getBlock(x, y, z + 1) == mod_LionKing.lionPortalFrame) {
            axisZ = 1;
        }
        if (axisX == axisZ) return false;

        if (world.isAirBlock(x - axisX, y, z - axisZ)) { 
            x -= axisX;
            z -= axisZ;
        }

        for (int offsetX = -1; offsetX <= 2; offsetX++) {
            for (int offsetY = -1; offsetY <= 3; offsetY++) {
                boolean isEdge = offsetX == -1 || offsetX == 2 || offsetY == -1 || offsetY == 3;
                if ((offsetX == -1 || offsetX == 2) && (offsetY == -1 || offsetY == 3)) continue;

                int blockX = x + axisX * offsetX;
                int blockZ = z + axisZ * offsetX;
                if (isEdge) {
                    if (world.getBlock(blockX, y + offsetY, blockZ) != mod_LionKing.lionPortalFrame) return false;
                } else if (!world.isAirBlock(blockX, y + offsetY, blockZ)) {
                    return false;
                }
            }
        }

        for (int width = 0; width < 2; width++) {
            for (int height = 0; height < 3; height++) {
                world.setBlock(x + axisX * width, y + height, z + axisZ * width, mod_LionKing.lionPortal, 0, 2);
            }
        }
        return true;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlock) { 
        int axisX = 0;
        int axisZ = 1;
        if (world.getBlock(x - 1, y, z) == this || world.getBlock(x + 1, y, z) == this) {
            axisX = 1;
            axisZ = 0;
        }

        int bottomY = y;
        while (world.getBlock(x, bottomY - 1, z) == this) bottomY--;
        if (world.getBlock(x, bottomY - 1, z) != mod_LionKing.lionPortalFrame) {
            world.setBlockToAir(x, y, z);
            return;
        }

        int height;
        for (height = 1; height < 4 && world.getBlock(x, bottomY + height, z) == this; height++) {}
        if (height != 3 || world.getBlock(x, bottomY + height, z) != mod_LionKing.lionPortalFrame) {
            world.setBlockToAir(x, y, z);
            return;
        }

        boolean hasXNeighbors = world.getBlock(x - 1, y, z) == this || world.getBlock(x + 1, y, z) == this;
        boolean hasZNeighbors = world.getBlock(x, y, z - 1) == this || world.getBlock(x, y, z + 1) == this;
        if (hasXNeighbors && hasZNeighbors) {
            world.setBlockToAir(x, y, z);
            return;
        }

        if ((world.getBlock(x + axisX, y, z + axisZ) != mod_LionKing.lionPortalFrame || world.getBlock(x - axisX, y, z - axisZ) != this) &&
            (world.getBlock(x - axisX, y, z - axisZ) != mod_LionKing.lionPortalFrame || world.getBlock(x + axisX, y, z + axisZ) != this)) {
            world.setBlockToAir(x, y, z);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        if (world.getBlock(x, y, z) == this) return false;

        boolean westPortal = world.getBlock(x - 1, y, z) == this && world.getBlock(x - 2, y, z) != this;
        boolean eastPortal = world.getBlock(x + 1, y, z) == this && world.getBlock(x + 2, y, z) != this;
        boolean northPortal = world.getBlock(x, y, z - 1) == this && world.getBlock(x, y, z - 2) != this;
        boolean southPortal = world.getBlock(x, y, z + 1) == this && world.getBlock(x, y, z + 2) != this;

        boolean xAxis = westPortal || eastPortal;
        boolean zAxis = northPortal || southPortal;

        return (xAxis && (side == 4 || side == 5)) || (zAxis && (side == 2 || side == 3));
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (entity.ridingEntity == null && entity.riddenByEntity == null && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            mod_LionKing.proxy.setInPrideLandsPortal(player);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        if (random.nextInt(100) == 0) {
            world.playSound(x + 0.5D, y + 0.5D, z + 0.5D, "portal.portal", 1.0F, random.nextFloat() * 0.4F + 0.8F, false);
        }
        for (int i = 0; i < 4; i++) {
            double posX = x + random.nextFloat();
            double posY = y + random.nextFloat();
            double posZ = z + random.nextFloat();
            double motionX = (random.nextFloat() - 0.5D) * 0.5D;
            double motionY = (random.nextFloat() - 0.5D) * 0.5D;
            double motionZ = (random.nextFloat() - 0.5D) * 0.5D;

            int direction = random.nextInt(2) * 2 - 1;
            if (world.getBlock(x - 1, y, z) == this || world.getBlock(x + 1, y, z) == this) {
                posZ = z + 0.5D + 0.25D * direction;
                motionZ = random.nextFloat() * 2.0F * direction;
            } else {
                posX = x + 0.5D + 0.25D * direction;
                motionX = random.nextFloat() * 2.0F * direction;
            }
            mod_LionKing.proxy.spawnParticle("prideLandsPortal", posX, posY, posZ, motionX, motionY, motionZ);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) { 
        return null; 
    }
}