package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MathHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.util.StatCollector;
import lionking.mod_LionKing;

import java.util.Random;
import java.util.List;

public abstract class LKEntitySpear extends Entity {
    private static final float SIZE = 0.5F;
    private static final float INITIAL_OFFSET_XZ = 0.16F;
    private static final double INITIAL_OFFSET_Y = 0.1D;
    private static final float VELOCITY_FACTOR = 1.5F;
    private static final double SPREAD_FACTOR = 0.0075D;
    private static final float GRAVITY = 0.05F;
    private static final float DRAG = 0.99F;
    private static final float WATER_DRAG = 0.8F;
    private static final float BOUNCE_FACTOR = -0.1F;
    private static final float PENETRATION_OFFSET = 0.05F;
    private static final int MAX_TICKS_IN_GROUND = 6000;
    private static final int MIN_TICKS_FOR_COLLISION = 5;
    private static final int BASE_DAMAGE = 7;
    private static final int POISONED_BASE_DAMAGE = 5;
    private static final int MAX_DAMAGE = 160;
    private static final int POISON_DURATION_MIN = 3;
    private static final int POISON_DURATION_MAX = 6;

    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private int inTile;
    private int inData;
    private boolean inGround;
    public boolean doesArrowBelongToPlayer = true;
    public int arrowShake;
    public Entity shootingEntity;
    private int ticksInGround;
    private int ticksInAir;
    private int spearDamage;
    private boolean fished;
    private final Random rand = new Random();

    public LKEntitySpear(World world) {
        super(world);
        setSize(SIZE, SIZE);
    }

    public LKEntitySpear(World world, double x, double y, double z, int damage) {
        this(world);
        spearDamage = damage;
        setPosition(x, y, z);
        yOffset = 0.0F;
    }

    public LKEntitySpear(World world, EntityLivingBase shooter, float velocity, int damage) {
        this(world);
        spearDamage = damage;
        shootingEntity = shooter;
        setLocationAndAngles(shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
        posX -= MathHelper.cos(rotationYaw * (float) Math.PI / 180F) * INITIAL_OFFSET_XZ;
        posY -= INITIAL_OFFSET_Y;
        posZ -= MathHelper.sin(rotationYaw * (float) Math.PI / 180F) * INITIAL_OFFSET_XZ;
        setPosition(posX, posY, posZ);
        yOffset = 0.0F;
        motionX = -MathHelper.sin(rotationYaw * (float) Math.PI / 180F) * MathHelper.cos(rotationPitch * (float) Math.PI / 180F);
        motionZ = MathHelper.cos(rotationYaw * (float) Math.PI / 180F) * MathHelper.cos(rotationPitch * (float) Math.PI / 180F);
        motionY = -MathHelper.sin(rotationPitch * (float) Math.PI / 180F);
        setArrowHeading(motionX, motionY, motionZ, velocity * VELOCITY_FACTOR, 1.0F);
    }

    @Override
    protected void entityInit() {}

    public void setArrowHeading(double x, double y, double z, float speed, float spread) {
        float magnitude = MathHelper.sqrt_double(x * x + y * y + z * z);
        x /= magnitude;
        y /= magnitude;
        z /= magnitude;
        x += rand.nextGaussian() * SPREAD_FACTOR * spread;
        y += rand.nextGaussian() * SPREAD_FACTOR * spread;
        z += rand.nextGaussian() * SPREAD_FACTOR * spread;
        x *= speed;
        y *= speed;
        z *= speed;
        motionX = x;
        motionY = y;
        motionZ = z;
        float horizontalMagnitude = MathHelper.sqrt_double(x * x + z * z);
        prevRotationYaw = rotationYaw = (float) (Math.toDegrees(Math.atan2(x, z)));
        prevRotationPitch = rotationPitch = (float) (Math.toDegrees(Math.atan2(y, horizontalMagnitude)));
        ticksInGround = 0;
    }

    @Override
    public void setVelocity(double x, double y, double z) {
        motionX = x;
        motionY = y;
        motionZ = z;
        if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F) {
            float horizontalMagnitude = MathHelper.sqrt_double(x * x + z * z);
            prevRotationYaw = rotationYaw = (float) (Math.toDegrees(Math.atan2(x, z)));
            prevRotationPitch = rotationPitch = (float) (Math.toDegrees(Math.atan2(y, horizontalMagnitude)));
            setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
            ticksInGround = 0;
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (isBurning()) {
            setDead();
            return;
        }

        updateRotation();
        if (inGround) {
            handleInGround();
        } else {
            handleInAir();
        }
    }

    private void updateRotation() {
        if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F) {
            float horizontalMagnitude = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
            prevRotationYaw = rotationYaw = (float) (Math.toDegrees(Math.atan2(motionX, motionZ)));
            prevRotationPitch = rotationPitch = (float) (Math.toDegrees(Math.atan2(motionY, horizontalMagnitude)));
        }
        if (arrowShake > 0) arrowShake--;
    }

    private void handleInGround() {
        Block block = worldObj.getBlock(xTile, yTile, zTile);
        int blockId = Block.getIdFromBlock(block);
        int metadata = worldObj.getBlockMetadata(xTile, yTile, zTile);

        if (blockId == inTile && metadata == inData) {
            ticksInGround++;
            if (ticksInGround >= MAX_TICKS_IN_GROUND || spearDamage == MAX_DAMAGE) {
                setDead();
            }
        } else {
            inGround = false;
            motionX *= rand.nextFloat() * 0.2F;
            motionY *= rand.nextFloat() * 0.2F;
            motionZ *= rand.nextFloat() * 0.2F;
            ticksInGround = 0;
            ticksInAir = 0;
        }
    }

    private void handleInAir() {
        ticksInAir++;
        Vec3 start = Vec3.createVectorHelper(posX, posY, posZ);
        Vec3 end = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
        MovingObjectPosition hit = worldObj.rayTraceBlocks(start, end, false);

        if (hit != null) {
            end = Vec3.createVectorHelper(hit.hitVec.xCoord, hit.hitVec.yCoord, hit.hitVec.zCoord);
        }

        Entity hitEntity = findHitEntity(start, end);
        if (hitEntity != null) {
            hit = new MovingObjectPosition(hitEntity);
        }

        if (hit != null) {
            handleCollision(hit);
        }

        updatePositionAndMotion();
    }

    private Entity findHitEntity(Vec3 start, Vec3 end) {
        List<Entity> entities = worldObj.getEntitiesWithinAABBExcludingEntity(this,
            boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
        Entity closestEntity = null;
        double minDistance = 0.0D;

        for (Entity entity : entities) {
            if (!entity.canBeCollidedWith() || (entity == shootingEntity && ticksInAir < MIN_TICKS_FOR_COLLISION)) {
                continue;
            }
            AxisAlignedBB expandedBB = entity.boundingBox.expand(0.3F, 0.3F, 0.3F);
            MovingObjectPosition intercept = expandedBB.calculateIntercept(start, end);
            if (intercept == null) continue;

            double distance = start.distanceTo(intercept.hitVec);
            if (distance < minDistance || minDistance == 0.0D) {
                closestEntity = entity;
                minDistance = distance;
            }
        }
        return closestEntity;
    }

    private void handleCollision(MovingObjectPosition hit) {
        if (hit.entityHit != null && !(hit.entityHit instanceof EntityPlayer)) {
            int damage = isPoisoned() ? POISONED_BASE_DAMAGE : BASE_DAMAGE;
            damage += rand.nextInt(4);

            if (hit.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, shootingEntity), damage)) {
                if (hit.entityHit instanceof EntityLivingBase) {
                    applyPoisonEffect((EntityLivingBase) hit.entityHit);
                }
                worldObj.playSoundAtEntity(this, "random.bowhit", 1.0F, 1.2F / (rand.nextFloat() * 0.2F + 0.9F));
                onCollideWithLiving(hit.entityHit);
            } else {
                bounceBack();
            }
        } else {
            stickInBlock(hit);
        }
    }

    private void applyPoisonEffect(EntityLivingBase entity) {
        if (isPoisoned() && rand.nextInt(4) != 0) {
            entity.addPotionEffect(new PotionEffect(Potion.poison.id,
                (rand.nextInt(POISON_DURATION_MAX - POISON_DURATION_MIN + 1) + POISON_DURATION_MIN) * 20, 0));
        }
    }

    private void bounceBack() {
        motionX *= BOUNCE_FACTOR;
        motionY *= BOUNCE_FACTOR;
        motionZ *= BOUNCE_FACTOR;
        rotationYaw += 180.0F;
        prevRotationYaw += 180.0F;
        ticksInAir = 0;
    }

    private void stickInBlock(MovingObjectPosition hit) {
        xTile = hit.blockX;
        yTile = hit.blockY;
        zTile = hit.blockZ;
        Block block = worldObj.getBlock(xTile, yTile, zTile);
        inTile = Block.getIdFromBlock(block);
        inData = worldObj.getBlockMetadata(xTile, yTile, zTile);
        motionX = (float) (hit.hitVec.xCoord - posX);
        motionY = (float) (hit.hitVec.yCoord - posY);
        motionZ = (float) (hit.hitVec.zCoord - posZ);
        float magnitude = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
        posX -= motionX / magnitude * PENETRATION_OFFSET;
        posY -= motionY / magnitude * PENETRATION_OFFSET;
        posZ -= motionZ / magnitude * PENETRATION_OFFSET;
        worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (rand.nextFloat() * 0.2F + 0.9F));
        inGround = true;
        arrowShake = 7;
    }

    private void updatePositionAndMotion() {
        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        float horizontalMagnitude = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
        rotationYaw = (float) (Math.toDegrees(Math.atan2(motionX, motionZ)));
        rotationPitch = (float) (Math.toDegrees(Math.atan2(motionY, horizontalMagnitude)));

        smoothRotation();
        applyDragAndGravity();
        spawnTrailParticles();
        setPosition(posX, posY, posZ);
    }

    private void smoothRotation() {
        while (rotationPitch - prevRotationPitch < -180F) prevRotationPitch -= 360F;
        while (rotationPitch - prevRotationPitch >= 180F) prevRotationPitch += 360F;
        while (rotationYaw - prevRotationYaw < -180F) prevRotationYaw -= 360F;
        while (rotationYaw - prevRotationYaw >= 180F) prevRotationYaw += 360F;
        rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
        rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
    }

    private void applyDragAndGravity() {
        float drag = isInWater() ? WATER_DRAG : DRAG;
        motionX *= drag;
        motionY *= drag;
        motionZ *= drag;
        motionY -= GRAVITY;

        if (isInWater() && !worldObj.isRemote && shootingEntity != null && !isPoisoned() && !fished && rand.nextInt(16) == 0) {
            spawnFishAndXP();
        }
    }

    private void spawnFishAndXP() {
        EntityItem fish = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Items.fish));
        double dx = shootingEntity.posX - posX;
        double dy = shootingEntity.posY - posY;
        double dz = shootingEntity.posZ - posZ;
        double distance = MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz);
        fish.motionX = dx * 0.1D;
        fish.motionY = dy * 0.1D + MathHelper.sqrt_double(distance) * 0.08D;
        fish.motionZ = dz * 0.1D;
        worldObj.spawnEntityInWorld(fish);
        shootingEntity.worldObj.spawnEntityInWorld(new EntityXPOrb(shootingEntity.worldObj,
            shootingEntity.posX, shootingEntity.posY + 0.5D, shootingEntity.posZ + 0.5D, rand.nextInt(3) + 1));
        fished = true;
    }

    private void spawnTrailParticles() {
        if (isInWater()) {
            for (int i = 0; i < 4; i++) {
                float offset = 0.25F;
                worldObj.spawnParticle("bubble", posX - motionX * offset, posY - motionY * offset, posZ - motionZ * offset,
                    motionX, motionY, motionZ);
            }
        }
        for (int i = 0; i < 4; i++) {
            worldObj.spawnParticle("crit", posX + motionX * i / 4D, posY + motionY * i / 4D, posZ + motionZ * i / 4D,
                -motionX, -motionY + 0.2D, -motionZ); 
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setShort("Damage", (short) spearDamage);
        nbt.setShort("xTile", (short) xTile);
        nbt.setShort("yTile", (short) yTile);
        nbt.setShort("zTile", (short) zTile);
        nbt.setByte("inTile", (byte) inTile);
        nbt.setByte("inData", (byte) inData);
        nbt.setByte("shake", (byte) arrowShake);
        nbt.setByte("inGround", (byte) (inGround ? 1 : 0));
        nbt.setBoolean("player", doesArrowBelongToPlayer);
        nbt.setBoolean("fished", fished);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        spearDamage = nbt.getShort("Damage");
        xTile = nbt.getShort("xTile");
        yTile = nbt.getShort("yTile");
        zTile = nbt.getShort("zTile");
        inTile = nbt.getByte("inTile") & 0xFF;
        inData = nbt.getByte("inData") & 0xFF;
        arrowShake = nbt.getByte("shake") & 0xFF;
        inGround = nbt.getByte("inGround") == 1;
        doesArrowBelongToPlayer = nbt.getBoolean("player");
        fished = nbt.getBoolean("fished");
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer player) {
        if (!worldObj.isRemote && inGround && doesArrowBelongToPlayer && arrowShake <= 0 && spearDamage < MAX_DAMAGE) {
            dropSpearItem();
            setDead();
        }
    }

    public void onCollideWithLiving(Entity entity) {
        if (!worldObj.isRemote && doesArrowBelongToPlayer && arrowShake <= 0 && spearDamage < MAX_DAMAGE) {
            dropSpearItem();
            setDead();
        }
    }

    private void dropSpearItem() {
        Item spearItem = isPoisoned() ? mod_LionKing.poisonedSpear : mod_LionKing.gemsbokSpear;
        EntityItem item = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(spearItem, 1, spearDamage + 1));
        item.delayBeforeCanPickup = 0;
        worldObj.spawnEntityInWorld(item);
    }

    public abstract boolean isPoisoned();

    @Override
    public float getShadowSize() {
        return 0.0F;
    }

    public String getEntityName() {
        return StatCollector.translateToLocal("entity.lionking.spear.name");
    }
}