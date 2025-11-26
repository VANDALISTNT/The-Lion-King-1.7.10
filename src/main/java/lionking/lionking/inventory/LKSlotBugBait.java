package lionking.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;

public class LKSlotBugBait extends Slot {
    public LKSlotBugBait(IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition) {
        super(inventory, slotIndex, xDisplayPosition, yDisplayPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        if (stack == null) return false;

        Item item = stack.getItem();
        if (!(item instanceof ItemFood)) return false;

        ItemFood food = (ItemFood) item;
        return food.func_150905_g(stack) > 0 && !food.isWolfsFavoriteMeat() &&
               food != Items.fish && food != Items.cooked_fished;
    }
}