package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.DamageSource;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.StatCollector;

import lionking.entity.LKEntityQuestAnimal;
import lionking.common.LKAchievementList;
import lionking.mod_LionKing;

public class LKEntitySkeletalHyena extends LKEntityHyena {
    private static final double MOVEMENT_SPEED = 1.6D;
    private static final int DATAWATCHER_HEADLESS = 21;
    private static final float HEADLESS_HEALTH = 15.0F;
    private static final int MAX_BONES_DROP = 3;
    private static final int MAX_SHARDS_DROP = 2;
    private static final int HEAD_DROP_CHANCE = 40;
    private static final int HYENA_HEAD_METADATA = 3;

    public LKEntitySkeletalHyena(World world) {
        super(world);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(MOVEMENT_SPEED);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataWatcher.addObject(DATAWATCHER_HEADLESS, (byte) 0);
    }

    public boolean isHeadless() {
        return dataWatcher.getWatchableObjectByte(DATAWATCHER_HEADLESS) == 1;
    }

    public void setHeadless(boolean headless) {
        dataWatcher.updateObject(DATAWATCHER_HEADLESS, (byte) (headless ? 1 : 0));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("Headless", isHeadless());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        setHeadless(nbt.getBoolean("Headless"));
    }

    public boolean onHyenaKilled(EntityPlayer player) {
        int looting = EnchantmentHelper.getLootingModifier(player);
        if (isHeadless()) {
            player.triggerAchievement(LKAchievementList.killHyena);
            dropItems(looting);
            return false;
        }

        int event = rand.nextInt(3);
        switch (event) {
            case 0:
                player.triggerAchievement(LKAchievementList.killHyena);
                dropItems(looting);
                if (rand.nextInt(HEAD_DROP_CHANCE) == 0) {
                    entityDropItem(new ItemStack(mod_LionKing.hyenaHeadItem, 1, HYENA_HEAD_METADATA), 0.0F);
                }
                return false;
            case 1:
                spawnHead(player, 0.01D, 0.2D, 0.01D);
                setHealth(HEADLESS_HEALTH);
                setHeadless(true);
                player.triggerAchievement(LKAchievementList.behead);
                return true;
            case 2:
                player.triggerAchievement(LKAchievementList.killHyena);
                dropItems(looting);
                setHeadless(true);
                spawnHead(player, 0.02D, 0.02D, 0.02D);
                player.triggerAchievement(LKAchievementList.behead);
                return false;
            default:
                return false;
        }
    }

    private void dropItems(int looting) {
        if (worldObj.isRemote) return;

        int bones = rand.nextInt(MAX_BONES_DROP) + rand.nextInt(1 + looting);
        for (int i = 0; i < bones; i++) {
            dropItem(mod_LionKing.hyenaBone, 1);
        }

        int shards = rand.nextInt(MAX_SHARDS_DROP) + rand.nextInt(1 + looting);
        for (int i = 0; i < shards; i++) {
            dropItem(mod_LionKing.hyenaBoneShard, 1);
        }
    }

    private void spawnHead(EntityPlayer player, double motionXFactor, double motionYFactor, double motionZFactor) {
        LKEntitySkeletalHyenaHead head = new LKEntitySkeletalHyenaHead(worldObj);
        head.setLocationAndAngles(posX, posY + 0.5D, posZ, rand.nextFloat() * 360.0F, rand.nextFloat() * 360.0F);
        head.motionX = rand.nextGaussian() * motionXFactor;
        head.motionY = rand.nextFloat() * motionYFactor;
        head.motionZ = rand.nextGaussian() * motionZFactor;

        if (!worldObj.isRemote) {
            worldObj.spawnEntityInWorld(head);
        }
    }

    public boolean canAttackZazus() {
        return false;
    }

    public boolean damagedBySunlight() {
        return false;
    }

    @Override
    protected String getHurtSound() {
        return "mob.skeleton.hurt";
    }

    @Override
    protected String getDeathSound() {
        return "mob.skeleton.death";
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    public String getEntityName() {
        return StatCollector.translateToLocal("entity.lionking.skeletalHyena.name");
    }
}