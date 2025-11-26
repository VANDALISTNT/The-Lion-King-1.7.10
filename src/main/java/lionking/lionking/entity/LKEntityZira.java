package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MathHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.Entity;
import net.minecraft.block.Block;
import net.minecraft.util.StatCollector;
import net.minecraft.init.Blocks; 

import lionking.mod_LionKing;
import lionking.common.LKLevelData;
import lionking.quest.LKQuestBase;
import lionking.common.LKIngame;
import lionking.common.LKCharacter;
import lionking.common.LKAngerable;
import lionking.common.LKAchievementList;
import lionking.entity.LKEntities;
import lionking.entity.LKEntityAILionAttack;
import lionking.entity.LKEntityAIAngerableAttackableTarget;

import java.util.List;

public class LKEntityZira extends EntityCreature implements LKCharacter, LKAngerable {
    private static final int TALK_DELAY = 40;
    private static final int SELF_TALK_DELAY = 80;
    private static final double DETECTION_RANGE = 24.0D;
    private static final double SPAWN_RANGE = 32.0D;
    private static final int MAX_OUTLANDERS = 16;

    private int talkTick = TALK_DELAY;
    private int selfTalkTick = SELF_TALK_DELAY;

    public LKEntityZira(World world) {
        super(world);
        getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new LKEntityAILionAttack(this, EntityPlayer.class, 1.7D, false));
        tasks.addTask(2, new EntityAIWander(this, 1.0D));
        tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
        tasks.addTask(4, new EntityAILookIdle(this));
        targetTasks.addTask(0, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(1, new LKEntityAIAngerableAttackableTarget(this, EntityPlayer.class, 0, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(300.0D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2D);
    }

    @Override
    public boolean isHostile() {
        return LKLevelData.ziraStage >= 27;
    }

    @Override
    protected int getExperiencePoints(EntityPlayer player) {
        return 0;
    }

    private void spawnOutlanders(int count, boolean inMound) {
        for (int i = 0; i < count; i++) {
            int x = MathHelper.floor_double(posX);
            int y = MathHelper.floor_double(posY);
            int z = MathHelper.floor_double(posZ);

            LKEntityOutlander outlander = rand.nextBoolean() ? new LKEntityOutlander(worldObj) : new LKEntityOutlandess(worldObj);
            outlander.inMound = inMound;
            outlander.setLocationAndAngles(x + 0.5D, y, z + 0.5D, rand.nextFloat() * 360.0F, 0.0F);

            if (!worldObj.isRemote) {
                worldObj.spawnEntityInWorld(outlander);
            }
            outlander.spawnExplosionParticle();
        }
    }

    public void spawnOutlandersInMound() {
        LKLevelData.setOutlandersHostile(1);
        spawnOutlanders(6, true);
    }

    public void spawnOutlandersInTree() {
        spawnOutlanders(6, true);
    }

    @Override
    public boolean isAIEnabled() {
        return true;
    }

    public void setSelfTalkTick(int tick) {
        this.selfTalkTick = tick;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (!worldObj.isRemote) {
            if (getHealth() < getMaxHealth() && LKLevelData.ziraStage < 27) {
                setHealth(getMaxHealth());
            }

            if (talkTick < TALK_DELAY) talkTick++;
            if (selfTalkTick < SELF_TALK_DELAY) selfTalkTick++;

            handleQuestInteractions();
            spawnRandomOutlanders();
            handleZiraStages();
        }
    }

    private void handleQuestInteractions() {
        EntityPlayer closestPlayer = worldObj.getClosestPlayerToEntity(this, DETECTION_RANGE);
        if (closestPlayer == null || LKQuestBase.outlandsQuest.getQuestStage() != 0 || !LKQuestBase.outlandsQuest.isDelayed()) return;

        LKQuestBase.outlandsQuest.setDelayed(false);
        LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.zira.quest0"));
        LKLevelData.setOutlandersHostile(0);

        List<?> nearbyEntities = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(SPAWN_RANGE, SPAWN_RANGE, SPAWN_RANGE));
        for (Object entity : nearbyEntities) {
            if (entity instanceof LKEntityOutlander) {
                LKEntityOutlander outlander = (LKEntityOutlander) entity;
                if (outlander.inMound && outlander.getAttackTarget() instanceof EntityPlayer) {
                    outlander.setAttackTarget(null);
                }
            }
        }
    }

    private void spawnRandomOutlanders() {
        if (rand.nextInt(400) != 0) return;

        int outlanderCount = countNearbyOutlanders();
        if (outlanderCount >= MAX_OUTLANDERS) return;

        int attempts = 0;
        while (attempts < 256) {
            int x = MathHelper.floor_double(posX) + (LKLevelData.ziraStage < 14 ? rand.nextInt(33) - 16 : rand.nextInt(9) - 4);
            int y = MathHelper.floor_double(posY) + (LKLevelData.ziraStage < 14 ? rand.nextInt(33) - 10 : rand.nextInt(3) - 1);
            int z = MathHelper.floor_double(posZ) + (LKLevelData.ziraStage < 14 ? rand.nextInt(33) - 16 : rand.nextInt(9) - 4);

            Block spawnBlock = LKLevelData.ziraStage < 14 ? mod_LionKing.outlandsPortalFrame : mod_LionKing.rafikiWood;
            if (worldObj.getBlock(x, y, z) == spawnBlock && worldObj.isAirBlock(x, y + 1, z)) {
                LKEntityOutlander outlander = rand.nextBoolean() ? new LKEntityOutlander(worldObj) : new LKEntityOutlandess(worldObj);
                outlander.inMound = true;
                outlander.setLocationAndAngles(x + 0.5D, y + 1, z + 0.5D, rand.nextFloat() * 360.0F, 0.0F);
                if (outlander.getCanSpawnHere()) {
                    worldObj.spawnEntityInWorld(outlander);
                    outlander.spawnExplosionParticle();
                    break;
                }
            }
            attempts++;
        }
    }

    private int countNearbyOutlanders() {
        List<?> nearbyEntities = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(SPAWN_RANGE, SPAWN_RANGE, SPAWN_RANGE));
        int count = 0;
        for (Object entity : nearbyEntities) {
            if (entity instanceof LKEntityOutlander && ((LKEntityOutlander) entity).inMound) {
                count++;
            }
        }
        return count;
    }

    private void handleZiraStages() {
        if (LKLevelData.ziraStage == 11 && selfTalkTick == SELF_TALK_DELAY) {
            LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.zira.stage11"));
            LKLevelData.setZiraStage(12);
            selfTalkTick = 0;
        } else if (LKLevelData.ziraStage == 12 && selfTalkTick == SELF_TALK_DELAY) {
            LKLevelData.setZiraStage(13);
            selfTalkTick = 0;
            LKQuestBase.outlandsQuest.setDelayed(false);
            worldObj.playSoundAtEntity(this, "minecraft:portal.travel", 1.0F, rand.nextFloat() * 0.4F + 0.8F);
            spawnParticles("flame", 24, 0.75F, 0.25F);
        } else if (LKLevelData.ziraStage == 13) {
            despawnWithOutlanders();
        } else if (LKLevelData.ziraStage == 17 && LKQuestBase.outlandsQuest.getQuestStage() == 8) {
            checkForPumbaaBox();
        } else if (LKLevelData.ziraStage == 19) {
            setDead();
        } else if (LKLevelData.ziraStage == 23 && selfTalkTick == TALK_DELAY) {
            LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.zira.stage23"));
            LKLevelData.setZiraStage(24);
            selfTalkTick = 0;
        } else if (LKLevelData.ziraStage == 24 && selfTalkTick == SELF_TALK_DELAY) {
            LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.zira.stage24"));
            LKLevelData.setZiraStage(25);
            selfTalkTick = 0;
        } else if (LKLevelData.ziraStage == 25 && selfTalkTick > TALK_DELAY) {
            spawnTermiteQueen();
        } else if (LKLevelData.ziraStage == 26 && ridingEntity == null) {
            mountTermiteQueen();
        } else if (LKLevelData.ziraStage > 25 && rand.nextInt(3) == 0) {
            spawnParticles(rand.nextInt(3) == 0 ? "smoke" : "flame", 1, 1.0F, 0.0F);
        } else if (LKLevelData.ziraStage == 27 && getHealth() <= 120.0F) {
            triggerFinalOutlanders();
        } else if (LKLevelData.ziraStage == 28 && rand.nextInt(160) == 0) {
            reinforceOutlanders();
        }
    }

    private void spawnParticles(String type, int count, float widthScale, float yOffset) {
        for (int i = 0; i < count; i++) {
            double d = rand.nextGaussian() * 0.02D;
            double d1 = rand.nextGaussian() * 0.02D + (type.equals("flame") ? rand.nextFloat() * 0.5F : 0.0F);
            double d2 = rand.nextGaussian() * 0.02D;
            worldObj.spawnParticle(type, posX + (rand.nextFloat() * width * 2.0F - width) * widthScale, posY + yOffset + rand.nextFloat() * height, posZ + (rand.nextFloat() * width * 2.0F - width) * widthScale, d, d1, d2);
        }
    }

    private void despawnWithOutlanders() {
        List<?> nearbyEntities = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(64.0D, 64.0D, 64.0D));
        for (Object entity : nearbyEntities) {
            if (entity instanceof LKEntityOutlander) {
                LKEntityOutlander outlander = (LKEntityOutlander) entity;
                if (outlander.inMound) {
                    spawnParticles("flame", 24, 0.75F, 0.25F);
                    outlander.setDead();
                }
            }
        }
        setDead();
    }

    private void checkForPumbaaBox() {
        EntityPlayer player = (EntityPlayer) worldObj.playerEntities.get(0);
        if (player != null && getDistanceSqToEntity(player) < 256.0F && player.inventory.hasItem(Item.getItemFromBlock(mod_LionKing.pumbaaBox))) {
            LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.zira.stage17.pumbaa"));
            LKLevelData.setZiraStage(18);
            LKLevelData.setOutlandersHostile(1);
        }
    }

    private void spawnTermiteQueen() {
        LKEntityTermiteQueen queen = new LKEntityTermiteQueen(worldObj);
        queen.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
        worldObj.spawnEntityInWorld(queen);
        mountEntity(queen);
        queen.motionY = 1.0D;
        LKLevelData.setZiraStage(26);
    }

    private void mountTermiteQueen() {
        List<?> mounts = worldObj.getEntitiesWithinAABB(LKEntityTermiteQueen.class, boundingBox.expand(64.0D, 64.0D, 64.0D));
        if (!mounts.isEmpty()) {
            LKEntityTermiteQueen mount = (LKEntityTermiteQueen) mounts.get(0);
            if (mount != null && mount.riddenByEntity == null) {
                mountEntity(mount);
            }
        }
    }

    private void triggerFinalOutlanders() {
        LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.zira.stage27"));
        for (int i = 0; i < 4; i++) {
            int x = MathHelper.floor_double(posX) - 6 + rand.nextInt(13);
            int z = MathHelper.floor_double(posZ) - 6 + rand.nextInt(13);
            int y = worldObj.getHeightValue(x, z);

            LKEntityOutlander outlander = rand.nextBoolean() ? new LKEntityOutlander(worldObj) : new LKEntityOutlandess(worldObj);
            outlander.inMound = true;
            outlander.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
            worldObj.spawnEntityInWorld(outlander);
            worldObj.spawnEntityInWorld(new EntityLightningBolt(worldObj, x, y, z));
        }
        LKLevelData.setZiraStage(28);
    }

    private void reinforceOutlanders() {
        int outlanderCount = countNearbyOutlanders();
        if (outlanderCount < 6) {
            int x = MathHelper.floor_double(posX) - 6 + rand.nextInt(13);
            int z = MathHelper.floor_double(posZ) - 6 + rand.nextInt(13);
            int y = worldObj.getHeightValue(x, z);

            LKEntityOutlander outlander = rand.nextBoolean() ? new LKEntityOutlander(worldObj) : new LKEntityOutlandess(worldObj);
            outlander.inMound = true;
            outlander.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
            worldObj.spawnEntityInWorld(outlander);
            worldObj.createExplosion(outlander, x, y + 1, z, 0.0F, false);
        }
    }

    @Override
    public boolean interact(EntityPlayer player) {
        if (talkTick != TALK_DELAY || worldObj.isRemote) return false;

        switch (LKLevelData.ziraStage) {
            case 0:
                if (LKQuestBase.outlandsQuest.getQuestStage() == 1) {
                    LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.zira.stage0"));
                    LKLevelData.setZiraStage(1);
                    resetTalkTick();
                    return true;
                }
                break;
            case 1:
                LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.zira.stage1"));
                LKLevelData.setZiraStage(2);
                resetTalkTick();
                return true;
            case 2:
                LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.zira.stage2"));
                LKLevelData.setZiraStage(3);
                resetTalkTick();
                return true;
            case 3:
                LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.zira.stage3"));
                LKLevelData.setZiraStage(4);
                resetTalkTick();
                return true;
            case 4:
                LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.zira.stage4"));
                LKLevelData.setZiraStage(5);
                resetTalkTick();
                LKQuestBase.outlandsQuest.progress(2);
                return true;
            case 5:
                return handleMaterialCheck(player, mod_LionKing.silver, 2, mod_LionKing.kivulite, 5,
                    "chat.zira.stage5.success", "speech.ziraIngots", 6, 3);
            case 6:
                if (player.inventory.hasItem(mod_LionKing.outlandsHelm)) {
                    LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.zira.stage6.success"));
                    LKLevelData.setZiraStage(7);
                    resetTalkTick();
                    LKQuestBase.outlandsQuest.setDelayed(true);
                    LKQuestBase.outlandsQuest.progress(4);
                    return true;
                } else {
                    LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.zira.stage6.fail"));
                    resetTalkTick();
                    return true;
                }
            case 7:
                LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.zira.stage7"));
                LKLevelData.setZiraStage(8);
                resetTalkTick();
                return true;
            case 8:
                LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.zira.stage8"));
                LKLevelData.setZiraStage(9);
                resetTalkTick();
                return true;
            case 9:
                LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.zira.stage9"));
                LKLevelData.setZiraStage(10);
                resetTalkTick();
                LKQuestBase.outlandsQuest.setDelayed(false);
                return true;
            case 10:
                return handleMaterialCheck(player, mod_LionKing.outlandsFeather, 3, null, 0,
                    "chat.zira.stage10.success", "speech.ziraFeathers", 11, 5, true);
            case 14:
                LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.zira.stage14"));
                LKLevelData.setZiraStage(15);
                resetTalkTick();
                LKQuestBase.outlandsQuest.setDelayed(true);
                LKQuestBase.outlandsQuest.progress(6);
                return true;
            case 15:
                LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.zira.stage15"));
                LKLevelData.setZiraStage(16);
                resetTalkTick();
                return true;
            case 16:
                LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.zira.stage16"));
                LKLevelData.setZiraStage(17);
                resetTalkTick();
                LKQuestBase.outlandsQuest.setDelayed(false);
                return true;
            case 17:
                LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("speech.ziraConquest"));
                resetTalkTick();
                return true;
        }
        return false;
    }

    private void resetTalkTick() {
        talkTick = 0;
    }

    private boolean handleMaterialCheck(EntityPlayer player, Item item1, int required1, Item item2, int required2, String successKey, String failKey, int nextStage, int questProgress) {
        return handleMaterialCheck(player, item1, required1, item2, required2, successKey, failKey, nextStage, questProgress, false);
    }

    private boolean handleMaterialCheck(EntityPlayer player, Item item1, int required1, Item item2, int required2, String successKey, String failKey, int nextStage, int questProgress, boolean consumeItems) {
        int count1 = 0;
        int count2 = item2 == null ? required2 : 0;
        ItemStack[] inventory = player.inventory.mainInventory;

        for (ItemStack stack : inventory) {
            if (stack != null) {
                if (stack.getItem() == item1) count1 += stack.stackSize;
                if (item2 != null && stack.getItem() == item2) count2 += stack.stackSize;
            }
        }

        if (count1 >= required1 && count2 >= required2) {
            if (consumeItems) {
                for (int i = 0; i < required1; i++) {
                    player.inventory.consumeInventoryItem(item1);
                }
            }
            LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal(successKey));
            LKLevelData.setZiraStage(nextStage);
            resetTalkTick();
            selfTalkTick = 0;
            LKQuestBase.outlandsQuest.setDelayed(true);
            LKQuestBase.outlandsQuest.progress(questProgress);
            if (nextStage == 6) {
                clearPoolCover(worldObj);
            }
            return true;
        } else {
            LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal(failKey));
            resetTalkTick();
            return true;
        }
    }

    private void clearPoolCover(World world) {
        int moundX = LKLevelData.moundX;
        int moundY = LKLevelData.moundY;
        int moundZ = LKLevelData.moundZ;

        for (int x = moundX - 2; x <= moundX + 2; x++) {
            for (int z = moundZ - 2; z <= moundZ + 2; z++) {
                for (int y = moundY; y <= moundY + 2; y++) {
                    if (world.getBlock(x, y, z) == Blocks.water || world.getBlock(x, y, z) == Blocks.flowing_water) {
                        world.setBlockToAir(x, y, z);
                    }
                }
            }
        }
    }

    @Override
    protected Entity findPlayerToAttack() {
        if (!isHostile()) return null;
        EntityPlayer player = worldObj.getClosestVulnerablePlayerToEntity(this, 16.0D);
        return (player != null && canEntityBeSeen(player)) ? player : null;
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        if (source.getEntity() instanceof EntityPlayer && LKLevelData.ziraStage == 28) {
            EntityPlayer player = (EntityPlayer) source.getEntity();
            handleDeathEffects(player);
        }
    }

    private void handleDeathEffects(EntityPlayer player) {
        LKLevelData.setZiraStage(29);
        LKQuestBase.outlandsQuest.progress(10);
        player.addStat(LKAchievementList.killZira, 1);

        despawnNearbyEntities(LKEntityOutlander.class, 18.0F);
        despawnNearbyEntities(LKEntityTermite.class, 18.0F);

        if (!worldObj.isRemote) {
            worldObj.createExplosion(this, posX, posY, posZ, 0.0F, false);
            dropItems(mod_LionKing.outlanderFur, rand.nextInt(7) + 7);
            dropItems(mod_LionKing.outlanderMeat, rand.nextInt(7) + 7);
            dropItems(mod_LionKing.ziraRug, 1);

            for (int i = 0; i < 20; i++) {
                worldObj.spawnEntityInWorld(new EntityXPOrb(worldObj, posX, posY, posZ, rand.nextInt(30) + 36));
            }

            spawnLightningBolts(5, 12);
            setDead();
        }
    }

    private void despawnNearbyEntities(Class<?> entityClass, float range) {
        List<?> entities = worldObj.getEntitiesWithinAABB(entityClass, boundingBox.expand(range, range, range));
        for (Object entity : entities) {
            if (entity instanceof LKEntityOutlander && ((LKEntityOutlander) entity).inMound) {
                ((Entity) entity).setDead();
            } else if (entity instanceof LKEntityTermite) {
                ((Entity) entity).setDead();
            }
        }
    }

    private void dropItems(Item item, int count) {
        for (int i = 0; i < count; i++) {
            entityDropItem(new ItemStack(item, 1), 0.0F);
        }
    }

    private void spawnLightningBolts(int count, int range) {
        for (int i = 0; i < count; i++) {
            int x = MathHelper.floor_double(posX) - range + rand.nextInt(range * 2 + 1);
            int z = MathHelper.floor_double(posZ) - range + rand.nextInt(range * 2 + 1);
            int y = worldObj.getHeightValue(x, z);
            worldObj.spawnEntityInWorld(new EntityLightningBolt(worldObj, x, y, z));
        }
    }

    @Override
    protected void attackEntity(Entity entity, float distance) {
        if (attackTime <= 0 && distance < 2.4F && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY) {
            attackTime = 20;
            attackEntityAsMob(entity);
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), 5.0F);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (LKLevelData.ziraStage < 27) return false;
        if (source == DamageSource.outOfWorld || (source.getEntity() instanceof EntityPlayer)) {
            return super.attackEntityFrom(source, amount);
        }
        return false;
    }

    @Override
    protected void kill() {
        setDead();
    }

    @Override
    protected String getLivingSound() {
        return rand.nextBoolean() ? "lionking:lion" : null;
    }

    @Override
    protected String getHurtSound() {
        return "lionking:lionroar";
    }

    @Override
    protected String getDeathSound() {
        return "lionking:lionangry";
    }

    @Override
    public float getBlockPathWeight(int x, int y, int z) {
        if (LKLevelData.ziraStage < 14) {
            if (y > posY && posY > LKLevelData.moundY + 24 || y < posY && posY < LKLevelData.moundY + 16) {
                return -999999.0F;
            }
            if (worldObj.getBlock(x, y - 1, z) == mod_LionKing.outlandsPortalFrame && worldObj.getBlockMetadata(x, y - 1, z) == 1 && worldObj.isAirBlock(x, y, z)) {
                return 10.0F;
            }
        } else if (LKLevelData.ziraStage >= 14 && LKLevelData.ziraStage < 19) {
            return worldObj.getBlock(x, y - 1, z) == mod_LionKing.rafikiWood && worldObj.getBlockMetadata(x, y - 1, z) == 2 && worldObj.isAirBlock(x, y, z) ? 10.0F : -999999.0F;
        }
        return 0.0F;
    }

    @Override
    public ItemStack getPickedResult(MovingObjectPosition target) {
        return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this));
    }

    @Override
    public String getEntityName() {
        return StatCollector.translateToLocal("entity.lionking.zira.name");
    }
}