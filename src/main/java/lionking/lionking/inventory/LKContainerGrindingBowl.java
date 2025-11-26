package lionking.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import lionking.tileentity.LKTileEntityGrindingBowl;
import lionking.common.LKGrindingRecipes;

import java.util.List;

public class LKContainerGrindingBowl extends Container {
    private final LKTileEntityGrindingBowl grindingBowl; 
    private int lastGrindTime; 

    public LKContainerGrindingBowl(EntityPlayer player, LKTileEntityGrindingBowl bowl) {
        this.grindingBowl = bowl;
        this.lastGrindTime = 0;

        addSlotToContainer(new Slot(bowl, 0, 40, 35));
        addSlotToContainer(new LKSlotGrindingBowl(bowl, 1, 116, 35));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlotToContainer(new Slot(player.inventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (int col = 0; col < 9; col++) {
            addSlotToContainer(new Slot(player.inventory, col, 8 + col * 18, 142));
        }
    }

    public int getGrindProgressScaled(int scale) {
        return grindingBowl.getGrindProgressScaled(scale);
    }

    @Override
    public void addCraftingToCrafters(ICrafting crafter) {
        super.addCraftingToCrafters(crafter);
        crafter.sendProgressBarUpdate(this, 0, grindingBowl.grindTime);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        if (lastGrindTime != grindingBowl.grindTime) {
            for (ICrafting crafter : (List<ICrafting>) crafters) {
                crafter.sendProgressBarUpdate(this, 0, grindingBowl.grindTime);
            }
            lastGrindTime = grindingBowl.grindTime;
        }
    }

    @Override
    public void updateProgressBar(int id, int value) {
        if (id == 0) {
            grindingBowl.grindTime = value;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return grindingBowl.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        Slot slot = (Slot) inventorySlots.get(slotIndex);
        if (slot == null || !slot.getHasStack()) {
            return null;
        }

        ItemStack originalStack = slot.getStack();
        ItemStack resultStack = originalStack.copy();
  
        if (slotIndex == 1) {
            if (!mergeItemStack(originalStack, 2, 38, true)) {
                return null;
            }
        }
        else if (slotIndex != 0) {
            if (LKGrindingRecipes.getInstance().getGrindingResult(originalStack) != null) {
                if (!mergeItemStack(originalStack, 0, 1, false)) {
                    return null;
                }
            }
            else if (slotIndex < 29) {
                if (!mergeItemStack(originalStack, 29, 38, false)) {
                    return null;
                }
            }
            else if (!mergeItemStack(originalStack, 2, 29, false)) {
                return null;
            }
        }
        else if (!mergeItemStack(originalStack, 2, 38, false)) {
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