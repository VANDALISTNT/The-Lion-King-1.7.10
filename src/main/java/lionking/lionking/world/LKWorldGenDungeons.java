package lionking.world;

import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.world.gen.feature.WorldGenerator;
import java.util.Random;
import cpw.mods.fml.common.FMLLog;
import lionking.mod_LionKing;
import lionking.common.LKLevelData;
import lionking.entity.LKEntities;
import lionking.entity.LKEntityCrocodile;
import lionking.entity.LKEntityHyena;
import lionking.tileentity.LKTileEntityMobSpawner;

public class LKWorldGenDungeons extends WorldGenerator {
    private static final int HEIGHT = 3;
    private static final int MIN_SIZE = 2;
    private static final int MAX_CHESTS = 3;
    private static final int MAX_VINE_LENGTH = 6;

    public LKWorldGenDungeons() {
        super(false);
        FMLLog.fine("Initialized LKWorldGenDungeons");
    }

    public boolean generate(World world, Random random, int x, int y, int z) {
        FMLLog.fine("Attempting to generate dungeon at (%d, %d, %d)", x, y, z);

        if (mod_LionKing.prideBrick == null || mod_LionKing.prideBrickMossy == null || 
            mod_LionKing.pridePillar == null || mod_LionKing.mobSpawner == null) {
            FMLLog.severe("Cannot generate dungeon at (%d, %d, %d): prideBrick, prideBrickMossy, pridePillar, or mobSpawner is null", x, y, z);
            return false;
        }

        int width = MIN_SIZE + random.nextInt(2);
        int depth = MIN_SIZE + random.nextInt(2);
        boolean spawnCrocodiles = random.nextInt(3) == 0;
        if (spawnCrocodiles) {
            width += random.nextInt(2) + 1;
            depth += random.nextInt(2) + 1;
            FMLLog.fine("Crocodile dungeon selected, adjusted size: width=%d, depth=%d", width, depth);
        }

        int airEntrances = 0;
        for (int dx = x - width - 1; dx <= x + width + 1; dx++) {
            for (int dy = y - 1; dy <= y + HEIGHT + 1; dy++) {
                for (int dz = z - depth - 1; dz <= z + depth + 1; dz++) {
                    Material material = world.getBlock(dx, dy, dz).getMaterial();
                    if (dy == y - 1 && !material.isSolid()) {
                        FMLLog.fine("Invalid floor at (%d, %d, %d): non-solid material", dx, dy, dz);
                        return false;
                    }
                    if (dy == y + HEIGHT + 1 && !material.isSolid()) {
                        FMLLog.fine("Invalid ceiling at (%d, %d, %d): non-solid material", dx, dy, dz);
                        return false;
                    }
                    if ((dx == x - width - 1 || dx == x + width + 1 || dz == z - depth - 1 || dz == z + depth + 1) && 
                        dy == y && world.isAirBlock(dx, dy, dz) && world.isAirBlock(dx, dy + 1, dz)) {
                        airEntrances++;
                    }
                }
            }
        }
        if (airEntrances < 1 || airEntrances > 5) {
            FMLLog.fine("Cannot generate dungeon at (%d, %d, %d): invalid air entrances (%d)", x, y, z, airEntrances);
            return false;
        }

        int pillarPos1 = random.nextInt(4);
        int pillarPos2 = random.nextInt(4);
        if (random.nextInt(4) == 0) pillarPos1 = -1;
        if (random.nextInt(4) == 0) pillarPos2 = -1;
        FMLLog.fine("Selected pillar positions: pos1=%d, pos2=%d", pillarPos1, pillarPos2);

        int blocksPlaced = 0;
        for (int dx = x - width - 1; dx <= x + width + 1; dx++) {
            for (int dy = y + HEIGHT; dy >= y - 1; dy--) {
                for (int dz = z - depth - 1; dz <= z + depth + 1; dz++) {
                    boolean isWall = dx == x - width - 1 || dx == x + width + 1 || 
                                     dy == y - 1 || dy == y + HEIGHT + 1 || 
                                     dz == z - depth - 1 || dz == z + depth + 1;

                    if (isWall) {
                        if (!spawnCrocodiles && dy >= 0 && !world.getBlock(dx, dy - 1, dz).getMaterial().isSolid()) {
                            world.setBlockToAir(dx, dy, dz);
                            FMLLog.fine("Cleared non-solid wall block at (%d, %d, %d)", dx, dy, dz);
                            continue;
                        }
                        if (!spawnCrocodiles && !world.getBlock(dx, dy, dz).getMaterial().isSolid()) continue;
                        Block block = dy == y - 1 && random.nextInt(4) != 0 ? mod_LionKing.prideBrickMossy : mod_LionKing.prideBrick;
                        world.setBlock(dx, dy, dz, block, 0, 2);
                        FMLLog.fine("Placed %s at (%d, %d, %d)", block.getUnlocalizedName(), dx, dy, dz);
                        blocksPlaced++;
                    } else {
                        world.setBlockToAir(dx, dy, dz);
                        FMLLog.fine("Cleared block at (%d, %d, %d)", dx, dy, dz);
                        blocksPlaced++;
                    }

                    generatePillar(world, dx, dy, dz, x, y, z, width, depth, pillarPos1, pillarPos2);

                    if (spawnCrocodiles && dx > x - width && dx < x + width && dz > z - depth && dz < z + depth && dy == y - 1) {
                        if (dx != x || dz != z) {
                            world.setBlock(dx, dy, dz, Blocks.water, 0, 2);
                            FMLLog.fine("Placed water at (%d, %d, %d)", dx, dy, dz);
                            blocksPlaced++;
                        }
                        Block block = random.nextInt(4) != 0 ? mod_LionKing.prideBrickMossy : mod_LionKing.prideBrick;
                        world.setBlock(dx, dy - 1, dz, block, 0, 2);
                        FMLLog.fine("Placed %s at (%d, %d, %d)", block.getUnlocalizedName(), dx, dy - 1, dz);
                        blocksPlaced++;
                    }
                }
            }
        }

        int chestCount = spawnCrocodiles ? MAX_CHESTS : 2;
        int chestsPlaced = 0;
        for (int i = 0; i < chestCount; i++) {
            if (generateChest(world, random, x, y, z, width, depth)) {
                chestsPlaced++;
            }
        }
        FMLLog.fine("Placed %d chests in dungeon at (%d, %d, %d)", chestsPlaced, x, y, z);

        world.setBlock(x, y, z, mod_LionKing.mobSpawner, 0, 2);
        FMLLog.fine("Placed mobSpawner at (%d, %d, %d)", x, y, z);
        LKTileEntityMobSpawner spawner = (LKTileEntityMobSpawner) world.getTileEntity(x, y, z);
        if (spawner != null) {
            int mobID = spawnCrocodiles ? LKEntities.getEntityIDFromClass(LKEntityCrocodile.class) 
                                       : LKEntities.getEntityIDFromClass(LKEntityHyena.class);
            spawner.setMobID(mobID);
            FMLLog.fine("Set spawner to %s at (%d, %d, %d)", spawnCrocodiles ? "crocodile" : "hyena", x, y, z);
        } else {
            FMLLog.warning("Failed to set spawner at (%d, %d, %d): tile entity is null", x, y, z);
        }

        if (spawnCrocodiles) {
            generateVines(world, random, x, y, z, width, depth);
        }

        if (blocksPlaced == 0) {
            FMLLog.warning("No blocks placed for dungeon at (%d, %d, %d)", x, y, z);
            return false;
        }

        FMLLog.info("Successfully generated %s dungeon at (%d, %d, %d) with %d blocks, %d chests", 
                    spawnCrocodiles ? "crocodile" : "hyena", x, y, z, blocksPlaced, chestsPlaced);
        return true;
    }

    private void generatePillar(World world, int dx, int dy, int dz, int x, int y, int z, int width, int depth, int pos1, int pos2) {
        int heightOffset = dy - y;
        if (dy < y) return;

        if ((pos1 == 0 || pos2 == 0) && dx == x - width && dz == z - depth) {
            world.setBlock(dx, dy, dz, mod_LionKing.pridePillar, heightOffset == 0 || heightOffset == 3 ? 1 : 2, 2);
            FMLLog.fine("Placed pridePillar at (%d, %d, %d), meta=%d", dx, dy, dz, heightOffset == 0 || heightOffset == 3 ? 1 : 2);
        }
        if ((pos1 == 1 || pos2 == 1) && dx == x + width && dz == z - depth) {
            world.setBlock(dx, dy, dz, mod_LionKing.pridePillar, heightOffset == 0 || heightOffset == 3 ? 1 : 2, 2);
            FMLLog.fine("Placed pridePillar at (%d, %d, %d), meta=%d", dx, dy, dz, heightOffset == 0 || heightOffset == 3 ? 1 : 2);
        }
        if ((pos1 == 2 || pos2 == 2) && dx == x - width && dz == z + depth) {
            world.setBlock(dx, dy, dz, mod_LionKing.pridePillar, heightOffset == 0 || heightOffset == 3 ? 1 : 2, 2);
            FMLLog.fine("Placed pridePillar at (%d, %d, %d), meta=%d", dx, dy, dz, heightOffset == 0 || heightOffset == 3 ? 1 : 2);
        }
        if ((pos1 == 3 || pos2 == 3) && dx == x + width && dz == z + depth) {
            world.setBlock(dx, dy, dz, mod_LionKing.pridePillar, heightOffset == 0 || heightOffset == 3 ? 1 : 2, 2);
            FMLLog.fine("Placed pridePillar at (%d, %d, %d), meta=%d", dx, dy, dz, heightOffset == 0 || heightOffset == 3 ? 1 : 2);
        }
    }

    private boolean generateChest(World world, Random random, int x, int y, int z, int width, int depth) {
        for (int attempt = 0; attempt <= MAX_CHESTS; attempt++) {
            int chestX = x + random.nextInt(width * 2 + 1) - width;
            int chestY = y;
            int chestZ = z + random.nextInt(depth * 2 + 1) - depth;

            if (!world.isAirBlock(chestX, chestY, chestZ) && world.getBlock(chestX, chestY, chestZ) != Blocks.vine) {
                FMLLog.fine("Cannot place chest at (%d, %d, %d): block is not air or vine", chestX, chestY, chestZ);
                continue;
            }

            int adjacentSolid = 0;
            if (world.getBlock(chestX - 1, chestY, chestZ).getMaterial().isSolid()) adjacentSolid++;
            if (world.getBlock(chestX + 1, chestY, chestZ).getMaterial().isSolid()) adjacentSolid++;
            if (world.getBlock(chestX, chestY, chestZ - 1).getMaterial().isSolid()) adjacentSolid++;
            if (world.getBlock(chestX, chestY, chestZ + 1).getMaterial().isSolid()) adjacentSolid++;

            if (adjacentSolid != 1 || !world.getBlock(chestX, chestY - 1, chestZ).isNormalCube()) {
                FMLLog.fine("Cannot place chest at (%d, %d, %d): invalid adjacent blocks (%d solid) or non-solid base", 
                            chestX, chestY, chestZ, adjacentSolid);
                continue;
            }

            world.setBlock(chestX, chestY, chestZ, Blocks.chest, 0, 2);
            FMLLog.fine("Placed chest at (%d, %d, %d)", chestX, chestY, chestZ);
            TileEntityChest chest = (TileEntityChest) world.getTileEntity(chestX, chestY, chestZ);
            if (chest == null) {
                FMLLog.warning("Failed to place chest at (%d, %d, %d): tile entity is null", chestX, chestY, chestZ);
                return false;
            }

            for (int i = 0; i < 8; i++) {
                ItemStack loot = pickCheckLootItem(random);
                if (loot != null) {
                    if (loot.getItem().getItemEnchantability() > 0 && random.nextInt(3) != 0) {
                        EnchantmentHelper.addRandomEnchantment(random, loot, 3);
                        FMLLog.fine("Added enchantment to loot item %s at (%d, %d, %d)", loot.getDisplayName(), chestX, chestY, chestZ);
                    }
                    chest.setInventorySlotContents(random.nextInt(chest.getSizeInventory()), loot);
                    FMLLog.fine("Added loot %s to chest at (%d, %d, %d)", loot.getDisplayName(), chestX, chestY, chestZ);
                }
            }
            if (random.nextInt(4) == 0) {
                ItemStack sapling = new ItemStack(mod_LionKing.passionSapling);
                chest.setInventorySlotContents(random.nextInt(chest.getSizeInventory()), sapling);
                FMLLog.fine("Added passionSapling to chest at (%d, %d, %d)", chestX, chestY, chestZ);
            }
            return true;
        }
        return false;
    }

    private ItemStack pickCheckLootItem(Random random) {
        int roll = random.nextInt(12);
        switch (roll) {
            case 0: 
                ItemStack hyenaBone = new ItemStack(mod_LionKing.hyenaBone, random.nextInt(3) + 2);
                FMLLog.fine("Selected loot: hyenaBone x%d", hyenaBone.stackSize);
                return hyenaBone;
            case 1: 
                ItemStack bug = new ItemStack(mod_LionKing.bug, random.nextInt(4) + 2);
                FMLLog.fine("Selected loot: bug x%d", bug.stackSize);
                return bug;
            case 2: case 3: case 4:
                switch (random.nextInt(7)) {
                    case 0: 
                        ItemStack dartBlue = new ItemStack(mod_LionKing.dartBlue, random.nextInt(5) + 3);
                        FMLLog.fine("Selected loot: dartBlue x%d", dartBlue.stackSize);
                        return dartBlue;
                    case 1: 
                        ItemStack dartYellow = new ItemStack(mod_LionKing.dartYellow, random.nextInt(4) + 3);
                        FMLLog.fine("Selected loot: dartYellow x%d", dartYellow.stackSize);
                        return dartYellow;
                    case 2: 
                        ItemStack dartRed = new ItemStack(mod_LionKing.dartRed, random.nextInt(4) + 3);
                        FMLLog.fine("Selected loot: dartRed x%d", dartRed.stackSize);
                        return dartRed;
                    case 3: 
                        ItemStack featherBlue = new ItemStack(mod_LionKing.featherBlue, random.nextInt(4) + 3);
                        FMLLog.fine("Selected loot: featherBlue x%d", featherBlue.stackSize);
                        return featherBlue;
                    case 4: 
                        ItemStack featherYellow = new ItemStack(mod_LionKing.featherYellow, random.nextInt(3) + 3);
                        FMLLog.fine("Selected loot: featherYellow x%d", featherYellow.stackSize);
                        return featherYellow;
                    case 5: 
                        ItemStack featherRed = new ItemStack(mod_LionKing.featherRed, random.nextInt(3) + 3);
                        FMLLog.fine("Selected loot: featherRed x%d", featherRed.stackSize);
                        return featherRed;
                    case 6: 
                        ItemStack silverDartShooter = new ItemStack(mod_LionKing.silverDartShooter);
                        FMLLog.fine("Selected loot: silverDartShooter");
                        return silverDartShooter;
                }
            case 5: 
                ItemStack chocolateMufasa = new ItemStack(mod_LionKing.chocolateMufasa, random.nextInt(3) + 1);
                FMLLog.fine("Selected loot: chocolateMufasa x%d", chocolateMufasa.stackSize);
                return chocolateMufasa;
            case 6: 
                ItemStack silver = new ItemStack(mod_LionKing.silver, random.nextInt(3) + 2);
                FMLLog.fine("Selected loot: silver x%d", silver.stackSize);
                return silver;
            case 7: 
                ItemStack dartQuiver = new ItemStack(mod_LionKing.dartQuiver, 1, random.nextInt(50));
                FMLLog.fine("Selected loot: dartQuiver, damage=%d", dartQuiver.getItemDamage());
                return dartQuiver;
            case 8: 
                ItemStack mango = new ItemStack(mod_LionKing.mango, random.nextInt(3) + 1);
                FMLLog.fine("Selected loot: mango x%d", mango.stackSize);
                return mango;
            case 9:
                switch (random.nextInt(3)) {
                    case 0: 
                        ItemStack peacockGem = new ItemStack(mod_LionKing.peacockGem, random.nextInt(2) + 1);
                        FMLLog.fine("Selected loot: peacockGem x%d", peacockGem.stackSize);
                        return peacockGem;
                    case 1: 
                        ItemStack compass = new ItemStack(Items.compass);
                        FMLLog.fine("Selected loot: compass");
                        return compass;
                    case 2: 
                        ItemStack jar = new ItemStack(mod_LionKing.jar);
                        FMLLog.fine("Selected loot: jar");
                        return jar;
                }
            case 10: case 11:
                switch (random.nextInt(7)) {
                    case 0: 
                        ItemStack shovelSilver = new ItemStack(mod_LionKing.shovelSilver);
                        FMLLog.fine("Selected loot: shovelSilver");
                        return shovelSilver;
                    case 1: 
                        ItemStack pickaxeSilver = new ItemStack(mod_LionKing.pickaxeSilver);
                        FMLLog.fine("Selected loot: pickaxeSilver");
                        return pickaxeSilver;
                    case 2: 
                        ItemStack axeSilver = new ItemStack(mod_LionKing.axeSilver);
                        FMLLog.fine("Selected loot: axeSilver");
                        return axeSilver;
                    case 3: 
                        ItemStack swordSilver = new ItemStack(mod_LionKing.swordSilver);
                        FMLLog.fine("Selected loot: swordSilver");
                        return swordSilver;
                    case 4: 
                        ItemStack helmetSilver = new ItemStack(mod_LionKing.helmetSilver);
                        FMLLog.fine("Selected loot: helmetSilver");
                        return helmetSilver;
                    case 5: 
                        ItemStack bootsSilver = new ItemStack(mod_LionKing.bootsSilver);
                        FMLLog.fine("Selected loot: bootsSilver");
                        return bootsSilver;
                    case 6: 
                        ItemStack note = new ItemStack(mod_LionKing.note, 1 + random.nextInt(3), 6);
                        FMLLog.fine("Selected loot: note x%d, damage=6", note.stackSize);
                        return note;
                }
            default: 
                FMLLog.fine("Selected no loot");
                return null;
        }
    }

    private void generateVines(World world, Random random, int x, int y, int z, int width, int depth) {
        int vinesPlaced = 0;
        for (int dx = x - width - 1; dx <= x + width + 1; dx++) {
            for (int dy = y + HEIGHT; dy >= y - 1; dy--) {
                for (int dz = z - depth - 1; dz <= z + depth + 1; dz++) {
                    Block block = world.getBlock(dx, dy, dz);
                    if (block == mod_LionKing.prideBrick || block == mod_LionKing.prideBrickMossy) {
                        if (tryPlaceVine(world, random, dx - 1, dy, dz, 8)) vinesPlaced++;
                        if (tryPlaceVine(world, random, dx + 1, dy, dz, 2)) vinesPlaced++;
                        if (tryPlaceVine(world, random, dx, dy, dz - 1, 2)) vinesPlaced++;
                        if (tryPlaceVine(world, random, dx, dy, dz + 1, 4)) vinesPlaced++;
                    }
                }
            }
        }
        FMLLog.fine("Placed %d vines in dungeon at (%d, %d, %d)", vinesPlaced, x, y, z);
    }

    private boolean tryPlaceVine(World world, Random random, int x, int y, int z, int metadata) {
        if (random.nextInt(4) != 0 || !world.isAirBlock(x, y, z)) return false;
        world.setBlock(x, y, z, Blocks.vine, metadata, 2);
        FMLLog.fine("Placed vine at (%d, %d, %d), meta=%d", x, y, z, metadata);
        int length = random.nextInt(2) + 4;
        for (int dy = 1; dy < length && world.isAirBlock(x, y - dy, z); dy++) {
            world.setBlock(x, y - dy, z, Blocks.vine, metadata, 2);
            FMLLog.fine("Placed vine extension at (%d, %d, %d), meta=%d", x, y - dy, z, metadata);
        }
        return true;
    }

    private boolean isBlockReplaceable(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        return block == Blocks.air || block.isLeaves(world, x, y, z) || block == Blocks.tallgrass || block == Blocks.deadbush;
    }
}