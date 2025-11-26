package lionking.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class LKTileEntityMountedShooter extends TileEntity {
    private int shooterType; 
    public int dartID; 
    public int dartStackSize; 
    public int fireCounter; 

    @Override
    public void updateEntity() {
        if (fireCounter > 0) {
            fireCounter--; 
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setByte("ShooterType", (byte) shooterType);
        nbt.setInteger("DartID", dartID);
        nbt.setInteger("DartStackSize", dartStackSize);
        nbt.setInteger("FireCounter", fireCounter); 
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        shooterType = nbt.getByte("ShooterType");
        dartID = nbt.getInteger("DartID");
        dartStackSize = nbt.getInteger("DartStackSize");
        fireCounter = nbt.getInteger("FireCounter"); 
    }

    public void setShooterType(int type) {
        this.shooterType = type;
        if (!worldObj.isRemote) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord); 
        }
    }

    public int getShooterType() {
        return shooterType;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, nbt);
    }

    @Override
    public void onDataPacket(net.minecraft.network.NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g()); 
    }
}
