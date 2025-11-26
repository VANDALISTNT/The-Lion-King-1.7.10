package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MathHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import lionking.mod_LionKing;
import lionking.common.LKAchievementList;

import java.util.List;

public class LKEntityPumbaaBomb extends EntityThrowable {
    public LKEntityPumbaaBomb(World world) {
        super(world);
    }

    public LKEntityPumbaaBomb(World world, EntityLivingBase entityliving) {
        super(world, entityliving);
    }

    public LKEntityPumbaaBomb(World world, double d, double d1, double d2) {
        super(world, d, d1, d2);
    }

    @Override
    protected void onImpact(MovingObjectPosition movingobjectposition) {
        if (movingobjectposition.entityHit != null) {
            movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), 0F);
        }
        if (!worldObj.isRemote) {
            explode();
        }

        worldObj.playSoundEffect(posX, posY, posZ, "lionking:flatulence", 4F, (1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
        worldObj.spawnParticle("hugeexplosion", posX, posY, posZ, 0.0D, 0.0D, 0.0D);

        if (!worldObj.isRemote) {
            setDead();
        }
    }

    private void explode() {
        float explosionSize = 14F + (worldObj.rand.nextFloat() * 5.0F);

        int l = MathHelper.floor_double(posX - (double)explosionSize - 1.0D);
        int i1 = MathHelper.floor_double(posX + (double)explosionSize + 1.0D);
        int k1 = MathHelper.floor_double(posY - (double)explosionSize - 1.0D);
        int l1 = MathHelper.floor_double(posY + (double)explosionSize + 1.0D);
        int i2 = MathHelper.floor_double(posZ - (double)explosionSize - 1.0D);
        int j2 = MathHelper.floor_double(posZ + (double)explosionSize + 1.0D);

        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.getBoundingBox(l, k1, i2, i1, l1, j2));
        Vec3 vec = Vec3.createVectorHelper(posX, posY, posZ);

        for (int k2 = 0; k2 < list.size(); k2++) {
            Entity entity = (Entity)list.get(k2);
            double d4 = entity.getDistance(posX, posY, posZ) / (double)explosionSize;
            if (d4 <= 1.0D) {
                double d6 = entity.posX - posX;
                double d8 = entity.posY - posY;
                double d10 = entity.posZ - posZ;
                double d11 = MathHelper.sqrt_double(d6 * d6 + d8 * d8 + d10 * d10);
                d6 /= d11;
                d8 /= d11;
                d10 /= d11;
                double d12 = (double)worldObj.getBlockDensity(vec, entity.boundingBox);
                double d13 = (1.0D - d4) * d12;

                if (entity instanceof EntityLivingBase && !(entity instanceof EntityPlayer)) {
                    int damage = 17 + worldObj.rand.nextInt(6);
                    entity.attackEntityFrom(DamageSource.setExplosionSource(null), damage);
                    if (entity instanceof IMob && getThrower() != null && getThrower() instanceof EntityPlayer) {
                    ((EntityPlayer)getThrower()).triggerAchievement(LKAchievementList.fartBomb);
                    }
                }
                double d14 = d13;
                entity.motionX += d6 * d14;
                entity.motionY += d8 * d14;
                entity.motionZ += d10 * d14;
            }
        }

        for (int i = l; i <= i1; i++) {
            for (int j = k1; j <= l1; j++) {
                for (int k = i2; k <= j2; k++) {
                    ChunkCoordinates coords = new ChunkCoordinates(i, j, k);

                    if (worldObj.rand.nextInt(3) > 0 && Math.sqrt(coords.getDistanceSquared(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ))) / (double)(explosionSize * 0.7F) <= 1D) {
                        Block block = worldObj.getBlock(i, j, k);
                        if (block != null && block != Blocks.air) {
                            int metadata = worldObj.getBlockMetadata(i, j, k);
                            if (block == mod_LionKing.prideBrick && metadata == 0) {
                                worldObj.setBlock(i, j, k, mod_LionKing.prideBrickMossy, 1, 3);
                            } else if (block == Blocks.grass) {
                                worldObj.setBlock(i, j, k, Blocks.dirt, 0, 3);
                            } else if (block instanceof BlockFlower || block.getMaterial() == Material.plants) {
                                block.dropBlockAsItemWithChance(worldObj, i, j, k, metadata, 0.4F, 0);
                                worldObj.setBlockToAir(i, j, k);
                            }
                        }
                    }
                }
            }
        }
    }
}