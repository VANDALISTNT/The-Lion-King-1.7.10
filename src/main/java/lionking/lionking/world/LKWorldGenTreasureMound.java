package lionking.world;

import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenerator;
import cpw.mods.fml.common.FMLLog;
import lionking.mod_LionKing;
import lionking.biome.LKOutlandsBiome;

public class LKWorldGenTreasureMound extends WorldGenerator {
    @Override
    public boolean generate(World world, Random random, int i, int j, int k) {
        // Check height limits
        if (j < 1 || j + 13 >= 256) {
            FMLLog.fine("Cannot generate Treasure Mound at (%d, %d, %d): invalid height", i, j, k);
            return false;
        }

        if (world.getBiomeGenForCoords(i, k) == LKOutlandsBiome.outlandsRiver) {
            FMLLog.fine("Skipping Treasure Mound generation in outlandsRiver at (%d, %d, %d)", i, j, k);
            return false;
        }

        for (int i1 = 1; i1 < 5; i1++) {
            for (int k1 = 1; k1 < 5; k1++) {
                if (world.getBlock(i + i1, j - 1, k + k1) != Blocks.sand || !world.isAirBlock(i + i1, j, k + k1)) {
                    FMLLog.fine("Cannot generate Treasure Mound at (%d, %d, %d): invalid base", i, j, k);
                    return false;
                }
            }
        }

        if (mod_LionKing.outsand == null || mod_LionKing.termite == null || mod_LionKing.prideBrick == null) {
            FMLLog.warning("Cannot generate Treasure Mound at (%d, %d, %d): outsand, termite, or prideBrick is null", i, j, k);
            return false;
        }

        for (int i1 = 1; i1 < 5; i1++) {
            for (int k1 = 1; k1 < 5; k1++) {
                world.setBlock(i + i1, j - 1, k + k1, mod_LionKing.outsand, 0, 2);
            }
        }

        int[][] heights = new int[6][6];
        for (int i1 = 0; i1 < 6; i1++) {
            for (int k1 = 0; k1 < 6; k1++) {
                heights[i1][k1] = 3 + random.nextInt(2);
                if (i1 > 0 && i1 < 5 && k1 > 0 && k1 < 5) {
                    heights[i1][k1] = 6 + random.nextInt(4);
                }
                if (i1 > 1 && i1 < 4 && k1 > 1 && k1 < 4) {
                    heights[i1][k1] = 8 + random.nextInt(6);
                }
            }
        }

        for (int i1 = 0; i1 < 6; i1++) {
            for (int k1 = 0; k1 < 6; k1++) {
                int height = heights[i1][k1];
                for (int j1 = 0; j1 < height && j + j1 < 256; j1++) {
                    world.setBlock(i + i1, j + j1, k + k1, mod_LionKing.termite, 0, 2);
                }
                for (int j1 = -1; world.isAirBlock(i + i1, j + j1, k + k1); j1--) {
                    world.setBlock(i + i1, j + j1, k + k1, mod_LionKing.termite, 0, 2);
                }
            }
        }

        int clearHeight = 2 + random.nextInt(2);
        for (int i1 = 1; i1 < 5; i1++) {
            for (int k1 = 1; k1 < 5; k1++) {
                for (int j1 = 0; j1 < clearHeight; j1++) {
                    world.setBlock(i + i1, j + j1, k + k1, Blocks.air, 0, 2);
                }
            }
        }

        int columnHeight = clearHeight + 1;
        for (int j1 = 0; j1 < columnHeight; j1++) {
            world.setBlock(i + 1, j + j1, k + 1, mod_LionKing.prideBrick, 1, 2);
            world.setBlock(i + 1, j + j1, k + 4, mod_LionKing.prideBrick, 1, 2);
            world.setBlock(i + 4, j + j1, k + 1, mod_LionKing.prideBrick, 1, 2);
            world.setBlock(i + 4, j + j1, k + 4, mod_LionKing.prideBrick, 1, 2);
        }

        int direction = random.nextInt(4);
        ChunkCoordinates[] chestLocations = placeEntrance(world, random, i, j, k, direction);

        if (!world.isRemote && chestLocations != null) {
            for (ChunkCoordinates pos : chestLocations) {
                world.setBlock(pos.posX, pos.posY, pos.posZ, Blocks.chest, 0, 2);
                TileEntityChest chest = (TileEntityChest) world.getTileEntity(pos.posX, pos.posY, pos.posZ);
                if (chest != null) {
                    for (int i3 = 0; i3 < 4; i3++) {
                        ItemStack item = pickLoot(random);
                        if (item != null && item.getItem().getItemEnchantability() > 0 && random.nextInt(3) == 0) {
                            EnchantmentHelper.addRandomEnchantment(random, item, 3);
                        }
                        if (item != null) {
                            chest.setInventorySlotContents(random.nextInt(chest.getSizeInventory()), item);
                        }
                    }
                    FMLLog.fine("Filled chest at (%d, %d, %d)", pos.posX, pos.posY, pos.posZ);
                } else {
                    FMLLog.warning("Failed to create chest at (%d, %d, %d)", pos.posX, pos.posY, pos.posZ);
                }
            }
        }

        FMLLog.info("Successfully generated Treasure Mound at (%d, %d, %d)", i, j, k);
        return true;
    }

    private ChunkCoordinates[] placeEntrance(World world, Random random, int i, int j, int k, int direction) {
        ChunkCoordinates[] chestLocations = new ChunkCoordinates[2];
        switch (direction) {
            case 0:
                world.setBlock(i, j + 1, k + 2, Blocks.air, 0, 2);
                world.setBlock(i, j + 1, k + 3, Blocks.air, 0, 2);
                world.setBlock(i, j + 2, k + 2, mod_LionKing.termite, 0, 2);
                world.setBlock(i, j + 2, k + 3, mod_LionKing.termite, 0, 2);
                chestLocations[0] = new ChunkCoordinates(i + 4, j, k + 2);
                chestLocations[1] = new ChunkCoordinates(i + 4, j, k + 3);
                break;
            case 1:
                world.setBlock(i + 5, j + 1, k + 2, Blocks.air, 0, 2);
                world.setBlock(i + 5, j + 1, k + 3, Blocks.air, 0, 2);
                world.setBlock(i + 5, j + 2, k + 2, mod_LionKing.termite, 0, 2);
                world.setBlock(i + 5, j + 2, k + 3, mod_LionKing.termite, 0, 2);
                chestLocations[0] = new ChunkCoordinates(i + 1, j, k + 2);
                chestLocations[1] = new ChunkCoordinates(i + 1, j, k + 3);
                break;
            case 2:
                world.setBlock(i + 2, j + 1, k, Blocks.air, 0, 2);
                world.setBlock(i + 3, j + 1, k, Blocks.air, 0, 2);
                world.setBlock(i + 2, j + 2, k, mod_LionKing.termite, 0, 2);
                world.setBlock(i + 3, j + 2, k, mod_LionKing.termite, 0, 2);
                chestLocations[0] = new ChunkCoordinates(i + 2, j, k + 4);
                chestLocations[1] = new ChunkCoordinates(i + 3, j, k + 4);
                break;
            case 3:
                world.setBlock(i + 2, j + 1, k + 5, Blocks.air, 0, 2);
                world.setBlock(i + 3, j + 1, k + 5, Blocks.air, 0, 2);
                world.setBlock(i + 2, j + 2, k + 5, mod_LionKing.termite, 0, 2);
                world.setBlock(i + 3, j + 2, k + 5, mod_LionKing.termite, 0, 2);
                chestLocations[0] = new ChunkCoordinates(i + 2, j, k + 1);
                chestLocations[1] = new ChunkCoordinates(i + 3, j, k + 1);
                break;
        }
        return chestLocations;
    }

    private ItemStack pickLoot(Random random) {
        int type = random.nextInt(7);
        switch (type) {
            case 0: return mod_LionKing.itemTermite != null ? new ItemStack(mod_LionKing.itemTermite, 2 + random.nextInt(5)) : null;
            case 1: return mod_LionKing.dartBlack != null ? new ItemStack(mod_LionKing.dartBlack, 4 + random.nextInt(4)) : null;
            case 2: return mod_LionKing.nukaShard != null ? new ItemStack(mod_LionKing.nukaShard, 3 + random.nextInt(7)) : null;
            case 3: return mod_LionKing.featherBlack != null ? new ItemStack(mod_LionKing.featherBlack, 2 + random.nextInt(3)) : null;
            case 4: return mod_LionKing.lionCooked != null ? new ItemStack(mod_LionKing.lionCooked, 2 + random.nextInt(3)) : null;
            case 5:
                int toolType = random.nextInt(5);
                switch (toolType) {
                    case 0: return mod_LionKing.swordCorrupt != null ? new ItemStack(mod_LionKing.swordCorrupt) : null;
                    case 1: return mod_LionKing.pickaxeCorrupt != null ? new ItemStack(mod_LionKing.pickaxeCorrupt) : null;
                    case 2: return mod_LionKing.swordKivulite != null ? new ItemStack(mod_LionKing.swordKivulite) : null;
                    case 3: return mod_LionKing.pickaxeKivulite != null ? new ItemStack(mod_LionKing.pickaxeKivulite) : null;
                    case 4: return mod_LionKing.axeKivulite != null ? new ItemStack(mod_LionKing.axeKivulite) : null;
                }
            case 6: return mod_LionKing.kivulite != null ? new ItemStack(mod_LionKing.kivulite, 1 + random.nextInt(3)) : null;
            default: return null;
        }
    }

    private boolean isBlockReplaceable(World world, int i, int j, int k) {
        Block block = world.getBlock(i, j, k);
        return block == Blocks.air || block.isLeaves(world, i, j, k) || block == Blocks.tallgrass || block == Blocks.deadbush;
    }
}