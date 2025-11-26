package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.StatCollector;

public class LKEntityZazuEgg extends EntityThrowable {
    private static final String PARTICLE_TYPE = "snowballpoof";
    private static final int PARTICLE_COUNT = 8;
    private static final int ZAZU_CHILD_AGE = -24000;

    public LKEntityZazuEgg(World world) {
        super(world);
    }

    public LKEntityZazuEgg(World world, EntityLivingBase thrower) {
        super(world, thrower);
    }

    public LKEntityZazuEgg(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Override
    protected void onImpact(MovingObjectPosition hit) {
        if (hit.entityHit != null) {
            hit.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), 0.0F);
        }

        if (!worldObj.isRemote) {
            if (rand.nextBoolean()) {
                spawnZazu();
            }
            setDead();
        }

        spawnImpactParticles();
    }

    private void spawnZazu() {
        LKEntityZazu zazu = new LKEntityZazu(worldObj);
        zazu.setGrowingAge(ZAZU_CHILD_AGE);
        zazu.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
        worldObj.spawnEntityInWorld(zazu);
    }

    private void spawnImpactParticles() {
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            worldObj.spawnParticle(PARTICLE_TYPE, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
        }
    }

    public String getEntityName() {
        return StatCollector.translateToLocal("entity.lionking.zazuEgg.name");
    }
}