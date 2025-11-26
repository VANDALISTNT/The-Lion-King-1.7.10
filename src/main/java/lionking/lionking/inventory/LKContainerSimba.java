package lionking.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import lionking.entity.LKEntitySimba;

public class LKContainerSimba extends Container {
    private final LKInventorySimba simbaInventory; 

    public LKContainerSimba(EntityPlayer player, LKEntitySimba simba) {
        this.simbaInventory = simba.inventory;

        
        for (int col = 0; col < 9; col++) {
            addSlotToContainer(new Slot(simbaInventory, col, 8 + col * 18, 31));
        }

        
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
        return simbaInventory.isUseableByPlayer(player);
    }

    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        Slot slot = (Slot) inventorySlots.get(slotIndex);
        if (slot == null || !slot.getHasStack()) {
            return null;
        }

        ItemStack originalStack = slot.getStack();
        ItemStack resultStack = originalStack.copy();

        
        if (slotIndex < 9) {
            if (!mergeItemStack(originalStack, 9, 45, true)) {
                return null;
            }
        }
        
        else if (!mergeItemStack(originalStack, 0, 9, false)) {
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
