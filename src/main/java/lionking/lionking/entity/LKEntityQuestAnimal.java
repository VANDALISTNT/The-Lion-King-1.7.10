package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.StatCollector;

import lionking.quest.LKAnimalQuest;
import lionking.common.LKCharacterSpeech;
import lionking.common.LKIngame;
import lionking.common.LKAchievementList;
import lionking.mod_LionKing;

public abstract class LKEntityQuestAnimal extends EntityAnimal {
    private int talkTick = 40;
    public LKAnimalQuest quest;

    public LKEntityQuestAnimal(World world) {
        super(world);
        quest = new LKAnimalQuest();
    }

    public abstract LKCharacterSpeech getCharacterSpeech();
    public abstract LKCharacterSpeech getChildCharacterSpeech();
    public abstract String getAnimalName();
    public abstract ItemStack getQuestItem();
    public abstract String getEntityName();

    public double getQuestItemRenderOffset() {
        return 0.25D;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        quest.init(dataWatcher);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (talkTick < 40) {
            talkTick++;
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        quest.writeToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        quest.readFromNBT(nbt);
    }

    @Override
    public boolean interact(EntityPlayer entityplayer) {
        if (!super.interact(entityplayer)) {
            if (LKIngame.hasAmulet(entityplayer) && talkTick == 40) {
                ItemStack itemstack = entityplayer.inventory.getCurrentItem();
                if (isChild()) {
                    if (!worldObj.isRemote) {
                        String message = StatCollector.translateToLocal("chat.animal.child_speech." + getChildCharacterSpeech().toString().toLowerCase());
                        LKIngame.sendMessageToAllPlayers(message);
                    }
                    talkTick = 0;
                    return true;
                } else {
                    if (quest.hasQuest() && itemstack != null && quest.isRequiredItem(itemstack)) {
                        int i = itemstack.stackSize - quest.getRequiredItem().stackSize;
                        if (i == 0) {
                            entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
                        } else {
                            itemstack.stackSize = i;
                        }
                        if (!worldObj.isRemote) {
                            String message = StatCollector.translateToLocalFormatted("chat.animal.quest_end", getAnimalName());
                            LKIngame.sendMessageToAllPlayers(message);
                            for (int j = 0; j < 5; j++) {
                                worldObj.spawnEntityInWorld(new EntityXPOrb(worldObj, posX, posY, posZ, 11 + getRNG().nextInt(5)));
                            }
                        }
                        quest.setHasQuest(false);
                        entityplayer.triggerAchievement(LKAchievementList.animalQuest);
                        talkTick = 0;
                        return true;
                    } else {
                        if (!quest.hasQuest() && getRNG().nextInt(5) == 0) {
                            if (!worldObj.isRemote) {
                                ItemStack questItem = getQuestItem();
                                quest.setQuest(questItem);
                                String message = StatCollector.translateToLocalFormatted("chat.animal.quest_start", getAnimalName(), questItem.getDisplayName());
                                LKIngame.sendMessageToAllPlayers(message);
                            }
                            talkTick = 0;
                            return true;
                        } else {
                            if (!worldObj.isRemote) {
                                String message = StatCollector.translateToLocal("chat.animal.speech." + getCharacterSpeech().toString().toLowerCase());
                                LKIngame.sendMessageToAllPlayers(message);
                            }
                            talkTick = 0;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public ItemStack getPickedResult(MovingObjectPosition target) {
        return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this));
    }
}