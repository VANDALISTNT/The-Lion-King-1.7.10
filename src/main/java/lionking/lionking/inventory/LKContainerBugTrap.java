package lionking.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import lionking.tileentity.LKTileEntityBugTrap;
import lionking.inventory.LKSlotBugBait;
import lionking.inventory.LKSlotBugTrap;

public class LKContainerBugTrap extends Container {
    private final LKTileEntityBugTrap bugTrap; 

    public LKContainerBugTrap(EntityPlayer player, LKTileEntityBugTrap trap) {
        this.bugTrap = trap;

        
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                addSlotToContainer(new LKSlotBugBait(trap, col + row * 2, 40 + row * 18, 28 + col * 18));
            }
        }

        
        addSlotToContainer(new LKSlotBugTrap(trap, 4, 109, 32));

        
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlotToContainer(new Slot(player.inventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        
        for (int col = 0; col < 9; col++) {
            addSlotToContainer(new Slot(player.inventory, col, 8 + col * 18, 142));
        }
    }

    
    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return bugTrap.isUseableByPlayer(player);
    }

    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        Slot slot = (Slot) inventorySlots.get(slotIndex);
        if (slot == null || !slot.getHasStack()) {
            return null;
        }

        ItemStack originalStack = slot.getStack();
        ItemStack resultStack = originalStack.copy();

        
        if (slotIndex == 4) {
            if (!mergeItemStack(originalStack, 5, 41, true)) { 
                return null;
            }
        }
        
        else if (slotIndex > 4) {
            if (((LKSlotBugBait) inventorySlots.get(0)).isItemValid(originalStack)) {
                if (!mergeItemStack(originalStack, 0, 4, false)) { 
                    return null;
                }
            } else if (slotIndex < 32) { 
                if (!mergeItemStack(originalStack, 32, 41, false)) {
                    return null;
                }
            } else if (!mergeItemStack(originalStack, 5, 32, false)) { 
                return null;
            }
        }
        
        else if (!mergeItemStack(originalStack, 5, 41, false)) {
            return null;
        }

        
        if (originalStack.stackSize == 0) {
            slot.putStack(null);
        } else {
            slot.onSlotChanged();
        }

        
        if (originalStack.stackSize == resultStack.stackSize) {
            return null; 
        }
        slot.onPickupFromSlot(player, originalStack);
        return resultStack;
    }
}
