package lionking.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import lionking.entity.LKEntityTimon;
import lionking.mod_LionKing; 
import lionking.common.LKAchievementList; 

public class LKSlotTimon extends Slot {
    private final LKEntityTimon timon;
    private final int cost;

    public LKSlotTimon(IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition, LKEntityTimon timon, int cost) {
        super(inventory, slotIndex, xDisplayPosition, yDisplayPosition);
        this.timon = timon;
        this.cost = cost;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false; 
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
        if (stack == null) return;

        for (int i = 0; i < cost; i++) {
            player.inventory.consumeInventoryItem(mod_LionKing.bug); 
        }

        timon.setEaten();

        player.triggerAchievement(LKAchievementList.hakunaMatata);

        super.onPickupFromSlot(player, stack);
    }

    public int getCost() {
        return cost;
    }
}