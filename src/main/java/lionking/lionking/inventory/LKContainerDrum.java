package lionking.inventory;

import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import lionking.mod_LionKing;
import lionking.tileentity.LKTileEntityDrum;
import lionking.common.LKAchievementList;
import lionking.item.LKItemNote;

import java.util.List;
import java.util.Random;

public class LKContainerDrum extends Container {
    private final IInventory theDrum;
    private final IInventory theDrumEnchant;
    private final World worldObj;
    private final EntityPlayer thePlayer;
    private final Random rand = new Random();
    public final int[] enchantLevels = new int[3];

    public LKContainerDrum(EntityPlayer player, World world, LKTileEntityDrum drum) {
        this.theDrum = new LKInventoryDrum(this, false, drum);
        this.theDrumEnchant = new LKInventoryDrum(this, "Bongo Drum", false, 1);
        this.worldObj = world;
        this.thePlayer = player;

        addSlotToContainer(new LKSlotDrum(theDrumEnchant, 0, 43, 43));

        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 4; col++) {
                addSlotToContainer(new LKSlotNote(theDrum, col + row * 4, row == 0 ? 8 : 152, col * 20 + 5));
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlotToContainer(new Slot(player.inventory, col + row * 9 + 9, 8 + col * 18, 85 + row * 18));
            }
        }

        for (int col = 0; col < 9; col++) {
            addSlotToContainer(new Slot(player.inventory, col, 8 + col * 18, 143));
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting crafter) {
        super.addCraftingToCrafters(crafter);
        for (int i = 0; i < 3; i++) {
            crafter.sendProgressBarUpdate(this, i, enchantLevels[i]);
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (Object obj : crafters) {
            ICrafting crafter = (ICrafting) obj;
            for (int i = 0; i < 3; i++) {
                crafter.sendProgressBarUpdate(this, i, enchantLevels[i]);
            }
        }
    }

    @Override
    public void updateProgressBar(int id, int value) {
        if (id >= 0 && id <= 2) {
            enchantLevels[id] = value;
        } else {
            super.updateProgressBar(id, value);
        }
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventory) {
        if (inventory == theDrumEnchant || inventory == theDrum) {
            ItemStack itemStack = theDrumEnchant.getStackInSlot(0);
            if (itemStack != null && itemStack.isItemEnchantable()) {
                if (!worldObj.isRemote) {
                    int noteValues = calculateNoteValues();
                    updateEnchantLevels(itemStack, noteValues);
                    detectAndSendChanges();
                }
            } else {
                resetEnchantLevels();
            }
        }
    }

    @Override
    public boolean enchantItem(EntityPlayer player, int button) {
        ItemStack itemStack = theDrumEnchant.getStackInSlot(0);
        if (button >= 0 && button < 3 && enchantLevels[button] > 0 && itemStack != null &&
            (player.experienceLevel >= enchantLevels[button] || player.capabilities.isCreativeMode)) {
            if (!worldObj.isRemote) {
                List<EnchantmentData> enchantments = EnchantmentHelper.buildEnchantmentList(rand, itemStack, enchantLevels[button]);
                if (enchantments != null) {
                    player.addExperienceLevel(-enchantLevels[button]);
                    applyEnchantments(itemStack, enchantments);
                    onCraftMatrixChanged(theDrumEnchant);
                    player.addStat(LKAchievementList.drumEnchant, 1);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return theDrum.isUseableByPlayer(player);
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        if (!worldObj.isRemote) {
            ItemStack itemStack = theDrumEnchant.getStackInSlotOnClosing(0);
            if (itemStack != null) {
                player.dropPlayerItemWithRandomChoice(itemStack, false); 
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

        if (slotIndex < 9) {
            if (!mergeItemStack(originalStack, 9, 45, true)) {
                return null;
            }
        }
        else if (slotIndex < 36) {
            if (!mergeItemStack(originalStack, 36, 45, false)) {
                return null;
            }
        }
        else if (!mergeItemStack(originalStack, 9, 36, false)) {
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

    private int calculateNoteValues() {
        int noteValues = 0;
        for (int i = 0; i < 8; i++) {
            ItemStack note = theDrum.getStackInSlot(i);
            if (note != null && note.getItem() == mod_LionKing.note) {
                noteValues += note.stackSize * LKItemNote.getNoteValue(note.getItemDamage());
            }
        }
        return MathHelper.floor_float(noteValues / 7F);
    }

    private void updateEnchantLevels(ItemStack itemStack, int noteValues) {
        for (int i = 0; i < 3; i++) {
            int level = EnchantmentHelper.calcItemStackEnchantability(rand, i, noteValues, itemStack);
            enchantLevels[i] = level;
            if (level >= 30) {
                thePlayer.addStat(LKAchievementList.powerDrum, 1);
            }
        }
    }

    private void resetEnchantLevels() {
        for (int i = 0; i < 3; i++) {
            enchantLevels[i] = 0;
        }
    }

    private void applyEnchantments(ItemStack itemStack, List<EnchantmentData> enchantments) {
        for (EnchantmentData enchantment : enchantments) {
            itemStack.addEnchantment(enchantment.enchantmentobj, enchantment.enchantmentLevel);
        }
    }
}