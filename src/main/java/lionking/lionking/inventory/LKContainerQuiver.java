package lionking.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import lionking.inventory.LKSlotQuiver;

public class LKContainerQuiver extends Container {
    private final LKInventoryQuiver quiverInventory;

    public LKContainerQuiver(EntityPlayer player, LKInventoryQuiver quiver) {
        this.quiverInventory = quiver;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 2; col++) {
                addSlotToContainer(new LKSlotQuiver(quiverInventory, col + row * 2, 209 + col * 18, 32 + row * 18));
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlotToContainer(new Slot(player.inventory, col + row * 9 + 9, 8 + col * 18, 18 + row * 18));
            }
        }

        for (int col = 0; col < 9; col++) {
            addSlotToContainer(new Slot(player.inventory, col, 8 + col * 18, 76));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return quiverInventory.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        Slot slot = (Slot) inventorySlots.get(slotIndex);
        if (slot == null || !slot.getHasStack()) {
            return null;
        }

        ItemStack originalStack = slot.getStack();
        ItemStack resultStack = originalStack.copy();

        if (slotIndex < 6) {
            if (!mergeItemStack(originalStack, 6, inventorySlots.size(), true)) {
                return null;
            }
        }
        else {
            boolean movedToQuiver = false;
            for (int i = 0; i < 6; i++) {
                Slot quiverSlot = (Slot) inventorySlots.get(i);
                if (quiverSlot.isItemValid(originalStack) && mergeItemStack(originalStack, i, i + 1, false)) {
                    movedToQuiver = true;
                    break;
                }
            }

            if (!movedToQuiver) {
                if (slotIndex < 33) { 
                    if (!mergeItemStack(originalStack, 33, 42, false)) { 
                        return null;
                    }
                } else { 
                    if (!mergeItemStack(originalStack, 6, 33, false)) { 
                        return null;
                    }
                }
            }
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

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        quiverInventory.closeInventory(); 
    }
}