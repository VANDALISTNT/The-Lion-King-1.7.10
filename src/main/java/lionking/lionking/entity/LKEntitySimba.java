package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MathHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemFood;
import net.minecraft.item.Item;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.StatCollector;

import lionking.mod_LionKing;
import lionking.common.LKIngame;
import lionking.common.LKLevelData;
import lionking.common.LKCharacter;
import lionking.common.LKAchievementList;
import lionking.inventory.LKInventorySimba;
import lionking.entity.LKEntityAISimbaAttack;
import lionking.entity.LKEntityAISimbaFollow;
import lionking.entity.LKEntityAISimbaWander;
import lionking.entity.LKEntityAISimbaAttackPlayerAttacker;
import lionking.entity.LKEntityAISimbaAttackPlayerTarget;
import lionking.entity.LKEntityLionBase;
import lionking.entity.LKEntityTicketLion;
import lionking.entity.LKEntityDart;
import lionking.entity.LKEntitySpear;
import lionking.entity.LKEntities;

public class LKEntitySimba extends EntityCreature implements LKCharacter, IAnimals {
    private static final float WIDTH = 1.3F;
    private static final float HEIGHT = 1.6F;
    private static final double CHILD_MAX_HEALTH = 15.0D;
    private static final double ADULT_MAX_HEALTH = 30.0D;
    private static final double MOVEMENT_SPEED = 0.25D;
    private static final double ATTACK_SPEED = 1.3D;
    private static final double WANDER_SPEED = 1.0D;
    private static final float WATCH_RANGE = 8.0F;
    private static final float CHILD_ATTACK_DAMAGE = 3.0F;
    private static final float ADULT_ATTACK_DAMAGE = 4.0F;
    private static final int FISHING_RECHARGE_MAX = 24000;
    private static final int EATING_DURATION = 12;
    private static final int DEATH_PARTICLE_COUNT = 32;
    private static final int TELEPORT_PARTICLE_COUNT = 12;
    private static final int HEAL_PARTICLE_COUNT = 7;

    public LKInventorySimba inventory;
    private int eatingTick;

    public LKEntitySimba(World world) {
        super(world);
        setSize(WIDTH, HEIGHT);
        getNavigator().setAvoidsWater(true);
        inventory = new LKInventorySimba(this);
        initializeAITasks();
    }

    private void initializeAITasks() {
        tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(2, new LKEntityAISimbaAttack(this, ATTACK_SPEED, true));
        tasks.addTask(3, new LKEntityAISimbaFollow(this, ATTACK_SPEED, 10.0F, 2.0F));
        tasks.addTask(4, new LKEntityAISimbaWander(this, WANDER_SPEED));
        tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, WATCH_RANGE));
        tasks.addTask(6, new EntityAILookIdle(this));
        targetTasks.addTask(1, new LKEntityAISimbaAttackPlayerAttacker(this));
        targetTasks.addTask(2, new LKEntityAISimbaAttackPlayerTarget(this));
        targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(isChild() ? CHILD_MAX_HEALTH : ADULT_MAX_HEALTH);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(MOVEMENT_SPEED);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataWatcher.addObject(12, 0); 
        dataWatcher.addObject(13, ""); 
        dataWatcher.addObject(14, 0); 
        dataWatcher.addObject(15, 0); 
        dataWatcher.addObject(17, (byte) 0); 
        dataWatcher.addObject(18, (byte) 0); 
    }

    @Override
    public boolean isAIEnabled() {
        return true;
    }

    public boolean hasCharm() {
        return dataWatcher.getWatchableObjectByte(17) == 1;
    }

    public void setHasCharm(boolean hasCharm) {
        dataWatcher.updateObject(17, (byte) (hasCharm ? 1 : 0));
    }

    public boolean isSitting() {
        return dataWatcher.getWatchableObjectByte(18) == 1;
    }

    public void setSitting(boolean sitting) {
        dataWatcher.updateObject(18, (byte) (sitting ? 1 : 0));
    }

    public EntityPlayer getOwner() {
        return worldObj.getPlayerEntityByName(getOwnerName());
    }

    public String getOwnerName() {
        return dataWatcher.getWatchableObjectString(13);
    }

    public void setOwnerName(String name) {
        dataWatcher.updateObject(13, name);
    }

    @Override
    public void onDeath(DamageSource source) {
        if (!worldObj.isRemote) {
            inventory.dropAllItems();
            if (hasCharm()) {
                entityDropItem(new ItemStack(mod_LionKing.charm, 1, 1), 0.0F);
            }
            spawnDeathParticles();
        }
        super.onDeath(source);
    }

    private void spawnDeathParticles() {
        for (int i = 0; i < DEATH_PARTICLE_COUNT; i++) {
            double dx = rand.nextGaussian() * 0.02D;
            double dy = rand.nextGaussian() * 0.02D + rand.nextFloat() * 0.25F;
            double dz = rand.nextGaussian() * 0.02D;
            double x = posX + (rand.nextFloat() * width * 2.0F - width) * 0.75F;
            double y = posY + 0.25F + rand.nextFloat() * height;
            double z = posZ + (rand.nextFloat() * width * 2.0F - width) * 0.75F;
            LKIngame.spawnCustomFX(worldObj, 64 + rand.nextInt(4), 16, true, x, y, z, dx, dy, dz);
        }
    }

    public void applyTeleportationEffects(Entity entity) {
        for (int i = 0; i < TELEPORT_PARTICLE_COUNT; i++) {
            double dx = rand.nextGaussian() * 0.02D;
            double dy = rand.nextGaussian() * 0.02D;
            double dz = rand.nextGaussian() * 0.02D;
            double x = posX + (rand.nextFloat() * width * 2.0F - width);
            double y = posY + 0.5D + rand.nextFloat() * height;
            double z = posZ + (rand.nextFloat() * width * 2.0F - width);
            worldObj.spawnParticle("portal", x, y, z, dx, dy, dz);
        }
        if (entity instanceof EntityPlayer) {
            ((EntityPlayer) entity).triggerAchievement(LKAchievementList.teleportSimba);
        }
    }

    public boolean canUsePortal(EntityPlayer player) {
        return hasCharm() && !isSitting() && getOwner() == player;
    }

    public int getAge() {
        return dataWatcher.getWatchableObjectInt(12);
    }

    public void setAge(int age) {
        dataWatcher.updateObject(12, age);
    }

    public int getFishingRecharge() {
        return dataWatcher.getWatchableObjectInt(14);
    }

    public void setFishingRecharge(int recharge) {
        dataWatcher.updateObject(14, recharge);
    }

    public int getFishingCount() {
        return dataWatcher.getWatchableObjectInt(15);
    }

    public void setFishingCount(int count) {
        dataWatcher.updateObject(15, count);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        updateOwnerStatus();
        if (!worldObj.isRemote) {
            updateSimbaState();
            handleFishing();
            handleEating();
        }
    }

    private void updateOwnerStatus() {
        if (!getOwnerName().isEmpty() && MinecraftServer.getServer() != null) {
            EntityPlayer owner = getOwner();
            if (owner != null) {
                LKLevelData.setHasSimba(owner, isEntityAlive());
            }
        }
    }

    private void updateSimbaState() {
        if (isSitting()) {
            getNavigator().clearPathEntity();
        }

        int age = getAge();
        if (age < -1) {
            setAge(++age);
        } else if (age == -1) {
            setAge(0);
            getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(ADULT_MAX_HEALTH);
            setHealth(getMaxHealth());
        }

        if (getFishingCount() == -1) {
            int recharge = getFishingRecharge() + 1;
            setFishingRecharge(recharge);
            if (recharge == FISHING_RECHARGE_MAX) {
                setFishingCount(0);
                setFishingRecharge(0);
            }
        }
    }

    private void handleFishing() {
        if (isSitting() || getFishingRecharge() != 0) return;

        int x = MathHelper.floor_double(posX);
        int y = MathHelper.floor_double(posY);
        int z = MathHelper.floor_double(posZ);
        if (worldObj.getBlock(x, y, z) != Blocks.water) return;

        int count = getFishingCount();
        if (count == 0 && rand.nextInt(160) == 0) {
            setFishingCount(1);
            catchFishAt(x, y, z);
        } else if (count == 1 && rand.nextInt(160) == 0) {
            catchFishAt(x, y, z);
            setFishingCount(rand.nextInt(4) == 0 ? -1 : 2);
        } else if (count == 2 && rand.nextInt(200) == 0) {
            catchFishAt(x, y, z);
            setFishingCount(rand.nextInt(3) == 0 ? -1 : 3);
        } else if (count == 3 && rand.nextInt(200) == 0) {
            catchFishAt(x, y, z);
            setFishingCount(rand.nextInt(2) == 0 ? -1 : 4);
        } else if (count == 4 && rand.nextInt(220) == 0) {
            catchFishAt(x, y, z);
            setFishingCount(-1);
        }
    }

    private void catchFishAt(int x, int y, int z) {
        EntityItem fish = new EntityItem(worldObj, x + 0.5D, y + 0.5D, z + 0.5D, new ItemStack(Items.fish, 1, 0));
        fish.delayBeforeCanPickup = 10;
        fish.addVelocity(0.0F, 0.3D + rand.nextFloat() / 3.0D, 0.0F);
        worldObj.spawnEntityInWorld(fish);
    }

    private void handleEating() {
        if (eatingTick > 0) {
            if (eatingTick % 4 == 0) {
                worldObj.playSoundAtEntity(this, "random.eat", 0.8F + 0.5F * rand.nextInt(2),
                    (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            }
            eatingTick--;
        }
    }

    @Override
    public boolean isChild() {
        return getAge() < 0;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public void setAttackTarget(EntityLivingBase target) {
        if (target instanceof LKEntityLionBase || target instanceof EntityTameable ||
            target instanceof LKEntityTicketLion || target instanceof LKCharacter ||
            target instanceof EntityPlayer || isSitting()) {
            return;
        }
        super.setAttackTarget(target);
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), isChild() ? CHILD_ATTACK_DAMAGE : ADULT_ATTACK_DAMAGE);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("Age", getAge());
        nbt.setTag("Inventory", inventory.writeToNBT(new NBTTagList()));
        nbt.setInteger("FishingRecharge", getFishingRecharge());
        nbt.setInteger("FishingCount", getFishingCount());
        nbt.setBoolean("Sitting", isSitting());
        nbt.setBoolean("Charm", hasCharm());
        nbt.setString("Owner", getOwnerName() != null ? getOwnerName() : "");
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        setAge(nbt.getInteger("Age"));
        inventory.readFromNBT(nbt.getTagList("Inventory", 10));
        setFishingRecharge(nbt.getInteger("FishingRecharge"));
        setFishingCount(nbt.getInteger("FishingCount"));
        setSitting(nbt.getBoolean("Sitting"));
        setHasCharm(nbt.getBoolean("Charm"));
        String owner = nbt.getString("Owner");
        if (!owner.isEmpty()) setOwnerName(owner);
    }

    @Override
    protected boolean isMovementCeased() {
        return isSitting();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        Entity entity = source.getEntity();
        if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow) &&
            !(entity instanceof LKEntityDart) && !(entity instanceof LKEntitySpear)) {
            amount = (amount + 1.0F) / 2.0F;
        }
        setSitting(false);
        if (super.attackEntityFrom(source, amount)) {
            if (entity != this && entity != null && !(entity instanceof EntityPlayer)) {
                entityToAttack = entity;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean interact(EntityPlayer player) {
        if (!isEntityAlive() || player != getOwner()) return false;

        ItemStack stack = player.inventory.getCurrentItem();
        if (stack != null) {
            if (handleCharmInteraction(player, stack)) return true;
            if (handleHealingInteraction(player, stack)) return true;
        }

        if (isChild()) {
            if (!worldObj.isRemote) setSitting(!isSitting());
            return true;
        } else {
            if (!worldObj.isRemote) {
                player.openGui(mod_LionKing.instance, mod_LionKing.GUI_ID_SIMBA, worldObj, getEntityId(), 0, 0);
            }
            return true;
        }
    }

    private boolean handleCharmInteraction(EntityPlayer player, ItemStack stack) {
        if (stack.getItem() == mod_LionKing.charm && stack.getItemDamage() == 0 && !hasCharm()) {
            if (stack.stackSize == 1) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
            } else {
                stack.stackSize--;
            }
            spawnParticles("portal", TELEPORT_PARTICLE_COUNT);
            if (!worldObj.isRemote) setHasCharm(true);
            return true;
        }
        return false;
    }

    private boolean handleHealingInteraction(EntityPlayer player, ItemStack stack) {
        if (getHealth() >= getMaxHealth()) return false;

        if (stack.getItem() == mod_LionKing.hyenaBone) {
            consumeItem(player, stack);
            heal(1);
            spawnParticles("smoke", HEAL_PARTICLE_COUNT);
            eatingTick = EATING_DURATION;
            return true;
        }

        Item item = stack.getItem();
        if (item != mod_LionKing.lionRaw && item != mod_LionKing.lionCooked && item instanceof ItemFood) {
            ItemFood food = (ItemFood) item;
            if (food.isWolfsFavoriteMeat() || food == Items.fish || food == Items.cooked_fished) {
                consumeItem(player, stack);
                heal(food.func_150905_g(stack));
                spawnParticles("smoke", HEAL_PARTICLE_COUNT);
                eatingTick = EATING_DURATION;
                return true;
            }
        }
        return false;
    }

    private void consumeItem(EntityPlayer player, ItemStack stack) {
        if (--stack.stackSize <= 0) {
            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
        }
    }

    private void spawnParticles(String type, int count) {
        for (int i = 0; i < count; i++) {
            double dx = rand.nextGaussian() * 0.02D;
            double dy = rand.nextGaussian() * 0.02D;
            double dz = rand.nextGaussian() * 0.02D;
            double x = posX + (rand.nextFloat() * width * 2.0F - width);
            double y = posY + 0.5D + rand.nextFloat() * height;
            double z = posZ + (rand.nextFloat() * width * 2.0F - width);
            worldObj.spawnParticle(type, x, y, z, dx, dy, dz);
        }
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
    public ItemStack getPickedResult(MovingObjectPosition target) {
        return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this));
    }

    public String getEntityName() {
        return StatCollector.translateToLocal("entity.lionking.simba.name");
    }
}