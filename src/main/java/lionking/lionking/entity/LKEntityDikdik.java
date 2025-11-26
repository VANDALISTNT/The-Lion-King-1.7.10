package lionking.entity;

import net.minecraft.world.World;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MathHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks; 

import lionking.mod_LionKing; 

public class LKEntityDikdik extends EntityAmbientCreature {
    public LKEntityDikdik(World world) {
        super(world); 
        setSize(0.6F, 1F); 
        getNavigator().setAvoidsWater(true); 
        tasks.addTask(0, new EntityAISwimming(this)); 
        tasks.addTask(1, new LKEntityAIAmbientAvoid(this, LKEntityHyena.class, 8.0F, 1D, 1.5D)); 
        tasks.addTask(2, new LKEntityAIAmbientPanic(this, 1.5D)); 
        tasks.addTask(3, new LKEntityAIAmbientWander(this, 1.2D)); 
        tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 4.0F)); 
        tasks.addTask(5, new EntityAILookIdle(this)); 
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8D); 
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D); 
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataWatcher.addObject(20, Byte.valueOf((byte) getRNG().nextInt(3))); 
    }

    @Override
    public boolean isAIEnabled() {
        return true; 
    }

    public byte getDikdikType() {
        return dataWatcher.getWatchableObjectByte(20); 
    }

    public void setDikdikType(byte b) {
        dataWatcher.updateObject(20, Byte.valueOf(b)); 
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setByte("DikdikType", getDikdikType()); 
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        setDikdikType(nbt.getByte("DikdikType")); 
    }

    @Override
    public boolean getCanSpawnHere() {
        if (worldObj.provider.dimensionId != mod_LionKing.idUpendi && getRNG().nextInt(3) > 0) {
            return false; 
        }

        int i = MathHelper.floor_double(posX); 
        int j = MathHelper.floor_double(boundingBox.minY); 
        int k = MathHelper.floor_double(posZ); 
        return (worldObj.getBlock(i, j - 1, k) == Blocks.grass || worldObj.getBlock(i, j - 1, k) == Blocks.sand) 
            && worldObj.getFullBlockLightValue(i, j, k) > 8 && super.getCanSpawnHere(); 
    }

    @Override
    public ItemStack getPickedResult(MovingObjectPosition target) {
        return new ItemStack(mod_LionKing.spawnEgg, 1, LKEntities.getEntityID(this)); 
    }
}