package lionking.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import lionking.item.LKItemInfo;

public class LKSlotItemInfo extends Slot {
    public LKSlotItemInfo(IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition) {
        super(inventory, slotIndex, xDisplayPosition, yDisplayPosition);
    }

    @Override
    public int getSlotStackLimit() {
        return 1; 
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        if (stack == null || stack.getItem() == null) {
            return false;
        }
        Item item = stack.getItem();
        return stack != null && LKItemInfo.getItemInfo(stack) != null; 
    }
}