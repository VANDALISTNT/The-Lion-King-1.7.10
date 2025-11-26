package lionking.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class LKContainerItemInfo extends Container {
    private final IInventory itemInfoInventory = new InventoryBasic("LKItemInfo", false, 1);

    public LKContainerItemInfo(EntityPlayer player) {
        addSlotToContainer(new LKSlotItemInfo(itemInfoInventory, 0, 182, 104));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlotToContainer(new Slot(player.inventory, col + row * 9 + 9, 182 + col * 18, 139 + row * 18));
            }
        }

        for (int col = 0; col < 9; col++) {
            addSlotToContainer(new Slot(player.inventory, col, 182 + col * 18, 197));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);

        if (!player.worldObj.isRemote) {
            Slot infoSlot = getSlot(0);
            ItemStack stack = infoSlot.getStack();

            if (stack != null) {
                if (!player.inventory.addItemStackToInventory(stack)) {
                    player.entityDropItem(stack, 0.0F);
                }
                infoSlot.putStack(null);
            }
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        Slot slot = (Slot) inventorySlots.get(slotIndex);
        if (slot == null || !slot.getHasStack()) {
            return null;
        }

        ItemStack originalStack = slot.getStack();
        ItemStack resultStack = originalStack.copy();

        if (slotIndex == 0) {
            if (!mergeItemStack(originalStack, 1, 37, true)) {
                return null;
            }
        } else if (slotIndex < 28) {
            if (!mergeItemStack(originalStack, 28, 37, false)) {
                return null;
            }
        } else if (!mergeItemStack(originalStack, 1, 28, false)) {
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