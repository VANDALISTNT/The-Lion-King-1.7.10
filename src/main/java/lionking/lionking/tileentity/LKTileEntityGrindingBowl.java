package lionking.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

import java.util.Random;

import lionking.common.LKGrindingRecipes; 

public class LKTileEntityGrindingBowl extends TileEntity implements ISidedInventory {
    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    private static final int GRIND_DURATION = 200;
    private ItemStack[] inventory = new ItemStack[2]; 
    private final Random rand = new Random();
    public int grindTime;
    public float stickRotation;
    private static final int[] INPUT_SLOTS = {INPUT_SLOT};
    private static final int[] OUTPUT_SLOTS = {OUTPUT_SLOT};

    public LKTileEntityGrindingBowl() {
        stickRotation = rand.nextInt(360);
    }

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if (inventory[slot] == null) return null;

        if (inventory[slot].stackSize <= amount) {
            ItemStack stack = inventory[slot];
            inventory[slot] = null;
            return stack;
        }

        ItemStack splitStack = inventory[slot].splitStack(amount);
        if (inventory[slot].stackSize == 0) {
            inventory[slot] = null;
        }
        return splitStack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack stack = inventory[slot];
        inventory[slot] = null;
        return stack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory[slot] = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }
    }

    @Override
    public String getInventoryName() { 
        return "Grinding Bowl";
    }

    @Override
    public boolean hasCustomInventoryName() { 
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    public int getGrindProgressScaled(int scale) {
        return (grindTime * scale) / GRIND_DURATION;
    }

    @Override
    public void updateEntity() {
        boolean inventoryChanged = false;

        if (!worldObj.isRemote) {
            if (canGrind()) {
                grindTime++;
                if (grindTime >= GRIND_DURATION) {
                    grindTime = 0;
                    grindItem();
                    inventoryChanged = true;
                }
            } else if (grindTime > 0) {
                grindTime = 0;
                inventoryChanged = true;
            }
        }

        if (worldObj.rand.nextBoolean()) {
            spawnSmokeParticle();
        }
        stickRotation = (stickRotation + 8.0F) % 360.0F;

        if (inventoryChanged) {
            markDirty(); 
        }
    }

    private void spawnSmokeParticle() {
        double offsetX = xCoord + 0.5F + (worldObj.rand.nextFloat() - 0.5F) * 0.5F;
        double offsetY = yCoord + 0.6F;
        double offsetZ = zCoord + 0.5F + (worldObj.rand.nextFloat() - 0.5F) * 0.5F;
        worldObj.spawnParticle("smoke", offsetX, offsetY, offsetZ, 0.0D, 0.0D, 0.0D);
    }

    private boolean canGrind() {
        if (inventory[INPUT_SLOT] == null) {
            return false;
        }

        ItemStack result = LKGrindingRecipes.getInstance().getGrindingResult(inventory[INPUT_SLOT]);
        if (result == null) {
            return false;
        }

        if (inventory[OUTPUT_SLOT] == null) {
            return true;
        }

        if (!inventory[OUTPUT_SLOT].isItemEqual(result)) {
            return false;
        }

        int newStackSize = inventory[OUTPUT_SLOT].stackSize + result.stackSize;
        return newStackSize <= getInventoryStackLimit() && newStackSize <= result.getMaxStackSize();
    }

    private void grindItem() {
        if (!canGrind()) {
            return;
        }

        ItemStack result = LKGrindingRecipes.getInstance().getGrindingResult(inventory[INPUT_SLOT]);
        if (inventory[OUTPUT_SLOT] == null) {
            inventory[OUTPUT_SLOT] = result.copy();
        } else if (inventory[OUTPUT_SLOT].isItemEqual(result)) {
            inventory[OUTPUT_SLOT].stackSize += result.stackSize;
        }

        if (inventory[INPUT_SLOT].getItem().hasContainerItem()) {
            inventory[INPUT_SLOT] = new ItemStack(inventory[INPUT_SLOT].getItem().getContainerItem());
        } else {
            inventory[INPUT_SLOT].stackSize--;
            if (inventory[INPUT_SLOT].stackSize <= 0) {
                inventory[INPUT_SLOT] = null;
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagList items = nbt.getTagList("Items", 10); 
        for (int i = 0; i < inventory.length; i++) {
            inventory[i] = null; 
        }

        for (int i = 0; i < items.tagCount(); i++) {
            NBTTagCompound itemTag = items.getCompoundTagAt(i); 
            int slot = itemTag.getByte("LKSlotGrindingBowl") & 0xFF;
            if (slot >= 0 && slot < inventory.length) {
                inventory[slot] = ItemStack.loadItemStackFromNBT(itemTag);
            }
        }
        grindTime = nbt.getShort("GrindTime");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setShort("GrindTime", (short) grindTime);

        NBTTagList items = new NBTTagList();
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null) {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setByte("LKSlotGrindingBowl", (byte) i);
                inventory[i].writeToNBT(itemTag);
                items.appendTag(itemTag);
            }
        }
        nbt.setTag("Items", items);
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && 
                player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory() {} 

    @Override
    public void closeInventory() {} 

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return slot == INPUT_SLOT && LKGrindingRecipes.getInstance().getGrindingResult(stack) != null;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return side == 0 ? OUTPUT_SLOTS : INPUT_SLOTS;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return slot == INPUT_SLOT && isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return slot == OUTPUT_SLOT;
    }
}
