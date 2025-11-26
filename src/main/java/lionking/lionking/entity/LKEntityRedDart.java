package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.MathHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;

import lionking.mod_LionKing;
import lionking.entity.LKEntityDart;

public class LKEntityRedDart extends LKEntityDart {
    public LKEntityRedDart(World world) {
        super(world);
    }
    
    public LKEntityRedDart(World world, double d, double d1, double d2) {
        super(world, d, d1, d2);
    }
    
    public LKEntityRedDart(World world, EntityLivingBase entityliving, float f, boolean flag) {
        super(world, entityliving, f, flag);
    }
    
    public ItemStack getDartItem() {
        return new ItemStack(mod_LionKing.dartRed);
    }
    
    public int getDamage() {
        return 6;
    }
    
    @Override
    public void onHitEntity(EntityLivingBase hitEntity) {
        if (shootingEntity != null) {
            hitEntity.addVelocity(
                -MathHelper.sin((shootingEntity.rotationYaw * 3.141593F) / 180F) * 0.45F, 
                0.10000000000000001D, 
                MathHelper.cos((shootingEntity.rotationYaw * 3.141593F) / 180F) * 0.45F
            );
        }
        if (!hitEntity.isImmuneToFire()) {
            hitEntity.setFire(silverFired ? worldObj.rand.nextInt(4) + 3 : worldObj.rand.nextInt(2) + 3);
        }
    }
    
    public void spawnParticles() {
        worldObj.spawnParticle("flame", posX, posY, posZ, -motionX * 0.1, -motionY * 0.1, -motionZ * 0.1);
    }
    
    public float getSpeedReduction() {
        return 0.02F;
    }
}