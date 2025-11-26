package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.block.BlockSand;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import lionking.entity.LKEntityOutsand;
import lionking.client.LKCreativeTabs;

import java.util.Random;

public class LKBlockOutsand extends Block {
    public LKBlockOutsand() {
        super(Material.sand);
        setCreativeTab(LKCreativeTabs.tabBlock);
        setBlockName("outsand");
    }

    @Override
    public boolean isFireSource(World world, int x, int y, int z, ForgeDirection side) {
        return side == ForgeDirection.UP;
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlock) {
        world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        tryToFall(world, x, y, z);
    }

    private void tryToFall(World world, int x, int y, int z) {
        if (canFallBelow(world, x, y - 1, z) && y >= 0) {
            byte range = 32;
            if (BlockSand.fallInstantly || !world.checkChunksExist(x - range, y - range, z - range, x + range, y + range, z + range)) {
                world.setBlockToAir(x, y, z);
                while (canFallBelow(world, x, y - 1, z) && y > 0) y--;
                if (y > 0) {
                    world.setBlock(x, y, z, this, 0, 3);
                }
            } else if (!world.isRemote) {
                LKEntityOutsand entity = new LKEntityOutsand(world, (double)(x + 0.5F), (double)(y + 0.5F), (double)(z + 0.5F), this);
                world.spawnEntityInWorld(entity);
            }
        }
    }

    @Override
    public int tickRate(World world) {
        return 3;
    }

    public static boolean canFallBelow(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        if (block.isAir(world, x, y, z) || block == Blocks.fire) return true;
        Material material = block.getMaterial();
        return material == Material.water || material == Material.lava;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        double posX = x + 0.5D + (random.nextFloat() - 0.5D) * 0.5D;
        double posY = y + random.nextFloat();
        double posZ = z + random.nextFloat();
        double motionX = random.nextFloat() * 2.0F * (random.nextInt(2) * 2 - 1);
        double motionY = (random.nextFloat() - 0.5D) * 0.5D;
        double motionZ = (random.nextFloat() - 0.5D) * 0.5D;
        world.spawnParticle("smoke", posX, posY, posZ, motionX, motionY, motionZ);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        applyFireDamage(entity, world);
    }

    @Override
    public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
        applyFireDamage(entity, world);
    }

    private void applyFireDamage(Entity entity, World world) {
        if (entity instanceof EntityLivingBase && !entity.isImmuneToFire() && world.rand.nextBoolean()) {
            entity.attackEntityFrom(DamageSource.inFire, 2F);
        }
    }
}
