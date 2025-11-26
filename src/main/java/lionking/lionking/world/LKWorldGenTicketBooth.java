package lionking.world;

import net.minecraft.world.World;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.StatCollector;
import net.minecraft.world.gen.feature.WorldGenerator;
import java.util.Random;
import java.util.Iterator;
import cpw.mods.fml.common.FMLLog;
import lionking.mod_LionKing;
import lionking.common.LKLevelData;
import lionking.entity.LKEntityTicketLion;

public class LKWorldGenTicketBooth extends WorldGenerator {
    public static boolean canGenerateAtPosition(int i, int j, int k) {
        if (LKLevelData.ticketBoothLocations.isEmpty()) {
            FMLLog.fine("No existing ticket booths, allowing generation at (%d, %d, %d)", i, j, k);
            return true;
        } else {
            Iterator it = LKLevelData.ticketBoothLocations.iterator();
            while (it.hasNext()) {
                ChunkCoordinates coords = (ChunkCoordinates)it.next();
                if (coords.getDistanceSquared(i, j, k) < mod_LionKing.boothLimit * mod_LionKing.boothLimit) {
                    FMLLog.fine("Cannot generate ticket booth at (%d, %d, %d): too close to existing booth", i, j, k);
                    return false;
                }
            }
            FMLLog.fine("Sufficient distance from other ticket booths at (%d, %d, %d)", i, j, k);
            return true;
        }
    }

    @Override
    public boolean generate(World world, Random random, int i, int j, int k) {
        FMLLog.fine("Attempting to generate ticket booth at (%d, %d, %d)", i, j, k);

        if (!isBoundingBoxClear(world, i + 2, i + 16, j + 1, j + 5, k - 3, k + 10) ||
            !isBoundingBoxClear(world, i + 1, i + 1, j + 1, j + 3, k - 3, k + 10) ||
            !isBoundingBoxClear(world, i - 3, i, j + 1, j + 3, k - 3, k + 5)) {
            FMLLog.fine("Cannot generate ticket booth at (%d, %d, %d): bounding box not clear", i, j, k);
            return false;
        }

        if (!isSolidGround(world, i - 3, j, k) || !isSolidGround(world, i - 3, j, k + 3) ||
            !isSolidGround(world, i + 7, j, k + 3) || !isSolidGround(world, i + 11, j, k + 4) ||
            !isSolidGround(world, i + 3, j, k + 5) || !isSolidGround(world, i + 3, j, k + 2)) {
            FMLLog.fine("Cannot generate ticket booth at (%d, %d, %d): invalid ground", i, j, k);
            return false;
        }

        int woolType = mod_LionKing.randomBooths ? random.nextInt(16) : 14;
        int woodType = 0;
        Block stairBlock = Blocks.oak_stairs;
        if (mod_LionKing.randomBooths) {
            int l = random.nextInt(4);
            switch (l) {
                case 0: stairBlock = Blocks.oak_stairs; woodType = 0; break;
                case 1: stairBlock = Blocks.spruce_stairs; woodType = 1; break;
                case 2: stairBlock = Blocks.birch_stairs; woodType = 2; break;
                case 3: stairBlock = Blocks.jungle_stairs; woodType = 3; break;
            }
        }

        Block seatBlock = Blocks.oak_stairs;
        if (mod_LionKing.randomBooths) {
            int l = random.nextInt(6);
            switch (l) {
                case 0: seatBlock = Blocks.oak_stairs; break;
                case 1: seatBlock = Blocks.spruce_stairs; break;
                case 2: seatBlock = Blocks.birch_stairs; break;
                case 3: seatBlock = Blocks.jungle_stairs; break;
                case 4: seatBlock = Blocks.stone_stairs; break;
                case 5: seatBlock = Blocks.stone_brick_stairs; break;
            }
        }

        Block fillerBlock = world.getWorldChunkManager().getBiomeGenAt(i, k).fillerBlock;
        if (fillerBlock == null) {
            FMLLog.warning("Filler block is null at (%d, %d, %d), using dirt", i, j, k);
            fillerBlock = Blocks.dirt;
        }

        for (int i1 = -2; i1 < 16; i1++) {
            for (int k1 = -2; k1 < 10; k1++) {
                if (!(k1 > 4 && i1 < 2)) {
                    world.setBlock(i + i1, j, k + k1, Blocks.cobblestone, 0, 2);
                    Block block = world.getBlock(i + i1, j - 1, k + k1);
                    for (int j1 = 1; block == null || !block.isOpaqueCube(); j1++) {
                        world.setBlock(i + i1, j - j1, k + k1, fillerBlock, 0, 2);
                        block = world.getBlock(i + i1, j - j1 - 1, k + k1);
                    }
                    for (int j1 = 1; j1 < 4; j1++) {
                        world.setBlock(i + i1, j + j1, k + k1, Blocks.planks, woodType, 2);
                    }
                    if (i1 > 2) {
                        for (int j1 = 4; j1 < 6; j1++) {
                            world.setBlock(i + i1, j + j1, k + k1, Blocks.planks, woodType, 2);
                        }
                    }
                    world.setBlock(i + i1, j + 2, k + k1, Blocks.stonebrick, 0, 2);
                }
            }
        }

        for (int i1 = 3; i1 < 15; i1++) {
            for (int k1 = -1; k1 < 9; k1++) {
                for (int j1 = 1; j1 < 5; j1++) {
                    world.setBlock(i + i1, j + j1, k + k1, Blocks.air, 0, 2);
                }
                if (!(i1 < 10 && i1 % 2 == 1)) {
                    world.setBlock(i + i1, j, k + k1, Blocks.wool, woolType, 2);
                }
                if (i1 < 10 && i1 % 2 == 1 && k1 != 3 && k1 != 4) {
                    world.setBlock(i + i1, j + 1, k + k1, seatBlock, 1, 2);
                }
                if (k1 == 3 || k1 == 4) {
                    world.setBlock(i + i1, j, k + k1, Blocks.cobblestone, 0, 2);
                }
            }
        }

        for (int j1 = 0; j1 < 5; j1++) {
            for (int k1 = 2; k1 < 6; k1++) {
                if (!(j1 > 0 && j1 < 4 && k1 > 2 && k1 < 5)) {
                    world.setBlock(i + 14, j + j1, k + k1, mod_LionKing.lionPortalFrame, 0, 2);
                }
            }
        }

        for (int i1 = -2; i1 < 3; i1++) {
            for (int j1 = 1; j1 < 3; j1++) {
                world.setBlock(i + i1, j + j1, k + 3, Blocks.air, 0, 2);
            }
        }

        for (int i1 = -1; i1 < 1; i1++) {
            for (int j1 = 1; j1 < 3; j1++) {
                for (int k1 = -1; k1 < 2; k1++) {
                    world.setBlock(i + i1, j + j1, k + k1, Blocks.air, 0, 2);
                    if (i1 == -1 && j1 == 2) {
                        world.setBlock(i + i1 - 1, j + j1, k + k1, Blocks.air, 0, 2);
                    }
                    if (i1 == 0 && j1 == 2 && k1 != 0) {
                        world.setBlock(i + i1, j + j1, k + k1, Blocks.torch, k1 == -1 ? 3 : 2, 2);
                    }
                    if (i1 == -1 && j1 == 1 && k1 == 0) {
                        world.setBlock(i + i1 - 1, j + j1, k + k1, Blocks.fence, 0, 2);
                    }
                    if (i1 == -1 && j1 == 2 && k1 != 0) {
                        world.setBlock(i + i1 - 1, j + j1, k + k1, Blocks.glass_pane, 0, 2);
                    }
                }
            }
        }

        for (int i1 = 4; i1 < 15; i1++) {
            world.setBlock(i + i1, j + 4, k - 1, stairBlock, 7, 2);
            world.setBlock(i + i1, j + 4, k + 8, stairBlock, 6, 2);
        }

        for (int k1 = 0; k1 < 8; k1++) {
            world.setBlock(i + 3, j + 4, k + k1, stairBlock, 5, 2);
            if (k1 < 2 || k1 > 5) {
                world.setBlock(i + 14, j + 4, k + k1, stairBlock, 4, 2);
            }
        }

        for (int j1 = 0; j1 < 5; j1++) {
            Block block = j1 == 2 ? Blocks.glowstone : Blocks.planks;
            world.setBlock(i + 3, j + j1, k - 1, block, block == Blocks.planks ? woodType : 0, 2);
            world.setBlock(i + 14, j + j1, k - 1, block, block == Blocks.planks ? woodType : 0, 2);
            world.setBlock(i + 3, j + j1, k + 8, block, block == Blocks.planks ? woodType : 0, 2);
            world.setBlock(i + 14, j + j1, k + 8, block, block == Blocks.planks ? woodType : 0, 2);
        }

        for (int k1 = -2; k1 < 5; k1++) {
            if (k1 == 3) {
                world.setBlock(i - 3, j + 3, k + k1, Blocks.planks, woodType, 2);
            } else {
                world.setBlock(i - 3, j + 3, k + k1, stairBlock, 0, 2);
            }
        }

        for (int i1 = -2; i1 < 4; i1++) {
            world.setBlock(i + i1, j + 3, k - 3, stairBlock, 2, 2);
        }

        for (int i1 = -2; i1 < 1; i1++) {
            world.setBlock(i + i1, j + 3, k + 5, stairBlock, 3, 2);
        }

        generateSupports(world, i + 1, j + 3, k + 5, stairBlock, 0, woodType);
        world.setBlock(i + 1, j + 3, k + 5, Blocks.planks, woodType, 2);

        for (int k1 = 6; k1 < 10; k1++) {
            world.setBlock(i + 1, j + 3, k + k1, stairBlock, 0, 2);
        }

        for (int k1 = -2; k1 < 10; k1++) {
            world.setBlock(i + 2, j + 5, k + k1, stairBlock, 0, 2);
            world.setBlock(i + 16, j + 5, k + k1, stairBlock, 1, 2);
        }

        for (int i1 = 3; i1 < 16; i1++) {
            world.setBlock(i + i1, j + 5, k - 3, stairBlock, 2, 2);
            world.setBlock(i + i1, j + 5, k + 10, stairBlock, 3, 2);
        }

        world.setBlock(i + 2, j + 3, k + 10, stairBlock, 3, 2);
        world.setBlock(i + 3, j + 3, k + 10, stairBlock, 3, 2);
		
        generateSupports(world, i - 3, j + 3, k - 3, stairBlock, 0, woodType);
        generateSupports(world, i - 3, j + 3, k + 5, stairBlock, 0, woodType);
        generateSupports(world, i + 1, j + 3, k + 10, stairBlock, 0, woodType);
        generateSupports(world, i + 4, j + 3, k + 10, stairBlock, 3, woodType);
        generateSupports(world, i + 4, j + 3, k - 3, stairBlock, 1, woodType);
        world.setBlock(i + 2, j + 5, k - 3, stairBlock, 0, 2);
        world.setBlock(i + 2, j + 5, k + 10, stairBlock, 0, 2);
        generateSupports(world, i + 16, j + 5, k - 3, stairBlock, 2, woodType);
        generateSupports(world, i + 16, j + 5, k + 10, stairBlock, 3, woodType);

        world.setBlock(i - 2, j + 1, k + 3, Blocks.wooden_door, 0, 2);
        world.setBlock(i - 2, j + 2, k + 3, Blocks.wooden_door, 8, 2);

        for (int i1 = 5; i1 < 13; i1++) {
            if (i1 % 3 != 1) {
                world.setBlock(i + i1, j + 2, k - 1, Blocks.torch, 0, 2);
                world.setBlock(i + i1, j + 2, k + 8, Blocks.torch, 0, 2);
            }
        }

        for (int k1 = 1; k1 < 7; k1++) {
            if (k1 < 2 || k1 > 5) {
                world.setBlock(i + 14, j + 2, k + k1, Blocks.torch, 0, 2);
            }
            if (k1 < 3 || k1 > 4) {
                world.setBlock(i + 3, j + 2, k + k1, Blocks.torch, 0, 2);
            }
        }

        world.setBlock(i - 3, j + 2, k - 2, Blocks.torch, 0, 2);
        world.setBlock(i - 3, j + 2, k + 2, Blocks.torch, 0, 2);
        world.setBlock(i - 3, j + 2, k + 4, Blocks.torch, 0, 2);
        world.setBlock(i - 4, j + 3, k + 3, Blocks.wall_sign, 4, 2);
        TileEntitySign sign = (TileEntitySign)world.getTileEntity(i - 4, j + 3, k + 3);
        if (sign != null) {
            sign.signText[0] = StatCollector.translateToLocal("sign.line1"); //"---------------"
            sign.signText[1] = StatCollector.translateToLocal("sign.line2"); //"Now showing:"
            sign.signText[2] = StatCollector.translateToLocal("sign.line3"); //"The Lion King"
            sign.signText[3] = StatCollector.translateToLocal("sign.line4"); //"---------------"
            FMLLog.fine("Placed sign at (%d, %d, %d)", i - 4, j + 3, k + 3);
        } else {
            FMLLog.warning("Failed to place sign at (%d, %d, %d)", i - 4, j + 3, k + 3);
        }

        if (!world.isRemote) {
            LKEntityTicketLion lion = new LKEntityTicketLion(world);
            lion.setLocationAndAngles(i + 0.5D, j + 1, k + 0.5D, 0.0F, 0.0F);
            if (world.spawnEntityInWorld(lion)) {
                FMLLog.fine("Successfully spawned Ticket Lion at (%d, %d, %d)", i, j + 1, k);
            } else {
                FMLLog.warning("Failed to spawn Ticket Lion at (%d, %d, %d)", i, j + 1, k);
            }
        }

        world.setBlock(i + 2, j + 1, k, Blocks.chest, 5, 2);
        world.setBlock(i + 2, j + 2, k, Blocks.trapdoor, 3, 2);
        TileEntityChest chest = (TileEntityChest)world.getTileEntity(i + 2, j + 1, k);
        if (chest != null) {
            int i1 = 2 + random.nextInt(4);
            for (int j1 = 0; j1 < i1; j1++) {
                chest.setInventorySlotContents(random.nextInt(chest.getSizeInventory()), getBasicLoot(random));
            }

            Item suitPart = null;
            int l = random.nextInt(4);
            switch (l) {
                case 0: suitPart = mod_LionKing.ticketLionHead; break;
                case 1: suitPart = mod_LionKing.ticketLionSuit; break;
                case 2: suitPart = mod_LionKing.ticketLionLegs; break;
                case 3: suitPart = mod_LionKing.ticketLionFeet; break;
            }
            chest.setInventorySlotContents(random.nextInt(chest.getSizeInventory()), new ItemStack(suitPart));
            FMLLog.fine("Filled chest at (%d, %d, %d)", i + 2, j + 1, k);
        } else {
            FMLLog.warning("Failed to place chest at (%d, %d, %d)", i + 2, j + 1, k);
        }

        FMLLog.info("Successfully generated ticket booth at (%d, %d, %d)", i, j, k);
        return true;
    }

    private ItemStack getBasicLoot(Random random) {
        int i = random.nextInt(11);
        switch (i) {
            case 0: return new ItemStack(Items.stick, 2 + random.nextInt(4));
            case 1: return new ItemStack(Items.paper, 1 + random.nextInt(3));
            case 2: return new ItemStack(Items.book, 1 + random.nextInt(2));
            case 3: return new ItemStack(Items.bread, 3 + random.nextInt(2));
            case 4: return new ItemStack(Items.compass);
            case 5: return new ItemStack(Items.gold_nugget, 2 + random.nextInt(6));
            case 6: return new ItemStack(Items.apple, 1 + random.nextInt(3));
            case 7: return new ItemStack(Items.string, 2 + random.nextInt(2));
            case 8: return new ItemStack(Items.bowl, 1 + random.nextInt(4));
            case 9: return new ItemStack(Items.cookie, 1 + random.nextInt(3));
            case 10: return new ItemStack(Items.coal, 1 + random.nextInt(2));
            default: return new ItemStack(Items.stick, 2 + random.nextInt(4));
        }
    }

    private void generateSupports(World world, int i, int j, int k, Block stairBlock, int stairMetadata, int woodType) {
        world.setBlock(i, j, k, stairBlock, stairMetadata, 2);
        Block block = world.getBlock(i, j - 1, k);
        for (int j1 = 1; block == null || !block.isOpaqueCube(); j1++) {
            Block supportBlock = Blocks.fence;
            int supportMetadata = 0;
            Block blockBelow = world.getBlock(i, j - j1, k);
            if (blockBelow != null && (blockBelow.getMaterial() == Material.water || blockBelow.getMaterial() == Material.lava)) {
                supportBlock = Blocks.planks;
                supportMetadata = woodType;
            }
            world.setBlock(i, j - j1, k, supportBlock, supportMetadata, 2);
            block = world.getBlock(i, j - j1 - 1, k);
        }
    }

    private boolean isBoundingBoxClear(World world, int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                for (int k = minZ; k <= maxZ; k++) {
                    if (i == minX || i == maxX || j == minY || j == maxY || k == minZ || k == maxZ) {
                        Block block = world.getBlock(i, j, k);
                        if (block == null || block instanceof BlockFlower || block == Blocks.water || block == Blocks.snow) {
                            continue;
                        } else {
                            FMLLog.fine("Bounding box check failed at (%d, %d, %d): block=%s", i, j, k, block.getUnlocalizedName());
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean isSolidGround(World world, int i, int j, int k) {
        Block block = world.getBlock(i, j, k);
        Block block1 = world.getBlock(i, j - 1, k);
        Block block2 = world.getBlock(i, j - 2, k);
        if (block != null && block.isOpaqueCube() &&
            block1 != null && block1.isOpaqueCube() &&
            block2 != null && block2.isOpaqueCube()) {
            return true;
        }
        FMLLog.fine("Solid ground check failed at (%d, %d, %d)", i, j, k);
        return false;
    }

    private boolean isBlockReplaceable(World world, int i, int j, int k) {
        Block block = world.getBlock(i, j, k);
        return block == Blocks.air || block.isLeaves(world, i, j, k) || block == Blocks.tallgrass || block == Blocks.deadbush;
    }
}
