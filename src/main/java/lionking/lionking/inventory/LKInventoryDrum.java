package lionking.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.entity.player.EntityPlayer;

import lionking.tileentity.LKTileEntityDrum;

public class LKInventoryDrum extends InventoryBasic {
    private LKContainerDrum container;
    private LKTileEntityDrum drumInv;

    public LKInventoryDrum(LKContainerDrum drum, String s, boolean flag, int i) {
        super(s, flag, i);
        container = drum;
    }

    public LKInventoryDrum(LKContainerDrum drum, boolean flag, LKTileEntityDrum inv) {
        super(inv.getInventoryName(), flag, inv.getSizeInventory());
        container = drum;
        drumInv = inv;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        if (drumInv != null) {
            return drumInv.getStackInSlot(i);
        }
        return super.getStackInSlot(i);
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (drumInv != null) {
            ItemStack stack = drumInv.getStackInSlot(i);
            if (stack != null) {
                if (stack.stackSize <= j) {
                    ItemStack itemstack = stack;
                    drumInv.setInventorySlotContents(i, null);
                    markDirty();
                    return itemstack;
                }
                ItemStack itemstack1 = stack.splitStack(j);
                if (stack.stackSize == 0) {
                    drumInv.setInventorySlotContents(i, null);
                }
                markDirty();
                return itemstack1;
            } else {
                return null;
            }
        }
        return super.decrStackSize(i, j);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        if (drumInv != null) {
            return drumInv.getStackInSlotOnClosing(i);
        }
        return super.getStackInSlotOnClosing(i);
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        if (drumInv != null) {
            drumInv.setInventorySlotContents(i, itemstack);
            if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
                itemstack.stackSize = getInventoryStackLimit();
            }
            markDirty();
        } else {
            super.setInventorySlotContents(i, itemstack);
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        if (drumInv != null) {
            return drumInv.isUseableByPlayer(entityplayer);
        }
        return super.isUseableByPlayer(entityplayer);
    }

    @Override
    public int getInventoryStackLimit() {
        return drumInv != null ? drumInv.getInventoryStackLimit() : 1;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (drumInv != null) {
            drumInv.markDirty();
        }
        container.onCraftMatrixChanged(this);
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        return true;
    }

    public boolean isInvNameLocalized() {
        return false;
    }
}