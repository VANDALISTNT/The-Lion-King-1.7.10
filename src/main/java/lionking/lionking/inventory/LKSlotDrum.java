package lionking.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class LKSlotDrum extends Slot {
    public LKSlotDrum(IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition) {
        super(inventory, slotIndex, xDisplayPosition, yDisplayPosition);
    }

    // Ограничение максимального размера стака
    @Override
    public int getSlotStackLimit() {
        return 1; // Максимум 1 предмет в слоте
    }
}