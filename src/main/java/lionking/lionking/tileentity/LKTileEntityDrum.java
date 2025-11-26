package lionking.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class LKTileEntityDrum extends TileEntity implements IInventory {
    private static final int INVENTORY_SIZE = 8; 
    private static final int MAX_NOTE = 24; 
    public byte note; 
    private final ItemStack[] inventory = new ItemStack[INVENTORY_SIZE]; 

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        NBTTagList items = new NBTTagList();
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null) {
                NBTTagCompound itemData = new NBTTagCompound();
                itemData.setByte("Slot", (byte) i);
                inventory[i].writeToNBT(itemData);
                items.appendTag(itemData);
            }
        }
        nbt.setTag("Items", items);
        nbt.setByte("Note", note);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagList items = nbt.getTagList("Items", 10); 
        for (int i = 0; i < items.tagCount(); i++) {
            NBTTagCompound itemData = items.getCompoundTagAt(i); 
            int slot = itemData.getByte("Slot") & 0xFF; 
            if (slot >= 0 && slot < inventory.length) {
                inventory[slot] = ItemStack.loadItemStackFromNBT(itemData);
            }
        }
        note = nbt.getByte("Note");
        clampNote(); 
    }

    public void changePitch() {
        note = (byte) ((note + 1) % (MAX_NOTE + 1)); 
        markDirty(); 
        if (!worldObj.isRemote) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord); 
        }
    }

    private void clampNote() {
        if (note < 0) note = 0;
        if (note > MAX_NOTE) note = MAX_NOTE;
    }

    @Override
    public int getSizeInventory() {
        return INVENTORY_SIZE;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if (inventory[slot] == null) return null;

        if (inventory[slot].stackSize <= amount) {
            ItemStack stack = inventory[slot];
            inventory[slot] = null;
            markDirty(); 
            return stack;
        }

        ItemStack splitStack = inventory[slot].splitStack(amount);
        if (inventory[slot].stackSize == 0) {
            inventory[slot] = null;
        }
        markDirty(); 
        return splitStack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack stack = inventory[slot];
        inventory[slot] = null;
        return stack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory[slot] = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }
        markDirty(); 
    }

    @Override
    public String getInventoryName() { 
        return "Bongo Drum"; 
    }

    @Override
    public boolean hasCustomInventoryName() { 
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64; 
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && 
                player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory() {} 

    @Override
    public void closeInventory() {} 

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return true; 
    }
}