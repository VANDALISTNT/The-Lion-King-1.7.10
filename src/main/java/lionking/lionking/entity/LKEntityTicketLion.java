package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.StatCollector;
import net.minecraft.init.Items;
import net.minecraft.util.ChatComponentTranslation;

import lionking.mod_LionKing;
import lionking.common.LKIngame;
import lionking.entity.LKEntities;
import lionking.entity.LKEntityQuestAnimal;

public class LKEntityTicketLion extends EntityCreature implements IAnimals {
    private static final float WIDTH = 1.3F;
    private static final float HEIGHT = 1.6F;
    private static final double WANDER_SPEED = 1.0D;
    private static final float WATCH_RANGE = 10.0F;
    private static final double MAX_HEALTH = 20.0D;
    private static final double MOVEMENT_SPEED = 0.2D;
    private static final int TALK_COOLDOWN = 40;
    private static final int CHRISTMAS_TICKET_DAMAGE = 1;
    private static final int DEATH_PARTICLE_COUNT = 12;
    private static final int TRADE_PARTICLE_COUNT = 7;
    private static final double PARTICLE_VELOCITY = 0.02D;
    private static final float PARTICLE_SCALE = 0.75F;

    private int talkTick;

    public LKEntityTicketLion(World world) {
        super(world);
        setSize(WIDTH, HEIGHT);
        talkTick = TALK_COOLDOWN;
        getNavigator().setAvoidsWater(true);
        initializeAITasks();
    }

    private void initializeAITasks() {
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIWander(this, WANDER_SPEED));
        tasks.addTask(2, new EntityAIWatchClosest(this, EntityPlayer.class, WATCH_RANGE));
        tasks.addTask(3, new EntityAILookIdle(this));
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
        if (talkTick < TALK_COOLDOWN) talkTick++;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public boolean interact(EntityPlayer player) {
        if (talkTick != TALK_COOLDOWN) return false;

        if (LKIngame.isChristmas()) {
            return handleChristmasInteraction(player);
        } else {
            return handleTradeInteraction(player);
        }
    }

    private boolean handleChristmasInteraction(EntityPlayer player) {
        if (!player.inventory.hasItem(mod_LionKing.ticket)) {
            ItemStack heldItem = player.inventory.getCurrentItem();
            ItemStack christmasTicket = new ItemStack(mod_LionKing.ticket, 1, CHRISTMAS_TICKET_DAMAGE);

            if (heldItem == null) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, christmasTicket);
            } else {
                player.inventory.addItemStackToInventory(christmasTicket);
            }

            if (!worldObj.isRemote) {
                LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.ticketLion.christmas"));
            }
            talkTick = 0;
            return true;
        }
        return false;
    }

    private boolean handleTradeInteraction(EntityPlayer player) {
        ItemStack heldItem = player.inventory.getCurrentItem();

        if (heldItem != null && heldItem.getItem() == Items.gold_ingot) {
            if (heldItem.stackSize == 1) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
            } else {
                heldItem.stackSize--;
            }
            player.inventory.addItemStackToInventory(new ItemStack(mod_LionKing.ticket));
            if (!worldObj.isRemote) {
                LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.ticketLion.tradeSuccess"));
                addEffects(TRADE_PARTICLE_COUNT);
            }
        } else if (!worldObj.isRemote) {
            LKIngame.sendMessageToAllPlayers(StatCollector.translateToLocal("chat.ticketLion.tradeRequest"));
        }
        talkTick = 0;
        return true;
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        if (!worldObj.isRemote) {
            addEffects(DEATH_PARTICLE_COUNT);
        }
    }

    private void addEffects(int count) {
        for (int i = 0; i < count; i++) {
            double dx = rand.nextGaussian() * PARTICLE_VELOCITY;
            double dy = rand.nextGaussian() * PARTICLE_VELOCITY;
            double dz = rand.nextGaussian() * PARTICLE_VELOCITY;
            double x = posX + (rand.nextFloat() * width * 2.0F - width) * PARTICLE_SCALE;
            double y = posY + 0.25F + rand.nextFloat() * height;
            double z = posZ + (rand.nextFloat() * width * 2.0F - width) * PARTICLE_SCALE;
            LKIngame.spawnCustomFX(worldObj, 48, 32, false, x, y, z, dx, dy, dz);
        }
    }

    @Override
    protected String getLivingSound() {
        return "lionking:lion";
    }

    @Override
    protected String getHurtSound() {
        return "lionking:lionroar";
    }

    @Override
    protected String getDeathSound() {
        return "lionking:liondeath";
    }

    @Override
    public float getBlockPathWeight(int x, int y, int z) {
        if (worldObj.canBlockSeeTheSky(x, y, z)) {
            return -999999.0F;
        }
        return super.getBlockPathWeight(x, y, z);
    }

    @Override
    public ItemStack getPickedResult(MovingObjectPosition target) {
        return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this));
    }

    
    public String getEntityName() {
        return StatCollector.translateToLocal("entity.lionking.ticketLion.name");
    }
}
