package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityItem;
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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.StatCollector;

import lionking.mod_LionKing;
import lionking.common.LKCharacterSpeech;
import lionking.entity.LKEntityQuestAnimal;

public class LKEntityGemsbok extends LKEntityQuestAnimal {
    public LKEntityGemsbok(World world) {
        super(world);
        setSize(0.9F, 1.4F);
        getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPanic(this, 1.3D));
        tasks.addTask(2, new EntityAIMate(this, 1D));
        tasks.addTask(3, new EntityAITempt(this, 1.2D, Items.wheat, false));
        tasks.addTask(3, new EntityAITempt(this, 1.2D, mod_LionKing.corn, false));
        tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
        tasks.addTask(5, new EntityAIWander(this, 1D));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        tasks.addTask(7, new EntityAILookIdle(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(12D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
    }

    @Override
    public boolean isAIEnabled() {
        return true;
    }

    @Override
    public boolean isBreedingItem(ItemStack itemstack) {
        return itemstack != null && (itemstack.getItem() == Items.wheat || itemstack.getItem() == mod_LionKing.corn);
    }

    protected int getDropItemId() {
        return Item.getIdFromItem(mod_LionKing.gemsbokHide);
    }

    @Override
    protected void dropFewItems(boolean flag, int i) {
        int j = getRNG().nextInt(2) + getRNG().nextInt(1 + i) + 1;
        for (int k = 0; k < j; k++) {
            dropItems(mod_LionKing.gemsbokHide, 1);
        }
        if (getRNG().nextBoolean()) {
            dropItems(mod_LionKing.gemsbokHorn, 1);
        }
    }

    private void dropItems(Item item, int count) {
        if (!worldObj.isRemote) {
            for (int i = 0; i < count; i++) {
                EntityItem entityItem = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(item, 1));
                worldObj.spawnEntityInWorld(entityItem);
            }
        }
    }

    @Override
    public EntityAgeable createChild(EntityAgeable entity) {
        return new LKEntityGemsbok(worldObj);
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
        return "lionking:zebradeath";
    }

    @Override
    public LKCharacterSpeech getCharacterSpeech() {
        return LKCharacterSpeech.GEMSBOK;
    }

    @Override
    public LKCharacterSpeech getChildCharacterSpeech() {
        return LKCharacterSpeech.GEMSBOK_CALF;
    }

    @Override
    public String getAnimalName() {
        return StatCollector.translateToLocal("entity.gemsbok.name");
    }

    @Override
    public String getEntityName() {
        return StatCollector.translateToLocal("entity.lionking.gemsbok.name");
    }

    @Override
    public ItemStack getQuestItem() {
        int i = getRNG().nextInt(5);
        switch (i) {
            default:
            case 0:
                return new ItemStack(mod_LionKing.mango, 5 + getRNG().nextInt(10));
            case 1:
                return new ItemStack(mod_LionKing.kiwano, 6 + getRNG().nextInt(17));
            case 2:
                return new ItemStack(Blocks.tallgrass, 10 + getRNG().nextInt(6), 1);
            case 3:
                return new ItemStack(mod_LionKing.prideWood, 8 + getRNG().nextInt(16));
            case 4:
                return new ItemStack(Blocks.tallgrass, 3 + getRNG().nextInt(7), 2);
        }
    }
}