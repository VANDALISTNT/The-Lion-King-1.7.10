package lionking.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.World;
import net.minecraft.world.IBlockAccess;
import net.minecraft.item.Item;
import net.minecraftforge.common.IShearable;

import lionking.client.LKCreativeTabs;
import lionking.mod_LionKing;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Random;

public class LKBlockLeaves extends BlockLeavesBase implements IShearable {
    @SideOnly(Side.CLIENT)
    private IIcon[] leafIcons;

    private int[] adjacentTreeBlocks;
    private int leafMode;
    protected boolean graphicsLevel; 

    public LKBlockLeaves() { 
        super(Material.leaves, false);
        setTickRandomly(true);
        setCreativeTab(LKCreativeTabs.tabDecorations); 
        setBlockName("leaves"); 
    }

    @Override
    public boolean isOpaqueCube() {
        return !graphicsLevel;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block oldBlock, int oldMetadata) { 
        int radius = 1;
        int bounds = radius + 1;
        if (!world.checkChunksExist(x - bounds, y - bounds, z - bounds, x + bounds, y + bounds, z + bounds)) return;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    Block block = world.getBlock(x + dx, y + dy, z + dz); 
                    if (block == this) {
                        int metadata = world.getBlockMetadata(x + dx, y + dy, z + dz);
                        world.setBlockMetadataWithNotify(x + dx, y + dy, z + dz, metadata | 8, 4);
                    }
                }
            }
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (world.isRemote) return;

        int metadata = world.getBlockMetadata(x, y, z);
        if ((metadata & 8) == 0 || (metadata & 4) != 0) return;

        byte searchRadius = 4;
        int bounds = searchRadius + 1;
        byte arraySize = 32;
        int arraySquare = arraySize * arraySize;
        int halfArray = arraySize / 2;

        if (adjacentTreeBlocks == null) {
            adjacentTreeBlocks = new int[arraySize * arraySize * arraySize];
        }

        if (!world.checkChunksExist(x - bounds, y - bounds, z - bounds, x + bounds, y + bounds, z + bounds)) return;

        for (int dx = -searchRadius; dx <= searchRadius; dx++) {
            for (int dy = -searchRadius; dy <= searchRadius; dy++) {
                for (int dz = -searchRadius; dz <= searchRadius; dz++) {
                    Block block = world.getBlock(x + dx, y + dy, z + dz); 
                    int index = (dx + halfArray) * arraySquare + (dy + halfArray) * arraySize + (dz + halfArray);
                    if (block == mod_LionKing.prideWood || block == mod_LionKing.prideWood2) { 
                        adjacentTreeBlocks[index] = 0;
                    } else if (block == this) {
                        adjacentTreeBlocks[index] = -2;
                    } else {
                        adjacentTreeBlocks[index] = -1;
                    }
                }
            }
        }

        for (int distance = 1; distance <= 4; distance++) {
            for (int dx = -searchRadius; dx <= searchRadius; dx++) {
                for (int dy = -searchRadius; dy <= searchRadius; dy++) {
                    for (int dz = -searchRadius; dz <= searchRadius; dz++) {
                        int index = (dx + halfArray) * arraySquare + (dy + halfArray) * arraySize + (dz + halfArray);
                        if (adjacentTreeBlocks[index] != distance - 1) continue;

                        int[] offsets = {-1, 1};
                        for (int offset : offsets) {
                            if (adjacentTreeBlocks[(dx + halfArray + offset) * arraySquare + (dy + halfArray) * arraySize + (dz + halfArray)] == -2) {
                                adjacentTreeBlocks[(dx + halfArray + offset) * arraySquare + (dy + halfArray) * arraySize + (dz + halfArray)] = distance;
                            }
                            if (adjacentTreeBlocks[(dx + halfArray) * arraySquare + (dy + halfArray + offset) * arraySize + (dz + halfArray)] == -2) {
                                adjacentTreeBlocks[(dx + halfArray) * arraySquare + (dy + halfArray + offset) * arraySize + (dz + halfArray)] = distance;
                            }
                            if (adjacentTreeBlocks[(dx + halfArray) * arraySquare + (dy + halfArray) * arraySize + (dz + halfArray + offset)] == -2) {
                                adjacentTreeBlocks[(dx + halfArray) * arraySquare + (dy + halfArray) * arraySize + (dz + halfArray + offset)] = distance;
                            }
                        }
                    }
                }
            }
        }

        int centerValue = adjacentTreeBlocks[halfArray * arraySquare + halfArray * arraySize + halfArray];
        if (centerValue >= 0) {
            world.setBlockMetadataWithNotify(x, y, z, metadata & ~8, 4);
        } else {
            removeLeaves(world, x, y, z);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        if (world.canLightningStrikeAt(x, y + 1, z) && !world.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && random.nextInt(15) == 1) { 
            double posX = x + random.nextFloat();
            double posY = y - 0.05D;
            double posZ = z + random.nextFloat();
            world.spawnParticle("dripWater", posX, posY, posZ, 0.0D, 0.0D, 0.0D);
        }

        if (this == mod_LionKing.passionLeaves) { 
            for (int i = 0; i < 4; i++) {
                double posX = x + random.nextFloat();
                double posY = y + random.nextFloat();
                double posZ = z + random.nextFloat();
                double motionX = (random.nextFloat() - 0.5F) * 0.01F;
                double motionY = random.nextFloat() * 0.01F;
                double motionZ = (random.nextFloat() - 0.5F) * 0.01F;
                mod_LionKing.proxy.spawnParticle("passion", posX, posY, posZ, motionX, motionY, motionZ);
            }
        }
    }

    private void removeLeaves(World world, int x, int y, int z) {
        dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
        world.setBlockToAir(x, y, z);
    }

    @Override
    public int quantityDropped(Random random) {
        if (this == mod_LionKing.mangoLeaves || this == mod_LionKing.passionLeaves) { 
            return random.nextInt(12) == 0 ? 1 : 0;
        }
        if (this == mod_LionKing.bananaLeaves) {
            return random.nextInt(9) == 0 ? 1 : 0;
        }
        return random.nextInt(22) == 0 ? 1 : 0;
    }

    @Override
    public Item getItemDropped(int metadata, Random random, int fortune) { 
        if (this == mod_LionKing.forestLeaves) {
            return Item.getItemFromBlock(mod_LionKing.forestSapling); 
        }
        if (this == mod_LionKing.mangoLeaves) {
            return random.nextInt(5) > 1 ? mod_LionKing.mango : Item.getItemFromBlock(mod_LionKing.mangoSapling); 
        }
        if (this == mod_LionKing.passionLeaves) {
            return random.nextBoolean() ? mod_LionKing.passionFruit : Item.getItemFromBlock(mod_LionKing.passionSapling); 
        }
        if (this == mod_LionKing.bananaLeaves) {
            return Item.getItemFromBlock(mod_LionKing.bananaSapling); 
        }
        return Item.getItemFromBlock(mod_LionKing.sapling); 
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        return leafIcons[leafMode];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) { 
        leafIcons = new IIcon[2];
        leafIcons[0] = iconRegister.registerIcon("lionking:acaciaLeaves_fast");
        leafIcons[1] = iconRegister.registerIcon("lionking:acaciaLeaves_fancy");
    }

    public void setGraphicsLevel(boolean fancy) {
        graphicsLevel = fancy;
        leafMode = fancy ? 1 : 0;
    }

    @Override
    public boolean isShearable(ItemStack item, IBlockAccess world, int x, int y, int z) { 
        return true;
    }

    @Override
    public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, int x, int y, int z, int fortune) { 
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(new ItemStack(this, 1, world.getBlockMetadata(x, y, z) & 3));
        return drops;
    }
}
