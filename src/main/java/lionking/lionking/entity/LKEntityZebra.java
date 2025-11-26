package lionking.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.util.StatCollector;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;

import lionking.mod_LionKing;
import lionking.entity.LKEntityQuestAnimal;

public class LKEntityZebra extends EntityAnimal {
    private static final double TEMPT_SPEED = 1.25D;
    private static final double WANDER_SPEED = 1.0D;
    private static final float WATCH_RANGE = 8.0F;
    private static final double MAX_HEALTH = 20.0D;
    private static final double MOVEMENT_SPEED = 0.25D;

    public LKEntityZebra(World world) {
        super(world);
        setSize(1.4F, 1.6F);
        getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPanic(this, 1.5D));
        tasks.addTask(2, new EntityAIMate(this, 1.0D));
        tasks.addTask(3, new EntityAITempt(this, TEMPT_SPEED, Items.wheat, false));
        tasks.addTask(3, new EntityAITempt(this, TEMPT_SPEED, mod_LionKing.corn, false));
        tasks.addTask(4, new EntityAIFollowParent(this, 1.25D));
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
        return stack != null && (stack.getItem() == Items.wheat || stack.getItem() == mod_LionKing.corn);
    }

    @Override
    protected Item getDropItem() {
        return mod_LionKing.zebraHide;
    }

    @Override
    protected void dropFewItems(boolean hitByPlayer, int looting) {
        dropItems(mod_LionKing.zebraHide, rand.nextInt(2) + rand.nextInt(1 + looting));
        int meatCount = rand.nextInt(3) + 1 + rand.nextInt(1 + looting);
        dropItems(isBurning() ? mod_LionKing.zebraCooked : mod_LionKing.zebraRaw, meatCount);
    }

    private void dropItems(Item item, int count) {
        for (int i = 0; i < count; i++) {
            dropItem(item, 1);
        }
    }

    @Override
    public boolean interact(EntityPlayer player) {
        ItemStack heldItem = player.inventory.getCurrentItem();
        if (heldItem != null && heldItem.getItem() == mod_LionKing.jar && !isChild()) {
            if (!worldObj.isRemote) {
                heldItem.stackSize--;
                player.inventory.addItemStackToInventory(new ItemStack(mod_LionKing.jarMilk));
                player.addChatMessage(new ChatComponentTranslation("chat.zebra.milk"));
            }
            return true;
        }
        return super.interact(player);
    }

    @Override
    public EntityAgeable createChild(EntityAgeable mate) {
        return new LKEntityZebra(worldObj);
    }

    @Override
    protected String getLivingSound() {
        return "lionking:zebra";
    }

    @Override
    protected String getHurtSound() {
        return "lionking:zebrahurt";
    }

    @Override
    protected String getDeathSound() {
        return "lionking:zebrahurt";
    }

    public ItemStack dropDebugItems(int type) {
        if (type == 0) {
            return new ItemStack(Items.stick, 40 + rand.nextInt(25));
        }
        if (type == 1) {
            return new ItemStack(Blocks.tallgrass, 3 + rand.nextInt(8), 2);
        }
        return null;
    }

    
    public String getEntityName() {
        return StatCollector.translateToLocal("entity.lionking.zebra.name");
    }
}
