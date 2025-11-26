package lionking.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.item.Item;

import lionking.entity.LKEntityOutlander;
import lionking.entity.LKEntityZira;
import lionking.tileentity.LKTileEntityOutlandsPool;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Random;

public class LKBlockOutlandsPool extends BlockContainer {
    public LKBlockOutlandsPool() {
        super(Material.rock);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.875F, 1.0F);
        setLightLevel(0.875F);
        setCreativeTab(null);
        setBlockName("outlandsPool");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new LKTileEntityOutlandsPool();
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean getBlocksMovement(IBlockAccess world, int x, int y, int z) {
        return true;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        if (world.getBlock(x, y, z) == this) return false;
        return side == 1 || super.shouldSideBeRendered(world, x, y, z, side);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public int getMobilityFlag() {
        return 2;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        return true; 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        if (random.nextBoolean()) {
            world.spawnParticle("smoke", x + random.nextFloat(), y + 0.8F, z + random.nextFloat(), 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (entity instanceof EntityLivingBase && !(entity instanceof LKEntityOutlander) && !(entity instanceof LKEntityZira)) {
            entity.attackEntityFrom(DamageSource.inFire, 2F);
            world.playSoundAtEntity(entity, "random.fizz", 0.7F, 1.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.4F);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) {
        return null;
    }
}