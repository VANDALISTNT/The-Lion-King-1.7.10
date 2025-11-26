package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;

import lionking.mod_LionKing;

public class LKEntityPinkDart extends LKEntityDart {
    public LKEntityPinkDart(World world) {
        super(world);
    }

    public LKEntityPinkDart(World world, double d, double d1, double d2) {
        super(world, d, d1, d2);
    }

    public LKEntityPinkDart(World world, EntityLivingBase entityliving, float f, boolean flag) {
        super(world, entityliving, f, flag);
    }

    @Override
    public ItemStack getDartItem() {
        return new ItemStack(mod_LionKing.dartPink);
    }

    @Override
    public int getDamage() {
        return 7;
    }

    @Override
    public void onHitEntity(EntityLivingBase hitEntity) {
        if (shootingEntity != null && shootingEntity instanceof EntityLivingBase && worldObj.rand.nextBoolean()) {
            EntityLivingBase shootingEntityLiving = (EntityLivingBase) shootingEntity;
            if (shootingEntityLiving.getHealth() < shootingEntityLiving.getMaxHealth()) {
                int healthBonus = 1 + (silverFired ? worldObj.rand.nextInt(3) : worldObj.rand.nextInt(2));
                shootingEntityLiving.heal((float) healthBonus);
            }
        }
    }

    @Override
    public void spawnParticles() {
        mod_LionKing.proxy.spawnParticle("passion", posX, posY, posZ, -motionX * 0.1, -motionY * 0.1, -motionZ * 0.1);
    }

    @Override
    public float getSpeedReduction() {
        return 0.04F;
    }
}