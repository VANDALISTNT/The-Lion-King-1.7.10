package lionking.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;
import java.util.HashSet;

import lionking.mod_LionKing;

public class LKCompatibility {
    private static Item[] toolItems; 
    public static boolean isTimberModInstalled; 

    public static void timber(World world, int x, int y, int z, int blockID) {
        try {
            Class<?> timberTreeClass = Class.forName("BlockTimberTree");
            Class<?> timberModClass = Class.forName("mod_Timber");

            Field isAxeField = timberTreeClass.getDeclaredField("isAxe");
            Field keyField = timberModClass.getDeclaredField("key");
            isAxeField.setAccessible(true);
            keyField.setAccessible(true);

            boolean isAxe = (Boolean) isAxeField.get(null);
            int key = (Integer) keyField.get(null);

            if (isAxe && !Keyboard.isKeyDown(key)) {
                int radius = 1;
                Block targetBlock = Block.getBlockById(blockID); 
                for (int dx = -radius; dx <= radius; dx++) {
                    for (int dz = -radius; dz <= radius; dz++) {
                        for (int dy = 0; dy <= radius; dy++) {
                            Block currentBlock = world.getBlock(x + dx, y + dy, z + dz);
                            if (currentBlock != targetBlock) {
                                continue;
                            }

                            if (currentBlock != null && !world.isRemote) {
                                world.setBlockToAir(x + dx, y + dy, z + dz);
                            }
                        }
                    }
                }
            }
        } catch (Throwable ignored) {
        }
    }

    public static void init() {
        toolItems = new Item[]{
                mod_LionKing.rafikiStick, mod_LionKing.dartShooter, mod_LionKing.zebraBoots, mod_LionKing.shovel,
                mod_LionKing.pickaxe, mod_LionKing.axe, mod_LionKing.sword, mod_LionKing.hoe,
                mod_LionKing.silverDartShooter, mod_LionKing.shovelSilver, mod_LionKing.pickaxeSilver,
                mod_LionKing.axeSilver, mod_LionKing.swordSilver, mod_LionKing.hoeSilver,
                mod_LionKing.tunnahDiggah, mod_LionKing.helmetSilver, mod_LionKing.bodySilver,
                mod_LionKing.legsSilver, mod_LionKing.bootsSilver, mod_LionKing.gemsbokSpear,
                mod_LionKing.helmetGemsbok, mod_LionKing.bodyGemsbok, mod_LionKing.legsGemsbok,
                mod_LionKing.bootsGemsbok, mod_LionKing.shovelPeacock, mod_LionKing.pickaxePeacock,
                mod_LionKing.axePeacock, mod_LionKing.swordPeacock, mod_LionKing.hoePeacock,
                mod_LionKing.helmetPeacock, mod_LionKing.bodyPeacock, mod_LionKing.legsPeacock,
                mod_LionKing.bootsPeacock, mod_LionKing.wings, mod_LionKing.shovelKivulite,
                mod_LionKing.pickaxeKivulite, mod_LionKing.axeKivulite, mod_LionKing.swordKivulite,
                mod_LionKing.poisonedSpear, mod_LionKing.shovelCorrupt, mod_LionKing.pickaxeCorrupt,
                mod_LionKing.axeCorrupt, mod_LionKing.swordCorrupt, mod_LionKing.staff
        };

        setupTMICompatibility();
        setupTimberCompatibility();
    }

    private static void setupTMICompatibility() {
        try {
            Class<?> tmiConfigClass = Class.forName("TMIConfig");
            Field toolIdsField = tmiConfigClass.getDeclaredField("toolIds");
            toolIdsField.setAccessible(true);

            @SuppressWarnings("unchecked")
            HashSet<Integer> toolIds = (HashSet<Integer>) toolIdsField.get(null);
            for (Item tool : toolItems) {
                toolIds.add(Item.getIdFromItem(tool));
            }

            toolIdsField.set(null, toolIds);
        } catch (Throwable ignored) {
        }
    }

    private static void setupTimberCompatibility() {
        try {
            Class<?> timberModClass = Class.forName("mod_Timber");
            Field axesField = timberModClass.getDeclaredField("axes");
            axesField.setAccessible(true);

            String axes = (String) axesField.get(null);
            axes += ", " + Item.getIdFromItem(mod_LionKing.axe) + ", " + Item.getIdFromItem(mod_LionKing.axeSilver) + ", " +
                    Item.getIdFromItem(mod_LionKing.axePeacock) + ", " + Item.getIdFromItem(mod_LionKing.axeKivulite) + ", " +
                    Item.getIdFromItem(mod_LionKing.axeCorrupt);

            axesField.set(null, axes);
            isTimberModInstalled = true;
        } catch (Throwable ignored) {
            isTimberModInstalled = false;
        }
    }
}