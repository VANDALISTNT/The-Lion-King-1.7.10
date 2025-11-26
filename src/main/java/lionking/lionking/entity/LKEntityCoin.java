package lionking.entity;

import net.minecraftforge.common.DimensionManager;
import net.minecraft.world.World;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MathHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;

import lionking.common.LKLevelData;

public class LKEntityCoin extends EntityThrowable {
    public LKEntityCoin(World world) {
        super(world); 
    }

    public LKEntityCoin(World world, EntityLivingBase entityliving, byte b) {
        super(world, entityliving); 
        setCoinType(b); 
    }

    public LKEntityCoin(World world, double d, double d1, double d2) {
        super(world, d, d1, d2); 
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataWatcher.addObject(12, Byte.valueOf((byte) 0)); 
    }

    
    public void setCoinType(byte b) {
        dataWatcher.updateObject(12, Byte.valueOf(b)); 
    }

    
    public byte getCoinType() {
        return dataWatcher.getWatchableObjectByte(12); 
    }

    @Override
    protected void onImpact(MovingObjectPosition movingobjectposition) {
        if (!worldObj.isRemote) { 
            if (getThrower() instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer) getThrower(); 
                breakCoin(entityplayer); 
            }
            setDead(); 
        }
    }

    
    private void breakCoin(EntityPlayer entityplayer) {
        entityplayer.fallDistance = 0F; 
        double[] position = new double[3]; 
        float[] rotation = new float[2]; 

        if (getCoinType() == 0) { 
            position = new double[]{0, 103, 0}; 
            rotation = new float[]{worldObj.rand.nextFloat() * 360F, 0F}; 
        } else if (getCoinType() == 1) { 
            position = new double[]{LKLevelData.moundX + 0.5D, LKLevelData.moundY + 10D, LKLevelData.moundZ + 0.5D}; 
            rotation = new float[]{90F, 0F}; 
        }

        
        DimensionManager.getWorld(entityplayer.dimension).theChunkProviderServer.loadChunk(MathHelper.floor_double(position[0]) >> 4, MathHelper.floor_double(position[2]) >> 4);
        
        entityplayer.setPositionAndRotation(position[0], position[1], position[2], rotation[0], rotation[1]);
        worldObj.playSoundAtEntity(entityplayer, "random.glass", 4F, 1F); 
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setByte("Type", getCoinType()); 
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        setCoinType(nbt.getByte("Type")); 
    }
}
