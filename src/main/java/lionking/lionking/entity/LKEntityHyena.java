package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MathHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.enchantment.EnchantmentHelper;

import lionking.mod_LionKing; 
import lionking.common.LKAchievementList; 
import lionking.entity.LKEntities; 

import java.util.List;

public class LKEntityHyena extends EntityMob {
    public LKEntityHyena(World world) {
        super(world); 
        setSize(0.6F, 0.8F); 
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20D); 
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(1.5D); 
        getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3D); 
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataWatcher.addObject(20, Byte.valueOf((byte) getRNG().nextInt(3))); 
    }

    public byte getHyenaType() {
        return dataWatcher.getWatchableObjectByte(20); 
    }

    public void setHyenaType(byte b) {
        dataWatcher.updateObject(20, Byte.valueOf(b)); 
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setByte("HyenaType", getHyenaType()); 
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        setHyenaType(nbt.getByte("HyenaType")); 
    }

    @Override
    protected void attackEntity(Entity entity, float f) {
        if (attackTime <= 0 && f < 1.7F && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY) {
            attackTime = 20; 
            attackEntityAsMob(entity); 
        }
    }

    @Override
    public void onLivingUpdate() {
        if (damagedBySunlight() && worldObj.isDaytime() && !worldObj.isRemote) { 
            float f = getBrightness(1.0F); 
            if (f > 0.5F && worldObj.canBlockSeeTheSky(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ)) && getRNG().nextFloat() * 30F < (f - 0.4F) * 2.0F) {
                attackEntityFrom(DamageSource.generic, (float) (getRNG().nextInt(2) + 1)); 
            }
        }
        super.onLivingUpdate();
    }

    @Override
    protected void updateEntityActionState() {
        super.updateEntityActionState();
        if (canAttackZazus() && entityToAttack == null && !hasPath() && worldObj.rand.nextInt(300) == 0) { 
            List list = worldObj.getEntitiesWithinAABB(LKEntityZazu.class, 
                AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(16D, 4D, 16D)); 
            if (!list.isEmpty()) {
                setTarget((Entity) list.get(getRNG().nextInt(list.size()))); 
            }
        }
    }

    @Override
    protected String getLivingSound() {
        return "mob.wolf.growl"; 
    }

    @Override
    protected String getHurtSound() {
        return "mob.wolf.hurt"; 
    }

    @Override
    protected String getDeathSound() {
        return "mob.wolf.death"; 
    }

    @Override
    protected Item getDropItem() {
        return null; 
    }

    @Override
    public void onDeath(DamageSource damagesource) {
        if (damagesource.getEntity() instanceof EntityPlayer && onHyenaKilled((EntityPlayer) damagesource.getEntity())) {
            return; 
        }
        super.onDeath(damagesource);
    }

    public boolean onHyenaKilled(EntityPlayer entityplayer) {
        entityplayer.triggerAchievement(LKAchievementList.killHyena); 
        int looting = EnchantmentHelper.getLootingModifier(entityplayer); 
        if (!worldObj.isRemote) {
            int bones = getRNG().nextInt(3) + getRNG().nextInt(1 + looting); 
            for (int i = 0; i < bones; i++) {
                entityDropItem(new ItemStack(mod_LionKing.hyenaBone, 1), 0F); 
            }

            if (getRNG().nextInt(40) == 0) { 
                entityDropItem(new ItemStack(mod_LionKing.hyenaHeadItem, 1, getHyenaType()), 0F); 
            }
        }
        return false; 
    }

    public boolean canAttackZazus() {
        return true; 
    }

    public boolean damagedBySunlight() {
        return true; 
    }

    @Override
    public ItemStack getPickedResult(MovingObjectPosition target) {
        return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this)); 
    }
}