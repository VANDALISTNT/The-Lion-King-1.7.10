package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MathHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText; 
import net.minecraft.util.StatCollector;

import java.util.List;

public abstract class LKEntityDart extends Entity {
    private int xTile;
    private int yTile;
    private int zTile;
    private int inTile;
    private int inData;
    private boolean inGround;
    public boolean doesArrowBelongToPlayer;
    public int arrowShake;
    public Entity shootingEntity;
    private int ticksInGround;
    private int ticksInAir;
    public boolean silverFired;

    public LKEntityDart(World world) {
        super(world);
        xTile = -1;
        yTile = -1;
        zTile = -1;
        inTile = 0;
        inData = 0;
        inGround = false;
        doesArrowBelongToPlayer = true;
        arrowShake = 0;
        ticksInAir = 0;
        setSize(0.5F, 0.5F);
    }

    public LKEntityDart(World world, double d, double d1, double d2) {
        super(world);
        xTile = -1;
        yTile = -1;
        zTile = -1;
        inTile = 0;
        inData = 0;
        inGround = false;
        doesArrowBelongToPlayer = true;
        arrowShake = 0;
        ticksInAir = 0;
        setSize(0.5F, 0.5F);
        setPosition(d, d1, d2);
        yOffset = 0.0F;
    }

    public LKEntityDart(World world, EntityLivingBase entityliving, float f, boolean flag) {
        super(world);
        silverFired = flag;
        xTile = -1;
        yTile = -1;
        zTile = -1;
        inTile = 0;
        inData = 0;
        inGround = false;
        doesArrowBelongToPlayer = true;
        arrowShake = 0;
        ticksInAir = 0;
        shootingEntity = entityliving;
        setSize(0.5F, 0.5F);
        setLocationAndAngles(entityliving.posX, entityliving.posY + (double) entityliving.getEyeHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
        posX -= MathHelper.cos((rotationYaw / 180F) * (float)Math.PI) * 0.16F;
        posY -= 0.10000000149011612D;
        posZ -= MathHelper.sin((rotationYaw / 180F) * (float)Math.PI) * 0.16F;
        setPosition(posX, posY, posZ);
        yOffset = 0.0F;
        motionX = -MathHelper.sin((rotationYaw / 180F) * (float)Math.PI) * MathHelper.cos((rotationPitch / 180F) * (float)Math.PI);
        motionZ = MathHelper.cos((rotationYaw / 180F) * (float)Math.PI) * MathHelper.cos((rotationPitch / 180F) * (float)Math.PI);
        motionY = -MathHelper.sin((rotationPitch / 180F) * (float)Math.PI);
        setDartHeading(motionX, motionY, motionZ, f * 1.5F, 1.0F);
    }

    @Override
    protected void entityInit() {
    }

    public void setDartHeading(double d, double d1, double d2, float f, float f1) {
        float f2 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        d /= f2;
        d1 /= f2;
        d2 /= f2;
        d += worldObj.rand.nextGaussian() * 0.0074999998323619366D * (double) f1;
        d1 += worldObj.rand.nextGaussian() * 0.0074999998323619366D * (double) f1;
        d2 += worldObj.rand.nextGaussian() * 0.0074999998323619366D * (double) f1;
        d *= f;
        d1 *= f;
        d2 *= f;
        motionX = d;
        motionY = d1;
        motionZ = d2;
        float f3 = MathHelper.sqrt_double(d * d + d2 * d2);
        prevRotationYaw = rotationYaw = (float) ((Math.atan2(d, d2) * 180D) / Math.PI);
        prevRotationPitch = rotationPitch = (float) ((Math.atan2(d1, f3) * 180D) / Math.PI);
        ticksInGround = 0;
    }

    @Override
    public void setVelocity(double d, double d1, double d2) {
        motionX = d;
        motionY = d1;
        motionZ = d2;
        if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt_double(d * d + d2 * d2);
            prevRotationYaw = rotationYaw = (float) ((Math.atan2(d, d2) * 180D) / Math.PI);
            prevRotationPitch = rotationPitch = (float) ((Math.atan2(d1, f) * 180D) / Math.PI);
            setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
            ticksInGround = 0;
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (isBurning()) {
            setDead();
        }
        if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
            prevRotationYaw = rotationYaw = (float) ((Math.atan2(motionX, motionZ) * 180D) / Math.PI);
            prevRotationPitch = rotationPitch = (float) ((Math.atan2(motionY, f) * 180D) / Math.PI);
        }
        Block block = worldObj.getBlock(xTile, yTile, zTile);
        if (block != null && block != Blocks.air) {
            block.setBlockBoundsBasedOnState(worldObj, xTile, yTile, zTile);
            AxisAlignedBB axisalignedbb = block.getCollisionBoundingBoxFromPool(worldObj, xTile, yTile, zTile);
            if (axisalignedbb != null && axisalignedbb.isVecInside(Vec3.createVectorHelper(posX, posY, posZ))) {
                inGround = true;
            }
        }
        if (arrowShake > 0) {
            arrowShake--;
        }
        if (inGround) {
            Block currentBlock = worldObj.getBlock(xTile, yTile, zTile);
            int currentMeta = worldObj.getBlockMetadata(xTile, yTile, zTile);
            if (currentBlock != null && currentBlock == Block.getBlockById(inTile) && currentMeta == inData) {
                inGroundEvent();
                ticksInGround++;
                if (ticksInGround == 1200) {
                    setDead();
                }
            } else {
                inGround = false;
                motionX *= (double) (worldObj.rand.nextFloat() * 0.2F);
                motionY *= (double) (worldObj.rand.nextFloat() * 0.2F);
                motionZ *= (double) (worldObj.rand.nextFloat() * 0.2F);
                ticksInGround = 0;
                ticksInAir = 0;
            }
        } else {
            ticksInAir++;
            Vec3 vec3d = Vec3.createVectorHelper(posX, posY, posZ);
            Vec3 vec3d1 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
            MovingObjectPosition movingobjectposition = worldObj.rayTraceBlocks(vec3d, vec3d1, false);
            vec3d = Vec3.createVectorHelper(posX, posY, posZ);
            vec3d1 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
            if (movingobjectposition != null) {
                vec3d1 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
            }
            Entity entity = null;
            List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
            double d = 0.0D;
            for (int l = 0; l < list.size(); l++) {
                Entity entity1 = (Entity) list.get(l);
                if (!entity1.canBeCollidedWith() || entity1 == shootingEntity && ticksInAir < 5) {
                    continue;
                }
                float f5 = 0.3F;
                AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand(f5, f5, f5);
                MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec3d, vec3d1);
                if (movingobjectposition1 == null) {
                    continue;
                }
                double d1 = vec3d.distanceTo(movingobjectposition1.hitVec);
                if (d1 < d || d == 0.0D) {
                    entity = entity1;
                    d = d1;
                }
            }

            if (entity != null) {
                movingobjectposition = new MovingObjectPosition(entity);
            }
            if (movingobjectposition != null) {
                if (movingobjectposition.entityHit != null && !(movingobjectposition.entityHit instanceof EntityPlayer)) {
                    float f1 = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
                    int j1 = getDamage();
                    if (silverFired) {
                        j1 += worldObj.rand.nextInt(2) + 1;
                    }
                    if (movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, shootingEntity), (float) j1)) {
                        if (movingobjectposition.entityHit instanceof EntityLivingBase) {
                            EntityLivingBase hitEntity = (EntityLivingBase) movingobjectposition.entityHit;
                            onHitEntity(hitEntity);
                            if (!worldObj.isRemote && shootingEntity instanceof EntityPlayer) {
                                ((EntityPlayer) shootingEntity).addChatMessage(new ChatComponentText(StatCollector.translateToLocal("chat.dart.hit")));
                            }
                        }
                        worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (worldObj.rand.nextFloat() * 0.2F + 0.9F));
                        setDead();
                    } else {
                        motionX *= -0.10000000149011612D;
                        motionY *= -0.10000000149011612D;
                        motionZ *= -0.10000000149011612D;
                        rotationYaw += 180F;
                        prevRotationYaw += 180F;
                        ticksInAir = 0;
                    }
                } else {
                    xTile = movingobjectposition.blockX;
                    yTile = movingobjectposition.blockY;
                    zTile = movingobjectposition.blockZ;
                    inTile = Block.getIdFromBlock(worldObj.getBlock(xTile, yTile, zTile));
                    inData = worldObj.getBlockMetadata(xTile, yTile, zTile);
                    motionX = (float) (movingobjectposition.hitVec.xCoord - posX);
                    motionY = (float) (movingobjectposition.hitVec.yCoord - posY);
                    motionZ = (float) (movingobjectposition.hitVec.zCoord - posZ);
                    float f2 = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
                    posX -= (motionX / (double) f2) * 0.05000000074505806D;
                    posY -= (motionY / (double) f2) * 0.05000000074505806D;
                    posZ -= (motionZ / (double) f2) * 0.05000000074505806D;
                    worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (worldObj.rand.nextFloat() * 0.2F + 0.9F));
                    inGround = true;
                    arrowShake = 7;
                }
            }
            posX += motionX;
            posY += motionY;
            posZ += motionZ;
            float f3 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
            rotationYaw = (float) ((Math.atan2(motionX, motionZ) * 180D) / Math.PI);
            for (rotationPitch = (float) ((Math.atan2(motionY, f3) * 180D) / Math.PI);

            rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F) {
            }
            for (; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) {
            }
            for (; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) {
            }
            for (; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) {
            }
            rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
            rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
            float f4 = 0.99F;
            float f6 = getSpeedReduction();
            if (silverFired) {
                f6 /= 2.5F;
            }
            if (isInWater()) {
                for (int k1 = 0; k1 < 4; k1++) {
                    float f7 = 0.25F;
                    worldObj.spawnParticle("bubble", posX - motionX * (double) f7, posY - motionY * (double) f7, posZ - motionZ * (double) f7, motionX, motionY, motionZ);
                }
                f4 = 0.8F;
            }
            motionX *= f4;
            motionY *= f4;
            motionZ *= f4;
            motionY -= f6;
            setPosition(posX, posY, posZ);
            spawnParticles();
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("xTile", (short) xTile);
        nbttagcompound.setShort("yTile", (short) yTile);
        nbttagcompound.setShort("zTile", (short) zTile);
        nbttagcompound.setByte("inTile", (byte) inTile);
        nbttagcompound.setByte("inData", (byte) inData);
        nbttagcompound.setByte("shake", (byte) arrowShake);
        nbttagcompound.setByte("inGround", (byte) (inGround ? 1 : 0));
        nbttagcompound.setBoolean("player", doesArrowBelongToPlayer);
        nbttagcompound.setBoolean("silver", silverFired);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        xTile = nbttagcompound.getShort("xTile");
        yTile = nbttagcompound.getShort("yTile");
        zTile = nbttagcompound.getShort("zTile");
        inTile = nbttagcompound.getByte("inTile") & 0xff;
        inData = nbttagcompound.getByte("inData") & 0xff;
        arrowShake = nbttagcompound.getByte("shake") & 0xff;
        inGround = nbttagcompound.getByte("inGround") == 1;
        doesArrowBelongToPlayer = nbttagcompound.getBoolean("player");
        silverFired = nbttagcompound.getBoolean("silver");
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityplayer) {
        if (worldObj.isRemote) {
            return;
        }
        if (inGround && doesArrowBelongToPlayer && arrowShake <= 0) {
            EntityItem entityitem = new EntityItem(worldObj, posX, posY, posZ, getDartItem());
            entityitem.delayBeforeCanPickup = 0;
            worldObj.spawnEntityInWorld(entityitem);
            setDead();
        }
    }

    @Override
    public float getShadowSize() {
        return 0.0F;
    }

    public abstract ItemStack getDartItem();

    public abstract int getDamage();

    public abstract void onHitEntity(EntityLivingBase hitEntity);

    public abstract void spawnParticles();

    public abstract float getSpeedReduction();

    public void inGroundEvent() {}
}