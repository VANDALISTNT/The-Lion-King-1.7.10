package lionking.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import lionking.entity.LKEntityTimon;
import lionking.mod_LionKing;

public class LKContainerTimon extends Container {
    private final LKInventoryTimon timonInventory;

    public LKContainerTimon(EntityPlayer player, LKEntityTimon timon) {
        this.timonInventory = timon.inventory;

        for (int i = 0; i < 5; i++) {
            addSlotToContainer(new LKSlotTimon(timonInventory, i, 15 + i * 33, 32, timon, LKInventoryTimon.costs[i]));
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
        return timonInventory.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        Slot slot = (Slot) inventorySlots.get(slotIndex);
        if (slot == null || !slot.getHasStack()) {
            return null;
        }

        ItemStack originalStack = slot.getStack();
        ItemStack resultStack = originalStack.copy();

        if (slotIndex < 5) {
            if (!mergeItemStack(originalStack, 5, 41, true)) {
                return null;
            }
        } else if (slotIndex < 32) {
            if (!mergeItemStack(originalStack, 32, 41, false)) {
                return null;
            }
        } else if (!mergeItemStack(originalStack, 5, 32, false)) {
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

    @Override
    public ItemStack slotClick(int slotIndex, int mouseButton, int modifier, EntityPlayer player) {
        if (slotIndex >= 0) {
            Slot slot = (Slot) inventorySlots.get(slotIndex);
            if (slot instanceof LKSlotTimon) {
                int cost = ((LKSlotTimon) slot).getCost(); 
                if (!hasTradableStack(player, cost)) {
                    return null;
                }
            }
        }
        return super.slotClick(slotIndex, mouseButton, modifier, player);
    }

    private boolean hasTradableStack(EntityPlayer player, int cost) {
        for (ItemStack stack : player.inventory.mainInventory) {
            if (stack != null && stack.getItem() == mod_LionKing.bug && stack.stackSize >= cost) {
                return true;
            }
        }
        return false;
    }
}