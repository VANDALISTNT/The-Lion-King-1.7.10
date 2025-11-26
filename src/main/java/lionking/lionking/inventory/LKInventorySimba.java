package lionking.inventory;

import net.minecraft.inventory.IInventory; 
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import lionking.entity.LKEntitySimba;

public class LKInventorySimba implements IInventory {
    public ItemStack[] inventory; 
    public LKEntitySimba theSimba; 

    public LKInventorySimba(LKEntitySimba entity) {
        inventory = new ItemStack[9]; 
        theSimba = entity; 
    }

    private int getInventorySlotContainItem(int itemId) {
        for (int j = 0; j < inventory.length; j++) {
            if (inventory[j] != null && Item.getIdFromItem(inventory[j].getItem()) == itemId) { 
                return j; 
            }
        }
        return -1; 
    }

    private int storeItemStack(ItemStack itemstack) {
        int itemId = Item.getIdFromItem(itemstack.getItem());
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null && Item.getIdFromItem(inventory[i].getItem()) == itemId && inventory[i].isStackable() && 
                inventory[i].stackSize < inventory[i].getMaxStackSize() && inventory[i].stackSize < getInventoryStackLimit() && 
                (!inventory[i].getHasSubtypes() || inventory[i].getItemDamage() == itemstack.getItemDamage())) { 
                return i; 
            }
        }
        return -1; 
    }

    private int getFirstEmptyStack() {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) { 
                return i; 
            }
        }
        return -1; 
    }

    private int storePartialItemStack(ItemStack itemstack) {
        int itemId = Item.getIdFromItem(itemstack.getItem()); 
        int j = itemstack.stackSize; 
        int k = storeItemStack(itemstack); 
        if (k < 0) { 
            k = getFirstEmptyStack(); 
        }
        if (k < 0) { 
            return j; 
        }
        if (inventory[k] == null) { 
            inventory[k] = new ItemStack(itemstack.getItem(), 0, itemstack.getItemDamage()); 
        }
        int l = j; 
        if (l > inventory[k].getMaxStackSize() - inventory[k].stackSize) { 
            l = inventory[k].getMaxStackSize() - inventory[k].stackSize;
        }
        if (l > getInventoryStackLimit() - inventory[k].stackSize) { 
            l = getInventoryStackLimit() - inventory[k].stackSize;
        }
        if (l == 0) { 
            return j; 
        } else {
            j -= l; 
            inventory[k].stackSize += l; 
            inventory[k].animationsToGo = 5; 
            return j; 
        }
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        ItemStack[] aitemstack = inventory;
        if (aitemstack[i] != null) { 
            if (aitemstack[i].stackSize <= j) { 
                ItemStack itemstack = aitemstack[i]; 
                aitemstack[i] = null; 
                return itemstack; 
            }
            ItemStack itemstack1 = aitemstack[i].splitStack(j); 
            if (aitemstack[i].stackSize == 0) { 
                aitemstack[i] = null; 
            }
            return itemstack1; 
        } else {
            return null; 
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        ItemStack[] aitemstack = inventory;
        aitemstack[i] = itemstack; 
    }

    public NBTTagList writeToNBT(NBTTagList nbttaglist) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null) { 
                NBTTagCompound nbttagcompound = new NBTTagCompound(); 
                nbttagcompound.setByte("Slot", (byte) i); 
                inventory[i].writeToNBT(nbttagcompound); 
                nbttaglist.appendTag(nbttagcompound); 
            }
        }
        return nbttaglist; 
    }

    public void readFromNBT(NBTTagList nbttaglist) {
        inventory = new ItemStack[9]; 
        for (int i = 0; i < nbttaglist.tagCount(); i++) { 
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i); 
            int j = nbttagcompound.getByte("Slot") & 0xff; 
            ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound); 
            if (itemstack == null || itemstack.getItem() == null) { 
                continue;
            }
            if (j >= 0 && j < inventory.length) { 
                inventory[j] = itemstack; 
            }
        }
    }

    @Override
    public int getSizeInventory() {
        return inventory.length; 
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        ItemStack[] aitemstack = inventory;
        return aitemstack[i]; 
    }

    @Override
    public String getInventoryName() { 
        return "Inventory"; 
    }

    @Override
    public int getInventoryStackLimit() {
        return 64; 
    }

    @Override
    public void markDirty() { 
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        if (theSimba.getHealth() <= 0) { 
            return false;
        }
        return entityplayer.getDistanceSqToEntity(theSimba) <= 64D; 
    }

    public void dropAllItems() {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null) { 
                theSimba.entityDropItem(inventory[i], 0.0F); 
                inventory[i] = null; 
            }
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        if (this.inventory[i] != null) { 
            ItemStack stack = this.inventory[i]; 
            this.inventory[i] = null; 
            return stack; 
        } else {
            return null; 
        }
    }

    @Override
    public void openInventory() {} 

    @Override
    public void closeInventory() {} 

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        return true; 
    }

    @Override
    public boolean hasCustomInventoryName() { 
        return false; 
    }
}