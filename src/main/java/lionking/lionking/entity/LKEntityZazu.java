package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.util.StatCollector;
import net.minecraft.init.Items; 

import lionking.mod_LionKing; 
import lionking.common.LKIngame; 
import lionking.common.LKAchievementList; 

public class LKEntityZazu extends EntityAnimal {
    private static final float WIDTH = 0.6F;
    private static final float HEIGHT = 0.7F;
    private static final double PANIC_SPEED = 1.5D;
    private static final double MATE_SPEED = 1.3D;
    private static final double TEMPT_SPEED = 1.3D;
    private static final double FOLLOW_SPEED = 1.4D;
    private static final double WANDER_SPEED = 1.0D;
    private static final float WATCH_RANGE = 6.0F;
    private static final double MAX_HEALTH = 8.0D;
    private static final double MOVEMENT_SPEED = 0.2D;
    private static final int TALK_COOLDOWN = 300;

    private float wingPosition;
    private float wingDestination;
    private float wingSpeed;
    private int talkTick;
    public float field_752_b; 
    public float destPos;     

    public LKEntityZazu(World world) {
        super(world);
        setSize(WIDTH, HEIGHT);
        wingSpeed = 5.0F;
        talkTick = TALK_COOLDOWN;
        getNavigator().setAvoidsWater(true);
        initializeAITasks();
    }

    private void initializeAITasks() {
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPanic(this, PANIC_SPEED));
        tasks.addTask(2, new LKEntityAIZazuMate(this, MATE_SPEED));
        tasks.addTask(3, new EntityAITempt(this, TEMPT_SPEED, Items.wheat_seeds, false)); 
        tasks.addTask(3, new EntityAITempt(this, TEMPT_SPEED, mod_LionKing.cornKernels, false));
        tasks.addTask(4, new EntityAIFollowParent(this, FOLLOW_SPEED));
        tasks.addTask(5, new EntityAIWander(this, WANDER_SPEED));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, WATCH_RANGE));
        tasks.addTask(7, new EntityAILookIdle(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(MAX_HEALTH); 
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(MOVEMENT_SPEED); 
    }

    @Override
    public boolean isAIEnabled() {
        return true;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        updateWingAnimation();
        updateTalkCooldown();
    }

    private void updateWingAnimation() {
        float prevWingPosition = wingPosition;
        float prevWingDestination = wingDestination;
        wingDestination += (onGround ? -1 : 4) * 0.3D;

        if (wingDestination < 0.0F) wingDestination = 0.0F;
        if (wingDestination > 5.0F) wingDestination = 1.0F;
        if (!onGround && wingSpeed < 1.0F) wingSpeed = 1.0F;

        wingSpeed *= 0.9D;
        if (!onGround && motionY < 0.0D) motionY *= 0.6D;

        wingPosition += wingSpeed * 2.0F;
        field_752_b = wingPosition; 
        destPos = wingDestination;  
    }

    private void updateTalkCooldown() {
        if (talkTick < TALK_COOLDOWN) talkTick++;
    }

    private boolean canTalk() {
        return talkTick == TALK_COOLDOWN && !isChild() && isMorning();
    }

    private boolean isMorning() {
        long time = worldObj.getWorldTime() % 24000;
        return time > 23000L || (time >= 0L && time < 4500L);
    }

    @Override
    protected void fall(float distance) {
    }

    @Override
    protected String getLivingSound() {
        return "lionking:zazulive";
    }

    @Override
    protected String getHurtSound() {
        return "lionking:zazuhurt";
    }

    @Override
    protected String getDeathSound() {
        return "lionking:zazuhurt";
    }

    @Override
    protected Item getDropItem() {
        return mod_LionKing.featherBlue;
    }

    @Override
    protected void dropFewItems(boolean recentlyHit, int looting) {
        int featherCount = rand.nextInt(3) + (looting > 0 ? rand.nextInt(looting + 1) : 0);
        dropItems(mod_LionKing.featherBlue, featherCount);

        if (featherCount > 0) {
            if (rand.nextInt(5) > 1) dropItem(mod_LionKing.featherYellow, 1);
            if (rand.nextInt(4) == 0) dropItem(mod_LionKing.featherRed, 1);
        }
    }

    private void dropItems(Item item, int count) {
        for (int i = 0; i < count; i++) {
            dropItem(item, 1);
        }
    }

    @Override
    public boolean interact(EntityPlayer player) {
        ItemStack heldItem = player.inventory.getCurrentItem();
        if ((heldItem == null || !isBreedingItem(heldItem)) && canTalk()) {
            player.triggerAchievement(LKAchievementList.morningReport);
            if (!worldObj.isRemote) {
                LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.zazu.morningReport"));
            }
            talkTick = 0;
            return true;
        }
        return super.interact(player);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack != null && (stack.getItem() == Items.wheat_seeds || stack.getItem() == mod_LionKing.cornKernels);
    }

    @Override
    public EntityAgeable createChild(EntityAgeable entity) {
        return new LKEntityZazu(worldObj);
    }

    @Override
    public ItemStack getPickedResult(MovingObjectPosition target) {
        return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this));
    }
     
    public String getEntityName() { 
        return StatCollector.translateToLocal("entity.lionking.zazu.name");
    }
}