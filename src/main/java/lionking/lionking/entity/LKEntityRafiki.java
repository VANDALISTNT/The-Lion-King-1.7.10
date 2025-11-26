package lionking.entity;

import net.minecraft.world.WorldType;
import net.minecraft.world.World;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MathHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.StatCollector;
import net.minecraft.init.Items;
import net.minecraft.block.Block;

import lionking.mod_LionKing;
import lionking.quest.LKQuestBase;
import lionking.block.LKBlockOutlandsPortal;
import lionking.common.LKCharacter;
import lionking.common.LKIngame;
import lionking.common.LKLevelData;
import lionking.common.LKAchievementList;
import lionking.entity.LKEntityScar;
import lionking.entity.LKEntities;

public class LKEntityRafiki extends EntityCreature implements LKCharacter {
    private int talkTick;
    private boolean hasSpawnedOneScar;
    private int processTick;
    private int talkPortalTick;
    private int talkDustTick;
    public boolean isThisTheRealRafiki;

    public LKEntityRafiki(World world) {
        super(world);
        setSize(0.7F, 1.6F);
        talkTick = 40;
        hasSpawnedOneScar = false;
        processTick = 100;
        talkPortalTick = 120;
        talkDustTick = 120;
        isThisTheRealRafiki = false;
        getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIWander(this, 1.0D));
        tasks.addTask(2, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(3, new EntityAILookIdle(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2D);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataWatcher.addObject(17, Byte.valueOf((byte) 0));
        dataWatcher.addObject(18, Byte.valueOf((byte) 0));
        dataWatcher.addObject(19, Byte.valueOf((byte) 0));
        dataWatcher.addObject(20, Byte.valueOf((byte) 0));
        dataWatcher.addObject(21, Byte.valueOf((byte) 0));
        dataWatcher.addObject(22, Byte.valueOf((byte) 0));
        dataWatcher.addObject(23, Byte.valueOf((byte) 0));
        dataWatcher.addObject(24, Byte.valueOf((byte) 0));
        dataWatcher.addObject(25, Byte.valueOf((byte) 0));
        dataWatcher.addObject(26, Byte.valueOf((byte) 0));
        dataWatcher.addObject(27, Byte.valueOf((byte) 0));
        dataWatcher.addObject(28, Byte.valueOf((byte) 0));
        dataWatcher.addObject(29, Byte.valueOf((byte) 0));
        dataWatcher.addObject(30, Byte.valueOf((byte) 0));
        dataWatcher.addObject(31, Byte.valueOf((byte) 0));
    }

    @Override
    public boolean isAIEnabled() {
        return true;
    }

    @Override
    public void setJumping(boolean flag) {
    }

    @Override
    protected void jump() {
    }

    @Override
    public String getEntityName() {
        return "Rafiki";
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        
        if (LKLevelData.ziraStage == 14 && !worldObj.isRemote) {
            setDead();
        }
        
        if (isThisTheRealRafiki && worldObj.getWorldInfo().getWorldTime() % 100L == 0L) {
            ChunkCoordinates currentPos = new ChunkCoordinates(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
            double distanceFromHome = Math.sqrt(currentPos.getDistanceSquared(0, 103, 0));
            if (distanceFromHome > 20.0D) {
                for (int i = 0; i < 12; i++) {
                    double d = getRNG().nextGaussian() * 0.02D;
                    double d1 = getRNG().nextGaussian() * 0.02D;
                    double d2 = getRNG().nextGaussian() * 0.02D;
                    worldObj.spawnParticle("portal", (posX + (double) (getRNG().nextFloat() * width * 2.0F)) - (double) width, posY + 0.5D + (double) (getRNG().nextFloat() * height), (posZ + (double) (getRNG().nextFloat() * width * 2.0F)) - (double) width, d, d1, d2);
                }
                setLocationAndAngles(0, 103, 0, rotationYaw, 0.0F);
            }
        }

        boolean flag = false;
        for (int i = -16; i < 17; i++) {
            for (int j = -5; j < 6; j++) {
                for (int k = -16; k < 17; k++) {
                    if (worldObj.getBlock(MathHelper.floor_double(posX) + i, MathHelper.floor_double(posY) + j, MathHelper.floor_double(posZ) + k) == mod_LionKing.flowerVase) {
                        flag = true;
                    }
                }
            }
        }
        if (flag && getRNG().nextInt(150) == 0) {
            addHeartFX();
        }

        if (!worldObj.isRemote && getHealth() < getMaxHealth()) {
            setHealth(getMaxHealth());
        }

        if (talkTick < 40) talkTick++;
        if (processTick < 100) processTick++;
        if (talkPortalTick < 120) talkPortalTick++;
        if (talkDustTick < 120) talkDustTick++;

        if (canProcess() && getHasBegunPortal() && !getHasFinishedPortal()) {
            if (!worldObj.isRemote) {
                String message = StatCollector.translateToLocal("chat.rafiki.portal_start");
                LKIngame.sendMessageToAllPlayers(message);
            }
            processTick = 0;
            LKQuestBase.rafikiQuest.setDelayed(false);
            dataWatcher.updateObject(22, Byte.valueOf((byte) 1));

            worldObj.setBlockToAir(-9, 105, -1);
            worldObj.setBlockToAir(-9, 105, 0);
            worldObj.setBlockToAir(-9, 106, -1);
            worldObj.setBlockToAir(-9, 106, 0);
            worldObj.setBlockToAir(-9, 107, -1);
            worldObj.setBlockToAir(-9, 107, 0);

            worldObj.createExplosion(this, -6, 106, -0.5, 0.0F, false);
            LKBlockOutlandsPortal.tryToCreatePortal(worldObj, -13, 105, -1);
            talkPortalTick = 0;
        }
        
        if (getHasFinishedPortal() && canTalkPortal() && !getHasSpokenAboutPortal()) {
            if (!worldObj.isRemote) {
                String message = StatCollector.translateToLocal("chat.rafiki.portal_guide");
                LKIngame.sendMessageToAllPlayers(message);
            }
            dataWatcher.updateObject(23, Byte.valueOf((byte) 1));
        }
        
        if (getHasTakenMango() && canTalkDust() && !getHasSpokenDust()) {
            if (!worldObj.isRemote) {
                String message = StatCollector.translateToLocal("chat.rafiki.dust_kings");
                LKIngame.sendMessageToAllPlayers(message);
            }
            dataWatcher.updateObject(26, Byte.valueOf((byte) 1));
            talkDustTick = 0;
        }
        
        if (getHasSpokenDust() && canTalkDust() && !getHasSpokenMagic()) {
            if (!worldObj.isRemote) {
                String message = StatCollector.translateToLocal("chat.rafiki.dust_discovery");
                LKIngame.sendMessageToAllPlayers(message);
            }
            dataWatcher.updateObject(27, Byte.valueOf((byte) 1));
            talkDustTick = 0;
        }
        
        if (getHasSpokenMagic() && canTalkDust() && !getHasSpokenMagicDust()) {
            if (!worldObj.isRemote) {
                String message = StatCollector.translateToLocal("chat.rafiki.dust_magic");
                LKIngame.sendMessageToAllPlayers(message);
            }
            dataWatcher.updateObject(28, Byte.valueOf((byte) 1));
            talkDustTick = 0;
        }
        
        if (getHasSpokenMagicDust() && canTalkDust() && !getHasGivenMagicDust()) {
            if (!worldObj.isRemote) {
                String message = StatCollector.translateToLocal("chat.rafiki.dust_give");
                LKIngame.sendMessageToAllPlayers(message);
                dropItem(mod_LionKing.lionDust, 4);
            }
            dataWatcher.updateObject(29, Byte.valueOf((byte) 1));
            talkDustTick = 0;
        }
        
        if (getHasGivenMagicDust() && canTalkDust() && !getHasSpokenAltar()) {
            if (!worldObj.isRemote) {
                String message = StatCollector.translateToLocal("chat.rafiki.altar_craft");
                LKIngame.sendMessageToAllPlayers(message);
            }
            dataWatcher.updateObject(30, Byte.valueOf((byte) 1));
            talkDustTick = 0;
        }
        
        if (getHasSpokenAltar() && canTalkDust() && !getHasSpokenAltarUse()) {
            if (!worldObj.isRemote) {
                String message = StatCollector.translateToLocal("chat.rafiki.altar_use");
                LKIngame.sendMessageToAllPlayers(message);
            }
            dataWatcher.updateObject(31, Byte.valueOf((byte) 1));
            talkDustTick = 0;
            LKQuestBase.rafikiQuest.setDelayed(false);
        }

        if (LKLevelData.ziraStage == 20 && !worldObj.isRemote) {
            String message = StatCollector.translateToLocal("chat.rafiki.zira_return");
            LKIngame.sendMessageToAllPlayers(message);
            LKLevelData.setZiraStage(21);
            processTick = 0;
        }

        if (LKLevelData.ziraStage == 21 && processTick > 80 && !worldObj.isRemote) {
            String message = StatCollector.translateToLocal("chat.rafiki.zira_end");
            LKIngame.sendMessageToAllPlayers(message);
            LKLevelData.setZiraStage(22);
            LKQuestBase.outlandsQuest.setDelayed(false);
        }
    }

    public void addHeartFX() {
        for (int i = 0; i < 7; i++) {
            double d = getRNG().nextGaussian() * 0.02D;
            double d1 = getRNG().nextGaussian() * 0.02D;
            double d2 = getRNG().nextGaussian() * 0.02D;
            worldObj.spawnParticle("heart", (posX + (double) (getRNG().nextFloat() * width * 2.0F)) - (double) width, posY + 0.5D + (double) (getRNG().nextFloat() * height), (posZ + (double) (getRNG().nextFloat() * width * 2.0F)) - (double) width, d, d1, d2);
        }
    }

    private boolean canTalk() {
        return talkTick == 40;
    }

    private boolean canProcess() {
        return processTick == 100;
    }

    private boolean canTalkPortal() {
        return talkPortalTick == 120;
    }

    private boolean canTalkDust() {
        return talkDustTick == 120;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("TheRealRafiki", isThisTheRealRafiki);
        if (dataWatcher.getWatchableObjectByte(17) == 1) nbttagcompound.setBoolean("HasInteracted", true);
        if (dataWatcher.getWatchableObjectByte(18) == 1) nbttagcompound.setBoolean("HasSpokenAboutStick", true);
        if (dataWatcher.getWatchableObjectByte(19) == 1) nbttagcompound.setBoolean("HasObtainedStick", true);
        if (dataWatcher.getWatchableObjectByte(20) == 1) nbttagcompound.setBoolean("HasSpawnedScar", true);
        if (dataWatcher.getWatchableObjectByte(21) == 1) nbttagcompound.setBoolean("HasBegunPortal", true);
        if (dataWatcher.getWatchableObjectByte(22) == 1) nbttagcompound.setBoolean("HasFinishedPortal", true);
        if (dataWatcher.getWatchableObjectByte(23) == 1) nbttagcompound.setBoolean("HasSpokenAboutPortal", true);
        if (dataWatcher.getWatchableObjectByte(24) == 1) nbttagcompound.setBoolean("HasTakenTermites", true);
        if (dataWatcher.getWatchableObjectByte(25) == 1) nbttagcompound.setBoolean("HasTakenMango", true);
        if (dataWatcher.getWatchableObjectByte(26) == 1) nbttagcompound.setBoolean("HasSpokenDust", true);
        if (dataWatcher.getWatchableObjectByte(27) == 1) nbttagcompound.setBoolean("HasSpokenMagic", true);
        if (dataWatcher.getWatchableObjectByte(28) == 1) nbttagcompound.setBoolean("HasSpokenMagicDust", true);
        if (dataWatcher.getWatchableObjectByte(29) == 1) nbttagcompound.setBoolean("HasGivenMagicDust", true);
        if (dataWatcher.getWatchableObjectByte(30) == 1) nbttagcompound.setBoolean("HasSpokenAltar", true);
        if (dataWatcher.getWatchableObjectByte(31) == 1) nbttagcompound.setBoolean("HasSpokenAltarUse", true);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        isThisTheRealRafiki = nbttagcompound.getBoolean("TheRealRafiki");
        dataWatcher.updateObject(17, Byte.valueOf((byte) (nbttagcompound.getBoolean("HasInteracted") ? 1 : 0)));
        dataWatcher.updateObject(18, Byte.valueOf((byte) (nbttagcompound.getBoolean("HasSpokenAboutStick") ? 1 : 0)));
        dataWatcher.updateObject(19, Byte.valueOf((byte) (nbttagcompound.getBoolean("HasObtainedStick") ? 1 : 0)));
        dataWatcher.updateObject(20, Byte.valueOf((byte) (nbttagcompound.getBoolean("HasSpawnedScar") ? 1 : 0)));
        dataWatcher.updateObject(21, Byte.valueOf((byte) (nbttagcompound.getBoolean("HasBegunPortal") ? 1 : 0)));
        dataWatcher.updateObject(22, Byte.valueOf((byte) (nbttagcompound.getBoolean("HasFinishedPortal") ? 1 : 0)));
        dataWatcher.updateObject(23, Byte.valueOf((byte) (nbttagcompound.getBoolean("HasSpokenAboutPortal") ? 1 : 0)));
        dataWatcher.updateObject(24, Byte.valueOf((byte) (nbttagcompound.getBoolean("HasTakenTermites") ? 1 : 0)));
        dataWatcher.updateObject(25, Byte.valueOf((byte) (nbttagcompound.getBoolean("HasTakenMango") ? 1 : 0)));
        dataWatcher.updateObject(26, Byte.valueOf((byte) (nbttagcompound.getBoolean("HasSpokenDust") ? 1 : 0)));
        dataWatcher.updateObject(27, Byte.valueOf((byte) (nbttagcompound.getBoolean("HasSpokenMagic") ? 1 : 0)));
        dataWatcher.updateObject(28, Byte.valueOf((byte) (nbttagcompound.getBoolean("HasSpokenMagicDust") ? 1 : 0)));
        dataWatcher.updateObject(29, Byte.valueOf((byte) (nbttagcompound.getBoolean("HasGivenMagicDust") ? 1 : 0)));
        dataWatcher.updateObject(30, Byte.valueOf((byte) (nbttagcompound.getBoolean("HasSpokenAltar") ? 1 : 0)));
        dataWatcher.updateObject(31, Byte.valueOf((byte) (nbttagcompound.getBoolean("HasSpokenAltarUse") ? 1 : 0)));
    }

    public boolean getHasInteracted() {
        return dataWatcher.getWatchableObjectByte(17) == 1;
    }

    public boolean getHasSpokenAboutStick() {
        return dataWatcher.getWatchableObjectByte(18) == 1 && getHasInteracted();
    }

    public boolean getHasObtainedStick() {
        return dataWatcher.getWatchableObjectByte(19) == 1 && getHasSpokenAboutStick();
    }

    public boolean getHasSpawnedScar() {
        return dataWatcher.getWatchableObjectByte(20) == 1 && getHasObtainedStick();
    }

    public boolean getHasBegunPortal() {
        return dataWatcher.getWatchableObjectByte(21) == 1 && getHasSpawnedScar();
    }

    public boolean getHasFinishedPortal() {
        return dataWatcher.getWatchableObjectByte(22) == 1 && getHasBegunPortal();
    }

    public boolean getHasSpokenAboutPortal() {
        return dataWatcher.getWatchableObjectByte(23) == 1 && getHasFinishedPortal();
    }

    public boolean getHasTakenTermites() {
        return dataWatcher.getWatchableObjectByte(24) == 1 && getHasSpokenAboutPortal();
    }

    public boolean getHasTakenMango() {
        return dataWatcher.getWatchableObjectByte(25) == 1 && getHasTakenTermites();
    }

    public boolean getHasSpokenDust() {
        return dataWatcher.getWatchableObjectByte(26) == 1 && getHasTakenMango();
    }

    public boolean getHasSpokenMagic() {
        return dataWatcher.getWatchableObjectByte(27) == 1 && getHasSpokenDust();
    }

    public boolean getHasSpokenMagicDust() {
        return dataWatcher.getWatchableObjectByte(28) == 1 && getHasSpokenMagic();
    }

    public boolean getHasGivenMagicDust() {
        return dataWatcher.getWatchableObjectByte(29) == 1 && getHasSpokenMagicDust();
    }

    public boolean getHasSpokenAltar() {
        return dataWatcher.getWatchableObjectByte(30) == 1 && getHasGivenMagicDust();
    }

    public boolean getHasSpokenAltarUse() {
        return dataWatcher.getWatchableObjectByte(31) == 1 && getHasSpokenAltar();
    }

    @Override
    public boolean interact(EntityPlayer entityplayer) {
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();

        if (!getHasInteracted() && canTalk()) {
            if (!worldObj.isRemote) {
                String message = StatCollector.translateToLocal("chat.rafiki.welcome");
                LKIngame.sendMessageToAllPlayers(message);
            }
            dataWatcher.updateObject(17, Byte.valueOf((byte) 1));
            talkTick = 0;
            return true;
        }
        
        if (getHasInteracted() && canTalk() && itemstack != null) {
            if (itemstack.getItem() == mod_LionKing.silver) {
                if (itemstack.stackSize > 3) {
                    itemstack.stackSize -= 3;
                    if (!worldObj.isRemote) {
                        dropItem(mod_LionKing.rafikiCoin, 1);
                        String message = StatCollector.translateToLocal("chat.rafiki.coin_give");
                        LKIngame.sendMessageToAllPlayers(message);
                    }
                } else if (itemstack.stackSize == 3) {
                    entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
                    if (!worldObj.isRemote) {
                        String message = StatCollector.translateToLocal("chat.rafiki.coin_give");
                        LKIngame.sendMessageToAllPlayers(message);
                        dropItem(mod_LionKing.rafikiCoin, 1);
                    }
                } else {
                    if (!worldObj.isRemote) {
                        String message = StatCollector.translateToLocal("chat.rafiki.coin_need");
                        LKIngame.sendMessageToAllPlayers(message);
                    }
                }
                talkTick = 0;
                return true;
            }
            
            if (itemstack.getItem() == mod_LionKing.fur && entityplayer.inventory.hasItem(Items.book)) {
                if (itemstack.stackSize > 1) itemstack.stackSize--;
                else entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
                entityplayer.inventory.consumeInventoryItem(Items.book);
                if (!worldObj.isRemote) {
                    String message = StatCollector.translateToLocal("chat.rafiki.book_give");
                    LKIngame.sendMessageToAllPlayers(message);
                    dropItem(mod_LionKing.questBook, 1);
                }
                talkTick = 0;
                return true;
            }
            if (itemstack.getItem() == Items.book && entityplayer.inventory.hasItem(mod_LionKing.fur)) {
                if (itemstack.stackSize > 1) itemstack.stackSize--;
                else entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
                entityplayer.inventory.consumeInventoryItem(mod_LionKing.fur);
                if (!worldObj.isRemote) {
                    String message = StatCollector.translateToLocal("chat.rafiki.book_give");
                    LKIngame.sendMessageToAllPlayers(message);
                    dropItem(mod_LionKing.questBook, 1);
                }
                talkTick = 0;
                return true;
            }
        }
        
        if (!getHasSpokenAboutStick() && canTalk()) {
            if (!worldObj.isRemote) {
                String message = StatCollector.translateToLocal("chat.rafiki.stick_offer");
                LKIngame.sendMessageToAllPlayers(message);
            }
            dataWatcher.updateObject(18, Byte.valueOf((byte) 1));
            talkTick = 0;
            LKQuestBase.rafikiQuest.progress(1);
            return true;
        }
        
        if (!getHasObtainedStick() && canTalk()) {
            if (itemstack != null && itemstack.getItem() == mod_LionKing.hyenaBone && itemstack.stackSize == 64) {
                entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, new ItemStack(mod_LionKing.rafikiStick));
                if (!worldObj.isRemote) {
                    String message = StatCollector.translateToLocal("chat.rafiki.stick_give");
                    LKIngame.sendMessageToAllPlayers(message);
                }
                dataWatcher.updateObject(19, Byte.valueOf((byte) 1));
                entityplayer.triggerAchievement(LKAchievementList.getStick);
                talkTick = 0;
                LKQuestBase.rafikiQuest.setDelayed(true);
                LKQuestBase.rafikiQuest.progress(2);
                return true;
            } else {
                if (!worldObj.isRemote) {
                    String message = StatCollector.translateToLocal("chat.rafiki.hyena_bones");
                    LKIngame.sendMessageToAllPlayers(message);
                }
                talkTick = 0;
                return true;
            }
        }
        
        if (getHasObtainedStick() && canTalk()) {
            if (itemstack != null && itemstack.getItem() == mod_LionKing.hyenaBone && itemstack.stackSize == 64) {
                entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, new ItemStack(mod_LionKing.rafikiStick));
                if (!worldObj.isRemote) {
                    String message = StatCollector.translateToLocal("chat.rafiki.stick_another");
                    LKIngame.sendMessageToAllPlayers(message);
                }
                talkTick = 0;
                return true;
            } else if (!getHasSpawnedScar()) {
                if (!worldObj.isRemote) {
                    String message = StatCollector.translateToLocal("chat.rafiki.scar_return");
                    LKIngame.sendMessageToAllPlayers(message);
                }
                dataWatcher.updateObject(20, Byte.valueOf((byte) 1));
                talkTick = 0;

                if (worldObj.getWorldInfo().getTerrainType() == WorldType.FLAT && dimension == 0) {
                    if (!worldObj.isRemote) {
                        LKEntityScar scar = new LKEntityScar(worldObj);
                        scar.setLocationAndAngles(posX, posY, posZ, 0F, 0F);
                        worldObj.spawnEntityInWorld(scar);
                        hasSpawnedOneScar = true;
                    }
                } else {
                    int spawnAttempts = 0;
                    while (!hasSpawnedOneScar && spawnAttempts < 100) {
                        int i = MathHelper.floor_double(posX);
                        int j = MathHelper.floor_double(posY);
                        int k = MathHelper.floor_double(posZ);

                        int i1 = i + getRNG().nextInt(80) - getRNG().nextInt(80);
                        int j1 = 18 + getRNG().nextInt(18);
                        int k1 = k + getRNG().nextInt(80) - getRNG().nextInt(80);

                        tryToSpawnScar(worldObj, i1, j1, k1);
                        spawnAttempts++;
                    }
                    if (!hasSpawnedOneScar && !worldObj.isRemote) {
                        String message = StatCollector.translateToLocal("chat.rafiki.scar_spawn_failed");
                        LKIngame.sendMessageToAllPlayers(message);
                    }
                }

                LKQuestBase.rafikiQuest.setDelayed(false);
                return true;
            }
        }
        
        if (!getHasBegunPortal() && canTalk()) {
            if (LKQuestBase.rafikiQuest.getQuestStage() == 3) {
                dataWatcher.updateObject(21, Byte.valueOf((byte) 1));
                if (!worldObj.isRemote) {
                    String message = StatCollector.translateToLocal("chat.rafiki.asante_sana");
                    LKIngame.sendMessageToAllPlayers(message);
                }
                talkTick = 0;
                processTick = 0;
                LKQuestBase.rafikiQuest.setDelayed(true);
                LKQuestBase.rafikiQuest.progress(4);
                return true;
            } else {
                if (!worldObj.isRemote) {
                    String message = StatCollector.translateToLocal("chat.rafiki.mention_scar");
                    LKIngame.sendMessageToAllPlayers(message);
                }
                talkTick = 0;
                return true;
            }
        }
        
        if (getHasSpokenAboutPortal() && !getHasTakenTermites() && canTalk()) {
            if (itemstack != null && itemstack.getItem() == mod_LionKing.termiteDust && itemstack.stackSize >= 4) {
                entityplayer.inventory.consumeInventoryItem(mod_LionKing.termiteDust);
                entityplayer.inventory.consumeInventoryItem(mod_LionKing.termiteDust);
                entityplayer.inventory.consumeInventoryItem(mod_LionKing.termiteDust);
                entityplayer.inventory.consumeInventoryItem(mod_LionKing.termiteDust);
                for (int i = 0; i < 7; i++) {
                    double d = getRNG().nextGaussian() * 0.02D;
                    double d1 = getRNG().nextGaussian() * 0.02D;
                    double d2 = getRNG().nextGaussian() * 0.02D;
                    worldObj.spawnParticle("smoke", (posX + (double) (getRNG().nextFloat() * width * 2.0F)) - (double) width, posY + 0.5D + (double) (getRNG().nextFloat() * height), (posZ + (double) (getRNG().nextFloat() * width * 2.0F)) - (double) width, d, d1, d2);
                }
                if (!worldObj.isRemote) {
                    String message = StatCollector.translateToLocal("chat.rafiki.termite_taken");
                    LKIngame.sendMessageToAllPlayers(message);
                }
                dataWatcher.updateObject(24, Byte.valueOf((byte) 1));
                talkTick = 0;
                LKQuestBase.rafikiQuest.progress(5);
                return true;
            } else {
                if (!worldObj.isRemote) {
                    String message = StatCollector.translateToLocal("chat.rafiki.termites");
                    LKIngame.sendMessageToAllPlayers(message);
                }
                talkTick = 0;
                return true;
            }
        }
        
        if (getHasTakenTermites() && !getHasTakenMango() && canTalk()) {
            if (itemstack != null && itemstack.getItem() == mod_LionKing.mangoDust && itemstack.stackSize >= 4) {
                entityplayer.inventory.consumeInventoryItem(mod_LionKing.mangoDust);
                entityplayer.inventory.consumeInventoryItem(mod_LionKing.mangoDust);
                entityplayer.inventory.consumeInventoryItem(mod_LionKing.mangoDust);
                entityplayer.inventory.consumeInventoryItem(mod_LionKing.mangoDust);
                for (int i = 0; i < 7; i++) {
                    double d = getRNG().nextGaussian() * 0.02D;
                    double d1 = getRNG().nextGaussian() * 0.02D;
                    double d2 = getRNG().nextGaussian() * 0.02D;
                    worldObj.spawnParticle("smoke", (posX + (double) (getRNG().nextFloat() * width * 2.0F)) - (double) width, posY + 0.5D + (double) (getRNG().nextFloat() * height), (posZ + (double) (getRNG().nextFloat() * width * 2.0F)) - (double) width, d, d1, d2);
                }
                if (!worldObj.isRemote) {
                    String message = StatCollector.translateToLocal("chat.rafiki.mango_taken");
                    LKIngame.sendMessageToAllPlayers(message);
                }
                dataWatcher.updateObject(25, Byte.valueOf((byte) 1));
                talkTick = 0;
                talkDustTick = 0;
                LKQuestBase.rafikiQuest.setDelayed(true);
                LKQuestBase.rafikiQuest.progress(6);
                return true;
            } else {
                if (!worldObj.isRemote) {
                    String message = StatCollector.translateToLocal("chat.rafiki.mangoes");
                    LKIngame.sendMessageToAllPlayers(message);
                }
                talkTick = 0;
                return true;
            }
        }
        
        if (getHasSpokenAltarUse() && canTalk()) {
            if (itemstack != null && itemstack.getItem() == mod_LionKing.mangoDust) {
                if (entityplayer.inventory.hasItem(mod_LionKing.termiteDust)) {
                    entityplayer.inventory.consumeInventoryItem(mod_LionKing.mangoDust);
                    entityplayer.inventory.consumeInventoryItem(mod_LionKing.termiteDust);
                    if (!worldObj.isRemote) {
                        String message = StatCollector.translateToLocal("chat.rafiki.dust_more");
                        LKIngame.sendMessageToAllPlayers(message);
                        dropItem(mod_LionKing.lionDust, 1);
                    }
                    talkTick = 0;
                    return true;
                }
            }
            if (itemstack != null && itemstack.getItem() == mod_LionKing.termiteDust) {
                if (entityplayer.inventory.hasItem(mod_LionKing.mangoDust)) {
                    entityplayer.inventory.consumeInventoryItem(mod_LionKing.mangoDust);
                    entityplayer.inventory.consumeInventoryItem(mod_LionKing.termiteDust);
                    if (!worldObj.isRemote) {
                        String message = StatCollector.translateToLocal("chat.rafiki.dust_more");
                        LKIngame.sendMessageToAllPlayers(message);
                        dropItem(mod_LionKing.lionDust, 1);
                    }
                    talkTick = 0;
                    return true;
                }
            } else {
                if (!LKQuestBase.rafikiQuest.isComplete()) {
                    if (!worldObj.isRemote) {
                        String message = StatCollector.translateToLocal("chat.rafiki.star_altar");
                        LKIngame.sendMessageToAllPlayers(message);
                    }
                    talkTick = 0;
                    return true;
                }
            }
        }
        
        if (canTalk() && LKQuestBase.rafikiQuest.isComplete() && (LKLevelData.ziraStage < 19 || LKLevelData.ziraStage > 21)) {
            if (!worldObj.isRemote) {
                String message = StatCollector.translateToLocal("chat.rafiki.hint");
                LKIngame.sendMessageToAllPlayers(message);
            }
            talkTick = 0;
            return true;
        }
        return false;
    }

    private void tryToSpawnScar(World world, int i, int j, int k) {
        if (canScarSpawnHere(world, i, j, k)) {
            if (!worldObj.isRemote) {
                LKEntityScar scar = new LKEntityScar(world);
                scar.setLocationAndAngles(i, j, k, 0F, 0F);
                world.spawnEntityInWorld(scar);
                hasSpawnedOneScar = true;
            }
        }
    }

    @Override
    protected String getLivingSound() {
        return getRNG().nextBoolean() ? "lionking:rafiki" : null;
    }

    @Override
    protected String getHurtSound() {
        return "lionking:rafiki";
    }

    @Override
    protected String getDeathSound() {
        return "lionking:rafiki";
    }

    private boolean canScarSpawnHere(World world, int i, int j, int k) {
        return world.isAirBlock(i, j, k) && world.isAirBlock(i, j + 1, k) && world.getBlock(i, j - 1, k).isOpaqueCube();
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    public ItemStack getHeldItem() {
        return new ItemStack(mod_LionKing.rafikiStick);
    }

    @Override
    public float getBlockPathWeight(int i, int j, int k) {
        return worldObj.getBlock(i, j - 1, k) == mod_LionKing.rafikiWood && worldObj.getBlockMetadata(i, j - 1, k) == 2 && worldObj.isAirBlock(i, j, k) ? 20.0F : -999999F;
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
    public ItemStack getPickedResult(MovingObjectPosition target) {
        return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this));
    }
}