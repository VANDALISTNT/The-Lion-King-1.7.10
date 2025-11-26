package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper; 

import lionking.mod_LionKing;

public class LKEntityBlueDart extends LKEntityDart {
    public LKEntityBlueDart(World world) {
        super(world); 
    }

    public LKEntityBlueDart(World world, double d, double d1, double d2) {
        super(world, d, d1, d2); 
    }

    public LKEntityBlueDart(World world, EntityLivingBase entityliving, float f, boolean flag) {
        super(world, entityliving, f, flag); 
    }

    @Override
    public ItemStack getDartItem() {
        return new ItemStack(mod_LionKing.dartBlue); 
    }

    @Override
    public int getDamage() {
        return 7; 
    }

    @Override
    public void onHitEntity(EntityLivingBase hitEntity) {
        if (!worldObj.isRemote) {
            setDead();
        }
    }
    
    @Override
    public void spawnParticles() {
        
    }

    @Override
    public float getSpeedReduction() {
        return 0.05F; 
    }

    @Override
    public void inGroundEvent() { 
        if (!worldObj.isRemote) {
            int i = MathHelper.floor_double(posX);
            int j = MathHelper.floor_double(posY);
            int k = MathHelper.floor_double(posZ);
            setDead();
        }
    }
}
