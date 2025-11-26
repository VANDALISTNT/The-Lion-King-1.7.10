package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.DamageSource;
import net.minecraft.potion.Potion;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.Entity;
import net.minecraft.util.StatCollector;

import lionking.common.LKCharacter;
import lionking.common.LKLevelData;
import lionking.entity.LKEntities;
import lionking.mod_LionKing;

public class LKEntityTermiteQueen extends EntityCreature implements LKCharacter, IMob {
    private static final float BASE_WIDTH = 1.5F;
    private static final float BASE_HEIGHT = 1.2F;
    private static final double ATTACK_SPEED = 1.4D;
    private static final double WANDER_SPEED = 1.0D;
    private static final float WATCH_RANGE = 8.0F;
    private static final double MAX_HEALTH = 25.0D;
    private static final double FOLLOW_RANGE = 32.0D;
    private static final double MOVEMENT_SPEED = 0.25D;
    private static final float EXPLOSION_RADIUS = 3.0F;
    private static final int BASE_ATTACK_DAMAGE = 3;
    private static final double DETECTION_RANGE = 16.0D;
    private static final float INITIAL_SCALE = 25.0F;
    private static final float SCALE_FACTOR = 0.07F;
    private static final int DATAWATCHER_SCALE = 20;

    private boolean exploded;

    public LKEntityTermiteQueen(World world) {
        super(world);
        isImmuneToFire = true;
        rescale();
        initializeAITasks();
    }

    private void initializeAITasks() {
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new LKEntityAITermiteQueenAttack(this, EntityPlayer.class, ATTACK_SPEED, false));
        tasks.addTask(2, new EntityAIWander(this, WANDER_SPEED));
        tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, WATCH_RANGE));
        tasks.addTask(4, new EntityAILookIdle(this));
        targetTasks.addTask(0, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(MAX_HEALTH);
        getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(FOLLOW_RANGE);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(MOVEMENT_SPEED);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataWatcher.addObject(DATAWATCHER_SCALE, INITIAL_SCALE);
    }

    private void rescale() {
        setSize(BASE_WIDTH * getScale(), BASE_HEIGHT * getScale());
        setPosition(posX, posY, posZ);
    }

    private void setScale(float scale) {
        dataWatcher.updateObject(DATAWATCHER_SCALE, scale);
    }

    public float getScale() {
        return 3.0F - ((INITIAL_SCALE - dataWatcher.getWatchableObjectFloat(DATAWATCHER_SCALE)) * SCALE_FACTOR);
    }

    @Override
    protected int getExperiencePoints(EntityPlayer player) {
        return 0;
    }

    @Override
    protected boolean isAIEnabled() {
        return true;
    }

    @Override
    protected Entity findPlayerToAttack() {
        EntityPlayer player = worldObj.getClosestVulnerablePlayerToEntity(this, DETECTION_RANGE);
        return player != null && canEntityBeSeen(player) ? player : null;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source == DamageSource.outOfWorld) {
            return super.attackEntityFrom(source, amount);
        }

        Entity attacker = source.getEntity();
        if (!(attacker instanceof EntityPlayer)) {
            return false;
        }

        float currentHealth = getHealth();
        boolean damaged = super.attackEntityFrom(source, 1.0F);

        if (getHealth() > 0 && damaged) {
            if (!worldObj.isRemote) {
                setScale(getHealth());
                if (getHealth() < currentHealth) {
                    spawnTermite();
                }
            }
            rescale();
        }
        return damaged;
    }

    private void spawnTermite() {
        LKEntityTermite termite = new LKEntityTermite(worldObj);
        termite.setLocationAndAngles(posX, posY, posZ, rand.nextFloat() * 360.0F, 0.0F);
        if (!worldObj.isRemote) {
            worldObj.spawnEntityInWorld(termite);
        }
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (riddenByEntity instanceof LKEntityZira && !worldObj.isRemote) {
            riddenByEntity.rotationYaw = rotationYaw;
            riddenByEntity.rotationPitch = rotationPitch;
        }

        if (getHealth() == 1 && !worldObj.isRemote) {
            handleDeathSequence();
        }
    }

    private void handleDeathSequence() {
        if (riddenByEntity instanceof LKEntityZira) {
            LKLevelData.setZiraStage(27);
            riddenByEntity.attackEntityFrom(DamageSource.outOfWorld, 100.0F);
            riddenByEntity.mountEntity(null);
        }

        if (!exploded) {
            worldObj.createExplosion(this, posX, posY + 3.0D, posZ, EXPLOSION_RADIUS,
                worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
            exploded = true;
        }

        attackEntityFrom(DamageSource.outOfWorld, 1.0F);
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        int damage = BASE_ATTACK_DAMAGE;

        if (isPotionActive(Potion.damageBoost)) {
            damage += 3 << getActivePotionEffect(Potion.damageBoost).getAmplifier();
        }
        if (isPotionActive(Potion.weakness)) {
            damage -= 2 << getActivePotionEffect(Potion.weakness).getAmplifier();
        }

        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("HasExploded", exploded);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        exploded = nbt.getBoolean("HasExploded");
        setScale(getHealth());
        rescale();
    }

    @Override
    protected String getLivingSound() {
        return "mob.silverfish.say";
    }

    @Override
    protected String getHurtSound() {
        return "mob.silverfish.hit";
    }

    @Override
    protected String getDeathSound() {
        return "mob.silverfish.kill";
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public ItemStack getPickedResult(MovingObjectPosition target) {
        return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this));
    }

    @Override
    public String getEntityName() {
        return StatCollector.translateToLocal("entity.lionking.termiteQueen.name");
    }
}