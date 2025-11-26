package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.DamageSource;
import net.minecraft.nbt.NBTTagList;
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
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.StatCollector;

import lionking.inventory.LKInventoryTimon;
import lionking.common.LKCharacter;
import lionking.common.LKIngame;
import lionking.mod_LionKing;

public class LKEntityTimon extends EntityCreature implements LKCharacter {
    private static final float WIDTH = 0.4F;
    private static final float HEIGHT = 0.9F;
    private static final double WANDER_SPEED = 1.0D;
    private static final float WATCH_RANGE = 8.0F;
    private static final double MAX_HEALTH = 100.0D;
    private static final double MOVEMENT_SPEED = 0.2D;
    private static final int TALK_COOLDOWN = 120;
    private static final int TALK_DELAY = 40;
    private static final int INV_REFRESH_RATE = 20;
    private static final int MIN_SPAWN_HEIGHT = 60;

    private int talkTick;
    public LKInventoryTimon inventory;
    private int invRefreshTick;

    public LKEntityTimon(World world) {
        super(world);
        setSize(WIDTH, HEIGHT);
        talkTick = TALK_COOLDOWN;
        getNavigator().setAvoidsWater(true);
        inventory = new LKInventoryTimon(this);
        initializeAITasks();
    }

    private void initializeAITasks() {
        tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(2, new EntityAIWander(this, WANDER_SPEED));
        tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, WATCH_RANGE));
        tasks.addTask(4, new EntityAILookIdle(this));
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
    protected void entityInit() {
        super.entityInit();
        for (int i = 13; i <= 22; i++) {
            dataWatcher.addObject(i, (byte) 0);
        }
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!worldObj.isRemote) {
            if (getHealth() < getMaxHealth()) setHealth(getMaxHealth());
            updateTalkCooldown();
            refreshInventory();
            handleDialogueSequence();
        }
    }

    private void updateTalkCooldown() {
        if (talkTick < TALK_COOLDOWN) talkTick++;
    }

    private void refreshInventory() {
        if (invRefreshTick++ < INV_REFRESH_RATE) return;
        invRefreshTick = 0;

        ItemStack[] defaults = {
            new ItemStack(mod_LionKing.tunnahDiggah),
            new ItemStack(mod_LionKing.pumbaaBomb),
            new ItemStack(mod_LionKing.crystal),
            new ItemStack(mod_LionKing.xpGrub),
            new ItemStack(mod_LionKing.amulet)
        };

        for (int i = 0; i < defaults.length; i++) {
            if (inventory.getStackInSlot(i) == null) {
                inventory.setInventorySlotContents(i, defaults[i]);
            }
        }
    }

    private void handleDialogueSequence() {
        if (!canTalk()) return;

        DialogueStep[] steps = {
            new DialogueStep(13, 14, "chat.timon.initial", "Timon"),
            new DialogueStep(14, 15, "chat.pumbaa.canEat", "Pumbaa"),
            new DialogueStep(15, 16, "chat.timon.noEat", "Timon"),
            new DialogueStep(16, 17, "chat.timon.reconsider", "Timon"),
            new DialogueStep(17, 18, "chat.pumbaa.requestFood", "Pumbaa"),
            new DialogueStep(18, 19, "chat.timon.dismissIdea", "Timon"),
            new DialogueStep(19, 20, "chat.timon.claimIdea", "Timon"),
            new DialogueStep(20, 21, "chat.pumbaa.favoriteFood", "Pumbaa"),
            new DialogueStep(21, -1, "chat.timon.offerTrade", "Timon")
        };

        for (DialogueStep step : steps) {
            if (checkAndTriggerDialogue(step)) return;
        }
    }

    private boolean checkAndTriggerDialogue(DialogueStep step) {
        if (isDialogueStepActive(step.previousStep) && !isDialogueStepActive(step.nextStep)) {
            LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal(step.messageKey));
            dataWatcher.updateObject(step.nextStep, (byte) 1);
            talkTick = 0;
            return true;
        }
        return false;
    }

    private boolean isDialogueStepActive(int step) {
        return step == -1 || dataWatcher.getWatchableObjectByte(step) == 1;
    }

    private boolean canTalk() {
        return talkTick == TALK_COOLDOWN;
    }

    @Override
    public boolean interact(EntityPlayer player) {
        if (player.capabilities.isCreativeMode) {
            openTimonGui(player);
            return true;
        }

        if (talkTick >= TALK_DELAY) {
            if (!hasSpoken()) {
                LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.timon.initial"));
                dataWatcher.updateObject(13, (byte) 1);
                talkTick = 0;
                return true;
            }

            if (hasTimonSpokenFood()) {
                if (player.inventory.hasItem(mod_LionKing.bug)) { 
                    openTimonGui(player);
                    return true;
                } else {
                    String messageKey = hasEaten() ? "speech.timon.moreBugs" : "speech.timon.bugs";
                    LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal(messageKey));
                    talkTick = 0;
                    return true;
                }
            }
        }
        return super.interact(player);
    }

    private void openTimonGui(EntityPlayer player) {
        player.openGui(mod_LionKing.instance, mod_LionKing.GUI_ID_TIMON, worldObj, getEntityId(), 0, 0);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setTag("Inventory", inventory.writeToNBT(new NBTTagList()));
        for (int i = 13; i <= 22; i++) {
            if (dataWatcher.getWatchableObjectByte(i) == 1) {
                compound.setBoolean(getDialogueTag(i), true);
            }
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        inventory.readFromNBT(compound.getTagList("Inventory", 10));
        for (int i = 13; i <= 22; i++) {
            dataWatcher.updateObject(i, (byte) (compound.getBoolean(getDialogueTag(i)) ? 1 : 0));
        }
    }

    private String getDialogueTag(int index) {
        String[] tags = {
            "HasSpoken", "HasPumbaaSpoken", "HasTimonSpokenAgain", "HasTimonSpokenYetAgain",
            "HasPumbaaSpokenAgain", "HasTimonDismissedIdea", "HasTimonClaimedIdea",
            "HasPumbaaSpokenFood", "HasTimonSpokenFood", "HasEaten"
        };
        return tags[index - 13];
    }

    private boolean hasSpoken() { return dataWatcher.getWatchableObjectByte(13) == 1; }
    private boolean hasPumbaaSpoken() { return dataWatcher.getWatchableObjectByte(14) == 1; }
    private boolean hasTimonSpokenAgain() { return dataWatcher.getWatchableObjectByte(15) == 1; }
    private boolean hasTimonSpokenYetAgain() { return dataWatcher.getWatchableObjectByte(16) == 1; }
    private boolean hasPumbaaSpokenAgain() { return dataWatcher.getWatchableObjectByte(17) == 1; }
    private boolean hasTimonDismissedIdea() { return dataWatcher.getWatchableObjectByte(18) == 1; }
    private boolean hasTimonClaimedIdea() { return dataWatcher.getWatchableObjectByte(19) == 1; }
    private boolean hasPumbaaSpokenFood() { return dataWatcher.getWatchableObjectByte(20) == 1; }
    private boolean hasTimonSpokenFood() { return dataWatcher.getWatchableObjectByte(21) == 1; }
    private boolean hasEaten() { return dataWatcher.getWatchableObjectByte(22) == 1; }

    public void setFollowable() { dataWatcher.updateObject(12, (byte) 0); }
    public void setEaten() { dataWatcher.updateObject(22, (byte) 1); }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void kill() {
        setDead();
    }

    @Override
    public float getBlockPathWeight(int x, int y, int z) {
        if (posY < MIN_SPAWN_HEIGHT && y < posY) return -999999F;
        return worldObj.getBlock(x, y - 1, z) == Blocks.grass ? 10.0F : worldObj.getLightBrightness(x, y, z) - 0.5F;
    }

    @Override
    public ItemStack getPickedResult(MovingObjectPosition target) {
        return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this));
    }

    public String getEntityName() { 
        return StatCollector.translateToLocal("entity.lionking.timon.name");
    }

    class DialogueStep {
        int previousStep;
        int nextStep;
        String messageKey;
        String speaker;

        DialogueStep(int previousStep, int nextStep, String messageKey, String speaker) {
            this.previousStep = previousStep;
            this.nextStep = nextStep;
            this.messageKey = messageKey;
            this.speaker = speaker;
        }
    }
}