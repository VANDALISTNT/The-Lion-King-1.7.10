package lionking.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.init.Items;
import net.minecraft.util.ChatComponentTranslation;

import lionking.mod_LionKing;
import lionking.common.LKAngerable;

import java.util.List;

public abstract class LKEntityLionBase extends LKEntityQuestAnimal implements LKAngerable {
    private int hostileTick;

    public LKEntityLionBase(World world) {
        super(world);
        hostileTick = 0;
        getNavigator().setAvoidsWater(true);
        initializeAITasks();
    }

    private void initializeAITasks() {
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new LKEntityAILionAttack(this, EntityPlayer.class, 1.5D, false));
        tasks.addTask(2, new LKEntityAIAngerablePanic(this, 1.5D));
        tasks.addTask(3, new LKEntityAIAngerableMate(this, 1.0D));
        tasks.addTask(4, new EntityAITempt(this, 1.4D, mod_LionKing.hyenaBone, false));
        tasks.addTask(5, new EntityAIFollowParent(this, 1.4D));
        tasks.addTask(6, new EntityAIWander(this, 1.0D));
        tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(8, new EntityAILookIdle(this));
        targetTasks.addTask(0, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(1, new LKEntityAIAngerableAttackableTarget(this, EntityPlayer.class, 0, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(18.0D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2D);
    }

    @Override
    public boolean isAIEnabled() {
        return true;
    }

    @Override
    public boolean isHostile() {
        return hostileTick > 0;
    }

    @Override
    protected int getExperiencePoints(EntityPlayer player) {
        return 2 + worldObj.rand.nextInt(3);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (hostileTick > 0 && getAttackTarget() == null) {
            hostileTick--;
        }
    }

    @Override
    protected Item getDropItem() {
        return isBurning() ? mod_LionKing.lionCooked : mod_LionKing.lionRaw;
    }

    @Override
    protected void dropFewItems(boolean recentlyHit, int lootingLevel) {
        int furCount = getRNG().nextInt(2) + 1 + getRNG().nextInt(1 + lootingLevel);
        for (int i = 0; i < furCount; i++) {
            if (getRNG().nextInt(4) != 0) {
                dropItem(mod_LionKing.fur, 1);
            }
        }

        int meatCount = getRNG().nextInt(2) + 1 + getRNG().nextInt(1 + lootingLevel);
        Item meat = isBurning() ? mod_LionKing.lionCooked : mod_LionKing.lionRaw;
        for (int i = 0; i < meatCount; i++) {
            dropItem(meat, 1);
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        int damage = 3;
        if (isPotionActive(Potion.damageBoost)) {
            damage += 3 << getActivePotionEffect(Potion.damageBoost).getAmplifier();
        }
        if (isPotionActive(Potion.weakness)) {
            damage -= 2 << getActivePotionEffect(Potion.weakness).getAmplifier();
        }
        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), Math.max(damage, 0));
    }

    @Override
    public EntityAgeable createChild(EntityAgeable entity) {
        return getRNG().nextBoolean() ? new LKEntityLion(worldObj) : new LKEntityLioness(worldObj);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        Entity attacker = source.getEntity();
        if (isChild()) {
            fleeingTick = 60;
            provokeNearbyAdults(attacker);
        } else if (attacker instanceof EntityLivingBase) {
            becomeAngryAt((EntityLivingBase) attacker);
        }
        return super.attackEntityFrom(source, amount);
    }

    private void provokeNearbyAdults(Entity attacker) {
        List<Entity> nearbyEntities = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(12.0D, 12.0D, 12.0D));
        for (Entity entity : nearbyEntities) {
            if (entity instanceof LKEntityLionBase && !((LKEntityLionBase) entity).isChild() && attacker instanceof EntityLivingBase) {
                ((LKEntityLionBase) entity).becomeAngryAt((EntityLivingBase) attacker);
            }
        }
    }

    private void becomeAngryAt(EntityLivingBase target) {
        setAttackTarget(target);
        hostileTick = 6000;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("Angry", hostileTick);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        hostileTick = nbt.getInteger("Angry");
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack != null && stack.getItem() == mod_LionKing.hyenaBone;
    }

    @Override
    public boolean canMateWith(EntityAnimal mate) {
        if (mate == this || !isInLove() || !mate.isInLove()) {
            return false;
        }
        return (this instanceof LKEntityLion && mate instanceof LKEntityLioness) ||
               (this instanceof LKEntityLioness && mate instanceof LKEntityLion);
    }

    @Override
    public boolean interact(EntityPlayer player) {
        ItemStack heldItem = player.inventory.getCurrentItem();
        if (heldItem != null && heldItem.getItem() == mod_LionKing.jarMilk && !isChild() && isHostile()) {
            player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(mod_LionKing.jar));
            hostileTick = 0;
            spawnCalmParticles();
            if (!worldObj.isRemote) {
                player.addChatMessage(new ChatComponentTranslation("chat.lion.calmed"));
            }
            return true;
        } else if (isHostile()) {
            if (!worldObj.isRemote) {
                player.addChatMessage(new ChatComponentTranslation("chat.lion.hostile"));
            }
            return false;
        }
        return super.interact(player);
    }

    private void spawnCalmParticles() {
        for (int i = 0; i < 7; i++) {
            double dx = getRNG().nextGaussian() * 0.02D;
            double dy = getRNG().nextGaussian() * 0.02D;
            double dz = getRNG().nextGaussian() * 0.02D;
            worldObj.spawnParticle("smoke",
                    posX + getRNG().nextFloat() * width * 2.0F - width,
                    posY + 0.5D + getRNG().nextFloat() * height,
                    posZ + getRNG().nextFloat() * width * 2.0F - width,
                    dx, dy, dz);
        }
    }

    @Override
    protected String getLivingSound() {
        return "lionking:lion";
    }

    @Override
    protected String getHurtSound() {
        return "lionking:lionroar";
    }

    @Override
    protected String getDeathSound() {
        return "lionking:liondeath";
    }

    @Override
    public ItemStack getQuestItem() {
        int choice = getRNG().nextInt(5);
        switch (choice) {
            case 0: return new ItemStack(mod_LionKing.zebraRaw, 5 + getRNG().nextInt(7));
            case 1: return new ItemStack(Items.fish, 3 + getRNG().nextInt(6));
            case 2: return new ItemStack(mod_LionKing.hyenaBoneShard, 6 + getRNG().nextInt(9));
            case 3: return new ItemStack(mod_LionKing.crocodileMeat, 3 + getRNG().nextInt(11));
            case 4: return new ItemStack(mod_LionKing.hyenaHeadItem, 1, getRNG().nextInt(2));
            default: return new ItemStack(mod_LionKing.zebraRaw, 5 + getRNG().nextInt(7));
        }
    }
}