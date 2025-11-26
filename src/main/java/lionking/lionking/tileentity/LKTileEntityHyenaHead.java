package lionking.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class LKTileEntityHyenaHead extends TileEntity {
    private int hyenaType; 
    private int rotation; 

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setByte("HyenaType", (byte) hyenaType);
        nbt.setByte("Rotation", (byte) rotation);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        hyenaType = nbt.getByte("HyenaType");
        rotation = nbt.getByte("Rotation");
    }

    public void setHyenaType(int type) {
        this.hyenaType = type;
        if (!worldObj.isRemote) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord); 
        }
    }

    public int getHyenaType() {
        return hyenaType;
    }

    public void setRotation(int rot) {
        this.rotation = rot & 15; 
        if (!worldObj.isRemote) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord); 
        }
    }

    public int getRotation() {
        return rotation;
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
