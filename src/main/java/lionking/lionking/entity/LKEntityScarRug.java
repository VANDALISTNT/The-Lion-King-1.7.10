package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MathHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.block.Block;
import net.minecraft.util.StatCollector;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.entity.Entity;

import lionking.mod_LionKing;
import lionking.common.LKCharacterSpeech;
import lionking.entity.LKEntities;
import lionking.entity.LKEntityQuestAnimal;

public class LKEntityScarRug extends Entity {
    private static final float WIDTH = 1.2F;
    private static final float HEIGHT = 0.2F;
    private static final double GRAVITY = 0.04D;
    private static final float GROUND_DRAG = 0.58800006F;
    private static final float AIR_DRAG = 0.98F;
    private static final double MOTION_Y_DRAG = 0.98D;
    private static final double GROUND_BOUNCE = -0.5D;
    private static final int TALK_COOLDOWN = 40;
    private static final int DATAWATCHER_TYPE = 16;

    private int talkTick = TALK_COOLDOWN;

    public LKEntityScarRug(World world) {
        super(world);
        setSize(WIDTH, HEIGHT);
    }

    public LKEntityScarRug(World world, int type) {
        this(world);
        setType(type);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (talkTick < TALK_COOLDOWN) {
            talkTick++;
        }

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        motionY -= GRAVITY;

        moveEntity(motionX, motionY, motionZ);

        float drag = onGround ? getGroundDrag() : AIR_DRAG;
        motionX *= drag;
        motionY *= MOTION_Y_DRAG;
        motionZ *= drag;

        if (onGround) {
            motionY *= GROUND_BOUNCE;
        }
    }

    private float getGroundDrag() {
        Block block = worldObj.getBlock(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1, MathHelper.floor_double(posZ));
        if (block != null && !block.isAir(worldObj, MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1, MathHelper.floor_double(posZ))) {
            return block.slipperiness * AIR_DRAG;
        }
        return GROUND_DRAG;
    }

    @Override
    protected void entityInit() {
        dataWatcher.addObject(DATAWATCHER_TYPE, 0);
    }

    public void setType(int type) {
        dataWatcher.updateObject(DATAWATCHER_TYPE, type);
    }

    public int getType() {
        return dataWatcher.getWatchableObjectInt(DATAWATCHER_TYPE);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setInteger("Type", getType());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        setType(nbt.getInteger("Type"));
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return boundingBox;
    }

    public void dropAsItem() {
        worldObj.playSoundAtEntity(this, "lionking:lionangry", 1.0F, pitchVariation());
        if (!worldObj.isRemote) {
            entityDropItem(getRugItem(), 0.0F);
        }
        setDead();
    }

    public boolean interact(EntityPlayer player) {
        if (talkTick == TALK_COOLDOWN) {
            worldObj.playSoundAtEntity(this, "lionking:lionroar", 1.0F, pitchVariation());
            if (!worldObj.isRemote) {
                player.addChatMessage(new ChatComponentTranslation(LKCharacterSpeech.giveSpeech(getType() == 0 ? LKCharacterSpeech.RUG_SCAR : LKCharacterSpeech.RUG_ZIRA)));
            }
            talkTick = 0;
            return true;
        }
        return false;
    }

    private float pitchVariation() {
        return (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F + 1.0F;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public ItemStack getPickedResult(MovingObjectPosition target) {
        return getRugItem();
    }

    private ItemStack getRugItem() {
        return new ItemStack(getType() == 0 ? mod_LionKing.scarRug : mod_LionKing.ziraRug, 1);
    }


    public String getEntityName() {
        return StatCollector.translateToLocal("entity.lionking.scarRug.name");
    }
}
