package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.StatCollector;

import lionking.mod_LionKing;
import lionking.entity.LKEntities;
import lionking.entity.LKEntityQuestAnimal;

public class LKEntitySkeletalHyenaHead extends EntityLiving implements IMob {
    private static final float WIDTH = 0.6F;
    private static final float HEIGHT = 0.6F;
    private static final double MAX_HEALTH = 15.0D;
    private static final double MOVEMENT_SPEED = 1.2D;
    private static final double DETECTION_RANGE = 16.0D;
    private static final float ATTACK_DAMAGE = 3.0F;
    private static final float ATTACK_RANGE = 1.0F;
    private static final int EXPERIENCE_VALUE = 2;
    private static final int MIN_JUMP_DELAY = 10;
    private static final int MAX_JUMP_DELAY = 30;
    private static final int DROP_CHANCE = 40;
    private static final int HYENA_HEAD_DROP_METADATA = 3;

    private float field_70813_a;
    private float field_70811_b;
    private float field_70812_c;
    private int headJumpDelay;

    public LKEntitySkeletalHyenaHead(World world) {
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
    public void onUpdate() {
        if (!worldObj.isRemote && worldObj.difficultySetting.getDifficultyId() == 0) {
            setDead();
            return;
        }

        field_70811_b += (field_70813_a - field_70811_b) * 0.5F;
        field_70812_c = field_70811_b;
        boolean wasOnGround = onGround;
        super.onUpdate();

        if (onGround && !wasOnGround) {
            worldObj.playSoundAtEntity(this, getHurtSound(), 0.4F, pitchVariation(0.8F));
            field_70813_a = -0.5F;
        } else if (!onGround && wasOnGround) {
            field_70813_a = 1.0F;
        }
        field_70813_a *= 0.6F;
    }

    @Override
    protected void updateEntityActionState() {
        despawnEntity();
        EntityPlayer player = worldObj.getClosestVulnerablePlayerToEntity(this, DETECTION_RANGE);

        if (player != null) {
            faceEntity(player, 10.0F, 20.0F);
        }

        if (onGround && --headJumpDelay <= 0) {
            headJumpDelay = rand.nextInt(MAX_JUMP_DELAY - MIN_JUMP_DELAY + 1) + MIN_JUMP_DELAY;
            if (player != null) headJumpDelay /= 3;

            isJumping = true;
            worldObj.playSoundAtEntity(this, getHurtSound(), 0.4F, pitchVariation(0.8F));
            moveStrafing = 1.0F - rand.nextFloat() * 2.0F;
            moveForward = 1.0F;
        } else {
            isJumping = false;
            if (onGround) {
                moveStrafing = moveForward = 0.0F;
            }
        }
    }

    private float pitchVariation(float basePitch) {
        return ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) * basePitch;
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer player) {
        if (canEntityBeSeen(player) && getDistanceSqToEntity(player) < ATTACK_RANGE &&
            player.attackEntityFrom(DamageSource.causeMobDamage(this), ATTACK_DAMAGE)) {
            worldObj.playSoundAtEntity(this, "mob.attack", 1.0F, pitchVariation(1.0F));
        }
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        if (!worldObj.isRemote && source.getEntity() instanceof EntityPlayer && rand.nextInt(DROP_CHANCE) == 0) {
            entityDropItem(new ItemStack(mod_LionKing.hyenaHeadItem, 1, HYENA_HEAD_DROP_METADATA), 0.0F);
        }
    }

    @Override
    protected String getHurtSound() {
        return "mob.skeleton.hurt";
    }

    @Override
    protected String getDeathSound() {
        return "mob.skeleton.death";
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Override
    public ItemStack getPickedResult(MovingObjectPosition target) {
        return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this));
    }


    public String getEntityName() {
        return StatCollector.translateToLocal("entity.lionking.skeletalHyenaHead.name");
    }
}
