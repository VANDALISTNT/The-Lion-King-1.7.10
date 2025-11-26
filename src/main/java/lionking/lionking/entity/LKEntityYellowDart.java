package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.MathHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.StatCollector;

import lionking.mod_LionKing;
import lionking.entity.LKEntityDart;

public class LKEntityYellowDart extends LKEntityDart {
    private static final int DAMAGE = 8;
    private static final float KNOCKBACK_XZ = 0.45F;
    private static final double KNOCKBACK_Y = 0.1D;
    private static final float SPEED_REDUCTION = 0.02F;
    private static final double PARTICLE_VELOCITY_FACTOR = 0.1;

    public LKEntityYellowDart(World world) {
        super(world);
    }

    public LKEntityYellowDart(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public LKEntityYellowDart(World world, EntityLivingBase shooter, float velocity, boolean isFromDartGun) {
        super(world, shooter, velocity, isFromDartGun);
    }

    @Override
    public ItemStack getDartItem() {
        return new ItemStack(mod_LionKing.dartYellow);
    }

    @Override
    public int getDamage() {
        return DAMAGE;
    }

    @Override
    public void onHitEntity(EntityLivingBase hitEntity) {
        if (shootingEntity != null) {
            float yawRadians = (float) Math.toRadians(shootingEntity.rotationYaw);
            hitEntity.addVelocity(
                -MathHelper.sin(yawRadians) * KNOCKBACK_XZ,
                KNOCKBACK_Y,
                MathHelper.cos(yawRadians) * KNOCKBACK_XZ
            );
        }
    }

    @Override
    public void spawnParticles() {
        worldObj.spawnParticle("smoke", posX, posY, posZ,
            -motionX * PARTICLE_VELOCITY_FACTOR,
            -motionY * PARTICLE_VELOCITY_FACTOR,
            -motionZ * PARTICLE_VELOCITY_FACTOR
        );
    }

    @Override
    public float getSpeedReduction() {
        return SPEED_REDUCTION;
    }

    public String getEntityName() {
        return StatCollector.translateToLocal("entity.lionking.yellowDart.name");
    }
}