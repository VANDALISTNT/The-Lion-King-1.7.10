package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.entity.EntityLivingBase;

public class LKEntityThrownTermite extends EntityThrowable {
    private static final float EXPLOSION_POWER = 1.8F;
    private static final int SMOKE_PARTICLE_COUNT = 8;
    private static final String PARTICLE_TYPE = "smoke";

    public LKEntityThrownTermite(World world) {
        super(world);
    }

    public LKEntityThrownTermite(World world, EntityLivingBase thrower) {
        super(world, thrower);
    }

    public LKEntityThrownTermite(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Override
    protected void onImpact(MovingObjectPosition hit) {
        if (hit.entityHit != null) {
            hit.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), 0.0F);
        }

        if (!worldObj.isRemote) {
            worldObj.createExplosion(this, posX, posY, posZ, EXPLOSION_POWER, true);
            setDead();
        }

        spawnImpactParticles();
    }

    private void spawnImpactParticles() {
        for (int i = 0; i < SMOKE_PARTICLE_COUNT; i++) {
            worldObj.spawnParticle(PARTICLE_TYPE, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
        }
    }
}