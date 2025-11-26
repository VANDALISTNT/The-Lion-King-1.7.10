package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.EntityCreature;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.StatCollector;

import lionking.entity.LKEntityQuestAnimal;
import lionking.common.LKCharacter;
import lionking.common.LKLevelData; 
import lionking.common.LKIngame;   
import lionking.quest.LKQuestBase; 
import lionking.mod_LionKing;

public class LKEntityPumbaa extends EntityCreature implements LKCharacter {
    private int talkTick;
    private boolean spawningBox;

    public LKEntityPumbaa(World world) {
        super(world);
        setSize(1.2F, 1.3F);
        talkTick = 140;
        getNavigator().setAvoidsWater(true);
        tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(2, new LKEntityAIPumbaaFollowTimon(this, 1.2D));
        tasks.addTask(3, new EntityAIWander(this, 1D));
        tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(5, new EntityAILookIdle(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2D);
    }

    @Override
    public boolean isAIEnabled() {
        return true;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!worldObj.isRemote && getHealth() < getMaxHealth()) {
            setHealth(getMaxHealth());
        }
        if (worldObj.rand.nextInt(1200) == 0) {
            fart();
        }
        if (talkTick < 140) {
            talkTick++;
        }

        if (!worldObj.isRemote) {
            if (LKLevelData.pumbaaStage == 1 && talkTick == 140) {
                LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.pumbaa.stage1"));
                LKLevelData.setPumbaaStage(2);
                talkTick = 40;
            }
            if (LKLevelData.pumbaaStage == 2 && talkTick == 140) {
                LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.pumbaa.stage2"));
                LKLevelData.setPumbaaStage(3);
                talkTick = 20;
            }
            if (LKLevelData.pumbaaStage == 3 && talkTick == 140) {
                LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.pumbaa.stage3"));
                LKLevelData.setPumbaaStage(4);
                talkTick = 20;
            }
            if (LKLevelData.pumbaaStage == 4 && talkTick == 140) {
                LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.pumbaa.stage4"));
                LKLevelData.setPumbaaStage(5);
                talkTick = 0;
            }
            if (LKLevelData.pumbaaStage == 5 && talkTick == 140) {
                LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.pumbaa.stage5"));
                LKLevelData.setPumbaaStage(6);
                talkTick = 60;
            }
            if (LKLevelData.pumbaaStage == 6 && talkTick == 140) {
                LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.pumbaa.stage6"));
                LKLevelData.setPumbaaStage(7);
                talkTick = 40;
            }
            if (LKLevelData.pumbaaStage == 7 && talkTick == 140) {
                LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.pumbaa.stage7"));
                LKLevelData.setPumbaaStage(8);
                talkTick = 0;
            }
            if (LKLevelData.pumbaaStage == 8 && talkTick == 140) {
                LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.pumbaa.stage8"));
                LKLevelData.setPumbaaStage(9);
                talkTick = 0;
            }
            if (LKLevelData.pumbaaStage == 9 && talkTick == 140) {
                LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.pumbaa.stage9"));
                LKLevelData.setPumbaaStage(10);
                talkTick = 0;
                LKQuestBase.outlandsQuest.progress(7);
            }
            if (LKLevelData.pumbaaStage == 12 && talkTick == 140) {
                LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.pumbaa.stage12"));
                LKLevelData.setPumbaaStage(13);
                talkTick = 0;
            }
        }

        boolean box = spawningBox || LKLevelData.pumbaaStage == 11;

        if (box && talkTick > 20 && talkTick < 48 && talkTick % 4 == 0) {
            worldObj.playSoundAtEntity(this, "random.eat", 0.8F + 0.5F * (float) getRNG().nextInt(2), (getRNG().nextFloat() - getRNG().nextFloat()) * 0.2F + 1.0F);
        }

        if (box && talkTick > 100) {
            spawnBox();
            if (LKLevelData.pumbaaStage == 11 && !worldObj.isRemote) {
                LKLevelData.setPumbaaStage(12);
                LKQuestBase.outlandsQuest.setDelayed(false);
            }
        }
    }

    private void spawnBox() {
        if (!worldObj.isRemote) {
            motionX = 0;
            motionY = 1.5D;
            motionZ = 0;
            EntityItem item = new EntityItem(worldObj, posX + 0.5D, posY + 0.5D, posZ + 0.5D, new ItemStack(mod_LionKing.pumbaaBox));
            worldObj.spawnEntityInWorld(item);
            spawningBox = false;
        }
        fart();
    }

    public void fart() {
        worldObj.playSoundAtEntity(this, "lionking:flatulence", getSoundVolume(), (1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
        for (int i = 0; i < 14; i++) {
            double d = getRNG().nextGaussian() * 0.02D;
            double d1 = getRNG().nextGaussian() * 0.02D;
            double d2 = getRNG().nextGaussian() * 0.02D;
            worldObj.spawnParticle("smoke", (posX + (double) (getRNG().nextFloat() * width * 2.0F)) - (double) width, posY + 0.5D + (double) (getRNG().nextFloat() * height), (posZ + (double) (getRNG().nextFloat() * width * 2.0F)) - (double) width, d, d1, d2);
        }
    }

    @Override
    public boolean interact(EntityPlayer entityplayer) {
        if (!worldObj.isRemote) {
            if (LKQuestBase.outlandsQuest.getQuestStage() == 6 && LKLevelData.pumbaaStage == 0 && talkTick == 140) {
                LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.pumbaa.stage0"));
                LKLevelData.setPumbaaStage(1);
                talkTick = 40;
                return true;
            }
            if (LKLevelData.pumbaaStage == 10 && talkTick == 140) {
                ItemStack[] inv = entityplayer.inventory.mainInventory;
                int bugs = 0;
                for (int i = 0; i < inv.length; i++) {
                    ItemStack itemstack = inv[i];
                    if (itemstack != null && Item.getIdFromItem(itemstack.getItem()) == Item.getIdFromItem(mod_LionKing.bug)) {
                        bugs += itemstack.stackSize;
                    }
                }

                if (bugs >= 16 && entityplayer.inventory.hasItemStack(new ItemStack(Item.getItemFromBlock(mod_LionKing.planks))) && entityplayer.inventory.hasItemStack(new ItemStack(mod_LionKing.jarLava)) && entityplayer.inventory.hasItemStack(new ItemStack(mod_LionKing.itemTermite))) {
                    LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.pumbaa.craft_start"));
                    for (int i = 0; i < 16; i++) {
                        entityplayer.inventory.consumeInventoryItem(mod_LionKing.bug);
                    }
                    entityplayer.inventory.consumeInventoryItem(Item.getItemFromBlock(mod_LionKing.planks));
                    entityplayer.inventory.consumeInventoryItem(mod_LionKing.jarLava);
                    entityplayer.inventory.consumeInventoryItem(mod_LionKing.itemTermite);
                    LKLevelData.setPumbaaStage(11);
                    talkTick = 0;
                    LKQuestBase.outlandsQuest.setDelayed(true);
                    LKQuestBase.outlandsQuest.progress(8);
                    return true;
                } else {
                    String[] keys = {
                        "chat.pumbaa.need_ingredients1",
                        "chat.pumbaa.need_ingredients2",
                        "chat.pumbaa.need_ingredients3",
                        "chat.pumbaa.need_ingredients4"
                    };
                    LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal(keys[getRNG().nextInt(keys.length)]));
                    talkTick = 80;
                    return true;
                }
            }
            if (LKLevelData.pumbaaStage == 13 && LKQuestBase.outlandsQuest.getQuestStage() == 8) {
                ItemStack[] inv = entityplayer.inventory.mainInventory;
                int bugs = 0;
                for (int i = 0; i < inv.length; i++) {
                    ItemStack itemstack = inv[i];
                    if (itemstack != null && Item.getIdFromItem(itemstack.getItem()) == Item.getIdFromItem(mod_LionKing.bug)) {
                        bugs += itemstack.stackSize;
                    }
                }

                if (!entityplayer.inventory.hasItemStack(new ItemStack(mod_LionKing.pumbaaBox)) && bugs >= 16 && entityplayer.inventory.hasItemStack(new ItemStack(Item.getItemFromBlock(mod_LionKing.planks))) && entityplayer.inventory.hasItemStack(new ItemStack(mod_LionKing.jarLava)) && entityplayer.inventory.hasItemStack(new ItemStack(mod_LionKing.itemTermite))) {
                    LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.pumbaa.craft_start"));
                    for (int i = 0; i < 16; i++) {
                        entityplayer.inventory.consumeInventoryItem(mod_LionKing.bug);
                    }
                    entityplayer.inventory.consumeInventoryItem(Item.getItemFromBlock(mod_LionKing.planks));
                    entityplayer.inventory.consumeInventoryItem(mod_LionKing.jarLava);
                    entityplayer.inventory.consumeInventoryItem(mod_LionKing.itemTermite);
                    spawningBox = true;
                    talkTick = 0;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected String getLivingSound() {
        return "mob.pig";
    }

    @Override
    protected String getHurtSound() {
        return "mob.pig";
    }

    @Override
    protected String getDeathSound() {
        return "mob.pigdeath";
    }

    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        return false;
    }

    @Override
    protected void kill() {
        setDead();
    }

    @Override
    public float getBlockPathWeight(int i, int j, int k) {
        if (posY < 60 && j < posY) {
            return -999999F;
        }
        return worldObj.getBlock(i, j - 1, k) == Blocks.grass ? 10.0F : worldObj.getLightBrightness(i, j, k) - 0.5F;
    }

    @Override
    public ItemStack getPickedResult(MovingObjectPosition target) {
        return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this));
    }

    @Override
    public String getEntityName() {
        return StatCollector.translateToLocal("entity.lionking.pumbaa.name");
    }
}