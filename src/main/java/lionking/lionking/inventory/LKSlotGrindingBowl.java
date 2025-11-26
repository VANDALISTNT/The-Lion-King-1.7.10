package lionking.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class LKSlotGrindingBowl extends Slot {
    public LKSlotGrindingBowl(IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition) {
        super(inventory, slotIndex, xDisplayPosition, yDisplayPosition);
    }

    // Проверка допустимости предмета для слота
    @Override
    public boolean isItemValid(ItemStack stack) {
        return false; // Нельзя помещать предметы в этот слот
    }
}