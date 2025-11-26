package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.item.Item;
import net.minecraft.util.StatCollector;
import net.minecraft.entity.Entity;

import lionking.mod_LionKing;
import lionking.entity.LKEntities;
import lionking.entity.LKEntityQuestAnimal;

public class LKEntityTermite extends EntityMob {
    private static final float WIDTH = 0.4F;
    private static final float HEIGHT = 0.4F;
    private static final double MAX_HEALTH = 9.0D;
    private static final double MOVEMENT_SPEED = 1.0D;
    private static final float EXPLOSION_RADIUS = 1.7F;
    private static final int EXPLOSION_DELAY = 20;
    private static final float DETONATION_RANGE = 3.0F;
    private static final float DETONATION_RANGE_ACTIVE = 7.0F;
    private static final int EXPERIENCE_VALUE = 3;
    private static final int DATAWATCHER_STATE = 16;

    private int timeSinceIgnited;
    private int lastActiveTime;

    public LKEntityTermite(World world) {
        super(world);
        setSize(WIDTH, HEIGHT);
        experienceValue = EXPERIENCE_VALUE;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(MAX_HEALTH);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(MOVEMENT_SPEED);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataWatcher.addObject(DATAWATCHER_STATE, (byte) -1);
    }

    @Override
    public void onUpdate() {
        lastActiveTime = timeSinceIgnited;

        if (worldObj.isRemote) {
            updateClientState();
        }

        super.onUpdate();

        if (entityToAttack == null && timeSinceIgnited > 0) {
            setTermiteState(-1);
            if (--timeSinceIgnited < 0) timeSinceIgnited = 0;
        }
    }

    private void updateClientState() {
        int state = getTermiteState();
        if (state > 0 && timeSinceIgnited == 0) {
            worldObj.playSoundAtEntity(this, "random.fuse", 1.0F, 0.5F);
        }
        timeSinceIgnited += state;
        if (timeSinceIgnited < 0) timeSinceIgnited = 0;
        if (timeSinceIgnited >= EXPLOSION_DELAY) timeSinceIgnited = EXPLOSION_DELAY;
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
    protected void attackEntity(Entity entity, float distance) {
        if (worldObj.isRemote) return;

        int state = getTermiteState();
        boolean withinRange = (state <= 0 && distance < DETONATION_RANGE) || (state > 0 && distance < DETONATION_RANGE_ACTIVE);

        if (withinRange) {
            if (timeSinceIgnited == 0) {
                worldObj.playSoundAtEntity(this, "random.fuse", 1.0F, 0.5F);
            }
            setTermiteState(1);
            if (++timeSinceIgnited >= EXPLOSION_DELAY) {
                worldObj.createExplosion(this, posX, posY, posZ, EXPLOSION_RADIUS,
                    worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
                setDead();
            }
            hasAttacked = true;
        } else {
            setTermiteState(-1);
            if (--timeSinceIgnited < 0) timeSinceIgnited = 0;
        }
    }

    public float setTermiteFlashTime(float partialTicks) {
        return (lastActiveTime + (timeSinceIgnited - lastActiveTime) * partialTicks) / 28.0F;
    }

    @Override
    protected Item getDropItem() {
        return null;
    }

    private int getTermiteState() {
        return dataWatcher.getWatchableObjectByte(DATAWATCHER_STATE);
    }

    private void setTermiteState(int state) {
        dataWatcher.updateObject(DATAWATCHER_STATE, (byte) state);
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        if (!worldObj.isRemote && source.getEntity() instanceof EntityPlayer) {
            dropItem(mod_LionKing.itemTermite, 1);
            setDead();
        }
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    @Override
    public ItemStack getPickedResult(MovingObjectPosition target) {
        return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this));
    }

    
    public String getEntityName() {
        return StatCollector.translateToLocal("entity.lionking.termite.name");
    }
}
