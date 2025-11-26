package lionking.tileentity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import lionking.mod_LionKing;
import lionking.common.LKLevelData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LKTileEntityOutlandsPool extends TileEntity {
    private static final List<ListedItem> inventory = new ArrayList<ListedItem>();
    private static int timeUntilInventoryClear = 0;
    private static final int CLEAR_DELAY = 75;

    @Override
    public void updateEntity() {
        if (worldObj.provider.dimensionId != mod_LionKing.idOutlands || worldObj.isRemote) {
            return;
        }

        collectItems();

        if (timeUntilInventoryClear > 0 && worldObj.rand.nextInt(3) == 0) {
            spawnSmokeParticle();
        }
        if (timeUntilInventoryClear > 0 && worldObj.rand.nextInt(100) == 0) {
            playFizzSound();
        }
    }

    private void collectItems() {
        AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 0.875, zCoord + 1);
        List<EntityItem> items = worldObj.getEntitiesWithinAABB(EntityItem.class, bounds);

        for (EntityItem entity : items) {
            ItemStack stack = entity.getEntityItem();
            if (stack != null) {
                ListedItem item = new ListedItem(worldObj.getWorldTime(), stack.copy(), new Location(entity.posX, entity.posY, entity.posZ));
                if (!inventory.contains(item)) {
                    inventory.add(item);
                    timeUntilInventoryClear = CLEAR_DELAY;
                }
                worldObj.playSoundAtEntity(entity, "random.fizz", 0.7F, 1.6F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.4F);
                entity.setDead();
            }
        }
    }

    private void spawnSmokeParticle() {
        worldObj.spawnParticle("smoke", xCoord + worldObj.rand.nextFloat(), yCoord + 0.8F, zCoord + worldObj.rand.nextFloat(),
                0.0D, worldObj.rand.nextFloat() * 0.25D, 0.0D);
    }

    private void playFizzSound() {
        worldObj.playSoundEffect(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, "random.fizz", 0.7F,
                1.6F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.4F);
    }

    public static void updateInventory(World world) {
        if (world.isRemote || --timeUntilInventoryClear > 0) {
            return;
        }

        if (!inventory.isEmpty()) {
            processRecipes(world);
            dropRemainingItems(world);
            inventory.clear();
        }
    }

    private static void processRecipes(World world) {
        int silver = countItems(mod_LionKing.silver);
        int kivulite = countItems(mod_LionKing.kivulite);
        if (silver > 1 && kivulite > 4) {
            processSilverKivuliteRecipe(world, silver, kivulite);
        }

        int featherBlue = countItems(mod_LionKing.featherBlue);
        int featherYellow = countItems(mod_LionKing.featherYellow);
        int featherRed = countItems(mod_LionKing.featherRed);
        int featherBlack = countItems(mod_LionKing.featherBlack);
        if (featherBlue > 0 && featherYellow > 0 && featherRed > 0 && featherBlack > 0) {
            processFeatherRecipe(world, featherBlue, featherYellow, featherRed, featherBlack);
        }

        int rafikiCoins = countItems(mod_LionKing.rafikiCoin);
        if (rafikiCoins > 0) {
            processRafikiCoinRecipe(world, rafikiCoins);
        }
    }

    private static int countItems(Item item) {
        int count = 0;
        for (ListedItem listedItem : inventory) {
            if (listedItem != null && listedItem.item.getItem() == item) {
                count += listedItem.item.stackSize;
            }
        }
        return count;
    }

    private static void processSilverKivuliteRecipe(World world, int silver, int kivulite) {
        int count = Math.min(MathHelper.floor_double(silver / 2.0), MathHelper.floor_double(kivulite / 5.0));
        consumeItems(mod_LionKing.silver, count * 2);
        consumeItems(mod_LionKing.kivulite, count * 5);
        spawnItems(world, new ItemStack(mod_LionKing.outlandsHelm), count);
    }

    private static void processFeatherRecipe(World world, int blue, int yellow, int red, int black) {
        int count = Math.min(Math.min(blue, yellow), Math.min(red, black));
        consumeItems(mod_LionKing.featherBlue, count);
        consumeItems(mod_LionKing.featherYellow, count);
        consumeItems(mod_LionKing.featherRed, count);
        consumeItems(mod_LionKing.featherBlack, count);
        spawnItems(world, new ItemStack(mod_LionKing.outlandsFeather), count);
    }

    private static void processRafikiCoinRecipe(World world, int coinCount) {
        consumeItems(mod_LionKing.rafikiCoin, coinCount);
        spawnItems(world, new ItemStack(mod_LionKing.ziraCoin), coinCount);
    }

    private static void consumeItems(Item item, int count) {
        int remaining = count;
        Iterator<ListedItem> iterator = inventory.iterator();
        while (iterator.hasNext() && remaining > 0) {
            ListedItem listedItem = iterator.next();
            if (listedItem != null && listedItem.item.getItem() == item) {
                int toRemove = Math.min(remaining, listedItem.item.stackSize);
                listedItem.item.stackSize -= toRemove;
                remaining -= toRemove;
                if (listedItem.item.stackSize <= 0) {
                    iterator.remove();
                }
            }
        }
    }

    private static void dropRemainingItems(World world) {
        for (ListedItem listedItem : inventory) {
            if (listedItem != null) {
                int randX = LKLevelData.moundX + (world.rand.nextBoolean() ? 1 : -1);
                int randZ = LKLevelData.moundZ + (world.rand.nextBoolean() ? 1 : -1);
                spawnItem(world, listedItem.item, randX + 0.25D + world.rand.nextFloat() / 2.0F, LKLevelData.moundY + 8D, randZ + 0.25D + world.rand.nextFloat() / 2.0F);
            }
        }
    }

    private static void spawnItems(World world, ItemStack stack, int count) {
        for (int i = 0; i < count; i++) {
            spawnItem(world, stack.copy(), LKLevelData.moundX + 0.45D + world.rand.nextFloat() * 0.1F,
                    LKLevelData.moundY + 9D, LKLevelData.moundZ + 0.45D + world.rand.nextFloat() * 0.1F);
        }
    }

    private static void spawnItem(World world, ItemStack stack, double x, double y, double z) {
        EntityItem item = new EntityItem(world, x, y, z, stack);
        item.delayBeforeCanPickup = 10;
        item.motionX = 0.0D;
        item.motionY = 0.4D + world.rand.nextFloat() / 5.0F;
        item.motionZ = 0.0D;
        world.spawnEntityInWorld(item);
        world.playSoundAtEntity(item, "random.explode", 1.5F, 0.5F + world.rand.nextFloat() * 0.2F);
    }

    public static void writeInventoryToNBT(NBTTagCompound levelData) {
        NBTTagList tagList = new NBTTagList();
        for (ListedItem listedItem : inventory) {
            if (listedItem != null) {
                NBTTagCompound nbt = new NBTTagCompound();
                listedItem.item.writeToNBT(nbt);
                nbt.setDouble("X", listedItem.location.posX);
                nbt.setDouble("Y", listedItem.location.posY);
                nbt.setDouble("Z", listedItem.location.posZ);
                nbt.setLong("TIMESTAMP", listedItem.timestamp);
                tagList.appendTag(nbt);
            }
        }
        levelData.setTag("PoolInventory", tagList);
    }

    public static void readInventoryFromNBT(NBTTagCompound levelData) {
        inventory.clear();
        NBTTagList tagList = levelData.getTagList("PoolInventory", 10); 
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound nbt = tagList.getCompoundTagAt(i);
            ItemStack stack = ItemStack.loadItemStackFromNBT(nbt);
            double x = nbt.getDouble("X");
            double y = nbt.getDouble("Y");
            double z = nbt.getDouble("Z");
            long timestamp = nbt.getLong("TIMESTAMP");
            inventory.add(new ListedItem(timestamp, stack, new Location(x, y, z)));
        }
    }

    private static class ListedItem {
        public final long timestamp;
        public ItemStack item;
        public final Location location;

        public ListedItem(long timestamp, ItemStack item, Location location) {
            this.timestamp = timestamp;
            this.item = item;
            this.location = location;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ListedItem)) return false;
            ListedItem other = (ListedItem) obj;
            return timestamp == other.timestamp &&
                    location.equals(other.location) &&
                    item.getItem() == other.item.getItem() &&
                    item.stackSize == other.item.stackSize &&
                    item.getItemDamage() == other.item.getItemDamage();
        }
    }

    private static class Location {
        public final double posX, posY, posZ;

        public Location(double x, double y, double z) {
            this.posX = x;
            this.posY = y;
            this.posZ = z;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Location)) return false;
            Location other = (Location) obj;
            return posX == other.posX && posY == other.posY && posZ == other.posZ;
        }
    }
}