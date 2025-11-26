package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import net.minecraft.util.StatCollector;

import lionking.common.LKCharacterSpeech;
import lionking.mod_LionKing;

public class LKEntityRhino extends LKEntityQuestAnimal {
    private static final float WIDTH = 1.3F;
    private static final float HEIGHT = 1.2F;
    private static final double MAX_HEALTH = 16.0D;
    private static final double MOVEMENT_SPEED = 0.2D;
    private static final double PANIC_SPEED = 1.5D;
    private static final double MATE_SPEED = 1.0D;
    private static final double TEMPT_SPEED = 1.0D;
    private static final double FOLLOW_SPEED = 1.3D;
    private static final double WANDER_SPEED = 1.0D;
    private static final float WATCH_RANGE = 6.0F;

    public LKEntityRhino(World world) {
        super(world);
        setSize(WIDTH, HEIGHT);
        getNavigator().setAvoidsWater(true);
        initializeAITasks();
    }

    private void initializeAITasks() {
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPanic(this, PANIC_SPEED));
        tasks.addTask(2, new EntityAIMate(this, MATE_SPEED));
        tasks.addTask(3, new EntityAITempt(this, TEMPT_SPEED, Items.wheat, false));
        tasks.addTask(3, new EntityAITempt(this, TEMPT_SPEED, mod_LionKing.corn, false));
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
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == Items.wheat || stack.getItem() == mod_LionKing.corn;
    }

    @Override
    public EntityAgeable createChild(EntityAgeable entity) {
        return new LKEntityRhino(worldObj);
    }

    @Override
    protected Item getDropItem() {
        return mod_LionKing.horn;
    }

    @Override
    protected void dropFewItems(boolean recentlyHit, int looting) {
        int hornCount = rand.nextInt(2) + rand.nextInt(1 + looting);
        for (int i = 0; i < hornCount; i++) {
            dropItem(mod_LionKing.horn, 1);
        }

        int meatCount = rand.nextInt(2) + 1 + rand.nextInt(1 + looting);
        for (int i = 0; i < meatCount; i++) {
            dropItem(isBurning() ? mod_LionKing.rhinoCooked : mod_LionKing.rhinoRaw, 1);
        }
    }

    @Override
    protected String getLivingSound() {
        return "lionking:rhino";
    }

    @Override
    protected String getHurtSound() {
        return "lionking:rhinohurt";
    }

    @Override
    protected String getDeathSound() {
        return "lionking:rhinodeath";
    }

    @Override
    public LKCharacterSpeech getCharacterSpeech() {
        return LKCharacterSpeech.RHINO;
    }

    @Override
    public LKCharacterSpeech getChildCharacterSpeech() {
        return LKCharacterSpeech.RHINO_CALF;
    }

    @Override
    public String getAnimalName() {
        return StatCollector.translateToLocal("entity.lionking.rhino.name");
    }

    @Override
    public ItemStack getQuestItem() {
        int type = rand.nextInt(5);
        switch (type) {
            case 0:
                return new ItemStack(mod_LionKing.leaves, 5 + rand.nextInt(8));
            case 1:
                return new ItemStack(Blocks.deadbush, 5 + rand.nextInt(10));
            case 2:
                return new ItemStack(mod_LionKing.cornStalk, 8 + rand.nextInt(16));
            case 3:
                return new ItemStack(Items.stick, 40 + rand.nextInt(25));
            case 4:
                return new ItemStack(Blocks.tallgrass, 4 + rand.nextInt(7), 2);
            default:
                return new ItemStack(mod_LionKing.leaves, 5 + rand.nextInt(8));
        }
    }

    @Override
    public String getEntityName() {
        return StatCollector.translateToLocal("entity.lionking.rhino.name");
    }
}