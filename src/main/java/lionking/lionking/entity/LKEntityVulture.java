package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.StatCollector;
import net.minecraft.world.EnumDifficulty;

import lionking.mod_LionKing;
import lionking.entity.LKEntities;

public class LKEntityVulture extends EntityMob {
    private static final float WIDTH = 0.8F;
    private static final float HEIGHT = 1.5F;
    private static final double ATTACK_SPEED = 1.0D;
    private static final double WANDER_SPEED = 1.0D;
    private static final float WATCH_RANGE = 8.0F;
    private static final double MAX_HEALTH = 16.0D;
    private static final double MOVEMENT_SPEED = 0.25D;
    private static final double ATTACK_DAMAGE = 3.0D;
    private static final int MIN_SPAWN_HEIGHT = 60;
    private static final int SPAWN_CHANCE = 3;

    private float wingPosition;
    private float wingDestination;
    private float wingSpeed;

    public LKEntityVulture(World world) {
        super(world);
        setSize(WIDTH, HEIGHT);
        wingSpeed = 1.0F;
        initializeAITasks();
    }

    private void initializeAITasks() {
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIAttackOnCollide(this, EntityPlayer.class, ATTACK_SPEED, false));
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
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(MOVEMENT_SPEED);
        getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(ATTACK_DAMAGE);
    }

    @Override
    protected boolean isAIEnabled() {
        return true;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        updateWingAnimation();
    }

    private void updateWingAnimation() {
        wingDestination += (onGround ? -1 : 4) * 0.3D;

        if (wingDestination < 0.0F) wingDestination = 0.0F;
        if (wingDestination > 5.0F) wingDestination = 1.0F;
        if (!onGround && wingSpeed < 1.0F) wingSpeed = 1.0F;

        wingSpeed *= 0.9D;
        if (!onGround && motionY < 0.0D) motionY *= 0.6D;

        wingPosition += wingSpeed * 2.0F;
    }

    @Override
    protected void fall(float distance) {
    }

    @Override
    protected Item getDropItem() {
        return mod_LionKing.featherBlack;
    }

    @Override
    public boolean getCanSpawnHere() {
        if (posY < MIN_SPAWN_HEIGHT && rand.nextInt(SPAWN_CHANCE) != 0) {
            return false;
        }
        return worldObj.difficultySetting != EnumDifficulty.PEACEFUL &&
               worldObj.checkNoEntityCollision(boundingBox) &&
               worldObj.getCollidingBoundingBoxes(this, boundingBox).isEmpty() &&
               !worldObj.isAnyLiquid(boundingBox);
    }

    @Override
    protected String getLivingSound() {
        return "lionking:vulture";
    }

    @Override
    protected String getHurtSound() {
        return "lionking:vulturehurt";
    }

    @Override
    protected String getDeathSound() {
        return "lionking:vulturehurt";
    }

    @Override
    public ItemStack getPickedResult(MovingObjectPosition target) {
        return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this));
    }

    public String getEntityName() {
        return StatCollector.translateToLocal("entity.lionking.vulture.name");
    }
}