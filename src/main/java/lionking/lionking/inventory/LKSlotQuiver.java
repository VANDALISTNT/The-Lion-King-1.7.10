package lionking.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import lionking.mod_LionKing;

public class LKSlotQuiver extends Slot {
    public LKSlotQuiver(IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition) {
        super(inventory, slotIndex, xDisplayPosition, yDisplayPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        if (stack == null) return false;

        Item item = stack.getItem();
        return item == mod_LionKing.dartBlue || item == mod_LionKing.dartYellow ||
               item == mod_LionKing.dartRed || item == mod_LionKing.dartBlack ||
               item == mod_LionKing.dartPink; 
    }
}
