package lionking.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;

import java.util.List;
import java.util.Random;

import lionking.entity.LKEntityBug;
import lionking.mod_LionKing;
import lionking.biome.LKBiomeGenMountains;
import lionking.biome.LKBiomeGenAridSavannah;
import lionking.biome.LKBiomeGenDesert;
import lionking.biome.LKBiomeGenRainforest;
import lionking.biome.LKBiomeGenWoodedSavannah;
import lionking.biome.LKBiomeGenBananaForest;

public class LKTileEntityBugTrap extends TileEntity implements ISidedInventory {
    private static final int INPUT_SLOT_COUNT = 4; 
    private static final int OUTPUT_SLOT = 4; 
    private static final int[] INPUT_SLOTS = {0, 1, 2, 3}; 
    private static final int[] OUTPUT_SLOTS = {OUTPUT_SLOT}; 
    private static final int CLOSURE_DURATION = 50; 
    private ItemStack[] inventory = new ItemStack[INPUT_SLOT_COUNT + 1]; 
    private final Random rand = new Random();
    private int closureTime = -1; 

    public LKTileEntityBugTrap() {}

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            trySpawnBug(); 
            captureBugs(); 
        }

        if (closureTime >= 0 && closureTime < CLOSURE_DURATION) {
            closureTime++;
        } else if (closureTime >= CLOSURE_DURATION) {
            closureTime = -1;
        }

        updateBlockMetadata();
    }

    private void trySpawnBug() {
        if (rand.nextInt(4000) != 0) return;

        float probability = calculateBugProbability();
        if (rand.nextFloat() >= probability) return;

        int attempts = 0;
        while (attempts < 64) {
            double x = xCoord + rand.nextInt(8) - rand.nextInt(8);
            double y = yCoord + rand.nextInt(2) - rand.nextInt(2);
            double z = zCoord + rand.nextInt(8) - rand.nextInt(8);
            int floorX = MathHelper.floor_double(x);
            int floorY = MathHelper.floor_double(y);
            int floorZ = MathHelper.floor_double(z);

            if (worldObj.isAirBlock(floorX, floorY, floorZ) &&
                    (worldObj.getBlock(floorX, floorY - 1, floorZ) == net.minecraft.init.Blocks.grass ||
                            worldObj.getBlock(floorX, floorY - 1, floorZ) == net.minecraft.init.Blocks.dirt)) {
                LKEntityBug bug = new LKEntityBug(worldObj);
                bug.setLocationAndAngles(x, y, z, rand.nextFloat(), rand.nextFloat());
                worldObj.spawnEntityInWorld(bug);
                break;
            }
            attempts++;
        }
    }

    private void captureBugs() {
        AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1)
                .expand(16.0D, 8.0D, 16.0D);
        List<LKEntityBug> bugs = worldObj.getEntitiesWithinAABB(LKEntityBug.class, bounds);

        for (LKEntityBug bug : bugs) {
            if (bug.targetTrap != this) continue;

            double distanceSq = bug.getDistanceSq(xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F);
            if (distanceSq <= 4.0F) {
                handleBugCapture(bug);
            } else {
                bug.trapTick = -1; 
            }
        }
    }

    private void handleBugCapture(LKEntityBug bug) {
        if (bug.trapTick == -1 && getAverageBaitSaturation() > 0.0F) {
            consumeBait(bug);
        }

        if (bug.trapTick >= 34) {
            attractBugToTrap(bug);
        }

        if (bug.trapTick == 36) {
            closureTime = 0; 
        }

        if (bug.trapTick >= 40) {
            captureBugResult(bug);
            bug.setDead();
        }
    }

    private void consumeBait(LKEntityBug bug) {
        int slot = rand.nextInt(INPUT_SLOT_COUNT);
        ItemStack stack = getStackInSlot(slot);
        if (stack == null || !(stack.getItem() instanceof ItemFood)) return;

        if (stack.stackSize > 1) {
            stack.stackSize--;
        } else {
            Item item = stack.getItem();
            if (item.hasContainerItem()) {
                setInventorySlotContents(slot, item.getContainerItem(stack));
            } else if (stack.getItem() == net.minecraft.init.Items.mushroom_stew) { 
                setInventorySlotContents(slot, new ItemStack(net.minecraft.init.Items.bowl));
            } else {
                setInventorySlotContents(slot, null);
            }
        }
        bug.trapTick = 0; 
    }

    private void attractBugToTrap(LKEntityBug bug) {
        double dx = (xCoord + 0.5D - bug.posX) / 8.0D;
        double dy = (yCoord - bug.posY) / 8.0D;
        double dz = (zCoord + 0.5D - bug.posZ) / 8.0D;
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        double factor = 1.0D - distance;

        if (factor > 0.0D) {
            factor *= factor;
            bug.motionX += dx / distance * factor * 0.06D;
            bug.motionY += dy / distance * factor * 0.06D;
            bug.motionZ += dz / distance * factor * 0.06D;
        }
        bug.moveEntity(bug.motionX, bug.motionY, bug.motionZ);
    }

    private void captureBugResult(LKEntityBug bug) {
        ItemStack output = getStackInSlot(OUTPUT_SLOT);
        if (output == null) {
            setInventorySlotContents(OUTPUT_SLOT, new ItemStack(mod_LionKing.bug)); 
        } else if (output.getItem() == mod_LionKing.bug && output.stackSize < 64) { 
            output.stackSize++;
        } else {
            bug.dropItem(mod_LionKing.bug, 1); 
        }
    }

    private void updateBlockMetadata() {
        int metadata;
        if (closureTime >= 0 && closureTime < 8) {
            metadata = closureTime; 
        } else if (closureTime >= 8 && closureTime < 42) {
            metadata = 8; 
        } else if (closureTime >= 42 && closureTime < 50) {
            metadata = 50 - closureTime; 
        } else {
            metadata = 0; 
        }
        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, metadata, 3);
    }

    public float getAverageBaitSaturation() {
        float totalSaturation = 0.0F;
        for (int i = 0; i < INPUT_SLOT_COUNT; i++) {
            ItemStack stack = getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemFood) {
                float saturation = ((ItemFood) stack.getItem()).func_150906_h(stack); 
                float stackFactor = (float) stack.stackSize / 64.0F + 0.6F;
                totalSaturation += saturation * Math.min(stackFactor, 1.0F);
            }
        }
        return totalSaturation * 0.25F;
    }

    private float calculateBugProbability() {
        if (worldObj.provider.dimensionId != mod_LionKing.idPrideLands) return 0.0F; 

        AxisAlignedBB playerBounds = AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1)
                .expand(16.0D, 16.0D, 16.0D);
        if (!worldObj.getEntitiesWithinAABB(EntityPlayer.class, playerBounds).isEmpty()) return 0.0F;

        float saturation = getAverageBaitSaturation();
        if (saturation <= 0.0F) return 0.0F;

        float probability = 0.0F;
        for (int x = -8; x <= 8; x++) {
            for (int y = -4; y <= 4; y++) {
                for (int z = -8; z <= 8; z++) {
                    if (worldObj.getBlock(xCoord + x, yCoord + y, zCoord + z) == net.minecraft.init.Blocks.tallgrass) {
                        probability += 0.02F;
                    }
                }
            }
        }

        BiomeGenBase biome = worldObj.getBiomeGenForCoords(xCoord, zCoord);
        if (biome instanceof LKBiomeGenMountains) probability /= 2.0F;
        else if (biome instanceof LKBiomeGenAridSavannah) probability /= 3.0F;
        else if (biome instanceof LKBiomeGenDesert) probability /= 10.0F;
        else if (biome instanceof LKBiomeGenRainforest) probability *= 2.0F;
        else if (biome instanceof LKBiomeGenWoodedSavannah || biome instanceof LKBiomeGenBananaForest) probability *= 1.5F;
        if (worldObj.getWorldInfo().isRaining()) probability *= 1.3F;

        int nearbyTraps = countNearbyTraps();
        float trapFactor = 0.3F + (0.7F / nearbyTraps);
        probability *= trapFactor * (1.0F + saturation);

        float finalProbability = probability / 10.0F;
        return Math.min(finalProbability, 0.9F);
    }

    private int countNearbyTraps() {
        int count = 0;
        for (int x = -8; x <= 8; x++) {
            for (int y = -4; y <= 4; y++) {
                for (int z = -8; z <= 8; z++) {
                    TileEntity te = worldObj.getTileEntity(xCoord + x, yCoord + y, zCoord + z);
                    if (te instanceof LKTileEntityBugTrap) count++;
                }
            }
        }
        return count;
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
            markDirty();
            return stack;
        }

        ItemStack splitStack = inventory[slot].splitStack(amount);
        if (inventory[slot].stackSize == 0) inventory[slot] = null;
        markDirty();
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
        markDirty();
    }

    @Override
    public String getInventoryName() {
        return "Bug Trap"; 
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
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
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagList items = nbt.getTagList("Items", 10);
        for (int i = 0; i < items.tagCount(); i++) {
            NBTTagCompound itemTag = items.getCompoundTagAt(i);
            int slot = itemTag.getByte("BugSlots") & 0xFF;
            if (slot >= 0 && slot < inventory.length) {
                inventory[slot] = ItemStack.loadItemStackFromNBT(itemTag);
            }
        }
        closureTime = nbt.getInteger("Closure");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        NBTTagList items = new NBTTagList();
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null) {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setByte("BugSlots", (byte) i);
                inventory[i].writeToNBT(itemTag);
                items.appendTag(itemTag);
            }
        }
        nbt.setTag("Items", items);
        nbt.setInteger("Closure", closureTime);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("Closure", closureTime);
        for (int i = 0; i < INPUT_SLOT_COUNT; i++) {
            if (inventory[i] != null) {
                nbt.setTag("Slot" + i, inventory[i].writeToNBT(new NBTTagCompound()));
            }
        }
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        closureTime = pkt.func_148857_g().getInteger("Closure");
        for (int i = 0; i < INPUT_SLOT_COUNT; i++) {
            String key = "Slot" + i;
            if (pkt.func_148857_g().hasKey(key)) {
                inventory[i] = ItemStack.loadItemStackFromNBT(pkt.func_148857_g().getCompoundTag(key));
            } else {
                inventory[i] = null;
            }
        }
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if (slot >= INPUT_SLOT_COUNT) return false;
        Item item = stack.getItem();
        if (!(item instanceof ItemFood)) return false;
        ItemFood food = (ItemFood) item;
        return food.func_150906_h(stack) > 0.0F && !food.isWolfsFavoriteMeat() && 
               food != net.minecraft.init.Items.fish && food != net.minecraft.init.Items.cooked_fished; 
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return side == 0 ? OUTPUT_SLOTS : INPUT_SLOTS;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return slot < INPUT_SLOT_COUNT && isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return slot == OUTPUT_SLOT;
    }
}