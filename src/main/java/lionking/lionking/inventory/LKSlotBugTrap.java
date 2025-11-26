package lionking.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import lionking.mod_LionKing;
import lionking.common.LKAchievementList;

public class LKSlotBugTrap extends Slot {
    public LKSlotBugTrap(IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition) {
        super(inventory, slotIndex, xDisplayPosition, yDisplayPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false; 
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
        if (stack != null && stack.getItem() == mod_LionKing.bug) {
            player.triggerAchievement(LKAchievementList.bugTrap);
        }
        super.onPickupFromSlot(player, stack); 
    }
}